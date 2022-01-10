package demo.customer.service;

import static demo.order.api.AggregateType.ORDER;
import static java.lang.Long.parseLong;

import org.springframework.stereotype.Service;

import demo.order.api.OrderCancelledEvent;
import demo.order.api.OrderCreatedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

  private final CustomerService customerService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
        .forAggregateType(ORDER)
        .onEvent(OrderCreatedEvent.class, this::handleOrderCreatedEvent)
        .onEvent(OrderCancelledEvent.class, this::handleOrderCancelledEvent)
        .build();
  }

  public void handleOrderCreatedEvent(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    OrderCreatedEvent event = domainEventEnvelope.getEvent();
    customerService.reserveCredit(parseLong(domainEventEnvelope.getAggregateId()),
        event.getCustomerId(), event.getOrderTotal());
  }

  public void handleOrderCancelledEvent(DomainEventEnvelope<OrderCancelledEvent> domainEventEnvelope) {
    customerService.releaseCredit(parseLong(domainEventEnvelope.getAggregateId()),
        domainEventEnvelope.getEvent().getCustomerId());
  }

}
