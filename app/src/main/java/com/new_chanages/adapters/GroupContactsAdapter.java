package com.new_chanages.adapters;

import android.app.AlertDialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.utils.Utils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.new_chanages.activity.EditGroupActivity;
import com.new_chanages.activity.Update;
import com.new_chanages.api_interface.GroupsApiInterface;
import com.new_chanages.holders.GroupNameRCVHolder;
import com.new_chanages.models.ContactDetailsModel;
import com.new_chanages.models.RemoveContactPostParameter;
import com.polls.polls.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GroupContactsAdapter extends RecyclerView.Adapter<GroupNameRCVHolder> {
    private ArrayList<ContactDetailsModel> arrayList;
    private Context context;
    private GroupNameRCVHolder listHolder;
    String created_by;
    AlertDialog.Builder builder;
    Update update;


    public GroupContactsAdapter(Context context, ArrayList<ContactDetailsModel> arrayList, String created_by,EditGroupActivity edit) {
        this.context = context;
        this.arrayList = arrayList;
        this.created_by=created_by;
        builder=new AlertDialog.Builder(context);
        update=edit;

    }

    @Override
    public int getItemCount(){
        // return (null != arrayList ? arrayList.size() : 0);
        return arrayList.size();
    }

    @Override
    public void onBindViewHolder(GroupNameRCVHolder holder, final int position) {
        ContactDetailsModel model = arrayList.get(position);

        String name =Utils.getContactName(model.getMobile_number(),context);
        if(TextUtils.isEmpty(name))
        {
           name=model.getName();
        }
        else
        {

        }
        holder.tv_group_name.setText(name);
        holder.tv_contact_num.setVisibility(View.VISIBLE);
        holder.tv_contact_num.setText(model.getMobile_number());
        holder.iv_arrow.setVisibility(View.VISIBLE);
        final Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
        Typeface faceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_regular.ttf");
        holder.tv_contact_num.setTypeface(face);
        holder.tv_group_name.setTypeface(faceRegular);
        String imageUrl = model.getProfile_image();
        if(imageUrl!=null && !imageUrl.equals(""))
        {
            Utils.loadImageWithGlideSingleImageRounderCorner(context, imageUrl, holder.iv_group_icon, R.drawable.img_ic_user);
        }

        String userId = MApplication.getString(context, Constants.USER_ID);




        if(created_by.equalsIgnoreCase(userId))
        {
            builder.setTitle("Remove user from group");
            builder.setMessage("Do you want to remove this user ?");
            holder.iv_arrow.setVisibility(View.VISIBLE);
        }
        else
        {
            String id =model.getId();
            if(id!=null&&id.length()>0)
            {
                if(userId.equalsIgnoreCase(id))
                {

                    builder.setTitle("Exit from group");
                    builder.setMessage("Do You want to exit from this group ?");
                    holder.iv_arrow.setImageResource(R.drawable.exit_app);
                    holder.iv_arrow.setVisibility(View.VISIBLE);
                }
                else {
                    holder.iv_arrow.setVisibility(View.GONE);
                }
            }
            else
            {
                holder.iv_arrow.setVisibility(View.GONE);
            }


        }


        if (created_by.equals(model.getId())){
            holder.group_admin.setVisibility(View.VISIBLE);
            holder.iv_arrow.setVisibility(View.GONE);

        }
        else
        {
            holder.group_admin.setVisibility(View.GONE);
        }


        // FOR USER ADMIN VIEW
      /*  if(userId.equals(model.getId())){
            holder.group_admin.setVisibility(View.VISIBLE);
            holder.iv_arrow.setVisibility(View.GONE);
        }else {
            //holder.group_admin.setVisibility(View.VISIBLE);
        }*/


        holder.iv_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





             final String  group_id = MApplication.getString(context, Constants.GET_GROUP_POLL_ID);




                      builder.setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteservice(position,arrayList.get(position).getMobile_number(),group_id);
                            }

                        })

                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();






                //arrayList.remove(position);
                //notifyDataSetChanged();

            }
        });



       //  holder.iv_group_icon.setImageBitmap(imageUrl);
    }

    private void deleteservice(final int position, final String mobile_number, String group_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupsApiInterface service = retrofit.create(GroupsApiInterface.class);

        Call<JsonElement> call=service.removeContactsFromGroup(new RemoveContactPostParameter("removecontactsfromgroupapi",mobile_number,group_id));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    if( jsonObject.get("success").getAsString().equals("1"))
                    {
                        //JsonArray result = jsonObject.get("results").getAsJsonArray();
                        Toast toast = Toast.makeText(context , "Contact removed Successfully", Toast.LENGTH_LONG);
                        toast.show();

                        update.delete(mobile_number);

                       try
                       {
                           arrayList.remove(position);
                           notifyDataSetChanged();
                       }
                       catch (Exception ae)
                       {
                           ae.printStackTrace();
                       }



                }else {

                }

            }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
               // Toast toast = Toast.makeText(context , ""+t, Toast.LENGTH_LONG);
               // toast.show();
            }
        });
    }

    @Override
    public GroupNameRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.editprofile_layout, viewGroup, false);

        listHolder = new GroupNameRCVHolder(mainGroup);
        return listHolder;

    }






}