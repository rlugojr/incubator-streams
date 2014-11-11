/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.streams.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Queues;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;
import org.apache.streams.config.StreamsConfigurator;
import org.apache.streams.core.StreamsDatum;
import org.apache.streams.core.StreamsPersistReader;
import org.apache.streams.core.StreamsResultSet;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KafkaPersistReader implements StreamsPersistReader, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPersistReader.class);

    protected volatile Queue<StreamsDatum> persistQueue;

    private ObjectMapper mapper = new ObjectMapper();

    private KafkaConfiguration config;

    private ConsumerConfig consumerConfig;
    private ConsumerConnector consumerConnector;

    public KafkaStream<String, String> stream;

    private boolean isStarted = false;
    private boolean isStopped = false;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public KafkaPersistReader() {
        this(KafkaConfigurator.detectConfiguration(StreamsConfigurator.config.getConfig("kafka")));
    }

    public KafkaPersistReader(KafkaConfiguration config) {
        this.config = config;
    }

    @Override
    public StreamsResultSet readAll() {
        return readCurrent();
    }

    @Override
    public void startStream() {
        isStarted = true;
    }

    @Override
    public StreamsResultSet readCurrent() {

        ConsumerIterator it = stream.iterator();
        while (it.hasNext()) {
            MessageAndMetadata item = it.next();
            write(new StreamsDatum((String)item.message(), (String)item.key()));
        }

        StreamsResultSet current;
        current = new StreamsResultSet(Queues.newConcurrentLinkedQueue(persistQueue));
        persistQueue.clear();

        return current;
    }

    private void write( StreamsDatum entry ) {
        persistQueue.offer(entry);
    }

    @Override
    public StreamsResultSet readNew(BigInteger bigInteger) {
        return null;
    }

    @Override
    public StreamsResultSet readRange(DateTime dateTime, DateTime dateTime2) {
        return null;
    }

    @Override
    public boolean isRunning() {
        return isStarted && !isStopped;
    }

    @Override
    public void prepare(Object configurationObject) {

        Properties props = new Properties();

        props.put("zookeeper.connect", config.getZkconnect());
        props.put("group.id", "streams");
        props.put("zookeeper.session.timeout.ms", "1000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "500");
        props.put("auto.offset.reset", "smallest");

        VerifiableProperties vprops = new VerifiableProperties(props);

        consumerConfig = new ConsumerConfig(props);

        consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

        Map topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(config.getTopic(), new Integer(1));
        Map<String, List<KafkaStream<String,String>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap, new StringDecoder(vprops), new StringDecoder(vprops));
        stream = consumerMap.get(config.getTopic()).get(0);

        persistQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void cleanUp() {
        consumerConnector.shutdown();
        isStopped = true;
    }
}
