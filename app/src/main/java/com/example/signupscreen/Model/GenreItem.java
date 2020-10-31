package com.example.signupscreen.Model;

public class GenreItem {
    public String name,image;

    public GenreItem(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public GenreItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
