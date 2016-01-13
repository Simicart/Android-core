package com.simicart.core.home.controller;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.entity.Category;
import com.simicart.core.catalog.category.fragment.CategoryFragment;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.config.Constants;

public class CategoryHomeListener {

	protected ArrayList<Category> listCategory;
	protected String urlSearch;

	public void setListCategory(ArrayList<Category> listCategory) {
		this.listCategory = listCategory;
	}

	public OnItemClickListener createOnTouchCategory() {
		return new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Category cate = listCategory.get(position);
				String idCat = cate.getCategoryId();
				String name = cate.getCategoryName();
				boolean hasChild = cate.hasChild();

				if (hasChild) {
					CategoryFragment fragment = CategoryFragment.newInstance(
							idCat, name);
					SimiManager.getIntance().replaceFragment(fragment);
					SimiManager.getIntance().hideKeyboard();
				} else {
					if (idCat.equals("-1")) {
						urlSearch = Constants.GET_ALL_PRODUCTS;
					} else {
						urlSearch = Constants.GET_CATEGORY_PRODUCTS;
					}
					ListProductFragment fragment = ListProductFragment
							.newInstance(urlSearch, idCat, null, null, name, null, null, null);
//					fragment.setCategoryId(idCat);
//					fragment.setCategoryName(name);
//					if (idCat.equals("-1")) {
//						fragment.setUrlSearch(Constants.GET_ALL_PRODUCTS);
//					} else {
//						fragment.setUrlSearch(Constants.GET_CATEGORY_PRODUCTS);
//					}
					SimiManager.getIntance().replaceFragment(fragment);
					SimiManager.getIntance().hideKeyboard();
				}
			}
		};
	}

}
