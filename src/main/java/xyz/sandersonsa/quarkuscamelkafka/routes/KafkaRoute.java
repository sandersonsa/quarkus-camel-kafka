package xyz.sandersonsa.quarkuscamelkafka.routes;

import java.io.File;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.jboss.logging.Logger;

@ApplicationScoped
public class KafkaRoute extends RouteBuilder {

    private static final Logger LOG = Logger.getLogger(KafkaRoute.class);

    @Override
    public void configure() throws Exception {

        // produces messages to kafka
        from("timer:kafka?period={{timer.period}}&delay={{timer.delay}}").routeId("FromTimer2Kafka")
                // .setBody().simple("Message #${exchangeProperty.CamelTimerCounter}")
                // <to uri="language:constant:resource:classpath:/myfiles/file.xml"/>
                // .setBody(constant("resource:classpath:files/sample.txt"))
                // .from("file:src/files/sample.txt?noop=true")
                .process(new Processor() {
                    public void process(Exchange msg) {
                        File file = new File("src/files/sample.txt");
                        LOG.info("File name: " + file.getName());
                        msg.getIn().setBody(file);
                    }
                })
                .convertBodyTo(File.class)
                // .setBody().simple("resource:classpath:src/files/sample.txt")
                .setHeader(KafkaConstants.HEADERS, constant("MSG HEADER"))
                .setHeader("source", constant("Outside cluste Openshift"))
                .setHeader(KafkaConstants.KEY, constant("MSG KEY"))
                .to("kafka:{{kafka.topic.name}}")
                .log("Message correctly sent to the topic! : \"${body}\" ");

    }
}
