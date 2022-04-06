package com.example.testois.dao;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Inventory implements Serializable {
        private int id;
        private String name;
        private int quantity;
        private String description;
        private int threshold;
        private Bitmap image;

        public Inventory(String name, int quantity, String description, int threshold,Bitmap image) {
                this.name = name;
                this.quantity = quantity;
                this.description = description;
                this.threshold = threshold;
                this.image = image;
        }
        public Inventory(int id, String name, int quantity, String description, int threshold, Bitmap image) {
                this.id = id;
                this.name = name;
                this.quantity = quantity;
                this.description = description;
            this.threshold = threshold;
                this.image = image;
        }
    public Inventory(int id, String name, int quantity, String description, int threshold) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.description = description;
        this.threshold = threshold;
    }
    public Inventory(String name, int quantity, String description, int threshold) {
        this.name = name;
        this.quantity = quantity;
        this.description = description;
        this.threshold = threshold;
    }

        public int getId() { return id; }
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

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public int getThreshold(){return threshold;}

        public void setThreshold(int threshold) {this.threshold = threshold;}

        public Bitmap getImage() { return image; }

        public void setImage(Bitmap image) { this.image = image; }
}
