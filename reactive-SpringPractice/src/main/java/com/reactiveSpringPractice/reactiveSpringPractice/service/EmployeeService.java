package com.reactiveSpringPractice.reactiveSpringPractice.service;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Employee;
import reactor.core.publisher.Flux;

public interface EmployeeService {
    Flux<Employee> getAllEmployees();
}
