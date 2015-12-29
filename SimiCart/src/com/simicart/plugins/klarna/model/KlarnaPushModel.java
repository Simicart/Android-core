package com.simicart.plugins.klarna.model;

import com.simicart.core.base.model.SimiModel;
import com.simicart.plugins.klarna.fragment.KlarnaFragment;

public class KlarnaPushModel extends SimiModel {
	@Override
	protected void setUrlAction() {
		url_action = KlarnaFragment.URL_PUSH_KLARNA;
	}

}
