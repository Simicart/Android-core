package com.simicart.plugins.facebooklogin;

import java.security.MessageDigest;

import android.util.Log;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.DataLocal;

public class AutoSignInFacebook {

	public AutoSignInFacebook(String method, String type) {
		if (type.equals("facebook") && method.equals("autoSignIn")) {
			autoSignIn();
		}
	}

	private String generatePass(String email) {
		try {
			String mes = "simicart" + email;
			byte[] bytesOfMessage = mes.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			String pass = thedigest.toString();
			return pass;
		} catch (Exception e) {
			Log.e("Facebook Login", "Can't generate password");
			return "";
		}
	}

	private void autoSignIn() {
		final String email = DataLocal.getEmail();
		final String name = DataLocal.getUsername();
		final FacebookModel model = new FacebookModel();
		model.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				if (isSuccess) {
					DataLocal.saveTypeSignIn("facebook");
					DataLocal.saveSignInState(true);
					String name = model.getName();
					String cartQty = model.getCartQty();
					if (null != name) {
						DataLocal.saveData(name, email, generatePass(email));
					}
					if (null != cartQty) {
						SimiManager.getIntance().onUpdateCartQty(cartQty);
					}
				}
			}
		});
		model.addParam("email", email);
		model.addParam("name", name);
		model.request();
	}
}
