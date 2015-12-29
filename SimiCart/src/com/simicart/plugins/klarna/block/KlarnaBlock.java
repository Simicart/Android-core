package com.simicart.plugins.klarna.block;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.fragment.CartFragment;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.klarna.common.KlarnaXMLParser;
import com.simicart.plugins.klarna.delegate.KlarnaDelegate;
import com.simicart.plugins.klarna.fragment.KlarnaFragment;
import com.simicart.plugins.klarna.model.KlarnaPushModel;

public class KlarnaBlock extends SimiBlock implements KlarnaDelegate {

	protected WebView webView;

	public KlarnaBlock(View view, Context context) {
		super(view, context);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void initView() {
		webView = (WebView) mView.findViewById(Rconfig.getInstance().id(
				"webview_Ad"));
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		showLoading();
	}

	@Override
	public void onLoadWebView(JSONArray json) {
		// webView.removeView(mImageView);

		if (null != json) {
			String data = json.toString();
			String url_ex = KlarnaFragment.URL_CHECKOUT_KLARNA + data;
			String url = Config.getInstance().getBaseUrl() + url_ex;
			webView.loadUrl(url);
			webView.setWebViewClient(new WebViewClient() {

				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					super.onPageStarted(view, url, favicon);

				}

				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					if (url.contains("simiklarna/api/checkout/")) {
						dismissLoading();
					}

					if (url.contains("/simiklarna/api/success")) {
						webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
					}

					if (url.contains("checkout/cart")) {

					}

				}

			});

		}

	}

	protected void onFailPayment() {
		SimiManager.getIntance().showToast("");
		CartFragment fragment = new CartFragment();
		SimiManager.getIntance().replaceFragment(fragment);
	}

	class MyJavaScriptInterface {
		@JavascriptInterface
		public void processHTML(String html) {

			try {
				String result = "";
				InputStream is = new ByteArrayInputStream(
						html.getBytes("UTF-8"));
				InputSource inStream = new InputSource(is);

				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();
				KlarnaXMLParser klarnaParser = new KlarnaXMLParser();
				xr.setContentHandler(klarnaParser);
				xr.parse(inStream);

				result = klarnaParser.getResult();
				JSONObject json = new JSONObject(result);

				Log.e("KlarnaBlock ",
						"MyJavaScriptInterface JSON " + json.toString());

				JSONObject json_data = json.getJSONObject("data");

				if (json_data.has("klarna_order")) {
					String order = json_data.getString("klarna_order");
					pushKlarna(order);
				}
			} catch (Exception e) {
			}

		}
	}

	public void pushKlarna(String klarna_order) {
		((Activity) mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showLoading();
			}
		});

		KlarnaPushModel pushModel = new KlarnaPushModel();
		pushModel.setDelegate(new ModelDelegate() {

			@Override
			public void callBack(String message, boolean isSuccess) {
				((Activity) mContext).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dismissLoading();
					}
				});
				SimiManager.getIntance().showToast(message);
				if (isSuccess) {
					SimiManager.getIntance().backToHomeFragment();
				} else {
					SimiManager.getIntance().backPreviousFragment();
				}

			}
		});

		pushModel.addParam("klarna_order", klarna_order);
		pushModel.request();

	}
}
