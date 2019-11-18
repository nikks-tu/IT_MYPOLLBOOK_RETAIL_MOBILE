package com.new_chanages.models;

public class Shipment_respnce {

    private String msg;

    private String success;

    private Results[] results;

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    public Results getResults ()
    {
        return results[0];
    }

    public void setResults (Results[] results)
    {
        this.results = results;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [msg = "+msg+", success = "+success+", results = "+results+"]";
    }
}
