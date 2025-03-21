package com.codeisevenlycooked.evenly.service;
import com.codeisevenlycooked.evenly.entity.*;
import com.codeisevenlycooked.evenly.repository.OrderRepository;
import com.codeisevenlycooked.evenly.repository.PaymentRepository;
import com.codeisevenlycooked.evenly.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public boolean processPayment(Long orderId) {

        boolean isPaymentSuccess = Math.random() < 0.95;

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        List<Payment> existingPayments = paymentRepository.findByOrderId(orderId);

        if (!existingPayments.isEmpty() && "SUCCESS".equals(existingPayments.get(0).getStatus())) {
            throw new RuntimeException("이 주문은 이미 결제 완료되었습니다.");
        }

        BigDecimal amount = order.getTotalPrice();
        String paymentMethod = order.getPaymentMethod();

       Payment payment = new Payment(
               amount,
               paymentMethod,
               isPaymentSuccess ? "SUCCESS" : "FAILED",
               LocalDateTime.now(),
               order
       );

       paymentRepository.save(payment);

       if (isPaymentSuccess) {
           handleSuccessfulPayment(order);
       } else {
           handlePaymentFailure(payment);
       }

       return isPaymentSuccess;
    }

    private void handleSuccessfulPayment(Order order) {

        order.changeStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();

            product.changeStock(orderItem.getQuantity());
            productRepository.save(product);
        }
    }

    private void handlePaymentFailure(Payment payment) {
    }

}
