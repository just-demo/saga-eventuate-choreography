package demo.order.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import demo.order.api.OrderSnapshotEvent;
import demo.order.entity.Order;
import demo.order.repository.OrderRepository;
import demo.order.service.CustomerEventConsumer;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.viewsupport.rebuild.DBLockService;
import io.eventuate.tram.viewsupport.rebuild.DomainEventWithEntityId;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportServiceFactory;

@Configuration
public class OrderConfig {

  @Bean
  // TODO: really needed???
  public HttpMessageConverters customConverters() {
    HttpMessageConverter<?> additional = new MappingJackson2HttpMessageConverter();
    return new HttpMessageConverters(additional);
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(CustomerEventConsumer customerEventConsumer,
      DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("customerServiceEvents", customerEventConsumer.domainEventHandlers());
  }

  @Bean
  public DomainSnapshotExportService<Order> domainSnapshotExportService(OrderRepository orderRepository,
      DomainSnapshotExportServiceFactory<Order> domainSnapshotExportServiceFactory) {
    return domainSnapshotExportServiceFactory.make(
        Order.class,
        orderRepository,
        order -> {
          DomainEvent domainEvent = new OrderSnapshotEvent(order.getId(),
              order.getCustomerId(),
              order.getOrderTotal(),
              order.getState());

          return new DomainEventWithEntityId(order.getId(), domainEvent);
        },
        new DBLockService.TableSpec("orders", "order0_"),
        "MySqlReader");
  }
}
