package com.simicart.core.catalog.search.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.catalog.search.entity.ItemListPopup;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class ListPopupAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ItemListPopup> listItems;

	private int mIDIconNormal;
	private int mIDIconChecked;
	private Drawable icon_nomal;
	private Drawable icon_checked;

	public ListPopupAdapter(Context context, ArrayList<ItemListPopup> listItem) {
		this.mContext = context;
		this.listItems = listItem;
		mIDIconNormal = Rconfig.getInstance().drawable("core_radiobox");
		mIDIconChecked = Rconfig.getInstance().drawable("core_radiobox2");
		icon_nomal = mContext.getResources().getDrawable(mIDIconNormal);
		icon_nomal.setColorFilter(Config.getInstance().getColorMain(),
				PorterDuff.Mode.SRC_ATOP);
		icon_checked = mContext.getResources().getDrawable(mIDIconChecked);
		icon_checked.setColorFilter(Config.getInstance().getColorMain(),
				PorterDuff.Mode.SRC_ATOP);
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater.inflate(
				Rconfig.getInstance().layout("core_item_popup_search"), null);
		ItemListPopup item = listItems.get(position);
		TextView txt_name = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("txt_name"));
		ImageView imageView = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("img_checksearch"));
		txt_name.setText(item.getName());
		if (item.isCheckSearch() == true) {
			imageView.setImageDrawable(icon_checked);
		} else {
			imageView.setImageDrawable(icon_nomal);
		}
		return convertView;
	}

}
