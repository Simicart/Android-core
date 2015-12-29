package com.simicart.plugins.wishlist.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.delegate.ProductDelegate;
import com.simicart.core.catalog.product.entity.Attributes;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.EventBlock;
import com.simicart.core.style.VerticalViewPager2;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;
import com.simicart.plugins.wishlist.entity.ButtonAddWishList;

public class ProductWishlistBlock extends SimiBlock implements ProductDelegate {

	protected LinearLayout ll_option;
	protected LinearLayout ll_price;
	protected Button btn_addtocart;
	protected LinearLayout ll_image;
	protected String[] Images;
	FragmentManager mFragmentChild;
	FragmentManager mFragmentManager;
	protected MyWishListDelegate mDelegate;
	public static ButtonAddWishList bt_addWish;

	public ButtonAddWishList getBt_addWish() {
		return bt_addWish;
	}

	public void setDelegate(MyWishListDelegate delegate) {
		mDelegate = delegate;
	}

	public void setListenerAddToCart(OnTouchListener listener) {
		btn_addtocart.setOnTouchListener(listener);
	}

	public ProductWishlistBlock(View view, Context context,
			FragmentManager fragmentChild, FragmentManager fragmentManager) {
		super(view, context);
		mFragmentChild = fragmentChild;
		mFragmentManager = fragmentManager;
	}

	@Override
	public void initView() {
		ll_option = (LinearLayout) mView.findViewById(Rconfig.getInstance().id(
				"ll_option"));

		ll_price = (LinearLayout) mView.findViewById(Rconfig.getInstance().id(
				"ll_price"));

		btn_addtocart = (Button) mView.findViewById(Rconfig.getInstance().id(
				"btn_addtocart"));

		ll_image = (LinearLayout) mView.findViewById(Rconfig.getInstance().id(
				"ll_image_product"));

	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			Product product = (Product) entity.get(0);
			showProductDetail(product);
		}
	}

	@SuppressWarnings("deprecation")
	public void showProductDetail(Product product) {
		// name
		String product_name = product.getName();
		TextView tv_title = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_name"));
		tv_title.setText(Html.fromHtml("<b>" + product_name + "</b>"));

		// image
		String[] images = product.getImages();
		Images = images;
//		showImageProduct(images);

		// add button wish list
		addButtonWishList(mContext, product);

		// tv_productdescription
		showProductDescription(product);

		// description
		showDescription(product);

		// tech specs
		ArrayList<Attributes> attributes = product.getAttributes();
		if (null != attributes && attributes.size() > 0) {
			showTechSpecs(attributes);
		}

		// add to cart
		btn_addtocart.setTextColor(Color.WHITE);
		if (product.getStock()) {
			btn_addtocart.setText(Config.getInstance().getText("Add to Cart"));
			GradientDrawable gdDefault = new GradientDrawable();
			gdDefault.setColor(Config.getInstance().getColorMain());
			gdDefault.setCornerRadius(15);
			btn_addtocart.setBackgroundDrawable(gdDefault);
		} else {
			btn_addtocart.setText(Config.getInstance().getText("Out Stock"));
			GradientDrawable gdDefault = new GradientDrawable();
			gdDefault.setColor(Color.LTGRAY);
			gdDefault.setCornerRadius(15);
			btn_addtocart.setBackgroundDrawable(gdDefault);
		}

		RelativeLayout rlt_product_label = (RelativeLayout) mView
				.findViewById(Rconfig.getInstance().id("l_lable"));
		EventBlock eventBlock = new EventBlock();
		eventBlock.dispatchEvent("com.simicart.image.product.detail",
				rlt_product_label, product);

	}

//	private void showImageProduct(String[] images) {
//		AdapterImageProductDetail mAdapter = new AdapterImageProductDetail(
//				mFragmentChild, images, mFragmentManager);
//		ViewPager mPager = (ViewPager) mView.findViewById(Rconfig.getInstance()
//				.id("pager"));
//		mPager.setAdapter(mAdapter);
//		CirclePageIndicator mIndicator = new CirclePageIndicator(mContext);
//		mIndicator.setFillColor(Config.getInstance().getColorMain());
//		mIndicator.setViewPager(mPager);
//		ll_image.addView(mIndicator);
//	}

	protected void showProductDescription(Product product) {
		TextView tv_description = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_shortDescription"));
		String decripition = Html.fromHtml(product.getShortDecripition())
				.toString();
		tv_description.setText(decripition);
	}

	protected void showDescription(Product product) {
		String description = product.getDecripition();
		TextView tv_label = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("tv_label_description"));
		TextView tv_description = (TextView) mView.findViewById(Rconfig
				.getInstance().id("tv_content_description"));

		tv_label.setText(Config.getInstance().getText("Description"));
		tv_description.setText(Html.fromHtml(description));
	}

	protected void showTechSpecs(ArrayList<Attributes> attributes) {
		LinearLayout ll_techSpecs = (LinearLayout) mView.findViewById(Rconfig
				.getInstance().id("ll_techSpecs"));
		TextView tv_label = new TextView(mContext);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_label.setLayoutParams(params);
		tv_label.setText(Config.getInstance().getText("Tech Specs"));
		tv_label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		tv_label.setTypeface(Typeface.DEFAULT_BOLD);
		tv_label.setTextColor(Color.parseColor("#000000"));
		ll_techSpecs.addView(tv_label);

		for (Attributes attributeProduct : attributes) {
			TextView tv_title = new TextView(mContext);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			tv_title.setLayoutParams(lp);
			tv_title.setText(Html.fromHtml(attributeProduct.getTitle()));
			tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			tv_title.setTypeface(null, Typeface.BOLD);
			tv_title.setTextColor(Color.parseColor("#000000"));
			ll_techSpecs.addView(tv_title);

			TextView tv_value = new TextView(mContext);
			tv_value.setLayoutParams(lp);
			if (!attributeProduct.getValue().equals("null")
					&& attributeProduct.getValue() != null) {

				tv_value.setTextColor(Color.parseColor("#000000"));
				tv_value.setText(Html.fromHtml(attributeProduct.getValue()));

			} else {
				tv_value.setText("");
			}
			ll_techSpecs.addView(tv_value);
		}
	}

	private void addButtonWishList(Context context, Product product) {
//		final RelativeLayout layout = (RelativeLayout) mView
//				.findViewById(Rconfig.getInstance().id("rl_lable"));
//
//		bt_addWish = new ButtonAddWishList(context);
//
//		bt_addWish.setEnable(false);
//		((ViewGroup) layout).addView(bt_addWish.getImageAddWishList());
//
//		ProductWishList productWishList = new ProductWishList(product);
//		if (!productWishList.getWishlist_item_id().equals("0")) {
//			bt_addWish.setEnable(false);
//		} else {
//			bt_addWish.setEnable(true);
//		}
//		if (productWishList.getWishlist_item_id().equals("0")
//				|| productWishList.getProduct().getOptions().size() > 0) {
//			bt_addWish.setEnable(true);
//		}
//		ControllerAddWishList controllerAddWishList = new ControllerAddWishList(
//				mContext, bt_addWish, productWishList);
//		controllerAddWishList.setDelegate(mDelegate);
//		controllerAddWishList.setUpdateWishList(true);
//		controllerAddWishList.onAddToWishList();
	}

	@Override
	public void onUpdateOptionView(View view) {
		ll_option.removeAllViewsInLayout();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ll_option.addView(view, params);

	}

	@Override
	public void onUpdatePriceView(View view) {
		ll_price.removeAllViewsInLayout();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		ll_price.addView(view, params);

	}

	@Override
	public String[] getImage() {
		// TODO Auto-generated method stub
		return Images;
	}

	@Override
	public boolean isShown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onVisibleTopBottom(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateViewPager(VerticalViewPager2 viewpager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LinearLayout getLayoutMore() {
		return null;
	}

}
