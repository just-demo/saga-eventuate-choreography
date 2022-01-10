package demo.history.service;

import static demo.order.api.OrderState.CANCELLED;
import static demo.order.api.OrderState.REJECTED;

import java.math.BigDecimal;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import demo.history.repository.CustomerViewRepository;
import demo.order.api.OrderState;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderHistoryViewService {

  private final CustomerViewRepository customerViewRepository;

  public void createCustomer(Long customerId, String customerName, BigDecimal creditLimit) {
    customerViewRepository.addCustomer(customerId, customerName, creditLimit);
  }

  @Retryable(value = DuplicateKeyException.class, maxAttempts = 4, backoff = @Backoff(delay = 250))
  public void addOrder(Long customerId, Long orderId, BigDecimal orderTotal) {
    customerViewRepository.addOrder(customerId, orderId, orderTotal);
  }

  public void approveOrder(Long customerId, Long orderId) {
    updateOrderState(customerId, orderId, OrderState.APPROVED);
  }

  @Retryable(value = DuplicateKeyException.class, maxAttempts = 4, backoff = @Backoff(delay = 250))
  private void updateOrderState(Long customerId, Long orderId, OrderState state) {
    customerViewRepository.updateOrderState(customerId, orderId, state);
  }

  public void rejectOrder(Long customerId, Long orderId) {
    updateOrderState(customerId, orderId, REJECTED);
  }

  public void cancelOrder(Long customerId, long orderId) {
    updateOrderState(customerId, orderId, CANCELLED);
  }
}
