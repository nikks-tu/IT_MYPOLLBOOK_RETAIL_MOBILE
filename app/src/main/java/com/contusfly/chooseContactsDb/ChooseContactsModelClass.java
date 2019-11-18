package com.contusfly.chooseContactsDb;

/**
 * Created by user on 5/2/16.
 */
public class ChooseContactsModelClass {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    private int isActive;

    public int getIsFiledActive() {
        return isFiledActive;
    }

    public void setIsFiledActive(int isFiledActive) {
        this.isFiledActive = isFiledActive;
    }

    private int isFiledActive;

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    private int isDone;

}
