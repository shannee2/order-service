package com.order.dto.restaurant;

import java.util.List;

public class RestaurantResponse {
    private Long id;
    private String name;
    private List<MenuItemResponse> menuItems;

    @Override
    public String toString() {
        return "RestaurantResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", menuItems=" + menuItems +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<MenuItemResponse> getMenuItems() {
        return menuItems;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMenuItems(List<MenuItemResponse> menuItems) {
        this.menuItems = menuItems;
    }


    public static class MenuItemResponse {
        private Long id;
        private String name;
        private double amount;
        private String currency;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getAmount() {
            return amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}
