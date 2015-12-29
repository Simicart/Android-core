package com.simicart.core.home.block;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;

public class SearchHomeBlock extends SimiBlock {

	protected String mCatID = "-1";
	protected String mCatName = "";
	protected String mQuery = "";
	private RelativeLayout rlt_layout;
	private String tag;
	private EditText et_search;
	private RelativeLayout relativeLayout;

	public SearchHomeBlock(View view, Context context) {
		super(view, context);
		view.setBackgroundColor(0);
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setCateID(String catID) {
		this.mCatID = catID;
	}

	public void setCatName(String catName) {
		this.mCatName = catName;
	}

	@Override
	public void initView() {
		LinearLayout ll_search = (LinearLayout) mView.findViewById(Rconfig
				.getInstance().id("ll_search"));
		ll_search.setBackgroundColor(Config.getInstance()
				.getSearch_box_background());

		rlt_layout = (RelativeLayout) mView.findViewById(Rconfig.getInstance()
				.id("rlt_layout"));
		relativeLayout = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rlt_layout"));
		ImageView img_ic_search = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_ic_search"));
		Drawable drawable = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_search"));
		drawable.setColorFilter(Config.getInstance().getSearch_icon_color(),
				PorterDuff.Mode.SRC_ATOP);
		img_ic_search.setImageDrawable(drawable);

		et_search = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_search"));
		et_search.setHint(Config.getInstance().getText("Search products"));
		if (!mCatName.equals("")
				&& !mCatName.equals(Config.getInstance()
						.getText("all products"))) {
			et_search.setHint(Config.getInstance().getText("Searching for")
					+ "" + mCatName);
			et_search.setTypeface(null, Typeface.BOLD);
		}
		if (!mQuery.equals("")) {
			et_search.setText(mQuery);
		}
		et_search.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		et_search.setTextColor(Config.getInstance().getSearch_text_color());
		et_search.setHintTextColor(Config.getInstance().getSearch_text_color());
		et_search.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					rlt_layout.setVisibility(View.GONE);
				} else {
					rlt_layout.setVisibility(View.VISIBLE);
				}
			}
		});
		et_search.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)
						&& (event.getAction() == KeyEvent.ACTION_DOWN)) {
					showSearchScreen(et_search.getText().toString(), tag);
					Utils.hideKeyboard(v);
					return true;
				}
				return false;
			}
		});
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_search.requestFocus();
				InputMethodManager imm = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(et_search, InputMethodManager.SHOW_IMPLICIT);
			}
		});
		super.initView();
	}

	public void showSearchScreen(String key, String tag) {
		if (key != null && !key.equals("")) {
			ListProductFragment fragment = ListProductFragment.newInstance();
			fragment.setQuerySearch(key);
			fragment.setTag_search(tag);
			fragment.setUrlSearch(Constants.SEARCH_PRODUCTS);
			SimiManager.getIntance().addFragment(fragment);
		}
	}

	@Override
	public void drawView(SimiCollection collection) {
	}

	public void setQuery(String mQuery) {
		this.mQuery = mQuery;
	}
}