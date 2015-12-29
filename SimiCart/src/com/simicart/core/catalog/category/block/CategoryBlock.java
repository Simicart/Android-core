package com.simicart.core.catalog.category.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.category.adapter.CategoryBaseAdapter;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.NoScrollListView;

public class CategoryBlock extends SimiBlock {
	protected String mName;
	protected NoScrollListView lv_Category;
	protected CategoryBaseAdapter mAdapter;
	private LinearLayout ll_category;

	public CategoryBlock(View view, Context context) {
		super(view, context);
	}

	public void setCategoryName(String name) {
		mName = name;
	}

	public void setClicker(OnItemClickListener clicker) {
		lv_Category.setOnItemClickListener(clicker);
	}

	@Override
	public void initView() {
		lv_Category = (NoScrollListView) mView.findViewById(Rconfig
				.getInstance().id("lv_categories"));
		ll_category =  (LinearLayout) mView.findViewById(Rconfig
				.getInstance().id("ll_categories"));
		View v_line = (View) mView.findViewById(Rconfig.getInstance().id(
				"v_line2"));
		ColorDrawable sage ;
		if(DataLocal.isTablet) {
			 sage = new ColorDrawable(Config.getInstance()
						.getMenu_line_color());
			 ll_category.setBackgroundColor(Config.getInstance().getMenu_background());
			 v_line.setBackgroundColor(Config.getInstance().getMenu_line_color());
		}else {
			 sage = new ColorDrawable(Config.getInstance()
						.getLine_color());
			 v_line.setBackgroundColor(Config.getInstance().getLine_color());
		}
		lv_Category.setDivider(sage);
		lv_Category.setDividerHeight(1);

		
		
	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			ArrayList<Category> categories = new ArrayList<Category>();
			for (SimiEntity simiEntity : entity) {
				Category category = (Category) simiEntity;
				categories.add(category);
			}
			if (categories.size() > 0) {
				if (null == mAdapter) {
					mAdapter = new CategoryBaseAdapter(mContext, categories);
					lv_Category.setAdapter(mAdapter);
				} else {
					mAdapter.setCategories(categories);
					mAdapter.notifyDataSetChanged();
				}
			}
		}

	}

}
