package com.reactiveSpringPractice.reactiveSpringPractice.service.impl;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Employee;
import com.reactiveSpringPractice.reactiveSpringPractice.repository.EmployeeRepository;
import com.reactiveSpringPractice.reactiveSpringPractice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;


    @Override
    public Flux<Employee> getAllEmployees() {
        return employeeRepository.findBy();
    }
}
