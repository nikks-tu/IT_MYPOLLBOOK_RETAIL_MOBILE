package com.contusfly.createpoll;

/**
 * Created by user on 1/9/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.polls.polls.R;

import java.util.ArrayList;

/**
 * Created by user on 7/24/2015.
 */
public class CreatePollOptionsAdapter extends BaseAdapter {
    private final String[] options;
    private final Integer[] imgOptions;
    private final String[] optionsDetails;
    Context context;
    private ArrayList<String> listGroupid;
    private ArrayList<String> mArrayList;
    private ArrayList<String> mCategory;
    private DatabaseHelper db;


    public CreatePollOptionsAdapter(Context context, String[] options, String[] imgOptions, Integer[] imgOptionsCategory) {
        this.context = context;
        this.options=options;
        this.optionsDetails=imgOptions;
        this.imgOptions=imgOptionsCategory;
        listGroupid=new ArrayList<>();
        mArrayList =new ArrayList<>();
        mCategory=new ArrayList<>();
        db=new DatabaseHelper(context);
    }



    /*private view holder class*/
    private class ViewHolder {
        private TextView txtOptions;
        private TextView txtOptionsDetails;

        private ImageView imgOptions;

        public ImageView imageRightArrow;

        private ImageView imageUnselectCategory;
        private ImageView imageSelectCategory;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.singleview_polloptions, null);
            holder = new ViewHolder();
            holder.txtOptions=(TextView)convertView.findViewById(R.id.txtGroup);
            holder.txtOptionsDetails=(TextView)convertView.findViewById(R.id.txtDetails);
            holder.imgOptions=(ImageView)convertView.findViewById(R.id.imgOptions);
            holder.imageRightArrow=(ImageView)convertView.findViewById(R.id.imageView);
            holder.imageUnselectCategory=(ImageView)convertView.findViewById(R.id.imageUnselectCategory);
            holder.imageSelectCategory=(ImageView)convertView.findViewById(R.id.imageSelectCategory);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtOptions.setText(options[position]);
        holder.txtOptionsDetails.setText(optionsDetails[position]);
        holder.imgOptions.setImageResource(imgOptions[position]);
        if(position==0){
            mCategory=MApplication.loadStringArray(context,mCategory,Constants.ACTIVITY_CATEGORY_INFO,Constants.ACTIVITY_CATEGORY_INFO_SIZE);
       Log.e("mCategory",mCategory+"");
          if(mCategory.contains("Public")) {
              holder.imageRightArrow.setVisibility(View.INVISIBLE);
              holder.imageUnselectCategory.setVisibility(View.INVISIBLE);
              holder.imageSelectCategory.setVisibility(View.VISIBLE);
          }else{
              holder.imageRightArrow.setVisibility(View.INVISIBLE);
              holder.imageUnselectCategory.setVisibility(View.VISIBLE);
              holder.imageSelectCategory.setVisibility(View.INVISIBLE);
          }
        }else if(position==1){
            mCategory=MApplication.loadStringArray(context,mCategory,Constants.ACTIVITY_CATEGORY_INFO,Constants.ACTIVITY_CATEGORY_INFO_SIZE);

            if(!db.getAllGroupDetails(1).isEmpty()){
               holder.imageUnselectCategory.setVisibility(View.INVISIBLE);
               holder.imageSelectCategory.setVisibility(View.VISIBLE);
                if(!mCategory.contains("Groups")){
                    mCategory.add("Groups");
                }

           }else{
               holder.imageUnselectCategory.setVisibility(View.VISIBLE);
               holder.imageSelectCategory.setVisibility(View.INVISIBLE);
               if(mCategory.contains("Groups")){
                   mCategory.remove("Groups");
               }
           }
        }else if(position==2){
            if(!db.getAllContactsDetails(1).isEmpty()){
                holder.imageUnselectCategory.setVisibility(View.INVISIBLE);
                holder.imageSelectCategory.setVisibility(View.VISIBLE);
                if(!mCategory.contains("Contacts")){
                    mCategory.add("Contacts");
                }

            }else{
                holder.imageUnselectCategory.setVisibility(View.VISIBLE);
                holder.imageSelectCategory.setVisibility(View.INVISIBLE);
                if(mCategory.contains("Contacts")){
                    mCategory.remove("Contacts");
                }
            }

        }
        MApplication.saveStringArray(context,mCategory,Constants.ACTIVITY_CATEGORY_INFO,Constants.ACTIVITY_CATEGORY_INFO_SIZE);
        return convertView;
    }

    @Override
    public int getCount() {
        return options.length;
    }

    @Override
    public Object getItem(int position) {
        return options.length;
    }

    @Override
    public long getItemId(int position) {
        return options.length;
    }
}