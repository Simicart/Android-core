package com.simicart.plugins.locator.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.GPSTracker;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.DataLocator;
import com.simicart.plugins.locator.MathForDummies;
import com.simicart.plugins.locator.NetworkConnection;
import com.simicart.plugins.locator.RoundedImageView;
import com.simicart.plugins.locator.SaveChooseOfUser;
import com.simicart.plugins.locator.ShowMapError;
import com.simicart.plugins.locator.StoreParser;
import com.simicart.plugins.locator.entity.SearchObject;
import com.simicart.plugins.locator.entity.StoreObject;
import com.simicart.plugins.locator.model.ModelLocator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StoreLocatorFragment extends SimiFragment implements
		LocationListener {
	LinearLayout parentContainer;
	LinearLayout parent, processBar;
//	ProgressBar progressBar;
	LatLng start;
	LatLng end;
	View view, store_list;
	MapView mMapView = null;
	MapFragment mMapFragment = null;
	GoogleMap map;
	Document mDoc;
	Bundle bundle;
	ListView list_store;
	ListAdapter adapter;
	public  List<StoreObject> list_store_object =new ArrayList<>();
	List<StoreObject> mStore_maker;
	public static Location currrentLocation = null;
	private static boolean LOCATION_FIXED = false;
	private int page = 0;
	private View footerLayout;
	private LinearLayout layout_search;
	TabHost tab;
	TextView txt_km;
	private String country, city, state, zipcode, tag;
	public static String url_list_store = Config.getInstance().getBaseUrl()
			+ "storelocator/api/get_store_list";
	Handler handler = new Handler();
	private int selectedListItem = -1;
	// Giang
	private static int COUNT_LOCATION = 1;
	// haita
	private boolean check_request;
	public static FragmentManager childFragment;
	public Activity mActivity;
	private SearchObject search_object;
	private boolean check_trigger;

	// end haita
	public static StoreLocatorFragment newInstansce(String country,
			String city, String state, String zipcode, String tag,
			SearchObject search_object) {
		StoreLocatorFragment fragment = new StoreLocatorFragment();
		Bundle bundle = new Bundle();
		setData(Constants.KeyData.COUNTRY, country,
				Constants.KeyData.TYPE_STRING, bundle);
		setData(Constants.KeyData.CITY, city, Constants.KeyData.TYPE_STRING,
				bundle);
		setData(Constants.KeyData.STATE, state, Constants.KeyData.TYPE_STRING,
				bundle);
		setData(Constants.KeyData.ZIPCODE, zipcode,
				Constants.KeyData.TYPE_STRING, bundle);
		setData(Constants.KeyData.TAG, tag, Constants.KeyData.TYPE_STRING,
				bundle);
		bundle.putSerializable(Constants.KeyData.SEARCH_OBJECT, search_object);
		return fragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mActivity = getActivity();
		bundle = savedInstanceState;
		// haita
		this.check_request = true;
		childFragment = getChildFragmentManager();
		this.check_trigger = true;
		// end haita
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// hai ta request to server
		
		// end request

		// getdata
		if (getArguments() != null) {
			country = (String) getData(Constants.KeyData.COUNTRY,
					Constants.KeyData.TYPE_STRING, getArguments());
			city = (String) getData(Constants.KeyData.CITY,
					Constants.KeyData.TYPE_STRING, getArguments());
			state = (String) getData(Constants.KeyData.STATE,
					Constants.KeyData.TYPE_STRING, getArguments());
			zipcode = (String) getData(Constants.KeyData.ZIPCODE,
					Constants.KeyData.TYPE_STRING, getArguments());
			tag = (String) getData(Constants.KeyData.TAG,
					Constants.KeyData.TYPE_STRING, getArguments());
			search_object = (SearchObject) getArguments().getSerializable(
					Constants.KeyData.SEARCH_OBJECT);
		}
		SimiManager.getIntance().setChildFragment(getChildFragmentManager());
		if (DataLocal.isTablet) {
			view = inflater.inflate(
					Rconfig.getInstance().layout("plugins_storelocator_list"),
					container, false);
			this.setViewTablet(view);
		} else {
			view = inflater.inflate(
					Rconfig.getInstance().layout("plugins_storelocator"), null);
			this.setViewMobile();
		}
		request();
//		if (this.check_request) {
//			triggerLocation(mActivity);
//		}
//		request();
		
		return view;
	}
	
	private void request() {
		final ModelLocator mModel = new ModelLocator();
		ModelDelegate delegate = new ModelDelegate() {
			
			@Override
			public void callBack(String message, boolean isSuccess) {
				if(isSuccess) {
					Log.d("duyquang", "123"+message);
					try {
					JSONObject object = mModel.getJSON();
					JSONArray jsonArray = object.getJSONArray("data");
					Gson gson = new GsonBuilder().create();
					list_store_object.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						StoreObject storeObject = gson.fromJson(jsonArray.getJSONObject(i).toString(), StoreObject.class);
						list_store_object.add(storeObject);
						
					}
					
					initData(list_store_object);
					triggerLocation(mActivity);
				
				} catch (JSONException e) {
					e.printStackTrace();
				}
				}else {new ShowMapError(mActivity).showDiagloError(Config
						.getInstance().getText("Result"), Config.getInstance()
						.getText("No store match with your searching"));
				}
			}
		};
		mModel.setDelegate(delegate);
		mModel.addParam("limit", "10");
		mModel.addParam("offset", "0");
		mModel.request();
	}

	public void setViewTablet(View view) {

		mStore_maker = new ArrayList<StoreObject>();

		LayoutInflater inflater = (LayoutInflater) SimiManager.getIntance()
				.getCurrentContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		store_list = view;

		processBar = (LinearLayout) store_list.findViewById(Rconfig
				.getInstance().getIdLayout("progressBar"));
		processBar.setVisibility(View.GONE);
		list_store = (ListView) store_list.findViewById(Rconfig.getInstance()
				.getIdLayout("list_store"));
		layout_search = (LinearLayout) store_list.findViewById(Rconfig
				.getInstance().getIdLayout("layout_search"));
		TextView storelocator_search = (TextView) store_list
				.findViewById(Rconfig.getInstance().getIdLayout(
						"storelocator_search"));
		storelocator_search.setText(Config.getInstance().getText(
				"Search By Area"));
		footerLayout = inflater.inflate(
				Rconfig.getInstance().getId("core_loading_list", "layout"),
				null);

		if (list_store_object != null) {
			if (list_store_object.size() > 10) {
				layout_search.setVisibility(View.GONE);
			} else if (list_store_object.size() <= 10) {
				layout_search.setVisibility(View.VISIBLE);
			}
		}
		layout_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (search_object == null) {
					search_object = new SearchObject();
				}
				SearchStoreFragment fragment = SearchStoreFragment
						.newInstance(search_object);
				SimiManager.getIntance().addPopupFragment(fragment);
			}
		});

		// list_store.setSelector(R.drawable.my_selector);
		list_store.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectedListItem = position;
				StoreDetailFragment storeDetailFragment = StoreDetailFragment
						.newInstance(list_store_object.get(position));
				SimiManager.getIntance().addFragmentSub(storeDetailFragment);
				adapter.notifyDataSetChanged();
			}

		});

		list_store.setOnScrollListener(new OnScrollListener() {
			private int mLastFirstVisibleItem;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (mLastFirstVisibleItem < firstVisibleItem) {
					layout_search.setVisibility(View.GONE);
				}
				if (mLastFirstVisibleItem > firstVisibleItem) {
					layout_search.setVisibility(View.VISIBLE);
				}
				mLastFirstVisibleItem = firstVisibleItem;

			}
		});

		// haita
//		if (!this.check_request) {
//			processBar.setVisibility(View.GONE);
//			this.initData(this.list_store_object);
//		}
		// end haita

	}

	public void setViewMobile() {
		tab = (TabHost) view.findViewById(android.R.id.tabhost);
		tab.setup();
		mStore_maker = new ArrayList<StoreObject>();
		tab.addTab(tab.newTabSpec(Config.getInstance().getText("Store List"))
				.setIndicator(Config.getInstance().getText("Store List"))
				.setContent(new TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						LayoutInflater inflater = (LayoutInflater) SimiManager
								.getIntance()
								.getCurrentContext()
								.getSystemService(
										Context.LAYOUT_INFLATER_SERVICE);
						store_list = inflater.inflate(Rconfig.getInstance()
								.getId("plugins_storelocator_list", "layout"),
								null);
						processBar = (LinearLayout) store_list
								.findViewById(Rconfig.getInstance()
										.getIdLayout("progressBar"));
						processBar.setVisibility(View.GONE);
						list_store = (ListView) store_list.findViewById(Rconfig
								.getInstance().getIdLayout("list_store"));
						layout_search = (LinearLayout) store_list
								.findViewById(Rconfig.getInstance()
										.getIdLayout("layout_search"));
						TextView storelocator_search = (TextView) store_list
								.findViewById(Rconfig.getInstance()
										.getIdLayout("storelocator_search"));
						storelocator_search.setText(Config.getInstance()
								.getText("Search By Area"));
						// processBar = new LinearLayout(getActivity());
						// progressBar = new ProgressBar(getActivity());
						footerLayout = inflater.inflate(Rconfig.getInstance()
								.getId("core_loading_list", "layout"), null);

						if (list_store_object != null) {
							if (list_store_object.size() > 10) {
								layout_search.setVisibility(View.GONE);
							} else if (list_store_object.size() <= 10) {
								layout_search.setVisibility(View.VISIBLE);
							}
						}
						layout_search.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (search_object == null) {
									search_object = new SearchObject();
								}
								SearchStoreFragment fragment = SearchStoreFragment
										.newInstance(search_object);
								SimiManager.getIntance().addPopupFragment(
										fragment);
							}
						});

						list_store
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										selectedListItem = position;
										StoreDetailFragment storeDetailFragment = StoreDetailFragment
												.newInstance(list_store_object
														.get(position));
										SimiManager.getIntance()
												.addFragmentSub(
														storeDetailFragment);
										adapter.notifyDataSetChanged();
									}

								});

						list_store.setOnScrollListener(new OnScrollListener() {
							private int mLastFirstVisibleItem;

							@Override
							public void onScrollStateChanged(AbsListView view,
									int scrollState) {

							}

							@Override
							public void onScroll(AbsListView view,
									int firstVisibleItem, int visibleItemCount,
									int totalItemCount) {
								if (mLastFirstVisibleItem < firstVisibleItem) {
									layout_search.setVisibility(View.GONE);
								}
								if (mLastFirstVisibleItem > firstVisibleItem) {
									layout_search.setVisibility(View.VISIBLE);
								}
								mLastFirstVisibleItem = firstVisibleItem;

							}
						});
						return store_list;
					}
				}));
		tab.addTab(tab.newTabSpec(Config.getInstance().getText("Map"))
				.setIndicator(Config.getInstance().getText("Map"))
				.setContent(new TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						LayoutInflater inflater = (LayoutInflater) SimiManager
								.getIntance()
								.getCurrentContext()
								.getSystemService(
										Context.LAYOUT_INFLATER_SERVICE);
						final View convertView = inflater.inflate(
								Rconfig.getInstance().getId(
										"plugins_storelocator_map_view",
										"layout"), null);
						txt_km = (TextView) convertView.findViewById(Rconfig
								.getInstance().getIdLayout("txt_km"));
						Handler handler = new Handler();
						Runnable run = new Runnable() {

							@Override
							public void run() {
								try {
									MapsInitializer.initialize(getActivity());
								} catch (Exception e) {
								}
								mMapView = (MapView) convertView
										.findViewById(Rconfig.getInstance()
												.getIdLayout("map"));
								mMapView.onCreate(bundle);
								mMapView.onResume();

								map = mMapView.getMap();
								map.getUiSettings().setMyLocationButtonEnabled(
										false);
								map.setMyLocationEnabled(true);

								addMaker();
								getAllPointFromStoreMarker(mStore_maker);
								CameraUpdate zoom = CameraUpdateFactory
										.zoomTo(2);
								if (currrentLocation != null) {
									start = new LatLng(currrentLocation
											.getLatitude(), currrentLocation
											.getLongitude());

									CameraUpdate center = CameraUpdateFactory
											.newLatLng(new LatLng(
													currrentLocation
															.getLatitude(),
													currrentLocation
															.getLongitude()));

									map.moveCamera(center);
									zoom = CameraUpdateFactory.zoomTo(10);

								}
								map.animateCamera(zoom);
								// map.getMyLocation().getLatitude();
								map.setOnMarkerClickListener(new OnMarkerClickListener() {

									@Override
									public boolean onMarkerClick(Marker marker) {
										marker.showInfoWindow();
										return true;
									}
								});
								map.setOnCameraChangeListener(new OnCameraChangeListener() {

									@Override
									public void onCameraChange(
											CameraPosition camera) {
										TaskLoadMaker taskLoad = new TaskLoadMaker();
										taskLoad.data = putData(
												String.valueOf(camera.target.latitude),
												String.valueOf(camera.target.longitude),
												String.valueOf(page * 10));
										taskLoad.execute();
									}
								});
								map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

									@Override
									public void onInfoWindowClick(Marker maker) {
										for (int i = 0; i < mStore_maker.size(); i++) {
											if (MathForDummies.round(Double
													.parseDouble(mStore_maker
															.get(i)
															.getLatitude()), 5) == MathForDummies.round(
													maker.getPosition().latitude,
													5)
													&& MathForDummies.round(
															Double.parseDouble(mStore_maker
																	.get(i)
																	.getLongtitude()),
															5) == MathForDummies.round(
															maker.getPosition().longitude,
															5)) {
												StoreDetailFragment detail = StoreDetailFragment
														.newInstance(mStore_maker
																.get(i));
												SimiManager.getIntance()
														.addPopupFragment(
																detail);
												break;
											}
										}
									}
								});
								map.setInfoWindowAdapter(new InfoWindowAdapter() {

									@Override
									public View getInfoWindow(Marker arg0) {
										return null;
									}

									@Override
									public View getInfoContents(Marker arg0) {
										LayoutInflater inflater = (LayoutInflater) getActivity()
												.getSystemService(
														Context.LAYOUT_INFLATER_SERVICE);
										View v = inflater
												.inflate(
														Rconfig.getInstance()
																.getId("plugins_storelocator_info_window_layout",
																		"layout"),
														null);
										v.setMinimumWidth(200);
										v.setBackgroundColor(getResources()
												.getColor(android.R.color.white));
										TextView tvName = (TextView) v
												.findViewById(Rconfig
														.getInstance()
														.getIdLayout("tv_name"));
										TextView tvAddress = (TextView) v
												.findViewById(Rconfig
														.getInstance()
														.getIdLayout(
																"tv_address"));
										RoundedImageView img = (RoundedImageView) v
												.findViewById(Rconfig
														.getInstance()
														.getIdLayout(
																"img_store"));
										if (MathForDummies.round(
												arg0.getPosition().latitude, 5) == MathForDummies
												.round(currrentLocation
														.getLatitude(), 5)
												&& MathForDummies.round(
														arg0.getPosition().longitude,
														5) == MathForDummies.round(
														currrentLocation
																.getLongitude(),
														5)) {
											tvName.setText(Config.getInstance()
													.getText("You are here"));
											tvAddress.setText("");
										} else if (MathForDummies.round(
												arg0.getPosition().latitude, 5) != MathForDummies
												.round(currrentLocation
														.getLatitude(), 5)
												&& MathForDummies.round(
														arg0.getPosition().longitude,
														5) != MathForDummies.round(
														currrentLocation
																.getLongitude(),
														5)) {
											for (int i = 0; i < mStore_maker
													.size(); i++) {
												if (Long.parseLong(mStore_maker
														.get(i).getLatitude()
														.replaceAll("\\.", "")) != 0
														&& Long.parseLong(mStore_maker
																.get(i)
																.getLongtitude()
																.replaceAll(
																		"\\.",
																		"")) != 0) {
													if (MathForDummies.round(
															Double.parseDouble(mStore_maker
																	.get(i)
																	.getLatitude()),
															5) == MathForDummies.round(
															arg0.getPosition().latitude,
															5)
															&& MathForDummies
																	.round(Double
																			.parseDouble(mStore_maker
																					.get(i)
																					.getLongtitude()),
																			5) == MathForDummies.round(
																	arg0.getPosition().longitude,
																	5)) {
														tvName.setText(mStore_maker
																.get(i)
																.getName());
														tvAddress
																.setText(DataLocator
																		.convertAddress(mStore_maker
																				.get(i)));
														if (mStore_maker
																.get(i)
																.getImage_icon() != null
																&& !mStore_maker
																		.get(i)
																		.getImage_icon()
																		.equals("")) {
															DrawableManager
																	.fetchItemDrawableOnThread(
																			mStore_maker
																					.get(i)
																					.getImage_icon(),
																			img);
														}

													} else {
														img.setImageDrawable(getResources()
																.getDrawable(
																		Rconfig.getInstance()
																				.getIdDraw(
																						"plugins_locator_ic_store_android")));
													}
												}
											}
										}
										return v;
									}
								});
							}
						};
						handler.postDelayed(run, 100);
						return convertView;
					}
				}));
		// haita
//		if (!this.check_request) {
//			processBar.setVisibility(View.GONE);
//			this.initData(this.list_store_object);
//		}
		// end haita
	}

	private void getAllPointFromStoreMarker(List<StoreObject> mStore_maker) {
		if (currrentLocation == null) {
			currrentLocation = new Location("");
		}
		double longtitude = 0;
		double latitude = 0;
		if (mStore_maker.size() > 0) {
			for (int i = 0; i < mStore_maker.size(); i++) {
				StoreObject object = mStore_maker.get(i);
				longtitude += Double.parseDouble(mStore_maker.get(i)
						.getLongtitude());
				latitude += Double.parseDouble(mStore_maker.get(i)
						.getLatitude());
			}
			longtitude = longtitude / mStore_maker.size();
			latitude = latitude / mStore_maker.size();
			currrentLocation.setLongitude(longtitude);
			currrentLocation.setLatitude(latitude);
		} else {
			GPSTracker gpsTracker = new GPSTracker(getActivity());
			Location location = gpsTracker.getLocation();
			if (location != null) {
				currrentLocation.setLongitude(location.getLongitude());
				currrentLocation.setLatitude(location.getLatitude());
			}
		}
	}

	LocationManager locationManager;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1; // 1 minute

	// public Location getLocation() {
	// try {
	// locationManager = (LocationManager) getActivity().getSystemService(
	// Context.LOCATION_SERVICE);
	//
	// // getting GPS status
	// isGPSEnabled = locationManager
	// .isProviderEnabled(LocationManager.GPS_PROVIDER);
	//
	// Log.v("isGPSEnabled", "=" + isGPSEnabled);
	//
	// // getting network status
	// isNetworkEnabled = locationManager
	// .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	//
	// Log.v("isNetworkEnabled", "=" + isNetworkEnabled);
	//
	// if (isGPSEnabled == false && isNetworkEnabled == false) {
	// // no network provider is enabled
	// } else {
	// this.canGetLocation = true;
	// if (isNetworkEnabled) {
	// location = null;
	// locationManager.requestLocationUpdates(
	// LocationManager.NETWORK_PROVIDER,
	// MIN_TIME_BW_UPDATES,
	// MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	// Log.d("Network", "Network");
	// if (locationManager != null) {
	// location = locationManager
	// .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	// if (location != null) {
	// latitude = location.getLatitude();
	// longitude = location.getLongitude();
	// }
	// }
	// }
	// // if GPS Enabled get lat/long using GPS Services
	// if (isGPSEnabled) {
	// location = null;
	// if (location == null) {
	// locationManager.requestLocationUpdates(
	// LocationManager.GPS_PROVIDER,
	// MIN_TIME_BW_UPDATES,
	// MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	// Log.d("GPS Enabled", "GPS Enabled");
	// if (locationManager != null) {
	// location = locationManager
	// .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	// if (location != null) {
	// latitude = location.getLatitude();
	// longitude = location.getLongitude();
	// }
	// }
	// }
	// }
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return location;
	// }

	private boolean checkLocationPermission() {
		String permission = Manifest.permission.ACCESS_FINE_LOCATION;
		int res = SimiManager.getIntance().getCurrentActivity()
				.checkCallingOrSelfPermission(permission);
		return (res == PackageManager.PERMISSION_GRANTED);
	}

	public void triggerLocation(final Context context) {

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!enabled && SaveChooseOfUser.choose != 1) {
			new ShowMapError(mActivity).showGpsError();
			SaveChooseOfUser.choose = 1;
			this.check_trigger = false;
		} else {
			this.check_trigger = true;
		}

		// Define a listener that responds to location updates
				LocationListener locationListener = new LocationListener() {
					public void onLocationChanged(Location location) {
						// Called when a new location is found by the network location
						// provider.
						currrentLocation = location;
						// Giang
						// update distance when GPS off -> on
						if (COUNT_LOCATION == 1 && list_store_object != null) {
							for (int i = 0; i < list_store_object.size(); i++) {
								StoreObject sObject = list_store_object.get(i);
								String dis = distanceKM(currrentLocation.getLatitude(),
										currrentLocation.getLongitude(),
										Double.parseDouble(sObject.getLatitude()),
										Double.parseDouble(sObject.getLongtitude()));
								Log.d("duyquang", ""+dis);
								sObject.setDistance(dis);
							}
							adapter.notifyDataSetChanged();
//							COUNT_LOCATION += 1;
						}

						if (!LOCATION_FIXED) {
							LOCATION_FIXED = true;
						}
					}

					public void onStatusChanged(String provider, int status,
							Bundle extras) {
					}

					public void onProviderEnabled(String provider) {
					}

					public void onProviderDisabled(String provider) {
					}
				};
		// Register the listener with the Location Manager to receive location
		// updates
		if (checkLocationPermission()) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			Location loc = getLastKnownLocation(locationManager);

			if (loc == null) {
				loc = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (loc != null) {
				currrentLocation = loc;
			}
		} else {
			GPSTracker gpsTracker = new GPSTracker(getActivity());
			Location loc = gpsTracker.getLocation();
			if (loc != null) {
				currrentLocation = loc;
			}
		}

//		TaskLoad taskLoad = new TaskLoad();

//		try {
//			taskLoad.data = putData(
//					String.valueOf(currrentLocation.getLatitude()),
//					String.valueOf(currrentLocation.getLongitude()),
//					String.valueOf(page * 10));
//		} catch (Exception e) {
//			taskLoad.data = putData(String.valueOf(0), String.valueOf(0),
//					String.valueOf(page * 10));
//		}
//
//		taskLoad.execute();
		
	}

//	public class TaskLoad extends AsyncTask<Void, Void, JSONObject> {
//		JSONObject data;
//
//		@Override
//		protected JSONObject doInBackground(Void... params) {
//			return getJon(data, url_list_store);
//
//		}
//
//		@Override
//		protected void onPostExecute(JSONObject result) {
//			processBar.setVisibility(View.GONE);
//			// haita
//			check_request = false;
//
//			// end haita
//			// giang
//			StoreParser parser = new StoreParser();
//			if (parser.getResult(result) != null) {
//				if (parser.getResult(result).size() != 0) {
//					initData(parser.getResult(result));
//				}
//			} else {
//				new ShowMapError(mActivity).showDiagloError(Config
//						.getInstance().getText("Result"), Config.getInstance()
//						.getText("No store match with your searching"));
//			}// end Giang
//			try {
//				if (DataLocal.isTablet) {
//					view.findViewById(
//							Rconfig.getInstance()
//									.getIdLayout("progressBar_map"))
//							.setVisibility(View.GONE);
//					SimiFragment fragment = MyMapFragment
//							.newInstance(currrentLocation);
//					SimiManager.getIntance().addFragmentSub(fragment);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			super.onPostExecute(result);
//		}
//
//	}

	private void addMaker() {
		// if (currrentLocation != null) {
		// map.addMarker(new MarkerOptions().position(start).icon(
		// BitmapDescriptorFactory.fromResource(Rconfig.getInstance()
		// .getIdDraw("maker_my"))));
		// }
		for (int i = 0; i < mStore_maker.size(); i++) {
			LatLng end = new LatLng(Double.parseDouble(mStore_maker.get(i)
					.getLatitude()), Double.parseDouble(mStore_maker.get(i)
					.getLongtitude()));
			MarkerOptions options = new MarkerOptions().position(end).icon(
					BitmapDescriptorFactory.fromResource(Rconfig.getInstance()
							.getIdDraw("plugins_locator_maker_default")));
			map.addMarker(options);
		}
	}

	private void initData(List<StoreObject> list) {
		if (list != null) {
//			list_store_object = list;

			for (int i = 0; i < list.size(); i++) {
				mStore_maker.add(list.get(i));
			}
			if (list.size() >= 10) {
				list_store.addFooterView(footerLayout);
			}
			LinearLayout foot = (LinearLayout) footerLayout
					.findViewById(Rconfig.getInstance().id("ll_coreLoading"));
			adapter = new ListAdapter(SimiManager.getIntance()
					.getCurrentContext(), list, foot);
		}
		list_store.setAdapter(adapter);

	}

	private void initDataNew(List<StoreObject> list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				list_store_object.add(list.get(i));
				mStore_maker.add(list.get(i));
			}
			if (mMapView != null) {
				addMaker();
			}
		}
		adapter.notifyDataSetChanged();
	}

	public class ListAdapter extends ArrayAdapter<StoreObject> {
		private final Context context;
		private List<StoreObject> listObject;
		private LinearLayout mfoot;
		private int pageIndex = 0;

		public ListAdapter(Context context, List<StoreObject> list,
				LinearLayout foot) {
			super(context, Rconfig.getInstance().getId(
					"plugins_storelocator_item_list_store", "layout"), list);
			this.context = context;
			this.listObject = list;
			this.mfoot = foot;
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
								"plugins_storelocator_item_list_store",
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
			// String dist_travelled = decim.format(Double.parseDouble(object
			// .getDistance()) / 1000);
			
			if(object.getDistance() != null){
			txt_distan.setText(object.getDistance() + " "
					+ Config.getInstance().getText("km"));
			}
			if (object.getImage() != null && !object.getImage().equals("") && !object.getImage().equals("null")) {
				DrawableManager.fetchDrawableDetailOnThread(
						object.getImage(), img);
				// circle.setBackground(getResources().getDrawable(R.drawable.circle));
				img_default.setVisibility(View.GONE);
				img.setVisibility(View.VISIBLE);
			} else{
				img_default.setImageDrawable(getResources().getDrawable(
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
								&& !object.getPhone().equals("")) {
							String phone_number = "tel:" + object.getPhone();
							Intent callIntent = new Intent(Intent.ACTION_CALL);
							callIntent.setData(Uri.parse(phone_number));
							startActivity(callIntent);
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
							startActivity(gmail);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				image_email.setImageResource(Rconfig.getInstance().getIdDraw(
						"plugins_locator_mail"));
			} 

			if (object.getLatitude() != null && !object.getLatitude().equals("null")
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
						SimiManager.getIntance().addFragmentSub(fragment);

					}
				});
				image_map.setImageResource(Rconfig.getInstance().getIdDraw(
						"plugins_locator_map"));
			} else {
				map.setEnabled(false);
				image_map.setImageResource(Rconfig.getInstance().getIdDraw(
						"plugins_locator_map_disable"));
			}

			if (position == selectedListItem) {
				item.setBackgroundColor(Color.parseColor("#F7F4F2"));
			} else {
				// set the normal layout for listItem
				item.setBackgroundColor(0);
			}

			if (position == getCount() - 1 && getCount() >= 9) {
				if (listObject.size() % 10 == 0) {
					mfoot.setVisibility(View.VISIBLE);
					pageIndex++;
					if (NetworkConnection.haveInternet(context)) {
//						TaskNewLoad taskLoad = new TaskNewLoad();
//						if (currrentLocation == null) {
//
//							taskLoad.data = putData(String.valueOf(0),
//									String.valueOf(0),
//									String.valueOf(pageIndex * 10));
//						} else {
//							taskLoad.data = putData(
//									String.valueOf(currrentLocation
//											.getLatitude()),
//									String.valueOf(currrentLocation
//											.getLongitude()),
//									String.valueOf(pageIndex * 10));
//						}
//
//						taskLoad.execute();
						//request with model
						request();
						
					} else {
						Toast.makeText(context, "No NetWork Connection",
								Toast.LENGTH_LONG).show();
					}
					if (listObject.size() == 0) {
						mfoot.setVisibility(View.GONE);
					}
				} else {
					mfoot.setVisibility(View.GONE);
				}
			}

			return convertView;
		}
		
	}

	public String distanceKM(double lat1, double lon1, double lat2, double lon2) {
		int EARTH_RADIUS_KM = 6371;
		double lat1Rad = Math.toRadians(lat1);
		double lat2Rad = Math.toRadians(lat2);
		double deltaLonRad = Math.toRadians(lon2 - lon1);
		String dist_travelled2;
		double dist_travelled = Math
				.acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad)
						* Math.cos(lat2Rad) * Math.cos(deltaLonRad))
				* EARTH_RADIUS_KM;
		DecimalFormat decim = new DecimalFormat("#.##");
		dist_travelled2 = decim.format(dist_travelled);
		// dist_travelled2 = Double.parseDouble(new
		// DecimalFormat("##.######").format(dist_travelled));
		return dist_travelled2;

	}

//	public class TaskNewLoad extends AsyncTask<Void, Void, JSONObject> {
//		JSONObject data;
//
//		@Override
//		protected JSONObject doInBackground(Void... params) {
//			return getJon(data, url_list_store);
//
//		}
//
//		@Override
//		protected void onPostExecute(JSONObject result) {
//			StoreParser parser = new StoreParser();
//			initDataNew(parser.getResult(result));
//			super.onPostExecute(result);
//		}
//	}

	public class TaskLoadMaker extends AsyncTask<Void, Void, JSONObject> {
		JSONObject data;

		@Override
		protected JSONObject doInBackground(Void... params) {
			return getJon(data, url_list_store);

		}

		@Override
		protected void onPostExecute(JSONObject result) {
			StoreParser parser = new StoreParser();
			if (parser.getResult(result) != null
					&& parser.getResult(result).size() != 0) {
				for (int i = 0; i < parser.getResult(result).size(); i++) {
					if (check(parser.getResult(result).get(i), mStore_maker) == 0) {
						if (Long.parseLong(parser.getResult(result).get(i)
								.getLatitude().replaceAll("\\.", "")) != 0
								&& Long.parseLong(parser.getResult(result)
										.get(i).getLongtitude()
										.replaceAll("\\.", "")) != 0) {
							mStore_maker.add(parser.getResult(result).get(i));
						}
					}
				}
				addMaker();
			}
			super.onPostExecute(result);
		}

	}

	public static int check(StoreObject object, List<StoreObject> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getStoreID().equals(object.getStoreID())) {
				return 1;
			}
		}
		return 0;
	}

	public static JSONObject getJon(JSONObject data, String url) {
//		JSONObject mJSONRet = null;
//		 InputStream is = null;
//		 String json = "";
//		 HttpResponse httpResponse;
//		 String message = "Some errors occurred. Please try again later";
//		 try {
//		 HttpPost httpPost = new HttpPost(url);
//		 httpPost.setHeader("Token", Config.getInstance().getSecretKey());
//		 List<NameValuePair> nameValuePairs = getPostData(data.toString());
//		 httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
//		 "UTF-8"));
//		 Log.e("Param", nameValuePairs.toString());
//		 HttpConnectionParams.setConnectionTimeout(
//		 CoreAPIService.getNewHttpClient().getParams(), 6000);
//		 HttpConnectionParams.setSoTimeout(
//		 CoreAPIService.getNewHttpClient().getParams(), 60000);
//		 CoreAPIService.getNewHttpClient().getParams().setParameter(
//		 ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
//		 httpResponse = CoreAPIService.getNewHttpClient().execute(httpPost);
//		 StatusLine statusLine = httpResponse.getStatusLine();
//		 int statusCode = statusLine.getStatusCode();
//		 HttpEntity httpEntity;
//		 if (statusCode < 400) {
//		 httpEntity = httpResponse.getEntity();
//		 is = httpEntity.getContent();
//		 BufferedReader reader = new BufferedReader(
//		 new InputStreamReader(is, "UTF_8"), 8192);
//		
//		 StringBuilder sb = new StringBuilder();
//		 String line = null;
//		 while ((line = reader.readLine()) != null) {
//		 sb.append(line + "\n");
//		 }
//		 is.close();
//		 json = sb.toString();
//		 Log.e("SB String", sb.toString());
//		 mJSONRet = new JSONObject(json);
//		 return mJSONRet;
//		 } else {
//		 if (statusCode == 500) {
//		 message = "Internal Server Error";
//		 } else if (statusCode == 503) {
//		 message = "Service Unavailable";
//		 } else {
//		 message = "Some errors occurred. Please try again later";
//		 }
//		 httpResponse.getEntity().getContent().close();
//		 throw new IOException(statusLine.getReasonPhrase());
//		 }
//		
//		 } catch (ConnectTimeoutException e) {
//		 message = "The request timed out";
//		 } catch (IOException e) {
//		 message = e.getMessage();
//		 } catch (JSONException e) {
//		 message = "A connection failure occurred";
//		 }
//		return mJSONRet;
		return null;
	}

	public static List<NameValuePair> getPostData(String data) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("data", data));
		return nameValuePairs;
	}

	private JSONObject putData(String lat, String lng, String offset) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("lat", lat);
			jsonObject.put("lng", lng);
			jsonObject.put("offset", offset);
			jsonObject.put("limit", "10");
			if (country != null) {
				jsonObject.put("country", country);
			}
			if (city != null) {
				jsonObject.put("city", city);
			}
			if (state != null) {
				jsonObject.put("state", state);
			}
			if (zipcode != null) {
				jsonObject.put("zipcode", zipcode);
			}
			if (tag != null) {
				jsonObject.put("tag", tag);
			}
		} catch (Exception e) {

		}
		return jsonObject;
	}

	private Location getLastKnownLocation(LocationManager mLocationManager) {
		List<String> providers = mLocationManager.getProviders(true);
		Location bestLocation = null;
		for (String provider : providers) {
			Location l = mLocationManager.getLastKnownLocation(provider);
			if (l == null) {
				continue;
			}
			if (bestLocation == null
					|| l.getAccuracy() < bestLocation.getAccuracy()) {
				// Found best last known location: %s", l);
				bestLocation = l;
			}
		}
		return bestLocation;
	}

	@Override
	public void onResume() {
		super.onResume();
//		if (check_trigger == false) {
//			if (list_store_object != null) {
//				list_store_object.clear();
//				if (adapter != null) {
//					adapter.notifyDataSetChanged();
//				}
//				Handler handler = new Handler();
//				handler.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						triggerLocation(mActivity);
//					}
//				}, 2000);
//			}
//		}
		request();
		Log.d("quangdd", "onresume");
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
	
}
