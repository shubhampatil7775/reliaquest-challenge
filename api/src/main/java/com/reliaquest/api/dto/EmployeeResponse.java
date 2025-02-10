package com.reliaquest.api.model;

import java.util.List;

public class EmployeeResponse {
    private List<ApiEmployee> data;
    private String status;

    public List<ApiEmployee> getData() {
        return data;
    }

    public void setData(List<ApiEmployee> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}