package com.simicart.core.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.simicart.core.catalog.category.entity.Sort;
import com.simicart.core.config.Rconfig;

public class SortAdapter extends BaseAdapter {
	ArrayList<Sort> listSort;
	Context context;
	LayoutInflater inflater;
	String sort_option;

	public SortAdapter(Context context, ArrayList<Sort> listSort,
			String sortoption) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.listSort = listSort;
		this.sort_option = sortoption;
		if (sort_option == null || sort_option.equals("")
				|| sort_option.equals("None")) {
			this.sort_option = "0";
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listSort.size();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = this.inflater.inflate(
				Rconfig.getInstance().layout("core_item_sort"), null);
		TextView title = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("sort_text"));
		title.setText(this.listSort.get(position).getTitle());
		String op = "" + this.listSort.get(position).getId();
		if (!op.equals(this.sort_option)) {
			CheckedTextView check = (CheckedTextView) convertView
					.findViewById(Rconfig.getInstance().id("sort_check"));
			check.setBackgroundColor(0);
		}
		return convertView;
	}

	public void setListSort(ArrayList<Sort> listSort) {
		this.listSort = listSort;
	}
}
