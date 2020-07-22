package com.reactiveSpringPractice.reactiveSpringPractice.repository;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
    @Tailable
    Flux<Employee> findBy();
}
