package com.patelheggere.farmconnect.model;

public class AssemblyModel {
    private String assembly_id;
    private String name;
    private String assem_name_kn;
    private String taluk;
    private String district;
    private String dist_id;
    private String tlk_id;
    private String created_on;
    private String updated_on;

    public AssemblyModel() {
    }

    public AssemblyModel(String assembly_id, String name, String assem_name_kn, String taluk, String district, String dist_id, String tlk_id, String created_on, String updated_on) {
        this.assembly_id = assembly_id;
        this.name = name;
        this.assem_name_kn = assem_name_kn;
        this.taluk = taluk;
        this.district = district;
        this.dist_id = dist_id;
        this.tlk_id = tlk_id;
        this.created_on = created_on;
        this.updated_on = updated_on;
    }

    public String getAssembly_id() {
        return assembly_id;
    }

    public void setAssembly_id(String assembly_id) {
        this.assembly_id = assembly_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssem_name_kn() {
        return assem_name_kn;
    }

    public void setAssem_name_kn(String assem_name_kn) {
        this.assem_name_kn = assem_name_kn;
    }

    public String getTaluk() {
        return taluk;
    }

    public void setTaluk(String taluk) {
        this.taluk = taluk;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDist_id() {
        return dist_id;
    }

    public void setDist_id(String dist_id) {
        this.dist_id = dist_id;
    }

    public String getTlk_id() {
        return tlk_id;
    }

    public void setTlk_id(String tlk_id) {
        this.tlk_id = tlk_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }
}