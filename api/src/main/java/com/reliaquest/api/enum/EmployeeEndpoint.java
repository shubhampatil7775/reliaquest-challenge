package com.reliaquest.api.service;

public enum EmployeeEndpoint {
    GET_ALL_EMPLOYEES("/api/v1/employee"),
    GET_EMPLOYEES_BY_NAME_SEARCH("/api/v1/employee/search/"),
    GET_EMPLOYEE_BY_ID("/api/v1/employee/"),
    GET_HIGHEST_SALARY("/api/v1/employee/highestSalary"),
    GET_TOP_TEN_HIGHEST_EARNING_EMPLOYEE_NAMES("/api/v1/employee/topTenHighestEarningEmployeeNames"),
    CREATE_EMPLOYEE("/api/v1/employee"),
    DELETE_EMPLOYEE_BY_ID("/api/v1/employee");

    private final String path;

    EmployeeEndpoint(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
