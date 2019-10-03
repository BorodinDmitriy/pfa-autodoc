package com.autodoc.pfa.pfaautodoc.models;

import java.util.ArrayList;
import java.util.List;

public abstract class FileSubstitution {
    private String dealNumber;
    private String dealDate;
    private String basis;
    private String listenerCred;
    private String listenerCredRodPad;
    private String programType;
    private String programName;
    private String latency;
    private String cost;
    private String billNumber;
    private String billDate;
    private String billSum;
    private String billSumWords;
    private String actNumber;
    private String actDate;
    private String stampAndSign;
    private ArrayList<String> filesToModify;
    private Boolean needsStampAndSign;
    private Boolean needsConversionToPdf;

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

    public String getStampAndSign() {
        return stampAndSign;
    }

    public void setStampAndSign(String stampAndSign) {
        this.stampAndSign = stampAndSign;
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
}