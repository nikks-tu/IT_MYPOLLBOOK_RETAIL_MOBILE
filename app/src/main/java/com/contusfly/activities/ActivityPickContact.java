package com.contusfly.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.contusfly.MApplication;
import com.contusfly.adapters.AdapterPickContact;
import com.contusfly.model.Contact;
import com.contusfly.utils.Constants;
import com.contusfly.utils.Utils;
import com.contusfly.utils.WrapContentLayoutManager;
import com.contusfly.views.CustomRecyclerView;
import com.contusfly.views.CustomToast;
import com.polls.polls.R;

import java.util.ArrayList;
import java.util.List;


/**
 * The Activity class which acts as the base class for all other Activity classes for this
 * application, All other activities must extend this class so that they can receive the callbacks
 * of the chat service through the overriding methods of other this class all the callbacks
 * will be received from the broadcast receiver of this class.
 *
 * @author ContusTeam <developers@contus.in>
 * @version 1.1
 */
public class ActivityPickContact extends AppCompatActivity {

	/**
	 * The contacts.
	 */
	private List<Contact> contacts;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_contact);
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		Utils.setUpToolBar(this, toolbar, getSupportActionBar(), getString(R.string.text_select_contact));
		MApplication mApplication = (MApplication) getApplicationContext();
		TextView txtName = (TextView) findViewById(R.id.txt_name);
		CustomRecyclerView listNumber = (CustomRecyclerView) findViewById(R.id.list_numbers);
		listNumber.setLayoutManager(new WrapContentLayoutManager(this));
		Intent intent = getIntent();
		contacts = intent.getParcelableArrayListExtra(Constants.USERNAME);
		String name = mApplication.returnEmptyStringIfNull(contacts.get(0).getContactName());
		if (name.isEmpty())
			name = getString(R.string.text_unknown);
		txtName.setText(name);
		AdapterPickContact adapterPickContact = new AdapterPickContact(this);
		adapterPickContact.setData(contacts);
		listNumber.setAdapter(adapterPickContact);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.action_edit);
		item.setIcon(R.drawable.ic_action_ok);
		item.setTitle(getString(R.string.text_done));
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_edit)
			validateSelection();
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Validate selection.
	 */
	private void validateSelection() {
		boolean isSelected = false;
		for (Contact item : contacts) {
			isSelected = item.getIsSelected() == 1;
			if (isSelected)
				break;
		}
		if (isSelected) {
			Intent intent = new Intent();
			intent.putParcelableArrayListExtra(Constants.USERNAME, (ArrayList<Contact>) contacts);
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else
			CustomToast.showToast(this, getString(R.string.text_select_atleast_one));
	}
}
