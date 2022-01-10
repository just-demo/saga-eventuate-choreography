package demo.history.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import demo.history.entity.CustomerView;

public interface CustomerViewRepository extends MongoRepository<CustomerView, Long>, CustomerViewRepositoryCustom {

}
