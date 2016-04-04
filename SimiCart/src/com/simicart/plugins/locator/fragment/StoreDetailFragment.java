package com.simicart.plugins.locator.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.NoScrollListView;
import com.simicart.plugins.locator.DataLocator;
import com.simicart.plugins.locator.RoundedImageView;
import com.simicart.plugins.locator.SaveChooseOfUser;
import com.simicart.plugins.locator.ShowMapError;
import com.simicart.plugins.locator.entity.SpecialObject;
import com.simicart.plugins.locator.entity.StoreObject;

public class StoreDetailFragment extends SimiFragment {
	private View view;
	private TextView txt_name, txt_phone, txt_email, txt_web, txt_address,
			txt_des, txt_direc, txt_openning, txt_special, txt_holiday;
	private RoundedImageView img_map;
	private TextView txt_mon, txt_tue, txt_wed, txt_thur, txt_fri, txt_sat,
			txt_sun, read_more;
	private TextView lb_monday, lb_sunday, lb_tuesday, lb_wednesday,
			lb_thursday, lb_friday, lb_saturday;
	private LinearLayout map, special_layout, holiday_layout;
	private NoScrollListView list_special, list_holiday;
	private boolean readMore = true;
	private LinearLayout locator, phone, email, home, decrip;
	private StoreObject storeObject;

	public static StoreDetailFragment newInstance(StoreObject storeObject) {
		StoreDetailFragment detail = new StoreDetailFragment();
		Bundle bundle= new Bundle();
		bundle.putSerializable(Constants.KeyData.STORE_OBJECT, storeObject);
		detail.setArguments(bundle);
		return detail;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(
				Rconfig.getInstance().getId("plugins_storelocator_detail",
						"layout"), null);
		txt_name = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_name_store"));
		txt_address = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_address"));
		txt_email = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_email"));
		txt_phone = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_phone"));
		txt_web = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_web"));
		txt_des = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txtDescrip"));
		txt_mon = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_mon"));
		txt_tue = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_tus"));
		txt_wed = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_wen"));
		txt_thur = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_thur"));
		txt_fri = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_fri"));
		txt_sat = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_sat"));
		txt_sun = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_sun"));
		txt_direc = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("direction"));
		txt_special = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_special"));
		txt_openning = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_open_hour"));
		txt_holiday = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_holiday"));
		read_more = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("read_more"));
		img_map = (RoundedImageView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("image_map"));
		map = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("map"));
		special_layout = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("special_Layout"));
		holiday_layout = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("holiday_layout"));
		list_special = (NoScrollListView) view.findViewById(Rconfig
				.getInstance().getIdLayout("list_special"));
		list_holiday = (NoScrollListView) view.findViewById(Rconfig
				.getInstance().getIdLayout("list_holyday"));
		locator = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("location"));
		phone = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("phone_layout"));
		email = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("email_layout"));
		home = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("home"));
		decrip = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("decrep_layout"));

		lb_monday = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("lb_monday"));
		lb_saturday = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("lb_saturday"));
		lb_sunday = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("lb_sunday"));
		lb_tuesday = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("lb_tuesday"));
		lb_wednesday = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("lb_wednesday"));
		lb_thursday = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("lb_thursday"));
		lb_friday = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("lb_friday"));
		
		//getdata
		if(getArguments() != null){
		storeObject = (StoreObject) getArguments().getSerializable(Constants.KeyData.STORE_OBJECT);
		}
		
		lb_monday.setText(Config.getInstance().getText("Monday") + ":");
		lb_saturday.setText(Config.getInstance().getText("Saturday") + ":");
		lb_sunday.setText(Config.getInstance().getText("Sunday") + ":");
		lb_tuesday.setText(Config.getInstance().getText("Tuesday") + ":");
		lb_wednesday.setText(Config.getInstance().getText("Wednesday") + ":");
		lb_thursday.setText(Config.getInstance().getText("Thursday") + ":");
		lb_friday.setText(Config.getInstance().getText("Friday") + ":");
		
		initData();
		control();
		return view;
	}

	@SuppressLint("NewApi")
	private void initData() {
		txt_direc.setText(Config.getInstance().getText("Get Directions"));
		txt_special.setText(Config.getInstance().getText("Special Days"));
		txt_holiday.setText(Config.getInstance().getText("Holiday"));
		txt_openning.setText(Config.getInstance().getText("Opening Hours"));
		String url = "http://maps.google.com/maps/api/staticmap?center="
				+ storeObject.getLatitude()
				+ ","
				+ storeObject.getLongtitude()
				+ "&markers=icon:"
				+ getResources().getDrawable(
						Rconfig.getInstance().getIdDraw(
								"plugins_locator_maker_default")) + "|"
				+ storeObject.getLatitude() + "," + storeObject.getLongtitude()
				+ "&zoom=100&size=1000x1000&sensor=false";
		if (storeObject.getAddress() == null || storeObject.getAddress().equals("")
				|| storeObject.getAddress().equals("null")
				) {
			locator.setVisibility(View.GONE);
		}
		if (storeObject.getEmail() == null || storeObject.getEmail().equals("")
				|| storeObject.getEmail().equals("null")) {
			email.setVisibility(View.GONE);
		}
		if (storeObject.getPhone() == null || storeObject.getPhone().equals("")
				|| storeObject.getPhone().equals("null")
				) {
			phone.setVisibility(View.GONE);
		}
		if (storeObject.getDescription() == null || storeObject.getDescription().equals("")
				|| storeObject.getDescription().equals("null")
				) {
			decrip.setVisibility(View.GONE);
		}
		if (storeObject == null) {
			home.setVisibility(View.GONE);
		}
		txt_name.setText(storeObject.getName());
		txt_email.setText(storeObject.getEmail());
		txt_address.setText(DataLocator.convertAddress(storeObject));
		txt_phone.setText(storeObject.getPhone());
		txt_web.setText(storeObject.getLink());
		final String fulldess = storeObject.getDescription();
		String shortdess = "";
		if (fulldess.length() > 250) {
			readMore = true;
			read_more.setVisibility(View.VISIBLE);
			read_more
					.setText(">> " + Config.getInstance().getText("Read more"));
			shortdess = storeObject.getDescription().substring(0, 250);
			txt_des.setText(Html.fromHtml(shortdess) + "...");
		} else {
			readMore = false;
			read_more.setVisibility(View.GONE);
			txt_des.setText(Html.fromHtml(fulldess));
		}
		read_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (readMore == true) {
					readMore = false;
					txt_des.setText(Html.fromHtml(fulldess));
					read_more.setText("<< "
							+ Config.getInstance().getText("Show less"));
				} else if (readMore == false) {
					readMore = true;
					txt_des.setText(Html.fromHtml(storeObject.getDescription()
							.substring(0, 250)) + "...");
					read_more.setText(">> "
							+ Config.getInstance().getText("Read more"));
				}
			}
		});

		DrawableManager.fetchDrawableOnThread(url, img_map);
		if (storeObject.getMonday_status().equals("1")) {
			if (!storeObject.getMonday_open().equals("")
					&& !storeObject.getMonday_open().equals("00:00")
					&& !storeObject.getMonday_close().equals("")
					&& !storeObject.getMonday_close().equals("00:00")) {
				txt_mon.setText(storeObject.getMonday_open() + " - "
						+ storeObject.getMonday_close());
			} else {
				txt_mon.setText(Config.getInstance().getText("Open"));
			}
		} else if (storeObject.getMonday_status().equals("2")) {
			txt_mon.setText(Config.getInstance().getText("Close"));
		}

		if (storeObject.getTuesday_status().equals("1")) {
			if (!storeObject.getTuesday_open().equals("")
					&& !storeObject.getTuesday_open().equals("00:00")
					&& !storeObject.getTuesday_close().equals("")
					&& !storeObject.getTuesday_close().equals("00:00")) {
				txt_tue.setText(storeObject.getTuesday_open() + " - "
						+ storeObject.getTuesday_close());
			} else {
				txt_tue.setText(Config.getInstance().getText("Open"));
			}
		} else if (storeObject.getTuesday_status().equals("2")) {
			txt_tue.setText(Config.getInstance().getText("Close"));
		}

		if (storeObject.getWednesday_status().equals("1")) {
			if (!storeObject.getWednesday_open().equals("")
					&& !storeObject.getWednesday_open().equals("00:00")
					&& !storeObject.getWednesday_close().equals("")
					&& !storeObject.getWednesday_close().equals("00:00")) {
				txt_wed.setText(storeObject.getWednesday_open() + " - "
						+ storeObject.getWednesday_close());
			} else {
				txt_wed.setText(Config.getInstance().getText("Open"));
			}
		} else if (storeObject.getWednesday_status().equals("2")) {
			txt_wed.setText(Config.getInstance().getText("Close"));
		}

		if (storeObject.getThursday_status().equals("1")) {
			if (!storeObject.getThursday_open().equals("")
					&& !storeObject.getThursday_open().equals("00:00")
					&& !storeObject.getThursday_close().equals("")
					&& !storeObject.getThursday_close().equals("00:00")) {
				txt_thur.setText(storeObject.getThursday_open() + " - "
						+ storeObject.getThursday_close());
			} else {
				txt_thur.setText(Config.getInstance().getText("Open"));
			}
		} else if (storeObject.getThursday_status().equals("2")) {
			txt_thur.setText(Config.getInstance().getText("Close"));
		}

		if (storeObject.getFriday_status().equals("1")) {
			if (!storeObject.getFriday_open().equals("")
					&& !storeObject.getFriday_open().equals("00:00")
					&& !storeObject.getFriday_close().equals("")
					&& !storeObject.getFriday_close().equals("00:00")) {
				txt_fri.setText(storeObject.getFriday_open() + " - "
						+ storeObject.getFriday_close());
			} else {
				txt_fri.setText(Config.getInstance().getText("Open"));
			}
		} else if (storeObject.getFriday_status().equals("2")) {
			txt_fri.setText(Config.getInstance().getText("Close"));
		}

		if (storeObject.getSaturday_status().equals("1")) {
			if (!storeObject.getSaturday_open().equals("")
					&& !storeObject.getSaturday_open().equals("00:00")
					&& !storeObject.getSaturday_close().equals("")
					&& !storeObject.getSaturday_close().equals("00:00")) {
				txt_sat.setText(storeObject.getSaturday_open() + " - "
						+ storeObject.getSaturday_close());
			} else {
				txt_sat.setText(Config.getInstance().getText("Open"));
			}
		} else if (storeObject.getSaturday_status().equals("2")) {
			txt_sat.setText(Config.getInstance().getText("Close"));
		}

		if (storeObject.getSunday_status().equals("1")) {
			if (!storeObject.getSunday_open().equals("")
					&& !storeObject.getSunday_open().equals("00:00")
					&& !storeObject.getSunday_close().equals("")
					&& !storeObject.getSunday_close().equals("00:00")) {
				txt_sun.setText(storeObject.getSunday_open() + " - "
						+ storeObject.getSunday_close());
			} else {
				txt_sun.setText(Config.getInstance().getText("Close"));
			}
		} else if (storeObject.getSunday_status().equals("2")) {
			txt_sun.setText(Config.getInstance().getText("Close"));
		}
		// List<SpecialObject> list = new ArrayList<SpecialObject>();
		// for(int i = 0;i < 5; i ++){
		// SpecialObject object = new SpecialObject();
		// list.add(object);
		// }
		if (storeObject.getList_special() != null
				&& storeObject.getList_special().size() > 0) {
			SpecialAdapter adapter = new SpecialAdapter(getActivity(),
					storeObject.getList_special());
			list_special.setAdapter(adapter);
		} else {
			special_layout.setVisibility(View.GONE);
		}
		if (storeObject.getList_holiday() != null
				&& storeObject.getList_holiday().size() > 0) {
			HolidayAdapter hoAdapter = new HolidayAdapter(getActivity(),
					storeObject.getList_holiday());
			list_holiday.setAdapter(hoAdapter);
		} else {
			holiday_layout.setVisibility(View.GONE);
		}
		// img_map.setImageBitmap(DrawableManager.getGoogleMapThumbnail(Double.parseDouble(storeObject.getLatitude()),
		// Double.parseDouble(storeObject.getLongtitude())));
	}

	private void control() {
		map.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					map.setBackgroundDrawable(getResources().getDrawable(
							Rconfig.getInstance().getIdDraw(
									"plugins_locator_show_map_hover")));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					map.setBackgroundDrawable(getResources().getDrawable(
							Rconfig.getInstance().getIdDraw(
									"plugins_locator_show_map")));
					// Giang 28/10
					LocationManager service = (LocationManager) SimiManager
							.getIntance().getCurrentActivity()
							.getSystemService(Context.LOCATION_SERVICE);
					boolean enabled = service
							.isProviderEnabled(LocationManager.GPS_PROVIDER);
					if (!enabled) {
						if (SaveChooseOfUser.choose != 1) {
							new ShowMapError(getActivity()).showGpsError();
							SaveChooseOfUser.choose = 1;
						}
					}

					if (StoreLocatorFragment.currrentLocation == null) {
						Criteria criteria = new Criteria();
						String provider = service.getBestProvider(criteria,
								false);
						Location location = service
								.getLastKnownLocation(provider);
						if (location == null) {

							Toast.makeText(
									getActivity(),
									Config.getInstance().getText(
											"Location not available"),
									Toast.LENGTH_LONG).show();

							return true;
						} else {
							storeObject.setLatitude(location.getLatitude() + "");
							storeObject.setLongtitude(location.getLongitude()
									+ "");
						}
					} else {
						LatLng myLocator = new LatLng(Double
								.parseDouble(storeObject.getLatitude()), Double
								.parseDouble(storeObject.getLongtitude()));
						String uri_ = String
								.format(Locale.ENGLISH,
										"http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)",
										StoreLocatorFragment.currrentLocation
												.getLatitude(),
										StoreLocatorFragment.currrentLocation
												.getLongitude(),
										"Your Location", myLocator.latitude,
										myLocator.longitude, storeObject
												.getName());
						Intent intent_ = new Intent(Intent.ACTION_VIEW, Uri
								.parse(uri_));
						intent_.setClassName("com.google.android.apps.maps",
								"com.google.android.maps.MapsActivity");
						startActivity(intent_);
					}
				}
				return true;
			}
		});
		phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone_number = "tel:" + storeObject.getPhone();
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse(phone_number));
				startActivity(callIntent);
			}
		});
		email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String mail = storeObject.getEmail();
					Intent gmail = new Intent(Intent.ACTION_SEND);
					// gmail.setClassName("com.google.android.gm",
					// "com.google.android.gm.ConversationListActivity");
					gmail.putExtra(Intent.EXTRA_EMAIL, new String[] { mail });
					gmail.setData(Uri.parse(mail));
					gmail.putExtra(Intent.EXTRA_SUBJECT, "");
					gmail.setType("plain/text");
					gmail.putExtra(Intent.EXTRA_TEXT, "");
					startActivity(gmail);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		img_map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MapViewFragment fragment = MapViewFragment
						.newInstance(storeObject);
				SimiManager.getIntance().addFragmentSub(fragment);
			}
		});
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(storeObject.getLink()));
					startActivity(intent);
				} catch (Exception e) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("http:///" + storeObject.getLink()));
					startActivity(intent);
				}
			}
		});
	}

	private class SpecialAdapter extends ArrayAdapter<SpecialObject> {

		private Context context;
		private List<SpecialObject> list;

		public SpecialAdapter(Context context, List<SpecialObject> list) {
			super(context, Rconfig.getInstance().getId(
					"plugins_storelocator_item_list_special", "layout"), list);
			this.context = context;
			this.list = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = inflater.inflate(
						Rconfig.getInstance().getId(
								"plugins_storelocator_item_list_special",
								"layout"), null);
			}
			final SpecialObject object = list.get(position);
			Calendar cal = Calendar.getInstance();
			String[] date = object.getDate().split("-");
			cal.set(Integer.parseInt(date[0]), (Integer.parseInt(date[1]) - 1),
					Integer.parseInt(date[2]));
			SimpleDateFormat mFormat = new SimpleDateFormat("EEE MMM dd");
			TextView txt = (TextView) convertView.findViewById(Rconfig
					.getInstance().getIdLayout("txt_special"));
			txt.setText(mFormat.format(cal.getTime()) + "  "
					+ object.getTime_open() + " - " + object.getTime_close());
			txt.setTextSize(15);
			return convertView;
		}
	}

	private class HolidayAdapter extends ArrayAdapter<SpecialObject> {

		private Context context;
		private List<SpecialObject> list;

		public HolidayAdapter(Context context, List<SpecialObject> list) {
			super(context, Rconfig.getInstance().getId(
					"plugins_storelocator_item_list_special", "layout"), list);
			this.context = context;
			this.list = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = inflater.inflate(
						Rconfig.getInstance().getId(
								"plugins_storelocator_item_list_special",
								"layout"), null);
			}
			final SpecialObject object = list.get(position);
			Calendar cal = Calendar.getInstance();
			String[] date = object.getDate().split("-");
			cal.set(Integer.parseInt(date[0]), (Integer.parseInt(date[1]) - 1),
					Integer.parseInt(date[2]));
			SimpleDateFormat mFormat = new SimpleDateFormat("EEE MMM dd");
			TextView txt = (TextView) convertView.findViewById(Rconfig
					.getInstance().getIdLayout("txt_special"));
			txt.setText(mFormat.format(cal.getTime()) + "  " + Config.getInstance().getText("Close"));
			txt.setTextSize(15);
			return convertView;
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
}
