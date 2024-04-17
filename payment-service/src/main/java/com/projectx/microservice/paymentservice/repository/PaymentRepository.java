package com.projectx.microservice.paymentservice.repository;

import com.projectx.microservice.paymentservice.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentDetails,Long> {
    PaymentDetails findPaymentDetailsById(Long paymentId);
}
