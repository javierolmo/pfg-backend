package com.javi.uned.pfgbackend.adapters.api.sheets;

public class SheetDTO {

    private Integer id;
    private String name;
    private String date;
    private Long ownerId;
    private Boolean finished;
    private Boolean specs;
    private Boolean xml;
    private Boolean pdf;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Boolean getSpecs() {
        return specs;
    }

    public void setSpecs(Boolean specs) {
        this.specs = specs;
    }

    public Boolean getXml() {
        return xml;
    }

    public void setXml(Boolean xml) {
        this.xml = xml;
    }

    public Boolean getPdf() {
        return pdf;
    }

    public void setPdf(Boolean pdf) {
        this.pdf = pdf;
    }
}
