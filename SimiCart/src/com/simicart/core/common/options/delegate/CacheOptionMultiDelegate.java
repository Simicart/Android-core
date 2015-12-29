package com.simicart.core.common.options.delegate;


public interface CacheOptionMultiDelegate extends CacheOptionSingleDelegate{

	
	public void updatePriceMulti(float price,boolean operation);
	public float getPriceMulti();
	public boolean isCheckedAll();
	public boolean isRequired();
}
