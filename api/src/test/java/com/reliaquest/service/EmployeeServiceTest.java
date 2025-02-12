package com.reliaquest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.dto.DeleteEmployeeResponse;
import com.reliaquest.api.dto.EmployeeDetailsResponse;
import com.reliaquest.api.dto.GetAllEmployeeResponse;
import com.reliaquest.api.dto.GetSpecificEmployeeResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
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
                "{\"data\":[{\"id\":\"1\",\"employee_name\":\"Shubham Patil\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"shubham.patil@gmail.com\"}],\"status\":\"Successfully processed request.\"}";
        GetAllEmployeeResponse response = new GetAllEmployeeResponse();
        EmployeeDetailsResponse employeeDetails = new EmployeeDetailsResponse();
        employeeDetails.setId("1");
        employeeDetails.setEmployee_name("Shubham Patil");
        employeeDetails.setEmployee_salary(1000);
        employeeDetails.setEmployee_age(30);
        employeeDetails.setEmployee_title("Engineer");
        employeeDetails.setEmployee_email("shubham.patil@gmail.com");
        response.setData(new EmployeeDetailsResponse[] {employeeDetails});

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetAllEmployeeResponse.class)))
                .thenReturn(response);

        List<Employee> employees = employeeService.getAllEmployees();
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Shubham Patil", employees.get(0).getName());
    }

    @Test
    void testGetAllEmployeesEmpty() throws Exception {
        String jsonResponse = "{\"data\":[],\"status\":\"Successfully processed request.\"}";
        GetAllEmployeeResponse response = new GetAllEmployeeResponse();
        response.setData(new EmployeeDetailsResponse[] {});

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetAllEmployeeResponse.class)))
                .thenReturn(response);

        List<Employee> employees = employeeService.getAllEmployees();
        assertNotNull(employees);
        assertEquals(0, employees.size());
    }

    @Test
    void testGetEmployeesByNameSearch() throws Exception {
        String jsonResponse =
                "{\"data\":[{\"id\":\"1\",\"employee_name\":\"Shubham Patil\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"shubham.patil@gmail.com\"}],\"status\":\"Successfully processed request.\"}";
        GetAllEmployeeResponse response = new GetAllEmployeeResponse();
        EmployeeDetailsResponse employeeDetails = new EmployeeDetailsResponse();
        employeeDetails.setId("1");
        employeeDetails.setEmployee_name("Shubham Patil");
        employeeDetails.setEmployee_salary(1000);
        employeeDetails.setEmployee_age(30);
        employeeDetails.setEmployee_title("Engineer");
        employeeDetails.setEmployee_email("shubham.patil@gmail.com");
        response.setData(new EmployeeDetailsResponse[] {employeeDetails});

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetAllEmployeeResponse.class)))
                .thenReturn(response);

        List<Employee> employees = employeeService.getEmployeesByNameSearch("Shubham");
        assertNotNull(employees);
        assertEquals(1, employees.size());
        assertEquals("Shubham Patil", employees.get(0).getName());
    }

    @Test
    void testGetEmployeesByNameSearchNotFound() throws Exception {
        String jsonResponse = "{\"data\":[],\"status\":\"Successfully processed request.\"}";
        GetAllEmployeeResponse response = new GetAllEmployeeResponse();
        response.setData(new EmployeeDetailsResponse[] {});

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetAllEmployeeResponse.class)))
                .thenReturn(response);

        List<Employee> employees = employeeService.getEmployeesByNameSearch("Shubham");
        assertNull(employees);
    }

    @Test
    void testGetEmployeeById() throws Exception {
        String id = "1";
        String jsonResponse =
                "{\"data\":{\"id\":\"1\",\"employee_name\":\"Shubham Patil\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"shubham.patil@gmail.com\"},\"status\":\"Successfully processed request.\"}";
        GetSpecificEmployeeResponse response = new GetSpecificEmployeeResponse();
        EmployeeDetailsResponse employeeDetails = new EmployeeDetailsResponse();
        employeeDetails.setId(id);
        employeeDetails.setEmployee_name("Shubham Patil");
        employeeDetails.setEmployee_salary(1000);
        employeeDetails.setEmployee_age(30);
        employeeDetails.setEmployee_title("Engineer");
        employeeDetails.setEmployee_email("shubham.patil@gmail.com");
        response.setData(employeeDetails);

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetSpecificEmployeeResponse.class)))
                .thenReturn(response);

        Employee employee = employeeService.getEmployeeById(id);
        assertNotNull(employee);
        assertEquals(id, employee.getId());
        assertEquals("Shubham Patil", employee.getName());
    }

    @Test
    void testGetEmployeeByIdNotFound() {
        String id = "1";

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Employee employee = employeeService.getEmployeeById(id);
        assertNull(employee);
    }

    @Test
    void testGetHighestSalaryOfEmployees() throws Exception {
        String jsonResponse =
                "{\"data\":[{\"id\":\"1\",\"employee_name\":\"Shubham Patil\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"shubham.patil@gmail.com\"},{\"id\":\"2\",\"employee_name\":\"Jane Doe\",\"employee_salary\":2000,\"employee_age\":25,\"employee_title\":\"Manager\",\"employee_email\":\"jane.doe@example.com\"}],\"status\":\"Successfully processed request.\"}";
        GetAllEmployeeResponse response = new GetAllEmployeeResponse();
        EmployeeDetailsResponse employeeDetails1 = new EmployeeDetailsResponse();
        employeeDetails1.setId("1");
        employeeDetails1.setEmployee_name("Shubham Patil");
        employeeDetails1.setEmployee_salary(1000);
        employeeDetails1.setEmployee_age(30);
        employeeDetails1.setEmployee_title("Engineer");
        employeeDetails1.setEmployee_email("shubham.patil@gmail.com");
        EmployeeDetailsResponse employeeDetails2 = new EmployeeDetailsResponse();
        employeeDetails2.setId("2");
        employeeDetails2.setEmployee_name("Jane Doe");
        employeeDetails2.setEmployee_salary(2000);
        employeeDetails2.setEmployee_age(25);
        employeeDetails2.setEmployee_title("Manager");
        employeeDetails2.setEmployee_email("jane.doe@example.com");
        response.setData(new EmployeeDetailsResponse[] {employeeDetails1, employeeDetails2});

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetAllEmployeeResponse.class)))
                .thenReturn(response);

        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
        assertEquals(2000, highestSalary);
    }

    @Test
    void testGetHighestSalaryOfEmployeesNoEmployees() throws Exception {
        String jsonResponse = "{\"data\":[],\"status\":\"Successfully processed request.\"}";
        GetAllEmployeeResponse response = new GetAllEmployeeResponse();
        response.setData(new EmployeeDetailsResponse[] {});

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetAllEmployeeResponse.class)))
                .thenReturn(response);

        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
        assertEquals(0, highestSalary);
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        String jsonResponse =
                "{\"data\":[{\"id\":\"1\",\"employee_name\":\"Shubham Patil\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"shubham.patil@gmail.com\"},{\"id\":\"2\",\"employee_name\":\"Jane Doe\",\"employee_salary\":2000,\"employee_age\":25,\"employee_title\":\"Manager\",\"employee_email\":\"jane.doe@example.com\"}],\"status\":\"Successfully processed request.\"}";
        GetAllEmployeeResponse response = new GetAllEmployeeResponse();
        EmployeeDetailsResponse employeeDetails1 = new EmployeeDetailsResponse();
        employeeDetails1.setId("1");
        employeeDetails1.setEmployee_name("Shubham Patil");
        employeeDetails1.setEmployee_salary(1000);
        employeeDetails1.setEmployee_age(30);
        employeeDetails1.setEmployee_title("Engineer");
        employeeDetails1.setEmployee_email("shubham.patil@gmail.com");
        EmployeeDetailsResponse employeeDetails2 = new EmployeeDetailsResponse();
        employeeDetails2.setId("2");
        employeeDetails2.setEmployee_name("Jane Doe");
        employeeDetails2.setEmployee_salary(2000);
        employeeDetails2.setEmployee_age(25);
        employeeDetails2.setEmployee_title("Manager");
        employeeDetails2.setEmployee_email("jane.doe@example.com");
        response.setData(new EmployeeDetailsResponse[] {employeeDetails1, employeeDetails2});

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetAllEmployeeResponse.class)))
                .thenReturn(response);

        List<String> topTenHighestEarningEmployeeNames = employeeService.getTopTenHighestEarningEmployeeNames();
        assertNotNull(topTenHighestEarningEmployeeNames);
        assertEquals(2, topTenHighestEarningEmployeeNames.size());
        assertEquals("Jane Doe", topTenHighestEarningEmployeeNames.get(0));
        assertEquals("Shubham Patil", topTenHighestEarningEmployeeNames.get(1));
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNamesNoEmployees() throws Exception {
        String jsonResponse = "{\"data\":[],\"status\":\"Successfully processed request.\"}";
        GetAllEmployeeResponse response = new GetAllEmployeeResponse();
        response.setData(new EmployeeDetailsResponse[] {});

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetAllEmployeeResponse.class)))
                .thenReturn(response);

        List<String> topTenHighestEarningEmployeeNames = employeeService.getTopTenHighestEarningEmployeeNames();
        assertNotNull(topTenHighestEarningEmployeeNames);
        assertEquals(0, topTenHighestEarningEmployeeNames.size());
    }

    @Test
    void testCreateEmployee() throws Exception {
        CreateEmployeeRequest input = new CreateEmployeeRequest();
        input.setName("Shubham Patil");
        input.setTitle("Engineer");
        input.setSalary(1000);
        input.setAge(30);
        input.setEmail("shubham.patil@gmail.com");

        GetSpecificEmployeeResponse response = new GetSpecificEmployeeResponse();
        EmployeeDetailsResponse employeeDetails = new EmployeeDetailsResponse();
        employeeDetails.setId("1");
        employeeDetails.setEmployee_name("Shubham Patil");
        employeeDetails.setEmployee_salary(1000);
        employeeDetails.setEmployee_age(30);
        employeeDetails.setEmployee_title("Engineer");
        employeeDetails.setEmployee_email("shubham.patil@gmail.com");
        response.setData(employeeDetails);

        when(restTemplate.postForObject(anyString(), any(), eq(GetSpecificEmployeeResponse.class)))
                .thenReturn(response);

        Employee employee = employeeService.createEmployee(input);
        assertNotNull(employee);
        assertEquals("Shubham Patil", employee.getName());
    }

    @Test
    void testCreateEmployeeFailure() throws Exception {
        CreateEmployeeRequest input = new CreateEmployeeRequest();
        input.setName("Shubham Patil");
        input.setTitle("Engineer");
        input.setSalary(1000);
        input.setAge(30);
        input.setEmail("shubham.patil@gmail.com");

        when(restTemplate.postForObject(anyString(), any(), eq(GetSpecificEmployeeResponse.class)))
                .thenReturn(null);

        Employee employee = employeeService.createEmployee(input);
        assertNull(employee);
    }

    @Test
    void testDeleteEmployeeById() throws Exception {
        String id = "1";
        String jsonResponse =
                "{\"data\":{\"id\":\"1\",\"employee_name\":\"Shubham Patil\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"shubham.patil@gmail.com\"},\"status\":\"Successfully processed request.\"}";
        GetSpecificEmployeeResponse getResponse = new GetSpecificEmployeeResponse();
        EmployeeDetailsResponse employeeDetails = new EmployeeDetailsResponse();
        employeeDetails.setId(id);
        employeeDetails.setEmployee_name("Shubham Patil");
        employeeDetails.setEmployee_salary(1000);
        employeeDetails.setEmployee_age(30);
        employeeDetails.setEmployee_title("Engineer");
        employeeDetails.setEmployee_email("shubham.patil@gmail.com");
        getResponse.setData(employeeDetails);

        String deleteJsonResponse = "{\"data\":true,\"status\":\"Successfully processed request.\"}";
        DeleteEmployeeResponse deleteResponse = new DeleteEmployeeResponse();
        deleteResponse.setData(true);

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetSpecificEmployeeResponse.class)))
                .thenReturn(getResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(deleteJsonResponse, HttpStatus.OK));
        when(objectMapper.readValue(anyString(), eq(DeleteEmployeeResponse.class)))
                .thenReturn(deleteResponse);

        String result = employeeService.deleteEmployeeById(id);
        assertEquals("Employee deleted successfully", result);
    }

    @Test
    void testDeleteEmployeeByIdNotFound() {
        String id = "1";

        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        String result = employeeService.deleteEmployeeById(id);
        assertEquals("Employee not found", result);
    }

    @Test
    void testDeleteEmployeeByIdFailure() throws Exception {
        String id = "1";
        String jsonResponse =
                "{\"data\":{\"id\":\"1\",\"employee_name\":\"Shubham Patil\",\"employee_salary\":1000,\"employee_age\":30,\"employee_title\":\"Engineer\",\"employee_email\":\"shubham.patil@gmail.com\"},\"status\":\"Successfully processed request.\"}";
        GetSpecificEmployeeResponse getResponse = new GetSpecificEmployeeResponse();
        EmployeeDetailsResponse employeeDetails = new EmployeeDetailsResponse();
        employeeDetails.setId(id);
        employeeDetails.setEmployee_name("Shubham Patil");
        employeeDetails.setEmployee_salary(1000);
        employeeDetails.setEmployee_age(30);
        employeeDetails.setEmployee_title("Engineer");
        employeeDetails.setEmployee_email("shubham.patil@gmail.com");
        getResponse.setData(employeeDetails);

        String deleteJsonResponse = "{\"data\":false,\"status\":\"Successfully processed request.\"}";
        DeleteEmployeeResponse deleteResponse = new DeleteEmployeeResponse();
        deleteResponse.setData(false);

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(jsonResponse);
        when(objectMapper.readValue(anyString(), eq(GetSpecificEmployeeResponse.class)))
                .thenReturn(getResponse);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(deleteJsonResponse, HttpStatus.OK));
        when(objectMapper.readValue(anyString(), eq(DeleteEmployeeResponse.class)))
                .thenReturn(deleteResponse);

        String result = employeeService.deleteEmployeeById(id);
        assertEquals("Failed to delete employee", result);
    }
}
