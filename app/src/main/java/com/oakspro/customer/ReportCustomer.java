package com.oakspro.customer;

public class ReportCustomer {

    private String name, mobile, city, regdate;

    public ReportCustomer(){

    }

    public ReportCustomer(String name, String mobile, String city, String regdate) {
        this.name = name;
        this.mobile = mobile;
        this.city = city;
        this.regdate=regdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }
}
