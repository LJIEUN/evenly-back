//package com.codeisevenlycooked.evenly.service;
//
//import com.codeisevenlycooked.evenly.dto.ItemDto;
//import com.codeisevenlycooked.evenly.dto.OrderRequestDto;
//import com.codeisevenlycooked.evenly.dto.OrderResponseDto;
//import com.codeisevenlycooked.evenly.entity.*;
//import com.codeisevenlycooked.evenly.repository.OrderRepository;
//import com.codeisevenlycooked.evenly.repository.ProductRepository;
//import com.codeisevenlycooked.evenly.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class OrderServiceTest {
//
//    @Mock private OrderRepository orderRepository;
//    @Mock private ProductRepository productRepository;
//    @Mock private UserRepository userRepository;
//
//    @InjectMocks private OrderService orderService;
//
//    private OrderRequestDto requestDto;
//    private User user;
//    private Product product;
//    private Order order;
//
//    @BeforeEach
//    void setUp() {
//        user = new User("testUserId", "password123", "이브니", UserRole.USER); // 실제 값 사용
//        product = new Product();
//        ReflectionTestUtils.setField(product, "id", 1L);
//        ReflectionTestUtils.setField(product, "price", 25000);
//
//        ReflectionTestUtils.setField(product, "name", "상품명");
//        ReflectionTestUtils.setField(product, "description", "상품 설명");
//        ReflectionTestUtils.setField(product, "imageUrl", "URL");
//        ReflectionTestUtils.setField(product, "category", "카테고리");
//        ReflectionTestUtils.setField(product, "stock", 10);
//        ReflectionTestUtils.setField(product, "status", ProductStatus.AVAILABLE);
//
//        requestDto = new OrderRequestDto(
//                List.of(new ItemDto(1L, 2)), // 상품 ID = 1, 수량 = 2
//                "이브니",
//                "서울시 강남구",
//                "010-2222-4444",
//                "문 앞에 놓아주세요",
//                "CARD"
//        );
//
//        order = new Order(user, BigDecimal.ZERO,
//                requestDto.getReceiverName(), requestDto.getAddress(), requestDto.getMobile(),
//                requestDto.getDeliveryMessage(), requestDto.getPaymentMethod());
//    }
//
//    @Test
//    void createOrder_success() {
//        when(userRepository.findByUserId(anyString())).thenReturn(Optional.of(user));
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        when(orderRepository.save(any(Order.class))).thenReturn(order);
//
//        OrderResponseDto responseDto = orderService.createOrder("testUserId", requestDto);
//
//        assertThat(responseDto).isNotNull();
//        assertThat(responseDto.getReceiverName()).isEqualTo(requestDto.getReceiverName());
//        assertThat(responseDto.getOrderItems()).hasSize(1);
//        assertThat(responseDto.getTotalPrice()).isEqualTo(BigDecimal.valueOf(50000));
//
//        verify(userRepository, times(1)).findByUserId(anyString());
//        verify(productRepository, times(1)).findById(1L);
//        verify(orderRepository, times(1)).save(any(Order.class));
//    }
//
//}