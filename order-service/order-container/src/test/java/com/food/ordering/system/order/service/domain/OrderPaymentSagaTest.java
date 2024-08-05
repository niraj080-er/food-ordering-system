package com.food.ordering.system.order.service.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.saga.SagaStatus;
import static com.food.ordering.system.saga.order.SagaConstant.ORDER_SAGA_NAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = OrderServiceApplication.class)
@Sql(value = { "classpath:sql/OrderPaymentSagaTestSetUp.sql" })
@Sql(value = { "classpath:sql/OrderPaymentSagaTestCleanUp.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderPaymentSagaTest {

    @Autowired
    private OrderPaymnetSaga orderPaymnetSaga;
    @Autowired
    private PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    private final UUID SAGA_ID = UUID.fromString("f1b9b3b4-1b3b-4b3b-8b3b-3b3b3b3b3b3b");
    private final UUID ORDER_ID = UUID.fromString("f1b9b3b4-1b3b-4b3b-8b3b-3b3b3b3b3b3b");
    private final UUID PAYMENT_ID = UUID.fromString("f1b9b3b4-1b3b-4b3b-8b3b-3b3b3b3b3b3b");
    private final UUID CUSTOMER_ID = UUID.fromString("f1b9b3b4-1b3b-4b3b-8b3b-3b3b3b3b3b3b");
    private final BigDecimal PRICE = new BigDecimal(100);

    @Test
    void testDoublePayment() {
        orderPaymnetSaga.process(getPaymentResponse());
        orderPaymnetSaga.process(getPaymentResponse());

    }

    @Test
    void testDoublePaymentWithLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        Thread thread1 = new Thread(() -> {
            try {
                orderPaymnetSaga.process(getPaymentResponse());
            } catch (OptimisticLockingFailureException e) {
                log.error("OptimisticLockingFailureException occured for thread1 ");
            } finally {
                latch.countDown();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                orderPaymnetSaga.process(getPaymentResponse());
            } catch (OptimisticLockingFailureException e) {
                log.error("OptimisticLockingFailureException occured for thread1 ");
            } finally {
                latch.countDown();
            }
        });
        thread1.start();
        thread2.start();
        latch.wait();
        assertPaymentOutboxMessage();

    }

    @Test
    void testDoublePaymentWithThread() throws InterruptedException {
        Thread thread1 = new Thread(() -> orderPaymnetSaga.process(getPaymentResponse()));
        Thread thread2 = new Thread(() -> orderPaymnetSaga.process(getPaymentResponse()));
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertPaymentOutboxMessage();

    }

    @Test
    private void assertPaymentOutboxMessage() {
        Optional<PaymentOutboxEntity> paymentOutboxEntity = paymentOutboxJpaRepository
                .findByTypeAndSagaIdAndSagaStatusIn(ORDER_SAGA_NAME, SAGA_ID, List.of(SagaStatus.PROCESSING));
        assertTrue(paymentOutboxEntity.isPresent());
    }

    private PaymentResponse getPaymentResponse() {
        return PaymentResponse.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(SAGA_ID.toString())
                .paymentStatus(com.food.ordering.system.domain.valueobject.PaymentStatus.COMPLETED)
                .paymentId(PAYMENT_ID.toString())
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(PRICE)
                .createdAt(Instant.now())
                .failureMessages(new ArrayList<String>())
                .build();
    }

}
