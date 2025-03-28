package com.codeisevenlycooked.evenly.dto;

import com.codeisevenlycooked.evenly.entity.Order;
import com.codeisevenlycooked.evenly.entity.OrderItem;
import com.codeisevenlycooked.evenly.entity.OrderStatus;
import com.codeisevenlycooked.evenly.entity.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdminListDto {
    private Long id;
    private String orderNumber;
    private String userId;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createAt;

    private String receiverName;
    private String address;
    private String mobile;
    private String deliveryMessage;
    private PaymentMethod paymentMethod;

    private List<OrderItem> orderItems;

    public AdminListDto(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.userId = order.getUser().getUserId();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.createAt = order.getCreatedAt();

        this.receiverName = order.getReceiverName();
        this.address = order.getAddress();
        this.mobile = order.getMobile();
        this.deliveryMessage = order.getDeliveryMessage();
        this.paymentMethod = order.getPaymentMethod();
        this.orderItems = order.getOrderItems();
    }


}
