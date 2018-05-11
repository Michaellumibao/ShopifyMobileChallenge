package com.lumibao.shopifymobilechallenge;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by micha on 2018-05-03.
 */

public class OrderModel {
    private String province;
    private int year;
    private String email;
    private int orderNumber;

    public static OrderModel fromJson(JSONObject jsonObject) {
        OrderModel orderData = new OrderModel();

        try {
            orderData.province = jsonObject.getJSONObject("shipping_address").getString("province");
            String yearString = jsonObject.getString("created_at");
            orderData.year = Integer.parseInt(yearString.substring(0, 4));
            orderData.email = jsonObject.getString("contact_email");
            orderData.orderNumber = jsonObject.getInt("id");
            Log.i("app", orderData.province);
        } catch (JSONException e) {
            e.toString();
            return null;
        }
        return orderData;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String toString() {
        return "id: " + Integer.toString(orderNumber) + ", province: " + province
                + ", year: " + Integer.toString(year) + ", email: " + email;
    }

}
