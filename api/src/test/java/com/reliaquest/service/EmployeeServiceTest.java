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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class EmployeeServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(restTemplate.getForObject("http://localhost:8112/api/v1/employee", List.class))
                .thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeService.getAllEmployees();
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetEmployeesByNameSearch() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(restTemplate.getForObject("http://localhost:8112/api/v1/employee/search/John", List.class))
                .thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeService.getEmployeesByNameSearch("John");
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetEmployeeById() {
        Employee employee = new Employee();
        when(restTemplate.getForObject("http://localhost:8112/api/v1/employee/1", Employee.class))
                .thenReturn(employee);

        ResponseEntity<Employee> response = employeeService.getEmployeeById("1");
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testGetHighestSalaryOfEmployees() {
        when(restTemplate.getForObject("http://localhost:8112/api/v1/employee/highestSalary", Integer.class))
                .thenReturn(100000);

        ResponseEntity<Integer> response = employeeService.getHighestSalaryOfEmployees();
        assertEquals(100000, response.getBody());
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() {
        List<String> names = Arrays.asList("John", "Jane");
        when(restTemplate.getForObject(
                        "http://localhost:8112/api/v1/employee/topTenHighestEarningEmployeeNames", List.class))
                .thenReturn(names);

        ResponseEntity<List<String>> response = employeeService.getTopTenHighestEarningEmployeeNames();
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testCreateEmployee() {
        Employee employee = new Employee();
        CreateEmployeeInput input = new CreateEmployeeInput();
        when(restTemplate.postForObject("http://localhost:8112/api/v1/employee", input, Employee.class))
                .thenReturn(employee);

        ResponseEntity<Employee> response = employeeService.createEmployee(input);
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testDeleteEmployeeById() {
        restTemplate.delete("http://localhost:8112/api/v1/employee/1");
        ResponseEntity<String> response = employeeService.deleteEmployeeById("1");
        assertEquals("Employee deleted successfully", response.getBody());
    }
}
