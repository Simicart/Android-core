package com.simicart.plugins.locator.adapter;

import java.util.List;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.common.DataLocator;
import com.simicart.plugins.locator.entity.StoreObject;
import com.simicart.plugins.locator.fragment.MapViewFragment;
import com.simicart.plugins.locator.style.RoundedImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoreListAdapter extends ArrayAdapter<StoreObject> {
	private final Context context;
	private List<StoreObject> listObject;
	private int pageIndex = 0;

	public StoreListAdapter(Context context, List<StoreObject> list) {
		super(context, Rconfig.getInstance().getId(
				"plugins_store_locator_item_list_store", "layout"), list);
		this.context = context;
		this.listObject = list;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(
					Rconfig.getInstance().getId(
							"plugins_store_locator_item_list_store",
							"layout"), null);
		}
		final StoreObject object = listObject.get(position);
		TextView txt_name = (TextView) convertView.findViewById(Rconfig
				.getInstance().getIdLayout("txt_name"));
		TextView txt_distan = (TextView) convertView.findViewById(Rconfig
				.getInstance().getIdLayout("txt_distan"));
		TextView txt_adress = (TextView) convertView.findViewById(Rconfig
				.getInstance().getIdLayout("txt_adress"));

		RoundedImageView img = (RoundedImageView) convertView
				.findViewById(Rconfig.getInstance().getIdLayout("img"));
		ImageView img_default = (ImageView) convertView
				.findViewById(Rconfig.getInstance().getIdLayout(
						"img_default"));
		LinearLayout phone = (LinearLayout) convertView
				.findViewById(Rconfig.getInstance().getIdLayout("phone"));
		LinearLayout email = (LinearLayout) convertView
				.findViewById(Rconfig.getInstance().getIdLayout("email"));
		LinearLayout map = (LinearLayout) convertView.findViewById(Rconfig
				.getInstance().getIdLayout("map"));
		LinearLayout item = (LinearLayout) convertView.findViewById(Rconfig
				.getInstance().getIdLayout("item_list_store"));

		ImageView image_phone = (ImageView) convertView
				.findViewById(Rconfig.getInstance().getIdLayout(
						"image_phone"));
		ImageView image_email = (ImageView) convertView
				.findViewById(Rconfig.getInstance().getIdLayout(
						"image_email"));
		ImageView image_map = (ImageView) convertView.findViewById(Rconfig
				.getInstance().getIdLayout("image_map"));

		txt_name.setText(object.getName());
		txt_adress.setText(DataLocator.convertAddress(object));
		// DecimalFormat decim = new DecimalFormat("#.##");
		// String dist_travelled = decim.format(convertToDouble(object
		// .getDistance()) / 1000);

		if (object.getDistance() != null) {
			txt_distan.setText(String.format("%.2f", Double.parseDouble(object.getDistance())) + " "
					+ Config.getInstance().getText("km"));
		}
		if (object.getImage() != null && !object.getImage().equals("")
				&& !object.getImage().equals("null")) {
			DrawableManager.fetchDrawableDetailOnThread(object.getImage(),
					img);
			// circle.setBackground(getResources().getDrawable(R.drawable.circle));
			img_default.setVisibility(View.GONE);
			img.setVisibility(View.VISIBLE);
		} else {
			img_default.setImageDrawable(context.getResources().getDrawable(
					Rconfig.getInstance().getIdDraw(
							"plugins_locator_ic_store_android")));
			img_default.setVisibility(View.VISIBLE);
			img.setVisibility(View.GONE);
			// circle.setBackground(getResources().getDrawable(android.R.color.transparent));
		}
		if (object.getPhone() != null && !object.getPhone().equals("null")
				&& !object.getPhone().equals("")) {
			phone.setEnabled(true);
			phone.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!object.getPhone().equals("null")
							&& !object.getPhone().equals("")
							&& object.getPhone() != null) {
						try {
							Intent intent = new Intent(Intent.ACTION_DIAL,
									Uri.parse("tel:" + object.getPhone()));
							context.startActivity(intent);
						} catch (Exception e) {

						}
					}
				}
			});
			image_phone.setImageResource(Rconfig.getInstance().getIdDraw(
					"plugins_locator_phone"));
		} else {
			phone.setEnabled(false);
			image_phone.setImageResource(Rconfig.getInstance().getIdDraw(
					"plugins_locator_phone_disable"));
		}

		if (object.getEmail() != null && !object.getEmail().equals("null")
				&& !object.getEmail().equals("")) {
			email.setEnabled(true);
			email.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						String mail = object.getEmail();
						Intent gmail = new Intent(Intent.ACTION_SEND);
						// gmail.setClassName("com.google.android.gm",
						// "com.google.android.gm.ComposeActivityGmail");
						gmail.putExtra(Intent.EXTRA_EMAIL,
								new String[] { mail });
						gmail.setData(Uri.parse(mail));
						gmail.putExtra(Intent.EXTRA_SUBJECT, "");
						gmail.setType("plain/text");
						gmail.putExtra(Intent.EXTRA_TEXT, "");
						context.startActivity(gmail);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			image_email.setImageResource(Rconfig.getInstance().getIdDraw(
					"plugins_locator_mail"));
		}

		if (object.getLatitude() != null
				&& !object.getLatitude().equals("null")
				&& !object.getLatitude().equals("")
				&& !object.getLongtitude().equals("null")
				&& !object.getLongtitude().equals("")) {
			map.setEnabled(true);
			map.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MapViewFragment fragment = MapViewFragment
							.newInstance(object);
					if (DataLocal.isTablet) {
						SimiManager.getIntance().addPopupFragment(fragment);
					} else {
						SimiManager.getIntance().replaceFragment(fragment);
					}

				}
			});
			image_map.setImageResource(Rconfig.getInstance().getIdDraw(
					"plugins_locator_map"));
		} else {
			map.setEnabled(false);
			image_map.setImageResource(Rconfig.getInstance().getIdDraw(
					"plugins_locator_map_disable"));
		}

//		if (position == selectedListItem) {
//			item.setBackgroundColor(Color.parseColor("#F7F4F2"));
//		} else {
//			// set the normal layout for listItem
//			item.setBackgroundColor(0);
//		}

//		if (position == getCount() - 1 && getCount() >= 9) {
//			if (listObject.size() % 10 == 0) {
//				mfoot.setVisibility(View.VISIBLE);
//				pageIndex++;
//				if (NetworkConnection.haveInternet(context)) {
//					TaskNewLoad taskLoad = new TaskNewLoad();
//					if (currrentLocation == null) {
//
//						taskLoad.data = putData(String.valueOf(0),
//								String.valueOf(0),
//								String.valueOf(pageIndex * 10));
//					} else {
//						taskLoad.data = putData(
//								String.valueOf(currrentLocation
//										.getLatitude()),
//								String.valueOf(currrentLocation
//										.getLongitude()),
//								String.valueOf(pageIndex * 10));
//					}
//
//					taskLoad.execute();
//					// request with model
//					request();
//
//				} else {
//					Toast.makeText(context, "No NetWork Connection",
//							Toast.LENGTH_LONG).show();
//				}
//				if (listObject.size() == 0) {
//					mfoot.setVisibility(View.GONE);
//				}
//			} else {
//				mfoot.setVisibility(View.GONE);
//			}
//		}

		return convertView;
	}

}
