package com.simicart.plugins.rewardpoint.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.entity.ItemHistory;

public class AdapterListviewHistory extends ArrayAdapter<ItemHistory> {

	private Context mContext;
	private List<ItemHistory> listItem = new ArrayList<ItemHistory>();

	public AdapterListviewHistory(Context context, List<ItemHistory> listItem) {
		super(context, Rconfig.getInstance().layout(
				"plugins_rewardpoint_itemlistview_history"), listItem);
		this.mContext = context;
		this.listItem = listItem;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(
					Rconfig.getInstance().layout(
							"plugins_rewardpoint_itemlistview_history"), null);
		}
		ItemHistory itemHistory = listItem.get(position);
		TextView txt_title = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("txt_itemhistory_title"));
		TextView txt_pointlabel = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("txt_itemhistory_pointlabel"));
		TextView txt_createtime = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("txt_itemhistory_createtime"));
		TextView txt_expiration_time = (TextView) convertView
				.findViewById(Rconfig.getInstance().id(
						"txt_itemhistory_expiration_date"));

		if (Utils.validateString(itemHistory.getTitle()) == true) {
			txt_title.setText(itemHistory.getTitle());
		}
		if (Utils.validateString(itemHistory.getPointLabel()) == true) {
			txt_pointlabel.setText(itemHistory.getPointLabel());
		}
		if (Utils.validateString(itemHistory.getCreateTime()) == true) {
			txt_createtime.setText(itemHistory.getCreateTime());
		}
		if (Utils.validateString(itemHistory.getExpirationTime()) == true) {
			txt_expiration_time.setVisibility(View.VISIBLE);
			txt_expiration_time.setText(Config.getInstance().getText(
					"Expire on")
					+ ": " + itemHistory.getExpirationTime());
		} else {
			txt_expiration_time.setVisibility(View.GONE);
		}
		return convertView;
	}

}
