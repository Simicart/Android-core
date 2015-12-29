package com.simicart.core.home.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.magestore.simicart.R;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class HomeCategoryAdapter extends BaseAdapter {

	protected Context mContext;
	protected ArrayList<Category> listCategory;

	public HomeCategoryAdapter(Context context, ArrayList<Category> categorys) {
		this.mContext = context;
		this.listCategory = categorys;
	}

	@Override
	public int getCount() {
		return listCategory.size();
	}

	@Override
	public Category getItem(int position) {
		return listCategory.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater.inflate(
				Rconfig.getInstance().layout("core_home_category_item"), null);
		if (listCategory.size() == 1) {
			convertView.setPadding(Utils.getValueDp(6), 0, Utils.getValueDp(2),
					0);
		} else if (listCategory.size() > 1) {
			if (position == 0) {
				convertView.setPadding(Utils.getValueDp(6), 0,
						Utils.getValueDp(2), 0);
			} else if (position == (listCategory.size() - 1)) {
				convertView.setPadding(Utils.getValueDp(2), 0,
						Utils.getValueDp(6), 0);
			} else {
				convertView.setPadding(Utils.getValueDp(2), 0,
						Utils.getValueDp(2), 0);
			}
		}

		Category category = getItem(position);

		TextView txt_name = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("txt_category_item_home"));
		txt_name.setText(category.getCategoryName());
		txt_name.setTextColor(Config.getInstance().getContent_color());
		if (DataLocal.isLanguageRTL) {
			txt_name.setGravity(Gravity.RIGHT);
		} else {
			txt_name.setGravity(Gravity.LEFT);
		}

		ImageView img_category = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("img_category_item_home"));
		String urlImage = category.getCategoryImage();

		if (category.getCategoryId().equals("fake")) {
			img_category.setImageResource(Rconfig.getInstance().drawable(
					"fake_category"));
			img_category.setScaleType(ScaleType.FIT_XY);
		} else {
			DrawableManager.fetchDrawableOnThread(urlImage, img_category);
		}
		return convertView;
	}

}
