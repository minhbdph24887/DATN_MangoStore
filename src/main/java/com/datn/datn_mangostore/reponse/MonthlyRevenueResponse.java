package com.datn.datn_mangostore.reponse;

public class MonthlyRevenueResponse {
    private int month;
    private Integer totalRevenue;

    public MonthlyRevenueResponse(int month, Integer totalRevenue) {
        this.month = month;
        this.totalRevenue = totalRevenue;
    }

    public MonthlyRevenueResponse() {
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Integer getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Integer totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
