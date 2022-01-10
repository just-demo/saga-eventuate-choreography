package demo.customer.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import demo.customer.entity.Customer;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

}
