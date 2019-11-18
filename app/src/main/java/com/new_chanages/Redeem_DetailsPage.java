package com.new_chanages;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.contus.activity.CustomRequest;
import com.contus.app.Constants;
import com.contus.restclient.UserPollRestClient;
import com.contusfly.MApplication;
import com.contusfly.utils.Utils;
import com.new_chanages.activity.Rewards_Activty;
import com.new_chanages.models.GetErnigsInputModel;
import com.new_chanages.models.ProductsModel;
import com.polls.polls.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Redeem_DetailsPage extends AppCompatActivity {

    ImageView iv_tool_bar, back_button;
    TextView tv_productTitle, tv_productName, tv_redeem_pt_req, tv_productValidity;
    WebView wview_terms;
    int position;
    String selectedProduct = "";
    Button redeem_btn;
    String product_Id ="";
    Bundle extras;
    Context context;
    ProductsModel productsModel = new ProductsModel();
    ArrayList<ProductsModel> productsList = new ArrayList<>();
    String totalpoints="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem__details_page);
        Init();
        getList(false);
        redeem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String points= MApplication.getString(getApplicationContext(),"ToTal_points");
                int tot_numpoints =Integer.parseInt(points);
                int itemvalue =Integer.parseInt(totalpoints);

                if(tot_numpoints>=itemvalue)
                {

                    Intent intent = new Intent(getApplicationContext(), ShipmentPage.class);
                    intent.putExtra("productid_key",product_Id);
                    intent.putExtra("points_key",totalpoints);
                    startActivityForResult(intent,400);
                }
                else
                {
                    showDilog();
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==400&&resultCode==Activity.RESULT_OK)
        {
            Intent intent = new Intent(getApplicationContext(), Rewards_Activty.class);
            intent.putExtra("is_from_redeem",true);

            startActivity(intent);
            finish();
        }
    }

    private void showDilog() {

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.ok_layout);
        dialog.setCancelable(true);


        Button ok_btn =  dialog.findViewById(R.id.ok_btn);
        TextView text =  dialog.findViewById(R.id.text);



        // if button is clicked, close the custom dialog
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void Init() {
        context = Redeem_DetailsPage.this;
        /*extras = getIntent().getExtras();
        if (extras != null && !extras.equals(null)) {
            position = Integer.parseInt(extras.getString("productId"));
            selectedProduct = extras.getString("productId");
       }*/
        selectedProduct = MApplication.getString(context, Constants.SELECTED_PRODUCT);
       // Toast.makeText(context, "SelId " +selectedProduct, Toast.LENGTH_SHORT).show();

        iv_tool_bar = findViewById(R.id.iv_tool_bar);
        back_button    = findViewById(R.id.back_button);
        tv_productTitle = findViewById(R.id.tv_productTitle);
        tv_productName = findViewById(R.id.tv_productName);
        tv_redeem_pt_req = findViewById(R.id.tv_redeem_pt_req);
        tv_productValidity  = findViewById(R.id.tv_productValidity);
        wview_terms  = findViewById(R.id.wview_terms);
        redeem_btn = findViewById(R.id.redeem_btn);


        wview_terms.getSettings().setBuiltInZoomControls(true);
        wview_terms.setInitialScale(100);
        wview_terms.getSettings().setAppCacheEnabled(false);
        wview_terms.getSettings().setJavaScriptEnabled(true);
        wview_terms.getSettings().setLoadWithOverviewMode(true);
        wview_terms.getSettings().setUseWideViewPort(true);
        wview_terms.getSettings().setDefaultFontSize(30);
    }





    private void getList(final boolean more) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("action","getredeemproductsapi");

            Log.v("...", obj.toString());
        }
        catch (Exception ae)
        {

        }


        UserPollRestClient.getInstance().getEarnigs(new GetErnigsInputModel("getredeemproductsapi","0"), new Callback<JSONObject>() {
            @Override
            public void success(JSONObject jsonObject, Response response1) {

                String result =CustomRequest.ResponcetoString(response1);
                try {
                    JSONObject response = new JSONObject(result);

                    int success=response.optInt("success");

                    {
                        try {

                            if(success==1)
                            {
                                JSONObject results =response.optJSONObject("results");
                                String current_page =results.optString("current_page");
                                String from =results.optString("from");
                                String last_page =results.optString("last_page");
                                String per_page =results.optString("per_page");
                                String to =results.optString("to");
                                String total =results.optString("total");
                                JSONArray data =results.optJSONArray("data");

                                if(!more) {
                                    productsList = new ArrayList<>();
                                }
                                for(int i=0;i<data.length();i++)
                                {
                                    JSONObject obj = data.getJSONObject(i);
                                    product_Id = obj.optString("product_id");
                                    if(selectedProduct.equals(product_Id))
                                    {
                                        String expiry_date = obj.optString("expiry_date");
                                        String image= obj.optString("image");
                                        String money= obj.optString("money");
                                        String points= obj.optString("points");
                                        String product_description= obj.optString("product_description");
                                        String product_title= obj.optString("product_title");
                                        String quantity= obj.optString("quantity");
                                        String product_id = obj.optString("product_id");

                                        totalpoints=points;

                                        ProductsModel obj1 = new ProductsModel();
                                        obj1.setExpiry_date(expiry_date);
                                        obj1.setImage(image);
                                        obj1.setMoney(money);
                                        obj1.setPoints(points);
                                        obj1.setProduct_description(product_description);
                                        obj1.setProduct_title(product_title);
                                        obj1.setQuantity(quantity);
                                        obj1.setProduct_id(product_id);
                                        productsList.add(obj1);
                                        break;
                                    }
                           /* else
                            {
                                Toast.makeText(Redeem_DetailsPage.this, "No Data", Toast.LENGTH_SHORT).show();
                            }*/
                                }
                                if(productsList.size()>0)
                                {
                                    tv_productName.setText(productsList.get(0).getProduct_title()+" "+productsList.get(0).getQuantity()+" Left");
                                    tv_productTitle.setText(productsList.get(0).getProduct_title());
                                    tv_redeem_pt_req.setText(productsList.get(0).getPoints());
                                    wview_terms.loadData(productsList.get(0).getProduct_description(),"text/html", "UTF-8");
                                    tv_productValidity.setText("Validity :"+productsList.get(0).getExpiry_date().replace("00:00:00",""));
                                    //iv_tool_bar.setImageDrawable(productsList.get(0).getImage());
                                    Utils.loadImageWithGlide(Redeem_DetailsPage.this, productsList.get(0).getImage(), iv_tool_bar,
                                            R.drawable.placeholder_image);
                                    //   Toast.makeText(context, "fI " +  product_Id , Toast.LENGTH_SHORT).show();

                                    if(productsList.get(0).getQuantity().equalsIgnoreCase("0"))
                                    {
                                        redeem_btn.setClickable(false);
                                        redeem_btn.setEnabled(false);
                                       redeem_btn.setBackgroundColor(Color.parseColor("#B22222"));
                                        tv_productName.setText("Out of stock");
                                    }
                                    else
                                    {
                                        redeem_btn.setClickable(true);
                                        redeem_btn.setEnabled(true);
                                        redeem_btn.setBackgroundColor(Color.parseColor("#1C96EE"));
                                    }
                                }
                            }
                            else
                            {

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void failure(RetrofitError error) {

            }
        });



    }




}
