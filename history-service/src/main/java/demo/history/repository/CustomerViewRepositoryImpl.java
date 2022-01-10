package demo.history.repository;

import static java.lang.System.currentTimeMillis;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import demo.history.entity.CustomerView;
import demo.history.entity.OrderInfo;
import demo.order.api.OrderState;
import lombok.RequiredArgsConstructor;

//@Repository
@RequiredArgsConstructor
public class CustomerViewRepositoryImpl implements CustomerViewRepositoryCustom {

  private final MongoTemplate mongoTemplate;

  @Override
  public void addCustomer(Long customerId, String customerName, BigDecimal creditLimit) {
    mongoTemplate.upsert(new Query(where("id").is(customerId)), new Update()
        .set("name", customerName)
        .set("creditLimit", creditLimit)
        .set("creationTime", currentTimeMillis()), CustomerView.class);
  }

  @Override
  public void addOrder(Long customerId, Long orderId, BigDecimal orderTotal) {
    mongoTemplate.upsert(new Query(where("id").is(customerId)),
        new Update().set("orders." + orderId, new OrderInfo(orderId, orderTotal)), CustomerView.class);
  }

  @Override
  public void updateOrderState(Long customerId, Long orderId, OrderState state) {
    mongoTemplate.upsert(new Query(where("id").is(customerId)),
        new Update().set("orders." + orderId + ".state", state), CustomerView.class);
  }
}
