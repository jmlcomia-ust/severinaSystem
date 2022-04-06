package com.example.testois.dao;

public class Orders {
    private int id;
    private String name;
    private int quantity;
    private String description;
    private String status;

    public Orders(String name, int quantity, String description, String status){
        this.name=name;
        this.quantity=quantity;
        this.description=description;
        this.status=status;
    }
    public Orders(int id, String name, int quantity, String description, String status){
        this.id=id;
        this.name=name;
        this.quantity=quantity;
        this.description=description;
        this.status=status;
    }

    public Orders(int id, String stat) {
        this.id=id;
        this.status=status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() { return description;}

    public void setDescription(String description) {this.description=description;}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}