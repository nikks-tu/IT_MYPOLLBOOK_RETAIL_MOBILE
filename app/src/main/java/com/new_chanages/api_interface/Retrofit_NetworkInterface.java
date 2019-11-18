package com.new_chanages.api_interface;

import com.contus.app.Constants;
import com.new_chanages.models.GetAdressApi_InputModel;
import com.new_chanages.models.OrderResponce;
import com.new_chanages.models.ShipmentAdress_Model;
import com.new_chanages.models.Shipment_respnce;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public  interface Retrofit_NetworkInterface {

    @POST(Constants.GET_ADDRESS)
    Call<Shipment_respnce> getAddress(@Body GetAdressApi_InputModel postParameter);

    @POST(Constants.SHIP_THE_ITEM)
    Call<OrderResponce> save_the_shipmentdetails(@Body ShipmentAdress_Model postParameter);


}
