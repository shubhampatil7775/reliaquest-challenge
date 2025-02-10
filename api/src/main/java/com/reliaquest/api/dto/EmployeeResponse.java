package com.reliaquest.api.dto;

import java.util.List;

public class EmployeeResponse {
    private List<EmployeeDetailsResponse> data;
    private String status;

    public List<EmployeeDetailsResponse> getData() {
        return data;
    }

    public void setData(List<EmployeeDetailsResponse> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}