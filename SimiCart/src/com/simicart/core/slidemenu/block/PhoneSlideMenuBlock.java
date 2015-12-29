package com.simicart.core.slidemenu.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.slidemenu.adapter.SlideMenuAdapter;
import com.simicart.core.slidemenu.delegate.SlideMenuDelegate;
import com.simicart.core.slidemenu.entity.ItemNavigation;

public class PhoneSlideMenuBlock implements SlideMenuDelegate {

	protected ListView lv_navigation;
	protected LinearLayout ll_personal;
	protected TextView tv_acc;
	protected View mView;
	protected Context mContext;
	protected SlideMenuAdapter mAdapter;

	public void setListener(OnItemClickListener listener) {
		lv_navigation.setOnItemClickListener(listener);
	}

	public PhoneSlideMenuBlock(View view, Context context) {
		mView = view;
		mContext = context;
	}

	public void initView() {
		lv_navigation = (ListView) mView.findViewById(Rconfig.getInstance().id(
				"lv_navigation"));
		ColorDrawable sage = new ColorDrawable(Config.getInstance()
				.getMenu_line_color());
		lv_navigation.setDivider(sage);
		lv_navigation.setDividerHeight(1);
		ll_personal = (LinearLayout) mView.findViewById(Rconfig.getInstance()
				.id("ll_personal"));
		View v_line = (View) mView.findViewById(Rconfig.getInstance().id(
				"v_line"));
		tv_acc = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_name"));

		ImageView img_icon = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_icon"));
		Drawable icon = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_menu_personal"));
		icon.setColorFilter(Config.getInstance().getMenu_icon_color(),
				PorterDuff.Mode.SRC_ATOP);
		img_icon.setImageDrawable(icon);

		ImageView img_extended = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_extended"));
		Drawable ic_menu_extended = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_menu_extended"));
		ic_menu_extended.setColorFilter(Config.getInstance()
				.getMenu_icon_color(), PorterDuff.Mode.SRC_ATOP);
		img_extended.setImageDrawable(ic_menu_extended);

		v_line.setBackgroundColor(Config.getInstance().getMenu_line_color());
		tv_acc.setTextColor(Config.getInstance().getMenu_text_color());

		if (DataLocal.isLanguageRTL) {
			tv_acc.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		}
	}

	public void setClicker(OnItemClickListener clicker) {
		lv_navigation.setOnItemClickListener(clicker);
	}

	public void setClickerPersonal(OnClickListener clicker) {
		ll_personal.setOnClickListener(clicker);
	}

	@Override
	public void onSelectedItem(int position) {
		lv_navigation.setItemChecked(position, true);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAdapter(ArrayList<ItemNavigation> items) {

		// if (null == mAdapter) {
		mAdapter = new SlideMenuAdapter(items, mContext);
		lv_navigation.setAdapter(mAdapter);
		// } else {
		// Log.e("PhoneSlideMenuBlock ", "setAdapter 002");
		// mAdapter.setItems(items);
		// mAdapter.notifyDataSetChanged();
		// }
	}

	@Override
	public void setUpdateSignIn(String name) {
		tv_acc.setText(name);
	}

}
