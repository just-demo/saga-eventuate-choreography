package demo.customer.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import demo.customer.api.CustomerSnapshotEvent;
import demo.customer.entity.Customer;
import demo.customer.repository.CustomerRepository;
import demo.customer.service.OrderEventConsumer;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.viewsupport.rebuild.DBLockService;
import io.eventuate.tram.viewsupport.rebuild.DomainEventWithEntityId;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportServiceFactory;

@Configuration
public class CustomerConfig {

  // TODO: really needed?
  @Bean
  public HttpMessageConverters customConverters() {
    HttpMessageConverter<?> additional = new MappingJackson2HttpMessageConverter();
    return new HttpMessageConverters(additional);
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(OrderEventConsumer orderEventConsumer,
      DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("orderServiceEvents", orderEventConsumer.domainEventHandlers());
  }

  @Bean
  public DomainSnapshotExportService<Customer> domainSnapshotExportService(CustomerRepository customerRepository,
      DomainSnapshotExportServiceFactory<Customer> domainSnapshotExportServiceFactory) {
    return domainSnapshotExportServiceFactory.make(
        Customer.class,
        customerRepository,
        customer -> {
          DomainEvent domainEvent = new CustomerSnapshotEvent(customer.getId(),
              customer.getName(),
              customer.getCreditLimit());

          return new DomainEventWithEntityId(customer.getId(), domainEvent);
        },
        new DBLockService.TableSpec("customer", "customer0_"),
        "MySqlReader");
  }
}
