package com.reliaquest.api.service;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.Employee;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Autowired
    public EmployeeService(RestTemplate restTemplate, @Value("${employee.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ResponseEntity<List<Employee>> getAllEmployees() {
        String url = baseUrl + EmployeeEndpoint.GET_ALL_EMPLOYEES.getPath();
        logger.info("Fetching all employees from URL: {}", url);
        List<Employee> employees = restTemplate.getForObject(url, List.class);
        logger.info("Fetched {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        String url = baseUrl + EmployeeEndpoint.GET_EMPLOYEES_BY_NAME_SEARCH.getPath() + searchString;
        logger.info("Searching employees by name from URL: {}", url);
        List<Employee> employees = restTemplate.getForObject(url, List.class);
        logger.info("Found {} employees with search string: {}", employees.size(), searchString);
        return ResponseEntity.ok(employees);
    }

    public ResponseEntity<Employee> getEmployeeById(String id) {
        String url = baseUrl + EmployeeEndpoint.GET_EMPLOYEE_BY_ID.getPath() + id;
        logger.info("Fetching employee by ID from URL: {}", url);
        Employee employee = restTemplate.getForObject(url, Employee.class);
        logger.info("Fetched employee: {}", employee);
        return ResponseEntity.ok(employee);
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        String url = baseUrl + EmployeeEndpoint.GET_HIGHEST_SALARY.getPath();
        logger.info("Fetching highest salary from URL: {}", url);
        Integer highestSalary = restTemplate.getForObject(url, Integer.class);
        logger.info("Highest salary fetched: {}", highestSalary);
        return ResponseEntity.ok(highestSalary);
    }

    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        String url = baseUrl + EmployeeEndpoint.GET_TOP_TEN_HIGHEST_EARNING_EMPLOYEE_NAMES.getPath();
        logger.info("Fetching top 10 highest earning employee names from URL: {}", url);
        List<String> employeeNames = restTemplate.getForObject(url, List.class);
        logger.info("Fetched top 10 highest earning employee names: {}", employeeNames);
        return ResponseEntity.ok(employeeNames);
    }

    public ResponseEntity<Employee> createEmployee(CreateEmployeeInput employeeInput) {
        String url = baseUrl + EmployeeEndpoint.CREATE_EMPLOYEE.getPath();
        logger.info("Creating employee with input: {}", employeeInput);
        Employee employee = restTemplate.postForObject(url, employeeInput, Employee.class);
        logger.info("Created employee: {}", employee);
        return ResponseEntity.ok(employee);
    }

    public ResponseEntity<String> deleteEmployeeById(String id) {
        String url = baseUrl + EmployeeEndpoint.DELETE_EMPLOYEE_BY_ID.getPath() + id;
        logger.info("Deleting employee by ID from URL: {}", url);
        restTemplate.delete(url);
        logger.info("Deleted employee with ID: {}", id);
        return ResponseEntity.ok("Employee deleted successfully");
    }
}
