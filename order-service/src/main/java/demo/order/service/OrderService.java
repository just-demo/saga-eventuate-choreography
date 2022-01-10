package demo.order.service;

import static demo.order.api.AggregateType.ORDER;
import static java.util.Collections.singletonList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.order.api.OrderApprovedEvent;
import demo.order.api.OrderCancelledEvent;
import demo.order.api.OrderCreatedEvent;
import demo.order.api.OrderRejectedEvent;
import demo.order.entity.Order;
import demo.order.repository.OrderRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

  private final DomainEventPublisher domainEventPublisher;
  private final OrderRepository orderRepository;

  @Transactional
  public Order createOrder(Order order) {
    orderRepository.save(order);
    domainEventPublisher.publish(ORDER, order.getId(),
        singletonList(new OrderCreatedEvent(order.getCustomerId(), order.getOrderTotal())));
    return order;
  }

  public void approveOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.noteCreditReserved();
    domainEventPublisher.publish(ORDER, orderId, singletonList(new OrderApprovedEvent(order.getCustomerId())));
  }

  public void rejectOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.noteCreditReservationFailed();
    domainEventPublisher.publish(ORDER, orderId, singletonList(new OrderRejectedEvent(order.getCustomerId())));
  }

  @Transactional
  public Order cancelOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new IllegalArgumentException(String.format("order with id %s not found", orderId)));
    order.cancel();
    domainEventPublisher.publish(ORDER, orderId, singletonList(new OrderCancelledEvent(order.getCustomerId())));
    return order;
  }
}
