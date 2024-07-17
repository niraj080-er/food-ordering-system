package com.food.ordering.system.payment.service.domain;

import com.food.odering.system.payment.service.domain.PaymentDomainService;
import com.food.odering.system.payment.service.domain.entity.CreditEntity;
import com.food.odering.system.payment.service.domain.entity.CreditHistory;
import com.food.odering.system.payment.service.domain.entity.Payment;
import com.food.odering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.odering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.odering.system.payment.service.domain.event.PaymentEvent;
import com.food.odering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class PaymentRequestHelper {
    
    private  final PaymentDomainService paymentDomainService;
    private  final PaymentDataMapper paymentDataMapper;
    private  final PaymentRepository paymentRepository;
    private  final CreditEntryRepository creditEntryRepository;
    private  final CreditHistoryRepository creditHistoryRepository;
    private final DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher;
    private final DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher;
    private final DomainEventPublisher<PaymentFailedEvent> paymentFailedventDomainEventPublisher;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository,
                                CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository,
                                DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher, 
                                DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher, DomainEventPublisher<PaymentFailedEvent> paymentFailedventDomainEventPublisher) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.paymentCompletedEventDomainEventPublisher = paymentCompletedEventDomainEventPublisher;
        this.paymentCancelledEventDomainEventPublisher = paymentCancelledEventDomainEventPublisher;
        this.paymentFailedventDomainEventPublisher = paymentFailedventDomainEventPublisher;
    }

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest){
    log.info("Payment Receive for CustomerId : {} ", paymentRequest.getCustomerId());
        Payment payment=paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        CreditEntity creditEntity=getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistory=getCreditHistory(payment.getCustomerId());
        List<String> failureMessage=new ArrayList<>();
        PaymentEvent paymentEvent=
                paymentDomainService.validateInitiatePayment(payment,creditEntity,creditHistory,failureMessage, paymentCompletedEventDomainEventPublisher,paymentFailedventDomainEventPublisher);
        dbPersistObject(payment,failureMessage,creditEntity,creditHistory);
        return paymentEvent;
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistories=creditHistoryRepository.findCustomerId(customerId);
        if(creditHistories==null){
            log.error("could not find the credit history for the customer {} ",customerId.getValue());
            throw new PaymentApplicationServiceException("could not find the credit history for the customer "+customerId.getValue());
        }
        return creditHistories.get();
    }

    private CreditEntity getCreditEntry(CustomerId customerId) {
        Optional<CreditEntity> creditEntity=creditEntryRepository.findByCustomerId(customerId);
        if(creditEntity.isEmpty()){
            log.error("could not find the customer : {} " , customerId.getValue());
            throw new PaymentApplicationServiceException("could not find the customer : " + customerId.getValue());
        }
        return creditEntity.get();
    }

    @Transactional
    public PaymentEvent persistCancelledPayment(PaymentRequest paymentRequest){
        log.info("Payment rollback for CustomerId : {} ", paymentRequest.getCustomerId());
        Optional<Payment> paymentResponse=paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if(paymentResponse.isEmpty()){
            log.error("Payment with Order id: {} could not found ", paymentRequest.getCustomerId());
            throw new PaymentApplicationServiceException("Payment with order id could not found " + paymentRequest.getCustomerId());
        }
        Payment payment=paymentResponse.get();
        CreditEntity creditEntry=getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistory=getCreditHistory(payment.getCustomerId());
        List<String> failureMessage=new ArrayList<>();
        PaymentEvent paymentEvent=paymentDomainService.validateAndCancelPayment(payment,creditEntry,creditHistory,failureMessage,paymentCancelledEventDomainEventPublisher,paymentFailedventDomainEventPublisher);
        dbPersistObject(payment, failureMessage, creditEntry, creditHistory);
        return paymentEvent;

    }
    private void dbPersistObject(Payment payment,
                                        List<String> failureMessage,
                                        CreditEntity creditEntry,
                                        List<CreditHistory> creditHistory) {
        paymentRepository.save(payment);
        if(failureMessage.isEmpty()){
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistory.get(creditHistory.size()-1));
        }
    }

}
