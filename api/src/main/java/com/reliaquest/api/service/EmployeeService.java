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

    public List<Employee> getAllEmployees() {
        String url = baseUrl + EmployeeEndpoint.GET_ALL_EMPLOYEES.getPath();
        logger.info("Fetching all employees from URL: {}", url);
        String employeesDetails = restTemplate.getForObject(url, String.class);
        List<Employee> employees = null;
        try {
            GetAllEmployeeResponse response = objectMapper.readValue(employeesDetails, GetAllEmployeeResponse.class);
            employees = EmployeeMapper.mapToEmployeeList(response);
        } catch (IOException e) {
            logger.error("Error deserializing response: {}", e.getMessage());
        }
        logger.info("Fetched {} employees", employees.size());
        return employees;
    }

    public List<Employee> getEmployeesByNameSearch(String name) {
        logger.info("Searching employees by name: {}", name);
        List<Employee> allEmployees = getAllEmployees();
        List<Employee> filteredEmployees = allEmployees.stream()
                .filter(employee -> employee.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        logger.info("Found {} employees with search string: {}", filteredEmployees.size(), name);
        if(filteredEmployees.size() == 0) {
            logger.info("No employees found with search string: {}", name);
            return null;
        }
        return filteredEmployees;
    }

    public Employee getEmployeeById(String id) {
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
                return employee;
            } else {
                logger.info("Employee not found with ID: {}", id);
                return null;
            }
        } catch (HttpClientErrorException.NotFound e) {
            logger.info("Employee not found with ID: {}", id);
            return null;
        } catch (Exception e) {
            logger.error("Error fetching employee by ID: {}", id, e);
            return null;
        }
    }

    public Integer getHighestSalaryOfEmployees() {
        logger.info("Fetching all employees to determine highest salary");
        List<Employee> employees = getAllEmployees();
        if (employees == null || employees.isEmpty()) {
            logger.info("No employees found");
            return 0;
        }
        Integer highestSalary = employees.stream()
                .max(Comparator.comparingInt(Employee::getSalary))
                .map(Employee::getSalary)
                .orElse(0);
        logger.info("Highest salary fetched: {}", highestSalary);
        return highestSalary;
    }

    public List<String> getTopTenHighestEarningEmployeeNames() {
        logger.info("Fetching all employees to determine top 10 highest earning employee names");
        List<Employee> employees = getAllEmployees();
        if (employees == null || employees.isEmpty()) {
            logger.info("No employees found");
            return List.of();
        }
        List<String> topTenHighestEarningEmployeeNames = employees.stream()
                .sorted(Comparator.comparingInt(Employee::getSalary).reversed())
                .limit(10)
                .map(Employee::getName)
                .collect(Collectors.toList());
        logger.info("Fetched top 10 highest earning employee names: {}", topTenHighestEarningEmployeeNames);
        return topTenHighestEarningEmployeeNames;
    }

    public Employee createEmployee(CreateEmployeeRequest employeeInput) {
        String url = baseUrl + EmployeeEndpoint.CREATE_EMPLOYEE.getPath();
        logger.info("Creating employee with input email: {}", employeeInput.getEmail());
        GetSpecificEmployeeResponse employeeResponse =
                restTemplate.postForObject(url, employeeInput, GetSpecificEmployeeResponse.class);

        try {
            if (employeeResponse.getData() != null) {
                EmployeeDetailsResponse employeeDetails = employeeResponse.getData();
                Employee employee = EmployeeMapper.mapToEmployee(employeeDetails);

                logger.info("Created employee: {}", employee.getId());
                return employee;
            } else {
                logger.info("Error creating employee for: {}", employeeInput.getName());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error creating employee for: {}", employeeInput.getName(), e);
            return null;
        }
    }

    public String deleteEmployeeById(String id) {
        Employee employee = getEmployeeById(id);
        if (employee == null) {
            logger.error("Employee not found with ID: {}", id);
            return "Employee not found";
        }

        String name = employee.getName();
        String url = baseUrl + EmployeeEndpoint.DELETE_EMPLOYEE_BY_ID.getPath();
        logger.info("Deleting employee by name from URL: {}", url);
        DeleteEmployeeRequest input = new DeleteEmployeeRequest();
        input.setName(name);
        HttpEntity<DeleteEmployeeRequest> request = new HttpEntity<>(input);
        try {
            String jsonResponse = restTemplate
                    .exchange(url, HttpMethod.DELETE, request, String.class)
                    .getBody();
            DeleteEmployeeResponse deleteResponse = objectMapper.readValue(jsonResponse, DeleteEmployeeResponse.class);
            Boolean isDeleted = deleteResponse.getData();

            if (isDeleted) {
                logger.info("Deleted employee with name: {}", name);
                return "Employee deleted successfully";
            } else {
                logger.error("Employee already deleted or does not exist: {}", name);
                return "Failed to delete employee";
            }
        } catch (HttpServerErrorException.InternalServerError e) {
            logger.error("Internal server error while deleting employee with name: {}", name);
            return "Internal server error";
        } catch (Exception e) {
            logger.error("Error deleting employee by name: {}", name, e);
            return "Error deleting employee";
        }
    }
}
