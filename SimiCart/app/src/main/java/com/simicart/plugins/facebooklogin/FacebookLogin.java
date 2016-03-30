package com.simicart.plugins.facebooklogin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.simicart.MainActivity;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.checkout.fragment.AddressBookCheckoutFragment;
import com.simicart.core.checkout.model.CartModel;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.SignInDelegate;
import com.simicart.core.customer.fragment.SignInFragment;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.fragment.CacheFragment;
import com.simicart.core.event.fragment.EventFragment;
import com.simicart.core.home.fragment.HomeFragment;
import com.simicart.core.material.ButtonRectangle;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class FacebookLogin {

    private static Context mContext;
    private static View mView;
    private static Activity mActivity;
    private static SignInFragment mSignInFragment;
    CacheFragment mCacheFragment;
    SimiModel mModel;
    private static CallbackManager callbackManager;

    public FacebookLogin(String method, CacheFragment caheFragment) {
        this.mCacheFragment = caheFragment;
        if (method.equals("createFragment")) {
            mSignInFragment = (SignInFragment) caheFragment.getFragment();
        }
        if (method.equals("onActivityResult")) {
            onActivityResult();
        }
    }

    public FacebookLogin(String method, CacheBlock cacheBlock) {
        Log.e("FACEBOOK LOGIN", "FACEBOOOK LOGIN" + method);
        mView = cacheBlock.getView();
        mContext = MainActivity.context;
        mActivity = (Activity) mContext;
        if (method.equals("addButton")) {
            addButtonFaceBookLogin();
        }

    }

    public FacebookLogin(String method, MainActivity main) {
        if (method.equals("onActivityResult")) {
            onActivityResult(main);
        }
    }

    private void onActivityResult(MainActivity main) {

    }

    private void onActivityResult() {
        if (callbackManager != null) {
            callbackManager.onActivityResult(mSignInFragment.getRequestCode(),
                    mSignInFragment.getResultCode(), mSignInFragment.getData());
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

    public void addButtonFaceBookLogin() {
        FacebookSdk.sdkInitialize(mContext);
        callbackManager = CallbackManager.Factory.create();

        ButtonRectangle bt_signin = (ButtonRectangle) mView
                .findViewById(Rconfig.getInstance().id("bt_signIn"));
        RelativeLayout otherSignIn = (RelativeLayout) mView
                .findViewById(Rconfig.getInstance().id("rel_other_signin"));

        View v = mActivity.getLayoutInflater().inflate(
                Rconfig.getInstance().layout(
                        "plugin_facebooklogin_layout_login"), null);
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int dp = Utils.getValueDp(5);
        params.setMargins(dp, dp, dp, dp);
        params.addRule(RelativeLayout.BELOW, bt_signin.getId());
        v.setLayoutParams(params);
        otherSignIn.addView(v);

        // check session
        LoginManager.getInstance().logOut();

        final LoginButton login_button = (LoginButton) v.findViewById(Rconfig
                .getInstance().id("authButton"));
        login_button.setFragment(mSignInFragment);
        login_button.setReadPermissions(Arrays
                .asList("email,user_photos,user_birthday"));
		login_button.setText(Config.getInstance().getText(
				"Log in with Facebook"));
//		login_button.setLogoutText(Config.getInstance().getText("Log out"));
        login_button.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult result) {
                        Log.e("LoginFacebook", "Success");
                        GraphRequest request = GraphRequest.newMeRequest(
                                result.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {
                                        String name = "";
                                        String email = "";
                                        String id = "";
                                        try {
                                            if (object.has("id")) {
                                                id = object.getString("id");
                                            }
                                            if (object.has("name")) {
                                                name = object.getString("name");
                                            }
                                            if (object.has("email")) {
                                                email = object
                                                        .getString("email");
                                            } else {
                                                email = id + "@facebook.com";
                                            }
                                            if (email.length() > 0
                                                    && mSignInFragment != null) {
                                                requestFaceBookSignIn(
                                                        email,
                                                        name,
                                                        mSignInFragment
                                                                .getController()
                                                                .getIsCheckout());
                                            }
                                        } catch (Exception e) {
                                            Log.e("FacebookLogin:",
                                                    "Get Information");
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields",
                                "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("Login Facebook Error", error.getMessage());
                    }

                    @Override
                    public void onCancel() {
                        Log.e("Login Facebook Cancel:", "Cancel");
                    }
                });

    }

    private void requestFaceBookSignIn(final String email, final String name,
                                       final boolean checkOut) {

        final SignInDelegate mDelegate = mSignInFragment.getController()
                .getDelegate();

        mDelegate.showLoading();

        mModel = new FacebookModel();
        ModelDelegate delegate = new ModelDelegate() {

            @Override
            public void callBack(String message, boolean isSuccess) {
                mDelegate.dismissLoading();
                if (isSuccess) {
                    DataLocal.saveTypeSignIn("facebook");
                    DataLocal.saveData(name, email, getMD5(email));
                    DataLocal.saveSignInState(true);

                    String cartQty = ((FacebookModel) mModel).getCartQty();
                    if (null != cartQty && !cartQty.equals("0")) {
                        SimiManager.getIntance().onUpdateCartQty(cartQty);
                    }
                    SimiManager.getIntance().backPreviousFragment();

                    if (checkOut) {

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
                                    ArrayList<SimiEntity> entity = mModel
                                            .getCollection().getCollection();
                                    if (null != entity && entity.size() > 0) {
                                        ArrayList<Cart> carts = new ArrayList<Cart>();
                                        for (SimiEntity simiEntity : entity) {
                                            Cart cart = (Cart) simiEntity;
                                            carts.add(cart);
                                        }
                                        DataLocal.listCarts = carts;
                                    }

                                } else {
                                    SimiManager.getIntance()
                                            .showNotify(message);
                                }
                            }
                        };

                        mModel.setDelegate(delegate);
                        mModel.request();

                        AddressBookCheckoutFragment fragment = AddressBookCheckoutFragment
                                .newInstance();
                        SimiManager.getIntance().replacePopupFragment(fragment);

                    } else {
                        SimiFragment fragment = HomeFragment.newInstance();
                        // event for wish list
                        CacheFragment cache = new CacheFragment();
                        cache.setFragment(fragment);
                        EventFragment eventFragment = new EventFragment();
                        eventFragment.dispatchEvent(
                                "com.simicart.event.wishlist.afterSignIn",
                                cache);
                        fragment = cache.getFragment();

                        SimiManager.getIntance().removeDialog();
                        SimiManager.getIntance().replaceFragment(fragment);
                    }
                } else {
                    mDelegate.showNotify(message);
                }

            }
        };

        mModel.setDelegate(delegate);
        mModel.addParam("email", "" + email + "");
        mModel.addParam("name", "" + name + "");
        mModel.request();

    }
}
