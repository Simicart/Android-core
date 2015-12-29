package com.simicart.core.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.StringMatcher;
import android.graphics.PorterDuff;
public class IndexableListAdapter extends BaseAdapter implements SectionIndexer {

	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	protected Context context = null;
	protected ArrayList<String> mList = null;
	protected LayoutInflater inflater;
	protected String itemChecked;

	public IndexableListAdapter(Context context, ArrayList<String> mList,
			String itemChecked) {
		this.context = context;
		this.itemChecked = itemChecked;
		this.mList = mList;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = section; i >= 0; i--) {
			for (int j = 0; j < getCount(); j++) {
				if (i == 0) {
					// For numeric section
					for (int k = 0; k <= 9; k++) {
						if (StringMatcher.match(
								String.valueOf(getItem(j).charAt(0)),
								String.valueOf(k)))
							return j;
					}
				} else {
					if (StringMatcher.match(
							String.valueOf(getItem(j).charAt(0)),
							String.valueOf(mSections.charAt(i))))
						return j;
				}
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(
				Rconfig.getInstance().layout("core_item_list_string"), null);
		TextView tv_label = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_label"));
		String labelItem = mList.get(position);
		tv_label.setTextColor(Config.getInstance().getContent_color());
		tv_label.setText(labelItem);

		ImageView im_check = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("im_check"));
		if (itemChecked.equals(labelItem)) {
			im_check.setVisibility(View.VISIBLE);
		}
		changeColorImageView(im_check, "ic_action_accept");
		
		return convertView;
	}

	private void changeColorImageView (ImageView img,String src) {
		Drawable icon = context.getResources().getDrawable(
				Rconfig.getInstance().drawable(src));
		icon.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		img.setImageDrawable(icon);
	}
	@Override
	public int getCount() {
		return this.mList.size();
	}

	@Override
	public String getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
