package com.simicart.core.catalog.product.fragment;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.product.entity.Attributes;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

public class TechSpecsFragment extends SimiFragment {

	protected ArrayList<Attributes> mAttributes;

	public static TechSpecsFragment newInstance(ArrayList<Attributes> attributes) {
		TechSpecsFragment fragment = new TechSpecsFragment();
		
		Bundle args = new Bundle();
//		setData(Constants.KeyData.LIST_ATTRIBUTES, attributes, Constants.KeyData.TYPE_LIST_MODEL, args);
		args.putParcelableArrayList(Constants.KeyData.LIST_ATTRIBUTES, attributes);
	    fragment.setArguments(args);
		return fragment;
	}

//	public void setAttributes(ArrayList<Attributes> attributes) {
//		mAttributes = attributes;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance().layout(
						"core_information_description_layout"), container,
				false);
		if(getArguments() != null){
		mAttributes = getArguments().getParcelableArrayList(Constants.KeyData.LIST_ATTRIBUTES);
		}
		Log.d("quangdd","mAttributes"+mAttributes.get(0).toString());
		
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
				tv_title.setTypeface(null, Typeface.BOLD);
				tv_title.setTextColor(Config.getInstance().getContent_color());
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
}
