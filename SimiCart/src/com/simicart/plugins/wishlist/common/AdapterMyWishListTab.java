package com.simicart.plugins.wishlist.common;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
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
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.PriceV2;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.wishlist.controller.ItemWishListController;
import com.simicart.plugins.wishlist.delegate.MyWishListDelegate;
import com.simicart.plugins.wishlist.delegate.RefreshWishlistDelegate;
import com.simicart.plugins.wishlist.entity.ItemWishList;

public class AdapterMyWishListTab extends BaseAdapter implements RefreshWishlistDelegate{
	protected Context mContext;
	protected ArrayList<ItemWishList> mWishLists = new ArrayList<>();
	protected MyWishListDelegate mDelegate;
	protected TextView tv_exclTax;
	protected TextView tv_inclTax;

	protected ItemWishListController mController;

	public void setDelegate(MyWishListDelegate delegate) {
		mDelegate = delegate;
	}

	public void setWishItems(ArrayList<ItemWishList> items) {
		mWishLists = items;
	}

	public AdapterMyWishListTab(Context context,
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(mContext).inflate(
				Rconfig.getInstance().layout(
						"plugins_wishlist_mywishlist_items"), null);
		ImageView img_avartar = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("im_wishlist"));
		TextView tv_name = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_name"));
		tv_exclTax = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_price1"));
		tv_inclTax = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_price2"));

		final ItemWishList itemWishList = mWishLists.get(position);
		String urlImage = itemWishList.getProduct_image();
		if (null != urlImage) {
			DrawableManager.fetchDrawableOnThread(urlImage, img_avartar);
		}

		String name = itemWishList.getProduct_name();
		if (null != name) {
			tv_name.setText(name);
		}
		
		LinearLayout ll_stock = (LinearLayout) convertView.findViewById(Rconfig.getInstance().id("ll_stock"));
		ll_stock.setBackgroundColor(Config.getInstance().getColorMain());
		TextView txt_out_stock = (TextView) convertView.findViewById(Rconfig.getInstance().id("txt_out_stock"));
		txt_out_stock.setText(Config.getInstance().getText("Out Stock"));
		if(itemWishList.getStock_status().equals(Config.getInstance().getText("Out Stock"))){
			ll_stock.setVisibility(View.VISIBLE);
		}else{
			ll_stock.setVisibility(View.GONE);
		}

		// set price
		if (itemWishList.getmPriceV2() == null) {
			createPrice(itemWishList);
		} else {
			createPriceV2(itemWishList);
		}
		
		// click add cart
		final TextView tv_addcart = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_addcart"));
		if (!itemWishList.getStock_status().equals(
				Config.getInstance().getText("In Stock"))) {
			tv_addcart.setText(itemWishList.getStock_status());
			tv_addcart.setTextColor(Color.WHITE);
			GradientDrawable gdDefault = new GradientDrawable();
			gdDefault.setColor(Color.GRAY);
			tv_addcart.setBackgroundDrawable(gdDefault);
		} else {
			tv_addcart.setText(Config.getInstance().getText("Add To Cart"));
			tv_addcart.setTextColor(Color.WHITE);
			GradientDrawable gdDefault = new GradientDrawable();
			gdDefault.setColor(Config.getInstance().getColorMain());
			tv_addcart.setBackgroundDrawable(gdDefault);

			tv_addcart.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						GradientDrawable gdDefault = new GradientDrawable();
						gdDefault.setColor(Color.GRAY);
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
											"Ok");
						}
					}

					case MotionEvent.ACTION_CANCEL: {
						GradientDrawable gdDefault = new GradientDrawable();
						gdDefault.setColor(Config.getInstance().getColorMain());
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

		ImageView ll_deleteProduct = (ImageView) convertView
				.findViewById(Rconfig.getInstance().id("im_delete"));
		ll_deleteProduct.setColorFilter(Color.parseColor("#c4c4c4"));
		ll_deleteProduct.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
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
					break;
				}
				default:
					break;
				}
				return true;
			}
		});
		
		ImageView ll_shareProduct = (ImageView) convertView
				.findViewById(Rconfig.getInstance().id("im_share"));
		ll_shareProduct.setColorFilter(Color.parseColor("#c4c4c4"));
		ll_shareProduct.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					break;
				}
				case MotionEvent.ACTION_UP: {
					mController.controllerShare(itemWishList.getShare_mes());
				}

				case MotionEvent.ACTION_CANCEL: {
					break;
				}
				default:
					break;
				}
				return true;
			}
		});
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<String> listID = new ArrayList<String>();
				for (int i = 0; i < mWishLists.size(); i++) {
					listID.add(mWishLists.get(i).getProduct_id());
				}
				ProductDetailParentFragment fragment = ProductDetailParentFragment.newInstance();
				fragment.setProductID(itemWishList.getProduct_id());
				fragment.setListIDProduct(listID);
				SimiManager.getIntance().replaceFragment(fragment);
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
	}
	
	private void createPrice(ItemWishList itemWishList) {
		String regular_pr = "<b>"
				+ Config.getInstance().getPrice(
						Float.toString(itemWishList.getProduct_regular_price())) + "</b>";
		tv_exclTax.setText(Html.fromHtml(regular_pr));
		if (itemWishList.getProduct_regular_price() > itemWishList.getProduct_price()) {
			tv_exclTax.setPaintFlags(tv_exclTax.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
			String special_pr = "<b>"
					+ Config.getInstance().getPrice(
							Float.toString(itemWishList.getProduct_price())) + "</b>";
			tv_inclTax.setText(Html.fromHtml(special_pr));
		} else {
			tv_inclTax.setVisibility(View.GONE);
		}
	}
	
	private void createPriceV2(ItemWishList itemWishList) {
		PriceV2 priceV2 = itemWishList.getmPriceV2();
		if (priceV2.getExclTax() != -1 || priceV2.getInclTax() != -1) {
			showPrice("", "Incl. Tax", priceV2.getExclTax(),
					priceV2.getInclTax());
		} else if (priceV2.getExclTaxSpecial() != -1
				|| priceV2.getInclTaxSpecial() != -1) {
			showPrice("", "Incl. Tax", priceV2.getExclTaxSpecial(),
					priceV2.getInclTaxSpecial());
		} else if (priceV2.getExclTaxMinimal() != -1
				|| priceV2.getInclTaxMinimal() != -1) {
			showPrice("", "Incl. Tax", priceV2.getExclTaxMinimal(),
					priceV2.getInclTaxMinimal());
		} else if (priceV2.getExclTaxFrom() != -1
				|| priceV2.getExclTaxTo() != -1) {
			showPrice("From", "To", priceV2.getExclTaxFrom(),
					priceV2.getExclTaxTo());
		} else if (priceV2.getInclTaxFrom() != -1
				|| priceV2.getInclTaxTo() != -1) {
			showPrice("From", "To", priceV2.getInclTaxFrom(),
					priceV2.getInclTaxTo());
		} else if (priceV2.getRegularPrice() != -1 || priceV2.getPrice() != -1) {
			showPriceV2("", "", priceV2.getRegularPrice(), priceV2.getPrice());
		} else if (priceV2.getMinimalPrice() != -1) {
			if (priceV2.getMinimalPriceLabel().equals("")) {
				showPrice("", "", priceV2.getMinimalPrice(), -1);
			} else {
				showPrice(priceV2.getMinimalPriceLabel(), "",
						priceV2.getMinimalPrice(), -1);
			}
		} else {
			createPrice(itemWishList);
		}
	}

	private void showPriceV2(String ex, String ic, float exclTax,
			float inclTax) {

		if (inclTax != -1) {
			String incl_tax_s = "<b>"
					+ Config.getInstance().getPrice("" + inclTax) + "</b>";
			if (ex != null && !ex.equals("")) {
				incl_tax_s = Config.getInstance().getText(ic) + ": " + "<b>"
						+ Config.getInstance().getPrice("" + inclTax) + "</b>";
			}
			tv_inclTax.setText(Html.fromHtml(incl_tax_s));
			tv_exclTax.setPaintFlags(tv_exclTax.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			tv_exclTax.setTextColor(Color.parseColor("#ab452f"));
			tv_inclTax.setVisibility(View.GONE);
		}

		if (exclTax != -1) {
			String excl_tax_s = "<b>"
					+ Config.getInstance().getPrice("" + exclTax) + "</b>";
			if (ex != null && !ex.equals("")) {
				excl_tax_s = Config.getInstance().getText(ex) + ": " + "<b>"
						+ Config.getInstance().getPrice("" + exclTax) + "</b>";
			}
			tv_exclTax.setText(Html.fromHtml(excl_tax_s));
		} else {
			tv_exclTax.setVisibility(View.GONE);
		}
	}
	
	private void showPrice(String ex, String ic, float exclTax, float inclTax) {

		if (inclTax != -1) {
			String incl_tax_s = "<b>"
					+ Config.getInstance().getPrice("" + inclTax) + "</b>";
			if (ic != null && !ic.equals("")) {
				incl_tax_s = Config.getInstance().getText(ic) + ": " + "<b>"
						+ Config.getInstance().getPrice("" + inclTax) + "</b>";
			}
			tv_inclTax.setText(Html.fromHtml(incl_tax_s));
		} else {
			tv_exclTax.setTextColor(Color.parseColor("#ab452f"));
			tv_inclTax.setVisibility(View.GONE);
		}

		if (exclTax != -1) {
			String excl_tax_s = "<b>"
					+ Config.getInstance().getPrice("" + exclTax) + "</b>";
			if (ex != null && !ex.equals("")) {
				excl_tax_s = Config.getInstance().getText(ex) + ": " + "<b>"
						+ Config.getInstance().getPrice("" + exclTax) + "</b>";
			}
			tv_exclTax.setText(Html.fromHtml(excl_tax_s));
		} else {
			tv_exclTax.setVisibility(View.GONE);
		}
	}

	@Override
	public void refreshWishlist(int position) {	
		mWishLists.remove(position);
		notifyDataSetChanged();
	}

}
