package com.simicart.core.menutop.block;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.search.entity.TagSearch;
import com.simicart.core.catalog.search.fragment.ListProductFragment;
import com.simicart.core.catalog.search.model.ConstantsSearch;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.menutop.delegate.MenuTopDelegate;

public class MenuTopBlock extends SimiBlock implements MenuTopDelegate {

	protected RelativeLayout rltCart;
	protected View mViewCart;
	protected Drawable iconCart;
	protected ImageView imv_menu;
	protected ImageView img_logo;
	// protected int heightDevice;
	protected Context context;
	protected LinearLayout ll_search_land;
	protected LinearLayout ll_logo_land;
	protected LinearLayout.LayoutParams param_logo_cancel;
	protected LinearLayout.LayoutParams param_logo;
	protected LinearLayout.LayoutParams param_search;
	protected LinearLayout.LayoutParams param_search_cancel;
	protected LinearLayout ll_search;

	public MenuTopBlock(View view, Context context) {
		super(view, context);
		mView.setBackgroundColor(Config.getInstance().getKey_color());
		this.context = context;
	}

	@Override
	public void initView() {
		initButtonMenu();
		initCartView();
		if (DataLocal.isTablet) {
			initSearch();
		}
	}

	private void initSearch() {
		final ImageView img_search = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_ic_search2"));
		Drawable ic_search = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("icon_search_arrow"));
		ic_search.setColorFilter(Config.getInstance().getTop_menu_icon_color(),
				PorterDuff.Mode.SRC_ATOP);
		img_search.setImageDrawable(ic_search);
		ll_search = (LinearLayout) mView.findViewById(Rconfig.getInstance().id(
				"ll_search"));
		// int idPaddingLogo = Rconfig.getInstance().getDimens("wightDevice");
		// final float paddingLogo =
		// context.getResources().getDimension(idPaddingLogo);

		if (DataLocal.isTablet) {
			ll_search_land = (LinearLayout) mView.findViewById(Rconfig
					.getInstance().id("ll_search_land"));
			ll_logo_land = (LinearLayout) mView.findViewById(Rconfig
					.getInstance().id("ll_logo_land"));
		}
		param_search = new LinearLayout.LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f);
		param_logo = new LinearLayout.LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.5f);
		param_search_cancel = new LinearLayout.LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.05f);
		param_logo_cancel = new LinearLayout.LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 0.95f);

		img_search.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				img_search.setVisibility(View.GONE);
				ll_search.setVisibility(View.VISIBLE);
				// img_logo.setPadding(0, 0,(int)paddingLogo, 0);
				if (DataLocal.isTablet) {
					ll_logo_land.setLayoutParams(param_logo);
					ll_search_land.setLayoutParams(param_search);
					ll_search_land.setPadding(0, 5, 0, 5);
				}
			}
		});

		final ImageView img_ic_search = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_ic_search"));
		Drawable drawable_search = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_search_tablet"));
		drawable_search.setColorFilter(Config.getInstance()
				.getTop_menu_icon_color(), PorterDuff.Mode.SRC_ATOP);
		img_ic_search.setImageDrawable(drawable_search);

		final ImageView img_cancel = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_ic_cancel"));
		Drawable drawable = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("icon_delete_white"));
		drawable.setColorFilter(Config.getInstance().getTop_menu_icon_color(),
				PorterDuff.Mode.SRC_ATOP);
		img_cancel.setImageDrawable(drawable);
		img_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				img_search.setVisibility(View.VISIBLE);
				ll_search.setVisibility(View.GONE);
				Utils.hideKeyboard(mView);
				if (DataLocal.isTablet) {
					ll_logo_land.setLayoutParams(param_logo_cancel);
					ll_search_land.setLayoutParams(param_search_cancel);
					ll_search_land.setPadding(0, 5, 0, 5);
				}
			}
		});

		final EditText et_search = (EditText) mView.findViewById(Rconfig
				.getInstance().id("et_search"));
		et_search.setHint(Config.getInstance().getText("Search"));
		// et_search.setHintTextColor(Color.parseColor("#C7C7C7"));
		et_search.setHintTextColor(Config.getInstance()
				.getTop_menu_icon_color());
		et_search.setTextColor(Config.getInstance().getTop_menu_icon_color());
		et_search.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)
						&& (event.getAction() == KeyEvent.ACTION_DOWN)) {
					showSearchScreen(et_search.getText().toString());
					Utils.hideKeyboard(v);
					return true;
				}
				return false;
			}
		});
	}

	protected void showSearchScreen(String query) {
		ListProductFragment fragment = ListProductFragment.newInstance();
		fragment.setQuerySearch(query);
		fragment.setUrlSearch(ConstantsSearch.url_query);
		fragment.setTag_search(TagSearch.TAG_GRIDVIEW);
		SimiManager.getIntance().replaceFragment(fragment);
	}

	private void initButtonMenu() {
		imv_menu = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"img_menu"));
		Drawable ic_menu = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_drawer2"));
		ic_menu.setColorFilter(Config.getInstance().getTop_menu_icon_color(),
				PorterDuff.Mode.SRC_ATOP);
		imv_menu.setImageDrawable(ic_menu);
	}

	private void initCartView() {
		initIconCart();
		Button btn_cartnavigation = (Button) mView.findViewById(Rconfig
				.getInstance().id("btn_cartnavigation"));
		btn_cartnavigation.setBackgroundDrawable(iconCart);

		rltCart = (RelativeLayout) mView.findViewById(Rconfig.getInstance().id(
				"layout_cart"));
		mViewCart = mView.findViewById(Rconfig.getInstance().id(
				"btn_cartnavigation"));
	}

	protected void initIconCart() {
		iconCart = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_cart"));
		iconCart.setColorFilter(Config.getInstance().getTop_menu_icon_color(),
				PorterDuff.Mode.SRC_ATOP);
	}

	@Override
	public void updateBackground(int color) {
		if (color != 0) {
			rltCart.setBackgroundColor(0x80CACACA);
		} else {
			rltCart.setBackgroundColor(0);
		}
	}

	@Override
	public void updateCartQty(String qty) {
		TextView tv_cartQty = (TextView) mView.findViewById(Rconfig
				.getInstance().id("cart_qty"));
		if (qty == null || qty.equals("0") || qty.equals("")
				|| qty.equals("null")) {
			tv_cartQty.setVisibility(View.GONE);
		} else {
			try {
				int i_qty = Integer.valueOf(qty);
				if (i_qty > 0) {
					tv_cartQty.setVisibility(View.VISIBLE);
					tv_cartQty.setText(String.valueOf(qty));
				}
			} catch (Exception e) {

			}
		}
	}

	public void setOnTouchCart(OnTouchListener touchCart) {
		mViewCart.setOnTouchListener(touchCart);
	}

	public void setOnTouchMenu(OnTouchListener touchMenu) {
		imv_menu.setOnTouchListener(touchMenu);
	}

	@Override
	public void showCartLayout(boolean show) {
		if (show) {
			rltCart.setVisibility(View.VISIBLE);
			if (DataLocal.isTablet) {
				RelativeLayout layout_right = (RelativeLayout) mView
						.findViewById(Rconfig.getInstance().id("layout_right"));
				layout_right.setVisibility(View.VISIBLE);
				// if (ll_search_land.getLayoutParams() == param_search_cancel)
				// {
				// ll_logo_land.setLayoutParams(param_logo_cancel);
				// } else {
				if (ll_search.getVisibility() == LinearLayout.VISIBLE) {
					ll_logo_land.setLayoutParams(param_logo);
					ll_search_land.setLayoutParams(param_search);
				} else {
					ll_logo_land.setLayoutParams(param_logo_cancel);
					ll_search_land.setLayoutParams(param_search_cancel);
				}
			}
		} else {
			rltCart.setVisibility(View.GONE);
			if (DataLocal.isTablet) {
				RelativeLayout layout_right = (RelativeLayout) mView
						.findViewById(Rconfig.getInstance().id("layout_right"));
				if (ll_search_land.getVisibility() == LinearLayout.VISIBLE) {
					layout_right.setVisibility(View.INVISIBLE);
					ll_logo_land.setLayoutParams(param_logo_cancel);
				} else {
					layout_right.setVisibility(View.VISIBLE);
					ll_logo_land.setLayoutParams(param_logo_cancel);
				}
			}
		}
	}
}
