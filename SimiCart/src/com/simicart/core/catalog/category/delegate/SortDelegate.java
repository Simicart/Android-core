package com.simicart.core.catalog.category.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.catalog.category.entity.Sort;

public interface SortDelegate extends SimiDelegate {

	public void setSort_option(String mSort);
	public void setListSort(ArrayList<Sort> mListSort);
	public String getSortTag();

}
