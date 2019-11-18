package com.new_chanages.models;

public class OrderResponce {
    public OrderResponce(String success, String msg, String results) {
        this.success = success;
        this.msg = msg;
        this.results = results;
    }

    String success;
    String msg;
    String results;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }


}
