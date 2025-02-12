package com.reliaquest.api.mapper;

import com.reliaquest.api.dto.GetAllEmployeeResponse;
import com.reliaquest.api.model.Employee;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static List<Employee> mapToEmployeeList(GetAllEmployeeResponse response) {
        return Arrays.stream(response.getData())
                .map(apiEmployee -> {
                    Employee employee = new Employee();
                    employee.setId(apiEmployee.getId());
                    employee.setName(apiEmployee.getEmployee_name());
                    employee.setSalary(apiEmployee.getEmployee_salary());
                    employee.setAge(apiEmployee.getEmployee_age());
                    employee.setTitle(apiEmployee.getEmployee_title());
                    employee.setEmail(apiEmployee.getEmployee_email());
                    return employee;
                })
                .collect(Collectors.toList());
    }
}
