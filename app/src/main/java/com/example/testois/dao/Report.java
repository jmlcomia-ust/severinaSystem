package com.example.testois.dao;

public class Report {
    private int ord_id;
    private String ord_date;
    private String inv_name;
    private int inv_quantity;
    private int ord_quantity;
    public Report(int ord_id, String ord_date, String inv_name, int inv_quantity, int ord_quantity) {
        this.ord_id = ord_id;
        this.ord_date = ord_date;
        this.inv_quantity = inv_quantity;
        this.ord_quantity = ord_quantity;
    }
    public Report(String ord_date, String inv_name, int inv_quantity, int ord_quantity) {
        this.ord_date = ord_date;
        this.inv_quantity = inv_quantity;
        this.ord_quantity = ord_quantity;
    }
    public Report(String ord_date) {
        this.ord_date = ord_date;
    }

    public int getOrd_id() { return ord_id; }

    public void setOrd_id(int ord_id) { this.ord_id = ord_id; }

    public String getOrd_status() { return ord_date; }

    public void setOrd_status(String ord_status) { this.ord_date = ord_status; }

    public String getInv_name() {return inv_name;}

    public void setInv_name(String inv_name) {this.inv_name = inv_name;}

    public int getInv_quantity() { return inv_quantity; }

    public void setInv_quantity(int inv_quantity) { this.inv_quantity = inv_quantity; }

    public int getOrd_quantity() { return ord_quantity; }

    public void setOrd_quantity(int ord_quantity) { this.ord_quantity = ord_quantity; }
}
