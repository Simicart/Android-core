package com.simicart.plugins.locator.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.entity.SpecialObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpecialAdapter extends ArrayAdapter<SpecialObject> {

	private Context context;
	private List<SpecialObject> list;

	public SpecialAdapter(Context context, List<SpecialObject> list) {
		super(context, Rconfig.getInstance().getId("plugins_store_locator_item_list_special", "layout"), list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater
					.inflate(Rconfig.getInstance().getId("plugins_store_locator_item_list_special", "layout"), null);
		}
		final SpecialObject object = list.get(position);
		Calendar cal = Calendar.getInstance();
		String[] date = object.getDate().split("-");
		cal.set(Integer.parseInt(date[0]), (Integer.parseInt(date[1]) - 1), Integer.parseInt(date[2]));
		SimpleDateFormat mFormat = new SimpleDateFormat("EEE MMM dd");
		TextView txt = (TextView) convertView.findViewById(Rconfig.getInstance().getIdLayout("txt_special"));
		txt.setText(mFormat.format(cal.getTime()) + "  " + object.getTime_open() + " - " + object.getTime_close());
		txt.setTextSize(15);
		return convertView;
	}
}
