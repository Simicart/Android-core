package com.simicart.core.catalog.product.block;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.delegate.ProductDelegate;
import com.simicart.core.catalog.product.delegate.ProductDetailAdapterDelegate;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.fragment.OptionFragment;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.material.ButtonRectangle;
import com.simicart.core.style.CirclePageIndicator;
import com.simicart.core.style.VerticalViewPager2;

public class ProductDetailParentBlock extends SimiBlock implements
		ProductDelegate, ProductDetailAdapterDelegate {
	// protected LinearLayout ll_top;
	protected RelativeLayout rlt_top;
	protected LinearLayout ll_bottom;
	protected LinearLayout ll_more;
	protected ButtonRectangle btn_option;
	protected ButtonRectangle btn_addtocart;
	protected TextView tv_name_product;
	protected LinearLayout ll_price;
	protected Product mProduct;
	protected CirclePageIndicator mIndicator;
	protected OnClickListener onDoneOption;
	protected ImageView img_animation;
	protected AnimationSet animation;

	public ProductDetailParentBlock(View view, Context context) {
		super(view, context);

	}

	public void setOnDoneOption(OnClickListener onDoneOption) {
		this.onDoneOption = onDoneOption;
	}

	public void setAddToCartListener(OnClickListener listener) {
		btn_addtocart.setOnClickListener(listener);
	}

	public void setDetailListener(OnTouchListener listener) {

		ll_more.setOnTouchListener(listener);
	}

	public void setOptionListener(OnClickListener listener) {
		btn_option.setOnClickListener(listener);
	}

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		rlt_top = (RelativeLayout) mView.findViewById(Rconfig.getInstance().id(
				"ll_top_product_detatil"));
		ll_bottom = (LinearLayout) mView.findViewById(Rconfig.getInstance().id(
				"ll_bottom_product_detail"));
		rlt_top.setBackgroundResource(Rconfig.getInstance().drawable(
				"core_backgroud_top_product_detail"));
		rlt_top.setBackgroundColor(Color.parseColor(Config.getInstance()
				.getSection_color()));
		rlt_top.getBackground().setAlpha(100);

		// details
		ll_more = (LinearLayout) mView.findViewById(Rconfig.getInstance().id(
				"ll_more"));
		// ll_more.setVisibility(View.INVISIBLE);

		TextView tv_more = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_more"));
		tv_more.setText(Config.getInstance().getText("More"));
		tv_more.setTextColor(Config.getInstance().getContent_color());
		ImageView img_icon_more = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_more"));
		Drawable icon = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_icon_more"));
		icon.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		img_icon_more.setImageDrawable(icon);

		/* 23/11/2015 start Frank: fix bug display "More" length */
		ImageView img_seprate = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("img_seprate"));
		Drawable icon_img_seprate = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_background_right_border"));
		icon_img_seprate.setColorFilter(
				Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		img_seprate.setImageDrawable(icon_img_seprate);
		/* end Frank: fix bug display "More" length */

		// options
		btn_option = (ButtonRectangle) mView.findViewById(Rconfig.getInstance()
				.id("btn_option"));

		// add to cart
		btn_addtocart = (ButtonRectangle) mView.findViewById(Rconfig
				.getInstance().id("btn_addtocart"));

		// name product
		tv_name_product = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_name_product"));
		// price
		ll_price = (LinearLayout) mView.findViewById(Rconfig.getInstance().id(
				"layout_price"));

		// indicator
		mIndicator = (CirclePageIndicator) mView.findViewById(Rconfig
				.getInstance().id("indicator"));
		mIndicator.setFillColor(Config.getInstance().getColorMain());
		if (DataLocal.isTablet) {
			mIndicator.setScaleX(1.5f);
			mIndicator.setScaleY(1.5f);
		}
		mIndicator.setOrientation(LinearLayout.VERTICAL);
		setAnimation();
		img_animation = (ImageView) mView.findViewById(Rconfig.getInstance()
				.id("img_animation"));
		img_animation.setVisibility(View.INVISIBLE);
		tv_name_product.setVisibility(View.VISIBLE);
		rlt_top.setVisibility(View.VISIBLE);
		ll_bottom.setVisibility(View.VISIBLE);
	}

	public void setAnimation() {

		animation = new AnimationSet(false);
		final Animation rotate = AnimationUtils.loadAnimation(mContext,
				com.magestore.simicart.R.anim.add_to_cart_rotate);
		animation.addAnimation(rotate);
		animation.addAnimation(AnimationUtils.loadAnimation(mContext,
				com.magestore.simicart.R.anim.add_to_cart_zoom));
		if (DataLocal.isTablet) {

			animation.setDuration(800);
		} else {
			animation.setDuration(600);
		}
	}

	@Override
	public void drawView(SimiCollection collection) {

		if (null != collection) {
			mProduct = getProductFromCollection(collection);
			if (null != mProduct) {

				Log.e("ProductDetailParentBlock ",
						"drawView " + mProduct.getId());

				ll_bottom.setVisibility(View.VISIBLE);
				rlt_top.setVisibility(View.VISIBLE);

				showNameProduct();
				showOption();
				showAddToCart();
			} else {
				Log.e("ProductDetailParentBlock ", "drawView NULL");
			}

		}
		ll_more.setVisibility(View.VISIBLE);
	}

	protected void showNameProduct() {
		if (null != mProduct) {
			String name_product = mProduct.getName();
			tv_name_product.setVisibility(View.VISIBLE);
			tv_name_product.setTextColor(Config.getInstance()
					.getContent_color());
			if (Utils.validateString(name_product)) {

				Log.e("ProductDetailParentBlock ", "showNameProduct "
						+ name_product + tv_name_product.getVisibility());

				tv_name_product.setText(name_product.trim());

			}
		}
	}

	protected void showOption() {
		ArrayList<CacheOption> options = mProduct.getOptions();
		Drawable bg_button = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_background_button"));
		if (null == options || options.size() == 0) {

			bg_button.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
			btn_option.setText(Config.getInstance().getText("No Option"));
			btn_option.setTextColor(Color.parseColor("#FFFFFF"));
			btn_option.setClickable(false);
			btn_option.setVisibility(View.GONE);
			btn_option.setTextSize(Constants.SIZE_TEXT_BUTTON);
			btn_option.setBackgroundColor(Config.getInstance().getColorMain());
		} else {
			bg_button.setColorFilter(Config.getInstance().getColorMain(),
					PorterDuff.Mode.SRC_ATOP);
			btn_option.setVisibility(View.VISIBLE);
			btn_option.setText(Config.getInstance().getText("Options"));
			btn_option.setTextColor(Color.parseColor("#FFFFFF"));
			btn_option.setClickable(true);
			btn_option.setTextSize(Constants.SIZE_TEXT_BUTTON);
			btn_option.setBackgroundColor(Config.getInstance().getColorMain());
		}
	}

	protected void showAddToCart() {
		boolean stock = mProduct.getStock();
		Drawable bg_button = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_background_button"));

		Log.e("ProductDetailParentBlock ",
				"showAddToCart TOP " + rlt_top.getVisibility() + " BOTTOM "
						+ ll_bottom.getVisibility() + "NORMAL " + View.VISIBLE);

		if (stock) {
			bg_button.setColorFilter(Config.getInstance().getColorMain(),
					PorterDuff.Mode.SRC_ATOP);
			btn_addtocart.setText(Config.getInstance().getText("Add To Cart"));
			btn_addtocart.setTextColor(Color.parseColor("#FFFFFF"));
			btn_addtocart.setBackgroundColor(Config.getInstance()
					.getColorMain());
			btn_addtocart.setTextSize(Constants.SIZE_TEXT_BUTTON);
		} else {
			bg_button.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
			btn_addtocart.setText(Config.getInstance().getText("Out Stock"));
			btn_addtocart.setTextColor(Color.parseColor("#FFFFFF"));
			btn_addtocart.setBackgroundColor(Color.GRAY);
			btn_addtocart.setTextSize(Constants.SIZE_TEXT_BUTTON);
			btn_addtocart.setClickable(false);
		}
	}

	@Override
	public void onVisibleTopBottom(boolean isVisible) {
		if (isVisible) {
			if (rlt_top.getVisibility() == View.VISIBLE
					&& ll_bottom.getVisibility() == View.VISIBLE) {

				Log.e("ProductDetailParentBlock ", "onVisibleTopBottom GONE ");

				tv_name_product.setVisibility(View.GONE);
				rlt_top.setVisibility(View.GONE);
				ll_bottom.setVisibility(View.GONE);
			} else {
				Log.e("ProductDetailParentBlock ",
						"onVisibleTopBottom VISIBLE ");
				tv_name_product.setVisibility(View.VISIBLE);
				rlt_top.setVisibility(View.VISIBLE);
				ll_bottom.setVisibility(View.VISIBLE);
				showNameProduct();
			}
		}

	}

	@Override
	public void onUpdatePriceView(View view) {
		if (null != view) {
			ll_price.removeAllViewsInLayout();
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			if (DataLocal.isTablet) {
				params.gravity = Gravity.CENTER_HORIZONTAL;
			} else {
				params.gravity = Gravity.LEFT;
			}
			ll_price.addView(view, params);
		}
	}

	@Override
	public void onUpdateOptionView(View view) {
		if (null != view) {
			OptionFragment fragment = OptionFragment.newInstance(view,
					onDoneOption);
			FragmentTransaction frt = SimiManager.getIntance().getManager()
					.beginTransaction();
			frt.setCustomAnimations(
					Rconfig.getInstance().getId("abc_fade_in", "anim"), Rconfig
							.getInstance().getId("abc_fade_out", "anim"));
			frt.add(Rconfig.getInstance().id("container"), fragment);
			frt.addToBackStack(null);
			frt.commit();
		}

	}

	protected Product getProductFromCollection(SimiCollection collection) {
		Product product = null;
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			product = (Product) entity.get(0);
		}
		return product;
	}

	@Override
	public String[] getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isShown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateViewPager(VerticalViewPager2 viewpager) {
		if (null != mIndicator && null != viewpager
				&& null != viewpager.getAdapter()) {
			mIndicator.setViewPager(viewpager);
			mIndicator.setCurrentItem(0);
		}
	}

	@Override
	public LinearLayout getLayoutMore() {
		return ll_more;
	}

	@Override
	public String getCurrentID() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public int getCurrenPosition() {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public void startAnimation(String urlImage) {
		if (null != img_animation) {
			img_animation.setVisibility(View.VISIBLE);
			DrawableManager.fetchDrawableOnThreadForZTheme(urlImage,
					img_animation);
			img_animation.startAnimation(animation);
			img_animation.setVisibility(View.GONE);
		}
	}
}
