package com.simicart.core.catalog.product.fragment;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.entity.BusEntity;
import com.simicart.core.catalog.product.entity.Attributes;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

public class TechSpecsFragment extends SimiFragment {

	protected ArrayList<Attributes> mAttributes;

	public static TechSpecsFragment newInstance() {
		TechSpecsFragment fragment = new TechSpecsFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance()
						.layout("core_information_techspec_layout"), container,
				false);

		LinearLayout ll_techSpecs = (LinearLayout) rootView
				.findViewById(Rconfig.getInstance().id("l_scrollView"));

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		for (Attributes attributeProduct : mAttributes) {
			String title = attributeProduct.getTitle();
			String value = attributeProduct.getValue();
			if (Utils.validateString(title)) {
				TextView tv_title = new TextView(inflater.getContext());
				tv_title.setLayoutParams(lp);
				tv_title.setText(title);
				tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				tv_title.setTypeface(Typeface.DEFAULT_BOLD);
				tv_title.setTextColor(Config.getInstance().getContent_color());
				tv_title.setPadding(0, Utils.getValueDp(10), 0, 0);
				ll_techSpecs.addView(tv_title);
			}

			if (Utils.validateString(value)) {
				TextView tv_value = new TextView(inflater.getContext());
				tv_value.setLayoutParams(lp);
				tv_value.setText(Html.fromHtml(("<font color='"
						+ Config.getInstance().getContent_color_string() + "'>"
						+ value + "</font>")));
				ll_techSpecs.addView(tv_value);
			}
		}

		rootView.setBackgroundColor(Config.getInstance().getApp_backrground());

		return rootView;
	}

	@Override
	public void onEvent(BusEntity event) {
		super.onEvent(event);
		if (event.getKey() == Constants.KeyBus.PRODUCT) {
			Product product = (Product) event.getValue();
			mAttributes = product.getAttributes();
		}
	}
}
