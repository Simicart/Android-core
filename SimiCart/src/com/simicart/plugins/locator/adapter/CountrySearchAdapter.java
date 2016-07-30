package com.simicart.plugins.locator.adapter;

import java.util.List;

import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.entity.CountryObject;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CountrySearchAdapter extends BaseAdapter {
	private List<CountryObject> list;
	LayoutInflater inflater;

	public CountrySearchAdapter(Context context, List<CountryObject> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position).getCountry_name();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(
				Rconfig.getInstance()
						.getId("core_item_gender_layout", "layout"), null);
		TextView textView = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_gendername"));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		textView.setText(list.get(position).getCountry_name());
		if (DataLocal.isLanguageRTL) {
			textView.setGravity(Gravity.RIGHT);
		}
		return convertView;
	}

}
