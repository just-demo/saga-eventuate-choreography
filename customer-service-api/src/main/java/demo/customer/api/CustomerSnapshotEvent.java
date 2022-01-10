package demo.customer.api;

import java.math.BigDecimal;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSnapshotEvent implements DomainEvent {

  private Long id;
  private String name;
  private BigDecimal creditLimit;
}
