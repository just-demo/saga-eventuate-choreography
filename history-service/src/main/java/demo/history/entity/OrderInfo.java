package demo.history.entity;

import java.math.BigDecimal;
import demo.order.api.OrderState;

public class OrderInfo {

  private Long orderId;
  private OrderState state;
  private BigDecimal orderTotal;


  public OrderInfo() {
  }

  public OrderInfo(Long orderId, BigDecimal orderTotal) {
    this.orderId = orderId;
    this.orderTotal = orderTotal;
    this.state = OrderState.PENDING;
  }

  public void approve() {
    state = OrderState.APPROVED;
  }

  public void reject() {
    state = OrderState.REJECTED;
  }

  public BigDecimal getOrderTotal() {
    return orderTotal;
  }

  public OrderState getState() {
    return state;
  }
}
