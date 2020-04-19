package com.autodoc.pfa.pfaautodoc.models;

import java.util.ArrayList;
import java.util.List;

public abstract class FileSubstitution {
    private String dealNumber;
    private String dealDate;
    private String basis;
    private String listenerCred;
    private String listenerCredRodPad;
    private String listenerCredDatPad;
    private String dateOfBirth;
    private String listenerEducationType;
    private String listenerDiplomaData;
    private String listenerSpeciality;
    private String listenerDiplomaDate;
    private String passSerNum;
    private String passDelData;
    private String regAddress;
    private String programType;
    private String programName;
    private String latency;
    private String cost;
    private String educationStartTime;
    private String educationFinishTime;
    private String billNumber;
    private String billDate;
    private String billSum;
    private String billSumWords;
    private String actNumber;
    private String actDate;
    private String dateOfForm;
    private String socialId;
    private String stampAndSign;
    private ArrayList<String> filesToModify;
    private Boolean needsStampAndSign;
    private Boolean needsConversionToPdf;

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

    public String getDealNumber() {
        return dealNumber;
    }

    public void setDealNumber(String dealNumber) {
        this.dealNumber = dealNumber;
    }

    public String getDealDate() {
        return dealDate;
    }

    public void setDealDate(String dealDate) {
        this.dealDate = dealDate;
    }

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
    }

    public String getListenerCred() {
        return listenerCred;
    }

    public void setListenerCred(String listenerCred) {
        this.listenerCred = listenerCred;
    }

    public String getListenerCredRodPad() {
        return listenerCredRodPad;
    }

    public void setListenerCredRodPad(String listenerCredRodPad) {
        this.listenerCredRodPad = listenerCredRodPad;
    }

    public String getProgramType() {
        return programType;
    }

    public void setProgramType(String programType) {
        this.programType = programType;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillSum() {
        return billSum;
    }

    public void setBillSum(String billSum) {
        this.billSum = billSum;
    }

    public String getBillSumWords() {
        return billSumWords;
    }

    public void setBillSumWords(String billSumWords) {
        this.billSumWords = billSumWords;
    }

    public String getActDate() {
        return actDate;
    }

    public void setActDate(String actDate) {
        this.actDate = actDate;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getActNumber() {
        return actNumber;
    }

    public void setActNumber(String actNumber) {
        this.actNumber = actNumber;
    }

    public String getDateOfForm() {
        return dateOfForm;
    }

    public void setDateOfForm(String dateOfForm) {
        this.dateOfForm = dateOfForm;
    }

    public String getStampAndSign() {
        return stampAndSign;
    }

    public void setStampAndSign(String stampAndSign) {
        this.stampAndSign = stampAndSign;
    }

    public String getListenerCredDatPad() {
        return listenerCredDatPad;
    }

    public void setListenerCredDatPad(String listenerCredDatPad) {
        this.listenerCredDatPad = listenerCredDatPad;
    }

    public String getListenerEducationType() {
        return listenerEducationType;
    }

    public void setListenerEducationType(String listenerEducationType) {
        this.listenerEducationType = listenerEducationType;
    }

    public String getListenerDiplomaData() {
        return listenerDiplomaData;
    }

    public void setListenerDiplomaData(String listenerDiplomaData) {
        this.listenerDiplomaData = listenerDiplomaData;
    }

    public String getListenerSpeciality() {
        return listenerSpeciality;
    }

    public void setListenerSpeciality(String listenerSpeciality) {
        this.listenerSpeciality = listenerSpeciality;
    }

    public String getListenerDiplomaDate() {
        return listenerDiplomaDate;
    }

    public void setListenerDiplomaDate(String listenerDiplomaDate) {
        this.listenerDiplomaDate = listenerDiplomaDate;
    }

    public String getEducationStartTime() {
        return educationStartTime;
    }

    public void setEducationStartTime(String educationStartTime) {
        this.educationStartTime = educationStartTime;
    }

    public String getEducationFinishTime() {
        return educationFinishTime;
    }

    public void setEducationFinishTime(String educationFinishTime) {
        this.educationFinishTime = educationFinishTime;
    }

    public Boolean getNeedsStampAndSign() {
        return needsStampAndSign;
    }

    public void setNeedsStampAndSign(Boolean needsStampAndSign) {
        this.needsStampAndSign = needsStampAndSign;
    }

    public Boolean getNeedsConversionToPdf() {
        return needsConversionToPdf;
    }

    public void setNeedsConversionToPdf(Boolean needsConversionToPdf) {
        this.needsConversionToPdf = needsConversionToPdf;
    }

    public List<String> getFilesToModify() {
        return filesToModify;
    }

    public void setFilesToModify(ArrayList<String> filesToModify) {
        this.filesToModify = filesToModify;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }
}
