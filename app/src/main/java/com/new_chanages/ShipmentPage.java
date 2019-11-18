package com.new_chanages;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.new_chanages.api_interface.Retrofit_NetworkInterface;
import com.new_chanages.models.GetAdressApi_InputModel;
import com.new_chanages.models.OrderResponce;
import com.new_chanages.models.ShipmentAdress_Model;
import com.new_chanages.models.Shipment_respnce;
import com.polls.polls.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ShipmentPage extends AppCompatActivity implements View.OnClickListener {
    String userId;
    Button submit_btn;
    EditText first_name_et,last_name_et,mobile_no_et,mailid_et,address_et,pincode_et,land_mark_et,city_et,state_et;
    RadioButton chnge_address_rbtn,defalt_address_rbtn;
    String points,productid;
    TextView mobile_no_tv,address_tv,pincode_tv,landmark_tv,city_tv,state_tv,first_name_tv;
    ImageView back_button;
    String firstname,lastname,mobile_no,emailid,address,pincode,landmark,city,state;
    CheckBox save_as_defalt_cb;
    LinearLayout defalt_adress_layout,new_adress_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_page);

        userId = MApplication.getString(getApplicationContext(), Constants.USER_ID);
        submit_btn=findViewById(R.id.submit_btn);
        first_name_et=findViewById(R.id.first_name_et);
        last_name_et=findViewById(R.id.last_name_et);
        mobile_no_et=findViewById(R.id.mobile_no_et);
        mailid_et=findViewById(R.id.mailid_et);
        address_et=findViewById(R.id.address_et);
        pincode_et=findViewById(R.id.pincode_et);
        land_mark_et=findViewById(R.id.land_mark_et);
        city_et=findViewById(R.id.city_et);
        state_et=findViewById(R.id.state_et);
        chnge_address_rbtn=findViewById(R.id.chnge_address_rbtn);
        defalt_address_rbtn=findViewById(R.id.defalt_address_rbtn);
        productid=getIntent().getExtras().getString("productid_key");
        points= getIntent().getExtras().getString("points_key");
        mobile_no_tv =findViewById(R.id.mobile_no_tv);
        address_tv =findViewById(R.id.adress_tv);
        pincode_tv=findViewById(R.id.pincode_tv);
        landmark_tv=findViewById(R.id.landmark_tv);
        city_tv =findViewById(R.id.city_tv);
        state_tv=findViewById(R.id.state_tv);
        back_button=findViewById(R.id.back_button);
        save_as_defalt_cb =findViewById(R.id.save_as_defalt_cb);
        first_name_tv =findViewById(R.id.first_name_tv);
        defalt_adress_layout =findViewById(R.id.defalt_adress_layout);
        new_adress_layout   =findViewById(R.id.new_adress_layout);

        String Mobile = "<font color='#000000'>Mobile no </font>" + "<font color='#FF0000'>*</font>" ;
        String Address = "<font color='#000000'>Address </font>" + "<font color='#FF0000'>*</font>" ;
        String Pincode = "<font color='#000000'>Pincode</font>" + "<font color='#FF0000'>*</font>" ;
        String Landmark = "<font color='#000000'>Landmark </font>" + "<font color='#FF0000'>*</font>" ;
        String City = "<font color='#000000'>City </font>" + "<font color='#FF0000'>*</font>" ;
        String State = "<font color='#000000'>State </font>" + "<font color='#FF0000'>*</font>" ;
        String firstname1 = "<font color='#000000'>First Name</font>" + "<font color='#FF0000'>*</font>" ;

        mobile_no_tv.setText(Html.fromHtml(Mobile));
        address_tv.setText(Html.fromHtml(Address));
        pincode_tv.setText(Html.fromHtml(Pincode));
        landmark_tv.setText(Html.fromHtml(Landmark));
        city_tv.setText(Html.fromHtml(City));
        state_tv.setText(Html.fromHtml(State));
        first_name_tv.setText(Html.fromHtml(firstname1));


        submit_btn.setOnClickListener(this);
        back_button.setOnClickListener(this);

        getaddress();


        new_adress_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chnge_address_rbtn.setChecked(true);
            }
        });

        defalt_adress_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defalt_address_rbtn.setChecked(true);
            }
        });


        chnge_address_rbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {

                if(ischecked)
                {

                    settext_et("","","","","","","","","");
                    defalt_address_rbtn.setChecked(false);
                    enable_disable_the_edittext(true);
                    save_as_defalt_cb.setVisibility(View.VISIBLE);
                }


            }
        });

        defalt_address_rbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked)
                {
                    chnge_address_rbtn.setChecked(false);
                    enable_disable_the_edittext(false);
                    save_as_defalt_cb.setVisibility(View.GONE);
                    settext_et(firstname,lastname,mobile_no,emailid,address,landmark,city,state,pincode);
                }

            }
        });
    }




    public void getaddress(){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL).client(client)
                //.baseUrl("http://182.18.177.27/TUUserManagement/api/user/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit_NetworkInterface service = retrofit.create(Retrofit_NetworkInterface.class);

        Call<Shipment_respnce> call = service.getAddress(new GetAdressApi_InputModel("getaddressapi",userId));
        call.enqueue(new Callback<Shipment_respnce>() {
            @Override
            public void onResponse(Call<Shipment_respnce> call, Response<Shipment_respnce> response) {

                if(response.body().getResults()!=null)
                {

                    firstname=response.body().getResults().getAddress().getFirst_name();
                    lastname=response.body().getResults().getAddress().getLast_name();
                    mobile_no=response.body().getResults().getAddress().getMobile_no();
                    pincode=response.body().getResults().getAddress().getPincode();
                    emailid=response.body().getResults().getAddress().getEmail();
                    address=response.body().getResults().getAddress().getAddress();
                    landmark=response.body().getResults().getAddress().getLandmark();
                    city=response.body().getResults().getAddress().getCity();
                    state=response.body().getResults().getAddress().getState();

                   /* String addresss=response.body().getResults().getAddress().getFirst_name()+" "+response.body().getResults().getAddress().getLast_name()
                    +"\n"+response.body().getResults().getAddress().getMobile_no()+"\n"+response.body().getResults().getAddress().getAddress()+"\n"+response.body().getResults().getAddress().getPincode() ;*/

                    defalt_address_rbtn.setChecked(true);
                    defalt_address_rbtn.setVisibility(View.VISIBLE);

                    enable_disable_the_edittext(false);

                    settext_et(firstname,lastname,mobile_no,emailid,address,landmark,city,state,pincode);
                    defalt_adress_layout.setVisibility(View.VISIBLE);
                    chnge_address_rbtn.setVisibility(View.VISIBLE);


                }
                else
                {
                    chnge_address_rbtn.setVisibility(View.GONE);
                    defalt_address_rbtn.setVisibility(View.GONE);
                    defalt_adress_layout.setVisibility(View.GONE);
                    chnge_address_rbtn.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<Shipment_respnce> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), "Error" +t, Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void settext_et(String firstname, String lastname, String mobile_no,
                            String emailid, String address, String landmark, String city, String state,String pincode)
    {
        first_name_et.setText(firstname);
        last_name_et.setText(lastname);
        mobile_no_et.setText(mobile_no);
        pincode_et.setText(pincode);
        mailid_et.setText(emailid);
        city_et.setText(city);
        address_et.setText(address);
        land_mark_et.setText(landmark);
        state_et.setText(state);
    }

    private void enable_disable_the_edittext(boolean flag)
    {

                    first_name_et.setEnabled(flag);
                    last_name_et.setEnabled(flag);
                    mobile_no_et.setEnabled(flag);
                    pincode_et.setEnabled(flag);
                    mailid_et.setEnabled(flag);
                    address_et.setEnabled(flag);
                     city_et.setEnabled(flag);
                     state_et.setEnabled(flag);
                     land_mark_et.setEnabled(flag);
    }




    public void submitRequest(){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL).client(client)
                //.baseUrl("http://182.18.177.27/TUUserManagement/api/user/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit_NetworkInterface service = retrofit.create(Retrofit_NetworkInterface.class);

         ShipmentAdress_Model obj = new ShipmentAdress_Model();
        obj.setAction("placingredeemapi");
        obj.setAddress(address_et.getText().toString());
        obj.setCity(city_et.getText().toString());
        obj.setState(state_et.getText().toString());
        obj.setPincode(pincode_et.getText().toString());
        obj.setLandmark(land_mark_et.getText().toString());
        obj.setFirst_name(first_name_et.getText().toString());
        obj.setLast_name(last_name_et.getText().toString());
        obj.setMobile_no(mobile_no_et.getText().toString());
        obj.setEmail(mailid_et.getText().toString());
        obj.setChange_default_addr( save_as_defalt_cb.isChecked() );
        obj.setUse_default_addr(defalt_address_rbtn.isChecked() );
        obj.setUser_id(userId);
        obj.setProduct_id(productid);
        obj.setPoints(points);


        Call<OrderResponce> call = service.save_the_shipmentdetails(obj);
        call.enqueue(new Callback<OrderResponce>() {
            @Override
            public void onResponse(Call<OrderResponce> call, Response<OrderResponce> response) {
                int version_no;
                if(response.body()!=null)
                {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "" +response.body().getMsg(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<OrderResponce> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error" +t, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean checkmandatoryField(EditText et)

    {
        if(TextUtils.isEmpty(et.getText().toString()))
        {
            return false;
        }
        else {
            return true;
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.submit_btn)
        {

            if(chnge_address_rbtn.isChecked()||chnge_address_rbtn.getVisibility()==View.GONE)
            {
                if(checkmandatoryField(first_name_et)&&checkmandatoryField(mobile_no_et)&&checkmandatoryField(address_et)
                        &&checkmandatoryField(pincode_et)&&checkmandatoryField(city_et)&&checkmandatoryField(state_et)&&checkmandatoryField(land_mark_et))
                {
                     submitRequest();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please fill all mandatory fields",Toast.LENGTH_LONG).show();
                }
            }
            else if(defalt_address_rbtn.isChecked()) {
                submitRequest();
            }


        }
        else
        {
            finish();
        }
    }
}
