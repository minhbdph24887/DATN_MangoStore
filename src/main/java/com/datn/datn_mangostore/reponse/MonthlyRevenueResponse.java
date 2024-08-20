package com.datn.datn_mangostore.reponse;

import java.util.Date;

public class MonthlyRevenueResponse {
    private Date date;
    private int month;
    private Integer totalRevenue;

    public MonthlyRevenueResponse(Date date,
                                  int month,
                                  Integer totalRevenue) {
        this.date = date;
        this.month = month;
        this.totalRevenue = totalRevenue;
    }

    public MonthlyRevenueResponse() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
