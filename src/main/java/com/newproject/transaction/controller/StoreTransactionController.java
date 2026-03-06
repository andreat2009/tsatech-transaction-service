package com.newproject.transaction.controller;

import com.newproject.transaction.dto.StoreTransactionRequest;
import com.newproject.transaction.dto.StoreTransactionResponse;
import com.newproject.transaction.service.StoreTransactionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/{customerId}/transactions")
public class StoreTransactionController {
    private final StoreTransactionService service;

    public StoreTransactionController(StoreTransactionService service) {
        this.service = service;
    }

    @GetMapping
    public List<StoreTransactionResponse> list(@PathVariable Long customerId) {
        return service.list(customerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreTransactionResponse create(
        @PathVariable Long customerId,
        @Valid @RequestBody StoreTransactionRequest request
    ) {
        return service.create(customerId, request);
    }
}
