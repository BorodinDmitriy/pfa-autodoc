package com.autodoc.pfa.pfaautodoc.models;

public class FileTemplate {
    private Integer id;
    private String name;
    private String path;
    private String type;
    private Boolean isSigned;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSigned() {
        return isSigned;
    }

    public void setSigned(Boolean signed) {
        isSigned = signed;
    }
}
