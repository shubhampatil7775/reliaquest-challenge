package com.reliaquest.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.*;
import com.reliaquest.api.enums.EmployeeEndpoint;
import com.reliaquest.api.model.Employee;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmployeeService(
            RestTemplate restTemplate, @Value("${employee.api.base-url}") String baseUrl, ObjectMapper objectMapper) {
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
            GetAllEmployeeResponse response = objectMapper.readValue(jsonResponse, GetAllEmployeeResponse.class);
            employees = EmployeeMapper.mapToEmployeeList(response);
        } catch (IOException e) {
            logger.error("Error deserializing response: {}", e.getMessage());
        }
        logger.info("Fetched {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        logger.info("Searching employees by name: {}", searchString);
        ResponseEntity<List<Employee>> allEmployeesResponse = getAllEmployees();
        List<Employee> filteredEmployees = allEmployeesResponse.getBody().stream()
                .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
        logger.info("Found {} employees with search string: {}", filteredEmployees.size(), searchString);
        return ResponseEntity.ok(filteredEmployees);
    }

    public ResponseEntity<Employee> getEmployeeById(String id) {
        String url = baseUrl + EmployeeEndpoint.GET_EMPLOYEE_BY_ID.getPath() + id;
        logger.info("Fetching employee by ID from URL: {}", url);
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            GetSpecificEmployeeResponse employeeResponse =
                    objectMapper.readValue(jsonResponse, GetSpecificEmployeeResponse.class);

            if (employeeResponse.getData() != null) {
                EmployeeDetailsResponse employeeDetails = employeeResponse.getData();
                Employee employee = EmployeeMapper.mapToEmployee(employeeDetails);
                logger.info("Fetched employee: {}", employee.getId());
                return ResponseEntity.ok(employee);
            } else {
                logger.info("Employee not found with ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (HttpClientErrorException.NotFound e) {
            logger.info("Employee not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Error fetching employee by ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        logger.info("Fetching all employees to determine highest salary");
        ResponseEntity<List<Employee>> allEmployeesResponse = getAllEmployees();
        List<Employee> employees = allEmployeesResponse.getBody();
        if (employees == null || employees.isEmpty()) {
            logger.info("No employees found");
            return ResponseEntity.ok(0);
        }
        Integer highestSalary = employees.stream()
                .max(Comparator.comparingInt(Employee::getSalary))
                .map(Employee::getSalary)
                .orElse(0);
        logger.info("Highest salary fetched: {}", highestSalary);
        return ResponseEntity.ok(highestSalary);
    }

    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        logger.info("Fetching all employees to determine top 10 highest earning employee names");
        ResponseEntity<List<Employee>> allEmployeesResponse = getAllEmployees();
        List<Employee> employees = allEmployeesResponse.getBody();
        if (employees == null || employees.isEmpty()) {
            logger.info("No employees found");
            return ResponseEntity.ok(List.of());
        }
        List<String> topTenHighestEarningEmployeeNames = employees.stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                .limit(10)
                .map(Employee::getName)
                .collect(Collectors.toList());
        logger.info("Fetched top 10 highest earning employee names: {}", topTenHighestEarningEmployeeNames);
        return ResponseEntity.ok(topTenHighestEarningEmployeeNames);
    }

    public ResponseEntity<Employee> createEmployee(CreateEmployeeRequest employeeInput) {
        String url = baseUrl + EmployeeEndpoint.CREATE_EMPLOYEE.getPath();
        logger.info("Creating employee with input email: {}", employeeInput.getEmail());
        GetSpecificEmployeeResponse employeeResponse = restTemplate.postForObject(url, employeeInput, GetSpecificEmployeeResponse.class);

        try {
            if (employeeResponse.getData() != null) {
                EmployeeDetailsResponse employeeDetails = employeeResponse.getData();
                Employee employee = EmployeeMapper.mapToEmployee(employeeDetails);

                logger.info("Created employee: {}", employee.getId());
                return ResponseEntity.ok(employee);
            } else {
                logger.info("Error creating employee by ID: {}", employeeInput.getEmail());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }catch (Exception e) {
            logger.error("Error creating employee by ID: {}", employeeInput.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<String> deleteEmployeeById(String id) {
        ResponseEntity<Employee> employeeResponse = getEmployeeById(id);
        Employee employee = employeeResponse.getBody();
        if (employee == null) {
            logger.error("Employee not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

        String name = employee.getName();
        String url = baseUrl + EmployeeEndpoint.DELETE_EMPLOYEE_BY_ID.getPath();
        logger.info("Deleting employee by name from URL: {}", url);
        DeleteEmployeeRequest input = new DeleteEmployeeRequest();
        input.setName(name);
        HttpEntity<DeleteEmployeeRequest> request = new HttpEntity<>(input);
        try {
            String jsonResponse = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class).getBody();
            DeleteEmployeeResponse deleteResponse = objectMapper.readValue(jsonResponse, DeleteEmployeeResponse.class);
            Boolean isDeleted = deleteResponse.getData();

            if (isDeleted) {
                logger.info("Deleted employee with name: {}", name);
                return ResponseEntity.ok("Employee deleted successfully");
            } else {
                logger.error("Failed to delete employee with name: {}", name);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete employee");
            }
        } catch (HttpServerErrorException.InternalServerError e) {
            logger.error("Internal server error while deleting employee with name: {}", name);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        } catch (Exception e) {
            logger.error("Error deleting employee by name: {}", name, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting employee");
        }
    }
}
