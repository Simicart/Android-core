package com.simicart.core.catalog.filter.common;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.delegate.FilterRequestDelegate;
import com.simicart.core.catalog.filter.entity.FilterState;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

@SuppressLint("ViewHolder")
public class SelectedFilterAdapter extends BaseAdapter {

	protected ArrayList<FilterState> mStates;
	protected Context mContext;
	protected FilterRequestDelegate mDelegate;

	public void setDelegate(FilterRequestDelegate delegate) {
		mDelegate = delegate;
	}

	public SelectedFilterAdapter(ArrayList<FilterState> states, Context context) {
		mContext = context;
		mStates = states;
	}

	@Override
	public int getCount() {
		return mStates.size();
	}

	@Override
	public Object getItem(int position) {
		return mStates.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final FilterState state = (FilterState) getItem(position);
		String title = state.getTitle();
		String label = state.getLabel();

		LayoutInflater inflate = LayoutInflater.from(mContext);
		convertView = inflate.inflate(
				Rconfig.getInstance().layout("plugins_item_selected_filter"),
				null, false);

		String text = String.format("%s : %s", title, label);

		TextView tv_filter = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_filter"));
		tv_filter.setText(text);

		ImageView img_delete = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("img_delete"));
		img_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != mDelegate) {
					mDelegate.clearFilter(state);
				}
				if (DataLocal.isTablet) {
					SimiManager.getIntance().popFragmentDialog();
					SimiManager.getIntance().removeDialog();
					List<Fragment> list = SimiManager.getIntance()
							.getManager().getFragments();
					for (Fragment fragment : list) {
						if (fragment != null
								&& fragment.isVisible()
								&& fragment.getTargetRequestCode() == ConfigCheckout.TARGET_LISTPRODUCT) {
							fragment.onResume();
						}
					}
					
				} else {
					SimiManager.getIntance().backPreviousFragment();
				}

			}
		});

		return convertView;
	}

}
