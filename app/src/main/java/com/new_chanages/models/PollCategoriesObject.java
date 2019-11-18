package com.new_chanages.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PollCategoriesObject {

    @SerializedName("poll_id")
    @Expose
    private Integer pollId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("categories")
    @Expose
    private CategoriesObject categories;

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public CategoriesObject getCategories() {
        return categories;
    }

    public void setCategories(CategoriesObject categories) {
        this.categories = categories;
    }

}