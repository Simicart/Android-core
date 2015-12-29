package com.simicart.core.checkout.controller;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.simicart.MainActivity;
import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.checkout.delegate.PaymentMethodDelegate;
import com.simicart.core.checkout.delegate.ReviewOrderDelegate;
import com.simicart.core.checkout.delegate.SelectedShippingMethodDelegate;
import com.simicart.core.checkout.delegate.ShippingDelegate;
import com.simicart.core.checkout.entity.Condition;
import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.checkout.entity.ShippingMethod;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.checkout.fragment.AddressBookCheckoutFragment;
import com.simicart.core.checkout.fragment.ThankyouFragment;
import com.simicart.core.checkout.model.CouponCodeModel;
import com.simicart.core.checkout.model.PaymentMethodModel;
import com.simicart.core.checkout.model.PlaceOrderModel;
import com.simicart.core.checkout.model.ReviewOrderModel;
import com.simicart.core.checkout.model.ShippingMethodModel;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.entity.MyAddress;
import com.simicart.core.customer.fragment.NewAddressBookFragment;
import com.simicart.core.event.checkout.CheckoutData;
import com.simicart.core.event.checkout.EventCheckout;
import com.simicart.core.notification.NotificationActivity;
import com.simicart.core.notification.entity.NotificationEntity;

@SuppressLint("ClickableViewAccessibility")
public class ReviewOrderController extends SimiController implements
		SelectedShippingMethodDelegate {

	protected ReviewOrderDelegate mDelegate;
	protected PaymentMethodDelegate mPaymentMethodDelegate;
	protected ShippingDelegate mShippingDelegate;

	protected MyAddress mBillingAddress;
	protected MyAddress mShippingAddress;
	protected ArrayList<ShippingMethod> mShippingmethod;
	protected ArrayList<PaymentMethod> mPaymentMethods;
	protected TotalPrice mtotalPrice;

	protected OnClickListener onPlaceNow;
	protected OnClickListener onChooseBillingAddress;
	protected OnClickListener onChooseShippingAddress;
	protected OnItemClickListener onViewProductDetail;
	protected OnEditorActionListener couponCodeListener;
	protected int mAfterControll;

	public void setAfterControll(int controll) {
		mAfterControll = controll;
	}

	protected ArrayList<Condition> mConditions;

	public OnClickListener getOnChoiceBillingAddress() {
		return onChooseBillingAddress;
	}

	public OnClickListener getOnChoiceShippingAddress() {
		return onChooseShippingAddress;
	}

	public OnClickListener getOnPlaceNow() {
		return onPlaceNow;
	}

	public OnItemClickListener getOnViewDetailProduct() {
		return onViewProductDetail;
	}

	public void setDelegate(ReviewOrderDelegate delegate) {
		this.mDelegate = delegate;
	}

	public void setBillingAddress(MyAddress address) {
		mBillingAddress = address;
	}

	public void setShippingAddress(MyAddress address) {
		mShippingAddress = address;
	}

	public OnEditorActionListener getCouponCodeListener() {
		return couponCodeListener;
	}

	@Override
	public void onStart() {
		// mDelegate.setShipingAddress(mShippingAddress);
		// mDelegate.setBillingAddress(mBillingAddress);
		onRequestData();

		setOnPlaceNow();
		setOnChoiceBillingAddress();
		setOnChooseShippingAddress();
		setCouponCodeListener();
		setViewProductDetail();
	}

	protected void onRequestData() {
		mModel = new ReviewOrderModel();
		mModel.setDelegate(new ModelDelegate() {
			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					updateView();
				} else {
					mPaymentMethodDelegate.goneView();
					mShippingDelegate.goneView();
					mDelegate.goneView();
					mDelegate.scrollCenter();
				}
			}
		});

		String password = DataLocal.getPassword();
		if (mAfterControll == NewAddressBookFragment.NEW_CUSTOMER) {
			mModel.addParam(Constants.CUSTOMER_PASSWORD, password);
			mModel.addParam(Constants.CONFIRM_PASSWORD, password);
		} else {
			mModel.addParam(Constants.CUSTOMER_PASSWORD, "");
			mModel.addParam(Constants.CONFIRM_PASSWORD, "");
		}

		if (null != mBillingAddress) {
			try {
				JSONObject json_billingAddres = new JSONObject(
						Utils.endCodeJson(mBillingAddress.toParamsRequest()));

				mModel.addParam(Constants.BILLING_ADDRESS, json_billingAddres);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (null != mShippingAddress) {
			try {
				JSONObject json_shippingAddress = new JSONObject(
						Utils.endCodeJson(mShippingAddress.toParamsRequest()));

				mModel.addParam(Constants.SHIPPING_ADDRESS,
						json_shippingAddress);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		mModel.request();
		mDelegate.showLoading();
	}

	private void setViewProductDetail() {
		onViewProductDetail = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ProductOrderDetailFragment fragment = new
				// ProductOrderDetailFragment();
				// fragment.setProduct(DataLocal.listCarts.get(position));
				// SimiManager.getIntance().replacePopupFragment(fragment);
			}

		};
	}

	protected void setOnChooseShippingAddress() {
		onChooseShippingAddress = new OnClickListener() {

			@Override
			public void onClick(View v) {
				AddressBookCheckoutFragment fragment = AddressBookCheckoutFragment
						.newInstance();
				fragment.setAddressFor(AddressBookCheckoutFragment.SHIPPING_ADDRESS);
				fragment.setBillingAddress(mBillingAddress);
				fragment.setShippingAddress(mShippingAddress);
				fragment.setAfterController(mAfterControll);
				SimiManager.getIntance().replacePopupFragment(fragment);
			}
		};

	}

	protected void setOnChoiceBillingAddress() {
		onChooseBillingAddress = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("ReviewOrderController onChooseBillingAddress : ",
						"DataLocal size : " + DataLocal.listCarts.size());
				AddressBookCheckoutFragment fragment = AddressBookCheckoutFragment
						.newInstance();
				fragment.setAddressFor(AddressBookCheckoutFragment.BILLING_ADDRESS);
				fragment.setBillingAddress(mBillingAddress);
				fragment.setShippingAddress(mShippingAddress);
				fragment.setAfterController(mAfterControll);
				SimiManager.getIntance().replacePopupFragment(fragment);
			}
		};
	}

	protected void setCouponCodeListener() {
		couponCodeListener = new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					String code = v.getText().toString().trim();
					setCouponCode(code);
					// v.setText("");
					// v.setHint(Config.getInstance().getText(
					// "Enter a coupon code."));
					v.setFocusable(false);
					v.setFocusableInTouchMode(true);
					Utils.hideKeyboard(v);
				}
				return false;
			}
		};
	}

	protected void setCouponCode(String code) {

		final CouponCodeModel model = new CouponCodeModel();
		mDelegate.showLoading();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					mPaymentMethods = model.getListPayment();
					mPaymentMethodDelegate.setPaymentMethods(mPaymentMethods);
					mShippingmethod = model.getShippingMethods();
					// mDelegate.setShipingMethods(model.getShippingMethods());
					mShippingDelegate.setShippingMethods(mShippingmethod);
					mtotalPrice = model.getTotalPrice();
					mDelegate.setTotalPrice(mtotalPrice);
				}
				SimiManager.getIntance().showToast(message);

			}
		});

		model.addParam("coupon_code", code);
		model.request();
	}

	protected void onSelectedShippingMethod() {
		// try {
		// ShippingMethodFragment fragment = ShippingMethodFragment
		// .newInstance();
		// fragment.setListShipping(mShippingmethod);
		// fragment.setSelectedShippingMethodDelegate(this);
		// SimiManager.getIntance().replacePopupFragment(fragment);
		// } catch (Exception e) {
		// Log.e(getClass().getName(),
		// "BUG EXCEPSION onSelectedShippingMethod");
		// }
	}

	protected void setOnPlaceNow() {
		onPlaceNow = new OnClickListener() {

			@Override
			public void onClick(View v) {
				SimiManager.getIntance().hideKeyboard();
				if (isCompleteRequired()) {
					PaymentMethod paymentMethod = getPaymentMethod(PaymentMethod
							.getInstance().getPlacePaymentMethod());
					if (paymentMethod != null) {
						requestPlaceOrder(paymentMethod);
					}
				}
			}
		};
	}

	protected void requestPlaceOrder(final PaymentMethod paymentmethod) {
		// event for Klarna Payment
		CheckoutData data_klarna = new CheckoutData();
		data_klarna.setPaymentMethod(paymentmethod);
		EventCheckout event3 = new EventCheckout();
		event3.dispatchEvent("com.simicart.before.placeorder", data_klarna);
		if (data_klarna.isContructed()) {
			return;
		}
		// end event for Klarna Payment

		mDelegate.showLoading();
		mModel = new PlaceOrderModel();
		mModel.setDelegate(new ModelDelegate() {
			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					ConfigCheckout.getInstance().setCheckStatusCart(true);
					ConfigCheckout.getInstance().setmQty("" + 0);
					PaymentMethod.getInstance().setPlacePaymentMethod("");

					PaymentMethod.getInstance().setPlacePaymentMethod("");
					PaymentMethod.getInstance().setPlace_cc_number("");
					PaymentMethod.getInstance().setPlacecc_id("");
					ConfigCheckout.checkPaymentMethod = false;

					if (mAfterControll != NewAddressBookFragment.NEW_AS_GUEST) {
						String email = DataLocal.getEmail();
						String password = DataLocal.getPassword();
						DataLocal.saveEmailPassRemember(email, password);
						DataLocal.saveSignInState(true);
						SimiManager.getIntance().onUpdateItemSignIn();
					}
					ThankyouFragment fragment = ThankyouFragment.newInstance();
					SimiManager.getIntance().onUpdateCartQty(null);
					int showtype = paymentmethod.getShow_type();
					switch (showtype) {
					case 1:
						if (((PlaceOrderModel) mModel).getEnable().equals("1")) {
							recieveNotification(((PlaceOrderModel) mModel)
									.getNotificationEntity());
						} else {
							fragment.setMessage(message);
							fragment.setJsonObject(mModel.getCollection()
									.getJSON());
							if (DataLocal.isTablet) {
								SimiManager.getIntance().replacePopupFragment(
										fragment);
							} else {
								SimiManager.getIntance().replaceFragment(
										fragment);
							}
							// SimiManager.getIntance().showToast(message);
						}
						SimiManager.getIntance().backToHomeFragment();
						break;
					case 2:
						// event call paypal server.
						CheckoutData _CheckoutData2 = new CheckoutData();
						_CheckoutData2
								.setInvoice_number(((PlaceOrderModel) mModel)
										.getInvoiceNumber());
						_CheckoutData2.setTotal_price(mtotalPrice);
						_CheckoutData2.setPaymentMethod(paymentmethod);
						EventCheckout event2 = new EventCheckout();
						event2.dispatchEvent(
								"com.simicart.paymentmethod.placeorder",
								_CheckoutData2);
						// end event
						break;
					case 3:
						// update for payment showtype = 3
						CheckoutData _CheckoutData3 = new CheckoutData();
						_CheckoutData3.setPaymentMethod(paymentmethod);
						_CheckoutData3
								.setInvoice_number(((PlaceOrderModel) mModel)
										.getInvoiceNumber());
						_CheckoutData3
								.setJsonPlaceOrder(((PlaceOrderModel) mModel)
										.getJs_placeOrder());
						EventCheckout event3 = new EventCheckout();
						event3.dispatchEvent(
								"com.simicart.after.placeorder.webview",
								_CheckoutData3);
						// end event
						break;
					default:
						if (((PlaceOrderModel) mModel).getEnable().equals("1")) {
							recieveNotification(((PlaceOrderModel) mModel)
									.getNotificationEntity());
						} else {
							// SimiManager.getIntance().showToast(message);
							fragment.setMessage(message);
							fragment.setJsonObject(mModel.getCollection()
									.getJSON());
							if (DataLocal.isTablet) {
								SimiManager.getIntance().replacePopupFragment(
										fragment);
							} else {
								SimiManager.getIntance().replaceFragment(
										fragment);
							}
						}
						break;
					}

					// update placeordersuccess
					CheckoutData _CheckoutData = new CheckoutData();
					_CheckoutData.setInvoice_number(((PlaceOrderModel) mModel)
							.getInvoiceNumber());
					_CheckoutData.setTotal_price(mtotalPrice);
					_CheckoutData.setPaymentMethod(paymentmethod);
					_CheckoutData.setShowtype(showtype);
					EventCheckout event = new EventCheckout();
					event.dispatchEvent(
							"com.simicart.paymentmethod.placeordersuccess",
							_CheckoutData);
					// end event
				} else {
					SimiManager.getIntance().showToast(message);
				}
			}
		});
		mModel.addParam(Constants.PAYMENT_METHOD, ""
				+ PaymentMethod.getInstance().getPlacePaymentMethod());
		if (paymentmethod.getShow_type() == 1) {
			mModel.addParam(Constants.CARD_TYPE, ""
					+ PaymentMethod.getInstance().getPlace_cc_type());
			mModel.addParam(Constants.CARD_NUMBER, ""
					+ PaymentMethod.getInstance().getPlace_cc_number());
			mModel.addParam(Constants.EXPRIRED_MONTH, ""
					+ PaymentMethod.getInstance().getPlace_cc_exp_month());
			mModel.addParam(Constants.EXPRIRED_YEAR, ""
					+ PaymentMethod.getInstance().getPlace_cc_exp_year());
			if (paymentmethod.getData(Constants.USECCV).equals("1")) {
				mModel.addParam(Constants.CC_ID, ""
						+ PaymentMethod.getInstance().getPlacecc_id());
			}
		}
		if (Config.getInstance().getEnable_agreements() != 0) {
			mModel.addParam(Constants.CONDITION, ""
					+ Config.getInstance().getEnable_agreements());
		}
		mModel.request();

	}

	protected boolean isCompleteRequired() {
		if (PaymentMethod.getInstance().getPlacePaymentMethod().equals("")) {
			Utils.expand(mDelegate.getLayoutPayment());
			mDelegate.scrollCenter();
			SimiManager.getIntance().showNotify(null,
					"Please specify payment method", "Ok");
			return false;
		}

		if (mShippingmethod != null && mShippingmethod.size() > 0) {
			if (!isCheckShippingMethod()) {
				Utils.expand(mDelegate.getLayoutShipping());
				mDelegate.scrollCenter();
				SimiManager.getIntance().showNotify(null,
						"Please specify shipping method", "Ok");
				return false;
			}
		}

		// check condition
		if (Config.getInstance().getEnable_agreements() == 0
				&& mConditions != null && mConditions.size() != 0) {
			mDelegate.scrollDown();
			SimiManager
					.getIntance()
					.showNotify(
							null,
							"Please agree to all the terms and conditions before placing the order.",
							"Ok");
			return false;
		}
		return true;
	}

	private void recieveNotification(NotificationEntity notificationData) {
		SimiManager.getIntance().backToHomeFragment();
		Intent i = new Intent(MainActivity.context, NotificationActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("NOTIFICATION_DATA", notificationData);
		MainActivity.context.startActivity(i);
	}

	protected void updateView() {
		ReviewOrderModel model = (ReviewOrderModel) mModel;
		mConditions = (model.getConditions());
		mDelegate.setConditions(model.getConditions());
		mtotalPrice = model.getTotalPrice();

		mDelegate.setTotalPrice(mtotalPrice);
		mPaymentMethods = model.getPaymentMethods();
		if (mPaymentMethods != null && mPaymentMethods.size() > 0) {
			mPaymentMethodDelegate.setPaymentMethods(mPaymentMethods);
			mShippingDelegate.setPaymentMethod(mPaymentMethods);
		}
		mShippingmethod = model.getShippingMethods();
		if (mShippingmethod != null && mShippingmethod.size() > 0) {
			mShippingDelegate.setShippingMethods(mShippingmethod);
			mPaymentMethodDelegate.setListShippingMethod(mShippingmethod);
		}
		mDelegate.updateView(mModel.getCollection());

		if (mShippingmethod == null || mShippingmethod.size() <= 0) {
			mDelegate.setBillingAddress(mBillingAddress);
		} else {
			mDelegate.setShipingAddress(mShippingAddress);
			mDelegate.setBillingAddress(mBillingAddress);
		}

		// if (!checkAutoSelectShipping()) {
		// checkAutoSelectPaymentMehtod();
		// }

	}

	@Override
	public void onResume() {
		mDelegate.setShipingAddress(mShippingAddress);
		mDelegate.setBillingAddress(mBillingAddress);
		mDelegate.setConditions(mConditions);
		mDelegate.setTotalPrice(mtotalPrice);
		mPaymentMethodDelegate.setPaymentMethods(mPaymentMethods);
		mPaymentMethodDelegate.setListShippingMethod(mShippingmethod);
		mShippingDelegate.setPaymentMethod(mPaymentMethods);
		mShippingDelegate.setShippingMethods(mShippingmethod);
		// mDelegate.setShipingMethods(mShippingmethod);
		mDelegate.updateView(mModel.getCollection());
	}

	public void onDestroy() {
		ShippingMethod.refreshShipping();
		if (null != mShippingmethod && mShippingmethod.size() > 0) {
			for (ShippingMethod method : mShippingmethod) {
				method.setS_method_selected(false);
			}
		}
	}

	public void setDelegate(PaymentMethodDelegate paymentMethodDelegate) {
		this.mPaymentMethodDelegate = paymentMethodDelegate;
	}

	public void setShippingDelegate(ShippingDelegate shippingDelegate) {
		this.mShippingDelegate = shippingDelegate;
	}

	/**
	 * SelectedShippingMethodDelegate
	 */
	@Override
	public void updateTotalPrice(TotalPrice totalPrice) {
		mtotalPrice = totalPrice;
		mDelegate.setTotalPrice(totalPrice);

	}

	@Override
	public void updatePaymentMethod(ArrayList<PaymentMethod> paymentMethod) {
		mPaymentMethods = paymentMethod;
		mPaymentMethodDelegate.setPaymentMethods(mPaymentMethods);
	}

	public void updateListShipping(ArrayList<ShippingMethod> listShippingMethos) {
		mShippingDelegate.setShippingMethods(listShippingMethos);
	}

	@Override
	public void updateShippingMethod(ShippingMethod shippingMethod) {
		ArrayList<ShippingMethod> listShipping = ((ReviewOrderModel) mModel)
				.getShippingMethods();
		for (int i = 0; i < listShipping.size(); i++) {
			ShippingMethod shippingMethod2 = listShipping.get(i);
			String id = shippingMethod2.getS_method_id();
			if (id.equals(shippingMethod.getS_method_id())) {
				listShipping.set(i, shippingMethod);
			}
		}
		((ReviewOrderModel) mModel).setShippingMethods(listShipping);
		// mDelegate.setShipingMethods(listShipping);
		mShippingDelegate.setShippingMethods(listShipping);

	}

	protected PaymentMethod getPaymentMethod(String payment_name) {
		for (PaymentMethod paymentMethod : mPaymentMethods) {
			if (paymentMethod.getPayment_method().equals(payment_name)) {
				return paymentMethod;
			}
		}
		return null;
	}

	protected boolean isCheckShippingMethod() {
		for (ShippingMethod shippingMethod : mShippingmethod) {
			if (shippingMethod.isS_method_selected()) {
				return true;
			}
		}
		return false;
	}

	protected boolean checkAutoSelectShipping() {
		if (null != mShippingmethod && mShippingmethod.size() > 0) {
			ShippingMethod currentMethod = null;
			for (ShippingMethod shippingMethod : mShippingmethod) {
				if (shippingMethod.isS_method_selected()) {
					currentMethod = shippingMethod;
				}
			}
			if (currentMethod == null) {
				autoSelectShipping();
				return true;
			}
		}
		return false;
	}

	protected void autoSelectShipping() {
		if (null != mShippingmethod) {
			mDelegate.showLoading();
			final ShippingMethod firstShippingMethod = mShippingmethod.get(0);
			String code = firstShippingMethod.getS_method_code();
			String id = firstShippingMethod.getS_method_id();
			final ShippingMethodModel mModel_shipping = new ShippingMethodModel();
			mModel_shipping.setDelegate(new ModelDelegate() {

				@Override
				public void callBack(String message, boolean isSuccess) {
					mDelegate.dismissLoading();
					if (isSuccess) {
						ArrayList<PaymentMethod> list_payments = ((ShippingMethodModel) mModel_shipping)
								.getListPayment();
						if (null != list_payments && list_payments.size() > 0) {
							mPaymentMethods = list_payments;
							mPaymentMethodDelegate
									.setPaymentMethods(mPaymentMethods);
						}
						TotalPrice totalPrice = ((ShippingMethodModel) mModel_shipping)
								.getTotalPrice();
						if (null != totalPrice) {
							mtotalPrice = totalPrice;
							mDelegate.setTotalPrice(totalPrice);
						}
						firstShippingMethod.setS_method_selected(true);
						mDelegate.setShipingMethods(((ReviewOrderModel) mModel)
								.getShippingMethods());
						checkAutoSelectPaymentMehtod();
					}
				}
			});
			mModel_shipping.addParam("s_method_id", id);
			mModel_shipping.addParam("s_method_code", code);
			mModel_shipping.request();
		}
	}

	protected void checkAutoSelectPaymentMehtod() {
		if (null != mPaymentMethods && mPaymentMethods.size() > 0) {
			for (int i = 0; i < mPaymentMethods.size(); i++) {
				PaymentMethod paymentMethod = mPaymentMethods.get(i);
				int show_type = paymentMethod.getShow_type();
				if (show_type == 0
						&& !paymentMethod.getData(Constants.CONTENT).equals("")) {
					if (Config.getInstance().isReload_payment_method()) {
						PaymentMethod.getInstance().setPlacePaymentMethod(
								paymentMethod.getPayment_method());
						autoSelectPaymentMethod(paymentMethod);
						return;
					}
				}
			}
		}
	}

	protected void autoSelectPaymentMethod(final PaymentMethod paymentMethod) {
		final PaymentMethodModel model = new PaymentMethodModel();
		mDelegate.showDialogLoading();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissDialogLoading();
				if (isSuccess) {
					PaymentMethod.getInstance().setPlacePaymentMethod(
							paymentMethod.getPayment_method());

					TotalPrice totalPrice = model.getTotalPrice();
					if (null != totalPrice) {
						setSavePaymentMethod(totalPrice);
						ReviewOrderModel model = (ReviewOrderModel) mModel;
						mPaymentMethodDelegate.setPaymentMethods(model
								.getPaymentMethods());
					}
				} else {
					SimiManager.getIntance().showNotify(message);
				}
			}
		};
		model.setDelegate(delegate);
		model.addParam("payment_method", paymentMethod.getPayment_method());
		Log.e("ReviewOrderController ", "AutoSelectPaymentMethod "
				+ paymentMethod.getPayment_method());
		model.request();
	}

	public void setSavePaymentMethod(TotalPrice totalPrice) {
		mtotalPrice = totalPrice;
		mDelegate.setTotalPrice(totalPrice);

	}

	public void setInitViewShipping(String shippingName) {
		mDelegate.setInitViewShippingMethod(shippingName);
	}

	public void setInitViewPayment(String paymentName) {
		mDelegate.setInitViewPaymentMethod(paymentName);
	}

	public void updateShipping(SimiCollection collection) {
		mDelegate.updateView(collection);
	}
}
