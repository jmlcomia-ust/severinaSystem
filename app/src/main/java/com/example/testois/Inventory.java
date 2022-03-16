package com.example.testois;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Inventory implements Serializable {
        private String id;
        private String name;
        private String quantity;
        private String description;
        private byte[] image;
/*
        public Inventory(String name, String quantity, String description, byte[] image) {
                this.name = name;
                this.quantity = quantity;
                this.description = description;
                this.image = image;
        }
        public Inventory(String id, String name, String quantity, String description, byte[] image) {
                this.id = id;
                this.name = name;
                this.quantity = quantity;
                this.description = description;
                this.image = image;
        }

 */
    public Inventory(String id, String name, String quantity, String description) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.description = description;
    }
    public Inventory(String name, String quantity, String description) {
        this.name = name;
        this.quantity = quantity;
        this.description = description;
    }

    public Inventory() {

    }

        public String getId() { return id; }
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

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public byte[] getImage() { return image; }

        public void setImage(byte[] image) { this.image = image; }
}
