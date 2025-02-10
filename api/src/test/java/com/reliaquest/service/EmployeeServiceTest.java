package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.Employee;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class EmployeeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeService employeeService;

    @Value("${employee.api.base-url}")
    private String baseUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeService(restTemplate, baseUrl);
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(restTemplate.getForObject(baseUrl + EmployeeEndpoint.GET_ALL_EMPLOYEES.getPath(), List.class))
                .thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeService.getAllEmployees();
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetEmployeesByNameSearch() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(restTemplate.getForObject(
                        baseUrl + EmployeeEndpoint.GET_EMPLOYEES_BY_NAME_SEARCH.getPath() + "John", List.class))
                .thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeService.getEmployeesByNameSearch("John");
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetEmployeeById() {
        Employee employee = new Employee();
        when(restTemplate.getForObject(baseUrl + EmployeeEndpoint.GET_EMPLOYEE_BY_ID.getPath() + "1", Employee.class))
                .thenReturn(employee);

        ResponseEntity<Employee> response = employeeService.getEmployeeById("1");
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testGetHighestSalaryOfEmployees() {
        when(restTemplate.getForObject(baseUrl + EmployeeEndpoint.GET_HIGHEST_SALARY.getPath(), Integer.class))
                .thenReturn(100000);

        ResponseEntity<Integer> response = employeeService.getHighestSalaryOfEmployees();
        assertEquals(100000, response.getBody());
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() {
        List<String> names = Arrays.asList("John", "Jane");
        when(restTemplate.getForObject(
                        baseUrl + EmployeeEndpoint.GET_TOP_TEN_HIGHEST_EARNING_EMPLOYEE_NAMES.getPath(), List.class))
                .thenReturn(names);

        ResponseEntity<List<String>> response = employeeService.getTopTenHighestEarningEmployeeNames();
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testCreateEmployee() {
        Employee employee = new Employee();
        CreateEmployeeInput input = new CreateEmployeeInput();
        when(restTemplate.postForObject(baseUrl + EmployeeEndpoint.CREATE_EMPLOYEE.getPath(), input, Employee.class))
                .thenReturn(employee);

        ResponseEntity<Employee> response = employeeService.createEmployee(input);
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testDeleteEmployeeById() {
        restTemplate.delete(baseUrl + EmployeeEndpoint.DELETE_EMPLOYEE_BY_ID.getPath() + "1");
        ResponseEntity<String> response = employeeService.deleteEmployeeById("1");
        assertEquals("Employee deleted successfully", response.getBody());
    }
}
