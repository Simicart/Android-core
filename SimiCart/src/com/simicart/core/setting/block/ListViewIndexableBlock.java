package com.simicart.core.setting.block;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.adapter.IndexableListAdapter;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.IndexableListView;

public class ListViewIndexableBlock extends SimiBlock {
	protected IndexableListView lv_language;
	protected IndexableListAdapter mAdapter;
	protected ArrayList<String> mList;
	protected String itemChecked;

	public ListViewIndexableBlock(View view, Context context) {
		super(view, context);
	}

	public void setOnItemClicker(OnItemClickListener clicker) {
		lv_language.setOnItemClickListener(clicker);
	}

	@Override
	public void initView() {
		lv_language = (IndexableListView) mView.findViewById(Rconfig
				.getInstance().id("listview"));
		ColorDrawable sage = new ColorDrawable(Config.getInstance()
				.getLine_color());
		lv_language.setDivider(sage);
		lv_language.setDividerHeight(1);
		if (mList.size() > 0) {
			if (null == mAdapter) {
				Collections.sort(mList);
				mAdapter = new IndexableListAdapter(mContext, mList,
						itemChecked);
				lv_language.setAdapter(mAdapter);
			}
		}
		lv_language.setFastScrollEnabled(true);

	}

	@Override
	public void drawView(SimiCollection collection) {
	}

	public void setList(ArrayList<String> list_string) {
		this.mList = list_string;
	}

	public void setItemChecked(String itemChecked) {
		this.itemChecked = itemChecked;
	}
}
