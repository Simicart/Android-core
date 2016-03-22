package com.simicart.core.checkout.controller;

import java.util.ArrayList;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;

import com.magestore.simicart.R;
import com.simicart.MainActivity;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.checkout.checkoutwebview.fragment.CheckoutWebviewFragment;
import com.simicart.core.checkout.delegate.CartAdapterDelegate;
import com.simicart.core.checkout.delegate.CartDelegate;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.fragment.AddressBookCheckoutFragment;
import com.simicart.core.checkout.model.EditCartItemModel;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

@SuppressLint("ClickableViewAccessibility")
public class CartListenerController implements CartAdapterDelegate {
	protected OnClickListener mCheckoutClicker;
	protected ArrayList<Cart> mCarts;
	protected CartDelegate mBlockDelegate;
	protected String mMessage;
	protected String mWebviewUrl;

	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public CartListenerController() {
		mCheckoutClicker = new OnClickListener() {

			@Override
			public void onClick(View v) {
				onCheckOut();
			}
		};
	}

	protected void onCheckOut() {
		if (!mMessage.contains("NOT CHECKOUT")) {
			if (mWebviewUrl != null && !mWebviewUrl.equals("")) {
				CheckoutWebviewFragment fragment = CheckoutWebviewFragment
						.newInstanse(mWebviewUrl, true);
				SimiManager.getIntance().replaceFragment(fragment);
			} else {
				if (DataLocal.isSignInComplete()) {
					AddressBookCheckoutFragment fragment = AddressBookCheckoutFragment
							.newInstance(0, Constants.KeyAddress.ALL_ADDRESS,
									null, null);
					if (DataLocal.isTablet) {
						SimiManager.getIntance().replacePopupFragment(fragment);
					} else {
						SimiManager.getIntance().replaceFragment(fragment);
					}
				} else {
					mBlockDelegate.showPopupCheckout();
				}
			}
		} else {
			SimiManager.getIntance().showNotify(null, mMessage, "Ok");
		}
	}

	protected void showProductDetail(int position, ArrayList<String> listID) {
		Cart cart = mCarts.get(position);
		if (null != cart) {
			String id = cart.getProduct_id();
			if (Utils.validateString(id)) {
				ProductDetailParentFragment fragment = ProductDetailParentFragment
						.newInstance(id, listID);
				// fragment.setProductID(id);
				// fragment.setListIDProduct(listID);
				SimiManager.getIntance().replaceFragment(fragment);
				SimiManager.getIntance().removeDialog();
			}
		}
	}

	@Override
	public OnTouchListener getOnTouchListener(final int position) {
		OnTouchListener onTouch = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					v.setBackgroundColor(Color.parseColor("#EBEBEB"));
					break;
				}
				case MotionEvent.ACTION_UP: {
					deleteItemCart(position);
				}

				case MotionEvent.ACTION_CANCEL: {
					v.setBackgroundColor(0);
					break;
				}
				default:
					break;
				}
				return true;
			}
		};

		return onTouch;
	}

	public OnClickListener getClickItemCartListener(final int position,
			final ArrayList<String> listID) {
		OnClickListener clicker = new OnClickListener() {

			@Override
			public void onClick(View v) {
				showProductDetail(position, listID);

			}
		};

		return clicker;
	}

	private void deleteItemCart(final int position) {
		new AlertDialog.Builder(SimiManager.getIntance().getCurrentActivity())
				.setMessage(
						Config.getInstance()
								.getText(
										"Are you sure you want to delete this product?"))
				.setPositiveButton(Config.getInstance().getText("Yes"),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								editItemCart(position, "0");
							}
						})
				.setNegativeButton(Config.getInstance().getText("No"),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// do nothing
							}
						}).show();

	}

	protected void editItemCart(final int position, String qty) {
		try {
		Cart cart = mCarts.get(position);
		String cartID = cart.getId();

		JSONObject jsonParam = null;
		try {
			jsonParam = new JSONObject();
			jsonParam.put("cart_item_id", cartID);
			jsonParam.put("product_qty", qty);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null != jsonParam) {
			JSONArray arrParams = new JSONArray();
			arrParams.put(jsonParam);
			editItem(arrParams);
		}
		} catch (Exception e) {
		}
	}

	protected void editItem(JSONArray arrParams) {
		mBlockDelegate.showDialogLoading();

		final EditCartItemModel model = new EditCartItemModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				mBlockDelegate.dismissDialogLoading();
				if (isSuccess) {
					mBlockDelegate.setMessage(message);
					int cartQty = model.getQty();
					SimiManager.getIntance().onUpdateCartQty(
							String.valueOf(cartQty));
					DataLocal.listCarts.clear();
					mBlockDelegate.updateView(((EditCartItemModel) model)
							.getCollection());
					mBlockDelegate
							.onUpdateTotalPrice(((EditCartItemModel) model)
									.getTotalPrice());
				}
				// else {
				// SimiManager.getIntance().showNotify(null, message, "Ok");
				// }
			}
		});

		model.addParam("cart_items", arrParams);
		model.request();
	}

	protected void onEditQty(final int position, String qty) {
		mCarts.get(position).setQty(Integer.parseInt(qty));
	}

	public void setListCart(ArrayList<Cart> carts) {
		mCarts = carts;
	}

	public OnClickListener getCheckoutClicker() {
		return mCheckoutClicker;
	}

	public void setCartDelegate(CartDelegate delegate) {
		mBlockDelegate = delegate;
	}

	private void showDialogNumberPicker(final int position, final int qty,
			int min, int max) {
		final Dialog dialoglayout = new Dialog(MainActivity.context);
		dialoglayout.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialoglayout.setContentView(Rconfig.getInstance().layout(
				"core_cart_dialog_layout"));
		if (!DataLocal.isTablet) {
			dialoglayout.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			dialoglayout.getWindow().setBackgroundDrawable(
					new ColorDrawable(Color.TRANSPARENT));
			dialoglayout.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		} else {
			dialoglayout.getWindow().setBackgroundDrawable(
					new ColorDrawable(Color.TRANSPARENT));
		}
		dialoglayout.show();

		final WheelView wheel = (WheelView) dialoglayout.findViewById(Rconfig
				.getInstance().id("select_quantity"));

		if (!Utils.validateString("" + min)) {
			min = 1;
		}
		if (!Utils.validateString("" + max)) {
			max = 1;
		}
		final NumericWheelAdapter minAdapter = new NumericWheelAdapter(
				MainActivity.context, min, max);
		wheel.setViewAdapter(minAdapter);
		if (qty > 0) {
			wheel.setCurrentItem((qty - 1));
		}

		TextView bt_apply = (TextView) dialoglayout.findViewById(Rconfig
				.getInstance().id("bt_apply"));
		bt_apply.setText(Config.getInstance().getText("Done"));
		bt_apply.setTextColor(Color.parseColor("#0173F2"));
		bt_apply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String value = String.valueOf(minAdapter.getItemText(wheel
						.getCurrentItem()));
				if (Integer.parseInt(value) != qty) {
					editItemCart(position, String.valueOf(minAdapter
							.getItemText(wheel.getCurrentItem())));
				}
				dialoglayout.dismiss();
			}
		});

		TextView bt_cancel = (TextView) dialoglayout.findViewById(Rconfig
				.getInstance().id("bt_cancel"));
		bt_cancel.setText(Config.getInstance().getText("Cancel"));
		bt_cancel.setTextColor(Color.parseColor("#0173F2"));
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialoglayout.dismiss();
			}
		});
	}

	@Override
	public OnClickListener getClickQtyItem(final int position, final int qty,
			final int min, final int max) {
		OnClickListener onclick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialogNumberPicker(position, qty, min, max);
			}
		};
		return onclick;
	}

	public void setWebviewUrl(String url) {
		this.mWebviewUrl = url;
	}

}
