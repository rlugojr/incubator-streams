package org.apache.streams.kafka.test;

import com.google.common.collect.Lists;
import kafka.admin.AdminUtils;
import kafka.utils.TestUtils;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.apache.streams.core.StreamsDatum;
import org.apache.streams.core.StreamsResultSet;
import org.apache.streams.kafka.KafkaConfiguration;
import org.apache.streams.kafka.KafkaPersistReader;
import org.apache.streams.kafka.KafkaPersistWriter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import scala.collection.JavaConversions;
import scala.collection.Seq;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by sblackmon on 10/20/14.
 */
public class TestKafkaPersist {

    private TestKafkaCluster testKafkaCluster;
    private KafkaConfiguration testConfiguration;

    private String testTopic = "testTopic";

    @Before
    public void prepareTest() {

        try {
            testKafkaCluster = new TestKafkaCluster();
        } catch (Throwable e ) {
            e.printStackTrace();
        }

        String zkConnect = testKafkaCluster.getZkConnectString().replace("127.0.0.1", "localhost");
        String kafkaBroker = testKafkaCluster.getKafkaBrokerString().replace("127.0.0.1", "localhost");

        testConfiguration = new KafkaConfiguration();
        testConfiguration.setBrokerlist(kafkaBroker);
        testConfiguration.setZkconnect(zkConnect);
        testConfiguration.setTopic(testTopic);

        ZkClient zkClient = new ZkClient(testConfiguration.getZkconnect(), 1000, 1000, ZKStringSerializer$.MODULE$);

        AdminUtils.createTopic(zkClient, testTopic, 1, 1, new Properties());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            //Handle exception
        }
    }

    @Test
    public void testPersistWriterString() {

        assert(testConfiguration != null);
        assert(testKafkaCluster != null);

        KafkaPersistWriter testPersistWriter = new KafkaPersistWriter(testConfiguration);
        testPersistWriter.prepare(testConfiguration);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            //Handle exception
        }

        String testJsonString = "{\"dummy\":\"true\"}";

        testPersistWriter.write(new StreamsDatum(testJsonString, "test"));

        testPersistWriter.cleanUp();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            //Handle exception
        }

        KafkaPersistReader testPersistReader = new KafkaPersistReader(testConfiguration);
        try {
            testPersistReader.prepare(testConfiguration);
        } catch( Throwable e ) {
            e.printStackTrace();
            Assert.fail();
        }

<<<<<<< Updated upstream
=======
        testPersistReader.startStream();

>>>>>>> Stashed changes
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            //Handle exception
        }

        StreamsResultSet testResult = testPersistReader.readCurrent();

        testPersistReader.cleanUp();

        assert(testResult.size() == 1);

        StreamsDatum datum = testResult.iterator().next();

        assert(datum.getDocument() instanceof String);

        String datumString = (String) datum.getDocument();

        assert(datumString.contains("dummy") && datumString.contains("true"));

    }

    @After
    public void shutdownTest() {
        try {
            testKafkaCluster.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            testKafkaCluster = null;
        }
        
    }
}
