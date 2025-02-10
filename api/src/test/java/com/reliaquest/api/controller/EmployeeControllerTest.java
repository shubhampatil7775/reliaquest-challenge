package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeService.getAllEmployees()).thenReturn(ResponseEntity.ok(employees));

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetEmployeesByNameSearch() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeService.getEmployeesByNameSearch("John")).thenReturn(ResponseEntity.ok(employees));

        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch("John");
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetEmployeeById() {
        Employee employee = new Employee();
        when(employeeService.getEmployeeById("1")).thenReturn(ResponseEntity.ok(employee));

        ResponseEntity<Employee> response = employeeController.getEmployeeById("1");
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testGetHighestSalaryOfEmployees() {
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(ResponseEntity.ok(100000));

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();
        assertEquals(100000, response.getBody());
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() {
        List<String> names = Arrays.asList("John", "Jane");
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(ResponseEntity.ok(names));

        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testCreateEmployee() {
        Employee employee = new Employee();
        CreateEmployeeInput input = new CreateEmployeeInput();
        when(employeeService.createEmployee(input)).thenReturn(ResponseEntity.ok(employee));

        ResponseEntity<Employee> response = employeeController.createEmployee(input);
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testDeleteEmployeeById() {
        when(employeeService.deleteEmployeeById("1")).thenReturn(ResponseEntity.ok("Employee deleted successfully"));

        ResponseEntity<String> response = employeeController.deleteEmployeeById("1");
        assertEquals("Employee deleted successfully", response.getBody());
    }
}
