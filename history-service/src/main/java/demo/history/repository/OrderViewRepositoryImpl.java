package demo.history.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import demo.history.entity.OrderView;
import demo.order.api.OrderState;
import lombok.RequiredArgsConstructor;

//@Repository
@RequiredArgsConstructor
public class OrderViewRepositoryImpl implements OrderViewRepositoryCustom {

  private final MongoTemplate mongoTemplate;

  @Override
  public void addOrder(Long orderId, BigDecimal orderTotal) {
    mongoTemplate.upsert(new Query(where("id").is(orderId)),
        new Update().set("orderTotal", orderTotal), OrderView.class);
  }

  @Override
  public void updateOrderState(Long orderId, OrderState state) {
    mongoTemplate.updateFirst(new Query(where("id").is(orderId)),
        new Update().set("state", state), OrderView.class);
  }
}
