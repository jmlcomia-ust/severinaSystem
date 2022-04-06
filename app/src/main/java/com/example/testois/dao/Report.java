package com.example.testois.dao;

import android.graphics.Bitmap;

public class Report {
    private int ord_id;
    private String ord_description;
    private int inv_quantity;
    private int ord_quantity;

    public Report(int ord_id, String ord_description, int inv_quantity, int ord_quantity) {
        this.ord_id = ord_id;
        this.ord_description = ord_description;
        this.inv_quantity = inv_quantity;
        this.ord_quantity = ord_quantity;
    }

    public int getOrd_id() { return ord_id; }

    public void setOrd_id(int ord_id) { this.ord_id = ord_id; }

    public String getOrd_description() { return ord_description; }

    public void setOrd_description(String ord_description) { this.ord_description = ord_description; }

    public int getInv_quantity() { return inv_quantity; }

    public void setInv_quantity(int inv_quantity) { this.inv_quantity = inv_quantity; }

    public int getOrd_quantity() { return ord_quantity; }

    public void setOrd_quantity(int ord_quantity) { this.ord_quantity = ord_quantity; }
}
