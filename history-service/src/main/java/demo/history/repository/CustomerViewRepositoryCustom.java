package demo.history.repository;

import java.math.BigDecimal;
import demo.order.api.OrderState;

public interface CustomerViewRepositoryCustom {

  void addCustomer(Long customerId, String customerName, BigDecimal creditLimit);

  void addOrder(Long customerId, Long orderId, BigDecimal orderTotal);

  void updateOrderState(Long customerId, Long orderId, OrderState state);
}
