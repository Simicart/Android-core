package com.simicart.core.home.model.page;

import com.simicart.core.config.DataLocal;
import com.simicart.core.splashscreen.model.SaveCurrencyModel;

public class CurrencyModel {
	public void saveCurrency() {
		String id = DataLocal.getCurrencyID();
		SaveCurrencyModel model = new SaveCurrencyModel();	
		model.addParam("currency", id);
		model.request();
	}

}
