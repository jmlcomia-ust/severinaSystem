package com.example.testois;

public class Inventory {
        private String id;
        private String name;
        private String quantity;

        public Inventory(String name, String quantity, String description) {
                this.name = name;
                this.quantity = quantity;
                this.description = description;
        }
        public Inventory(String id, String name, String quantity, String description) {
                this.id = id;
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

        private String description;
}
