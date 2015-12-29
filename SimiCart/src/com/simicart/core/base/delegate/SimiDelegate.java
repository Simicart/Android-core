package com.simicart.core.base.delegate;

import com.simicart.core.base.model.collection.SimiCollection;

public interface SimiDelegate {

	public void showLoading();

	public void dismissLoading();

	public void showDialogLoading();

	public void dismissDialogLoading();

	public void updateView(SimiCollection collection);
	
	
	
	
}
