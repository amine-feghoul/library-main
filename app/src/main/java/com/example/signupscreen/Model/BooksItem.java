package com.example.signupscreen.Model;

public class BooksItem {
    public String id,name,authorname,image,description,pdf,category,bgimage,page;
    public Double rate;


    public String getBgimage() {
        return bgimage;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setBgimage(String bgimage) {
        this.bgimage = bgimage;
    }

    public BooksItem(String name, String authorname, String image, String description, String bgimage, String pdf, String category, String page, Double rate) {
        this.name = name;
        this.authorname = authorname;
        this.image = image;
        this.description = description;
        this.pdf = pdf;
        this.category = category;
        this.rate = rate;
        this.bgimage = bgimage;
        this.page = page;

    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public BooksItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
