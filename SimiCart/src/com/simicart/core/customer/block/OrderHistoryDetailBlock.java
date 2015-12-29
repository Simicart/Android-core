package com.simicart.core.customer.block;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.adapter.ProductOrderAdapter;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.Utils;
import com.simicart.core.common.price.TotalPriceView;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.OrderHistoryReOrderDelegate;
import com.simicart.core.customer.entity.BillingAddress;
import com.simicart.core.customer.entity.OrderHisDetail;
import com.simicart.core.customer.entity.ShippingAddress;
import com.simicart.core.material.ButtonRectangle;

@SuppressLint("DefaultLocale")
public class OrderHistoryDetailBlock extends SimiBlock implements
		OrderHistoryReOrderDelegate {
	protected ButtonRectangle bt_reorder;
	private View view_top;
	private View view_bottom;
	private View view_order_date;

	public OrderHistoryDetailBlock(View view, Context context) {
		super(view, context);
	}

	public void setOnTouchReOrder(OnTouchListener listener) {
		bt_reorder.setOnTouchListener(listener);
	}

	@Override
	public void initView() {
		view_top = mView.findViewById(Rconfig.getInstance()
				.id("view_top"));
		view_bottom = mView.findViewById(Rconfig.getInstance()
				.id("view_bottom"));
		view_order_date = mView.findViewById(Rconfig.getInstance().id("view_order_date"));
		view_top.setBackgroundColor(Config.getInstance().getApp_backrground());
		view_bottom.setBackgroundColor(Config.getInstance().getApp_backrground());
		view_order_date.setBackgroundColor(Config.getInstance().getApp_backrground());
		TextView lb_date = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("lb_date"));
		lb_date.setText(Config.getInstance().getText("Order Date"));
		lb_date.setTextColor(Config.getInstance().getContent_color());
		TextView lb_orderT = (TextView) mView.findViewById(Rconfig
				.getInstance().id("lb_orderT"));
		lb_orderT.setText(Config.getInstance().getText("Order #"));
		lb_orderT.setTextColor(Config.getInstance().getContent_color());
		TextView lb_total = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("lb_total"));
		lb_total.setText(Config.getInstance().getText("Order Total"));
		lb_total.setTextColor(Config.getInstance().getContent_color());

		TextView lb_shipto = (TextView) mView.findViewById(Rconfig
				.getInstance().id("lb_shipto"));
		lb_shipto
				.setText(Config.getInstance().getText("SHIP TO").toUpperCase());
		lb_shipto.setTextColor(Config.getInstance().getSection_text_color());
		lb_shipto.setBackgroundColor(Color.parseColor(Config.getInstance().getSection_color()));
		TextView lb_items = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("lb_items"));
		lb_items.setText(Config.getInstance().getText("ITEMS").toUpperCase());
		lb_items.setTextColor(Config.getInstance().getSection_text_color());
		lb_items.setBackgroundColor(Color.parseColor(Config.getInstance().getSection_color()));
		TextView lb_payment = (TextView) mView.findViewById(Rconfig
				.getInstance().id("lb_payment"));
		lb_payment.setText(Config.getInstance().getText("PAYMENT")
				.toUpperCase());
		lb_payment.setTextColor(Config.getInstance().getSection_text_color());
		lb_payment.setBackgroundColor(Color.parseColor(Config.getInstance().getSection_color()));
		bt_reorder = (ButtonRectangle) mView.findViewById(Rconfig.getInstance()
				.id("bt_reorder"));
		bt_reorder.setTextColor(Color.WHITE);
		bt_reorder.setText(Config.getInstance().getText("Reorder"));
		bt_reorder.setTextSize(16);
		bt_reorder.setBackgroundColor(Config.getInstance().getColorMain());
	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			OrderHisDetail orderHisDetail = (OrderHisDetail) entity.get(0);
			TextView tv_date = (TextView) mView.findViewById(Rconfig
					.getInstance().id("tv_date"));
			tv_date.setText(orderHisDetail.getOrder_date());
			tv_date.setTextColor(Config.getInstance().getContent_color());
			TextView tv_orderT = (TextView) mView.findViewById(Rconfig
					.getInstance().id("tv_orderT"));
			tv_orderT.setText(orderHisDetail.getOrder_code());
			tv_orderT.setTextColor(Config.getInstance().getContent_color());
			TextView tv_total = (TextView) mView.findViewById(Rconfig
					.getInstance().id("tv_total"));
			tv_total.setTextColor(Color.parseColor(Config.getInstance()
					.getPrice_color()));

			String symbol = orderHisDetail.getTotal_price().getCurrencySymbol();
			String price = Config.getInstance().getPrice(
					orderHisDetail.getOrder_total());
			if (null != symbol) {
				price = Config.getInstance().getPrice(
						orderHisDetail.getOrder_total(), symbol);
			}
			tv_total.setText(price);

			ShippingAddress shippingAddress = orderHisDetail
					.getShipping_address();
			if (shippingAddress != null) {
				// name
				TextView st_name = (TextView) mView.findViewById(Rconfig
						.getInstance().id("st_name"));
				st_name.setTextColor(Config.getInstance().getContent_color());
				String name = shippingAddress.getName();
				if (Utils.validateString(name)) {
					st_name.setText(name);
				} else {
					st_name.setVisibility(View.GONE);
				}
				// street
				TextView st_street = (TextView) mView.findViewById(Rconfig
						.getInstance().id("st_street"));
				st_street.setTextColor(Config.getInstance().getContent_color());
				String street = shippingAddress.getStreet();
				if (Utils.validateString(street)) {
					st_street.setText(street);
				} else {
					st_street.setVisibility(View.GONE);
				}
				// city
				TextView st_city = (TextView) mView.findViewById(Rconfig
						.getInstance().id("st_city"));
				st_city.setTextColor(Config.getInstance().getContent_color());
				String city = shippingAddress.getCity();
				if (Utils.validateString(city)) {
					if (shippingAddress.getState_name() == null
							|| shippingAddress.getState_name().equals("null")) {
						st_city.setText(city + ", " + shippingAddress.getZip());
					} else {
						st_city.setText(city + ", "
								+ shippingAddress.getState_name() + ", "
								+ shippingAddress.getZip());
					}
				} else {
					st_city.setVisibility(View.GONE);
				}

				// country
				TextView st_country = (TextView) mView.findViewById(Rconfig
						.getInstance().id("st_country"));
				st_country
						.setTextColor(Config.getInstance().getContent_color());
				String country = shippingAddress.getCountry_name();
				if (Utils.validateString(country)) {
					st_country.setText(country);
				} else {
					st_country.setVisibility(View.GONE);
				}
				// phone
				TextView st_phone = (TextView) mView.findViewById(Rconfig
						.getInstance().id("st_phone"));
				st_phone.setTextColor(Config.getInstance().getContent_color());
				String phone = shippingAddress.getPhone();
				if (Utils.validateString(phone)) {
					st_phone.setText(phone);
				} else {
					st_phone.setVisibility(View.GONE);
				}
				// email
				TextView st_email = (TextView) mView.findViewById(Rconfig
						.getInstance().id("st_email"));
				st_email.setTextColor(Config.getInstance().getContent_color());
				String email = shippingAddress.getEmail();
				if (Utils.validateString(email)) {
					st_email.setText(email);
				} else {
					st_email.setVisibility(View.GONE);
				}
			} else {
				LinearLayout ll_shipping = (LinearLayout) mView
						.findViewById(Rconfig.getInstance().id("ll_shipping"));
				ll_shipping.setVisibility(View.GONE);
			}

			TextView st_shipingmethod = (TextView) mView.findViewById(Rconfig
					.getInstance().id("st_shipingmethod"));
			st_shipingmethod.setTextColor(Config.getInstance()
					.getContent_color());

			String shippingMethod = orderHisDetail.getShipping_method();
			if (Utils.validateString(shippingMethod)) {
				st_shipingmethod.setText(shippingMethod);
			} else {
				TextView lb_shipto = (TextView) mView.findViewById(Rconfig
						.getInstance().id("lb_shipto"));
				lb_shipto.setVisibility(View.GONE);
				st_shipingmethod.setVisibility(View.GONE);
			}

			// Payment
			TextView p_paymentmethod = (TextView) mView.findViewById(Rconfig
					.getInstance().id("p_paymentmethod"));
			p_paymentmethod.setText(orderHisDetail.getPayment_method());
			p_paymentmethod.setTextColor(Config.getInstance()
					.getContent_color());

			BillingAddress billingAddress = orderHisDetail.getBilling_address();
			TextView p_name = (TextView) mView.findViewById(Rconfig
					.getInstance().id("p_name"));
			p_name.setTextColor(Config.getInstance().getContent_color());
			p_name.setText(billingAddress.getName());
			TextView p_street = (TextView) mView.findViewById(Rconfig
					.getInstance().id("p_street"));
			p_street.setTextColor(Config.getInstance().getContent_color());
			p_street.setText(billingAddress.getStreet());
			TextView p_city = (TextView) mView.findViewById(Rconfig
					.getInstance().id("p_city"));
			p_city.setTextColor(Config.getInstance().getContent_color());
			if (billingAddress.getState_name() == null
					|| billingAddress.getState_name().equals("null")) {
				p_city.setText(billingAddress.getCity() + ", "
						+ billingAddress.getZip());
			} else {
				p_city.setText(billingAddress.getCity() + ", "
						+ billingAddress.getState_name() + ", "
						+ billingAddress.getZip());
			}
			TextView p_country = (TextView) mView.findViewById(Rconfig
					.getInstance().id("p_country"));
			p_country.setTextColor(Config.getInstance().getContent_color());
			p_country.setText(billingAddress.getCountry_name());
			TextView p_phone = (TextView) mView.findViewById(Rconfig
					.getInstance().id("p_phone"));
			p_phone.setTextColor(Config.getInstance().getContent_color());
			p_phone.setText(billingAddress.getPhone());
			TextView p_email = (TextView) mView.findViewById(Rconfig
					.getInstance().id("p_email"));
			p_email.setTextColor(Config.getInstance().getContent_color());
			p_email.setText(billingAddress.getEmail());

			TextView p_couponCode = (TextView) mView.findViewById(Rconfig
					.getInstance().id("p_couponCode"));
			p_couponCode.setTextColor(Config.getInstance().getContent_color());
			if (orderHisDetail.getOrder_gift_code() == null
					|| orderHisDetail.getOrder_gift_code().equals("null")) {
				p_couponCode.setText(Config.getInstance()
						.getText("Coupon Code")
						+ ": "
						+ Config.getInstance().getText("NONE").toUpperCase());
			} else {
				p_couponCode.setText(Config.getInstance()
						.getText("Coupon Code")
						+ ": "
						+ orderHisDetail.getOrder_gift_code());
			}

			// Fee detail
			LinearLayout ll_price = (LinearLayout) mView.findViewById(Rconfig
					.getInstance().id("ll_price"));
			TextView tv_label_price = (TextView) mView.findViewById(Rconfig
					.getInstance().id("lb_feeDetail"));
			tv_label_price.setTextColor(Config.getInstance().getSection_text_color());
			tv_label_price.setBackgroundColor(Color.parseColor(Config.getInstance().getSection_color()));
			TotalPrice totalPrice = orderHisDetail.getTotal_price();
			TotalPriceView viewPrice = new TotalPriceView(totalPrice);

			if (null != symbol) {

				viewPrice.setSymbol(symbol);
			}

			View view = viewPrice.getTotalPriceView();
			LinearLayout.LayoutParams params = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.RIGHT;
			params.setMargins(0, 5, 15, 5);
			if (null != view) {
				ll_price.removeAllViews();
				ll_price.addView(view, params);
				tv_label_price.setText(Config.getInstance()
						.getText("FEE DETAIL").toUpperCase());
			} else {
				tv_label_price.setVisibility(View.GONE);
				ll_price.setVisibility(View.GONE);
			}

			// list item
			ListView list_item = (ListView) mView.findViewById(Rconfig
					.getInstance().id("list_item"));
			ProductOrderAdapter orderAdapter = new ProductOrderAdapter(
					mContext, orderHisDetail.getOrder_item());
			if (null != symbol) {
				orderAdapter.setCurrencySymbol(symbol);
			}
			list_item.setAdapter(orderAdapter);

		}
	}

	@Override
	public ButtonRectangle reOrder() {
		// TODO Auto-generated method stub
		return bt_reorder;
	}
}
