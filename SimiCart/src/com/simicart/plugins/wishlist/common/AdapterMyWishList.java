package com.simicart.plugins.wishlist.common;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.checkout.entity.Option;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.wishlist.controller.ItemWishListController;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;
import com.simicart.plugins.wishlist.delegate.RefreshWishlistDelegate;
import com.simicart.plugins.wishlist.entity.ItemWishList;

@SuppressLint({ "ClickableViewAccessibility", "ViewHolder" })
public class AdapterMyWishList extends BaseAdapter implements RefreshWishlistDelegate{

	protected Context mContext;
	protected ArrayList<ItemWishList> mWishLists = new ArrayList<>();
	protected MyWishListDelegate mDelegate;

	protected ItemWishListController mController;

	public void setDelegate(MyWishListDelegate delegate) {
		mDelegate = delegate;
	}

	public void setWishItems(ArrayList<ItemWishList> items) {
		mWishLists = items;
	}

	public AdapterMyWishList(Context context,
			ArrayList<ItemWishList> wishLists, ItemWishListController controller) {
		mContext = context;
		mWishLists = wishLists;
		mController = controller;
		mController.setRefreshWishlistDelegate(this);
	}

	@Override
	public int getCount() {
		return mWishLists.size();
	}

	@Override
	public Object getItem(int position) {
		return mWishLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(mContext).inflate(
				Rconfig.getInstance().layout(
						"plugins_wishlist_layout_item_mywishlist"), null);

		ImageView img_avartar = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("img_item_wishlist"));
		TextView tv_name = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_name"));
		TextView tv_price1 = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_price1"));
		TextView tv_price2 = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_price2"));
		tv_price2.setVisibility(View.GONE);

		TextView lbl_option1 = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("lbl_option1"));
		TextView lbl_option2 = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("lbl_option2"));
		TextView tv_option1 = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_option1"));
		TextView tv_option2 = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_option2"));
		TextView tv_option3 = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_option3"));

		TextView tv_stock = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_stock"));

		final ItemWishList itemWishList = mWishLists.get(position);
		String urlImage = itemWishList.getProduct_image();
		if (null != urlImage) {
			DrawableManager.fetchDrawableOnThread(urlImage, img_avartar);
		}

		String name = itemWishList.getProduct_name();
		if (null != name) {
			tv_name.setText(name);
		}

		// set options
		ArrayList<Option> options = itemWishList.getmOptions();
		if (null != options && options.size() > 0) {
			if (options.size() == 1) {
				Option option = options.get(0);
				lbl_option1.setVisibility(View.VISIBLE);
				tv_option1.setVisibility(View.VISIBLE);
				lbl_option1.setText(option.getOption_title());
				tv_option1.setText(option.getOption_value());
			} else if (options.size() == 2) {
				Option option = options.get(0);
				lbl_option1.setVisibility(View.VISIBLE);
				tv_option1.setVisibility(View.VISIBLE);
				lbl_option1.setText(option.getOption_title());
				tv_option1.setText(option.getOption_value());
				option = options.get(1);
				lbl_option2.setVisibility(View.VISIBLE);
				tv_option2.setVisibility(View.VISIBLE);
				lbl_option2.setText(option.getOption_title());
				tv_option2.setText(option.getOption_value());
			} else {
				Option option = options.get(0);
				lbl_option1.setVisibility(View.VISIBLE);
				tv_option1.setVisibility(View.VISIBLE);
				lbl_option1.setText(option.getOption_title());
				tv_option1.setText(option.getOption_value());
				option = options.get(1);
				lbl_option2.setVisibility(View.VISIBLE);
				tv_option2.setVisibility(View.VISIBLE);
				lbl_option2.setText(option.getOption_title());
				tv_option2.setText(option.getOption_value());
				tv_option3.setVisibility(View.VISIBLE);
				tv_option3.setText("........");
			}
		}

		// set price
		tv_price1.setText(Config.getInstance().getPrice(
				Float.toString(itemWishList.getProduct_price())));
		tv_price1.setTextColor(Config.getInstance().getColorPrice());
		tv_price2.setVisibility(View.GONE);

		String stock = itemWishList.getStock_status();
		if (null != stock) {
			tv_stock.setText(stock);
		}
		tv_stock.setVisibility(View.GONE);

		// click add cart
		final TextView tv_addcart = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_addcart"));
		if (!itemWishList.getStock_status().equals(
				Config.getInstance().getText("In Stock"))) {
			tv_addcart.setText(itemWishList.getStock_status());
			tv_addcart.setTextColor(Color.WHITE);
			GradientDrawable gdDefault = new GradientDrawable();
			gdDefault.setColor(Color.GRAY);
			gdDefault.setCornerRadius(2);
			tv_addcart.setBackgroundDrawable(gdDefault);
		} else {
			tv_addcart.setText(Config.getInstance().getText("Add To Cart"));
			tv_addcart.setTextColor(Color.WHITE);
			GradientDrawable gdDefault = new GradientDrawable();
			gdDefault.setColor(Config.getInstance().getColorMain());
			gdDefault.setCornerRadius(2);
			tv_addcart.setBackgroundDrawable(gdDefault);

			tv_addcart.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						GradientDrawable gdDefault = new GradientDrawable();
						gdDefault.setColor(Color.GRAY);
						gdDefault.setCornerRadius(2);
						tv_addcart.setBackgroundDrawable(gdDefault);
						break;
					}
					case MotionEvent.ACTION_UP: {
						if (itemWishList.isSelected_all_required_options()) {

							String addID = itemWishList.getWishlist_item_id();
							if (DataLocal.isTablet) {
								String showID = null;
								if (position != (mWishLists.size() - 1)) {
									ItemWishList nextItem = mWishLists
											.get(position + 1);
									if (null != nextItem) {
										showID = nextItem.getProduct_id();
									}
								} else {
									ItemWishList nextItem = mWishLists.get(0);
									if (null != nextItem) {
										showID = nextItem.getProduct_id();
									}
								}
								mController.controllerAddAndShowNext(addID,
										showID, itemWishList.getProduct_id());
							} else {
								mController.controllerAddToCart(addID,
										itemWishList.getProduct_id(),position);
							}

						} else {
							SimiManager
									.getIntance()
									.showNotify(
											null,
											"The product is not selected all options requirement.",
											"Yes");
						}
					}

					case MotionEvent.ACTION_CANCEL: {
						GradientDrawable gdDefault = new GradientDrawable();
						gdDefault.setColor(Config.getInstance().getColorMain());
						gdDefault.setCornerRadius(2);
						tv_addcart.setBackgroundDrawable(gdDefault);
						break;
					}
					default:
						break;
					}
					return true;
				}
			});
		}

		final LinearLayout ll_more_show = (LinearLayout) convertView
				.findViewById(Rconfig.getInstance().id("ll_more_show"));
		final ImageView im_arrow = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("im_arrow"));
		final ImageView im_more = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("im_more"));
		final TextView tv_moreOption = (TextView) convertView
				.findViewById(Rconfig.getInstance().id("tv_moreOption"));
		tv_moreOption.setText(Config.getInstance().getText("More options"));
		LinearLayout ll_moreOption = (LinearLayout) convertView
				.findViewById(Rconfig.getInstance().id("ll_moreOption"));
		ll_moreOption.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ll_more_show.getVisibility() == View.VISIBLE) {
					ll_more_show.setVisibility(View.GONE);
					im_arrow.setVisibility(View.GONE);
					im_more.setRotation(0);
					notifyDataSetChanged();
				} else {
					ll_more_show.setVisibility(View.VISIBLE);
					im_arrow.setVisibility(View.VISIBLE);
					im_more.setRotation(180);
				}
			}
		});

		TextView tv_shareProduct = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_shareProduct"));
		tv_shareProduct.setText(Config.getInstance().getText("Share"));

		final LinearLayout ll_shareProduct = (LinearLayout) convertView
				.findViewById(Rconfig.getInstance().id("ll_shareProduct"));
		ll_shareProduct.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					ll_shareProduct.setBackgroundColor(Color.GRAY);
					break;
				}
				case MotionEvent.ACTION_UP: {
					mController.controllerShare(itemWishList.getShare_mes());
				}

				case MotionEvent.ACTION_CANCEL: {
					ll_shareProduct.setBackgroundResource(Rconfig.getInstance()
							.drawable("core_background_cart_popup"));
					break;
				}
				default:
					break;
				}
				return true;
			}
		});

		Drawable icon = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("plugins_wishlist_ic_delete"));
		icon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
		ImageView im_delete = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("im_delete"));
		im_delete.setBackgroundDrawable(icon);
		TextView tv_deleteProduct = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_deleteProduct"));
		tv_deleteProduct.setText(Config.getInstance().getText("Remove"));

		final LinearLayout ll_deleteProduct = (LinearLayout) convertView
				.findViewById(Rconfig.getInstance().id("ll_deleteProduct"));
		ll_deleteProduct.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					ll_deleteProduct.setBackgroundColor(Color.GRAY);
					break;
				}
				case MotionEvent.ACTION_UP: {
					AlertDialog.Builder alertboxDowload = new AlertDialog.Builder(
							mContext);
					alertboxDowload.setMessage(Config.getInstance().getText(
							"Are you sure you want to remove?"));
					alertboxDowload.setCancelable(false);
					alertboxDowload.setPositiveButton(Config.getInstance()
							.getText("Yes"),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									onRemoveProduct(itemWishList, position);
								}
							});
					alertboxDowload.setNegativeButton(Config.getInstance()
							.getText("No"),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
					alertboxDowload.show();
				}

				case MotionEvent.ACTION_CANCEL: {
					ll_deleteProduct.setBackgroundResource(Rconfig
							.getInstance().drawable(
									"core_background_cart_popup"));
					break;
				}
				default:
					break;
				}
				return true;
			}
		});

		RelativeLayout rl_product_wishlist = (RelativeLayout) convertView
				.findViewById(Rconfig.getInstance().id("rl_product_wishlist"));
		// click item
		img_avartar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<String> listID = new ArrayList<String>();
				for (int i = 0; i < mWishLists.size(); i++) {
					listID.add(mWishLists.get(i).getProduct_id());
				}
				ProductDetailParentFragment fragment = ProductDetailParentFragment
						.newInstance();
				fragment.setProductID(itemWishList.getProduct_id());
				fragment.setListIDProduct(listID);
				SimiManager.getIntance().removeDialog();
				SimiManager.getIntance().replaceFragment(fragment);
			}
		});
		// truong 07-09-2015
		ImageView img_share_wishlist = (ImageView) convertView
				.findViewById(Rconfig.getInstance().id("img_share_wishlist"));
		ImageView img_remove_wishlist = (ImageView) convertView
				.findViewById(Rconfig.getInstance().id("img_remove_wishlist"));
		img_share_wishlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mController.controllerShare(itemWishList.getShare_mes());
			}
		});
		img_remove_wishlist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertboxDowload = new AlertDialog.Builder(
						mContext);
				alertboxDowload.setMessage(Config.getInstance().getText(
						"Are you sure you want to remove?"));
				alertboxDowload.setCancelable(false);
				alertboxDowload.setPositiveButton(
						Config.getInstance().getText("Yes"),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								onRemoveProduct(itemWishList, position);
							}
						});
				alertboxDowload.setNegativeButton(
						Config.getInstance().getText("No"),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				alertboxDowload.show();
			}
		});
		return convertView;
	}

	protected void onRemoveProduct(ItemWishList itemWishList, int position) {
		String deletedID = itemWishList.getWishlist_item_id();

		Log.e("AdapterMyWishList ", "onRemoveProduct : " + deletedID);

		if (DataLocal.isTablet) {
			String nextID = null;
			if (position != (mWishLists.size() - 1)) {
				ItemWishList nextItem = mWishLists.get(position + 1);
				if (null != nextItem) {
					nextID = nextItem.getProduct_id();
				}
			} else {
				ItemWishList nextItem = mWishLists.get(0);
				if (null != nextItem) {
					nextID = nextItem.getProduct_id();
				}
			}
			mController.controllerRemoveAndShowNext(deletedID, nextID);
		} else {
			mController.controllerRemoveItem(deletedID);
		}
		mWishLists.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public void refreshWishlist(int position) {
		mWishLists.remove(position);
		notifyDataSetChanged();
	}
}
