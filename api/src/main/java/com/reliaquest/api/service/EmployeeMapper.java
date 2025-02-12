package com.reliaquest.api.service;

import com.reliaquest.api.dto.EmployeeDetailsResponse;
import com.reliaquest.api.dto.GetAllEmployeeResponse;
import com.reliaquest.api.model.Employee;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static List<Employee> mapToEmployeeList(GetAllEmployeeResponse response) {
        return Arrays.stream(response.getData())
                .map(EmployeeMapper::mapToEmployee)
                .collect(Collectors.toList());
    }

    public static Employee mapToEmployee(EmployeeDetailsResponse employeeDetails) {
        Employee employee = new Employee();
        employee.setId(employeeDetails.getId());
        employee.setName(employeeDetails.getEmployee_name());
        employee.setSalary(employeeDetails.getEmployee_salary());
        employee.setAge(employeeDetails.getEmployee_age());
        employee.setTitle(employeeDetails.getEmployee_title());
        employee.setEmail(employeeDetails.getEmployee_email());
        return employee;
    }
}
