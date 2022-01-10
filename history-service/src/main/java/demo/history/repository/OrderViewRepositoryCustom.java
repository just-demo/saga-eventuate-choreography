package demo.history.repository;

import java.math.BigDecimal;
import demo.order.api.OrderState;

public interface OrderViewRepositoryCustom {
  void addOrder(Long orderId, BigDecimal orderTotal);
  void updateOrderState(Long orderId, OrderState state);
}
