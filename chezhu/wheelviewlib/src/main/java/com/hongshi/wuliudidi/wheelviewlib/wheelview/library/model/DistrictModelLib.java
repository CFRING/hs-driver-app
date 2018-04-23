package com.hongshi.wuliudidi.wheelviewlib.wheelview.library.model;

public class DistrictModelLib {
    private String name;
    private String id;

    private String zipcode;

    public DistrictModelLib() {
        super();
    }

    public DistrictModelLib(String name, String id, String zipcode) {
        super();
        this.name = name;
        this.id = id;
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
