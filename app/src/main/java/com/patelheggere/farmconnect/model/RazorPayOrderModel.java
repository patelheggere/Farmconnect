package com.patelheggere.farmconnect.model;

public class RazorPayOrderModel {
    private String id;
    private String entity;
    private int amount;
    private int amount_paid;
    private String receipt;
    private String status;
    private long created_at;

    public RazorPayOrderModel() {

    }

    public RazorPayOrderModel(String id, String entity, int amount, int amount_paid, String receipt, String status, long created_at) {
        this.id = id;
        this.entity = entity;
        this.amount = amount;
        this.amount_paid = amount_paid;
        this.receipt = receipt;
        this.status = status;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(int amount_paid) {
        this.amount_paid = amount_paid;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
