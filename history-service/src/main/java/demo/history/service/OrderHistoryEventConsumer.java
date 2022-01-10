package demo.history.service;

import static demo.customer.api.AggregateType.CUSTOMER;
import static demo.order.api.AggregateType.ORDER;
import static java.lang.Long.parseLong;

import org.springframework.stereotype.Service;

import demo.customer.api.CustomerCreatedEvent;
import demo.order.api.OrderApprovedEvent;
import demo.order.api.OrderCancelledEvent;
import demo.order.api.OrderCreatedEvent;
import demo.order.api.OrderRejectedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class OrderHistoryEventConsumer {

  private final OrderHistoryViewService orderHistoryViewService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType(CUSTOMER)
        .onEvent(CustomerCreatedEvent.class, this::customerCreatedEventHandler)
        .andForAggregateType(ORDER)
        .onEvent(OrderCreatedEvent.class, this::orderCreatedEventHandler)
        .onEvent(OrderApprovedEvent.class, this::orderApprovedEventHandler)
        .onEvent(OrderRejectedEvent.class, this::orderRejectedEventHandler)
        .onEvent(OrderCancelledEvent.class, this::handleOrderCancelledEvent)
        .build();
  }

  private void customerCreatedEventHandler(DomainEventEnvelope<CustomerCreatedEvent> domainEventEnvelope) {
    CustomerCreatedEvent customerCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.createCustomer(parseLong(domainEventEnvelope.getAggregateId()),
        customerCreatedEvent.getName(), customerCreatedEvent.getCreditLimit());
  }

  private void orderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> eventEnvelope) {
    OrderCreatedEvent event = eventEnvelope.getEvent();
    orderHistoryViewService.addOrder(event.getCustomerId(), parseLong(eventEnvelope.getAggregateId()),
        event.getOrderTotal());
  }

  private void orderApprovedEventHandler(DomainEventEnvelope<OrderApprovedEvent> domainEventEnvelope) {
    OrderApprovedEvent orderApprovedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.approveOrder(orderApprovedEvent.getCustomerId(),
        parseLong(domainEventEnvelope.getAggregateId()));
  }

  private void orderRejectedEventHandler(DomainEventEnvelope<OrderRejectedEvent> domainEventEnvelope) {
    OrderRejectedEvent orderRejectedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.rejectOrder(orderRejectedEvent.getCustomerId(),
        parseLong(domainEventEnvelope.getAggregateId()));
  }

  private void handleOrderCancelledEvent(DomainEventEnvelope<OrderCancelledEvent> domainEventEnvelope) {
    OrderCancelledEvent orderRejectedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.cancelOrder(orderRejectedEvent.getCustomerId(),
        parseLong(domainEventEnvelope.getAggregateId()));
  }
}

