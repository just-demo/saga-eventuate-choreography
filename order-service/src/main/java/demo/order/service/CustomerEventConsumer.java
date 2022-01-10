package demo.order.service;

import static demo.customer.api.AggregateType.CUSTOMER;

import demo.customer.api.CustomerCreditReservationFailedEvent;
import demo.customer.api.CustomerCreditReservedEvent;
import demo.customer.api.CustomerValidationFailedEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerEventConsumer {

  private final OrderService orderService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType(CUSTOMER)
            .onEvent(CustomerCreditReservedEvent.class, this::handleCustomerCreditReservedEvent)
            .onEvent(CustomerCreditReservationFailedEvent.class, this::handleCustomerCreditReservationFailedEvent)
            .onEvent(CustomerValidationFailedEvent.class, this::handleCustomerValidationFailedEvent)
            .build();
  }

  private void handleCustomerCreditReservedEvent(DomainEventEnvelope<CustomerCreditReservedEvent> domainEventEnvelope) {
    orderService.approveOrder(domainEventEnvelope.getEvent().getOrderId());
  }

  private void handleCustomerCreditReservationFailedEvent(DomainEventEnvelope<CustomerCreditReservationFailedEvent> domainEventEnvelope) {
    orderService.rejectOrder(domainEventEnvelope.getEvent().getOrderId());
  }

  private void handleCustomerValidationFailedEvent(DomainEventEnvelope<CustomerValidationFailedEvent> domainEventEnvelope) {
    orderService.rejectOrder(domainEventEnvelope.getEvent().getOrderId());
  }

}
