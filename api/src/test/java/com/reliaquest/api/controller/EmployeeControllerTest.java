package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        Employee employee = new Employee();
        employee.setId("1");
        employee.setName("Shubham Patil");
        employee.setSalary(1000);
        employee.setAge(30);
        employee.setTitle("Engineer");
        employee.setEmail("shubham.patil@gmail.com");

        List<Employee> employees = Arrays.asList(employee);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Shubham Patil", response.getBody().get(0).getName());
    }

    @Test
    void testGetAllEmployeesEmpty() {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetEmployeesByNameSearch() {
        Employee employee = new Employee();
        employee.setId("1");
        employee.setName("Shubham Patil");
        employee.setSalary(1000);
        employee.setAge(30);
        employee.setTitle("Engineer");
        employee.setEmail("shubham.patil@gmail.com");

        List<Employee> employees = Arrays.asList(employee);
        when(employeeService.getEmployeesByNameSearch("Shubham")).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch("Shubham");
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Shubham Patil", response.getBody().get(0).getName());
    }

    @Test
    void testGetEmployeesByNameSearchNotFound() {
        when(employeeService.getEmployeesByNameSearch("Shubham")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch("Shubham");
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setId("1");
        employee.setName("Shubham Patil");
        employee.setSalary(1000);
        employee.setAge(30);
        employee.setTitle("Engineer");
        employee.setEmail("shubham.patil@gmail.com");

        when(employeeService.getEmployeeById("1")).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.getEmployeeById("1");
        assertNotNull(response.getBody());
        assertEquals("Shubham Patil", response.getBody().getName());
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        when(employeeService.getEmployeeById("1")).thenReturn(null);

        ResponseEntity<Employee> response = employeeController.getEmployeeById("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(100000);

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();
        assertEquals(100000, response.getBody());
    }

    @Test
    void testGetHighestSalaryOfEmployeesNoEmployees() {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(0);

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();
        assertEquals(0, response.getBody());
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        List<String> names = Arrays.asList("Shubham Patil", "Jane Doe");
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(names);

        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Shubham Patil", response.getBody().get(0));
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNamesNoEmployees() {
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(Collections.emptyList());

        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testCreateEmployee() {
        CreateEmployeeRequest input = new CreateEmployeeRequest();
        input.setName("Shubham Patil");
        input.setTitle("Engineer");
        input.setSalary(1000);
        input.setAge(30);
        input.setEmail("shubham.patil@gmail.com");

        Employee employee = new Employee();
        employee.setId("1");
        employee.setName("Shubham Patil");
        employee.setSalary(1000);
        employee.setAge(30);
        employee.setTitle("Engineer");
        employee.setEmail("shubham.patil@gmail.com");

        when(employeeService.createEmployee(any(CreateEmployeeRequest.class))).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.createEmployee(input);
        assertNotNull(response.getBody());
        assertEquals("Shubham Patil", response.getBody().getName());
    }

    @Test
    void testCreateEmployeeFailure() {
        CreateEmployeeRequest input = new CreateEmployeeRequest();
        input.setName("Shubham Patil");
        input.setTitle("Engineer");
        input.setSalary(1000);
        input.setAge(30);
        input.setEmail("shubham.patil@gmail.com");

        when(employeeService.createEmployee(any(CreateEmployeeRequest.class))).thenReturn(null);

        ResponseEntity<Employee> response = employeeController.createEmployee(input);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteEmployeeById() {
        when(employeeService.deleteEmployeeById("1")).thenReturn("Employee deleted successfully");

        ResponseEntity<String> response = employeeController.deleteEmployeeById("1");
        assertEquals("Employee deleted successfully", response.getBody());
    }

    @Test
    void testDeleteEmployeeByIdNotFound() {
        when(employeeService.deleteEmployeeById("1")).thenReturn("Employee not found");

        ResponseEntity<String> response = employeeController.deleteEmployeeById("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee not found", response.getBody());
    }

    @Test
    void testDeleteEmployeeByIdFailure() {
        when(employeeService.deleteEmployeeById("1")).thenReturn("Failed to delete employee");

        ResponseEntity<String> response = employeeController.deleteEmployeeById("1");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to delete employee", response.getBody());
    }
}
