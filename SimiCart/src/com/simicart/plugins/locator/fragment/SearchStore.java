package com.simicart.plugins.locator.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.ConfixSearch;
import com.simicart.plugins.locator.CountryParser;
import com.simicart.plugins.locator.NetworkConnection;
import com.simicart.plugins.locator.TagParser;
import com.simicart.plugins.locator.entity.CountryObject;
import com.simicart.plugins.locator.entity.SearchObject;

public class SearchStore extends SimiFragment {
	private View view;
	private EditText et_city, et_state, et_code;
	private LinearLayout country, progcessBar, btn_search, one, two, three,
			for_, search, clear_all;
	private GridView list_store_tag;
	private TextView txt_countryName, txt_search_Area, txt_search_tag,
			txt_search, txt_country, txt_city, txt_code, txt_state;
	public String url_search = Config.getInstance().getBaseUrl()
			+ "storelocator/api/get_search_config";
	public String url_country = Config.getInstance().getBaseUrl()
			+ "storelocator/api/get_country_list";
	public String url_tag_list = Config.getInstance().getBaseUrl()
			+ "storelocator/api/get_tag_list";
	String _item;
	private static List<CountryObject> list_country;
	private static List<String> list_confix;
	private static List<String> list_tag;
	int page = 0, count = 0, count_tag = 0;
	private String country_code;
	private String tag;
	private TagAdapter adapter;
	SearchObject search_object;

	public void setSearch_object(SearchObject search_object) {
		this.search_object = search_object;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(
				Rconfig.getInstance().getId("plugins_storelocator_search",
						"layout"), null);
		et_city = (EditText) view.findViewById(Rconfig.getInstance()
				.getIdLayout("et_city"));
		et_state = (EditText) view.findViewById(Rconfig.getInstance()
				.getIdLayout("et_state"));
		et_code = (EditText) view.findViewById(Rconfig.getInstance()
				.getIdLayout("et_code"));
		country = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("layout_country"));
		txt_countryName = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_name_country"));
		list_store_tag = (GridView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("list_store_tag"));
		txt_search_Area = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_Search_by_Area"));
		txt_search_tag = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_Search_by_Task"));
		txt_country = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_country"));
		txt_code = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_code"));
		txt_city = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_city"));
		txt_search = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_search"));
		txt_state = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("txt_state"));
		progcessBar = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("progressBar"));
		one = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("one"));
		two = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("two"));
		three = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("three"));
		for_ = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("for_"));
		search = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("search"));
		btn_search = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("btn_search"));
		clear_all = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.getIdLayout("clear_all"));
		if (!search_object.getCity().equals("")) {
			et_city.setText(search_object.getCity());
		}
		if (!search_object.getState().equals("")) {
			et_state.setText(search_object.getState());
		}
		if (!search_object.getZipcode().equals("")) {
			et_code.setText(search_object.getZipcode());
		}
		if (list_tag == null) {
			list_tag = new ArrayList<String>();
			list_tag.add("All");
			SearchLoad searchLoad = new SearchLoad();
			searchLoad.data1 = new JSONObject();
			searchLoad.data2 = new JSONObject();
			searchLoad.data3 = getObjectTag(String.valueOf(page * 10));
			searchLoad.execute();
		} else if (list_tag != null) {
			progcessBar.setVisibility(View.GONE);
			initData(list_tag, list_country, list_confix);
		}

		et_city.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				search_object.setCity(et_city.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		et_state.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				search_object.setState(et_state.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		et_code.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				search_object.setZipcode(et_code.getText().toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		return view;
	}

	private int checkSearch(String value, List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(value)) {
				return 1;
			}
		}
		return 0;
	}

	private void initData(List<String> list_tag,
			final List<CountryObject> list_country, List<String> list_confix) {
		txt_search_Area.setText(Config.getInstance().getText("Search By Area"));
		txt_search_tag.setText(Config.getInstance().getText("Filter by Tag"));
		txt_country.setText(Config.getInstance().getText("Country:"));
		txt_city.setText(Config.getInstance().getText("City") + ":");
		txt_code.setText(Config.getInstance().getText("Zip Code") + ":");
		txt_search.setText(Config.getInstance().getText("Search"));
		txt_state.setText(Config.getInstance().getText("State") + ":");
		TextView txt_clear = (TextView) view.findViewById(Rconfig.getInstance()
				.getIdLayout("clear_search"));
		txt_clear.setText("Clear search");
		if(search_object.getTag() != 0){
			count_tag = search_object.getTag();
		}
		if(search_object.getPosition_country() != 0){
			count = search_object.getPosition_country();
		}
		if (checkSearch("1", list_confix) == 0) {
			one.setVisibility(View.GONE);
		}
		if (checkSearch("2", list_confix) == 0) {
			two.setVisibility(View.GONE);
		}
		if (checkSearch("3", list_confix) == 0) {
			three.setVisibility(View.GONE);
		}
		if (checkSearch("4", list_confix) == 0) {
			for_.setVisibility(View.GONE);
		}
		if (checkSearch("5", list_confix) == 1) {
			search.setVisibility(View.GONE);
		}
		
		adapter = new TagAdapter(getActivity(), list_tag);
		list_store_tag.setAdapter(adapter);
		getGridView(list_store_tag);
		txt_countryName.setText(list_country.get(count).getCountry_name());
		country_code = list_country.get(count).getCountry_code();
		btn_search.setOnTouchListener(new OnTouchListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn_search.setBackgroundDrawable(getResources()
							.getDrawable(android.R.color.holo_orange_light));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					btn_search.setBackgroundDrawable(getResources()
							.getDrawable(android.R.color.holo_orange_dark));
					search_object.setCity(et_city.getText().toString());
					search_object.setState(et_state.getText().toString());
					search_object.setZipcode(et_code.getText().toString());
					StoreLocatorFragment fragment = StoreLocatorFragment
							.newInstans(country_code, et_city.getText()
									.toString(), et_state.getText().toString(),
									et_code.getText().toString(), tag);
					fragment.setSearch_object(search_object);
					SimiManager.getIntance().addFragment(fragment);
					SimiManager.getIntance().removeDialog();
					Utils.hideKeyboard(v);
				}
				return true;
			}
		});
		country.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCreatDialog(list_country).show();
			}
		});
		clear_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search_object.setName_country("");
				search_object.setCity("");
				search_object.setZipcode("");
				search_object.setState("");
				search_object.setPosition_country(0);
				StoreLocatorFragment fragment = StoreLocatorFragment
						.newInstans(null, null, null, null, _item);
				fragment.setSearch_object(search_object);
				SimiManager.getIntance().addFragment(fragment);
				SimiManager.getIntance().removeDialog();
			}
		});
	}

	private void initNewTag(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			list_tag.add(list.get(i));
		}
		adapter.notifyDataSetChanged();
	}

	Dialog dialog = null;

	private Dialog onCreatDialog(List<CountryObject> list_country) {
		dialog = new Dialog(getActivity());
		dialog.setCancelable(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setFeatureDrawableAlpha(1, 1);
		dialog.setContentView(Rconfig.getInstance().getId(
				"plugins_storelocator_country", "layout"));
		ListView listView = (ListView) dialog.findViewById(Rconfig
				.getInstance().getIdLayout("list_country"));
		CountryAdapter adapter = new CountryAdapter(getActivity(), list_country);
		listView.setAdapter(adapter);
		return dialog;
	}

	private JSONObject getObjectTag(String offset) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("offset", offset);
			jsonObject.put("limit", "10");
		} catch (Exception e) {

		}
		return jsonObject;
	}

	public class SearchLoad extends AsyncTask<Void, Void, JSONObject> {
		JSONObject data1, data2, data3, confixObject, countryObject, tagObject;

		@Override
		protected JSONObject doInBackground(Void... params) {
			confixObject = StoreLocatorFragment.getJon(data1, url_search);
			ConfixSearch confixSearch = new ConfixSearch();
			list_confix = confixSearch.getResult(confixObject);
			countryObject = StoreLocatorFragment.getJon(data2, url_country);
			CountryParser countryParser = new CountryParser();
			list_country = countryParser.getResult(countryObject);
			tagObject = StoreLocatorFragment.getJon(data3, url_tag_list);
			TagParser tagParser = new TagParser();
			for (int i = 0; i < tagParser.getResult(tagObject).size(); i++) {
				list_tag.add(tagParser.getResult(tagObject).get(i));
			}
			return tagObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			progcessBar.setVisibility(View.GONE);
			initData(list_tag, list_country, list_confix);
			super.onPostExecute(result);
		}

	}

	private class TagNewLoad extends AsyncTask<Void, Void, JSONObject> {
		private JSONObject data, tagObject;
		private List<String> list_tag = new ArrayList<String>();

		@Override
		protected JSONObject doInBackground(Void... params) {
			tagObject = StoreLocatorFragment.getJon(data, url_tag_list);
			TagParser tagParser = new TagParser();
			for (int i = 0; i < tagParser.getResult(tagObject).size(); i++) {
				list_tag.add(tagParser.getResult(tagObject).get(i));
			}
			return tagObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			initNewTag(list_tag);
			super.onPostExecute(result);
		}
	}

	private class TagAdapter extends ArrayAdapter<String> {
		private Context context;
		private List<String> list;
		private int pageIndex = 0;

		public TagAdapter(Context context, List<String> list) {
			super(context, Rconfig.getInstance().getId("storelocator_item_tag",
					"layout"), list);
			this.context = context;
			this.list = list;
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
				item_tag.setBackgroundDrawable(getResources().getDrawable(
						Rconfig.getInstance().getIdDraw(
								"plugins_locator_drawble_selec")));
				icon_item.setImageDrawable(getResources().getDrawable(
						Rconfig.getInstance().getIdDraw(
								"plugins_locator_ic_store_selec")));
				txt_item.setTextColor(getResources().getColor(
						android.R.color.holo_orange_dark));
			} else {
				item_tag.setBackgroundDrawable(getResources().getDrawable(
						Rconfig.getInstance().getIdDraw(
								"plugins_locator_drawble_search")));
				icon_item.setImageDrawable(getResources().getDrawable(
						Rconfig.getInstance().getIdDraw(
								"plugins_locator_ic_store")));
				txt_item.setTextColor(getResources().getColor(
						android.R.color.black));
			}
			if (position == getCount() - 1 && getCount() >= 9) {
				if ((list.size() - 1) % 10 == 0) {
					pageIndex++;
					if (NetworkConnection.haveInternet(context)) {
						TagNewLoad taskLoad = new TagNewLoad();
						taskLoad.data = getObjectTag(String
								.valueOf(pageIndex * 10));
						taskLoad.execute();
					} else {
						Toast.makeText(context, "No NetWork Connection",
								Toast.LENGTH_LONG).show();
					}
					if (list.size() == 0) {
						// mfoot.setVisibility(View.GONE);
					}
				} else {
					// mfoot.setVisibility(View.GONE);
				}
			}

			item_tag.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						item_tag.setBackgroundDrawable(getResources()
								.getDrawable(
										Rconfig.getInstance().getIdDraw(
												"plugins_locator_drawble_ho")));
						icon_item.setImageDrawable(getResources().getDrawable(
								Rconfig.getInstance().getIdDraw(
										"plugins_locator_ic_store_ho")));
						txt_item.setTextColor(getResources().getColor(
								android.R.color.white));
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
						item_tag.setBackgroundDrawable(getResources()
								.getDrawable(
										Rconfig.getInstance()
												.getIdDraw(
														"plugins_locator_drawble_selec")));
						icon_item.setImageDrawable(getResources().getDrawable(
								Rconfig.getInstance().getIdDraw(
										"plugins_locator_ic_store_selec")));
						txt_item.setTextColor(getResources().getColor(
								android.R.color.holo_orange_dark));
						StoreLocatorFragment fragment;
						tag = item;
						if (position == 0) {
							fragment = StoreLocatorFragment.newInstans(
									country_code, et_city.getText().toString(),
									et_state.getText().toString(), et_code
											.getText().toString(), "");
							_item = "";
						} else {
							fragment = StoreLocatorFragment.newInstans(
									country_code, et_city.getText().toString(),
									et_state.getText().toString(), et_code
											.getText().toString(), item);
							_item = item;
						}
						search_object.setTag(position);
						fragment.setSearch_object(search_object);
						SimiManager.getIntance().addFragment(fragment);
						SimiManager.getIntance().removeDialog();
						count_tag = position;
						notifyDataSetChanged();
					} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
						item_tag.setBackgroundDrawable(getResources()
								.getDrawable(
										Rconfig.getInstance()
												.getIdDraw(
														"plugins_locator_drawble_search")));
						icon_item.setImageDrawable(getResources().getDrawable(
								Rconfig.getInstance().getIdDraw(
										"plugins_locator_ic_store")));
						txt_item.setTextColor(getResources().getColor(
								android.R.color.black));
					}
					return true;
				}
			});
			txt_item.setText(item);
			return convertView;
		}
	}

	private class CountryAdapter extends ArrayAdapter<CountryObject> {
		private Context context;
		private List<CountryObject> list;
		RadioButton radioButton;
		public CountryAdapter(Context context, List<CountryObject> list) {
			super(context, Rconfig.getInstance().getId(
					"plugins_storelocator_item_list", "layout"), list);
			this.context = context;
			this.list = list;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = inflater.inflate(
						Rconfig.getInstance().getId(
								"plugins_storelocator_item_list", "layout"),
						null);
			}
			final CountryObject item = list.get(position);
			radioButton = (RadioButton) convertView
					.findViewById(Rconfig.getInstance()
							.getIdLayout("radio_btn"));
			radioButton.setText(item.getCountry_name());
			if (position == count) {
				radioButton.setChecked(true);
			} else {
				radioButton.setChecked(false);
			}
			radioButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					count = position;
					country_code = item.getCountry_code();
					txt_countryName.setText(item.getCountry_name());
					search_object.setName_country(item.getCountry_name());
					search_object.setPosition_country(position);
					notifyDataSetChanged();
					dialog.dismiss();
				}
			});
			return convertView;
		}

	}

	public void getGridView(GridView myListView) {
		ListAdapter myListAdapter = myListView.getAdapter();
		if (myListAdapter == null) {
			return;
		}
		int totalHeight = 50;
		ViewGroup.LayoutParams params = myListView.getLayoutParams();
		params.height = totalHeight * (myListAdapter.getCount() + 1);
		myListView.setLayoutParams(params);
	}

}
