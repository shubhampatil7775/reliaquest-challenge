package com.reliaquest.api.mapper;

import com.reliaquest.api.dto.EmployeeDetailsResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.dto.EmployeeResponse;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static List<Employee> mapToEmployeeList(EmployeeResponse response) {
        return response.getData().stream().map(apiEmployee -> {
            Employee employee = new Employee();
            employee.setId(apiEmployee.getId());
            employee.setName(apiEmployee.getEmployee_name());
            employee.setSalary(apiEmployee.getEmployee_salary());
            employee.setAge(apiEmployee.getEmployee_age());
            employee.setTitle(apiEmployee.getEmployee_title());
            employee.setEmail(apiEmployee.getEmployee_email());
            return employee;
        }).collect(Collectors.toList());
    }
}