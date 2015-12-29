package com.simicart.core.event.options;

import android.content.Context;

import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.common.options.delegate.CacheOptionMultiDelegate;
import com.simicart.core.common.options.delegate.CacheOptionSingleDelegate;
import com.simicart.core.common.options.multi.OptionMulti;
import com.simicart.core.common.options.single.OptionSingle;

public class DataOptionSingle {

	protected Context mContext;
	protected CacheOptionSingleDelegate mSingleDelegate;
	protected CacheOptionMultiDelegate mMultiDelegate;
	protected ProductOption mOptions;
	protected OptionSingle mOptionsSignle;
	protected OptionMulti mOptionsMulti;

	public OptionMulti getOptionsMulti() {
		return mOptionsMulti;
	}

	public void setOptionsMulti(OptionMulti mOptionsMulti) {
		this.mOptionsMulti = mOptionsMulti;
	}

	public OptionSingle getOptionsSignle() {
		return mOptionsSignle;
	}

	public void setOptionsSignle(OptionSingle mOptionsSignle) {
		this.mOptionsSignle = mOptionsSignle;
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public CacheOptionSingleDelegate getSingleDelegate() {
		return mSingleDelegate;
	}

	public void setSingleDelegate(CacheOptionSingleDelegate mDelegate) {
		this.mSingleDelegate = mDelegate;
	}

	public CacheOptionMultiDelegate getMultiDelegate() {
		return mMultiDelegate;
	}

	public void setMultiDelegate(CacheOptionMultiDelegate mDelegate) {
		this.mMultiDelegate = mDelegate;
	}

	public ProductOption getOptions() {
		return mOptions;
	}

	public void setOptions(ProductOption mOptions) {
		this.mOptions = mOptions;
	}

}
