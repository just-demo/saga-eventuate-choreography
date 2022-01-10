package demo.order.api;

import java.math.BigDecimal;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent implements DomainEvent {

  private Long customerId;
  private BigDecimal orderTotal;
}
