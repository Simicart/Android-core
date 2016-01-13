package com.simicart.core.customer.controller;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.fragment.AddressBookCheckoutFragment;
import com.simicart.core.checkout.model.CartModel;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.SignInDelegate;
import com.simicart.core.customer.fragment.ForgotPasswordFragment;
import com.simicart.core.customer.fragment.RegisterCustomerFragment;
import com.simicart.core.customer.model.SignInModel;
import com.simicart.core.event.controller.EventController;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.core.event.fragment.EventFragment;
import com.simicart.core.home.fragment.HomeFragment;

public class SignInController extends SimiController {

	protected SignInDelegate mDelegate;
	protected OnClickListener mSignInClicker;
	protected OnClickListener mForgotPassClicker;
	protected OnTouchListener mCreateAccClicker;
	protected OnClickListener mOutSideClicker;
	private TextWatcher mPassWatcher;
	private TextWatcher mEmailWatcher;
	protected OnCheckedChangeListener mOnCheckBox;

	protected boolean isCheckout = false;// sign in into checkout

	public boolean getIsCheckout() {
		return isCheckout;
	}

	public SignInDelegate getDelegate() {
		return mDelegate;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onStart() {

		mDelegate.updateView(null);

		mPassWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String email = mDelegate.getEmail();
				String password = mDelegate.getPassword();
				if (email.length() != 0 && password.length() != 0) {
					changeColorSignIn(Config.getInstance().getColorMain());
				} else {
					changeColorSignIn(Color.GRAY);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		};
		mEmailWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String email = mDelegate.getEmail();
				String password = mDelegate.getPassword();
				if (email.length() != 0 && password.length() != 0) {
					changeColorSignIn(Config.getInstance().getColorMain());
				} else {
					changeColorSignIn(Color.GRAY);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		};
		mSignInClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.hideKeyboard(v);
				String email = mDelegate.getEmail();
				String password = mDelegate.getPassword();
				if (email.length() != 0 && password.length() != 0) {
					onSignIn();
				} else {
					mDelegate.getSignIn().setBackgroundColor(Color.GRAY);
				}
			}
		};
		mCreateAccClicker = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Utils.hideKeyboard(v);
					v.setBackgroundColor(0xCCCACACA);
					break;
				case MotionEvent.ACTION_UP:
					v.setBackgroundColor(0xCCFFFFFF);
					onCreateAccount();
					break;
				case MotionEvent.ACTION_CANCEL:
					v.setBackgroundColor(0xCCFFFFFF);
					break;

				default:
					break;
				}
				return true;
			}
		};

		mForgotPassClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.hideKeyboard(v);
				onForgotPasswrod();

			}
		};
		mOutSideClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.hideKeyboard(v);
			}
		};

		mOnCheckBox = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked == false) {
					DataLocal.saveCheckRemember(false);
				} else {
					DataLocal.saveCheckRemember(true);
				}
			}
		};
	}

	@SuppressWarnings("deprecation")
	protected void changeColorSignIn(int color) {
		// GradientDrawable gdDefault = new GradientDrawable();
		// gdDefault.setColor(color);
		// gdDefault.setCornerRadius(3);
		mDelegate.getSignIn().setBackgroundColor(color);
	}

	protected void onSignIn() {

		final String email = mDelegate.getEmail();
		final String password = mDelegate.getPassword();
		onSingIn(email, password);
	}

	protected void onSingIn(final String email, final String password) {
		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mDelegate.getEmail())
				.matches()) {
			mDelegate.showNotify(Config.getInstance().getText(
					"Invalid email address"));
			return;
		}
		if (null == email || email.equals("")) {
			mDelegate.showNotify(Config.getInstance().getText(
					"Email is empty.Please input an email."));
			return;
		}
		if (null == password || password.equals("")) {
			mDelegate.showNotify(Config.getInstance().getText(
					"Password is empty.Please input a password."));
			return;
		}

		mDelegate.showLoading();
		DataLocal.saveData(email, password);

		mModel = new SignInModel();
		ModelDelegate delegate = new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mDelegate.dismissLoading();
				if (isSuccess) {
					DataLocal.isNewSignIn = true;
					DataLocal.saveTypeSignIn(Constants.NORMAL_SIGN_IN);

					String name = ((SignInModel) mModel).getName();
					String cartQty = ((SignInModel) mModel).getCartQty();
					ConfigCheckout.getInstance().setCheckStatusCart(true);
					ConfigCheckout.getInstance().setmQty(cartQty);
					if (null != name) {
						DataLocal.saveData(name, email, password);
						DataLocal.saveEmailPassRemember(email, password);
					}
					showToastSignIn();
					DataLocal.saveSignInState(true);
					if (null != cartQty && !cartQty.equals("0")) {
						SimiManager.getIntance().onUpdateCartQty(cartQty);
					}
					if (!isCheckout && DataLocal.isTablet) {
						SimiManager.getIntance().clearAllChidFragment();
						SimiManager.getIntance().removeDialog();
					} else {
						SimiManager.getIntance().backPreviousFragment();
					}
					// update wishlist_items_qty
					EventController event = new EventController();
					event.dispatchEvent(
							"com.simicart.core.customer.controller.SignInController",
							mModel.getJSON().toString());

					if (isCheckout) {

						mModel = new CartModel();
						mDelegate.showLoading();
						ModelDelegate delegate = new ModelDelegate() {

							@Override
							public void callBack(String message,
									boolean isSuccess) {
								mDelegate.dismissLoading();
								if (isSuccess) {
									int carQty = ((CartModel) mModel).getQty();
									SimiManager.getIntance().onUpdateCartQty(
											String.valueOf(carQty));
									ConfigCheckout.getInstance().setmQty(
											"" + carQty);

									ArrayList<SimiEntity> entity = mModel
											.getCollection().getCollection();
									if (null != entity && entity.size() > 0) {
										ArrayList<Cart> carts = new ArrayList<Cart>();
										DataLocal.listCarts.clear();
										for (int i = 0; i < entity.size(); i++) {
											SimiEntity simiEntity = entity
													.get(i);
											Cart cart = (Cart) simiEntity;
											carts.add(cart);
											DataLocal.listCarts.add(cart);
										}
									}
								}
							}
						};

						mModel.setDelegate(delegate);
						mModel.request();
						SimiFragment fragment = null;
						fragment = AddressBookCheckoutFragment.newInstance();
						// event for wish list
						CacheFragment cache = new CacheFragment();
						cache.setFragment(fragment);
						EventFragment eventFragment = new EventFragment();
						eventFragment.dispatchEvent(
								"com.simicart.event.wishlist.afterSignIn",
								cache);
						fragment = cache.getFragment();

						SimiManager.getIntance().replacePopupFragment(fragment);

					} else {
						SimiFragment fragment = null;
						fragment = HomeFragment.newInstance();

						// event for wish list
						CacheFragment cache = new CacheFragment();
						cache.setFragment(fragment);
						EventFragment eventFragment = new EventFragment();
						eventFragment.dispatchEvent(
								"com.simicart.event.wishlist.afterSignIn",
								cache);
						fragment = cache.getFragment();

						SimiManager.getIntance().replaceFragment(fragment);
					}
				}
			}
		};
		// mModel.setPriority(Priority.IMMEDIATE);
		mModel.setDelegate(delegate);
		mModel.addParam(Constants.USER_EMAIL, email);
		mModel.addParam(Constants.USER_PASSWORD, password);
		mModel.request();
	}

	private void showToastSignIn() {
		LayoutInflater inflater = SimiManager.getIntance().getCurrentActivity()
				.getLayoutInflater();
		View layout_toast = inflater
				.inflate(
						Rconfig.getInstance().layout(
								"core_custom_toast_productlist"),
						(ViewGroup) SimiManager
								.getIntance()
								.getCurrentActivity()
								.findViewById(
										Rconfig.getInstance().id(
												"custom_toast_layout")));
		TextView txt_toast = (TextView) layout_toast.findViewById(Rconfig
				.getInstance().id("txt_custom_toast"));
		Toast toast = new Toast(SimiManager.getIntance().getCurrentContext());
		txt_toast.setText(String.format(
				Config.getInstance().getText("Welcome %s! Start shopping now"),
				DataLocal.getUsername()));
		toast.setView(layout_toast);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 400);
		toast.show();
	}

	protected void onCreateAccount() {
		RegisterCustomerFragment fragment = RegisterCustomerFragment
				.newInstance();
		SimiManager.getIntance().replacePopupFragment(fragment);
	}

	protected void onForgotPasswrod() {
		ForgotPasswordFragment fragment = ForgotPasswordFragment.newInstance();
		SimiManager.getIntance().replacePopupFragment(fragment);

	}

	@Override
	public void onResume() {
		mDelegate.updateView(null);
	}

	public void setDelegate(SignInDelegate delegate) {
		mDelegate = delegate;
	}

	public OnClickListener getSignInClicker() {
		return mSignInClicker;
	}

	public OnClickListener getForgotPassClicker() {
		return mForgotPassClicker;
	}

	public OnTouchListener getCreateAccClicker() {
		return mCreateAccClicker;
	}

	public void setCheckout(boolean isCheckout) {
		this.isCheckout = isCheckout;
	}

	public OnClickListener getOutSideClicker() {
		return mOutSideClicker;
	}

	public TextWatcher getPassWatcher() {
		return mPassWatcher;
	}

	public TextWatcher getEmailWatcher() {
		return mEmailWatcher;
	}

	public OnCheckedChangeListener getOnCheckBox() {
		return mOnCheckBox;
	}
}
