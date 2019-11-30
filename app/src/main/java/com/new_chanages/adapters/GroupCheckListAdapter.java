package com.new_chanages.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contus.responsemodel.CategoryPollResponseModel;
import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.new_chanages.models.GroupsNameObject;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/17/2015.
 */
public class GroupCheckListAdapter extends ArrayAdapter<GroupsNameObject> {
    //Arraylist
    //private final ArrayList<String> groupList;
    //Loading the data from the prefernce
    private ArrayList<String> prefernceCategorySaveArray;
    //activity
    Activity context;
    //Response from the server
    private ArrayList<GroupsNameObject> groupList;
    //View holder class
    private ViewHolder holder;
    private View mViews;

    /**
     * initializes a new instance of the ListView class.
     *
     * @param context  -activity
     * @param category -category
     */
    public GroupCheckListAdapter(Activity context, ArrayList<GroupsNameObject> category) {
        super(context, 0, category);
        this.context = context;
        this.groupList = category;
        //Initializing the array list
        //groupList = new ArrayList<String>();
    }

    @Override
    public View getView(final int position, View mCategory, ViewGroup parent) {
        //getView() method to be able to inflate our custom layout & create a View class out of it,
        // we need an instance of LayoutInflater class
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mViews = mCategory;
        // The old view to reuse, if possible.If convertView is NOT null, we can simple re-use the convertView as the new View.
        // It will happen when a new row appear and a old row in the other end roll out.
        if (mViews == null) {
              /* create a new view of my layout and inflate it in the row */
            mViews = mInflater.inflate(R.layout.group_check_list, null);
            //view holder class
            holder = new ViewHolder();
            holder.tv_group_name = (TextView) mViews.findViewById(R.id.tv_group_name);
            holder.cb_group_selected = mViews.findViewById(R.id.cb_group_selected);
            mViews.setTag(holder);
        } else {
            holder = (ViewHolder) mViews.getTag();
        }
        final GroupsNameObject model = groupList.get(position);

        holder.tv_group_name.setText(model.getGroupName());






        holder.cb_group_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!buttonView.isChecked())
                {
                    model.setGroupStatus("FALSE");
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    dbHelper.deleteGroupFromList(String.valueOf(groupList.get(position).getGroupId()));
                    //Toast.makeText(context, "Test "+dbHelper.GetGroupListSize(), Toast.LENGTH_SHORT).show();
                    //notifyDataSetChanged();
                    //Toast.makeText(context, "Test "+position, Toast.LENGTH_SHORT).show();
                }
                else {
                    model.setGroupStatus("TRUE");
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    dbHelper.addGroupsToList(groupList.get(position).getGroupName(), String.valueOf(groupList.get(position).getGroupId()), "true");
                    //Toast.makeText(context, "Test "+dbHelper.GetGroupListSize(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "Test "+position, Toast.LENGTH_SHORT).show();
                    //notifyDataSetChanged();
                    dbHelper.close();
                }
            }
        });






// Newly Edited
        try{
            if(model.getGroupStatus().equals("TRUE")){
                holder.cb_group_selected.setChecked(true);
            }     else {
                holder.cb_group_selected.setChecked(false);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return mViews;
    }

    private class ViewHolder {
        //Imageview
        private CheckBox cb_group_selected;
        //Textview
        private TextView tv_group_name;
    }
}