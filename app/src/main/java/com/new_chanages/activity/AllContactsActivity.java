package com.new_chanages.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.model.Contact;
import com.contusfly.model.MDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.new_chanages.SendEvent;
import com.new_chanages.adapters.AddGroupContactsAdapter;
import com.new_chanages.adapters.SelectedContactsAdapter;
import com.new_chanages.api_interface.GroupsApiInterface;
import com.new_chanages.models.ContactModel;
import com.new_chanages.models.Results;
import com.new_chanages.postParameters.GetGroupsPostParameters;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AllContactsActivity extends AppCompatActivity implements SendEvent {

    ListView lv_contacts;
    Context mContext;
    ImageView iv_back, iv_save;
    FloatingActionButton fab_next;
    ImageView refresh_fab;
    ArrayList<ContactModel> contactList;
    MDatabaseHelper db;
    SearchView searchView;
    ArrayList<ContactModel> selectedContactList;
    ArrayList<ContactModel> myPollBookContactList;
    ArrayList<ContactModel> existingContacts;
   ProgressBar progressBar;
   TextView title;
    AddGroupContactsAdapter contactsAdapter;
    SelectedContactsAdapter selectedContactsAdapter;
    RecyclerView rcv_selected_contacts;
    String contacts="";
    String contactToSend="";
    String checkContactAction="existingcontactsapi";
    boolean isEdit = false;
    TextView norecords_tv;
    Boolean from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);
        contactToSend="";
        if(getIntent().getExtras()!=null)
        {
            if(getIntent().getStringExtra("fromActivity").equals("Edit"))
            {
                isEdit = true;
            }
        }
        from=getIntent().getBooleanExtra("FROM",false);

        initialize();

        //contactList = getContacts();







        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               iv_back.setVisibility(View.GONE);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;


                searchView.setMaxWidth(width);



            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                iv_back.setVisibility(View.VISIBLE);

                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                      if(contactsAdapter!=null){
                          contactsAdapter.filter(query);
                      }else{

                      }

                return false;
            }
        });



        String[] fields = new String[] {ContactsContract.Data.DISPLAY_NAME};

        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean isselected=false;
                if(myPollBookContactList!=null&&myPollBookContactList.size()>0)
                {
                    for (ContactModel obj : myPollBookContactList) {
                        if (obj.getIsContactSelected().equals("TRUE")) {
                            isselected=true;
                            break;
                        }

                    }
                }

                if(myPollBookContactList!=null&&isselected&&myPollBookContactList.size()>0)
                {
                    Intent intent = new Intent(mContext, GroupCreate.class);
                    intent.putExtra("FROM",from);
                    startActivityForResult(intent,2128);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please select atleast one contact",Toast.LENGTH_SHORT).show();
                }

            }
        });



        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactList = new ArrayList<>();
                MDatabaseHelper dbHelper = new MDatabaseHelper(mContext);
                contactList = dbHelper.getAllContactsList();
                if(contactList.size()>0)
                {
                    Toast.makeText(mContext, ""+myPollBookContactList.size(), Toast.LENGTH_SHORT).show();

                }
                if(!contacts.equals(""))
                {
                   // serviceCall();
                    MApplication.setString(mContext, Constants.CONTACT_LIST, contacts);
                    finish();
                }
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    private void initialize() {
        mContext = AllContactsActivity.this;
        lv_contacts = findViewById(R.id.lv_contacts);
        iv_back = findViewById(R.id.iv_back);
        iv_save = findViewById(R.id.iv_save);
        progressBar=findViewById(R.id.progressbar);
        title=findViewById(R.id.tv_title);
        rcv_selected_contacts = findViewById(R.id.rcv_selected_contacts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,  LinearLayoutManager.HORIZONTAL, false);
        rcv_selected_contacts.setLayoutManager(layoutManager);
        fab_next = findViewById(R.id.fab_next);
        refresh_fab=findViewById(R.id.refresh);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Light.ttf");
        title.setTypeface(face);


        searchView=findViewById(R.id.search_view);
        existingContacts = new ArrayList<>();
        contactList = new ArrayList<>();
        selectedContactList = new ArrayList<>();
       db = new MDatabaseHelper(mContext);
        norecords_tv =findViewById(R.id.norecords_tv);



        //Search view

        EditText searchEditText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));

        if(db.getAllContactsList().size()>0)
        {
           getContactsFromDataBase(db);
        }
        else
        {
            refreshAllContacs();
        }


        //db.deleteSelectedContacts();
        /*db.deleteContactList();
        db.close();*/
        refresh_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refresh_fab.setEnabled(false);
                rcv_selected_contacts.setAdapter(null);
                db.deleteContactList();
                db.close();
                refreshAllContacs();
            }
        });
    }

    public  void refreshAllContacs()
    {
        AysncTask task=new AysncTask();
        task.execute();
    }

    public void getContactsFromDataBase(MDatabaseHelper db)
    {
        contactList =new ArrayList<>();
        existingContacts =new ArrayList<>();
        contactList=db.getAllContactsList();
        existingContacts = db.getAllSelectedContacts();
        if (!isEdit) {
            if (contactList.size() > 0) {
                myPollBookContactList = new ArrayList<>();
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).getIsMPBContact().equals("true")) {
                        ContactModel model = new ContactModel();
                        model.setContactMPBName(contactList.get(i).getContactMPBName());
                        model.setContactPic(contactList.get(i).getContactPic());
                        model.setContactName(contactList.get(i).getContactName());
                        model.setContactSelected(contactList.get(i).getContactSelected());
                        model.setContactNumber(contactList.get(i).getContactNumber());
                        model.setContactId(contactList.get(i).getContactId());
                           myPollBookContactList.add(model);
                    }
                }

                setadapter();
            }


        }
        else
        {


            myPollBookContactList = new ArrayList<>();
            for (int i = 0; i < contactList.size(); i++) {
                if (contactList.get(i).getIsMPBContact().equals("true")) {
                    ContactModel model = new ContactModel();
                    model.setContactMPBName(contactList.get(i).getContactMPBName());
                    model.setContactPic(contactList.get(i).getContactPic());
                    model.setContactName(contactList.get(i).getContactName());
                    model.setContactSelected(contactList.get(i).getContactSelected());
                    model.setContactNumber(contactList.get(i).getContactNumber());
                    model.setContactId(contactList.get(i).getContactId());

                    for(int j=0;j<existingContacts.size();j++)
                    {
                        if(existingContacts.get(j).getContactNumber().equalsIgnoreCase(model.getContactNumber())||
                                existingContacts.get(j).getContactNumber().contains(model.getContactNumber())||
                                model.getContactNumber().contains(existingContacts.get(j).getContactNumber())
                        )
                        {
                            model.setContactSelected ("TRUE");
                        }
                    }
                    myPollBookContactList.add(model);
                }
            }


            if (myPollBookContactList.size() > 0) {

                setadapter();

            }

            ArrayList<ContactModel> list = new ArrayList<>();

            for (ContactModel obj : myPollBookContactList) {
                if (obj.getIsContactSelected().equals("TRUE")) {
                    list.add(obj);
                }

            }

            selectedContactsAdapter = new SelectedContactsAdapter(this, AllContactsActivity.this, list);
            rcv_selected_contacts.setAdapter(selectedContactsAdapter);
        }


        db.close();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == 2128) {
            if(resultCode == Activity.RESULT_OK){
                if(from){
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }else{
                    String result=data.getStringExtra("result");
                    finish();

                }


            }


            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    private void getContacts() {


        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME +
                " COLLATE LOCALIZED ASC";
        final ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, sortOrder);
        String lastnumber = "0";
        contactToSend="";
        if(c.getCount()>0)
        {

            MDatabaseHelper dbHelper = new MDatabaseHelper(mContext);
            for (int i=0; i<c.getCount(); i++)
            {
                if(!c.isLast())
                {
                    c.moveToNext();
                    ContactModel model = new ContactModel();
                    String name = c.getString(c.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));
                    model.setContactName(name);
                    model.setContactSelected("false");

                    String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

                    if(c.getInt(c.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0)
                    {

                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {

                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phoneNo= phoneNo.replaceAll("\\s", "");
                            phoneNo=phoneNo.replaceAll("-","");

                            if(phoneNo.length()>8) {

                                if (phoneNo.equals(lastnumber)) {

                                } else {
                                    lastnumber = phoneNo;

                                    Log.e("lastnumber ", lastnumber);
                                    int type = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                    switch (type) {
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                            Log.e("Not Inserted", "Not inserted");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                            if (contactToSend.equals("")) {
                                                contactToSend = phoneNo;
                                                model.setContactNumber(phoneNo);
                                            } else {
                                                if (!contactToSend.contains(phoneNo)) {
                                                    contactToSend = contactToSend + "," + phoneNo;
                                                }
                                            }
                                            contactList.add(model);
                                            dbHelper.addContactToList(name, phoneNo, "false", "", "", "");
                                            break;
                                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                            Log.e("Not Inserted", "Not inserted");
                                            break;
                                    }

                                }
                            }

                        }
                        pCur.close();
                    }



                   /* if(!c.isLast())
                    {
                        i=i+1;
                        c.moveToNext();
                    }*/
                }
            }
            if(!contactToSend.equals(""))
            {
                dbHelper.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });



                /*  final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {}
                }, 2000);*/
            }
        }
    }

    private void serviceCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GroupsApiInterface service = retrofit.create(GroupsApiInterface.class);

        Call<JsonElement> call = service.checkExistingContacts(new GetGroupsPostParameters(checkContactAction, contactToSend));
        call.enqueue(new Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    //Toast.makeText(mContext, jsonObject.get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                    Log.v("ContactResponce",jsonObject.toString());
                    if( jsonObject.get("success").getAsString().equals("1"))
                    {
                        JsonArray result = jsonObject.get("results").getAsJsonArray();
                        contacts = "";
                        if(result.size()>0)
                        {
                            MDatabaseHelper dbHelper = new MDatabaseHelper(mContext);
                            for (int i=0; i<result.size(); i++)
                            {
                                JsonObject object = result.get(i).getAsJsonObject();
                                MApplication.setString(mContext, Constants.CONTACT_LIST, contacts);
                                if(!object.get("profile_image").getAsString().equals(""))
                                {
                                    dbHelper.updateContactList(object.get("mobile_number").getAsString(), object.get("profile_image").getAsString(), object.get("name").getAsString(), "true");
                                }
                                else {
                                    dbHelper.updateContactList(object.get("mobile_number").getAsString(),"", object.get("name").getAsString(), "true");
                                }
                            }
                            contactList = dbHelper.getAllContactsList();
                            existingContacts = dbHelper.getAllSelectedContacts();
                            dbHelper.close();
                            if(!isEdit) {
                                if (contactList.size() > 0) {
                                    myPollBookContactList = new ArrayList<>();
                                    for (int i = 0; i < contactList.size(); i++) {
                                        if (contactList.get(i).getIsMPBContact().equals("true")) {
                                            ContactModel model = new ContactModel();
                                            model.setContactMPBName(contactList.get(i).getContactMPBName());
                                            model.setContactPic(contactList.get(i).getContactPic());
                                            model.setContactName(contactList.get(i).getContactName());
                                            model.setContactSelected(contactList.get(i).getContactSelected());
                                            model.setContactNumber(contactList.get(i).getContactNumber());
                                            model.setContactId(contactList.get(i).getContactId());
                                            myPollBookContactList.add(model);
                                        }
                                    }
                                    //dbHelper.deleteSelectedContacts();
                                    dbHelper.close();
                                 setadapter();
                                }





                            }
                            else {
                                boolean status = false;
                                myPollBookContactList = new ArrayList<>();
                               for(int i=0; i<contactList.size(); i++)
                               {
                                   for (int j=0; j<existingContacts.size(); j++)
                                   {
                                       if(contactList.get(i).getContactNumber().equals(existingContacts.get(j).getContactNumber()))                                       {
                                           if (contactList.get(i).getIsMPBContact().equals("true")) {
                                               ContactModel model = new ContactModel();
                                               model.setContactMPBName(contactList.get(i).getContactMPBName());
                                               model.setContactPic(contactList.get(i).getContactPic());
                                               model.setContactName(contactList.get(i).getContactName());
                                               model.setContactSelected("true");
                                               model.setContactNumber(contactList.get(i).getContactNumber());
                                               model.setContactId(contactList.get(i).getContactId());
                                               myPollBookContactList.add(model);
                                               break;
                                           }
                                       }
                                       else {
                                           if (contactList.get(i).getIsMPBContact().equals("true")) {
                                               ContactModel model = new ContactModel();
                                               model.setContactMPBName(contactList.get(i).getContactMPBName());
                                               model.setContactPic(contactList.get(i).getContactPic());
                                               model.setContactName(contactList.get(i).getContactName());
                                               model.setContactSelected(contactList.get(i).getContactSelected());
                                               model.setContactNumber(contactList.get(i).getContactNumber());
                                               model.setContactId(contactList.get(i).getContactId());
                                               myPollBookContactList.add(model);
                                               break;
                                           }
                                       }
                                   }
                               }
                                if (myPollBookContactList.size() > 0) {

                                    setadapter();

                                }
                            }

                        }
                        else
                        {
                            MApplication.setString(mContext, Constants.CONTACT_LIST, contacts);
                        }

                    }
                    else {
                        norecords_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, jsonObject.get("msg").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    /*Toast toast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();*/
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
               // Toast toast = Toast.makeText(mContext , ""+t, Toast.LENGTH_LONG);
                //toast.show();

            }
        });



    }

    private void setadapter() {

        contactsAdapter = new AddGroupContactsAdapter(this,AllContactsActivity.this, myPollBookContactList,norecords_tv);
        lv_contacts.setAdapter(contactsAdapter);
    }


    class AysncTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
           // serviceCall();
            getContacts();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            refresh_fab.setEnabled(true);
            serviceCall();
        }
    }


    public void update(String phonenumber, int i){
        if(i==0) {
            ArrayList<ContactModel> list = new ArrayList<>();
            for (ContactModel obj : myPollBookContactList) {
                if (obj.getIsContactSelected().equals("TRUE")) {
                    list.add(obj);
                }

            }

            rcv_selected_contacts.setVisibility(View.VISIBLE);
            selectedContactsAdapter = new SelectedContactsAdapter(this, AllContactsActivity.this, list);
            rcv_selected_contacts.setAdapter(selectedContactsAdapter);
        }
        if( i==1)
        {
            for (ContactModel obj : myPollBookContactList) {
                if (phonenumber.equalsIgnoreCase(obj.getContactNumber())) {
                    obj.setIsContactSelected("FALSE");
                    db.deleteContactFromSelected(obj.getContactNumber());

                }
            }
            setadapter();

        }




    }

}


