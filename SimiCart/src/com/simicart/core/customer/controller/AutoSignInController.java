package com.simicart.core.customer.controller;

import com.simicart.core.base.controller.SimiController;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.customer.model.SignInModel;
import com.simicart.core.event.controller.EventController;

public class AutoSignInController extends SimiController {

	@Override
	public void onStart() {
		String typeSignIn = DataLocal.getTypeSignIn();
		DataLocal.saveSignInState(false);
		if (typeSignIn.equals(Constants.NORMAL_SIGN_IN)) {
			final String email = DataLocal.getEmail();
			final String password = DataLocal.getPassword();
			final SignInModel model = new SignInModel();
			model.setDelegate(new ModelDelegate() {

				@Override
				public void callBack(String message, boolean isSuccess) {
					if (isSuccess) {
						DataLocal.isNewSignIn = true;
						DataLocal.saveSignInState(true);
						ConfigCheckout.getInstance().setCheckStatusCart(true);
						String name = model.getName();
						String cartQty = model.getCartQty();
						if (null != name) {
							DataLocal.saveData(name, email, password);

						}

						// update wishlist_items_qty
						EventController event = new EventController();
						event.dispatchEvent(
								"com.simicart.core.customer.controller.SignInController",
								model.getJSON().toString());
						if (null != cartQty) {
							DataLocal.qtyCartAuto = cartQty;
							ConfigCheckout.getInstance().setmQty(cartQty);
							SimiManager.getIntance().onUpdateCartQty(cartQty);
						}
					} else {
						DataLocal.saveSignInState(false);
					}
				}
			});
			model.addParam(Constants.USER_EMAIL, email);
			model.addParam(Constants.USER_PASSWORD, password);
			model.request();
		} else {
			// event for face book
			EventController event = new EventController();
			event.dispatchEvent("com.simicart.autoSignIn", typeSignIn);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

}
