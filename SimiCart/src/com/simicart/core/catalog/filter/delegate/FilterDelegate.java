package com.simicart.core.catalog.filter.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.catalog.filter.entity.FilterEntity;
import com.simicart.core.catalog.filter.entity.FilterState;

public interface FilterDelegate extends SimiDelegate {

	public void onShowListFilter(ArrayList<FilterEntity> filters);

	public void onShowListSelectedFilter(ArrayList<FilterState> states);

	public void onShowDetailFilter(FilterEntity entity);

}
