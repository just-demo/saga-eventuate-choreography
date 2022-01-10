package demo.history.entity;

import java.math.BigDecimal;
import demo.order.api.OrderState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OrderView {

  @Id
  private Long id;

  private OrderState state;
  private BigDecimal orderTotal;


  public OrderView() {
  }

  public OrderView(Long id, BigDecimal orderTotal) {
    this.id = id;
    this.orderTotal = orderTotal;
    this.state = OrderState.PENDING;
  }

  public BigDecimal getOrderTotal() {
    return orderTotal;
  }

  public OrderState getState() {
    return state;
  }
}
