package com.simicart.core.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.entity.CreditcardEntity;
import com.simicart.core.checkout.entity.ObjectSerializer;
import com.simicart.core.cms.entity.Cms;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.setting.entity.CurrencyEntity;
import com.simicart.core.store.entity.Stores;

public class DataLocal {
	public static Context mContext;
	public static SharedPreferences mSharedPre;
	public static boolean isTablet;
	public static boolean isCloud = false;
	public static boolean enNotification = true;
	public static final String NAME_REFERENCE = "simicart";
	private static String RESTART_COUNT = "RestartCount";
	private static String CATEGORY_ID = "CategoryKey";
	private static String CATEGORY_NAME = "CategoryNameKey";
	private static String EMAIL_KEY = "EmailKey";
	private static String USER_NAME_KEY = "UsernameKey";
	private static String PASS_WORD_KEY = "PasswordKey";
	private static String EMAIL_KEY_REMEMBER = "EmailKeyRemember";
	private static String PASS_WORK_REMEMBER = "PasswordRemember";
	private static String SIGNIN_KEY = "SignInKey";
	private static String NOTIFICATION_KEY = "NotificationKey";
	private static String STORE_KEY = "StoreKey";
	private static String CURRENCY_KEY = "CurrencyKey";
	private static String TYPE_SIGNIN = "TypeSignIn";
	private static String CHECK_REMEMBER_PASSWORD = "check_remember_password";
	public static ArrayList<Cms> listCms;
	public static ArrayList<Stores> listStores;
	public static ArrayList<Cart> listCarts;
	public static ConfigCustomerAddress ConfigCustomerAddress;
	public static ConfigCustomerAddress ConfigCustomerProfile;
	public static boolean isLanguageRTL = false;
	public static boolean isNewSignIn = false;
	public static String qtyCartAuto = "";
	public static ArrayList<CurrencyEntity> listCurrency;
	private static String EMAIL_CARD_CREDIT_CARD = "EmailCardCreditCard";
	private static String SIMI_CREDIT_CARD = "SimiCreditCard";
	public static String deepLinkItemID = "";
	public static String deepLinkItemName = "";
	public static String deepLinkItemHasChild = "";
	public static int deepLinkItemType = 0;

	public static void init(Context context) {
		mContext = context;
		mSharedPre = mContext.getSharedPreferences(NAME_REFERENCE,
				Context.MODE_PRIVATE);
		listCms = new ArrayList<Cms>();
		listStores = new ArrayList<Stores>();
		listCarts = new ArrayList<Cart>();
		listCurrency = new ArrayList<CurrencyEntity>();

	}

	public static String getCatID() {
		String cat_id = "-1";
		if (mSharedPre != null) {
			cat_id = mSharedPre.getString(CATEGORY_ID, "-1");
		}
		return cat_id;
	}

	public static String getCatName() {
		String cat_name = "";
		if (mSharedPre != null) {
			cat_name = mSharedPre.getString(CATEGORY_NAME, "");
		}
		return cat_name;
	}

	public static String getEmail() {
		String email = "";
		if (mSharedPre != null) {
			email = mSharedPre.getString(EMAIL_KEY, "");
		}
		return email;
	}

	public static String getPassword() {
		String password = "";
		if (mSharedPre != null) {
			password = mSharedPre.getString(PASS_WORD_KEY, "");
		}
		return password;
	}

	public static String getUsername() {
		String username = "";
		if (mSharedPre != null) {
			username = mSharedPre.getString(USER_NAME_KEY, "");
		}
		return username;
	}

	public static Boolean getCheckRemember() {
		boolean check_save = false;
		if (mSharedPre != null) {
			check_save = mSharedPre.getBoolean(CHECK_REMEMBER_PASSWORD, false);
		}
		return check_save;
	}

	public static String getStoreID() {
		String id = "";
		if (mSharedPre != null) {
			id = mSharedPre.getString(STORE_KEY, "");
		}
		return id;
	}

	public static String getCurrencyID() {
		String id = null;
		if (listCurrency != null && listCurrency.size() > 0) {
			id = listCurrency.get(0).getValue();
		}
		if (null != mSharedPre) {
			id = mSharedPre.getString(CURRENCY_KEY, id);
		}

		return id;
	}

	public static boolean isSignInComplete() {
		boolean isSignIn = false;
		if (mSharedPre != null) {
			isSignIn = mSharedPre.getBoolean(SIGNIN_KEY, false);
		}
		return isSignIn;
	}

	public static boolean enableNotification(Context context) {
		init(context);
		if (mSharedPre != null) {
			enNotification = mSharedPre.getBoolean(NOTIFICATION_KEY, true);
		}
		return enNotification;
	}

	public static boolean enableNotification() {
		if (mSharedPre != null) {
			enNotification = mSharedPre.getBoolean(NOTIFICATION_KEY, true);
		}
		return enNotification;
	}

	public static void saveSignInState(boolean state) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putBoolean(SIGNIN_KEY, state);
		editor.commit();
		SimiManager.getIntance().onUpdateItemSignIn();
	}

	public static void saveCateID(String cate_id, String cat_name) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(CATEGORY_ID, cate_id);
		editor.putString(CATEGORY_NAME, cat_name);
		editor.commit();
	}

	public static void saveData(String email, String pass) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(EMAIL_KEY, email);
		editor.putString(PASS_WORD_KEY, pass);
		editor.commit();
		saveEmailCreditCart(email);
	}

	public static void saveCheckRemember(boolean check) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putBoolean(CHECK_REMEMBER_PASSWORD, check);
		editor.commit();
	}

	public static void saveData(String name, String email, String pass) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(EMAIL_KEY, email);
		editor.putString(PASS_WORD_KEY, pass);
		editor.putString(USER_NAME_KEY, name);
		editor.commit();
	}

	public static void saveTypeSignIn(String type) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(TYPE_SIGNIN, type);
		editor.commit();
	}

	public static String getTypeSignIn() {
		return mSharedPre.getString(TYPE_SIGNIN, Constants.NORMAL_SIGN_IN);
	}

	public static void saveCurrencyID(String id) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(CURRENCY_KEY, id);
		editor.commit();
	}

	public static void saveStoreID(String id) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(STORE_KEY, id);
		editor.commit();
	}

	public static void saveNotificationSet(boolean enNotification) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putBoolean(NOTIFICATION_KEY, enNotification);
		editor.commit();

	}

	public static void saveEmailPassRemember(String email, String password) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(EMAIL_KEY_REMEMBER, email);
		editor.putString(PASS_WORK_REMEMBER, password);
		editor.commit();
	}

	public static String getEmailRemember() {
		String email = mSharedPre.getString(EMAIL_KEY_REMEMBER, null);
		return email;
	}

	public static String getPasswordRemember() {
		String password = mSharedPre.getString(PASS_WORK_REMEMBER, null);
		return password;
	}

	public static void saveEmailCreditCart(String email) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putString(EMAIL_CARD_CREDIT_CARD, email);
		editor.commit();
	}

	public static String getEmailCreditCart() {
		String email = "";
		if (DataLocal.isSignInComplete()) {
			email = mSharedPre.getString(EMAIL_CARD_CREDIT_CARD, "");
		}
		return email;
	}

	public static void saveHashMapCreditCart(
			HashMap<String, HashMap<String, CreditcardEntity>> hashMap) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		try {
			editor.putString(SIMI_CREDIT_CARD,
					ObjectSerializer.serialize(hashMap));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.commit();
	}

	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, CreditcardEntity>> getHashMapCreditCart() {
		HashMap<String, HashMap<String, CreditcardEntity>> creditCard = null;
		try {
			creditCard = (HashMap<String, HashMap<String, CreditcardEntity>>) ObjectSerializer
					.deserialize(mSharedPre.getString(
							SIMI_CREDIT_CARD,
							ObjectSerializer
									.serialize(new HashMap<String, HashMap<String, CreditcardEntity>>())));
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return creditCard;
	}

	public static void clearEmailPassowrd() {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.remove(EMAIL_KEY);
		editor.remove(PASS_WORD_KEY);
		editor.remove(TYPE_SIGNIN);
		editor.remove(USER_NAME_KEY);
		editor.remove(CATEGORY_ID);
		editor.remove(CATEGORY_NAME);
		editor.commit();
		listCarts.clear();
	}

	// public static void clearDataLocal() {
	// SharedPreferences.Editor editor = mSharedPre.edit();
	// editor.clear();
	// editor.commit();
	// listCarts.clear();
	// }

	public static int getRestartCount() {
		int count = 0;
		if (mSharedPre != null) {
			count = mSharedPre.getInt(RESTART_COUNT, 0);
		}
		return count;
	}

	public static void saveRestartCount(int count) {
		SharedPreferences.Editor editor = mSharedPre.edit();
		editor.putInt(RESTART_COUNT, count);
		editor.commit();
	}

}
