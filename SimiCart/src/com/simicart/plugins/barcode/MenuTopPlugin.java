package com.simicart.plugins.barcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.simicart.MainActivity;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class MenuTopPlugin {

	private static View rootView;
	private RelativeLayout rl_barcode;
	private Context mContext;

	public MenuTopPlugin(String method, View view, SimiEntity entity) {
		rootView = view;
		mContext = MainActivity.context;
		if (method.equals("addBarcodeToSearchView")) {
			addBarcodeToSeachview();
		}
	}

	public MenuTopPlugin(String method) {
		mContext = MainActivity.context;
		if (method.equals("checkquerysearchview")) {
			checkqueryChangeSearch();
		}
	}

	private void addBarcodeToSeachview() {
		SearchView mSearchView = (SearchView) rootView.findViewById(Rconfig
				.getInstance().id("searchpage"));
		LinearLayout linearLayout1 = (LinearLayout) mSearchView.getChildAt(0);
		LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
		LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
		AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3
				.getChildAt(0);
		if (DataLocal.isLanguageRTL) {
			autoComplete.setPadding(0, 0, Utils.getValueDp(35), 0);
		}

		rl_barcode = (RelativeLayout) rootView.findViewById(Rconfig
				.getInstance().id("rl_barcode"));
		ImageView img_barcode = new ImageView(mContext);
		img_barcode.setId(6789);
		Drawable barcode = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_barcode"));
		img_barcode.setBackgroundDrawable(barcode);
		RelativeLayout.LayoutParams paramBarcode = new RelativeLayout.LayoutParams(
				Utils.getValueDp(30), Utils.getValueDp(30));
		paramBarcode.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		paramBarcode.setMargins(0, Utils.getValueDp(10), Utils.getValueDp(10),
				0);
		rl_barcode.addView(img_barcode, paramBarcode);

		img_barcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Constants.SCANNER);
				intent.putExtra("SCAN_MODE", Constants.SCAN_MODE);
				intent.putExtra("SCAN_MODE", Constants.SCAN_MODE_BARCODE);
				SimiManager
						.getIntance()
						.getCurrentActivity()
						.startActivityForResult(intent,
								Constants.RESULT_BARCODE);
			}
		});
	}

	private void checkqueryChangeSearch() {
		rl_barcode = (RelativeLayout) rootView.findViewById(Rconfig
				.getInstance().id("rl_barcode"));
		ImageView imageView = (ImageView) rl_barcode.findViewById(6789);
		if (Constants.checkSearchQuery == true) {
			imageView.setVisibility(View.VISIBLE);
		} else {
			imageView.setVisibility(View.GONE);
		}
	}
}
