package com.reliaquest.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.mapper.EmployeeMapper;
import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.dto.EmployeeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmployeeService(RestTemplate restTemplate, @Value("${employee.api.base-url}") String baseUrl, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<List<Employee>> getAllEmployees() {
        String url = baseUrl + EmployeeEndpoint.GET_ALL_EMPLOYEES.getPath();
        logger.info("Fetching all employees from URL: {}", url);
        String jsonResponse = restTemplate.getForObject(url, String.class);
        List<Employee> employees = null;
        try {
            EmployeeResponse response = objectMapper.readValue(jsonResponse, EmployeeResponse.class);
            employees = EmployeeMapper.mapToEmployeeList(response);
        } catch (IOException e) {
            logger.error("Error deserializing response: {}", e.getMessage());
        }
        logger.info("Fetched {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        String url = baseUrl + EmployeeEndpoint.GET_EMPLOYEES_BY_NAME_SEARCH.getPath() + searchString;
        logger.info("Searching employees by name from URL: {}", url);
        String jsonResponse = restTemplate.getForObject(url, String.class);
        List<Employee> employees = null;
        try {
            EmployeeResponse response = objectMapper.readValue(jsonResponse, EmployeeResponse.class);
            employees = EmployeeMapper.mapToEmployeeList(response);
        } catch (IOException e) {
            logger.error("Error deserializing response: {}", e.getMessage());
        }
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
        String jsonResponse = restTemplate.getForObject(url, String.class);
        List<String> employeeNames = null;
        try {
            EmployeeResponse response = objectMapper.readValue(jsonResponse, EmployeeResponse.class);
            employeeNames = EmployeeMapper.mapToEmployeeList(response).stream().map(Employee::getName).collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error deserializing response: {}", e.getMessage());
        }
        logger.info("Fetched top 10 highest earning employee names: {}", employeeNames);
        return ResponseEntity.ok(employeeNames);
    }

    public ResponseEntity<Employee> createEmployee(CreateEmployeeRequest employeeInput) {
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