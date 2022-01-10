package demo.customer.entity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import demo.customer.exception.CustomerCreditLimitExceededException;

// TODO: user lombok
@Entity
@Table(name = "customers")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  private BigDecimal creditLimit;

  @ElementCollection
  private Map<Long, BigDecimal> creditReservations;

  private Long creationTime;

  @Version
  private Long version;

  BigDecimal availableCredit() {
    return creditLimit.subtract(creditReservations.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
  }

  public Customer() {
  }

  public Customer(String name, BigDecimal creditLimit) {
    this.name = name;
    this.creditLimit = creditLimit;
    this.creditReservations = Collections.emptyMap();
    this.creationTime = System.currentTimeMillis();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getCreditLimit() {
    return creditLimit;
  }

  public void reserveCredit(Long orderId, BigDecimal orderTotal) {
    if (availableCredit().compareTo(orderTotal) >= 0) {
      creditReservations.put(orderId, orderTotal);
    } else {
      throw new CustomerCreditLimitExceededException();
    }
  }

  public void unreserveCredit(long orderId) {
    creditReservations.remove(orderId);
  }
}
