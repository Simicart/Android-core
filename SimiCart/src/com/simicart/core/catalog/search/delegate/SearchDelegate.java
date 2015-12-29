package com.simicart.core.catalog.search.delegate;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.simicart.core.adapter.ProductListAdapter;
import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.search.adapter.GridViewProductListApdapter;

public interface SearchDelegate extends SimiDelegate {

	public void setQty(String qty);


	public void removeFooterView();

	public void addFooterView();

	public boolean getTypeView();

	public void setVisibilityMenuBotton(boolean temp);

	public RelativeLayout getLayoutToGridview();

	public RelativeLayout getLayoutToFilter();

	public RelativeLayout getLayoutToSort();

	public ListView getListView();

	public GridView getGridView();
	public String getTagSearch();
	public void setTagSearch(String tag_search);

	public GridViewProductListApdapter getAdapterGridview();

	public void setGridviewAdapter(Context context, ArrayList<Product> list,
			ArrayList<String> listId,int numcolumn);
	
	public void setListviewAdapter (Context context, ArrayList<Product> list);

	public ProductListAdapter getAdapterProductList();

	public ArrayList<Product> getListProduct();

	public ArrayList<String> getmIDs();
	
	public ArrayList<String> getListProductId();
	public Context getmContext();
	public ImageView getImageChangeview();
	public int getCurrentPosition();
	public void setCurrentPosition(int position);
	public String getQuery();
	public Animation getZoomIn();
	public Animation getZoomOut();
	public void setCheckFilter(boolean filter);
	public void setArrID(ArrayList<String> ids);
	public void setIsLoadMore(boolean loadmore);
	public EditText getEdittextSearch();
	public int getTotalResult();
}
