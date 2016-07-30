package com.simicart.plugins.locator.block;

import java.util.ArrayList;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.GPSTracker;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.locator.adapter.StoreListAdapter;
import com.simicart.plugins.locator.common.StoreLocatorConfig;
import com.simicart.plugins.locator.delegate.StoreLocatorStoreListDelegate;
import com.simicart.plugins.locator.entity.StoreObject;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class StoreLocatorStoreListBlock extends SimiBlock implements StoreLocatorStoreListDelegate {

	protected ListView listStore;
	protected LinearLayout llLayoutSearch;
	protected TextView tvStoreLocatorSearch;
	protected ProgressBar pbLoadMore;
	protected StoreListAdapter adapter;
	protected ArrayList<StoreObject> listStoreObject;
	
	public StoreLocatorStoreListBlock(View view, Context context) {
		super(view, context);
		// TODO Auto-generated constructor stub
	}
	
	public void onListStoreScroll(OnScrollListener listener) {
		listStore.setOnScrollListener(listener);
	}
	
	public void onSearchClick(OnClickListener listener) {
		llLayoutSearch.setOnClickListener(listener);
	}
	
	public void onItemListStoreClick(OnItemClickListener listener) {
		listStore.setOnItemClickListener(listener);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		listStore = (ListView) mView.findViewById(Rconfig.getInstance().id("lv_list_store"));
		llLayoutSearch = (LinearLayout) mView
				.findViewById(Rconfig.getInstance()
						.getIdLayout("layout_search"));
		tvStoreLocatorSearch = (TextView) mView
				.findViewById(Rconfig.getInstance()
						.getIdLayout("storelocator_search"));
		tvStoreLocatorSearch.setText(Config.getInstance()
				.getText(Config.getInstance().getText("Search By Area")));
		pbLoadMore = (ProgressBar) mView.findViewById(Rconfig.getInstance().id("progressBar_load"));
		pbLoadMore.setVisibility(View.GONE);
		listStoreObject = new ArrayList<>();
		adapter = new StoreListAdapter(mContext, listStoreObject);
		listStore.setAdapter(adapter);
	}

	@Override
	public void drawView(SimiCollection collection) {
		// TODO Auto-generated method stub
		ArrayList<SimiEntity> entities = collection.getCollection();
		if(entities != null) {
			for(int i=0;i<entities.size();i++) {
				StoreObject store = (StoreObject) entities.get(i);
				listStoreObject.add(store);
			}

			if(listStoreObject.size() > 0) {
				adapter.notifyDataSetChanged();
				StoreLocatorConfig.mapController.addMarker(listStoreObject);
			}
		}
	}

	@Override
	public void visibleSearchLayout(boolean visible) {
		// TODO Auto-generated method stub
		if(visible == true) {
			llLayoutSearch.setVisibility(View.VISIBLE);
		} else {
			llLayoutSearch.setVisibility(View.GONE);
		}
	}

	@Override
	public void showLoadMore(boolean isLoading) {
		// TODO Auto-generated method stub
		if(isLoading == true) {
			pbLoadMore.setVisibility(View.VISIBLE);
		} else {
			pbLoadMore.setVisibility(View.GONE);
		}
	}

	@Override
	public ArrayList<StoreObject> getListStore() {
		// TODO Auto-generated method stub
		return listStoreObject;
	}

	@Override
	public Location getCurrentLocation() {
		// TODO Auto-generated method stub
		GPSTracker gpsTracker = new GPSTracker(mContext);
		Location location = gpsTracker.getLocation();
		return location;
	}

}
