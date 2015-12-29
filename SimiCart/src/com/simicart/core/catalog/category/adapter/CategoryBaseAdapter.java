package com.simicart.core.catalog.category.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class CategoryBaseAdapter extends BaseAdapter {

	protected Context context = null;
	protected ArrayList<Category> categoryList = null;
	protected LayoutInflater inflater;

	public CategoryBaseAdapter(Context context, ArrayList<Category> listMh) {
		this.context = context;
		this.categoryList = listMh;
		this.inflater = LayoutInflater.from(context);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(
				Rconfig.getInstance().layout("core_item_list_category_layout"),
				null);
		if(DataLocal.isTablet) {
			convertView.setBackgroundColor(Config.getInstance().getMenu_background());
		}
		TextView tv_name = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_catename"));
		String cateName = this.categoryList.get(position).getCategoryName();
		tv_name.setText(cateName);
		if (DataLocal.isLanguageRTL) {
			tv_name.setGravity(Gravity.RIGHT);
		}

		ImageView iv_showmore = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("iv_showmore"));
		if (DataLocal.isTablet) {
			tv_name.setTextColor(Config.getInstance().getMenu_text_color());
			tv_name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			Drawable icon = context.getResources().getDrawable(
					Rconfig.getInstance().drawable("ic_menu_extended"));
			icon.setColorFilter(Config.getInstance().getMenu_icon_color(),
					PorterDuff.Mode.SRC_ATOP);
			iv_showmore.setImageDrawable(icon);
		} else {
			tv_name.setTextColor(Config.getInstance().getContent_color());
			Drawable icon = context.getResources().getDrawable(
					Rconfig.getInstance().drawable("ic_extend"));
			icon.setColorFilter(Config.getInstance().getContent_color(),
					PorterDuff.Mode.SRC_ATOP);
			iv_showmore.setImageDrawable(icon);
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return this.categoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setCategories(ArrayList<Category> categories) {
		categoryList = categories;
	}
}
