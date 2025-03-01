package com.order.dto.restaurant;

import java.util.List;

public class RestaurantResponse {
    private Long id;
    private String name;
    private String city;
    private String address;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private List<MenuItemResponse> menuItems;

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

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "RestaurantResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", menuItems=" + menuItems +
                '}';
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
