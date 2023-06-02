package org.imaginnovate.test.controllers;

import lombok.extern.slf4j.Slf4j;
import org.imaginnovate.test.entities.Employee;
import org.imaginnovate.test.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;

@Slf4j(topic = "EMPLOYEE_CONTROLLER")
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/create")
    public ResponseEntity<?> createEmplyoee(@Valid @RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.createEmplyoee(employee), HttpStatus.OK);
    }

    @GetMapping("/getTaxes/{employeeId}")
    public ResponseEntity<?> getTaxes(@PathVariable String employeeId) throws ParseException {
        return new ResponseEntity<>(employeeService.getTaxes(employeeId), HttpStatus.OK);
    }
}
