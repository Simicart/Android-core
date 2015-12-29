package com.simicart.core.common.options;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.common.options.date.CacheOptionDateView;
import com.simicart.core.common.options.datetime.CacheOptionDateTimeView;
import com.simicart.core.common.options.delegate.DependOptionDelegate;
import com.simicart.core.common.options.delegate.OptionProductDelegate;
import com.simicart.core.common.options.group.CacheOptionGroupView;
import com.simicart.core.common.options.multi.CacheOptionMultiView;
import com.simicart.core.common.options.single.CacheOptionSignleView;
import com.simicart.core.common.options.text.CacheOptionTextView;
import com.simicart.core.common.options.time.CacheOptionTimeView;

@SuppressLint("DefaultLocale")
public class ProductOptionParentView {

	protected String type_product;
	protected ArrayList<CacheOption> options;
	protected OptionProductDelegate mDelegate;
	protected ArrayList<CacheOptionView> mOptionView;

	protected boolean isFirst = true;

	public ArrayList<CacheOptionView> getOptionView() {
		return mOptionView;
	}

	public ProductOptionParentView(Product product,
			OptionProductDelegate delegate) {
		type_product = product.getType();
		options = product.getOptions();
		mDelegate = delegate;
		mOptionView = new ArrayList<CacheOptionView>();
	}

	public View initOptionView() {
		LinearLayout ll_option = null;
		ll_option = new LinearLayout(SimiManager.getIntance()
				.getCurrentContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ll_option.setOrientation(LinearLayout.VERTICAL);
		if (type_product.equals("grouped")) {
			for (CacheOption cacheOption2 : options) {
				CacheOptionGroupView cacheView = new CacheOptionGroupView(
						cacheOption2);
				cacheView.setDelegate(mDelegate);

				if (isFirst) {
					cacheView.setShowWhenStart(true);
					isFirst = false;
				}

				mOptionView.add(cacheView);
				ll_option.addView(cacheView.initOptionsView(), params);
			}
		} else if (type_product.equals("configurable")
				|| type_product.equals("bundle")) {

			ArrayList<CacheOptionSignleView> listOptionView = new ArrayList<CacheOptionSignleView>();
			for (CacheOption cacheOption : options) {
				String type = cacheOption.getOptionType().toLowerCase();
				CacheOptionView cacheView = null;
				if (type.equals("multi")) {
					CacheOptionMultiView multiOption = new CacheOptionMultiView(
							cacheOption);
					multiOption.setDelegate(mDelegate);

					if (isFirst) {
						multiOption.setShowWhenStart(true);
						isFirst = false;
					}

					ll_option.addView(multiOption.initOptionsView(), params);
					mOptionView.add(multiOption);
				} else if (type.equals("text")) {
					cacheView = new CacheOptionTextView(cacheOption);
				} else if (type.equals("date")) {
					cacheView = new CacheOptionDateView(cacheOption);
				} else if (type.equals("date_time")) {
					cacheView = new CacheOptionDateTimeView(cacheOption);
				} else if (type.equals("time")) {
					cacheView = new CacheOptionTimeView(cacheOption);
				} else {
					CacheOptionSignleView viewSingle = new CacheOptionSignleView(
							cacheOption);
					viewSingle.setDelegate(mDelegate);

					if (isFirst) {
						viewSingle.setShowWhenStart(true);
						isFirst = false;
					}

					ll_option.addView(viewSingle.initOptionsView(), params);
					listOptionView.add(viewSingle);
					mOptionView.add(viewSingle);
				}

				if (null != cacheView) {
					cacheView.setDelegate(mDelegate);

					if (isFirst) {
						cacheView.setShowWhenStart(true);
						isFirst = false;
					}

					ll_option.addView(cacheView.initOptionsView(), params);
					mOptionView.add(cacheView);
				}

			}

			setDependDelegate(listOptionView);

		} else {
			if (null != options) {
				for (CacheOption cacheOption2 : options) {
					String type = cacheOption2.getOptionType();
					type = type.toLowerCase();
					CacheOptionView cacheView = null;

					if (type.equals("single")) {
						cacheView = new CacheOptionSignleView(cacheOption2);
					} else if (type.equals("multi")) {
						cacheView = new CacheOptionMultiView(cacheOption2);
					} else if (type.equals("text")) {
						cacheView = new CacheOptionTextView(cacheOption2);
					} else if (type.equals("date")) {
						cacheView = new CacheOptionDateView(cacheOption2);
					} else if (type.equals("date_time")) {
						cacheView = new CacheOptionDateTimeView(cacheOption2);
					} else if (type.equals("time")) {
						cacheView = new CacheOptionTimeView(cacheOption2);
					}
					if (null != cacheView) {
						cacheView.setDelegate(mDelegate);

						if (isFirst) {
							cacheView.setShowWhenStart(true);
							isFirst = false;
						}

						ll_option.addView(cacheView.initOptionsView(), params);
						mOptionView.add(cacheView);
					}
				}
			}
		}

		return ll_option;
	}

	protected void setDependDelegate(ArrayList<CacheOptionSignleView> caches) {
		if (null != caches && caches.size() > 0) {
			for (int i = 0; i < caches.size(); i++) {
				CacheOptionSignleView viewSingle = caches.get(i);
				ArrayList<DependOptionDelegate> delegates = new ArrayList<DependOptionDelegate>();
				for (int j = 0; j < caches.size(); j++) {
					if (j != i) {
						delegates.add(caches.get(j));
					}
				}
				if (delegates.size() > 0) {
					viewSingle.setDependDelegate(delegates);
				}
			}
		}
	}

}
