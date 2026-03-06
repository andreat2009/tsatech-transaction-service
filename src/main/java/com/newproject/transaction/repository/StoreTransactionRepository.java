package com.newproject.transaction.repository;

import com.newproject.transaction.domain.StoreTransaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreTransactionRepository extends JpaRepository<StoreTransaction, Long> {
    List<StoreTransaction> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
