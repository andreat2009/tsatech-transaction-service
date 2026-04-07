package com.newproject.transaction.service;

import com.newproject.transaction.domain.StoreTransaction;
import com.newproject.transaction.dto.StoreTransactionRequest;
import com.newproject.transaction.dto.StoreTransactionResponse;
import com.newproject.transaction.events.EventPublisher;
import com.newproject.transaction.repository.StoreTransactionRepository;
import com.newproject.transaction.security.RequestActor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreTransactionService {
    private final StoreTransactionRepository repository;
    private final EventPublisher eventPublisher;
    private final RequestActor requestActor;

    public StoreTransactionService(StoreTransactionRepository repository, EventPublisher eventPublisher, RequestActor requestActor) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.requestActor = requestActor;
    }

    @Transactional(readOnly = true)
    public List<StoreTransactionResponse> list(Long customerId) {
        requestActor.assertCustomerAccessIfAuthenticated(customerId);
        return repository.findByCustomerIdOrderByCreatedAtDesc(customerId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public StoreTransactionResponse create(Long customerId, StoreTransactionRequest request) {
        requestActor.assertCustomerAccessIfAuthenticated(customerId);
        StoreTransaction transaction = new StoreTransaction();
        transaction.setCustomerId(customerId);
        transaction.setOrderId(request.getOrderId());
        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(OffsetDateTime.now());

        StoreTransaction saved = repository.save(transaction);
        StoreTransactionResponse response = toResponse(saved);
        eventPublisher.publish("STORE_TRANSACTION_CREATED", "store_transaction", saved.getId().toString(), response);
        return response;
    }

    private StoreTransactionResponse toResponse(StoreTransaction transaction) {
        StoreTransactionResponse response = new StoreTransactionResponse();
        response.setId(transaction.getId());
        response.setCustomerId(transaction.getCustomerId());
        response.setOrderId(transaction.getOrderId());
        response.setDescription(transaction.getDescription());
        response.setAmount(transaction.getAmount());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }
}
