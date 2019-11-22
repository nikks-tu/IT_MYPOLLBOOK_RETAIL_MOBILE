package com.new_chanages.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.contus.app.Constants;
import com.contusfly.MApplication;
import com.contusfly.chooseContactsDb.DatabaseHelper;
import com.contusfly.model.MDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.new_chanages.adapters.AddGroupContactsAdapter;
import com.new_chanages.adapters.SelectedContactsAdapter;
import com.new_chanages.api_interface.GroupsApiInterface;
import com.new_chanages.listeners.RecyclerItemClickListener;
import com.new_chanages.models.ContactModel;
import com.new_chanages.postParameters.GetGroupsPostParameters;
import com.polls.polls.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AllContactsActivity extends AppCompatActivity  {

    ListView lv_contacts;
    Context mContext;
    ImageView iv_back, iv_save;
    FloatingActionButton fab_next;
    ArrayList<ContactModel> contactList;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);

        initialize();
        if(getIntent().getExtras()!=null)
        {
            if(getIntent().getStringExtra("fromActivity").equals("Edit"))
            {
                isEdit = true;
            }
        }
        //contactList = getContacts();


        AysncTask task=new AysncTask();
        task.execute();



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
                Intent intent = new Intent(mContext, GroupCreate.class);
                startActivity(intent);
            }
        });

        rcv_selected_contacts.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rcv_selected_contacts, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                MDatabaseHelper dbHelper = new MDatabaseHelper(mContext);
                selectedContactList = dbHelper.getAllSelectedContacts();
                dbHelper.deleteContactFromSelected(selectedContactList.get(position).getContactNumber());
                if(dbHelper.getAllSeletedContactSize()>0)
                {
                    rcv_selected_contacts.setVisibility(View.VISIBLE);
                    selectedContactList = dbHelper.getAllSelectedContacts();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,  LinearLayoutManager.HORIZONTAL, false);
                    selectedContactsAdapter = new SelectedContactsAdapter(mContext, selectedContactList);
                    rcv_selected_contacts.setLayoutManager(layoutManager);
                    rcv_selected_contacts.setAdapter(selectedContactsAdapter);
                }
                else {
                    rcv_selected_contacts.setVisibility(View.GONE);
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));



        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactList = new ArrayList<>();
                MDatabaseHelper dbHelper = new MDatabaseHelper(mContext);
                contactList = dbHelper.getAllContactsList();
                if(contactList.size()>0)
                {
                    Toast.makeText(mContext, ""+myPollBookContactList.size(), Toast.LENGTH_SHORT).show();

                   /* contacts = "";
                    for (int i=0; i<contactList.size(); i++)
                    {
                       if(contacts.equals(""))
                       {
                           contacts = contactList.get(i).getContactNumber();
                       }
                       else {
                           contacts = contacts+","+contactList.get(i).getContactNumber();
                       }
                    }*/
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




        lv_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox   checkbox_user = view.findViewById(R.id.checkbox_user);
                ContactModel model=new ContactModel();
                if(checkbox_user.isChecked())
                {
                    checkbox_user.setChecked(false);
                    contactsAdapter.notifyDataSetChanged();
                    MDatabaseHelper dbHelper = new MDatabaseHelper(mContext);
                    dbHelper.deleteContactFromSelected(myPollBookContactList.get(position).getContactNumber());
                    if(dbHelper.getAllSeletedContactSize()>0)
                    {
                        rcv_selected_contacts.setVisibility(View.VISIBLE);
                        selectedContactList = dbHelper.getAllSelectedContacts();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,  LinearLayoutManager.HORIZONTAL, false);
                        selectedContactsAdapter = new SelectedContactsAdapter(mContext, selectedContactList);
                        rcv_selected_contacts.setLayoutManager(layoutManager);
                        rcv_selected_contacts.setAdapter(selectedContactsAdapter);
                    }
                    else {
                        rcv_selected_contacts.setVisibility(View.GONE);
                    }
                }
                else {
                    checkbox_user.setChecked(true);
                    MDatabaseHelper dbHelper = new MDatabaseHelper(mContext);
                    dbHelper.addContactToSelected(myPollBookContactList.get(position).getContactName(),
                            myPollBookContactList.get(position).getContactNumber(), "true",
                            myPollBookContactList.get(position).getContactPic(),
                            myPollBookContactList.get(position).getContactMPBName(),"true");


                    if(dbHelper.getAllSeletedContactSize()>0)
                    {
                        rcv_selected_contacts.setVisibility(View.VISIBLE);
                        selectedContactList = dbHelper.getAllSelectedContacts();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext,  LinearLayoutManager.HORIZONTAL, false);
                        selectedContactsAdapter = new SelectedContactsAdapter(mContext, selectedContactList);
                        rcv_selected_contacts.setLayoutManager(layoutManager);
                        rcv_selected_contacts.setAdapter(selectedContactsAdapter);
                    }
                    else {
                        rcv_selected_contacts.setVisibility(View.GONE);
                    }
                }
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
        fab_next = findViewById(R.id.fab_next);
        searchView=findViewById(R.id.search_view);
        rcv_selected_contacts = findViewById(R.id.rcv_selected_contacts);
        existingContacts = new ArrayList<>();
        contactList = new ArrayList<>();
        selectedContactList = new ArrayList<>();
        MDatabaseHelper db = new MDatabaseHelper(mContext);


        //Search view

        EditText searchEditText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));



        //db.deleteSelectedContacts();
        db.deleteContactList();
        db.close();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    private void getContacts() {
        // Run query
        final ContentResolver cr = getContentResolver();
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME +
                " COLLATE LOCALIZED ASC";
        String[] projection = new String[] {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        final Cursor c = cr.query(ContactsContract.Data.CONTENT_URI, projection, null, null, sortOrder);
        /*final ContentResolver cr2 = getContentResolver();
        String[] projection2 = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};
        final Cursor c2 = cr2.query(ContactsContract.Data.CONTENT_URI, projection2, null, null, null);*/

        if(c.getCount()>0)
        {

            MDatabaseHelper dbHelper = new MDatabaseHelper(mContext);
            for (int i=0; i<c.getCount(); i++)
            {
                if(!c.isLast())
                {
                    c.moveToNext();
                    ContactModel model = new ContactModel();
                    model.setContactName(c.getString(0));
                    model.setContactSelected("false");
                    model.setContactNumber(c.getString(1));

                    if(c.getString(1)!=null)
                   {
                       String contact = c.getString(1);
                       if(contact.length()>9 && !contact.matches(".*[a-zA-Z]+.*"))
                       {
                           if(contactToSend.equals(""))
                           {
                               contactToSend = contact;
                           }
                           else {
                               contactToSend = contactToSend +"," + contact;
                           }
                           contactList.add(model);
                           dbHelper.addContactToList(c.getString(0), c.getString(1), "false", "", "", "");
                       }
                   }
                    if(!c.isLast())
                    {
                        i=i+1;
                        c.moveToNext();
                    }
                }
            }
            if(!contactToSend.equals(""))
            {
                dbHelper.close();

                progressBar.setVisibility(View.VISIBLE);
                serviceCall();

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
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    //Toast.makeText(mContext, jsonObject.get("msg").getAsString(), Toast.LENGTH_SHORT).show();
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
                                    dbHelper.close();
                                    contactsAdapter = new AddGroupContactsAdapter(mContext, myPollBookContactList);

                                    lv_contacts.setAdapter(contactsAdapter);
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
                                    contactsAdapter = new AddGroupContactsAdapter(mContext, myPollBookContactList);
                                    lv_contacts.setAdapter(contactsAdapter);
                                }
                            }

                        }
                        else
                        {
                            MApplication.setString(mContext, Constants.CONTACT_LIST, contacts);
                        }
                    }
                    else {
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
                Toast toast = Toast.makeText(mContext , ""+t, Toast.LENGTH_LONG);
                toast.show();

            }
        });

    }




    class AysncTask extends AsyncTask<String,String,String>{



        @Override
        protected String doInBackground(String... strings) {
           // serviceCall();
            getContacts();
            progressBar.setVisibility(View.INVISIBLE);
            return null;
        }
    }


}


