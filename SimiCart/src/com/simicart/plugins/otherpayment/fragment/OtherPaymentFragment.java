package com.simicart.plugins.otherpayment.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.simicart.MainActivity;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.otherpayment.entity.OtherPaymentEntity;
import com.simicart.plugins.otherpayment.model.CancelOrderModel;

public class OtherPaymentFragment extends SimiFragment {
	protected OtherPaymentEntity mPayment;
	protected String mInvoiceNumber;
	protected String mUrlAction = "";

	public void setInvoiceNumber(String mInvoiceNumber) {
		this.mInvoiceNumber = mInvoiceNumber;
	}

	public void setUrlAction(String mUrlAction) {
		this.mUrlAction = mUrlAction;
	}

	public void setPayment(OtherPaymentEntity mPayment) {
		this.mPayment = mPayment;
	}

	public static OtherPaymentFragment newInstance() {
		OtherPaymentFragment fragment = new OtherPaymentFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				Rconfig.getInstance().layout("core_webview_layout"), container,
				false);
		final WebView webview = (WebView) rootView.findViewById(Rconfig
				.getInstance().id("webview_Ad"));
		final View mImageView = inflater.inflate(
				Rconfig.getInstance().layout("core_base_loading"), null, false);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);

		mImageView.setLayoutParams(lp);
		// add loading View
		webview.addView(mImageView);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setLayoutAlgorithm(
				WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webview.getSettings().setDomStorageEnabled(true);

		String url_site = mUrlAction;
		webview.loadUrl(url_site);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.e("URL_REDIRECT", url);
				if (url.contains(mPayment.getUrlRedirect())) {
					webview.removeView(mImageView);
				}
				if (url.contains("api/success")) {
					SimiManager.getIntance().showToast(
							mPayment.getMessageSuccess());
					SimiManager.getIntance().backToHomeFragment();
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (url.contains(mPayment.getUrlSuccess())) {
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(mPayment.getMessageSuccess());
				} else if (url.contains(mPayment.getUrlFail())) {
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(mPayment.getMessageFail());
				} else if (url.contains(mPayment.getUrlCancel())) {
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(mPayment.getMessageCancel());
				} else if (url.contains(mPayment.getUrlError())) {
					SimiManager.getIntance().backToHomeFragment();
					showToastMessage(mPayment.getMessageError());
				}
			}
		});
		rootView.setFocusableInTouchMode(true);
		rootView.requestFocus();
		rootView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						showDialog();
						return true;
					}
				}
				return false;
			}
		});

		return rootView;
	}

	public void showToastMessage(String message) {
		Toast toast = Toast.makeText(MainActivity.context, Config.getInstance()
				.getText(message), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(10000);
		toast.show();
	}

	private void showDialog() {
		new AlertDialog.Builder(SimiManager.getIntance().getCurrentActivity())
				.setMessage(
						Config.getInstance()
								.getText(
										"Are you sure that you want to cancel the order?"))
				.setPositiveButton(Config.getInstance().getText("Yes"),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								requestCancelOrder();
								if(!mPayment.getMessageCancel().equals("")){
									showToastMessage(mPayment.getMessageCancel());
								}else{
									showToastMessage("Your order has been canceled!");
								}
								SimiManager.getIntance().backToHomeFragment();
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
	
	private void requestCancelOrder(){
		CancelOrderModel cancelModel = new CancelOrderModel();
		cancelModel.setDelegate(new ModelDelegate() {
			
			@Override
			public void callBack(String message, boolean isSuccess) {
				if(isSuccess){
					Log.e("OrtherPaymentFragment", "Cancel Success");
				}else{
					Log.e("OrtherPaymentFragment", "Cancel Failed");
				}
			}
		});
		cancelModel.addParam("order_id", mInvoiceNumber);
		cancelModel.request();
	}
}
