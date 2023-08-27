package xyz.sandersonsa.quarkuscamelkafka.routes;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jboss.logging.Logger;

@ApplicationScoped
public class KafkaRoute extends RouteBuilder {

    private static final Logger LOG = Logger.getLogger(KafkaRoute.class);

    @Override
    public void configure() throws Exception {


        // from("kafka:" + TOPIC + "?brokers=localhost:{{kafkaPort}}" +
        //      "&groupId=A&sslKeystoreLocation=/path/to/keystore.jks&sslKeystorePassword=changeit&sslKeyPassword=changeit&securityProtocol=SSL")
        // .to("mock:result");


        // produces messages to kafka
        from("timer:foo?period={{timer.period}}&delay={{timer.delay}}")
                .routeId("FromTimer2Kafka")
                .setBody().simple("Message #${exchangeProperty.CamelTimerCounter}")
                .to("kafka:{{kafka.topic.name}}")
                .log("Message correctly sent to the topic! : \"${body}\" ");

        // kafka consumer
        // from("kafka:{{kafka.topic.name}}")
        //         .routeId("FromKafka2Seda")
        //         .log("Received : \"${body}\"")
        //         .to("seda:kafka-messages");
    

        // from("timer://foo?period={{period}}").setBody(constant("post message to kafka cluster topic"))
        //         .to("kafka:{{kafka.topic.name}}?brokers={{kafka.external.bootstrap.url}}"
        //                 + "&keySerializerClass={{kafka.key.serializer}}&serializerClass={{kafka.value.serializer}}"
        //                 + "&securityProtocol=SSL&sslTruststoreLocation={{truststore}}"
        //                 + "&sslTruststorePassword={{truststore.password}}")
        //         .process(new Processor() {
        //             @Override
        //             public void process(Exchange exchange) throws Exception {
        //                 List<RecordMetadata> recordMetaData1 = (List<RecordMetadata>) exchange.getIn().getHeader(KafkaConstants.KAFKA_RECORDMETA);
        //                 for (RecordMetadata rd : recordMetaData1) {
        //                     LOG.info("producer partition is:" + rd.partition());
        //                     LOG.info("producer partition message is:" + rd.toString());
        //                 }
        //             }
        //         });
    }
}
