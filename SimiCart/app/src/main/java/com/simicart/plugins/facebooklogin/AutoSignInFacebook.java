package com.simicart.plugins.facebooklogin;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.DataLocal;

public class AutoSignInFacebook {

	public AutoSignInFacebook(String method, String type) {
		if (type.equals("facebook") && method.equals("autoSignIn")) {
			autoSignIn();
		}
	}

	public static String getMD5(String email) {
		String input = "simicart" + email;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);
			// Now we need to zero pad it if you actually want the full 32
			// chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			// throw new RuntimeException(e);
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
						DataLocal.saveData(name, email, getMD5(email));
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
