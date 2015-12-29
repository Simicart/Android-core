package com.simicart.core.catalog.filter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.delegate.FilterRequestDelegate;
import com.simicart.core.catalog.filter.entity.FilterEntity;
import com.simicart.core.catalog.filter.entity.FilterState;
import com.simicart.core.catalog.filter.fragment.FilterFragment;
import com.simicart.core.common.ViewIdGenerator;

public class FilterView {

	protected Context mContext;
	protected ArrayList<FilterEntity> mFilterEntity;
	protected FilterRequestDelegate mDelegate;
	protected ArrayList<FilterState> mStates;

	public void setState(ArrayList<FilterState> states) {
		mStates = states;
	}

	public void setDelegate(FilterRequestDelegate delegate) {
		mDelegate = delegate;
	}

	public FilterView(Context context, ArrayList<FilterEntity> filterEntity,
			FilterRequestDelegate delegate) {
		mContext = context;
		mFilterEntity = filterEntity;
		mDelegate = delegate;
	}

	public View initView(final String catName) {
		Button btn_filter = new Button(mContext);
		btn_filter.setId(ViewIdGenerator.generateViewId());
		// btn_filter.setText(Config.getInstance().getText("Filter"));
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		btn_filter.setLayoutParams(params);
		// int padding = Utils.getValueDp(4);
		// btn_filter.setPadding(padding, padding, padding, padding);
		btn_filter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onFilter(catName);
			}
		});
		return btn_filter;
	}

	protected void onFilter(String catName) {
		FilterFragment fragment = FilterFragment.newInstance();
		fragment.setDelegate(mDelegate);
		fragment.setFilterEntity(mFilterEntity);
		fragment.setCatName(catName);
		fragment.setState(mStates);
		SimiManager.getIntance().replacePopupFragment(fragment);
	}

}
