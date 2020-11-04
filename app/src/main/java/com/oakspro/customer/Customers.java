package com.oakspro.customer;

public class Customers {
    private String name, mobile, address, city, mother_name, gender, email, nationalid, userid;

    public Customers(){

    }

    public Customers(String name, String mobile, String address, String city, String mother_name, String gender, String email, String nationalid, String userid) {
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.city = city;
        this.mother_name = mother_name;
        this.gender = gender;
        this.email = email;
        this.nationalid = nationalid;
        this.userid=userid;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalid() {
        return nationalid;
    }

    public void setNationalid(String nationalid) {
        this.nationalid = nationalid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
