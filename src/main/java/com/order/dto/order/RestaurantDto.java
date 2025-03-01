package com.order.dto.order;

import com.order.dto.restaurant.RestaurantResponse;

public class RestaurantDto {
    private Long id;
    private String name;
    private String city;
    private String address;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;

    public RestaurantDto(RestaurantResponse restaurantResponse) {
        this.id = restaurantResponse.getId();
        this.name = restaurantResponse.getName();
        this.city = restaurantResponse.getCity();
        this.address = restaurantResponse.getAddress();
        this.phoneNumber = restaurantResponse.getPhoneNumber();
        this.latitude = restaurantResponse.getLatitude();
        this.longitude = restaurantResponse.getLongitude();
    }

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
