package com.simicart.core.home.delegate;

import java.util.ArrayList;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.core.catalog.product.entity.ProductList;

public interface ProductListDelegate extends SimiDelegate{
	public void onUpdate(ArrayList<ProductList> productList);
}
