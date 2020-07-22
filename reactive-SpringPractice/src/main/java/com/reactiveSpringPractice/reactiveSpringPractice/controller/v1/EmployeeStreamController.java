package com.reactiveSpringPractice.reactiveSpringPractice.controller.v1;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Employee;
import com.reactiveSpringPractice.reactiveSpringPractice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class EmployeeStreamController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping(value = "/v1/employees", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }


}
