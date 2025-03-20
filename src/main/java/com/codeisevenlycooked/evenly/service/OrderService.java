package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.dto.OrderItemDto;
import com.codeisevenlycooked.evenly.dto.OrderItemResponseDto;
import com.codeisevenlycooked.evenly.dto.OrderRequestDto;
import com.codeisevenlycooked.evenly.dto.OrderResponseDto;
import com.codeisevenlycooked.evenly.entity.Order;
import com.codeisevenlycooked.evenly.entity.OrderItem;
import com.codeisevenlycooked.evenly.entity.Product;
import com.codeisevenlycooked.evenly.entity.User;
import com.codeisevenlycooked.evenly.repository.OrderItemRepository;
import com.codeisevenlycooked.evenly.repository.OrderRepository;
import com.codeisevenlycooked.evenly.repository.ProductRepository;
import com.codeisevenlycooked.evenly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponseDto createOrder(String userId, OrderRequestDto requestDto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Order order = new Order(
                user,
                BigDecimal.ZERO,
                requestDto.getReceiverName(),
                requestDto.getAddress(),
                requestDto.getMobile(),
                requestDto.getDeliveryMessage(),
                requestDto.getPaymentMethod()
        );

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItemResponseDto> orderItemResponses = new ArrayList<>();

        for (OrderItemDto itemDto : requestDto.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 없습니다."));

            BigDecimal itemTotalPrice = BigDecimal.valueOf(product.getPrice())
                    .multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            totalPrice = totalPrice.add(itemTotalPrice);

            order.addOrderItem(new OrderItem(product, itemDto.getQuantity(), BigDecimal.valueOf(product.getPrice())));
            orderItemResponses.add(new OrderItemResponseDto(
                    product.getId(),
                    product.getName(),
                    itemDto.getQuantity(),
                    BigDecimal.valueOf(product.getPrice()))
            );
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .receiverName(order.getReceiverName())
                .address(order.getAddress())
                .mobile(order.getMobile())
                .deliveryMessage(order.getDeliveryMessage())
                .paymentMethod(order.getPaymentMethod())
                .createdAt(order.getCreatedAt())
                .orderItems(orderItemResponses)
                .build();
    }
}
