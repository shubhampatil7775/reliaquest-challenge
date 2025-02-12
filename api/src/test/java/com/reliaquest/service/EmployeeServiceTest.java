package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.dto.EmployeeDetailsResponse;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.model.Employee;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

class EmployeeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() throws Exception {
        String jsonResponse =
                "{\"data\":[{\"id\":\"1\",\"employee_name\":\"John Doe\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"john.doe@example.com\"}],\"status\":\"Successfully processed request.\"}";
        EmployeeResponse employeeResponse = new EmployeeResponse();
        EmployeeDetailsResponse employeeDetails = new EmployeeDetailsResponse();
        employeeDetails.setId("1");
        employeeDetails.setEmployee_name("John Doe");
        employeeDetails.setEmployee_salary(1000);
        employeeDetails.setEmployee_age(30);
        employeeDetails.setEmployee_title("Engineer");
        employeeDetails.setEmployee_email("john.doe@example.com");
        employeeResponse.setData(Arrays.asList(employeeDetails));

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(EmployeeResponse.class))).thenReturn(employeeResponse);

        ResponseEntity<List<Employee>> response = employeeService.getAllEmployees();
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("John Doe", response.getBody().get(0).getName());
    }

    @Test
    void testGetEmployeeById() {
        String id = "1";
        EmployeeDetailsResponse employeeDetails = new EmployeeDetailsResponse();
        employeeDetails.setId(id);
        employeeDetails.setEmployee_name("John Doe");

        when(restTemplate.getForObject(anyString(), eq(EmployeeDetailsResponse.class)))
                .thenReturn(employeeDetails);

        ResponseEntity<Employee> response = employeeService.getEmployeeById(id);
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        String id = "1";

        when(restTemplate.getForObject(anyString(), eq(EmployeeDetailsResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        ResponseEntity<Employee> response = employeeService.getEmployeeById(id);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateEmployee() {
        CreateEmployeeRequest input = new CreateEmployeeRequest();
        input.setName("John Doe");
        input.setTitle("Engineer");
        input.setSalary(1000);
        input.setAge(30);
        input.setEmail("john.doe@example.com");

        EmployeeDetailsResponse employeeDetails = new EmployeeDetailsResponse();
        employeeDetails.setId("1");
        employeeDetails.setEmployee_name("John Doe");

        when(restTemplate.postForObject(anyString(), any(), eq(EmployeeDetailsResponse.class)))
                .thenReturn(employeeDetails);

        ResponseEntity<Employee> response = employeeService.createEmployee(input);
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    void testDeleteEmployeeById() {
        String id = "1";
        EmployeeDetailsResponse employeeDetails = new EmployeeDetailsResponse();
        employeeDetails.setId(id);
        employeeDetails.setEmployee_name("John Doe");

        when(restTemplate.getForObject(anyString(), eq(EmployeeDetailsResponse.class)))
                .thenReturn(employeeDetails);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Boolean.class)))
                .thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

        ResponseEntity<String> response = employeeService.deleteEmployeeById(id);
        assertEquals("Employee deleted successfully", response.getBody());
    }

    @Test
    void testDeleteEmployeeByIdNotFound() {
        String id = "1";

        when(restTemplate.getForObject(anyString(), eq(EmployeeDetailsResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        ResponseEntity<String> response = employeeService.deleteEmployeeById(id);
        assertEquals("Employee not found", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetHighestSalaryOfEmployees() throws Exception {
        String jsonResponse =
                "{\"data\":[{\"id\":\"1\",\"employee_name\":\"John Doe\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"john.doe@example.com\"},{\"id\":\"2\",\"employee_name\":\"Jane Doe\",\"employee_salary\":2000,\"employee_age\":25,\"employee_title\":\"Manager\",\"employee_email\":\"jane.doe@example.com\"}],\"status\":\"Successfully processed request.\"}";
        EmployeeResponse employeeResponse = new EmployeeResponse();
        EmployeeDetailsResponse employeeDetails1 = new EmployeeDetailsResponse();
        employeeDetails1.setId("1");
        employeeDetails1.setEmployee_name("John Doe");
        employeeDetails1.setEmployee_salary(1000);
        employeeDetails1.setEmployee_age(30);
        employeeDetails1.setEmployee_title("Engineer");
        employeeDetails1.setEmployee_email("john.doe@example.com");
        EmployeeDetailsResponse employeeDetails2 = new EmployeeDetailsResponse();
        employeeDetails2.setId("2");
        employeeDetails2.setEmployee_name("Jane Doe");
        employeeDetails2.setEmployee_salary(2000);
        employeeDetails2.setEmployee_age(25);
        employeeDetails2.setEmployee_title("Manager");
        employeeDetails2.setEmployee_email("jane.doe@example.com");
        employeeResponse.setData(Arrays.asList(employeeDetails1, employeeDetails2));

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(EmployeeResponse.class))).thenReturn(employeeResponse);

        ResponseEntity<Integer> response = employeeService.getHighestSalaryOfEmployees();
        assertEquals(2000, response.getBody());
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        String jsonResponse =
                "{\"data\":[{\"id\":\"1\",\"employee_name\":\"John Doe\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"john.doe@example.com\"},{\"id\":\"2\",\"employee_name\":\"Jane Doe\",\"employee_salary\":2000,\"employee_age\":25,\"employee_title\":\"Manager\",\"employee_email\":\"jane.doe@example.com\"}],\"status\":\"Successfully processed request.\"}";
        EmployeeResponse employeeResponse = new EmployeeResponse();
        EmployeeDetailsResponse employeeDetails1 = new EmployeeDetailsResponse();
        employeeDetails1.setId("1");
        employeeDetails1.setEmployee_name("John Doe");
        employeeDetails1.setEmployee_salary(1000);
        employeeDetails1.setEmployee_age(30);
        employeeDetails1.setEmployee_title("Engineer");
        employeeDetails1.setEmployee_email("john.doe@example.com");
        EmployeeDetailsResponse employeeDetails2 = new EmployeeDetailsResponse();
        employeeDetails2.setId("2");
        employeeDetails2.setEmployee_name("Jane Doe");
        employeeDetails2.setEmployee_salary(2000);
        employeeDetails2.setEmployee_age(25);
        employeeDetails2.setEmployee_title("Manager");
        employeeDetails2.setEmployee_email("jane.doe@example.com");
        employeeResponse.setData(Arrays.asList(employeeDetails1, employeeDetails2));

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(EmployeeResponse.class))).thenReturn(employeeResponse);

        ResponseEntity<List<String>> response = employeeService.getTopTenHighestEarningEmployeeNames();
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Jane Doe", response.getBody().get(0));
        assertEquals("John Doe", response.getBody().get(1));
    }

    @Test
    void testGetEmployeesByNameSearch() throws Exception {
        String jsonResponse =
                "{\"data\":[{\"id\":\"1\",\"employee_name\":\"John Doe\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"john.doe@example.com\"},{\"id\":\"2\",\"employee_name\":\"Jane Doe\",\"employee_salary\":2000,\"employee_age\":25,\"employee_title\":\"Manager\",\"employee_email\":\"jane.doe@example.com\"}],\"status\":\"Successfully processed request.\"}";
        EmployeeResponse employeeResponse = new EmployeeResponse();
        EmployeeDetailsResponse employeeDetails1 = new EmployeeDetailsResponse();
        employeeDetails1.setId("1");
        employeeDetails1.setEmployee_name("John Doe");
        employeeDetails1.setEmployee_salary(1000);
        employeeDetails1.setEmployee_age(30);
        employeeDetails1.setEmployee_title("Engineer");
        employeeDetails1.setEmployee_email("john.doe@example.com");
        EmployeeDetailsResponse employeeDetails2 = new EmployeeDetailsResponse();
        employeeDetails2.setId("2");
        employeeDetails2.setEmployee_name("Jane Doe");
        employeeDetails2.setEmployee_salary(2000);
        employeeDetails2.setEmployee_age(25);
        employeeDetails2.setEmployee_title("Manager");
        employeeDetails2.setEmployee_email("jane.doe@example.com");
        employeeResponse.setData(Arrays.asList(employeeDetails1, employeeDetails2));

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(EmployeeResponse.class))).thenReturn(employeeResponse);

        ResponseEntity<List<Employee>> response = employeeService.getEmployeesByNameSearch("Jane");
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Jane Doe", response.getBody().get(0).getName());
    }
}
