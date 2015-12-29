package com.simicart.core.customer.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.MyAddress;

public class AddressBookAdapter extends BaseAdapter {

	Context mContext = null;
	ArrayList<MyAddress> myAddresses = null;
	boolean is_order = false;

	public void isOrder(boolean is_order) {
		this.is_order = is_order;
	}

	public void setAddress(ArrayList<MyAddress> addresses) {
		myAddresses = addresses;
	}

	public AddressBookAdapter(Context context, ArrayList<MyAddress> myAddresses) {
		this.mContext = context;
		this.myAddresses = myAddresses;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		MyAddress myAddress = myAddresses.get(position);
		LayoutInflater inflater = LayoutInflater.from(mContext);

		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();

			if (DataLocal.isLanguageRTL) {
				convertView = inflater
						.inflate(
								Rconfig.getInstance().layout(
										"rtl_item_address_layout"), null);
			} else {
				convertView = inflater.inflate(
						Rconfig.getInstance()
								.layout("core_item_address_layout"), null);
			}

			holder.tv_name = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("name"));

			holder.tv_street = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("street"));

			holder.tv_city = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("city"));

			holder.tv_country = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("country"));
			holder.tv_phone = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("phone"));

			holder.tv_email = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("email"));
			
			holder.img_extend = (ImageView) convertView.findViewById(Rconfig
					.getInstance().id("image_expand"));

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundColor(Config.getInstance().getApp_backrground());
		Utils.changeColorTextView(holder.tv_name);
		Utils.changeColorTextView(holder.tv_street);
		Utils.changeColorTextView(holder.tv_city);
		Utils.changeColorTextView(holder.tv_country);
		Utils.changeColorTextView(holder.tv_phone);
		Utils.changeColorTextView(holder.tv_email);
		
		Utils.changeColorImageview(mContext, holder.img_extend,"ic_extend");

		// name
		String name = myAddress.getName();
		String prefix = myAddress.getPrefix();
		String suffix = myAddress.getSuffix();

		if (!Utils.validateString(prefix)) {
			holder.tv_name.setText(name + " " + suffix);
		} else {
			holder.tv_name.setText(prefix + " " + name + " " + suffix);
		}

		// Street
		String street = myAddress.getStreet();
		if (Utils.validateString(street)) {
			holder.tv_street.setText(myAddress.getStreet());
		}

		// city
		String state = myAddress.getStateName();
		String city = myAddress.getCity();
		String zip_code = myAddress.getZipCode();
		if (!Utils.validateString(state)) {
			holder.tv_city.setText(city + ", " + zip_code);
		} else {
			holder.tv_city.setText(city + ", " + state + ", " + zip_code);
		}

		// country
		String country = myAddress.getCountryName();
		if (Utils.validateString(country)) {
			holder.tv_country.setText(country);
		}

		// phone
		String phone = myAddress.getPhone();
		if (Utils.validateString(phone)) {
			holder.tv_phone.setText(phone);
		}

		// email
		String email = myAddress.getEmail();
		if (Utils.validateString(email)) {
			holder.tv_email.setText(email);
		}

		if (is_order) {
			ImageView img = (ImageView) convertView.findViewById(Rconfig
					.getInstance().id("image_expand"));
			img.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myAddresses.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_street;
		TextView tv_country;
		TextView tv_phone;
		TextView tv_email;
		TextView tv_city;
		ImageView img_extend;
	}

}
