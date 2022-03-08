package com.example.testois;

public class Orders {
    private String id;
    private String name;
    private String quantity;
    private String description;
    private String status;

    public Orders(String name, String quantity, String status){
        this.name=name;
        this.quantity=quantity;
        this.status=status;
    }
    public Orders(String id, String name, String quantity, String status){
        this.id=id;
        this.name=name;
        this.quantity=quantity;
        this.status=status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
