package com.simicart.core.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.BasicInforFragment;
import com.simicart.core.catalog.product.fragment.CustomerReviewFragment;
import com.simicart.core.catalog.product.fragment.DescriptionFragment;
import com.simicart.core.catalog.product.fragment.RelatedProductFragment;
import com.simicart.core.catalog.product.fragment.TechSpecsFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.event.base.UtilsEvent;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.block.EventBlock;

public class TabAdapterFragment extends FragmentStatePagerAdapter {
	protected Product mProduct;
	protected ArrayList<SimiFragment> mListFragment;
	protected ArrayList<String> mListTitle;

	public TabAdapterFragment(FragmentManager fm, Product product) {
		super(fm);
		this.mProduct = product;
		mListFragment = new ArrayList<SimiFragment>();
		mListTitle = new ArrayList<String>();
		addFragment();
		addTitle();
		EventTabFragment();
	}

	@Override
	public Fragment getItem(int position) {
		return mListFragment.get(position);
	}

	@Override
	public int getCount() {
		return mListFragment.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mListTitle.get(position);
	}

	private void addFragment() {
		BasicInforFragment fragment_basic = BasicInforFragment.newInstance();
		fragment_basic.setProduct(mProduct);

		mListFragment.add(fragment_basic);

		if (Utils.validateString(mProduct.getDecripition())) {
			DescriptionFragment fragment_description = DescriptionFragment
					.newInstance();
			fragment_description.setDescription(mProduct.getDecripition());
			mListFragment.add(fragment_description);
		}

		if (!mProduct.getAttributes().isEmpty()) {
			TechSpecsFragment fragment_tech = TechSpecsFragment.newInstance();
			fragment_tech.setAttributes(mProduct.getAttributes());
			mListFragment.add(fragment_tech);
		}

		if (mProduct.getRate() > 0 && mProduct.getReviewNumber() > 0) {
			CustomerReviewFragment fragment_review = CustomerReviewFragment
					.newInstance();
			fragment_review.setProductID(mProduct.getId());
			fragment_review.setRatingStar(mProduct.getStar());
			fragment_review.setProduct(mProduct);
			mListFragment.add(fragment_review);
		}

		RelatedProductFragment fragment_related = RelatedProductFragment
				.newInstance();
		fragment_related.setID(mProduct.getId());
		mListFragment.add(fragment_related);

	}

	private void addTitle() {
		mListTitle.add(Config.getInstance().getText("Basic Info"));
		if (Utils.validateString(mProduct.getDecripition())) {
			mListTitle.add(Config.getInstance().getText("Description"));
		}
		if (!mProduct.getAttributes().isEmpty()) {
			mListTitle.add(Config.getInstance().getText("Tech Specs"));
		}
		if (mProduct.getRate() > 0 && mProduct.getReviewNumber() > 0) {
			mListTitle.add(Config.getInstance().getText("Review"));
		}
		mListTitle.add(Config.getInstance().getText("Related Products"));
	}

	public void EventTabFragment() {
		EventBlock event = new EventBlock();
		CacheBlock cacheBlock = new CacheBlock();
		cacheBlock.setListFragment(mListFragment);
		cacheBlock.setListName(mListTitle);
		cacheBlock.setSimiEntity(mProduct);
		event.dispatchEvent("com.simicart.core.adapter.TabAdapterFragment",
				cacheBlock);
	}
}
