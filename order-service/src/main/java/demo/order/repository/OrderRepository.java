package demo.order.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import demo.order.entity.Order;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
}
