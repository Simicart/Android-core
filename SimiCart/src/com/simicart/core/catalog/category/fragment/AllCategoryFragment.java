package com.simicart.core.catalog.category.fragment;

public class AllCategoryFragment extends CategoryFragment {

	public AllCategoryFragment(String name, String id) {
		super(name, id);
	}

	public static AllCategoryFragment newInstance(String name, String id) {
		AllCategoryFragment fragment = new AllCategoryFragment(name, id);
		return fragment;
	}

}
