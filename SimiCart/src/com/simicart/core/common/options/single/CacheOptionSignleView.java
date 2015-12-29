package com.simicart.core.common.options.single;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.common.options.delegate.CacheOptionSingleDelegate;
import com.simicart.core.common.options.delegate.DependOptionDelegate;
import com.simicart.core.config.Rconfig;

public class CacheOptionSignleView extends CacheOptionView implements
		DependOptionDelegate, CacheOptionSingleDelegate {

	// protected RadioGroup mRadioGroup;
	protected int iconRadio;
	protected ArrayList<DependOptionDelegate> mListDelegate;
	protected HashMap<String, OptionSingle> hsm_ListID;
	protected OptionSingle opSignNone = null;

	public CacheOptionSignleView(CacheOption cacheOption) {
		super(cacheOption);
		iconRadio = Rconfig.getInstance().drawable("radio_bt");
	}

	@Override
	public CacheOption getCacheOption() {
		ArrayList<ProductOption> options = new ArrayList<ProductOption>();
		Set<String> keys = hsm_ListID.keySet();
		Iterator<String> iteratior = keys.iterator();
		while (iteratior.hasNext()) {
			String key = iteratior.next();
			OptionSingle option_single = hsm_ListID.get(key);
			ProductOption option = option_single.getOptions();
			options.add(option);
		}

		return mCacheOption;
	}

	public void setIconRadio(int idDrawable) {
		iconRadio = idDrawable;
	}

	public void setDependDelegate(ArrayList<DependOptionDelegate> delegates) {
		mListDelegate = delegates;
	}

	@Override
	protected void createCacheOption() {
		createOptionView();
	}

	protected void createOptionView() {
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		hsm_ListID = new HashMap<String, OptionSingle>();

		for (final ProductOption option : mCacheOption.getAllOption()) {
			OptionSingle option_signle = new OptionSingle(option, mContext,
					this);

			// // event single
			// DataOptionSingle data = new DataOptionSingle();
			// data.setContext(mContext);
			// data.setSingleDelegate(this);
			// data.setOptions(option);
			// data.setOptionsSignle(option_signle);
			// OptionSingleEvent single_event = new OptionSingleEvent();
			// single_event.dispatchEvent(
			// "com.simicart.common.options.single.OptionSignle", data);
			// option_signle = data.getOptionsSignle();
			// end event

			View view = option_signle.createView();
			ll_body.addView(view, param);
			hsm_ListID.put(option.getOptionId(), option_signle);
		}

		if (!mCacheOption.isRequired()) {
			// add radio button none
			opSignNone = new OptionSingle(null, mContext, this);
			opSignNone.setTypeNone(true);
			// // event none
			// DataOptionSingle data = new DataOptionSingle();
			// data.setContext(mContext);
			// data.setSingleDelegate(this);
			// data.setOptions(null);
			// data.setOptionsSignle(opSignNone);
			// OptionSingleEvent single_event = new OptionSingleEvent();
			// single_event.dispatchEvent(
			// "com.simicart.common.options.single.OptionSignle", data);
			// opSignNone = data.getOptionsSignle();
			// end event
			View view = opSignNone.createView();
			ll_body.addView(view, param);
		}

	}

	@Override
	public void onReceiveDependOption(ArrayList<String> options) {
		if (null != hsm_ListID) {
			Set<String> keys = hsm_ListID.keySet();
			Iterator<String> iteratior = keys.iterator();
			while (iteratior.hasNext()) {
				String key = iteratior.next();
				OptionSingle option_single = hsm_ListID.get(key);
				HashMap<String, String> dependList = option_single.getOptions()
						.getDependence_options();
				if (null != dependList && dependList.size() > 0) {
					if (compareList(dependList, options)) {
						option_single.setBackgroundColor(Color
								.parseColor("#D6C9C9"));
						if (option_single.isChecked) {

						}
					} else {
						option_single.setBackgroundColor(0);
						if (option_single.isCheckedOption()) {
							clearCheckAll();
						}
					}
				}
			}
		}

	}

	protected void clearCheckAll() {
		Set<String> keys = hsm_ListID.keySet();
		Iterator<String> iteratior = keys.iterator();
		while (iteratior.hasNext()) {
			String key = iteratior.next();
			OptionSingle option_single = hsm_ListID.get(key);
			option_single.updateView(false);
		}
	}

	// khi select 1 option dependent
	public void onSendDependOption(ArrayList<String> optionDepends,
			String currentID) {

		if (hsm_ListID.containsKey(currentID)) {
			OptionSingle option_single_current = hsm_ListID.get(currentID);
			option_single_current.setBackgroundColor(Color
					.parseColor("#D6C9C9"));
		}

		for (ProductOption option : mCacheOption.getAllOption()) {
			String id = option.getOptionId();
			if (!id.equals(currentID)) {
				if (hsm_ListID.containsKey(id)) {
					OptionSingle option_single = hsm_ListID.get(id);
					option_single.setBackgroundColor(Color
							.parseColor("#ffffff"));
					option_single.updateView(false);
				}
			}
		}

		if (null != mListDelegate && mListDelegate.size() > 0) {
			for (DependOptionDelegate delegate : mListDelegate) {
				delegate.onReceiveDependOption(optionDepends);
			}
		}

		// neu complete option require

	}

	public boolean compareList(HashMap<String, String> dependList,
			ArrayList<String> options) {
		if (options != null && options.size() > 0) {
			for (String ele1 : options) {
				for (String ele2 : dependList.keySet()) {
					if (ele2.equals(ele1)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	// cache option single delegate
	@Override
	public void updateStateCacheOption(String id, boolean isSeletecd) {
		if (isSeletecd) {
			mCacheOption.setCompleteRequired(true);
		} else {
			boolean aChecked = false;
			for (final ProductOption option : mCacheOption.getAllOption()) {
				if (option != null && option.getOptionId() != null
						&& !option.getOptionId().equals(id)
						&& option.isChecked()) {
					aChecked = true;
				}
			}
			if (aChecked) {
				mCacheOption.setCompleteRequired(true);
			} else {
				mCacheOption.setCompleteRequired(false);
			}
		}
	}

	@Override
	public void updatePriceParent(ProductOption option, boolean operation) {
		updatePriceForParent(option, operation);
	}

	protected void checkCompleteRequired() {
		boolean aChecked = false;
		for (final ProductOption option : mCacheOption.getAllOption()) {
			if (option.isChecked()) {
				aChecked = true;
			}
		}
		if (aChecked) {
			mCacheOption.setCompleteRequired(true);
		} else {
			mCacheOption.setCompleteRequired(false);
		}
	}

	@Override
	public void updatePriceForHeader(String price) {
		updatePriceHeader(price);
	}

	@Override
	public void clearCheckAll(String id) {
		if (id == null) {
			id = "none";
		}
		if (id.equals("none")) {
			Set<String> keys = hsm_ListID.keySet();
			Iterator<String> iteratior = keys.iterator();
			while (iteratior.hasNext()) {
				String key = iteratior.next();
				OptionSingle option_single = hsm_ListID.get(key);
				if (option_single.isCheckedOption()) {
					updatePriceForParent(option_single.getOptions(),
							SUB_OPERATOR);
				}
				option_single.updateView(false);
			}
		} else {
			if (null != opSignNone) {
				opSignNone.updateView(false);
			}
			Set<String> keys = hsm_ListID.keySet();
			Iterator<String> iteratior = keys.iterator();
			while (iteratior.hasNext()) {
				String key = iteratior.next();
				OptionSingle option_single = hsm_ListID.get(key);
				if (!option_single.equals(id)) {
					if (option_single.isCheckedOption()) {
						option_single.selectOption(false);
					}
				}
			}
		}

	}
}
