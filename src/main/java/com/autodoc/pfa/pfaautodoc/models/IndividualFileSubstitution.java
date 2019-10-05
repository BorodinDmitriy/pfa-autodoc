package com.autodoc.pfa.pfaautodoc.models;

public class IndividualFileSubstitution extends FileSubstitution {

    private String dateOfBirth;
    private String passSerNum;
    private String passDelData;
    private String regAddress;
    private String customer;

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassSerNum() {
        return passSerNum;
    }

    public void setPassSerNum(String passSerNum) {
        this.passSerNum = passSerNum;
    }

    public String getPassDelData() {
        return passDelData;
    }

    public void setPassDelData(String passDelData) {
        this.passDelData = passDelData;
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
