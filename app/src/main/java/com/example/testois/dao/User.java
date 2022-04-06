package com.example.testois.dao;

public class User {
        String id;
        String name = "owner.severina@gmail.com";
        String password = "owner.severina";

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

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }
}
