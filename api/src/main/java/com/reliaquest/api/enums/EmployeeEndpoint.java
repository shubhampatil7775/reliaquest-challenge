package com.reliaquest.api.enums;

public enum EmployeeEndpoint {
    GET_ALL_EMPLOYEES("/api/v1/employee"),
    GET_EMPLOYEE_BY_ID("/api/v1/employee/"),
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
