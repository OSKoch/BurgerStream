package com.burgerstream.backend;

import com.burgerstream.backend.exception.InvalidOrderException;
import com.burgerstream.backend.exception.ResourceNotFoundException;
import com.burgerstream.backend.model.menu.Side;
import com.burgerstream.backend.model.menu.SizeOption;
import com.burgerstream.backend.model.order.Order;
import com.burgerstream.backend.model.order.OrderItem;
import com.burgerstream.backend.repository.menu.SideRepository;
import com.burgerstream.backend.repository.menu.SizeOptionRepository;
import com.burgerstream.backend.repository.order.OrderItemRepository;
import com.burgerstream.backend.repository.order.OrderRepository;
import com.burgerstream.backend.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(OrderService.class)
public class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SideRepository sideRepository;

    @Autowired
    private SizeOptionRepository sizeOptionRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private Order order;
    private OrderItem item;

    @BeforeEach
    void setUp() {
        Side fries = new Side();
        fries.setName("Fries");
        fries.setBasePrice(BigDecimal.valueOf(25.00));
        fries.setShareable(Boolean.TRUE);
        sideRepository.save(fries);

        SizeOption large = new SizeOption();
        large.setSizeLabel("Large");
        large.setLabel("French Fries");
        large.setExtraPrice(BigDecimal.valueOf(10.00));
        sizeOptionRepository.save(large);

        item = new OrderItem();
        item.setMenuItem(fries);
        item.setSizeOption(large);
        item.setAmount(2);
        item.setItemEndPrice(BigDecimal.valueOf(70.00));

        order = new Order();
        order.setCustomerName("John Doe");
        order.setCustomerEmail("John@Doe.com");
        order.addOrderItem(item);
    }

    @Test
    void createOrder_savesOrderAndItemsCorrectly() {
        Order savedOrder = orderService.createOrder(order);

        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderEndPrice()).isEqualByComparingTo("70.00");
        assertThat(savedOrder.getOrderItems()).hasSize(1);

        OrderItem savedItem = savedOrder.getOrderItems().getFirst();
        assertThat(savedItem.getOrder()).isEqualTo(savedOrder);
        assertThat(savedItem.getMenuItem().getName()).isEqualTo("Fries");
    }

    @Test
    void createOrder_withEmptyItems_throwsInvalidOrderException(){
        order = new Order();
        order.setCustomerName("John Doe");
        order.setCustomerEmail("John@Doe.com");
        assertThatThrownBy( () -> orderService.createOrder(order))
                .isInstanceOf(InvalidOrderException.class);
    }

    @Test
    void getOrder_withValidId_getsTheCorrectOrder(){
        order = orderService.createOrder(order);
        Long orderId = order.getId();

        Order savedOrder = orderService.getOrder(orderId);

        assertThat(savedOrder).isEqualTo(order);
    }

    @Test
    void getOrder_withInvalidId_throwsResourceNotFoundException(){
        assertThatThrownBy( () -> orderService.getOrder(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("does not exist");
    }

    @Test
    void updateCustomerDetails_updatesNameAndEmailOnly(){
        Order savedOrder = orderService.createOrder(order);

        assertThat(savedOrder.getCustomerName()).isEqualTo("John Doe");
        assertThat(savedOrder.getCustomerEmail()).isEqualTo("John@Doe.com");

        Order newDetails = new Order();
        newDetails.setCustomerName("James Gunn");
        newDetails.setCustomerEmail("James@Gunn.com");

        orderService.updateCostumerDetails(savedOrder.getId(), newDetails);

        Order updatedOrder = orderRepository.findById(savedOrder.getId())
                .orElseThrow(() -> new RuntimeException("Order not found in test"));
        assertThat(updatedOrder.getCustomerName()).isEqualTo("James Gunn");
        assertThat(updatedOrder.getCustomerEmail()).isEqualTo("James@Gunn.com");
    }

    @Test
    void deletingOrder_removesOrderItemsBecauseOfOrphanTrue() {
        order = orderService.createOrder(order);
        Long orderId = order.getId();
        Long itemId = item.getId();

        assertThat(orderRepository.findById(orderId).isPresent());
        assertThat(orderItemRepository.findById(itemId).isPresent());

        orderService.deleteOrder(orderId);

        assertThat(orderRepository.findById(orderId)).isEmpty();

        assertThat(orderRepository.count()).isZero();

        assertThat(orderItemRepository.count()).isZero();
    }

    @Test
    void removingOrderItemFromOrder_removesItFromDatabase() {
        order = orderService.createOrder(order);
        assertThat(orderRepository.findAll()).hasSize(1);
        assertThat(order.getOrderItems()).hasSize(1);
        assertThat(orderItemRepository.findAll()).hasSize(1);

        // Remove the item
        order.removeOrderItem(order.getOrderItems().getFirst());
        orderRepository.save(order);

        // Should be gone from DB
        assertThat(orderRepository.findAll()).hasSize(1);
        assertThat(orderRepository.findById(order.getId()).get().getOrderItems()).isEmpty();
        assertThat(orderItemRepository.findAll().isEmpty());
    }

    @Test
    void getOrderItemsFromOrder_validId_getListOfOrderItems(){
        Side donut = new Side();
        donut.setName("Donut");
        donut.setBasePrice(BigDecimal.valueOf(35.00));
        sideRepository.save(donut);

        item = new OrderItem();
        item.setMenuItem(donut);
        item.setAmount(1);
        item.setItemEndPrice(BigDecimal.valueOf(70.00));

        order.addOrderItem(item);

        order = orderRepository.save(order);

        List<OrderItem> orderItems = orderService.getOrderItemsFromOrder(order.getId());

        assertThat(orderItems).hasSize(2);
        assertThat(orderItems.getFirst().getMenuItem().getName()).isEqualTo("Fries");
        assertThat(orderItems.get(1).getMenuItem().getName()).isEqualTo("Donut");
    }

    @Test
    void getOrderItemsFromOrder_invalidId_throwResourceNotFoundException(){
        assertThatThrownBy( () -> orderService.getOrderItemsFromOrder(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("does not exist");
    }
}