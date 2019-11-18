package com.contus.category;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.contus.app.Constants;
import com.contus.responsemodel.CategoryPollResponseModel;
import com.contusfly.MApplication;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/17/2015.
 */
public class CategoryAdapter extends ArrayAdapter<CategoryPollResponseModel.Results> {
    //Arraylist
    private final ArrayList<String> categoryArray;
    //Loading the data from the prefernce
    private ArrayList<String> prefernceCategorySaveArray;
    //activity
    Activity context;
    //Response from the server
    private List<CategoryPollResponseModel.Results> categoryModel;
    //View holder class
    private ViewHolder holder;
    private View mViews;

    /**
     * initializes a new instance of the ListView class.
     *
     * @param context  -activity
     * @param category -category
     */
    public CategoryAdapter(Activity context, List<CategoryPollResponseModel.Results> category) {
        super(context, 0, category);
        this.context = context;
        this.categoryModel = category;
        //Initializing the array list
        categoryArray = new ArrayList<String>();
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
            mViews = mInflater.inflate(R.layout.category_singleview, null);
            //view holder class
            holder = new ViewHolder();
            holder.txtCategory = (TextView) mViews.findViewById(R.id.txtCategory);
            holder.imageButtonUnSelect = (ImageView) mViews.findViewById(R.id.imageButtonUnSelect);
            holder.imageButton = (ImageView) mViews.findViewById(R.id.imageButton);
            mViews.setTag(holder);
        } else {
            holder = (ViewHolder) mViews.getTag();
        }
        //Setting the category name from the resposne in text view
       // if (!categoryModel.get(position).getCategoryName().equals("Admin"))
            holder.txtCategory.setText(categoryModel.get(position).getCategoryName());
        //Category id saved in the prefernce are loaded into the array list
        prefernceCategorySaveArray = MApplication.loadStringArray(context, categoryArray, Constants.CATEGORY_COUNT_ARRAY, Constants.CATEGORY_COUNT_SIZE);
        //If the user does not select any category ,saved category id in prefernce matches with the selected category id from
        //response then the category is selected otherwise the category is not selected
        if (!categoryModel.get(position).getUserCategory().isEmpty()) {
            if (prefernceCategorySaveArray.contains(categoryModel.get(position).getUserCategory().get(0).getCategoryId())) {
                //View visible
                holder.imageButton.setVisibility(View.VISIBLE);
                //view invisible
                holder.imageButtonUnSelect.setVisibility(View.GONE);
            }
        } else {
            //View visible
            holder.imageButtonUnSelect.setVisibility(View.VISIBLE);
            //view invisible
            holder.imageButton.setVisibility(View.GONE);
        }
        //If the condition is true ,notice that category is choosen from yhe create polls
        if (MApplication.getBoolean(context, Constants.YES_OR_NO)) {
            //View gone
            holder.imageButtonUnSelect.setVisibility(View.GONE);
            //Viw gone
            holder.imageButton.setVisibility(View.GONE);
        }
        //If the condition is false ,notice that category is choosen from category class
        if (!MApplication.getBoolean(context, Constants.YES_OR_NO)) {
            //View holder
            final ViewHolder finalHolder = holder;
            //Interface definition for a callback to be invoked when a view is clicked.
            mViews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //position
                    int clickposition = position;
                    //click ACTION
                    categoryClickAction(finalHolder, clickposition);
                }
            });

        }
        return mViews;
    }

    /**
     * Category click ACTION
     *
     * @param finalHolder-view       holder
     * @param clickposition-position
     */
    private void categoryClickAction(ViewHolder finalHolder, int clickposition) {
        //If image button unselect is visible
        if (finalHolder.imageButtonUnSelect.getVisibility() == View.VISIBLE) {
            //Invisible image unselect
            finalHolder.imageButtonUnSelect.setVisibility(View.INVISIBLE);
            //visible image unselect
            finalHolder.imageButton.setVisibility(View.VISIBLE);
            //Category id from the response
            String categoryId = categoryModel.get(clickposition).getCategoryId();
            //Category id is saved into the array list
            prefernceCategorySaveArray.add(categoryId);
            //Saving the array in preference
            MApplication.saveStringArray(context, prefernceCategorySaveArray, Constants.CATEGORY_COUNT_ARRAY, Constants.CATEGORY_COUNT_SIZE);
        } else {
            //View visible
            finalHolder.imageButtonUnSelect.setVisibility(View.VISIBLE);
            //View invisible
            finalHolder.imageButton.setVisibility(View.INVISIBLE);
            //Category id
            String categoryId = categoryModel.get(clickposition).getCategoryId();
            //If the category id is available in array,then the category id is removed
            if (prefernceCategorySaveArray.contains(categoryId)) {
                //Find the position of the category id in array
                int index = prefernceCategorySaveArray.indexOf(categoryId);
                //Remove the id in that position
                prefernceCategorySaveArray.remove(index);
                //Saving it in preference
                MApplication.saveStringArray(context, prefernceCategorySaveArray, Constants.CATEGORY_COUNT_ARRAY, Constants.CATEGORY_COUNT_SIZE);
            }
        }
    }

    /**
     * A ViewHolder object stores each of the component views inside the tag field of the Layout,
     * so you can immediately access them without the need to look them up repeatedly.
     */
    private class ViewHolder {
        //Imageview
        private ImageView imageButtonUnSelect;
        //Imageview
        private ImageView imageButton;
        //Textview
        private TextView txtCategory;
    }
}