package demo.order.model;


import demo.order.api.OrderState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderResponse {

  private Long orderId;
  private OrderState orderState;
}
