package com.simicart.plugins.SearchVoice;

import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;

public class SearchVoice {

	private View mRootView;
	private final int REQ_CODE_SPEECH_INPUT = 100;

	private final String SEARCH_TEXT_RESULT = "textvoice";
	private final String SEARCH_URL = "urlSearch";
	private final String SEARCH_CATE_ID = "cateId";
	private final String SEARCH_CATE_NAME = "cateName";
	private final String SEARCH_TAG = "tagSearch";

	private String cateId = null, cateName = null, tagSearch = null;

	public SearchVoice(String method, CacheBlock cacheBlock) {
		mRootView = cacheBlock.getView();
		// getListLanguageSupport
		if (InstanceVoice.LISTLANGUAGESUPPORT.size() == 0) {
			getListLanguageSupport();
		}

		if (method.equals("addIconSearchVoice")) {
			if (cacheBlock.getSimiEntity() != null) {
				JSONObject object = cacheBlock.getSimiEntity().getJSONObject();
				if (object != null) {
					addIconSearchVoiceProductList(object);
				}
			} else {
				addIconSearchHome();
			}
		}
		if (method.equals("resultSearch")) {
			JSONObject object = cacheBlock.getSimiEntity().getJSONObject();
			if (object != null) {
				handleSearchVoice(object);
			}
		}
		if (method.equals("addiconsearchtablet")) {
			View rootView = cacheBlock.getView();
			RelativeLayout rlt_layout = (RelativeLayout) rootView
					.findViewById(Rconfig.getInstance().id("rlt_right_menutop"));
			if (DataLocal.isTablet) {
				addSearchVoiceTablet(rlt_layout);
			}
		}
	}

	private void addSearchVoiceTablet(RelativeLayout rootView) {
		RelativeLayout relativeLayout = rootView;
		LinearLayout layoutlogo = (LinearLayout) rootView.findViewById(Rconfig
				.getInstance().id("layoutlogo"));
		RelativeLayout layout_cart = (RelativeLayout) rootView
				.findViewById(Rconfig.getInstance().id("layout_cart"));

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				60, LinearLayout.LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.LEFT_OF,
				Rconfig.getInstance().id("layout_cart"));
		layoutParams.addRule(RelativeLayout.RIGHT_OF,
				Rconfig.getInstance().id("layoutlogo"));
		RelativeLayout layout = new RelativeLayout(SimiManager.getIntance()
				.getCurrentContext());
		layout.setPadding(0, 0, 0, 14);
		layout.setLayoutParams(layoutParams);
		layoutlogo.addView(layout);

		ImageView imageView = new ImageView(SimiManager.getIntance()
				.getCurrentContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				Utils.getValueDp(25), Utils.getValueDp(25));
		params.setMargins(0, 0, 5, 0);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		imageView.setLayoutParams(params);
		imageView.setImageResource(Rconfig.getInstance().drawable(
				"ic_search_voice_tab"));
		imageView.setColorFilter(Color.parseColor("#ffffff"));
		layout.addView(imageView);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateData(null, null, null);
				promptSpeechInput();
			}
		});

	}

	private void getListLanguageSupport() {
		if (Locale.getAvailableLocales().length > 0) {
			for (Locale locale : Locale.getAvailableLocales()) {
				InstanceVoice.LISTLANGUAGESUPPORT.add(locale.toString());
			}
		}
	}

	private void handleSearchVoice(JSONObject object) {
		@SuppressWarnings("unused")
		String textSearch = null, tagSearch = null, categoryName = null, categoryId = null;
		try {
			categoryId = InstanceVoice.SEARCH_CATE_ID;
			tagSearch = InstanceVoice.SEARCH_TAG;
			categoryName = InstanceVoice.SEARCH_CATE_NAME;
			if (object.has(SEARCH_TEXT_RESULT)) {
				textSearch = object.getString(SEARCH_TEXT_RESULT);
			}
			searchProduct(categoryId, tagSearch, categoryName, textSearch);
		} catch (Exception e) {
		}
	}

	private void addIconSearchHome() {
		LinearLayout layoutSearch = (LinearLayout) mRootView
				.findViewById(Rconfig.getInstance().id("ll_search"));
		RelativeLayout rlt_layout = (RelativeLayout) mRootView
				.findViewById(Rconfig.getInstance().id("rlt_layout"));
		rlt_layout.setVisibility(View.GONE);

		LayoutParams param_view = new LayoutParams(4, LayoutParams.MATCH_PARENT);
		View view = new View(SimiManager.getIntance().getCurrentContext());
		view.setBackgroundColor(Color.parseColor("#ffffff"));
		view.setLayoutParams(param_view);
		layoutSearch.addView(view);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				100, LinearLayout.LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		RelativeLayout layout = new RelativeLayout(SimiManager.getIntance()
				.getCurrentContext());
		layout.setLayoutParams(layoutParams);
		layoutSearch.addView(layout);

		ImageView imageView = new ImageView(SimiManager.getIntance()
				.getCurrentContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				Utils.getValueDp(22), Utils.getValueDp(22));
		params.setMargins(0, 0, 5, 0);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		imageView.setLayoutParams(params);
		imageView.setImageResource(Rconfig.getInstance().drawable(
				"ic_search_voice"));
		layout.addView(imageView);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateData(null, null, null);
				promptSpeechInput();
			}
		});
	}

	private void addIconSearchVoiceProductList(JSONObject object) {

		LinearLayout layoutSearch = (LinearLayout) mRootView
				.findViewById(Rconfig.getInstance().id("ll_search"));
		RelativeLayout rlt_layout = (RelativeLayout) mRootView
				.findViewById(Rconfig.getInstance().id("rlt_layout"));
		rlt_layout.setVisibility(View.GONE);

		LayoutParams param_view = new LayoutParams(4, LayoutParams.MATCH_PARENT);
		View view = new View(SimiManager.getIntance().getCurrentContext());
		view.setBackgroundColor(Color.parseColor("#ffffff"));
		view.setLayoutParams(param_view);
		layoutSearch.addView(view);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				100, LinearLayout.LayoutParams.MATCH_PARENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		RelativeLayout layout = new RelativeLayout(SimiManager.getIntance()
				.getCurrentContext());
		layout.setLayoutParams(layoutParams);
		layoutSearch.addView(layout);

		ImageView imageView = new ImageView(SimiManager.getIntance()
				.getCurrentContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				Utils.getValueDp(22), Utils.getValueDp(22));
		params.setMargins(0, 0, 5, 0);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		imageView.setLayoutParams(params);
		imageView.setImageResource(Rconfig.getInstance().drawable(
				"ic_search_voice"));
		layout.addView(imageView);

		try {
			if (object != null) {
				if (object.has(SEARCH_CATE_ID)) {
					cateId = object.getString(SEARCH_CATE_ID);
				}
				if (object.has(SEARCH_CATE_NAME)) {
					cateName = object.getString(SEARCH_CATE_NAME);
				}
				if (object.has(SEARCH_TAG)) {
					tagSearch = object.getString(SEARCH_TAG);
				}
			}
		} catch (Exception e) {
		}
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateData(cateId, cateName, tagSearch);
				promptSpeechInput();
			}
		});
	}

	private void promptSpeechInput() {
		// updateData(url, cateId, cateName, tagSearch);
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		if (checkLanguageSupport()) {
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Config
					.getInstance().getLocale_identifier());
		} else {
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
					Locale.getDefault());
		}
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");
		try {
			SimiManager.getIntance().getCurrentActivity()
					.startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(SimiManager.getIntance().getCurrentContext(),
					"Sorry! Your device doesn\'t support speech input",
					Toast.LENGTH_SHORT).show();
		}
	}

	private boolean checkLanguageSupport() {
		if (InstanceVoice.LISTLANGUAGESUPPORT.size() > 0
				&& Utils.validateString(Config.getInstance()
						.getLocale_identifier())) {
			String locale = Config.getInstance().getLocale_identifier();
			System.out.println(locale);
			for (String language : InstanceVoice.LISTLANGUAGESUPPORT) {
				if (language
						.equals(Config.getInstance().getLocale_identifier())) {
					return true;
				}
			}
		}
		return false;
	}

	private void updateData(String cateId, String cateName, String tagsearch) {
		InstanceVoice.SEARCH_CATE_ID = cateId;
		InstanceVoice.SEARCH_CATE_NAME = cateName;
		InstanceVoice.SEARCH_TAG = tagsearch;
	}

	private void searchProduct(String cateID, String tagSearch,
			String cateName, String query) {
		SimiManager.getIntance().showToast(query);
		if (tagSearch == null) {
			tagSearch = TagSearch.TAG_LISTVIEW;
		}
		ListProductFragment fragment = ListProductFragment.newInstance(
				Constants.SEARCH_PRODUCTS, cateID, tagSearch, null, cateName,
				query, null, null);
		SimiManager.getIntance().addFragment(fragment);
	}

}
