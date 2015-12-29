package com.simicart.core.common.options.delegate;

import com.simicart.core.catalog.product.entity.ProductOption;

public interface OptionProductDelegate {

	public void updatePrice(ProductOption cacheOption, boolean isAdd);
}
