package com.simicart.core.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.GenderConfig;

public class Utils {

	public static Bitmap scaleToFill(Bitmap b, int width, int height) {
		float factorH = height / (float) b.getWidth();
		float factorW = width / (float) b.getWidth();
		float factorToUse = (factorH > factorW) ? factorW : factorH;
		return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse),
				(int) (b.getHeight() * factorToUse), true);
	}

	public static void expand(final View v) {
		v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targetHeight = v.getMeasuredHeight();

		// Older versions of android (pre API 21) cancel animations for views
		// with a height of 0.
		v.getLayoutParams().height = 1;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
						: (int) (targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int) (targetHeight / v.getContext().getResources()
				.getDisplayMetrics().density));
		v.startAnimation(a);
	}

	public static void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight
							- (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int) (initialHeight / v.getContext().getResources()
				.getDisplayMetrics().density));
		v.startAnimation(a);
	}

	public static String endCodeJson(List<NameValuePair> pair)
			throws JSONException {
		int total = pair.size();
		JSONObject obj = new JSONObject();
		for (int i = 0; i < total; i++) {
			obj.put(pair.get(i).getName(), pair.get(i).getValue());
		}
		return obj.toString();
	}

	public static BasicNameValuePair endCodeValuePair(String name, String value) {
		return new BasicNameValuePair(name, value);
	}

	public static String getLabelGender(String value) {
		for (GenderConfig genderConfig : DataLocal.ConfigCustomerAddress
				.getGenderConfigs()) {
			if (genderConfig.getValue().equals(value)) {
				return genderConfig.getLabel();
			}
		}
		return "";
	}

	public static String getValueGender(String label) {
		for (GenderConfig genderConfig : DataLocal.ConfigCustomerAddress
				.getGenderConfigs()) {
			if (genderConfig.getLabel().equals(label)) {
				return genderConfig.getValue();
			}
		}
		return "";
	}

	// hideKeyboard
	public static void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getRootView().getWindowToken(), 0);
	}

	// set padding
	public static void setPadding(View v, int left, int top, int right,
			int bottom) {
		float unit = v.getContext().getResources().getDisplayMetrics().density;
		left = (int) (left * unit + 0.5f);
		right = (int) (right * unit + 0.5f);
		top = (int) (top * unit + 0.5f);
		bottom = (int) (bottom * unit + 0.5f);
		v.setPadding(left, top, right, bottom);
	}

	public static String capitalizes(String source) {
		StringBuilder builder = new StringBuilder();
		source = source.toLowerCase();
		String[] arr = source.split(" ");
		for (String string : arr) {
			String tmp = string.substring(0, 1).toUpperCase()
					+ string.substring(1) + " ";
			builder.append(tmp);
		}
		return builder.toString().trim();
	}

	public static int getValueDp(int value) {
		float unit = SimiManager.getIntance().getCurrentContext()
				.getResources().getDisplayMetrics().density;
		return (int) (value * unit + 0.5f);
	}

	public static void changeTextview(String color, float size) {

	}

	public static boolean isTablet(Context context) {

		String type_device = context.getString(Rconfig.getInstance().getId(
				context, "type_device", "string"));
		if (type_device.equals("phone")) {
			return false;
		}
		return true;

	}

	@SuppressLint("SimpleDateFormat")
	public static void getTimeLoadPage(String namePage) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String currentDateandTime = sdf.format(new Date());
		System.err.println("Time Start " + namePage + ":" + currentDateandTime);
		Log.e("Time Start " + namePage + ":", currentDateandTime);
	}

	public static boolean validateString(String content) {
		if (null == content) {
			return false;
		}
		if (content.equals("")) {
			return false;
		}
		if (content.equals("null")) {
			return false;
		}

		return true;
	}

	public static void setBackgroundView(View view, String color) {
		view.setBackgroundColor(Color.parseColor(color));
	}

	public static final String md5(final String s) {
		final String MD5 = "MD5";
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
			if (s != null) {
				digest.update(s.getBytes());
			}
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuilder hexString = new StringBuilder();
			for (byte aMessageDigest : messageDigest) {
				String h = Integer.toHexString(0xFF & aMessageDigest);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void changeColorEditText(EditText editText) {
		if (editText != null) {
			editText.setTextColor(Config.getInstance().getContent_color());
			editText.setHintTextColor(Config.getInstance()
					.getHintContent_color());
		}
	}

	public static void changeColorTextView(TextView textView) {
		if (textView != null) {
			textView.setTextColor(Config.getInstance().getContent_color());
		}
	}

	public static void changeColorImageview(Context context,
			ImageView imageView, String src) {
		if (context != null && imageView != null && src != null) {
			Drawable icon = context.getResources().getDrawable(
					Rconfig.getInstance().drawable(src));
			icon.setColorFilter(Config.getInstance().getContent_color(),
					PorterDuff.Mode.SRC_ATOP);
			imageView.setImageDrawable(icon);
		}
	}

	public static void changeColorListView(ListView listView) {
		if (listView != null) {
			ColorDrawable sage = new ColorDrawable(Config.getInstance()
					.getLine_color());
			listView.setDivider(sage);
			listView.setDividerHeight(1);
		}
	}

	public static void changeColorLine(View view) {
		view.setBackgroundColor(Config.getInstance().getLine_color());
	}

}
