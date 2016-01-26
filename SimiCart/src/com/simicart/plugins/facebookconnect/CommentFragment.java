package com.simicart.plugins.facebookconnect;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

@SuppressLint("NewApi")
public class CommentFragment extends DialogFragment {

	private String urlProduct = "";
	private Button btn_login_otheraccount;
	private boolean checkLogin;
	private ProgressBar progressBar;

	// public static final String APP_ID = "724662554276476";
	String APP_ID = "";

	@Override
	public void onStart() {
		super.onStart();
		Window window = getDialog().getWindow();
		WindowManager.LayoutParams windowParams = window.getAttributes();
		windowParams.dimAmount = 0.50f;
		windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		window.setAttributes(windowParams);
		window.setBackgroundDrawableResource(android.R.color.transparent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_DeviceDefault_Light_NoActionBar);
	}

	public CommentFragment(String url) {
		this.urlProduct = url;
	}

	private WebView webview;
	private String html = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		APP_ID = getActivity().getResources().getString(
				Rconfig.getInstance().string("facebook_app_id"));
		View rootView = inflater
				.inflate(
						Rconfig.getInstance().layout(
								"plugin_fbconnect_layout_comment"), null);

		// View rootView = inflater.inflate(
		// Rconfig.layout("plugin_fbconnect_layout_comment"), container, false);
		progressBar = (ProgressBar) rootView.findViewById(Rconfig.getInstance()
				.id("loading_screen"));
		webview = (WebView) rootView.findViewById(Rconfig.getInstance().id(
				"webview_frag"));
		webview.getSettings().setJavaScriptEnabled(true);
		html = getHTMLDetail(urlProduct, APP_ID);
		webview.loadDataWithBaseURL("http://developers.facebook.com", html,
				"text/html", null, null);
		webview.setWebViewClient(new WebViewClientActivity());
		// ImageView btn_close = (ImageView) rootView
		// .findViewById(R.id.img_frag_comment);
		ImageView btn_close = (ImageView) rootView.findViewById(Rconfig
				.getInstance().id("img_frag_comment"));
		btn_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		btn_login_otheraccount = (Button) rootView.findViewById(Rconfig
				.getInstance().id("btn_comment_otheraccount"));
		btn_login_otheraccount.setText(Config.getInstance().getText(
				"Comment with another account"));
		checkLogin = checkLoginWebviewAndroid();
		if (checkLogin == true) {
			btn_login_otheraccount.setVisibility(View.VISIBLE);
		} else {
			btn_login_otheraccount.setVisibility(View.GONE);
		}
		btn_login_otheraccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginOtherAccount();
			}
		});

		return rootView;
	}

	private void loginOtherAccount() {
		CookieSyncManager.createInstance(getActivity().getApplicationContext());
		CookieManager cm = CookieManager.getInstance();
		cm.removeAllCookie();
		webview.loadDataWithBaseURL("http://developers.facebook.com", html,
				"text/html", null, null);
		btn_login_otheraccount.setVisibility(View.GONE);
	}

	private boolean checkLoginWebviewAndroid() {
		CookieSyncManager.createInstance(getActivity().getApplicationContext());
		CookieManager cm = CookieManager.getInstance();
		try {
			String check = cm.getCookie("http://developers.facebook.com");
			System.out.println(check);
			if (check != null) {
				return true;
			}
		} catch (Exception e) {
			System.out.println("Error:" + e.getMessage());
		}
		return false;
	}

	private String getHTMLDetail(String url, String appId) {
		String html = "<html>  <body><div id=\"fb-root\"></div><script>(function(d, s, id) {  var js, fjs = d.getElementsByTagName(s)[0];  if (d.getElementById(id)) return;  js = d.createElement(s); js.id = id;  js.src = \"//connect.facebook.net/en_GB/sdk.js#xfbml=1&appId="
				+ appId
				+ "&version=v2.0\";  fjs.parentNode.insertBefore(js, fjs);}(document, 'script', 'facebook-jssdk'));</script><div class=\"fb-comments\" data-href="
				+ "\""
				+ url
				+ "\""
				+ " data-numposts=\"5\" data-order-by=\"reverse_time\" data-colorscheme=\"light\"></div>  </body></html>";
		return html;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
	}

	boolean loadingFinished = true;
	boolean redirect = false;

	public class WebViewClientActivity extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.e("onPageStarted:", url);
			loadingFinished = false;
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (!loadingFinished) {
				redirect = true;
			}
			loadingFinished = false;
			view.loadUrl(url);
			if (url.contains("close_popup.php")) {
				webview.loadDataWithBaseURL(
						"http://developers.facebook.com?"
								+ "reload=https://www.facebook.com/plugins/comments.php?api_key=1721779108048112&channel_url=http%3A%2F%2Fstaticxx.facebook.com%2Fconnect%2Fxd_arbiter.php%3Fversion%3D42%23cb%3Df18b53000c%26domain%3Ddevelopers.facebook.com%26origin%3Dhttp%253A%252F%252Fdevelopers.facebook.com%252Ff301c888ec%26relation%3Dparent.parent&colorscheme=light&href=http%3A%2F%2Fwww.rxclubhouse.com%2Findex.php%2Foptimum-nutrition-micronized-creatine-powder.html&locale=en_GB&mobile=true&numposts=1&order_by=reverse_time&sdk=joey&skin=light&version=v2.0&ret=optin&hash=AQCoVWl2H72Y6J-P#_=_",
						html, "text/html", null, null);
				btn_login_otheraccount.setVisibility(View.VISIBLE);
			}
			return true;
		}

		@Override
		public void onPageFinished(WebView webView, String url) {
			Log.e("onPageFinisheds:", url);
			if (!redirect) {
				loadingFinished = true;
			}
			if (loadingFinished && !redirect) {
				progressBar.setVisibility(View.GONE);
			} else {
				redirect = false;
			}
		}

	}

}
