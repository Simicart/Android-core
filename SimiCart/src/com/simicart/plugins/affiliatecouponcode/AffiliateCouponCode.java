package com.simicart.plugins.affiliatecouponcode;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.config.Config;
import com.simicart.core.event.others.TotalPriceViewData;

public class AffiliateCouponCode {

	protected Context mContext;
	protected TotalPrice mTotalPrice;
	protected String mSymbol;
	protected int mTextSize = 16;
	protected String mColorLabel = "#000000";
	protected String mColorPrice = "red";
	protected String AFFILIATES_DISCOUNT = "affiliates_discount";

	public AffiliateCouponCode(TotalPriceViewData totalPriceViewData,
			TableLayout tbl_price) {
		Log.e("AffiliateCouponCode ", "AffiliateCouponCode 001");

		mContext = totalPriceViewData.getContext();
		mTotalPrice = totalPriceViewData.getTotalPrice();
		mSymbol = totalPriceViewData.getSymbol();
		mTextSize = totalPriceViewData.getTextSize();
		mColorLabel = totalPriceViewData.getColorLabel();
		mColorPrice = totalPriceViewData.getColorPrice();
		TableRow tbr_affilate = (TableRow) getAffilateCouponCode();
		if (null != tbr_affilate) {
			tbl_price.addView(tbr_affilate);
		}

	}

	protected View getAffilateCouponCode() {
		JSONObject json = mTotalPrice.getJSONObject();
		if (null != json) {
			if (json.has(AFFILIATES_DISCOUNT)) {
				try {
					String discount = json.getString(AFFILIATES_DISCOUNT);
					if (discount != null && !discount.equals("0")
							&& !discount.equals("null")) {
						TableRow tbr_discount = new TableRow(mContext);
						String label = "<font color='"
								+ mColorLabel
								+ "'>"
								+ Config.getInstance().getText(
										"Affiliates Discount") + ": </font>";
						String price = "<font color='" + mColorPrice + "'>"
								+ "-" + Config.getInstance().getPrice(discount)
								+ "</font>";
						if (null != mSymbol) {
							price = "<font color='"
									+ mColorPrice
									+ "'>"
									+ ""
									+ Config.getInstance().getPrice(discount,
											mSymbol) + "</font>";
						}
						TextView tv_label = (TextView) showView(label);
						TextView tv_price = (TextView) showView(price);
						tbr_discount.addView(tv_label);
						tbr_discount.addView(tv_price);
						return tbr_discount;
					}
				} catch (JSONException e) {
					return null;
				}
			}
		}
		return null;
	}

	protected View showView(String content) {
		TextView tv_price = new TextView(mContext);
		tv_price.setGravity(Gravity.RIGHT);
		tv_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
		tv_price.setText(Html.fromHtml(content));
		return tv_price;
	}

}
