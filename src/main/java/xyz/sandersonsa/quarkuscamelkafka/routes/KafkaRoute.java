package xyz.sandersonsa.quarkuscamelkafka.routes;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.jboss.logging.Logger;

@ApplicationScoped
public class KafkaRoute extends RouteBuilder {

    private static final Logger LOG = Logger.getLogger(KafkaRoute.class);

    @Override
    public void configure() throws Exception {


        // produces messages to kafka
        from("timer:foo?period={{timer.period}}&delay={{timer.delay}}")
                .routeId("FromTimer2Kafka")
                .setBody().simple("Message #${exchangeProperty.CamelTimerCounter}")
                .setHeader(KafkaConstants.HEADERS, constant("outside cluster openshift"))
                .setHeader(KafkaConstants.KEY, constant("key msg openshift"))
                .to("kafka:{{kafka.topic.name}}")
                .log("Message correctly sent to the topic! : \"${body}\" ");

        // kafka consumer
        // from("kafka:{{kafka.topic.name}}")
        //         .routeId("FromKafka2Seda")
        //         .log("Received : \"${body}\"")
        //         .to("seda:kafka-messages");
    }
}
