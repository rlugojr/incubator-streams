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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.typesafe.config.Config;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.streams.config.StreamsConfigurator;
import org.apache.streams.core.StreamsDatum;
import org.apache.streams.core.StreamsPersistWriter;
import org.apache.streams.util.GuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class KafkaPersistWriter implements StreamsPersistWriter, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPersistWriter.class);

    protected volatile Queue<StreamsDatum> persistQueue;

    private ObjectMapper mapper;

    private KafkaConfiguration config;

    private Producer<String, String> producer;

    public KafkaPersistWriter() {
       this(KafkaConfigurator.detectConfiguration(StreamsConfigurator.config.getConfig("kafka")));
    }

    public KafkaPersistWriter(KafkaConfiguration config) {
        this.config = config;
    }
    
    public Queue<StreamsDatum> getPersistQueue() {
        return persistQueue;
    }

    @Override
    public void write(StreamsDatum entry) {

        Preconditions.checkArgument(entry.getDocument() instanceof String);
        
        String key = entry.getId() != null ? entry.getId() : GuidUtils.generateGuid("kafkawriter");
        
        Preconditions.checkArgument(Strings.isNullOrEmpty(key) == false);
        Preconditions.checkArgument(entry.getDocument() instanceof String);
        Preconditions.checkArgument(Strings.isNullOrEmpty((String)entry.getDocument()) == false);

        KeyedMessage<String, String> data = new KeyedMessage<>(config.getTopic(), key, (String)entry.getDocument());

        producer.send(data);
    }

    @Override
    public void prepare(Object configurationObject) {

        mapper = new ObjectMapper();

        Properties props = new Properties();

        props.put("metadata.broker.list", config.getBrokerlist());
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "org.apache.streams.kafka.StreamsPartitioner");
        props.put("request.required.acks", "1");
        props.put("auto.create.topics.enable", "true");

        ProducerConfig config = new ProducerConfig(props);

        producer = new Producer(config);

    @Override
    public void prepare(Object configurationObject) {
        this.persistQueue  = new ConcurrentLinkedQueue<StreamsDatum>();
    }

    @Override
    public void cleanUp() {
        producer.close();
    }
}
