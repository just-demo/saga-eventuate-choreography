package demo.order.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.eventuate.common.json.mapper.JSonMapper;
import demo.order.entity.Order;
import demo.order.repository.OrderRepository;
import demo.order.model.CreateOrderRequest;
import demo.order.model.CreateOrderResponse;
import demo.order.model.GetOrderResponse;
import demo.order.service.OrderService;
import io.eventuate.tram.viewsupport.rebuild.DomainSnapshotExportService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService orderService;
  private final OrderRepository orderRepository;
  private final DomainSnapshotExportService<Order> domainSnapshotExportService;

  @PostMapping
  public CreateOrderResponse createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
    Order order = orderService.createOrder(
        new Order(createOrderRequest.getCustomerId(), createOrderRequest.getOrderTotal()));
    return new CreateOrderResponse(order.getId());
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<GetOrderResponse> getOrder(@PathVariable Long orderId) {
    return orderRepository
        .findById(orderId)
        .map(order -> ResponseEntity.ok(new GetOrderResponse(order.getId(), order.getState())))
        // TODO: simplify
        .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
  }

  @PostMapping("/{orderId}/cancel")
  public GetOrderResponse cancelOrder(@PathVariable Long orderId) {
    Order order = orderService.cancelOrder(orderId);
    return new GetOrderResponse(order.getId(), order.getState());
  }

  @PostMapping("/make-snapshot")
  public String makeSnapshot() {
    return JSonMapper.toJson(domainSnapshotExportService.exportSnapshots());
  }
}
