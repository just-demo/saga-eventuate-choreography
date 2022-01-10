package demo.customer.service;

import static demo.customer.api.AggregateType.CUSTOMER;
import static java.util.Collections.singletonList;

import java.math.BigDecimal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import demo.customer.api.CustomerCreatedEvent;
import demo.customer.api.CustomerCreditReservationFailedEvent;
import demo.customer.api.CustomerCreditReservedEvent;
import demo.customer.api.CustomerValidationFailedEvent;
import demo.customer.entity.Customer;
import demo.customer.exception.CustomerCreditLimitExceededException;
import demo.customer.repository.CustomerRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final DomainEventPublisher domainEventPublisher;

  @Transactional
  public Customer createCustomer(String name, BigDecimal creditLimit) {
    Customer customer = customerRepository.save(new Customer(name, creditLimit));
    domainEventPublisher.publish(CUSTOMER, customer.getId(),
        singletonList(new CustomerCreatedEvent(customer.getName(), customer.getCreditLimit())));
    return customer;
  }

  void reserveCredit(long orderId, long customerId, BigDecimal orderTotal) {
    Optional<Customer> possibleCustomer = customerRepository.findById(customerId);

    if (!possibleCustomer.isPresent()) {
      log.info("Non-existent customer: {}", customerId);
      domainEventPublisher.publish(CUSTOMER, customerId, singletonList(new CustomerValidationFailedEvent(orderId)));
      return;
    }

    Customer customer = possibleCustomer.get();
    try {
      customer.reserveCredit(orderId, orderTotal);
      domainEventPublisher.publish(CUSTOMER, customer.getId(), singletonList(new CustomerCreditReservedEvent(orderId)));
    } catch (CustomerCreditLimitExceededException e) {
      domainEventPublisher.publish(CUSTOMER, customer.getId(),
          singletonList(new CustomerCreditReservationFailedEvent(orderId)));
    }
  }

  void releaseCredit(long orderId, long customerId) {
    Customer customer = customerRepository.findById(customerId).get();
    customer.unreserveCredit(orderId);
  }
}
