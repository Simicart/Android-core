package com.simicart.plugins.locator.adapter;

import java.util.List;

import com.simicart.core.config.Rconfig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TagSearchAdapter extends ArrayAdapter<String> {
	private Context context;
	private List<String> list;
	private int pageIndex = 0;
	private int count_tag = 0;

	public TagSearchAdapter(Context context, List<String> list, int count_tag) {
		super(context, Rconfig.getInstance().getId("plugins_store_locator_item_tag",
				"layout"), list);
		this.context = context;
		this.list = list;
		this.count_tag = count_tag;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(
					Rconfig.getInstance().getId(
							"plugins_storelocator_item_tag", "layout"),
					null);
		}
		final String item = list.get(position);

		final ImageView icon_item = (ImageView) convertView
				.findViewById(Rconfig.getInstance()
						.getIdLayout("icon_item"));
		final TextView txt_item = (TextView) convertView
				.findViewById(Rconfig.getInstance().getIdLayout("txt_item"));
		final LinearLayout item_tag = (LinearLayout) convertView
				.findViewById(Rconfig.getInstance().getIdLayout("item_tag"));
		if (position == count_tag) {
			item_tag.setBackgroundDrawable(context.getResources().getDrawable(
					Rconfig.getInstance().getIdDraw(
							"plugins_locator_drawble_selec")));
			icon_item.setImageDrawable(context.getResources().getDrawable(
					Rconfig.getInstance().getIdDraw(
							"plugins_locator_ic_store_selec")));
			txt_item.setTextColor(context.getResources().getColor(
					android.R.color.holo_orange_dark));
		} else {
			item_tag.setBackgroundDrawable(context.getResources().getDrawable(
					Rconfig.getInstance().getIdDraw(
							"plugins_locator_drawble_search")));
			icon_item.setImageDrawable(context.getResources().getDrawable(
					Rconfig.getInstance().getIdDraw(
							"plugins_locator_ic_store")));
			txt_item.setTextColor(context.getResources().getColor(
					android.R.color.black));
		}
//		if (position == getCount() - 1 && getCount() >= 9) {
//			if ((list.size() - 1) % 10 == 0) {
//				pageIndex++;
//				if (NetworkConnection.haveInternet(context)) {
//					TagNewLoad taskLoad = new TagNewLoad();
//					taskLoad.data = getObjectTag(String
//							.valueOf(pageIndex * 10));
//					taskLoad.execute();
//				} else {
//					Toast.makeText(context, "No NetWork Connection",
//							Toast.LENGTH_LONG).show();
//				}
//				if (list.size() == 0) {
//					// mfoot.setVisibility(View.GONE);
//				}
//			} else {
//				// mfoot.setVisibility(View.GONE);
//			}
//		}

//		item_tag.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					item_tag.setBackgroundDrawable(context.getResources()
//							.getDrawable(
//									Rconfig.getInstance().getIdDraw(
//											"plugins_locator_drawble_ho")));
//					icon_item.setImageDrawable(context.getResources().getDrawable(
//							Rconfig.getInstance().getIdDraw(
//									"plugins_locator_ic_store_ho")));
//					txt_item.setTextColor(context.getResources().getColor(
//							android.R.color.white));
//				} else if (event.getAction() == MotionEvent.ACTION_UP) {
//					item_tag.setBackgroundDrawable(context.getResources()
//							.getDrawable(
//									Rconfig.getInstance()
//											.getIdDraw(
//													"plugins_locator_drawble_selec")));
//					icon_item.setImageDrawable(context.getResources().getDrawable(
//							Rconfig.getInstance().getIdDraw(
//									"plugins_locator_ic_store_selec")));
//					txt_item.setTextColor(context.getResources().getColor(
//							android.R.color.holo_orange_dark));
//					StoreLocatorFragment fragment;
//					tag = item;
//					if (position == 0) {
//						fragment = StoreLocatorFragment.newInstansce(
//								country_code, et_city.getText().toString(),
//								et_state.getText().toString(), et_code
//										.getText().toString(), "", null);
//						_item = "";
//					} else {
//						fragment = StoreLocatorFragment.newInstansce(
//								country_code, et_city.getText().toString(),
//								et_state.getText().toString(), et_code
//										.getText().toString(), item, search_object);
//						_item = item;
//					}
//					search_object.setTag(position);
////					fragment.setSearch_object(search_object);
//					SimiManager.getIntance().addFragment(fragment);
//					SimiManager.getIntance().removeDialog();
//					count_tag = position;
//					notifyDataSetChanged();
//				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//					item_tag.setBackgroundDrawable(getResources()
//							.getDrawable(
//									Rconfig.getInstance()
//											.getIdDraw(
//													"plugins_locator_drawble_search")));
//					icon_item.setImageDrawable(getResources().getDrawable(
//							Rconfig.getInstance().getIdDraw(
//									"plugins_locator_ic_store")));
//					txt_item.setTextColor(getResources().getColor(
//							android.R.color.black));
//				}
//				return true;
//			}
//		});
		txt_item.setText(item);
		return convertView;
	}
}
