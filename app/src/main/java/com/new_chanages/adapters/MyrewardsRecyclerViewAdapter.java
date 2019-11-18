package com.new_chanages.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.utils.Utils;
import com.new_chanages.Redeem_DetailsPage;
import com.new_chanages.ShipmentPage;
import com.new_chanages.models.ProductsModel;
import com.polls.polls.R;

import java.util.ArrayList;

public class MyrewardsRecyclerViewAdapter extends RecyclerView.Adapter<MyrewardsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<ProductsModel> mValues;
    Activity act;

    public MyrewardsRecyclerViewAdapter(Activity act ,ArrayList<ProductsModel> items) {
        mValues = items;
        this.act = act;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_rewards, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.product_title.setText(mValues.get(position).getProduct_title());
        holder.cups_count_tv.setText(mValues.get(position).getPoints());
        holder.dimands_count_tv.setText(mValues.get(position).getMoney());
        holder.product_left_tv.setText(mValues.get(position).getQuantity()+" Left");
        holder.date_tv.setText("Validity :"+mValues.get(position).getExpiry_date().replace("00:00:00",""));
        final String positionSelect = mValues.get(position).getProduct_id();

        if(mValues.get(position).getQuantity().equalsIgnoreCase("0"))
        {
            holder.redeem_btn.setBackgroundColor(Color.parseColor("#B22222"));
            holder.redeem_btn.setEnabled(false);
            holder.redeem_btn.setClickable(false);
            holder.product_left_tv.setText("Out of stock");
        }
        else
        {
            holder.redeem_btn.setBackgroundColor(Color.parseColor("#1C96EE"));
            holder.redeem_btn.setEnabled(true);
            holder.redeem_btn.setClickable(true);
        }

        Utils.loadImageWithGlideRounderCorner(act, mValues.get(position).getImage(), holder.product_iv,
                R.drawable.placeholder_image);


        holder.redeem_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MApplication.setString(act, Constants.SELECTED_PRODUCT, mValues.get(position).getProduct_id());

               String points= MApplication.getString(act,"ToTal_points");

               if(points!=null&&mValues.get(position).getPoints()!=null)
               {
                  int tot_numpoints =Integer.parseInt(points);
                   int itemvalue =Integer.parseInt(mValues.get(position).getPoints());
                   int quntity =Integer.parseInt(mValues.get(position).getQuantity());



                  if(tot_numpoints>=itemvalue)
                  {

                      if(quntity>0)
                      {
                          Intent intent = new Intent(act, ShipmentPage.class);
                          intent.putExtra("productid_key",mValues.get(position).getProduct_id());
                          intent.putExtra("points_key",mValues.get(position).getPoints());
                          act.startActivityForResult(intent,500);
                      }
                      else
                      {
                          Toast.makeText(act,"Product not available",Toast.LENGTH_SHORT).show();
                      }

                  }
                  else
                  {
                      showDilog();
                  }
               }

            }
        });

        holder.product_iv.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Added by Nikita
                MApplication.setString(act, Constants.SELECTED_PRODUCT, mValues.get(position).getProduct_id());
                Intent intent = new Intent(act, Redeem_DetailsPage.class);
                act.startActivity(intent);
            }
        });


        holder.content_layt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Added by Nikita
                MApplication.setString(act, Constants.SELECTED_PRODUCT, mValues.get(position).getProduct_id());
                Intent intent = new Intent(act, Redeem_DetailsPage.class);
                act.startActivity(intent);
            }
        });

    }

    private void showDilog() {

        // custom dialog
        final Dialog dialog = new Dialog(act);
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
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ProductsModel mItem;
        Button redeem_btn;
        TextView product_title,product_left_tv,dimands_count_tv,cups_count_tv,date_tv;
        ImageView product_iv;
        LinearLayout content_layt;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            redeem_btn    =     view.findViewById(R.id.redeem_btn);
            product_title  =    view.findViewById(R.id.product_title);
            product_iv      =   view.findViewById(R.id.product_iv);
            product_left_tv  =  view.findViewById(R.id.product_left_tv);
            dimands_count_tv =  view.findViewById(R.id.dimands_count_tv);
            cups_count_tv     = view.findViewById(R.id.cups_count_tv);
            date_tv           = view.findViewById(R.id.date_tv);
            content_layt     =view.findViewById(R.id.content_layt);
        }


    }
}
