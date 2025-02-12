package com.reliaquest.api.dto;

public class GetSpecificEmployeeResponse {
    private EmployeeDetailsResponse data;
    private String status;

    public EmployeeDetailsResponse getData() {
        return data;
    }

    public void setData(EmployeeDetailsResponse data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
