package com.simicart.plugins.rewardpoint.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.network.request.MySSLSocketFactory;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.utils.Constant;

public class FragmentRewardCard extends SimiFragment {

	private String passBookLogo = "";
	private String loyalty_redeem = "";
	private String passbook_text = "";
	private String background_passbook = "";
	private String passbook_foreground = "";
	private String passbook_barcode = "";

	private ImageView img_logo;
	private TextView txt_balance;
	private TextView txt_balance_content;
	private TextView txt_passbook;
	private ImageView img_add_passbook;
	private ImageView img_barcode;
	private TextView txt_barcode;
	private Context mContext;
	private String file_pkpass = "/reward.pkpass";
	protected ProgressDialog pd_loading;

	private LinearLayout layout_passbook;

	public FragmentRewardCard(String passBookLogo, String loyalty_redeem,
			String nameapp, String background, String passbook_foreground,
			String passbook_barcode) {
		this.passBookLogo = passBookLogo;
		this.loyalty_redeem = loyalty_redeem;
		this.passbook_text = nameapp;
		this.background_passbook = background;
		this.passbook_foreground = passbook_foreground.trim();
		this.passbook_barcode = passbook_barcode.trim();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				Rconfig.getInstance()
						.layout("plugins_rewardpoint_cardfragment"), container,
				false);
		mContext = getActivity();
		img_logo = (ImageView) view.findViewById(Rconfig.getInstance().id(
				"img_logo_passbook"));
		img_barcode = (ImageView) view.findViewById(Rconfig.getInstance().id(
				"img_code"));
		txt_barcode = (TextView) view.findViewById(Rconfig.getInstance().id(
				"txt_barcode"));
		txt_barcode.setText(passbook_barcode);
		txt_balance = (TextView) view.findViewById(Rconfig.getInstance().id(
				"txt_balance"));
		if (!passbook_foreground.equals("")) {
			txt_balance.setTextColor(Color
					.parseColor("#" + passbook_foreground));
		}
		txt_balance.setText(Config.getInstance().getText("Balance"));
		txt_balance_content = (TextView) view.findViewById(Rconfig
				.getInstance().id("txt_balance_content"));
		txt_balance_content.setText(loyalty_redeem);
		if (!passbook_foreground.equals("")) {
			txt_balance_content.setTextColor(Color.parseColor("#"
					+ passbook_foreground));
		}

		txt_passbook = (TextView) view.findViewById(Rconfig.getInstance().id(
				"txt_passbook"));
		txt_passbook.setText(passbook_text);
		if (!passbook_foreground.equals("")) {
			txt_passbook.setTextColor(Color.parseColor("#"
					+ passbook_foreground));
		}

		layout_passbook = (LinearLayout) view.findViewById(Rconfig
				.getInstance().id("layout_passbook"));
		if (!background_passbook.equals("")) {
			layout_passbook.setBackgroundColor(Color.parseColor("#"
					+ background_passbook.trim()));
		}
		GradientDrawable bg_passbook = new GradientDrawable();
		if (!background_passbook.equals("")) {
			bg_passbook.setColor(Color.parseColor("#"
					+ background_passbook.trim()));
		}else{
			bg_passbook.setColor(Color.parseColor("#FF6600"));
		}
		bg_passbook.setCornerRadius(Utils.getValueDp(15));
		bg_passbook.setStroke(1, Color.parseColor("#FF6600"));

		layout_passbook.setBackgroundDrawable(bg_passbook);

		if (!passBookLogo.equals("")) {
			DrawableManager.fetchDrawableOnThread(passBookLogo, img_logo);
		}
		img_add_passbook = (ImageView) view.findViewById(Rconfig.getInstance()
				.id("img_addto_passbook"));
		img_add_passbook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = Config.getInstance().getBaseUrl()
						+ Constant.GET_CART;
				new getFilePkpassFromServer().execute(url);
			}
		});
		getImageBarcode();
		return view;
	}

	private void getImageBarcode() {
		String url_request = Config.getInstance().getBaseUrl()
				+ Constant.GET_PASSBOOK_BARCODE + passbook_barcode;
		try {
			DrawableManager.fetchDrawableDetailOnThread(url_request,
					img_barcode);
		} catch (Exception e) {
			Log.d("Error Get Barcode:", e.getMessage());
		}

	}

	private class getFilePkpassFromServer extends
			AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd_loading = ProgressDialog.show(mContext, null, null, true, false);
			pd_loading.setContentView(Rconfig.getInstance().layout(
					"core_base_loading"));
			pd_loading.setCanceledOnTouchOutside(false);
			pd_loading.setCancelable(false);
			pd_loading.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			byte[] data;
			HttpClient httpclient = getNewHttpClient();
			HttpPost httppost = new HttpPost(url);
			String userAgent = System.getProperty("http.agent");
			String cookie = Config.getInstance().getCookie();
			httppost.setHeader("Cookie", cookie);
			// read response
			try {
				HttpResponse response = httpclient.execute(httppost);
				InputStream input = response.getEntity().getContent();
				data = new byte[input.available()];
				input.read(data);
				File path = new File(Environment.getExternalStorageDirectory()
						+ file_pkpass);
				if (!path.isFile()) {
					path.createNewFile();
				}
				OutputStream outputStream = new FileOutputStream(path);
				int read = 0;
				byte[] bytes = new byte[1024];
				while ((read = input.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
			} catch (Exception e) {
				Log.d("Error:", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd_loading.dismiss();
			String url_file = "file:///sdcard" + file_pkpass;
			launchPassWallet(mContext, Uri.parse(url_file), true);
		}
	}

	public static DefaultHttpClient getNewHttpClient() {
		DefaultHttpClient httpClient = null;
		try {
			if (httpClient == null) {
				HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));

				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
				sf.setHostnameVerifier((X509HostnameVerifier) SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				registry.register(new Scheme("https", sf, 443));

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(
						params, registry);
				httpClient = new DefaultHttpClient(ccm, params);
			}
			return httpClient;
		} catch (Exception e) {
			if (httpClient == null) {
				httpClient = new DefaultHttpClient();
			}
			return httpClient;
		}
	}

	@SuppressWarnings("unused")
	private static boolean launchPassWallet(Context applicationContext,
			Uri uri, boolean launchGooglePlay) {
		if (null != applicationContext) {
			PackageManager packageManager = applicationContext
					.getPackageManager();
			if (null != packageManager) {
				final String strPackageName = "com.attidomobile.passwallet";
				Intent startIntent = new Intent();
				startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startIntent.setAction(Intent.ACTION_VIEW);
				Intent passWalletLaunchIntent = packageManager
						.getLaunchIntentForPackage(strPackageName);
				if (null == passWalletLaunchIntent) {
					// PassWallet isn't installed, open Google Play:
					if (launchGooglePlay) {
						String strReferrer = "";
						try {
							final String strEncodedURL = URLEncoder.encode(
									uri.toString(), "UTF-8");
							strReferrer = "&referrer=" + strEncodedURL;
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
							strReferrer = "";
						}
						try {
							startIntent.setData(Uri
									.parse("market://details?id="
											+ strPackageName + strReferrer));
							applicationContext.startActivity(startIntent);
						} catch (android.content.ActivityNotFoundException anfe) {
							// Google Play not installed, open via website
							startIntent
									.setData(Uri
											.parse("http://play.google.com/store/apps/details?id="
													+ strPackageName
													+ strReferrer));
							applicationContext.startActivity(startIntent);
						}
					}
				} else {
					final String strClassName = "com.attidomobile.passwallet.activity.TicketDetailActivity";
					startIntent.setClassName(strPackageName, strClassName);
					startIntent.addCategory(Intent.CATEGORY_BROWSABLE);
					startIntent.setDataAndType(uri,
							"application/vnd.apple.pkpass");
					applicationContext.startActivity(startIntent);
					return true;
				}
			}
		}
		return false;
	}
}
