package com.simicart.core.catalog.category.block;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.simicart.core.adapter.SortAdapter;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.category.delegate.SortDelegate;
import com.simicart.core.catalog.category.entity.Sort;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class SortBlock extends SimiBlock implements SortDelegate {

	String sort_option = "";

	protected ListView lv_sort;
	protected String sort_tag;

	public SortBlock(View view, Context context) {
		super(view, context);
	}
	
	public void setSort_tag(String sort_tag) {
		this.sort_tag = sort_tag;
	}

	public void setSort_option(String sort_option) {
		this.sort_option = sort_option;
	}

	public void setSortOptionClick(OnItemClickListener onItemClick) {
		lv_sort.setOnItemClickListener(onItemClick);
	}

	@Override
	public void initView() {
		TextView sort_title = (TextView) mView.findViewById(Rconfig
				.getInstance().id("sort_title"));
		sort_title.setText(Html.fromHtml("<b>"
				+ Config.getInstance().getText("Sort") + "</b>"));
		lv_sort = (ListView) mView.findViewById(Rconfig.getInstance().id(
				"sort_list"));
	}

	@Override
	public void drawView(SimiCollection collection) {
	}

	@Override
	public void setListSort(ArrayList<Sort> mListSort) {
		SortAdapter sortAdapter = new SortAdapter(mContext, mListSort,
				sort_option);
//		sortAdapter.setListSort(listSort);
		TextView sort_title = (TextView) mView.findViewById(Rconfig
				.getInstance().id("sort_title"));
		sort_title.setText(Config.getInstance().getText("Sort"));
		lv_sort.setAdapter(sortAdapter);
	}

	@Override
	public String getSortTag() {
		return sort_tag;
	}
}
