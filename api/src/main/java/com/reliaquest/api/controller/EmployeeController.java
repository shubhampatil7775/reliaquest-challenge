package com.reliaquest.api.controller;

import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController implements IEmployeeController<Employee, CreateEmployeeRequest> {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @Override
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String name) {
        return employeeService.getEmployeesByNameSearch(name);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);
    }

    @Override
    @GetMapping("/highest-salary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return employeeService.getHighestSalaryOfEmployees();
    }

    @Override
    @GetMapping("/top-ten-highest-earners")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return employeeService.getTopTenHighestEarningEmployeeNames();
    }

    @Override
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeRequest employeeInput) {
        return employeeService.createEmployee(employeeInput);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        return employeeService.deleteEmployeeById(id);
    }
}
