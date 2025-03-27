package com.codeisevenlycooked.evenly.service;

import com.codeisevenlycooked.evenly.dto.*;
import com.codeisevenlycooked.evenly.entity.*;
import com.codeisevenlycooked.evenly.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public OrderCreationResponseDto createOrder(String userId, List<ItemDto> itemDtoList) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //초기화
        final BigDecimal[] totalPrice = {BigDecimal.ZERO};
        BigDecimal reducedPrice = BigDecimal.ZERO;
        BigDecimal shipmentFee = BigDecimal.valueOf(3000);

        List<OrderItem> orderItems = itemDtoList.stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new RuntimeException("상품이 없습니다."));

                    //재고 0이거나, 요청 수량 > 재고 수량 예외처리
                    if (product.getStock() <= 0 || itemDto.getQuantity() > product.getStock()) {
                        throw new IllegalArgumentException("상품 `" + product.getName() + "`의 재고가 부족합니다.");
                    }

                    BigDecimal itemTotalPrice = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(itemDto.getQuantity()));
                    totalPrice[0] = totalPrice[0].add(itemTotalPrice);

                    OrderItem orderItem = new OrderItem(product, itemDto.getQuantity(), BigDecimal.valueOf(product.getPrice()));
                    orderItem.setOrder(null);
                    return orderItem;
                }).toList();

        // 할인 가격(추후 적용)
//        reducedPrice = totalPrice[0].multiply(BigDecimal.valueOf(0.1)); // 10% 할인

        BigDecimal totalTotalPrice = totalPrice[0].subtract(reducedPrice).add(shipmentFee);

        Order order = Order.builder()
                .user(user)
                .totalPrice(totalPrice[0])
                .reducedPrice(reducedPrice)
                .shipmentFee(shipmentFee)
                .totalTotalPrice(totalTotalPrice)
                .status(OrderStatus.NOT_PAID)
                .orderItems(orderItems)
                .createdAt(LocalDateTime.now())
                .build();

        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        orderRepository.save(order);

        List<OrderItemResponseDto> itemResponseDto = order.getOrderItems().stream()
                .map(item -> new OrderItemResponseDto(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.totalPrice()
                )).toList();

        return new OrderCreationResponseDto(
                order.getId(),
                itemResponseDto,
                order.getTotalPrice(),
                order.getReducedPrice(),
                order.getShipmentFee(),
                order.getTotalTotalPrice()
        );
    }

    @Transactional
    public OrderResponseDto processPayment(String userId, OrderRequestDto requestDto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        if (order.getStatus() != OrderStatus.NOT_PAID) {
            throw new IllegalArgumentException("이미 결제가 진행된 주문입니다.");
        }

        boolean isPaymentSuccessful = Math.random() < 0.95;

        if (isPaymentSuccessful) {
            String orderNumber = generateOrderNumber();

            order = order.toBuilder()
                    .orderNumber(orderNumber)
                    .status(OrderStatus.PENDING)
                    .receiverName(requestDto.getReceiverName())
                    .address(requestDto.getAddress())
                    .mobile(requestDto.getMobile())
                    .deliveryMessage(requestDto.getDeliveryMessage())
                    .paymentMethod(requestDto.getPaymentMethod())
                    .createdAt(LocalDateTime.now())
                    .build();

            //재고 감소 + 장바구니 아이템 삭제
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();

                if (product.getStock() < orderItem.getQuantity()) {
                    throw new RuntimeException("재고가 부족합니다.");
                }

                product.changeStock(product.getStock() - orderItem.getQuantity());
                productRepository.save(product);

                updateCartIfExists(user, product.getId(), orderItem.getQuantity());
            }

            orderRepository.save(order);

            return convertToOrderResponseDto(order);
        } else {
            return convertToOrderResponseDto(order);
        }
    }

    @Transactional
    public List<OrderResponseDto> getOrderListAlreadyPaid(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        List<Order> orders = orderRepository.findByUser(user);

        List<Order> filteredOrders = orders.stream()
                .filter(order -> order.getOrderNumber() != null)
                .toList();

        if (filteredOrders.isEmpty()) {
            throw new IllegalArgumentException("주문 목록이 비어있습니다.");
        }

        return filteredOrders.stream()
                .map(this::convertToOrderResponseDto)
                .toList();
    }

    @Transactional
    public OrderResponseDto getOrderDetail(String userId, String orderNumber) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        Order order = orderRepository.findByOrderNumberAndUser(orderNumber, user)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다."));

        return convertToOrderResponseDto(order);
    }

    @Transactional
    private OrderResponseDto convertToOrderResponseDto(Order order) {
        List<OrderItemResponseDto> orderItems = order.getOrderItems().stream()
                .map(item -> new OrderItemResponseDto(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.totalPrice()
                ))
                .toList();

        OrderCreationResponseDto orderCreationResponseDto = new OrderCreationResponseDto(
                order.getId(),
                orderItems,
                order.getTotalPrice(),
                order.getReducedPrice(),
                order.getShipmentFee(),
                order.getTotalTotalPrice()
        );

        return new OrderResponseDto(
                orderCreationResponseDto,
                order.getOrderNumber(),
                order.getStatus(),
                order.getReceiverName(),
                order.getAddress(),
                order.getMobile(),
                order.getDeliveryMessage(),
                order.getPaymentMethod(),
                order.getCreatedAt()
        );
    }

    private String generateOrderNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String orderNumber = date + String.format("%04d", orderRepository.count() + 1);
        return orderNumber;
    }

    private void updateCartIfExists(User user, Long productId, int quantity) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("장바구니를 찾을 수 없습니다."));

        CartItem cartItem = cartItemRepository.findByCartAndProductId(cart, productId).orElse(null);

        if (cartItem != null) {
            if (cartItem.getQuantity() >= quantity) {
                cartItem.setQuantity(cartItem.getQuantity() - quantity);

                if (cartItem.getQuantity() <= 0) {
                    cartItemRepository.delete(cartItem);
                } else {
                    cartItemRepository.save(cartItem);
                }
            } else {
                cartItemRepository.delete(cartItem);
            }
        }
    }
}
