package com.reactiveSpringPractice.reactiveSpringPractice.component;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Employee;
import com.reactiveSpringPractice.reactiveSpringPractice.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class EmployeeBootStrap implements CommandLineRunner {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private MongoOperations mongoOperations;

    private List<String> competency = Arrays.asList("JVM", "AMC", "BA", "DEV OPS", "MEAN", "FEEN");

    @Override
    public void run(String... args)
    throws Exception {
        Random random = new Random();
        mongoOperations.dropCollection(Employee.class);
        //creating capped collection
        mongoOperations.createCollection(Employee.class,
                                         CollectionOptions.empty().maxDocuments(20).size(50000).capped());
        Flux.interval(Duration.ofSeconds(1))
            .map(it -> new Employee(null, "EmployeeName" + it, "TTN",
                                    competency.get(random.nextInt(competency.size()))))
            .flatMap(employee -> employeeRepository.insert(employee))
            .subscribe(employee -> log.info("employee inserted is {}", employee));
    }
}
