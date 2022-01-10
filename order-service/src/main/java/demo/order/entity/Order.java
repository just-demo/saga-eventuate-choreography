package demo.order.entity;


import static demo.order.api.OrderState.APPROVED;
import static demo.order.api.OrderState.CANCELLED;
import static demo.order.api.OrderState.PENDING;
import static demo.order.api.OrderState.REJECTED;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import demo.order.exception.PendingOrderCantBeCancelledException;
import demo.order.api.OrderState;

// TODO: use lombok
@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Enumerated(STRING)
  private OrderState state;

  private Long customerId;

  private BigDecimal orderTotal;

  @Version
  private Long version;

  public Order() {
  }

  public Order(Long customerId, BigDecimal orderTotal) {
    this.customerId = customerId;
    this.orderTotal = orderTotal;
    this.state = PENDING;
  }

  public Long getId() {
    return id;
  }

  public void noteCreditReserved() {
    this.state = APPROVED;
  }

  public void noteCreditReservationFailed() {
    this.state = REJECTED;
  }

  public OrderState getState() {
    return state;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public BigDecimal getOrderTotal() {
    return orderTotal;
  }

  public void cancel() {
    switch (state) {
      case PENDING:
        throw new PendingOrderCantBeCancelledException();
      case APPROVED:
        this.state = CANCELLED;
        return;
      default:
        throw new UnsupportedOperationException("Can't cancel in this state: " + state);
    }
  }
}
