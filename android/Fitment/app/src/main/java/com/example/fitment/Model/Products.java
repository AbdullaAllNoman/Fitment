package com.example.fitment.Model;

public class Products
{
    // pname(Product name)
    // pid(Product id)
    // model3dUrl(url for augmented view saved in DB)
    private String pname2,description,price,image,category,pid,date,time,model3dUrl;

    public Products() {

    }

    public Products(String pname, String description, String price, String image, String category, String pid, String date, String time, String model3dUrl) {
        this.pname2 = pname;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.model3dUrl = model3dUrl;
    }

    public String getPname2() {
        return pname2;
    }

    public void setPname(String pname2) {
        this.pname2 = pname2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getModel3dUrl() {
        return model3dUrl;
    }

    public void setModel3dUrl(String model3dUrl) {
        this.model3dUrl = model3dUrl;
    }
}
