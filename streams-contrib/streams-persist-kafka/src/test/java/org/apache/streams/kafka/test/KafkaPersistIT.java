package org.apache.streams.kafka.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import javassist.bytecode.stackmap.TypeData;
import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.apache.streams.console.ConsolePersistReader;
import org.apache.streams.console.ConsolePersistWriter;
import org.apache.streams.core.StreamBuilder;
import org.apache.streams.core.StreamsDatum;
import org.apache.streams.core.StreamsResultSet;
import org.apache.streams.kafka.KafkaConfiguration;
import org.apache.streams.kafka.KafkaPersistReader;
import org.apache.streams.kafka.KafkaPersistWriter;
import org.apache.streams.local.builders.LocalStreamBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;

import static org.mockito.Mockito.mock;

/**
 * Created by sblackmon on 10/20/14.
 */
public class KafkaPersistIT {

    private TestKafkaCluster testKafkaCluster;
    private KafkaConfiguration testConfiguration;

    private String testTopic = "testTopic";

    ConsolePersistReader reader = Mockito.mock(ConsolePersistReader.class);
    ConsolePersistWriter writer = Mockito.mock(ConsolePersistWriter.class);

    StreamsDatum testDatum = new StreamsDatum("{\"dummy\":\"true\"}");

    @Before
    public void prepareTest() {

        try {
            testKafkaCluster = new TestKafkaCluster();
        } catch (Throwable e ) {
            e.printStackTrace();
        }

        testConfiguration = new KafkaConfiguration();
        testConfiguration.setBrokerlist(testKafkaCluster.getKafkaBrokerString());
        testConfiguration.setZkconnect(testKafkaCluster.getZkConnectString());
        testConfiguration.setTopic(testTopic);

        ZkClient zkClient = new ZkClient(testKafkaCluster.getZkConnectString(), 1000, 1000, ZKStringSerializer$.MODULE$);

        AdminUtils.createTopic(zkClient, testTopic, 1, 1, new Properties());

        PowerMockito.when(reader.readCurrent())
                .thenReturn(
                        new StreamsResultSet(Queues.newConcurrentLinkedQueue(
                                Lists.newArrayList(testDatum)))
                );
    }

    @Test
    public void testPersistStream() {

        assert(testConfiguration != null);
        assert(testKafkaCluster != null);

        Map<String, Object> streamConfig = Maps.newHashMap();
        streamConfig.put(LocalStreamBuilder.TIMEOUT_KEY, 1000);

        StreamBuilder builder = new LocalStreamBuilder(1, streamConfig);

        KafkaPersistWriter kafkaWriter = new KafkaPersistWriter(testConfiguration);
        KafkaPersistReader kafkaReader = new KafkaPersistReader(testConfiguration);

        builder.newReadCurrentStream("stdin", reader);
        builder.addStreamsPersistWriter("writer", kafkaWriter, 1, "stdin");
        builder.newPerpetualStream("reader", kafkaReader);
        builder.addStreamsPersistWriter("stdout", writer, 1, "reader");

        builder.start();

        builder.stop();

        Mockito.verify(writer).write(testDatum);

    }
}
