package com.example.testois;

import android.widget.ImageView;

public class Inventory {
     String id;
     String name;
     ImageView imgStock;
     String quantity;
     String description;

        public Inventory() {

        }
    //getters and setter


        public String getId() { return id; }

        public void setId(String id) { this.id = id; }

        public String getName() {
                    return name;
            }

        public void setName(String name) {
                this.name = name;
        }

        public ImageView getImgStock() {
                return imgStock;
        }

        public void setImgStock(ImageView imgStock) {
                this.imgStock = imgStock;
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
}
