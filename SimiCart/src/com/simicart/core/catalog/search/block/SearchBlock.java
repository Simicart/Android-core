package com.simicart.core.catalog.search.block;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simicart.core.adapter.ProductListAdapter;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.filter.FilterEvent;
import com.simicart.core.catalog.filter.common.FilterConstant;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.search.adapter.GridViewProductListApdapter;
import com.simicart.core.catalog.search.adapter.ListPopupAdapter;
import com.simicart.core.catalog.search.delegate.SearchDelegate;
import com.simicart.core.catalog.search.entity.ItemListPopup;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class SearchBlock extends SimiBlock implements SearchDelegate,
		OnItemClickListener {
	protected RelativeLayout rlt_menu_bottom, ll_list, rlt_change_view,
			rlt_filter, rlt_sort;
	private FilterEvent mFilterEvent;
	private ProductListAdapter mAdapterListView;
	private GridViewProductListApdapter mAdapterGridView;
	private String cate_name = "";
	private String cate_id = "";
	private View ivView;
	private String mQuery = "";
	private String tag_search;
	private String type_search = "";
	ArrayList<String> mIds;
	private GridView grv_products;
	private ListView lv_products;
	private ImageView img_change_view;
	private ArrayList<Product> listProduct = new ArrayList<Product>();
	private int position_curent_product = 0;
	private TextView txt_total_item;
	private EditText edit_search;
	private Animation zoomin;
	private Animation zoomout;
	private ArrayList<String> listProductIds = new ArrayList<String>();

	private RelativeLayout rlt_layout;
	private RelativeLayout relativeLayoutSearch;

	private boolean check_filter;
	private boolean is_loadmore = false;

	protected ProgressBar progressbar;
	private View layout_toast;
	private TextView txt_toast;

	private PopupWindow popupWindow;
	private PopupWindow popUp;
	private String ALL_PRODUCT = "All Product";
	private int totalResult = 0;

	public void setTag_search(String tag_search) {
		this.tag_search = tag_search;
	}

	public void setType_search(String type_search) {
		this.type_search = type_search;
	}

	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}

	public void setCateName(String cate_name) {
		this.cate_name = cate_name;
	}

	public void setmQuery(String mQuery) {
		this.mQuery = mQuery;
	}

	public SearchBlock(View view, Context context) {
		super(view, context);
	}

	public void setFilterEvent(FilterEvent filterEvent) {
		mFilterEvent = filterEvent;
	}

	@Override
	public void initView() {
		rlt_menu_bottom = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("menu_bottom_search"));
		rlt_menu_bottom.setVisibility(View.GONE);
		rlt_filter = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("ll_to_filter"));
		rlt_sort = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("ll_to_sort"));
		rlt_change_view = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("ll_change_view_data"));
		lv_products = (ListView) mView.findViewById(Rconfig.getInstance().id(
				"lv_list_search"));
		lv_products.setBackgroundColor(Config.getInstance()
				.getApp_backrground());
		ColorDrawable sage = new ColorDrawable(Config.getInstance()
				.getLine_color());
		lv_products.setDivider(sage);
		lv_products.setDividerHeight(1);
		LayoutInflater inflaterHeader = SimiManager.getIntance()
				.getCurrentActivity().getLayoutInflater();
		ViewGroup header = (ViewGroup) inflaterHeader.inflate(Rconfig
				.getInstance().layout("core_header_listview_search"),
				lv_products, false);
		lv_products.addHeaderView(header);
		if (!DataLocal.isTablet) {
			img_change_view = (ImageView) mView.findViewById(Rconfig
					.getInstance().id("img_change_view"));
			img_change_view.setBackgroundResource(Rconfig.getInstance()
					.drawable("ic_to_gridview"));
		} else {
			txt_total_item = (TextView) mView.findViewById(Rconfig
					.getInstance().id("txt_totalitem"));
		}
		grv_products = (GridView) mView.findViewById(Rconfig.getInstance().id(
				"grv_product"));
		grv_products.setBackgroundColor(Config.getInstance()
				.getApp_backrground());
		if (tag_search.equals(TagSearch.TAG_LISTVIEW)) {
			lv_products.setVisibility(View.VISIBLE);
			grv_products.setVisibility(View.GONE);
		} else {
			lv_products.setVisibility(View.GONE);
			grv_products.setVisibility(View.VISIBLE);
			grv_products.setPadding(Utils.getValueDp(10), 0,
					Utils.getValueDp(10), 0);
		}
		zoomin = AnimationUtils.loadAnimation(mContext, Rconfig.getInstance()
				.getId("zoomin", "anim"));
		zoomout = AnimationUtils.loadAnimation(mContext, Rconfig.getInstance()
				.getId("zoomout", "anim"));
		progressbar = (ProgressBar) mView.findViewById(Rconfig.getInstance()
				.id("progressBar_load"));
		progressbar.setVisibility(View.GONE);
		// edit_search = (EditText) mView.findViewById(Rconfig.getInstance().id(
		// "et_search"));
		// edit_search.setText("");
		// edit_search.setText(mQuery);
		if (!DataLocal.isTablet) {
			initSearch();
		}
		createToast();
	}

	void createToast() {
		LayoutInflater inflater = SimiManager.getIntance().getCurrentActivity()
				.getLayoutInflater();
		layout_toast = inflater
				.inflate(
						Rconfig.getInstance().layout(
								"core_custom_toast_productlist"),
						(ViewGroup) SimiManager
								.getIntance()
								.getCurrentActivity()
								.findViewById(
										Rconfig.getInstance().id(
												"custom_toast_layout")));
		txt_toast = (TextView) layout_toast.findViewById(Rconfig.getInstance()
				.id("txt_custom_toast"));
	}

	void initSearch() {
		popUp = popupWindowsort();

		LinearLayout ll_search = (LinearLayout) mView.findViewById(Rconfig
				.getInstance().id("ll_search"));
		ll_search.setBackgroundColor(Config.getInstance()
				.getSearch_box_background());

		rlt_layout = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("rlt_layout"));
		relativeLayoutSearch = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rlt_layout"));
		ImageView img_ic_search = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_ic_search"));
		Drawable drawable = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_search"));
		drawable.setColorFilter(Config.getInstance().getSearch_icon_color(),
				PorterDuff.Mode.SRC_ATOP);
		img_ic_search.setImageDrawable(drawable);

		edit_search = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"edittext_search"));
		edit_search.setHint(Config.getInstance().getText("Search product"));
		edit_search.setHintTextColor(Color.parseColor("#32000000"));
		if (!cate_name.equals("") && cate_name != null) {
			edit_search.setHint(cate_name);
			// edit_search.setTypeface(null, Typeface.BOLD);
		}
		if (mQuery != null && !mQuery.equals("")) {
			edit_search.setHint(mQuery);
		}
		edit_search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		edit_search.setTextColor(Config.getInstance().getSearch_text_color());
		edit_search.setHintTextColor(Config.getInstance()
				.getSearch_text_color());
		edit_search.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					rlt_layout.setVisibility(View.GONE);
				} else {
					rlt_layout.setVisibility(View.VISIBLE);
				}
			}
		});
		edit_search.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)
						&& (event.getAction() == KeyEvent.ACTION_DOWN)) {
					showSearchScreen(edit_search.getText().toString(),
							tag_search);
					Utils.hideKeyboard(v);
					return true;
				}
				if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
					// Do your thing.
					Log.e("KeyCode", "back handle edittext");
					if (popUp.isShowing()) {
						hidePopupListView();
						return true;
					}
					return false; // So it is not propagated.
				}
				return false;
			}
		});
		relativeLayoutSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edit_search.requestFocus();
				InputMethodManager imm = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(edit_search, InputMethodManager.SHOW_IMPLICIT);
			}
		});
		edit_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (cate_name != null && !cate_name.equals("")
						&& !cate_name.equals("all categories")) {
					if (s.length() > 0 && edit_search.hasFocus()) {
						if (popUp.isShowing()) {
						} else {
							popUp.showAsDropDown(edit_search, 0, 0);
						}
					} else {
						hidePopupListView();
					}
				}
			}
		});

	}

	private PopupWindow popupWindowsort() {
		// initialize a pop up window type
		popupWindow = new PopupWindow(mContext);
		popupWindow.setFocusable(false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		ArrayList<ItemListPopup> listItem = new ArrayList<ItemListPopup>();
		ItemListPopup item1 = new ItemListPopup();
		item1.setName(cate_name);
		item1.setCheckSearch(false);
		listItem.add(item1);
		ItemListPopup item2 = new ItemListPopup();
		item2.setName(Config.getInstance().getText(ALL_PRODUCT));
		item2.setCheckSearch(true);
		listItem.add(item2);
		ListPopupAdapter adapter = new ListPopupAdapter(mContext, listItem);
		// the drop down list is a list view
		ListView listViewSort = new ListView(mContext);
		// listViewSort.setBackgroundColor(Color.parseColor("#E6ffffff"));
		listViewSort.setAdapter(adapter);
		listViewSort.setOnItemClickListener(this);
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
		popupWindow.setContentView(listViewSort);
		return popupWindow;
	}

	private void hidePopupListView() {
		if (popUp != null) {
			if (popUp.isShowing()) {
				popUp.dismiss();
			}
		}
		SimiManager.getIntance().hideKeyboard();
	}

	public void showSearchScreen(String key, String tag) {
		if (key != null && !key.equals("")) {
			hidePopupListView();
			ListProductFragment fragment = ListProductFragment.newInstance();
			fragment.setQuerySearch(key);
			fragment.setTag_search(tag);
			fragment.setCategoryId(cate_id);
			fragment.setUrlSearch(Constants.SEARCH_PRODUCTS);
			SimiManager.getIntance().addFragment(fragment);
		}
	}

	public void drawListView() {
		lv_products.setVisibility(View.VISIBLE);
		if (listProduct.size() > 0) {
			if (null == mAdapterListView) {
				mAdapterListView = new ProductListAdapter(mContext, listProduct);
				lv_products.setAdapter(mAdapterListView);
			} else {
				mAdapterListView.setProductList(listProduct);
				mAdapterListView.notifyDataSetChanged();
			}
			if (null != mFilterEvent) {
				Button btn_filter = (Button) mFilterEvent.initView(mContext,
						cate_name);
				addFilterButton(btn_filter);
			}
		} else {
			lv_products.setVisibility(View.GONE);
		}
	}

	public void drawDataToGridView() {
		if (listProduct.size() > 0) {
			Log.e("ProductListZThemeBlock ",
					"Product Size " + listProduct.size());
			if (null == mAdapterGridView) {
				if (DataLocal.isTablet) {
					mAdapterGridView = new GridViewProductListApdapter(
							mContext, listProduct, mIds, 4);
					grv_products.setNumColumns(4);
				} else {
					mAdapterGridView = new GridViewProductListApdapter(
							mContext, listProduct, mIds, 2);
				}
				grv_products.setAdapter(mAdapterGridView);
			} else {
				mAdapterGridView.setListProduct(listProduct);
				mAdapterGridView.notifyDataSetChanged();
			}
			if (null != mFilterEvent) {
				Button btn_filter = (Button) mFilterEvent.initView(mContext,
						cate_name);
				addFilterButton(btn_filter);
			}
		} else {
			grv_products.setVisibility(View.GONE);
		}
	}

	@Override
	public void drawView(SimiCollection collection) {
		addListProductId(collection.getJSON());
		ArrayList<SimiEntity> entityProducts = collection.getCollection();
		ArrayList<Product> products = new ArrayList<Product>();
		if (null != entityProducts && entityProducts.size() > 0) {
			for (SimiEntity simiEntity : entityProducts) {
				Product product = (Product) simiEntity;
				products.add(product);
			}
			if (check_filter == true) {
				listProduct.clear();
				listProduct.addAll(products);
				check_filter = false;
			} else {
				listProduct.clear();
				listProduct.addAll(products);
			}
		}

		if (null != mFilterEvent) {
			JSONObject json = collection.getJSON();
			if (null != json && json.has(FilterConstant.LAYEREDNAVIGATION)) {
				mFilterEvent.setJSON(json);
			}
		}
		if (addListProductId(collection.getJSON()) == false) {
			ArrayList<String> listID = new ArrayList<String>();
			for (int i = 0; i < listProduct.size(); i++) {
				listID.add(listProduct.get(i).getId());
			}
			listProductIds.addAll(listID);
		}
		if (listProduct.size() > 0) {
			rlt_menu_bottom.setVisibility(View.VISIBLE);
			if (tag_search.equals(TagSearch.TAG_GRIDVIEW)) {
				lv_products.setVisibility(View.GONE);
				grv_products.setVisibility(View.VISIBLE);
				grv_products.setSelection(position_curent_product);
				drawDataToGridView();
			} else {
				lv_products.setVisibility(View.VISIBLE);
				grv_products.setVisibility(View.GONE);
				lv_products.setSelection(position_curent_product);
				drawListView();
			}
			Utils.getTimeLoadPage("Search");
		}
	}

	public void visiableView() {
		((ViewGroup) mView).removeAllViewsInLayout();
		TextView tv_notify = new TextView(mContext);
		tv_notify.setText(Config.getInstance().getText(
				"Result products is empty"));
		tv_notify.setTypeface(null, Typeface.BOLD);
		tv_notify.setTextColor(Config.getInstance().getContent_color());
		if (DataLocal.isTablet) {
			tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
		} else {
			tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		tv_notify.setGravity(Gravity.CENTER);
		tv_notify.setLayoutParams(params);
		((ViewGroup) mView).addView(tv_notify);
	}

	private boolean addListProductId(JSONObject json) {
		if (json != null && json.has("other")) {
			try {
				JSONArray arrayId = json.getJSONArray("other");
				for (int i = 0; i < arrayId.length(); i++) {
					JSONObject object_other = (JSONObject) arrayId.get(i);
					if (object_other != null
							&& object_other.has("product_id_array")) {
						JSONArray array = object_other
								.getJSONArray("product_id_array");
						if (array.length() > 0) {
							listProductIds.clear();
							for (int j = 0; j < array.length(); j++) {
								String id = array.getString(j);
								listProductIds.add(id);
							}
						}
					}
				}
				return true;
			} catch (Exception e) {
				Log.e("Exception:", e.getMessage());
			}
		}
		return false;
	}

	public void setOnTourchChangeView(OnTouchListener touchListener) {
		if (!DataLocal.isTablet) {
			rlt_change_view.setOnTouchListener(touchListener);
		}
	}

	public void setOnTourchToFilter(OnTouchListener touchListener) {
		rlt_filter.setOnTouchListener(touchListener);
	}

	public void setOnTourchToSort(OnTouchListener touchListener) {
		rlt_sort.setOnTouchListener(touchListener);
	}

	public void setScrollListView(OnScrollListener scroller) {
		lv_products.setOnScrollListener(scroller);
	}

	public void setOnTouchListenerGridview(OnTouchListener listener) {
		grv_products.setOnTouchListener(listener);
	}

	public void setScrollGridView(OnScrollListener listener) {
		grv_products.setOnScrollListener(listener);
	}

	public void setOnItemListviewClick(OnItemClickListener clickListener) {
		lv_products.setOnItemClickListener(clickListener);
	}

	@SuppressLint("NewApi")
	protected void addFilterButton(Button btn_filter) {
		if (null != btn_filter) {
			// add filter button right of sort button
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					Utils.getValueDp(25), Utils.getValueDp(25));
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			btn_filter.setLayoutParams(params);
			btn_filter.setBackgroundResource(Rconfig.getInstance().drawable(
					"ic_filter"));
			rlt_filter.addView(btn_filter);
		}

	}

	@Override
	public void setQty(String qty) {
		if (checkQtyIsInteger(qty)) {

			int i_qty = Integer.parseInt(qty);

			if (DataLocal.isTablet) {
				txt_total_item.setText("");
				if (i_qty > 1) {
					qty = qty + " " + Config.getInstance().getText("items");
				} else {
					qty = qty + " " + Config.getInstance().getText("item");
				}
				txt_total_item.setText(qty);
			} else {
				if (is_loadmore == false && mView.getContext() != null) {
					totalResult = Integer.parseInt(qty);
					Toast toast = new Toast(mView.getContext());
					if (i_qty > 1) {
						qty = qty + " " + Config.getInstance().getText("items");
					} else {
						qty = qty + " " + Config.getInstance().getText("item");
					}
					txt_toast.setText(qty);
					toast.setView(layout_toast);
					toast.setDuration(Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
							0, 200);
					toast.show();
				}
			}
		} else {
			Log.e("ListSizeProduct ", ": Size = 0");
			lv_products.setVisibility(View.GONE);
			grv_products.setVisibility(View.GONE);
			rlt_menu_bottom.setVisibility(View.GONE);
			visiableView();
			return;
		}
	}

	private boolean checkQtyIsInteger(String qty) {
		if (!Utils.validateString(qty)) {
			return false;
		}
		try {
			int result = Integer.parseInt(qty);
			if (result <= 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void removeFooterView() {
		progressbar.setVisibility(View.GONE);
		lv_products.post(new Runnable() {
			@Override
			public void run() {
				while (lv_products.getFooterViewsCount() > 0) {
					lv_products.removeFooterView(ivView);
				}
			}
		});
	}

	@Override
	public void addFooterView() {
		if (tag_search.equals(TagSearch.TAG_GRIDVIEW)) {
			progressbar.setVisibility(View.VISIBLE);
		} else {
			progressbar.setVisibility(View.GONE);
			LayoutInflater inflater = (LayoutInflater) lv_products.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ivView = inflater.inflate(
					Rconfig.getInstance().layout("core_loading_list"), null,
					false);
			lv_products.post(new Runnable() {
				@Override
				public void run() {
					lv_products.addFooterView(ivView);
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
						int lastViewedPosition = lv_products
								.getFirstVisiblePosition();
						View v = lv_products.getChildAt(0);
						int topOffset = (v == null) ? 0 : v.getTop();
						lv_products.setAdapter(mAdapterListView);
						lv_products.setSelectionFromTop(lastViewedPosition,
								topOffset);
					}
				}
			});
		}
	}

	@Override
	public boolean getTypeView() {
		return false;
	}

	@Override
	public void setVisibilityMenuBotton(boolean temp) {
		if (null != rlt_menu_bottom && !DataLocal.isTablet) {
			if (temp) {
				rlt_menu_bottom.setVisibility(View.VISIBLE);
			} else {
				rlt_menu_bottom.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public RelativeLayout getLayoutToGridview() {
		return rlt_change_view;
	}

	@Override
	public RelativeLayout getLayoutToFilter() {
		return rlt_filter;
	}

	@Override
	public RelativeLayout getLayoutToSort() {
		return rlt_sort;
	}

	@Override
	public String getQuery() {
		return mQuery;
	}

	@Override
	public void setArrID(ArrayList<String> ids) {
		this.mIds = ids;
	}

	@Override
	public ListView getListView() {
		return lv_products;
	}

	@Override
	public ArrayList<Product> getListProduct() {
		return listProduct;
	}

	@Override
	public GridView getGridView() {
		return grv_products;
	}

	@Override
	public GridViewProductListApdapter getAdapterGridview() {
		return mAdapterGridView;
	}

	@Override
	public ArrayList<String> getmIDs() {
		return mIds;
	}

	@Override
	public Context getmContext() {
		return mContext;
	}

	@Override
	public ImageView getImageChangeview() {
		return img_change_view;
	}

	@Override
	public ProductListAdapter getAdapterProductList() {
		return mAdapterListView;
	}

	@Override
	public void setGridviewAdapter(Context context, ArrayList<Product> list,
			ArrayList<String> listId, int numcolum) {
		mAdapterGridView = new GridViewProductListApdapter(context, list,
				listId, numcolum);
	}

	@Override
	public void setListviewAdapter(Context context, ArrayList<Product> list) {
		Log.e("Create Product List ", "Second");
		mAdapterListView = new ProductListAdapter(context, list);
	}

	@Override
	public void setCurrentPosition(int position) {
		position_curent_product = position;
	}

	@Override
	public int getCurrentPosition() {
		return position_curent_product;
	}

	@Override
	public String getTagSearch() {
		return tag_search;
	}

	@Override
	public void setTagSearch(String tag_search) {
		setTag_search(tag_search);
	}

	@Override
	public Animation getZoomIn() {
		return zoomin;
	}

	@Override
	public Animation getZoomOut() {
		return zoomout;
	}

	@Override
	public void setCheckFilter(boolean filter) {
		check_filter = true;
	}

	@Override
	public ArrayList<String> getListProductId() {
		return listProductIds;
	}

	@Override
	public void setIsLoadMore(boolean loadmore) {
		this.is_loadmore = loadmore;
	}

	@Override
	public EditText getEdittextSearch() {
		return edit_search;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ItemListPopup item = (ItemListPopup) parent.getItemAtPosition(position);
		if (item.getName().equals(ALL_PRODUCT)) {
			showSearchScreen(edit_search.getText().toString(), tag_search);
		} else {
			ListProductFragment searchFragment = ListProductFragment
					.newInstance();
			searchFragment.setUrlSearch(ConstantsSearch.url_query);
			searchFragment.setCategoryId(cate_id);
			searchFragment.setQuerySearch(edit_search.getText().toString());
			SimiManager.getIntance().replaceFragment(searchFragment);
		}
		hidePopupListView();
		SimiManager.getIntance().hideKeyboard();
	}

	@Override
	public int getTotalResult() {
		return totalResult;
	}
}
