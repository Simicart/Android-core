package com.simicart.core.catalog.filter.common;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.catalog.filter.entity.FilterEntity;
import com.simicart.core.config.Rconfig;

@SuppressLint("ViewHolder")
public class FilterAdapter extends BaseAdapter {

	protected ArrayList<FilterEntity> mData;
	protected Context mContext;

	public FilterAdapter(Context context, ArrayList<FilterEntity> data) {
		mContext = context;
		mData = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		FilterEntity entity = (FilterEntity) getItem(position);

		String title = entity.getmTitle();
		convertView = inflater.inflate(
				Rconfig.getInstance().layout("plugins_filter_item_listview"),
				null);
		TextView tv_name = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_filter"));
		if (null != title) {
			tv_name.setTextColor(Color.parseColor("#000000"));
			tv_name.setText(title);
		}

		ImageView iv_showmore = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("img_show_more"));
		iv_showmore.setImageResource(Rconfig.getInstance().drawable(
				"ic_action_expand"));

		return convertView;
	}

}
