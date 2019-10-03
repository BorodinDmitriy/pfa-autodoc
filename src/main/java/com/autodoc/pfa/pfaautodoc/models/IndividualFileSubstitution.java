package com.autodoc.pfa.pfaautodoc.models;

public class IndividualFileSubstitution extends FileSubstitution {

    private String listenerCredentials;
    private String dateOfBirth;
    private String passportData;
    private String passportDeliveryData;
    private String registrationAddress;
    private String customer;


    public String getListenerCredentials() {
        return listenerCredentials;
    }

    public void setListenerCredentials(String listenerCredentials) {
        this.listenerCredentials = listenerCredentials;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassportData() {
        return passportData;
    }

    public void setPassportData(String passportData) {
        this.passportData = passportData;
    }

    public String getPassportDeliveryData() {
        return passportDeliveryData;
    }

    public void setPassportDeliveryData(String passportDeliveryData) {
        this.passportDeliveryData = passportDeliveryData;
    }

    public String getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress(String registrationAddress) {
        this.registrationAddress = registrationAddress;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
