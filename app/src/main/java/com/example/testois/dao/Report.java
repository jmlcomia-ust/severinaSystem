package com.example.testois.dao;

import android.graphics.Bitmap;

public class Report {
    private int ord_id;
    private String ord_status;
    private int inv_quantity;
    private int ord_quantity;

    public Report(int ord_id, String ord_status, int inv_quantity, int ord_quantity) {
        this.ord_id = ord_id;
        this.ord_status = ord_status;
        this.inv_quantity = inv_quantity;
        this.ord_quantity = ord_quantity;
    }

    public int getOrd_id() { return ord_id; }

    public void setOrd_id(int ord_id) { this.ord_id = ord_id; }

    public String getOrd_status() { return ord_status; }

    public void setOrd_status(String ord_status) { this.ord_status = ord_status; }

    public int getInv_quantity() { return inv_quantity; }

    public void setInv_quantity(int inv_quantity) { this.inv_quantity = inv_quantity; }

    public int getOrd_quantity() { return ord_quantity; }

    public void setOrd_quantity(int ord_quantity) { this.ord_quantity = ord_quantity; }
}
