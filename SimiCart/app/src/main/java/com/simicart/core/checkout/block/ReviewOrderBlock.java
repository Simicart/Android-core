package com.simicart.core.checkout.block;

import java.util.ArrayList;

import org.jsoup.Jsoup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.adapter.ProductOrderAdapter;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.checkout.delegate.ReviewOrderDelegate;
import com.simicart.core.checkout.entity.Condition;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.checkout.fragment.ConditionFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ViewIdGenerator;
import com.simicart.core.common.price.TotalPriceView;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.material.ButtonRectangle;
import com.simicart.core.style.CustomScrollView;

@SuppressLint("DefaultLocale")
public class ReviewOrderBlock extends SimiBlock implements ReviewOrderDelegate {

	private ButtonRectangle bt_placenow;
	private ListView lv_products;
	// private RelativeLayout rl_billingAddress;
	// private RelativeLayout rl_shippingAddress;
	private ImageView img_editBilling;
	private ImageView img_editShipping;
	private EditText edt_couponCode;
	public boolean accept = false;

	private CustomScrollView scrollView;

	private LinearLayout ll_shipping;
	private LinearLayout ll_payment;

	public ReviewOrderBlock(View view, Context context) {
		super(view, context);
	}

	public void setClickPlaceNow(OnClickListener onPlaceOrder) {
		bt_placenow.setOnClickListener(onPlaceOrder);
	}

	public void setOnViewDetaiProduct(OnItemClickListener onViewDetailProduct) {
		lv_products.setOnItemClickListener(onViewDetailProduct);
	}

	public void setOnChoiceShippingAddress(
			OnClickListener onChoiceShippingAddress) {
		img_editShipping.setOnClickListener(onChoiceShippingAddress);
	}

	public void setOnChoiceBillingAddress(OnClickListener onChoiceBillingAddress) {
		img_editBilling.setOnClickListener(onChoiceBillingAddress);
	}

	public void setCouponCodeListener(OnEditorActionListener listener) {
		edt_couponCode.setOnEditorActionListener(listener);
	}

	@Override
	public void initView() {
		scrollView = (CustomScrollView) mView.findViewById(Rconfig
				.getInstance().id("scrollView1"));
		scrollView
				.setBackgroundColor(Config.getInstance().getApp_backrground());
		// button place now
		initButtonPlaceNowView();

		// address
		initAddressView();

		// coupon code
		initCouponCodeView();

		// shipment details
		initShipmentDetailsView();

		// label payment method
		initLabelPaymentMethod();

		// label shpping
		initShippingView();
	}

	protected void initButtonPlaceNowView() {
		bt_placenow = (ButtonRectangle) mView.findViewById(Rconfig
				.getInstance().id("bt_placenow2"));
		bt_placenow.setText(Config.getInstance().getText("Place now"));
		bt_placenow.setTextColor(Color.WHITE);
		bt_placenow.setTextSize(Constants.SIZE_TEXT_BUTTON);
		bt_placenow.setBackgroundColor(Config.getInstance().getColorMain());
	}

	protected void initAddressView() {
		// billing address
		TextView tv_billing_address = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_billing_address"));
		tv_billing_address.setText(Config.getInstance().getText(
				"Billing Address"));
		tv_billing_address.setTextColor(Config.getInstance()
				.getSection_text_color());
		tv_billing_address.setBackgroundColor(Color.parseColor(Config
				.getInstance().getSection_color()));
		// rl_billingAddress = (RelativeLayout) mView.findViewById(Rconfig
		// .getInstance().id("billing_address"));
		Drawable ic_edit = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_icon_edit"));
		ic_edit.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		img_editBilling = (ImageView) mView.findViewById(Rconfig.getInstance()
				.id("img_edit_b_address"));
		img_editBilling.setImageDrawable(ic_edit);

		// shipping address
		TextView tv_shipping_address = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_shipping_address"));
		tv_shipping_address.setText(Config.getInstance().getText(
				"Shipping Address"));
		tv_shipping_address.setTextColor(Config.getInstance()
				.getSection_text_color());
		tv_shipping_address.setBackgroundColor(Color.parseColor(Config
				.getInstance().getSection_color()));
		// rl_shippingAddress = (RelativeLayout) mView.findViewById(Rconfig
		// .getInstance().id("shipping_address"));
		img_editShipping = (ImageView) mView.findViewById(Rconfig.getInstance()
				.id("img_edit_s_address"));
		img_editShipping.setImageDrawable(ic_edit);

	}

	protected void initShippingView() {
		RelativeLayout layout_shipping = (RelativeLayout) mView
				.findViewById(Rconfig.getInstance().id("shipping_method_rl"));
		TextView tv_shippingMethod = (TextView) mView.findViewById(Rconfig
				.getInstance().id("shipping_methods"));
		tv_shippingMethod.setText(Config.getInstance().getText(
				"Shipping Method"));
		layout_shipping.setBackgroundColor(Color.parseColor(Config
				.getInstance().getSection_color()));
		tv_shippingMethod.setBackgroundColor(Color.parseColor(Config
				.getInstance().getSection_color()));
		tv_shippingMethod.setTextColor(Config.getInstance()
				.getSection_text_color());
		ll_shipping = (LinearLayout) mView.findViewById(Rconfig.getInstance()
				.id("ll_shipping"));
		final ImageView ic_expand = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("ic_expand"));

		layout_shipping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ll_shipping.getVisibility() == View.VISIBLE) {
					ic_expand.setRotation(0);
					// ll_shipping.setVisibility(View.GONE);
					Utils.collapse(ll_shipping);
				} else {
					ic_expand.setRotation(90);
					// ll_shipping.setVisibility(View.VISIBLE);
					Utils.expand(ll_shipping);
				}
			}
		});
	}

	protected void initCouponCodeView() {
		edt_couponCode = (EditText) mView.findViewById(Rconfig.getInstance()
				.id("edt_counponCode"));
		edt_couponCode.setHighlightColor(Color.parseColor("#b2b2b2"));
		edt_couponCode.setHint(Config.getInstance().getText(
				"Enter a coupon code"));
	}

	protected void initShipmentDetailsView() {
		TextView tv_shipment_details = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_shipment_details"));
		tv_shipment_details.setBackgroundColor(Color.parseColor(Config
				.getInstance().getSection_color()));
		tv_shipment_details.setTextColor(Config.getInstance()
				.getSection_text_color());
		tv_shipment_details.setText(Config.getInstance().getText(
				"Shipment Details"));
		setProductList();
	}

	protected void initLabelPaymentMethod() {
		RelativeLayout rl_payment = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("payment_method_rl"));
		final ImageView ic_expand_payment = (ImageView) mView
				.findViewById(Rconfig.getInstance().id("ic_expand_payment"));
		TextView tv_label_payment_method = (TextView) mView
				.findViewById(Rconfig.getInstance().id("tv_payment"));
		tv_label_payment_method
				.setText(Config.getInstance().getText("Payment"));
		rl_payment.setBackgroundColor(Color.parseColor(Config.getInstance()
				.getSection_color()));
		tv_label_payment_method.setBackgroundColor(Color.parseColor(Config
				.getInstance().getSection_color()));
		tv_label_payment_method.setTextColor(Config.getInstance()
				.getSection_text_color());
		ll_payment = (LinearLayout) mView.findViewById(Rconfig.getInstance()
				.id("ll_payment"));
		rl_payment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ll_payment.getVisibility() == View.VISIBLE) {
					ic_expand_payment.setRotation(0);
					// ll_payment.setVisibility(View.GONE);
					Utils.collapse(ll_payment);
					if (DataLocal.isTablet) {
						View view = (View) mView.findViewById(Rconfig
								.getInstance().id("line_payment"));
						// view.setVisibility(View.GONE);
						Utils.collapse(view);
					}
					// Animation animOut =
					// AnimationUtils.loadAnimation(mContext, Rconfig
					// .getInstance().getId("view_to_up", "anim"));
					// ll_payment.startAnimation(animOut);
					// animOut.setAnimationListener(new AnimationListener() {
					// @Override
					// public void onAnimationEnd(Animation animation) {
					// ll_payment.setVisibility(View.GONE);
					// }

					// @Override
					// public void onAnimationRepeat(Animation animation) {
					// }
					//
					// @Override
					// public void onAnimationStart(Animation animation) {
					// }
					// });
				} else {
					ic_expand_payment.setRotation(90);
					// ll_payment.startAnimation(AnimationUtils.loadAnimation(mContext,
					// Rconfig.getInstance().getId("view_to_down", "anim")));
					// ll_payment.setVisibility(View.VISIBLE);
					Utils.expand(ll_payment);
					if (DataLocal.isTablet) {
						View view = (View) mView.findViewById(Rconfig
								.getInstance().id("line_payment"));
						view.setVisibility(View.VISIBLE);
					}
				}
			}
		});
	}

	@Override
	public void onUpdateAddress(MyAddress address) {

	}

	@Override
	public void setShipingMethods(ArrayList<ShippingMethod> shippingMethods) {
	}

	@Override
	public void setConditions(final ArrayList<Condition> conditions) {
		if (conditions != null && conditions.size() != 0) {

			LinearLayout ll_confirm = (LinearLayout) mView.findViewById(Rconfig
					.getInstance().id("ll_confirm"));
			ll_confirm.removeAllViews();

			final boolean check[] = new boolean[conditions.size()];

			for (int i = 0; i < conditions.size(); i++) {
				final int index = i;
				check[index] = conditions.get(index).isChecked();
				if (!check[index]) {
					Config.getInstance().setEnable_agreements(0);
				}
				final RelativeLayout rlt_termCondition = new RelativeLayout(
						mContext);
				RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				if (DataLocal.isTablet) {
					Utils.setPadding(rlt_termCondition, 20, 5, 18, 2);
				} else {
					Utils.setPadding(rlt_termCondition, 10, 5, 5, 2);
				}
				rlt_termCondition.setLayoutParams(lp2);
				ll_confirm.addView(rlt_termCondition);

				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				if (DataLocal.isLanguageRTL) {
					lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				} else {
					lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				}
				lp.addRule(RelativeLayout.CENTER_VERTICAL);

				TextView tv_confirm = new TextView(mContext);
				int id_tvConfirm = ViewIdGenerator.generateViewId();
				tv_confirm.setId(id_tvConfirm);
				tv_confirm.setPadding(0, 0, Utils.getValueDp(20), 0);
				tv_confirm.setLayoutParams(lp);
				tv_confirm
						.setTextColor(Config.getInstance().getContent_color());
				if (DataLocal.isTablet) {
					tv_confirm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
				} else {
					tv_confirm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
				}
				tv_confirm.setMaxLines(3);
				tv_confirm.setEllipsize(TruncateAt.END);

				String title = conditions.get(i).getTitle()
						+ conditions.get(i).getContent();

				// title = Html.fromHtml(title).toString();
				/* 24/11/2015 START Johan fix bug show short term condition */
				title = Html.fromHtml(Jsoup.parse(title).text()).toString();
				/* END Johan fix bug show short term condition */

				tv_confirm.setText(Html.fromHtml(title).toString());
				rlt_termCondition.addView(tv_confirm);

				final String content = conditions.get(i).getContent();
				rlt_termCondition.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ConditionFragment fragment = ConditionFragment
								.newInstance(content);
						// fragment.setContent(content);
						SimiManager.getIntance().replacePopupFragment(fragment);
					}
				});

				RelativeLayout.LayoutParams lp_im;
				if (DataLocal.isTablet) {
					lp_im = new RelativeLayout.LayoutParams(
							Utils.getValueDp(22), Utils.getValueDp(22));
				} else {
					lp_im = new RelativeLayout.LayoutParams(
							Utils.getValueDp(15), Utils.getValueDp(15));
				}
				lp_im.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				lp_im.addRule(RelativeLayout.CENTER_VERTICAL);
				ImageView im_cf = new ImageView(mContext);
				im_cf.setLayoutParams(lp_im);
				im_cf.setImageResource(Rconfig.getInstance().drawable(
						"ic_action_expand"));
				changeColorImageView(im_cf, "ic_action_expand");
				rlt_termCondition.addView(im_cf);

				LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp3.gravity = Gravity.LEFT;
				if (DataLocal.isTablet) {
					lp3.setMargins(Utils.getValueDp(14), 0,
							Utils.getValueDp(10), Utils.getValueDp(10));
				} else {
					lp3.setMargins(Utils.getValueDp(4), 0,
							Utils.getValueDp(10), Utils.getValueDp(10));
				}
				CheckBox checkBox = new CheckBox(mContext);
				if (DataLocal.isTablet) {
					checkBox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
				} else {
					checkBox.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
				}
				// Utils.setPadding(checkBox, 15, 5, 5, 2);
				// checkBox.setPadding(Utils.getValueDp(20), 0, 0, 0);
				String checkboxTitle = Html.fromHtml(
						Jsoup.parse(conditions.get(i).getCheckText())
								.toString()).toString();
				checkBox.setText(Html.fromHtml(checkboxTitle).toString());
				checkBox.setTextColor(Config.getInstance().getContent_color());
				// checkBox.setButtonDrawable(Rconfig.getInstance().drawable(
				// "check_box"));
				checkBox.setLayoutParams(lp3);
				ll_confirm.addView(checkBox);
				checkBox.setChecked(check[i]);

				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						check[index] = isChecked;
						conditions.get(index).setChecked(isChecked);
						ConfigCheckout.checkCondition = true;
						for (int i = 0; i < check.length; i++) {
							if (!check[i]) {
								Config.getInstance().setEnable_agreements(0);
								break;
							}
							Config.getInstance().setEnable_agreements(1);
						}
					}
				});
			}
		} else {
			LinearLayout ll_confirm = (LinearLayout) mView.findViewById(Rconfig
					.getInstance().id("ll_confirm"));
			ll_confirm.setVisibility(View.GONE);
		}

	}

	private void changeColorImageView(ImageView img, String src) {
		Drawable icon = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable(src));
		icon.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		img.setImageDrawable(icon);
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void setTotalPrice(TotalPrice totalPrice) {

		// label
		TextView lb_feeDetail = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_feeDetail"));
		lb_feeDetail.setText(Config.getInstance().getText("Fee Detail")
				.toUpperCase());
		lb_feeDetail.setVisibility(View.GONE);

		// coupon code
		String couponcode = totalPrice.getData(Constants.COUPON_CODE);
		if (null != couponcode && !couponcode.equals("")
				&& !couponcode.equals("null")) {
			edt_couponCode.setText(couponcode);
		}

		LinearLayout ll_price = (LinearLayout) mView.findViewById(Rconfig
				.getInstance().id("ll_price"));

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.RIGHT;
		params.setMargins(0, 10, 10, 10);
		TotalPriceView viewPrice = new TotalPriceView(totalPrice);
		View view = viewPrice.getTotalPriceView();
		if (null != view) {
			ll_price.removeAllViewsInLayout();
			ll_price.addView(view, params);
		}

	}

	public void setProductList() {
		lv_products = (ListView) mView.findViewById(Rconfig.getInstance().id(
				"review_product_list"));
		ProductOrderAdapter productAdapter = new ProductOrderAdapter(mContext,
				DataLocal.listCarts);
		lv_products.setAdapter(productAdapter);
	}

	@Override
	public void setBillingAddress(MyAddress address) {

		RelativeLayout rlt_billingAddr = (RelativeLayout) mView
				.findViewById(Rconfig.getInstance().id("billing_address"));
		rlt_billingAddr.setVisibility(View.VISIBLE);
		// billing address
		Drawable ic_name = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_bg_name_customer"));
		ic_name.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		Drawable ic_address = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_bg_location"));
		ic_address.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		Drawable ic_phone = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_bg_call"));
		ic_phone.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		Drawable ic_email = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_icon_email"));
		ic_email.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);

		ImageView img_iconBName = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_b_name"));
		img_iconBName.setImageDrawable(ic_name);
		ImageView img_iconBAddress = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_b_street"));
		img_iconBAddress.setImageDrawable(ic_address);
		ImageView img_iconBPhone = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_b_phone"));
		img_iconBPhone.setImageDrawable(ic_phone);
		ImageView img_iconBEmail = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_b_email"));
		img_iconBEmail.setImageDrawable(ic_email);

		TextView name = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_b_name"));
		TextView street = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_b_street"));
		TextView phone = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_b_phone"));
		TextView email = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_b_email"));
		name.setTextColor(Config.getInstance().getContent_color());
		street.setTextColor(Config.getInstance().getContent_color());
		phone.setTextColor(Config.getInstance().getContent_color());
		email.setTextColor(Config.getInstance().getContent_color());
		name.setText(address.getName());

		String street_text = address.getStreet();
		if (address.getStateName() == null
				|| address.getStateName().equals("null")) {
			street_text = street_text + ", " + address.getCity() + ", "
					+ address.getZipCode();
		} else {
			street_text = street_text + ", " + address.getCity() + ", "
					+ address.getStateName() + ", " + address.getZipCode();
		}
		street_text = street_text + ", " + address.getCountryName();

		street.setText(street_text);
		phone.setText(address.getPhone());
		email.setText(address.getEmail());
	}

	@Override
	public void setShipingAddress(MyAddress address) {

		RelativeLayout rlt_shippingAdr = (RelativeLayout) mView
				.findViewById(Rconfig.getInstance().id("shipping_address"));
		rlt_shippingAdr.setVisibility(View.VISIBLE);

		// shipping address
		Drawable ic_name = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_bg_name_customer"));
		ic_name.setColorFilter(Color.parseColor("#737373"),
				PorterDuff.Mode.SRC_ATOP);
		Drawable ic_address = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_bg_location"));
		ic_address.setColorFilter(Color.parseColor("#737373"),
				PorterDuff.Mode.SRC_ATOP);
		Drawable ic_phone = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_bg_call"));
		ic_phone.setColorFilter(Color.parseColor("#737373"),
				PorterDuff.Mode.SRC_ATOP);
		Drawable ic_email = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_icon_email"));
		ic_email.setColorFilter(Color.parseColor("#737373"),
				PorterDuff.Mode.SRC_ATOP);

		ImageView img_iconSName = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_s_name"));
		img_iconSName.setImageDrawable(ic_name);
		ImageView img_iconSAddress = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_s_street"));
		img_iconSAddress.setImageDrawable(ic_address);
		ImageView img_iconSPhone = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_s_phone"));
		img_iconSPhone.setImageDrawable(ic_phone);
		ImageView img_iconSEmail = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_s_email"));
		img_iconSEmail.setImageDrawable(ic_email);

		TextView name = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_s_name"));
		TextView street = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_s_street"));
		TextView phone = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_s_phone"));
		TextView email = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_s_email"));

		name.setTextColor(Config.getInstance().getContent_color());
		street.setTextColor(Config.getInstance().getContent_color());
		phone.setTextColor(Config.getInstance().getContent_color());
		email.setTextColor(Config.getInstance().getContent_color());

		name.setText(address.getName());
		String street_text = address.getStreet();

		if (address.getStateName() == null
				|| address.getStateName().equals("null")) {
			street_text = street_text + ", " + address.getCity() + ", "
					+ address.getZipCode();
		} else {
			street_text = street_text + ", " + address.getCity() + ", "
					+ address.getStateName() + ", " + address.getZipCode();
		}
		street_text = street_text + ", " + address.getCountryName();

		street.setText(street_text);

		phone.setText(address.getPhone());
		email.setText(address.getEmail());
	}

	@Override
	public void goneView() {
		if (null != mView) {
			mView.setVisibility(View.GONE);
		}
	}

	@Override
	public void setInitViewShippingMethod(String shippingName) {
		TextView tv_shippingMethod = (TextView) mView.findViewById(Rconfig
				.getInstance().id("shipping_methods"));
		if (shippingName.equals("")) {
			tv_shippingMethod.setText(Config.getInstance().getText(
					"Shipping Method"));
		} else {
			if (DataLocal.isLanguageRTL) {
				tv_shippingMethod.setText(Config.getInstance().getText(
						shippingName)
						+ " :"
						+ Config.getInstance().getText("Shipping Method"));
			} else {
				tv_shippingMethod.setText(Config.getInstance().getText(
						"Shipping Method")
						+ ": " + Config.getInstance().getText(shippingName));
			}
		}
	}

	@Override
	public void setInitViewPaymentMethod(String paymentName) {
		TextView tv_label_payment_method = (TextView) mView
				.findViewById(Rconfig.getInstance().id("tv_payment"));
		if (paymentName.equals("")) {
			tv_label_payment_method.setText(Config.getInstance().getText(
					"Payment"));
		} else {
			if (DataLocal.isLanguageRTL) {
				tv_label_payment_method.setText(Config.getInstance().getText(
						paymentName)
						+ " :" + Config.getInstance().getText("Payment"));
			} else {
				tv_label_payment_method.setText(Config.getInstance().getText(
						"Payment")
						+ ": " + Config.getInstance().getText(paymentName));
			}
		}
	}

	@Override
	public void scrollDown() {
		scrollView.fullScroll(ScrollView.FOCUS_DOWN);
	}

	@Override
	public void scrollCenter() {
		scrollView.scrollTo(0, 600);
	}

	@Override
	public LinearLayout getLayoutShipping() {
		return ll_shipping;
	}

	@Override
	public LinearLayout getLayoutPayment() {
		return ll_payment;
	}
}
