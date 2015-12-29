package com.simicart.plugins.productlabel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.DataLocal;

public class ProductLabel {
	protected Context context;
	protected int textSize = 0;
	protected int sizeLable = 0;

	public ProductLabel(String method, RelativeLayout view, Product entity) {

		this.context = view.getContext();

		String content_label = entity.getData("product_label");

		if (null != content_label) {
			try {
				JSONArray jsonAy = new JSONArray(content_label);
				int total = jsonAy.length();
				for (int i = 0; i < total; i++) {
					JSONObject json = null;
					String imageLabel = "";
					String imagetext = "";
					String positionLabel = "";
					try {
						json = jsonAy.getJSONObject(i);
						imageLabel = json.getString("image");
						imagetext = json.getString("content");
						positionLabel = json.getString("position");
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (!imageLabel.equals("")) {
						if (method.equals("addViewProductHome")) {
							addViewProductHome(view, imagetext, imageLabel,
									positionLabel);
						}
						if (method.equals("addViewProductInList")) {
							addViewProductInList(view, imagetext, imageLabel,
									positionLabel);
						}
						if (method.equals("addViewProductInGrid")) {
							addViewProductInGrid(view, imagetext, imageLabel,
									positionLabel);
						}
						if (method.equals("addViewProductInGrid4Col")) {
							addViewProductInGrid4Col(view, imagetext, imageLabel,
									positionLabel);
						}
					}
				}
			} catch (JSONException e) {

			}
		}
	}
	
	public void addViewProductInGrid4Col(View view, String imagetext,
			String imageLabel, String position) {
		if (DataLocal.isTablet) {
			this.textSize = 16;
			this.sizeLable = 50;
		} else {
			this.textSize = 8;
			this.sizeLable = 26;
		}
		this.addViewGrid(view, imagetext, imageLabel, position);
	}
	
	public void addViewProductInGrid(View view, String imagetext,
			String imageLabel, String position) {
		if (DataLocal.isTablet) {
			this.textSize = 16;
			this.sizeLable = 50;
		} else {
			this.textSize = 14;
			this.sizeLable = 50;
		}
		this.addViewGrid(view, imagetext, imageLabel, position);
	}

	public void addViewProductInList(View view, String imagetext,
			String imageLabel, String position) {
		if (DataLocal.isTablet) {
			this.textSize = 10;
			this.sizeLable = 30;
		} else {
			this.textSize = 10;
			this.sizeLable = 30;
		}
		this.addView(view, imagetext, imageLabel, position);
	}

	public void addViewProductHome(View view, String imagetext,
			String imageLabel, String position) {
		if (DataLocal.isTablet) {
			this.textSize = 14;
			this.sizeLable = 50;
		} else {
			this.textSize = 12;
			this.sizeLable = 40;
		}
		addView(view, imagetext, imageLabel, position);
	}

	public void addView(View view, String imagetext, String imageLabel,
			String position) {
		TextView label = new TextView(context);
		DrawableManager.fetchDrawableOnThread(imageLabel, label);
		label.setText(imagetext);
		label.setMaxLines(1);
		label.setEllipsize(TruncateAt.END);
		label.setTextColor(Color.parseColor("#FFFFFF"));

		label.setGravity(Gravity.CENTER);
		float scale = context.getResources().getDisplayMetrics().density;
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		int size = (int) (sizeLable * scale + 0.5f);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(size,
				size);
		switch (position) {
		case "1":
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			break;
		case "2":
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			break;
		case "3":
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			break;
		case "4":
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			break;
		case "5":
			lp.addRule(RelativeLayout.CENTER_IN_PARENT);
			// lp.addRule(RelativeLayout.CENTER_VERTICAL);
			break;
		case "6":
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			break;
		case "7":
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			break;
		case "8":
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			break;
		case "9":
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			break;
		default:
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			break;
		}
		label.setLayoutParams(lp); // causes layout update
		((ViewGroup) view).addView(label);
	}
	
	public void addViewGrid(View view, String imagetext, String imageLabel,
			String position) {
		TextView label = new TextView(context);
		DrawableManager.fetchDrawableOnThread(imageLabel, label);
		label.setText(imagetext);
		label.setMaxLines(1);
		label.setEllipsize(TruncateAt.END);
		label.setTextColor(Color.parseColor("#FFFFFF"));

		label.setGravity(Gravity.CENTER);
		float scale = context.getResources().getDisplayMetrics().density;
		label.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		int size = (int) (sizeLable * scale + 0.5f);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(size,
				size);
		lp.setMargins(0, 0, Utils.getValueDp(5), 0);
		switch (position) {
		case "1":
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			break;
		case "2":
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			break;
		case "3":
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			break;
		case "4":
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			break;
		case "5":
			lp.addRule(RelativeLayout.CENTER_IN_PARENT);
			// lp.addRule(RelativeLayout.CENTER_VERTICAL);
			break;
		case "6":
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			break;
		case "7":
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			break;
		case "8":
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			break;
		case "9":
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			break;
		default:
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			break;
		}
		label.setLayoutParams(lp); // causes layout update
		((ViewGroup) view).addView(label);
	}
}
