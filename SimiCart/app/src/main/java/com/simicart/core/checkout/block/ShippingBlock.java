package com.simicart.core.checkout.block;

import java.util.ArrayList;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.checkout.controller.ReviewOrderController;
import com.simicart.core.checkout.delegate.ReviewOrderDelegate;
import com.simicart.core.checkout.delegate.ShippingDelegate;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.checkout.model.ShippingMethodModel;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ViewIdGenerator;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.CustomScrollView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShippingBlock extends SimiBlock implements ShippingDelegate {
	protected ReviewOrderDelegate mDelegate;
	protected ReviewOrderController reviewOrder;
	protected LinearLayout ll_shipping;
	protected LinearLayout ll_payment;
	protected int mIDIconNormal;
	protected int mIDIconChecked;
	protected ArrayList<ImageView> lisCheckBoxs;
	private ImageView checkBox;
	private CustomScrollView scrollView;

	protected ArrayList<PaymentMethod> listPaymentMethod = new ArrayList<>();

	public ShippingBlock(View view, Context context) {
		super(view, context);
		ll_shipping = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.id("ll_shipping"));
		ll_payment = (LinearLayout) view.findViewById(Rconfig.getInstance().id(
				"ll_payment"));
		scrollView = (CustomScrollView) mView.findViewById(Rconfig
				.getInstance().id("scrollView1"));
		initIDIconCheckbox();
	}

	public void setReviewOrder(ReviewOrderController reviewOrder) {
		this.reviewOrder = reviewOrder;
	}

	protected void initIDIconCheckbox() {
		mIDIconNormal = Rconfig.getInstance().drawable("core_radiobox");
		mIDIconChecked = Rconfig.getInstance().drawable("core_radiobox2");
	}

	@Override
	public void setShippingMethods(
			ArrayList<ShippingMethod> list_shippingMethods) {
		if (null != list_shippingMethods) {
			ll_shipping.removeAllViews();
			lisCheckBoxs = new ArrayList<ImageView>();
			reviewOrder.setInitViewShipping("");
			for (int i = 0; i < list_shippingMethods.size(); i++) {
				ShippingMethod shppingMethod = list_shippingMethods.get(i);

				RelativeLayout rl_shipping = new RelativeLayout(mContext);
				RelativeLayout.LayoutParams lp_shipping = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				rl_shipping.setLayoutParams(lp_shipping);
				ll_shipping.addView(rl_shipping);

				// shipping price
				TextView tx_price = new TextView(ll_shipping.getContext());
				if (DataLocal.isTablet) {
					tx_price.setPadding(Utils.getValueDp(10), 0,
							Utils.getValueDp(20), 0);
				} else {
					tx_price.setPadding(Utils.getValueDp(5), 0,
							Utils.getValueDp(10), 0);
				}
				String incl_tax = shppingMethod.getS_method_fee_incl_tax();
				String price = shppingMethod.getS_method_fee();

				if (incl_tax == null || incl_tax.equals("")
						|| incl_tax.equals("null")) {
					tx_price.setText(Config.getInstance().getPrice(price));
					tx_price.setTextColor(Color.parseColor(Config.getInstance().getPrice_color()));
				} else {
					String price_method = "<font  color='"+Config.getInstance().getPrice_color()+"'>"
							+ Config.getInstance().getPrice(price)
							+ "</font> <font color='"+Config.getInstance().getContent_color_string()+"'>+"
							+"("+ Config.getInstance().getText("Incl. Tax")
							+ "</font> <font  color='"+Config.getInstance().getPrice_color()+"'> "
							+ Config.getInstance().getPrice(incl_tax)
							+ "</font>" +"<font color='"+ Config.getInstance().getContent_color_string() + "'>"+")"+"</font>";
					tx_price.setText(Html.fromHtml(price_method));
				}
				RelativeLayout.LayoutParams lp_price = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp_price.addRule(RelativeLayout.CENTER_VERTICAL);
				if (DataLocal.isLanguageRTL) {
					lp_price.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					lp_price.setMargins(Utils.getValueDp(30), 0,
							Utils.getValueDp(30), 0);
				} else {
					lp_price.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				}
				tx_price.setLayoutParams(lp_price);
				rl_shipping.addView(tx_price);

				// shipping name
				RelativeLayout rl_value = new RelativeLayout(
						ll_shipping.getContext());
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.CENTER_VERTICAL);
				if (DataLocal.isLanguageRTL) {
					lp.addRule(RelativeLayout.ALIGN_RIGHT, tx_price.getId());
				} else {
					lp.addRule(RelativeLayout.ALIGN_LEFT, tx_price.getId());
				}
				rl_value.setGravity(RelativeLayout.CENTER_VERTICAL);
				rl_value.setLayoutParams(lp);
				rl_shipping.addView(rl_value);

				if (i == (list_shippingMethods.size() - 1)) {

				} else {
					rl_value.setBackgroundResource(Rconfig.getInstance()
							.drawable("bottom_line_border"));
				}
				if (DataLocal.isTablet) {
					rl_value.setPadding(Utils.getValueDp(20),
							Utils.getValueDp(5), Utils.getValueDp(20),
							Utils.getValueDp(10));
				} else {
					rl_value.setPadding(Utils.getValueDp(10),
							Utils.getValueDp(5), Utils.getValueDp(10),
							Utils.getValueDp(10));
				}
				// ll_shipping.addView(rl_value);

				// title
				TextView tv_title = new TextView(ll_shipping.getContext());
				tv_title.setId(ViewIdGenerator.generateViewId());
				tv_title.setText(shppingMethod.getS_method_title(),
						TextView.BufferType.SPANNABLE);
				tv_title.setTextColor(Config.getInstance().getContent_color());
				RelativeLayout.LayoutParams tvtitle_lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				if (DataLocal.isLanguageRTL) {
					tvtitle_lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					tvtitle_lp.setMargins(Utils.getValueDp(30), 0, 0, 0);
					tv_title.setPadding(0, Utils.getValueDp(10), 0, 0);
				} else {
					tvtitle_lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					tvtitle_lp.setMargins(Utils.getValueDp(30), 0,
							Utils.getValueDp(30), 0);
					tv_title.setPadding(0, Utils.getValueDp(10),
							Utils.getValueDp(20), 0);
				}

				tvtitle_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				tv_title.setLayoutParams(tvtitle_lp);
				tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
				rl_value.addView(tv_title);

				// content
				TextView tv_content = new TextView(ll_shipping.getContext());
				tv_content.setTextColor(Config.getInstance().getContent_color());
				tv_content.setId(ViewIdGenerator.generateViewId());
				if (shppingMethod.getS_method_name() != null
						&& !shppingMethod.getS_method_name().equals("")
						&& !shppingMethod.getS_method_name().equals("null")) {
					tv_content.setText(shppingMethod.getS_method_name());
					RelativeLayout.LayoutParams tvcontent_lp = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					if (DataLocal.isLanguageRTL) {
						tvcontent_lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						tvcontent_lp.setMargins(Utils.getValueDp(30), 0, 0, 0);
					} else {
						tvcontent_lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
						tvcontent_lp.setMargins(Utils.getValueDp(30), 0,
								Utils.getValueDp(30), 0);
						tv_content.setPadding(0, Utils.getValueDp(10),
								Utils.getValueDp(20), 0);
					}
					tvcontent_lp
							.addRule(RelativeLayout.BELOW, tv_title.getId());

					tv_content.setLayoutParams(tvcontent_lp);
					tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
					rl_value.addView(tv_content);
				}

				// check box
				checkBox = new ImageView(ll_shipping.getContext());
				RelativeLayout.LayoutParams checkbox_lp = new RelativeLayout.LayoutParams(
						Utils.getValueDp(20), Utils.getValueDp(20));
				checkbox_lp.addRule(RelativeLayout.CENTER_VERTICAL);
				checkbox_lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				checkBox.setLayoutParams(checkbox_lp);

				Drawable icon_nomal = mContext.getResources().getDrawable(
						mIDIconNormal);
				icon_nomal.setColorFilter(Config.getInstance().getContent_color(),
						PorterDuff.Mode.SRC_ATOP);
				checkBox.setImageDrawable(icon_nomal);

				rl_value.addView(checkBox);
				checkBox.setId(ViewIdGenerator.generateViewId());
				lisCheckBoxs.add(checkBox);
				onTouchShipping(rl_value, lisCheckBoxs, checkBox.getId(),
						shppingMethod, list_shippingMethods, i);
				System.out.println(list_shippingMethods);
				handleViewShippingAndPayment(false);
				if (list_shippingMethods.size() == 1) {
					requestSelectShippingMethod(shppingMethod,
							list_shippingMethods, 0);
					handleViewShippingAndPayment(true);
				}
				if (list_shippingMethods.get(i).isS_method_selected()) {
					Drawable icon_checked = mContext.getResources()
							.getDrawable(mIDIconChecked);
					icon_checked.setColorFilter(Config.getInstance().getContent_color(), PorterDuff.Mode.SRC_ATOP);
					checkBox.setImageDrawable(icon_checked);
					reviewOrder.setInitViewShipping(list_shippingMethods.get(i)
							.getS_method_title());
					handleViewShippingAndPayment(true);
				}
			}
		}
	}

	private void handleViewShippingAndPayment(boolean check) {
		// if check = true,hide shipping and visible payment
		if (check == true) {
			Utils.collapse(ll_shipping);
			 if (ConfigCheckout.checkPaymentMethod == true) {
			 if(ConfigCheckout.checkCondition == false){
			 scrollView.fullScroll(ScrollView.FOCUS_DOWN);
			 }
			 } else {
			Utils.expand(ll_payment);
			scrollView.scrollTo(0, 600);
			 }
		} else {
			Utils.expand(ll_shipping);
			Utils.collapse(ll_payment);
			scrollView.scrollTo(0, 500);
		}
	}

	public void onTouchShipping(RelativeLayout rl_value,
			final ArrayList<ImageView> lisCheckBoxs, final int id_chectbox,
			final ShippingMethod shippingMethod,
			final ArrayList<ShippingMethod> list_shippingMethods,
			final int position) {
		rl_value.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestSelectShippingMethod(shippingMethod,
						list_shippingMethods, position);
			}
		});
	}

	protected void requestSelectShippingMethod(
			final ShippingMethod shippingMethod,
			final ArrayList<ShippingMethod> list_shippingMethods,
			final int position) {
		if (!shippingMethod.isS_method_selected()) {
			shippingMethod.setS_method_selected(true);
			for (int i = 0; i < list_shippingMethods.size(); i++) {
				if (i != position) {
					list_shippingMethods.get(i).setS_method_selected(false);
				}
			}
			mDelegate.showDialogLoading();
			final ShippingMethodModel mModel = new ShippingMethodModel();
			mModel.setDelegate(new ModelDelegate() {

				@Override
				public void callBack(String message, boolean isSuccess) {
					mDelegate.dismissDialogLoading();
					if (isSuccess) {
						ConfigCheckout.checkShippingMethod = true;
						reviewOrder.updateListShipping(list_shippingMethods);
						ArrayList<PaymentMethod> paymentMethod = ((ShippingMethodModel) mModel)
								.getListPayment();
						if (null != paymentMethod && paymentMethod.size() > 0) {
							reviewOrder.updatePaymentMethod(paymentMethod);
						}
						TotalPrice totalPrice = ((ShippingMethodModel) mModel)
								.getTotalPrice();
						if (null != totalPrice) {
							reviewOrder.setSavePaymentMethod(totalPrice);
						}

						reviewOrder.updateShippingMethod(shippingMethod);
						reviewOrder
								.updateShipping(((ShippingMethodModel) mModel)
										.getCollection());
						handleViewShippingAndPayment(true);
					} else {
						SimiManager
								.getIntance()
								.showNotify(
										"Some error occurred when save shippingmethod.");
					}
				}
			});

			mModel.addParam("s_method_id", shippingMethod.getS_method_id());
			mModel.addParam("s_method_code", shippingMethod.getS_method_code());
			mModel.request();
		}
	}

	@Override
	public void goneView() {
		if (null != mView) {
			mView.setVisibility(View.GONE);
		}
	}

	public void setDelegate(ReviewOrderDelegate mDelegate) {
		if(mDelegate != null)
		this.mDelegate = mDelegate;
	}

	@Override
	public void setPaymentMethod(ArrayList<PaymentMethod> paymentMethods) {
		this.listPaymentMethod = paymentMethods;
	}
}
