package demo.history.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.history.entity.CustomerView;
import demo.history.repository.CustomerViewRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/history")
public class HistoryController {

  private final CustomerViewRepository customerViewRepository;

  @GetMapping("/customers/{customerId}")
  public ResponseEntity<CustomerView> getCustomer(@PathVariable Long customerId) {
    return customerViewRepository
        .findById(customerId)
        .map(ResponseEntity::ok)
        // TODO: simplify
        .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
  }

  @GetMapping("/customers/count")
  public long getCustomerCount() {
    return customerViewRepository.count();
  }
}
