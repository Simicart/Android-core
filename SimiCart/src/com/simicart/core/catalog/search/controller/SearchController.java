package com.simicart.core.catalog.search.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.delegate.FilterRequestDelegate;
import com.simicart.core.catalog.category.fragment.SortFragment;
import com.simicart.core.catalog.filter.entity.FilterEntity;
import com.simicart.core.catalog.filter.entity.FilterState;
import com.simicart.core.catalog.filter.entity.ValueFilterEntity;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.catalog.search.delegate.SearchDelegate;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.catalog.search.model.ModelSearchBase;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class SearchController extends SimiController implements
		FilterRequestDelegate {
	protected SearchDelegate mDelegate;
	protected int limit = 8;
	protected String resultNumber;
	protected String mQuery;
	protected int mCurrentOffset = 0;
	protected String mID = "-1";
	protected String mName;
	protected String mSortType = "None";
	protected JSONObject jsonFilter;
	protected boolean isOnscroll = true;
	protected String tag_search;
	protected OnScrollListener mScrollListviewListener,
			mScrollGridviewListener;
	protected OnItemClickListener mListviewClick;
	protected OnTouchListener mOnTouchChangeViewData, mOnTouchToFilter,
			mOnTouchToSort, mOnTouchGridview;
	protected boolean is_back_filter;
	// protected String type_search;
	private Map<String, String> list_param = new HashMap<String, String>();

	// zoom gridview
	float distance_up = 0;
	float distance_down = 0;
	float down_Y = 0;
	float up_Y = 0;
	boolean clickDetected = true;
	private static boolean checkZoom = false;
	private int position = -1;
	protected int firstPos = -1;

	public void setList_Param(Map<String, String> list_query) {
		this.list_param = list_query;
	}

	public SearchController(String name, String id) {
		this.mName = name;
		this.mID = id;
	}

	@Override
	public void onStart() {
		createListener();
		requestProduct();
	}

	private void requestProduct() {
		if (mCurrentOffset == 0) {
			mDelegate.showLoading();
		}
		if (DataLocal.isTablet) {
			limit = 16;
		} else {
			limit = 8;
		}
		if (mModel == null) {
			mModel = new ModelSearchBase();
		}
		String param_url = getValueListParam(ConstantsSearch.PARAM_URL);
		if (param_url != null && !param_url.equals("")) {
			((ModelSearchBase) mModel).setUrlSearch(param_url);
		}
		String param_key = getValueListParam(ConstantsSearch.PARAM_KEY);
		if (param_key != null && !param_key.equals("")) {
			mModel.addParam(ConstantsSearch.PARAM_KEY, param_key);
		}
		String param_query = getValueListParam(ConstantsSearch.PARAM_QUERY);
		if (param_query != null && !param_query.equals("")) {
			mModel.addParam(ConstantsSearch.PARAM_QUERY, param_query);
		}
		String param_categoryid = getValueListParam(ConstantsSearch.PARAM_CATEGORY_ID);
		if (param_categoryid != null && !param_categoryid.equals("")
				&& !param_categoryid.equals("-1")) {
			mModel.addParam(ConstantsSearch.PARAM_CATEGORY_ID, param_categoryid);
		}
		String param_offset = getValueListParam(ConstantsSearch.PARAM_OFFSET);
		if (param_offset != null && !param_offset.equals("")) {
			mModel.addParam(ConstantsSearch.PARAM_OFFSET,
					String.valueOf(param_offset));
		} else {
			mModel.addParam(ConstantsSearch.PARAM_OFFSET,
					String.valueOf(mCurrentOffset));
		}
		String param_limit = getValueListParam(ConstantsSearch.PARAM_LIMIT);
		if (param_limit != null && !param_limit.equals("")) {
			mModel.addParam(ConstantsSearch.PARAM_LIMIT,
					String.valueOf(param_limit));
		} else {
			mModel.addParam(ConstantsSearch.PARAM_LIMIT, String.valueOf(limit));
		}
		String param_sort_option = getValueListParam(ConstantsSearch.PARAM_SORT_OPTION);
		if (param_sort_option != null && !param_sort_option.equals("")) {
			mModel.addParam(ConstantsSearch.PARAM_SORT_OPTION,
					param_sort_option);
		} else {
			mModel.addParam(ConstantsSearch.PARAM_SORT_OPTION, mSortType);
		}
		mModel.addParam(ConstantsSearch.PARAM_WIDTH, "300");
		mModel.addParam(ConstantsSearch.PARAM_HEIGHT, "300");
		if (null != jsonFilter) {
			mModel.addParam("filter", jsonFilter);
		} else {
			mModel.addParam("filter", "");
		}
		mModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				mDelegate.removeFooterView();
				if (isSuccess) {
					resultNumber = message;
					mDelegate.setQty(resultNumber);
					mDelegate.updateView(mModel.getCollection());
					isOnscroll = true;
				}
			}
		});
		mModel.request();
	}

	private String getValueListParam(String key) {
		if (list_param.containsKey(key)) {
			return list_param.get(key);
		}
		return "";
	}

	protected void createListener() {
		mListviewClick = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.e("SearchController ", "onItemClick POSITIOn " + position);
				selectemItemList(position);
			}
		};
		mOnTouchChangeViewData = new OnTouchListener() {

			RelativeLayout layout_changeview = mDelegate.getLayoutToGridview();

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// layout_changeview.setBackgroundColor(Color
					// .parseColor("#6e7f80"));
					break;
				case MotionEvent.ACTION_UP:
					// layout_changeview.setBackgroundColor(Config.getInstance()
					// .getColorSort());
					// toGridView(mDelegate.getQuery());
					changeDataView();
					break;
				case MotionEvent.ACTION_CANCEL:
					// layout_changeview.setBackgroundColor(Config.getInstance()
					// .getColorSort());
					break;
				}
				return true;
			}
		};

		mOnTouchToFilter = new OnTouchListener() {

			RelativeLayout layout_to_filter = mDelegate.getLayoutToFilter();

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// layout_to_filter.setBackgroundColor(Color
					// .parseColor("#6e7f80"));
					break;
				case MotionEvent.ACTION_UP:
					// layout_to_filter.setBackgroundColor(Config.getInstance()
					// .getColorSort());
					break;
				case MotionEvent.ACTION_CANCEL:
					// layout_to_filter.setBackgroundColor(Config.getInstance()
					// .getColorSort());
					break;
				}
				return true;
			}
		};

		mOnTouchToSort = new OnTouchListener() {
			RelativeLayout layout_to_sort = mDelegate.getLayoutToSort();

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// layout_to_sort.setBackgroundColo

					break;
				case MotionEvent.ACTION_UP:
					// layout_to_sort.setBackgroundColor(Config.getInstance()
					// .getColorSort());
					toSortLayout(mQuery);
					break;
				case MotionEvent.ACTION_CANCEL:
					// layout_to_sort.setBackgroundColor(Config.getInstance()
					// .getColorSort());
					break;
				}
				return true;
			}
		};

		mScrollGridviewListener = new OnScrollListener() {

			int currentFirstVisibleItem;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int threshold = 1;
				int count = view.getCount();
				if (scrollState == SCROLL_STATE_IDLE) {
					if ((view.getLastVisiblePosition() >= count - threshold)
							&& Integer.parseInt(resultNumber) > count) {
						if (isOnscroll) {
							mCurrentOffset += limit;
							isOnscroll = false;
							mDelegate.addFooterView();
							mDelegate.setTagSearch(TagSearch.TAG_GRIDVIEW);
							mDelegate.addFooterView();
							mDelegate.setIsLoadMore(true);
							requestProduct();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItem) {
				if (currentFirstVisibleItem > firstVisibleItem) {
					mDelegate.setVisibilityMenuBotton(true);
				} else if (currentFirstVisibleItem < firstVisibleItem) {
					mDelegate.setVisibilityMenuBotton(false);
				}
				currentFirstVisibleItem = firstVisibleItem;
				mDelegate.setCurrentPosition(view.getFirstVisiblePosition());
			}
		};
		mScrollListviewListener = new OnScrollListener() {

			int currentFirstVisibleItem;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				try {
					int threshold = 1;
					int count = view.getCount();
					Log.e("Count :", count + "");
					if (scrollState == SCROLL_STATE_IDLE) {
						if ((view.getLastVisiblePosition() >= count - threshold)
								&& Integer.parseInt(resultNumber) > count) {
							Log.e("ResultNumber :", resultNumber);
							Log.e("IsOnscroll:", isOnscroll + "");
							if (isOnscroll) {
								mCurrentOffset += limit;
								isOnscroll = false;
								mDelegate.addFooterView();
								mDelegate.setIsLoadMore(true);
								requestProduct();
							}
						}
					}

				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItem) {
				try {
					if (currentFirstVisibleItem > firstVisibleItem) {
						mDelegate.setVisibilityMenuBotton(true);
					} else if (currentFirstVisibleItem < firstVisibleItem) {
						mDelegate.setVisibilityMenuBotton(false);
					}
					currentFirstVisibleItem = firstVisibleItem;
					mDelegate
							.setCurrentPosition(view.getFirstVisiblePosition());
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		};
		mOnTouchGridview = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();
				if (action != MotionEvent.ACTION_UP
						&& action != MotionEvent.ACTION_DOWN) {
					clickDetected = false;
				}
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				// case MotionEvent.ACTION_SCROLL:
				case MotionEvent.ACTION_DOWN:
					// first finger down only
					float singer_down = event.getY();
					down_Y = singer_down;
					int postion = mDelegate.getGridView().pointToPosition(
							(int) event.getX(), (int) event.getY());
					position = postion;
					break;
				case MotionEvent.ACTION_UP:
					// first finger lifter
					float singer_up = event.getY();
					up_Y = singer_up;
					float value = up_Y - down_Y;
					float distance = Math.abs(value);
					if (value < 0) {
						// scroll down
						if (distance < 50) {
							if (clickDetected == false) {
								selectemItemGrid(position);
							}
						} else {
							if (checkZoom == false) {
								// scrollDown();
								mDelegate.setVisibilityMenuBotton(false);
							}
						}
					} else if (value == 0.0) {
						selectemItemGrid(position);
					} else {
						// scroll up
						if (distance < 50) {
							if (clickDetected == false) {
								selectemItemGrid(position);
							}
						} else {
							if (checkZoom == false) {
								// scrollUp();
								mDelegate.setVisibilityMenuBotton(true);
							}
						}
					}
					checkZoom = false;
					break;
				case MotionEvent.ACTION_POINTER_UP:
					// second finger lifted
					float up = spacing(event);
					distance_up = up;
					if (distance_up < distance_down) {
						// zoom out
						if (DataLocal.isTablet) {
							if (mDelegate.getGridView().getNumColumns() < 6) {
								firstPos = mDelegate.getGridView()
										.getFirstVisiblePosition();
								mDelegate.setGridviewAdapter(
										mDelegate.getmContext(),
										mDelegate.getListProduct(),
										mDelegate.getmIDs(), 6);
								mDelegate.getGridView().setAdapter(
										mDelegate.getAdapterGridview());
								mDelegate.getGridView().setNumColumns(6);
								mDelegate.getGridView().startAnimation(
										mDelegate.getZoomOut());
								mDelegate.getAdapterGridview()
										.notifyDataSetInvalidated();
								mDelegate.getGridView().invalidateViews();
							}
						} else {
							if (mDelegate.getGridView().getNumColumns() < 4) {
								firstPos = mDelegate.getGridView()
										.getFirstVisiblePosition();
								mDelegate.setGridviewAdapter(
										mDelegate.getmContext(),
										mDelegate.getListProduct(),
										mDelegate.getmIDs(), 4);
								mDelegate.getGridView().setAdapter(
										mDelegate.getAdapterGridview());
								mDelegate.getGridView().setNumColumns(4);
								mDelegate.getGridView().startAnimation(
										mDelegate.getZoomOut());
								mDelegate.getAdapterGridview()
										.notifyDataSetInvalidated();
								mDelegate.getGridView().invalidateViews();
							}
						}
					} else {
						if (DataLocal.isTablet) {
							if (mDelegate.getGridView().getNumColumns() > 4) {
								firstPos = mDelegate.getGridView()
										.getFirstVisiblePosition();
								mDelegate.setGridviewAdapter(
										mDelegate.getmContext(),
										mDelegate.getListProduct(),
										mDelegate.getmIDs(), 4);
								mDelegate.getGridView().setAdapter(
										mDelegate.getAdapterGridview());
								// zoom(2);
								mDelegate.getGridView().setNumColumns(4);
								mDelegate.getGridView().startAnimation(
										mDelegate.getZoomIn());
								mDelegate.getAdapterGridview()
										.notifyDataSetInvalidated();
								mDelegate.getGridView().invalidateViews();
							}
						} else {
							if (mDelegate.getGridView().getNumColumns() > 2) {
								firstPos = mDelegate.getGridView()
										.getFirstVisiblePosition();
								mDelegate.setGridviewAdapter(
										mDelegate.getmContext(),
										mDelegate.getListProduct(),
										mDelegate.getmIDs(), 2);
								mDelegate.getGridView().setAdapter(
										mDelegate.getAdapterGridview());
								// zoom(2);
								mDelegate.getGridView().setNumColumns(2);
								mDelegate.getGridView().startAnimation(
										mDelegate.getZoomIn());
								mDelegate.getAdapterGridview()
										.notifyDataSetInvalidated();
								mDelegate.getGridView().invalidateViews();
							}
						}
					}
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					// second finger down
					System.out.println("Second finger down");
					float down = spacing(event);
					System.out.println("DownDistance:" + down);
					distance_down = down;
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				default:
					break;
				}
				return false;
			}
		};

	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void selectemItemList(int position) {
		Log.e("abc", "++" + position);
		if (position >= 1) {
			ArrayList<Product> listProduct = mDelegate.getListProduct();
			String productId = listProduct.get(position - 1).getData(
					"product_id");
			if (productId != null) {
				ProductDetailParentFragment fragment = ProductDetailParentFragment
						.newInstance(productId, mDelegate.getListProductId());
				// fragment.setProductID(productId);
				// fragment.setListIDProduct(mDelegate.getListProductId());
				SimiManager.getIntance().replaceFragment(fragment);
			}
		}
		SimiManager.getIntance().hideKeyboard();
	}

	private void selectemItemGrid(int position) {
		if (position >= 0) {
			ArrayList<Product> listProduct = mDelegate.getListProduct();
			String productId = listProduct.get(position).getData("product_id");
			if (productId != null) {
				ProductDetailParentFragment fragment = ProductDetailParentFragment
						.newInstance(productId, mDelegate.getListProductId());
				// fragment.setProductID(productId);
				// fragment.setListIDProduct(mDelegate.getListProductId());
				SimiManager.getIntance().replaceFragment(fragment);
			}
		}
		SimiManager.getIntance().hideKeyboard();
	}

	private void changeDataView() {
		if (mDelegate.getTagSearch().equals(TagSearch.TAG_LISTVIEW)) {
			mDelegate.getImageChangeview().setBackgroundResource(
					Rconfig.getInstance().drawable("ic_to_listview"));
			mDelegate.getListView().setVisibility(View.GONE);
			mDelegate.getGridView().setVisibility(View.VISIBLE);
			if (mDelegate.getListProduct().size() > 0) {
				Log.e("ProductListZThemeBlock ", "Product Size "
						+ mDelegate.getListProduct().size());
				if (null == mDelegate.getAdapterGridview()) {
					mDelegate.setGridviewAdapter(mDelegate.getmContext(),
							mDelegate.getListProduct(), mDelegate.getmIDs(), 2);
					mDelegate.getGridView().setSelection(
							mDelegate.getCurrentPosition());
					mDelegate.getGridView().setAdapter(
							mDelegate.getAdapterGridview());
				} else {
					mDelegate.getAdapterGridview().setListProduct(
							mDelegate.getListProduct());
					mDelegate.getGridView().setSelection(
							mDelegate.getCurrentPosition());
					mDelegate.getAdapterGridview().notifyDataSetChanged();
				}
			}
			tag_search = TagSearch.TAG_GRIDVIEW;
			mDelegate.setTagSearch(TagSearch.TAG_GRIDVIEW);
		} else {
			mDelegate.getImageChangeview().setBackgroundResource(
					Rconfig.getInstance().drawable("ic_to_gridview"));
			mDelegate.getListView().setVisibility(View.VISIBLE);
			mDelegate.getGridView().setVisibility(View.GONE);
			if (mDelegate.getListProduct().size() > 0) {
				if (null == mDelegate.getAdapterProductList()) {
					mDelegate.setListviewAdapter(mDelegate.getmContext(),
							mDelegate.getListProduct());
					mDelegate.getListView().setSelection(
							mDelegate.getCurrentPosition());
					mDelegate.getListView().setAdapter(
							mDelegate.getAdapterProductList());
				} else {
					mDelegate.getAdapterProductList().setProductList(
							mDelegate.getListProduct());
					mDelegate.getListView().setSelection(
							mDelegate.getCurrentPosition());
					mDelegate.getAdapterProductList().notifyDataSetChanged();
				}
			}
			tag_search = TagSearch.TAG_LISTVIEW;
			mDelegate.setTagSearch(TagSearch.TAG_LISTVIEW);
		}

	}

	private void toSortLayout(String query) {

		// String param_key = "";
		// if (!getValueListParam(ConstantsSearch.PARAM_URL).equals("")) {
		// param_key = getValueListParam(ConstantsSearch.PARAM_URL);
		// }
		// SortFragment fragment =
		// SortFragment.newInstance(getValueListParam(ConstantsSearch.PARAM_URL),
		// mID, mName, mDelegate.getTagSearch(), jsonFilter, param_key, query,
		// mSortType);
		// if (!getValueListParam(ConstantsSearch.PARAM_URL).equals("")) {
		// fragment.setUrl_search(getValueListParam(ConstantsSearch.PARAM_URL));
		// }
		//
		// if (param_key != null && !param_key.equals("")) {
		// fragment.setKey(param_key);
		// }
		// fragment.setSortType(mSortType);
		// fragment.setJSONFilter(jsonFilter);
		// fragment.setSort_tag(mDelegate.getTagSearch());
		// fragment.setQuery(query);
		String param_url = "";
		if (!getValueListParam(ConstantsSearch.PARAM_URL).equals("")) {
			param_url = getValueListParam(ConstantsSearch.PARAM_URL);
		}

		String param_key = getValueListParam(ConstantsSearch.PARAM_KEY);
		if (getValueListParam(ConstantsSearch.PARAM_KEY) != null
				&& !getValueListParam(ConstantsSearch.PARAM_KEY).equals("")) {
			param_key = getValueListParam(ConstantsSearch.PARAM_KEY);
		}

		SortFragment fragment = SortFragment.newInstance(param_url, mID, mName,
				mDelegate.getTagSearch(), jsonFilter, param_key, query,
				mSortType);
		SimiManager.getIntance().replacePopupFragment(fragment);
	}

	@Override
	public void onResume() {
		if (is_back_filter) {
			is_back_filter = false;
			mDelegate.setCheckFilter(true);
			mDelegate.setTagSearch(tag_search);
			mModel.getCollection().getCollection().clear();
			requestProduct();
		} else {
			Log.d("Resume", "visibile MenuBottom");
			mDelegate.setTagSearch(tag_search);
			mDelegate.updateView(mModel.getCollection());
			if (mModel.getCollection().getCollection().size() > 0) {
				mDelegate.setQty(resultNumber.trim());
			}
			mDelegate.setVisibilityMenuBotton(true);
		}
	}

	@Override
	public void requestFilter(FilterEntity filterEntity) {
		if (null != filterEntity) {
			if (null == jsonFilter) {
				jsonFilter = new JSONObject();
			}
			String attribute = filterEntity.getmAttribute();
			ArrayList<ValueFilterEntity> valueEntity = filterEntity
					.getmValueFilters();
			if (null != valueEntity && valueEntity.size() > 0) {
				for (int i = 0; i < valueEntity.size(); i++) {
					ValueFilterEntity entity = valueEntity.get(i);
					if (entity.isSelected()) {
						String value = entity.getmValue();
						if (Utils.validateString(attribute)
								&& Utils.validateString(value)) {
							try {
								jsonFilter.put(attribute, value);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				// request
				is_back_filter = true;
				// requestNewData();

			}
		}
	}

	@Override
	public void clearFilter(FilterState filter) {
		if (null != filter && null != jsonFilter) {
			String attribute = filter.getAttribute();
			if (jsonFilter.has(attribute)) {
				jsonFilter.remove(attribute);
			}
			is_back_filter = true;
		}
	}

	@Override
	public void clearAllFilter() {
		is_back_filter = true;
		jsonFilter = null;
	}

	public boolean isBackFilter() {
		return is_back_filter;
	}

	public void setmID(String mID) {
		this.mID = mID;
	}

	public void setQuery(String query) {
		this.mQuery = query;
	}

	public void setTag_search(String tag_search) {
		this.tag_search = tag_search;
	}

	public void setJsonFilter(JSONObject json) {
		jsonFilter = json;
	}

	public void setmSortType(String mSortType) {
		this.mSortType = mSortType;
	}

	public void setDelegate(SearchDelegate mDelegate) {
		this.mDelegate = mDelegate;
	}

	public OnScrollListener getScrollListviewListener() {
		return mScrollListviewListener;
	}

	public OnTouchListener getmOnTouchChangeViewData() {
		return mOnTouchChangeViewData;
	}

	public OnTouchListener getmOnTouchGridview() {
		return mOnTouchGridview;
	}

	public OnTouchListener getmOnTouchToFilter() {
		return mOnTouchToFilter;
	}

	public OnTouchListener getmOnTouchToSort() {
		return mOnTouchToSort;
	}

	public OnItemClickListener getmListviewClick() {
		return mListviewClick;
	}

	public OnScrollListener getmScrollGridviewListener() {
		return mScrollGridviewListener;
	}

}
