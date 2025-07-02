package concurrent.order.service.infrastructure.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaOrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String ORDER_TOPIC = "order.created";

    public void kafkaSendOrderCreatedEvent(Object orderEvent) {

        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(ORDER_TOPIC, orderEvent);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error(" Kafka 메시지 전송 실패", ex);
            } else {
                log.info(" Kafka 메시지 전송 성공: {}", result.getProducerRecord().value());
            }
        });
    }
}
