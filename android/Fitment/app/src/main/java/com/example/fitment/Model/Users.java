package com.example.fitment.Model;

import com.google.firebase.auth.FirebaseAuth;

public class Users {

    private String userName, name,Phone,password, image, address, email;

    public Users()
    {

    }

    public Users(String userName, String name, String phone, String password, String image, String address, String email) {
        this.userName = userName;
        this.name = name;
        Phone = phone;
        this.password = password;
        this.image = image;
        this.address = address;
        this.email = email;
    }

    public Users(String name, String phone, String password, String email) {
        this.name = name;
        Phone = phone;
        this.password = password;
        this.email = email;
    }

    public String getUserName() {
        //return userName;
        // return unique id of firebase authentication
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
