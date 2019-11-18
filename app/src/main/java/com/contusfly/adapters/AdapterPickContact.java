/**
 * @category ContusMessanger
 * @package com.time.adapters
 * @version 1.0
 * @author ContusTeam <developers@contus.in>
 * @copyright Copyright (C) 2015 <Contus>. All rights reserved.
 * @license http://www.apache.org/licenses/LICENSE-2.0
 */
package com.contusfly.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.contusfly.model.Contact;
import com.polls.polls.R;

import java.util.List;


/**
 * Adapter class for picking the contact.
 * @version 1.1
 * @author ContusTeam <developers@contus.in>
 */
public class AdapterPickContact extends RecyclerView.Adapter<AdapterPickContact.ViewHolder> {

	/**
	 * The rosters list.
	 */
	private List<Contact> contacts;

	/**
	 * The context.
	 */
	private Context context;

	/**
	 * Instantiates a new adapter countries.
	 *
	 * @param context the context
	 */
	public AdapterPickContact(Context context) {
		this.context = context;
	}

	/**
	 * Sets the data.
	 *
	 * @param contacts the new data
	 */
	public void setData(List<Contact> contacts) {
		this.contacts = contacts;
	}

	@Override
	public AdapterPickContact.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return new ViewHolder(inflater.inflate(R.layout.row_pick_contact, parent, false));
	}

	@Override
	public void onBindViewHolder(final AdapterPickContact.ViewHolder holder, final int position) {
		final Contact item = contacts.get(position);
		holder.txtNumber.setText(item.getContactNos());
		holder.checkBox.setChecked(item.getIsSelected() == 1);
		holder.viewRow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int selection = item.getIsSelected() == 1 ? 0 : 1;
				item.setIsSelected(selection);
				holder.checkBox.setChecked(selection == 1);
			}
		});
	}

	@Override
	public int getItemCount() {
		return contacts.size();
	}

	/**
	 * The Class ViewHolder.
	 */
	public class ViewHolder extends RecyclerView.ViewHolder {

		/**
		 * The txt name.
		 */
		TextView txtNumber;

		/**
		 * The check box.
		 */
		CheckBox checkBox;

		/**
		 * The view row.
		 */
		View viewRow;

		/**
		 * Instantiates a new view holder.
		 *
		 * @param view the view
		 */
		public ViewHolder(View view) {
			super(view);
			txtNumber = (TextView) view.findViewById(R.id.txt_number);
			checkBox = (CheckBox) view.findViewById(R.id.checkbox_user);
			viewRow = view.findViewById(R.id.view_number);
		}
	}
}