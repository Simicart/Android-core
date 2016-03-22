package com.simicart.core.checkout.block;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.checkout.controller.ReviewOrderController;
import com.simicart.core.checkout.delegate.PaymentMethodDelegate;
import com.simicart.core.checkout.delegate.ReviewOrderDelegate;
import com.simicart.core.checkout.entity.CreditcardEntity;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.checkout.fragment.CreditCardFragment;
import com.simicart.core.checkout.model.PaymentMethodModel;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ViewIdGenerator;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.style.CustomScrollView;

public class PaymentMethodBlock extends SimiBlock implements
		PaymentMethodDelegate {

	protected LinearLayout ll_payment;
	protected LinearLayout ll_shipping;
	protected ArrayList<ImageView> lisCheckBoxs;
	protected ArrayList<TextView> listContent;
	protected ImageView checkBox;
	protected ReviewOrderDelegate mDelegate;
	protected ReviewOrderController reviewOrder;

	protected int mIDIconNormal;
	protected int mIDIconChecked;
	private CustomScrollView scrollView;
	private ArrayList<ShippingMethod> listShippingMethod = new ArrayList<>();

	public PaymentMethodBlock(String method) {
		if (method.equals("receiver_message")) {
			Toast.makeText(SimiManager.getIntance().getCurrentContext(),
					"Intent Detected.", Toast.LENGTH_LONG).show();
			if (ll_payment != null) {
				ll_payment.setVisibility(View.VISIBLE);
			}
		}
	}

	public PaymentMethodBlock(View view, Context context) {
		super(view, context);
		ll_payment = (LinearLayout) view.findViewById(Rconfig.getInstance().id(
				"ll_payment"));
		ll_shipping = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.id("ll_shipping"));
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
	public void setPaymentMethods(ArrayList<PaymentMethod> paymentMethods) {
		ll_payment.removeAllViews();
		lisCheckBoxs = new ArrayList<ImageView>();
		listContent = new ArrayList<TextView>();

		reviewOrder.setInitViewPayment("");
		String prePayment = PaymentMethod.getInstance().getPlacePaymentMethod();
		if (!checkPreviousPayment(prePayment, paymentMethods)) {
			PaymentMethod.getInstance().setPlacePaymentMethod("");
		}

		for (int i = 0; i < paymentMethods.size(); i++) {
			final PaymentMethod paymentMethod = paymentMethods.get(i);
			RelativeLayout rl_value = new RelativeLayout(mContext);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rl_value.setGravity(RelativeLayout.CENTER_VERTICAL);
			rl_value.setLayoutParams(lp);
			if (i == (paymentMethods.size() - 1)) {

			} else {
				rl_value.setBackgroundResource(Rconfig.getInstance().drawable(
						"bottom_line_border"));
			}
			if (DataLocal.isTablet) {
				rl_value.setPadding(Utils.getValueDp(20), Utils.getValueDp(5),
						Utils.getValueDp(20), Utils.getValueDp(10));
			} else {
				rl_value.setPadding(Utils.getValueDp(10), Utils.getValueDp(5),
						Utils.getValueDp(10), Utils.getValueDp(10));
			}
			ll_payment.addView(rl_value);

			// title
			TextView tv_title = new TextView(ll_payment.getContext());
			tv_title.setTextColor(Config.getInstance().getContent_color());
			tv_title.setId(ViewIdGenerator.generateViewId());
			tv_title.setText(paymentMethod.getTitle(),
					TextView.BufferType.SPANNABLE);
			RelativeLayout.LayoutParams tvtitle_lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			if (DataLocal.isLanguageRTL) {
				tvtitle_lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			} else {
				tvtitle_lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			}
			tvtitle_lp.setMargins(Utils.getValueDp(30), 0, 0, 0);
			tvtitle_lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			tv_title.setLayoutParams(tvtitle_lp);
			tv_title.setPadding(0, Utils.getValueDp(10), Utils.getValueDp(20),
					0);
			tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
			rl_value.addView(tv_title);

			// content
			int show_type = paymentMethod.getShow_type();
			TextView tv_content = new TextView(ll_payment.getContext());
			tv_content.setTextColor(Config.getInstance().getContent_color());
			tv_content.setId(ViewIdGenerator.generateViewId());
			if (show_type == 0 && paymentMethod.getData("content") != null
					&& !paymentMethod.getData("content").equals("")
					&& !paymentMethod.getData("content").equals("null")) {
				paymentMethod.setContent(paymentMethod.getData("content"));
				tv_content.setText(paymentMethod.getContent(),
						TextView.BufferType.SPANNABLE);
				RelativeLayout.LayoutParams tvcontent_lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				tvcontent_lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				tvcontent_lp.addRule(RelativeLayout.BELOW, tv_title.getId());
				tvcontent_lp.setMargins(Utils.getValueDp(30), 0, 0, 0);
				tv_content.setLayoutParams(tvcontent_lp);

				tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				tv_content.setVisibility(View.GONE);
				rl_value.addView(tv_content);
				listContent.add(tv_content);
			}

			// check box
			final ImageView checkBox = new ImageView(ll_payment.getContext());
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
			if (show_type == 1) {
				final ImageView img_edit = new ImageView(
						ll_payment.getContext());
				RelativeLayout.LayoutParams img_edit_lp = new RelativeLayout.LayoutParams(
						Utils.getValueDp(30), Utils.getValueDp(30));
				img_edit_lp.addRule(RelativeLayout.CENTER_VERTICAL);
				img_edit_lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				// img_edit_lp.addRule(RelativeLayout.ALIGN_LEFT);
				img_edit.setLayoutParams(img_edit_lp);

				Drawable icon_edit = mContext.getResources().getDrawable(
						Rconfig.getInstance().drawable("core_icon_edit"));
				img_edit.setImageDrawable(icon_edit);
				rl_value.addView(img_edit);
				rl_value.addView(tv_content);
				img_edit.setPadding(15, 15, 15, 15);
				RelativeLayout.LayoutParams tvcontent_lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				tvcontent_lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				tvcontent_lp.addRule(RelativeLayout.BELOW, tv_title.getId());

				tvcontent_lp.setMargins(50, 0, 0, 0);
				tv_content.setLayoutParams(tvcontent_lp);
				listContent.add(tv_content);
				setContentPaymentMethod(paymentMethod.getPayment_method(),
						tv_content);
				img_edit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						PaymentMethod.getInstance().setPlacePaymentMethod(
								paymentMethod.getPayment_method());
						setCheckBox(mContext, paymentMethod, lisCheckBoxs,
								checkBox.getId());
						nextCreditCardFragment(paymentMethod);
					}
				});
			}
			if (paymentMethod.getPayment_method().equals(
					PaymentMethod.getInstance().getPlacePaymentMethod())) {
				Drawable icon_checked = mContext.getResources().getDrawable(
						mIDIconChecked);
				icon_checked.setColorFilter(
						Config.getInstance().getColorMain(),
						PorterDuff.Mode.SRC_ATOP);
				checkBox.setImageDrawable(icon_checked);
				reviewOrder.setInitViewPayment(paymentMethod.getTitle());
			}

			onTouchPayment(rl_value, checkBox.getId(), lisCheckBoxs,
					tv_content.getId(), listContent, paymentMethod);
		}
	}

	private void setContentPaymentMethod(String paymentMethodCode,
			TextView tv_content) {
		String number = "";
		if (isSavedCC(paymentMethodCode)
				&& paymentMethodCode.equals(PaymentMethod.getInstance()
						.getmCheckPaymentMethod())) {
			// have been data and check co phai la payment clicked is display
			// content
			number = DataLocal.getHashMapCreditCart()
					.get(DataLocal.getEmailCreditCart()).get(paymentMethodCode)
					.getPaymentNumber();
			checkAndSetText(number, tv_content);
		} else {
			// the first, the new sigin is not show content
			tv_content.setVisibility(RelativeLayout.GONE);
		}
	}

	private boolean isSavedCC(String paymentMethodCode) {
		HashMap<String, HashMap<String, CreditcardEntity>> hashMap = DataLocal
				.getHashMapCreditCart();
		if (hashMap == null || hashMap.size() == 0) {
			return false;
		} else {
			if (hashMap.containsKey(DataLocal.getEmailCreditCart())) {
				HashMap<String, CreditcardEntity> creditcard = hashMap
						.get(DataLocal.getEmailCreditCart());
				if (creditcard.containsKey(paymentMethodCode)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	public void nextCreditCardFragment(PaymentMethod paymentMethod) {

		CreditCardFragment fcreditCard = CreditCardFragment.newInstance(true,
				paymentMethod);

		if (PaymentMethod.getInstance().getCurrentMethod().toLowerCase()
				.equals(paymentMethod.getPayment_method().toLowerCase())) {

			// fcreditCard.setIsCheckedMethod(true);
		} else {
			PaymentMethod.getInstance().setCurrentMethod(
					paymentMethod.getPayment_method());
		}
		// fcreditCard.setPaymentMethod(paymentMethod);
		SimiManager.getIntance().replacePopupFragment(fcreditCard);
	}

	private void checkAndSetText(String number, TextView tv_content) {
		// set number for content of creditCard
		if (null != number && number.length() > 4) {
			int lengNumber = number.length();
			number = "***" + number.substring(lengNumber - 4, lengNumber);
			tv_content.setVisibility(RelativeLayout.VISIBLE);
			tv_content.setText(number + "");
		}
	}

	public void onTouchPayment(final RelativeLayout rl_value,
			final int id_chectbox, final ArrayList<ImageView> lisCheckBoxs,
			final int id_content, final ArrayList<TextView> listContents,
			final PaymentMethod paymentMethod) {
		rl_value.setOnClickListener(new OnClickListener() {

			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) {
				setCheckBox(mContext, paymentMethod, lisCheckBoxs, id_chectbox);
				// for (ImageView checkBox : lisCheckBoxs) {
				// if (checkBox.getId() == id_chectbox) {
				// Drawable icon_checked = mContext.getResources()
				// .getDrawable(mIDIconChecked);
				// icon_checked.setColorFilter(Config.getInstance()
				// .getColorMain(), PorterDuff.Mode.SRC_ATOP);
				// checkBox.setImageDrawable(icon_checked);
				// } else {
				// Drawable icon_nomal = mContext.getResources()
				// .getDrawable(mIDIconNormal);
				// icon_nomal.setColorFilter(Config.getInstance()
				// .getColorMain(), PorterDuff.Mode.SRC_ATOP);
				// checkBox.setImageDrawable(icon_nomal);
				// }
				// }

				reviewOrder.setInitViewPayment(paymentMethod.getTitle());

				// save payment method
				PaymentMethod.getInstance().setPlacePaymentMethod(
						paymentMethod.getPayment_method());

				for (TextView content : listContents) {
					if (content.getId() == id_content) {
						content.setVisibility(View.VISIBLE);
						PaymentMethod.getInstance().setmCheckPaymentMethod(
								paymentMethod.getPayment_method());
						setContentPaymentMethod(
								paymentMethod.getPayment_method(), content);
					} else {
						content.setVisibility(View.GONE);
					}
				}

				/* Le Dong edit reload payment 26-10-2015 */
				if (Config.getInstance().isReload_payment_method()) {
					requestSavePaymentMethod(paymentMethod);
				}

				int show_type = paymentMethod.getShow_type();
				if (show_type == 0
						&& paymentMethod.getData(Constants.CONTENT) == null) {

				} else if (show_type == 1) {

					boolean checkMethod = false;
					if (PaymentMethod
							.getInstance()
							.getCurrentMethod()
							.toLowerCase()
							.equals(paymentMethod.getPayment_method()
									.toLowerCase())) {
						checkClickPaymentMethod(paymentMethod);
						checkMethod = true;
					} else {
						PaymentMethod.getInstance().setCurrentMethod(
								paymentMethod.getPayment_method());
						CreditCardFragment fcreditCard = CreditCardFragment
								.newInstance(checkMethod, paymentMethod);
						SimiManager.getIntance().replacePopupFragment(
								fcreditCard);
					}

					// if (PaymentMethod
					// .getInstance()
					// .getCurrentMethod()
					// .toLowerCase()
					// .equals(paymentMethod.getPayment_method()
					// .toLowerCase())) {
					//
					// fcreditCard.setIsCheckedMethod(true);
					// } else {
					// PaymentMethod.getInstance().setCurrentMethod(
					// paymentMethod.getPayment_method());
					// }
					// fcreditCard.setPaymentMethod(paymentMethod);

				}
				ConfigCheckout.checkPaymentMethod = true;
				Utils.collapse(ll_payment);
				if (ConfigCheckout.checkShippingMethod == true) {
					if (ConfigCheckout.checkCondition == false) {
						scrollView.fullScroll(ScrollView.FOCUS_DOWN);
					}
				} else {
					Utils.expand(ll_shipping);
					scrollView.scrollTo(0, 500);
				}
			}
		});
	}

	private void checkClickPaymentMethod(PaymentMethod paymentMethod) {
		if (isSavedCC(paymentMethod.getPayment_method())) {
			CreditcardEntity creditcardEntity = DataLocal
					.getHashMapCreditCart().get(DataLocal.getEmailCreditCart())
					.get(paymentMethod.getPayment_method());

			setCcType(creditcardEntity.getPaymentType(), paymentMethod);
			PaymentMethod.getInstance().setPlace_cc_exp_month(
					creditcardEntity.getPaymentMonth());
			PaymentMethod.getInstance().setPlace_cc_exp_year(
					creditcardEntity.getPaymentYear());
			PaymentMethod.getInstance().setPlace_cc_number(
					creditcardEntity.getPaymentNumber());
			PaymentMethod.getInstance().setPlacecc_id(
					creditcardEntity.getPaymentCvv());

			Utils.expand(ll_shipping);
			scrollView.scrollTo(0, 500);
		} else {
			nextCreditCardFragment(paymentMethod);
		}
	}

	private void setCcType(String paymentType, PaymentMethod paymentMethod) {
		JSONArray cc_types = null;
		try {
			cc_types = new JSONArray(paymentMethod.getData("cc_types"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (cc_types != null) {
			try {
				for (int i = 0; i < cc_types.length(); i++) {
					String cc_name = cc_types.getJSONObject(i).getString(
							"cc_name");
					if (cc_name.equals(paymentType)) {
						PaymentMethod.getInstance().setPlace_cc_type(
								cc_types.getJSONObject(i).getString("cc_code"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean checkShippingMethodChecked() {
		if (listShippingMethod != null & listShippingMethod.size() > 0) {
			for (int i = 0; i < listShippingMethod.size(); i++) {
				if (listShippingMethod.get(i).isS_method_selected()) {
					return true;
				}
			}
		}
		return false;
	}

	protected void requestSavePaymentMethod(PaymentMethod paymentMethod) {
		final PaymentMethodModel mModel = new PaymentMethodModel();
		mDelegate.showDialogLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissDialogLoading();
				if (isSuccess) {
					TotalPrice totalPrice = mModel.getTotalPrice();
					if (null != totalPrice) {
						reviewOrder.setSavePaymentMethod(totalPrice);
					}
				} else {
					SimiManager.getIntance().showNotify(message);
				}
			}
		};

		mModel.setDelegate(delegate);
		mModel.addParam("payment_method", paymentMethod.getPayment_method());
		mModel.request();
	}

	protected boolean checkPreviousPayment(String name,
			ArrayList<PaymentMethod> paymentMethods) {
		for (PaymentMethod paymentMethod : paymentMethods) {
			if (name.equals(paymentMethod.getPayment_method())) {
				return true;
			}
		}
		return false;
	}

	public void setCheckBox(Context mContext, PaymentMethod paymentMethod,
			ArrayList<ImageView> lisCheckBoxs, int id_chectbox) {

		for (ImageView checkBox : lisCheckBoxs) {
			if (checkBox.getId() == id_chectbox) {
				Drawable icon_checked = mContext.getResources().getDrawable(
						mIDIconChecked);
				icon_checked.setColorFilter(
						Config.getInstance().getColorMain(),
						PorterDuff.Mode.SRC_ATOP);
				checkBox.setImageDrawable(icon_checked);
			} else {
				Drawable icon_nomal = mContext.getResources().getDrawable(
						mIDIconNormal);
				icon_nomal.setColorFilter(Config.getInstance().getColorMain(),
						PorterDuff.Mode.SRC_ATOP);
				checkBox.setImageDrawable(icon_nomal);
				reviewOrder.setInitViewPayment(paymentMethod.getTitle());
			}
		}
	}

	public void setDelegate(ReviewOrderDelegate mDelegate) {
		if (mDelegate != null)
			this.mDelegate = mDelegate;
	}

	@Override
	public void goneView() {
		if (null != mView) {
			mView.setVisibility(View.GONE);
		}
	}

	@Override
	public void setListShippingMethod(ArrayList<ShippingMethod> list) {
		this.listShippingMethod = list;
	}
}
