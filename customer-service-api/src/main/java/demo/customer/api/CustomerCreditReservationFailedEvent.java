package demo.customer.api;

import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreditReservationFailedEvent implements DomainEvent {

  private Long orderId;
}
