package com.simicart.core.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.magestore.simicart.R;
import com.simicart.core.catalog.product.entity.PriceV2;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.EventBlock;

public class ProductListAdapter extends BaseAdapter {

	protected ArrayList<Product> mProducts;
	protected Context mContext;
	protected String mTypeProduct;
	public String PRODUCT_PRICE_TYPE_1 = "simple_virtual";
	public String PRODUCT_PRICE_TYPE_2 = "bundle";
	public String PRODUCT_PRICE_TYPE_3 = "grouped";
	public String PRODUCT_PRICE_TYPE_4 = "configurable";
	public ProductListAdapter(Context context, ArrayList<Product> ProductList) {
		mContext = context;
		mProducts = ProductList;
	}

	public ArrayList<Product> getProductList() {
		return mProducts;
	}

	public void setProductList(ArrayList<Product> products) {
		mProducts = products;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Product product = (Product) getItem(position);
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(
							Rconfig.getInstance().layout(
									"core_item_list_search"), null);
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("tv_productItemName"));
			holder.txtName
					.setTextColor(Config.getInstance().getContent_color());
			holder.imageView = (ImageView) convertView.findViewById(Rconfig
					.getInstance().id("iv_productItemImage"));
			holder.layoutStock = (LinearLayout) convertView
					.findViewById(Rconfig.getInstance().id("layout_stock"));
			holder.layoutStock.setBackgroundColor(Config.getInstance()
					.getOut_stock_background());
			ImageView ic_expand = (ImageView) convertView.findViewById(Rconfig
					.getInstance().id("ic_expand"));
			Drawable icon = mContext.getResources().getDrawable(
					Rconfig.getInstance().drawable("ic_extend"));
			icon.setColorFilter(Config.getInstance().getContent_color(),
					PorterDuff.Mode.SRC_ATOP);
			ic_expand.setImageDrawable(icon);
			
			holder.tv_regular_price = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("tv_regular"));
			holder.tv_special_price = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("tv_special"));
			holder.tv_minimal_price = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("tv_minimal"));
			holder.txt_outstock = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("txt_out_stock"));
			holder.txt_outstock.setTextColor(Config.getInstance()
					.getOut_stock_text());
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (DataLocal.isLanguageRTL) {
			holder.txtName.setGravity(Gravity.RIGHT);
		}
		
		if (product != null) {
			mTypeProduct = product.getType();
		}
		if (null != mTypeProduct) {
			mTypeProduct = mTypeProduct.toLowerCase();
		}
	
		if (mTypeProduct == null) {
			mTypeProduct = "simple";
		}
		if (mTypeProduct.equals("simple") || mTypeProduct.equals("virtual")
				|| mTypeProduct.equals("downloadable")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_1)) {
			mTypeProduct = PRODUCT_PRICE_TYPE_1;
			 getViewPriceType1(holder, product);
		} else if (mTypeProduct.equals("bundle")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_2)) {
			mTypeProduct = PRODUCT_PRICE_TYPE_2;
			 getViewPriceType2(holder, product);
		} else if (mTypeProduct.equals("grouped")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_3)) {
			mTypeProduct = PRODUCT_PRICE_TYPE_3;
			 getViewPriceType3(holder, product);
		} else if (mTypeProduct.equals("configurable")
				|| mTypeProduct.equals(PRODUCT_PRICE_TYPE_4)) {
			mTypeProduct = PRODUCT_PRICE_TYPE_4;
			 getViewPriceType1(holder, product);
		}

		holder.txtName.setText(product.getName());

		if (holder.imageView != null && product.getImage() != null) {
			DrawableManager.fetchDrawableOnThread(product.getImage(),
					holder.imageView);
		}
		if (product.getStock() == true) {
			holder.layoutStock.setVisibility(View.GONE);
		} else {
			holder.layoutStock.setVisibility(View.VISIBLE);
			holder.txt_outstock.setText(Config.getInstance().getText("Out Stock"));
		}


		RelativeLayout rl_product_list = (RelativeLayout) convertView
				.findViewById(Rconfig.getInstance().id("rel_product_list"));

		EventBlock eventBlock = new EventBlock();
		eventBlock.dispatchEvent("com.simicart.image.product.list",
				rl_product_list, product);
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView txtName;
		LinearLayout layoutStock;
		TextView txt_outstock;
		LinearLayout ll_price;
		TextView tv_regular_price;
		TextView tv_special_price;
		TextView tv_minimal_price;
	}

	@Override
	public int getCount() {
		return this.mProducts.size();
	}

	@Override
	public Object getItem(int position) {
		return mProducts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	public void getViewPriceType1(ViewHolder holder, Product mProduct) {
		String regular_price = "";
		String special_price = "";
		String minimal_price = "";

		PriceV2 priceV2 = mProduct.getPriceV2();

		// step 1 : display regular price
		float excl_tax = priceV2.getExclTax();
		float incl_tax = priceV2.getInclTax();
		float product_regualar_price = priceV2.getRegularPrice();
		if (excl_tax > -1 && incl_tax > -1) {
			regular_price = getHtmlForPrice(incl_tax);
		} else {
			if (product_regualar_price > -1) {
				regular_price = getHtmlForPrice(product_regualar_price);
			}
		}

		// step 2 : display special price
		float excl_tax_special = priceV2.getExclTaxSpecial();
		float incl_tax_special = priceV2.getInclTaxSpecial();
		float price = priceV2.getPrice();
		if (excl_tax_special > -1 && incl_tax_special > -1) {
			special_price = getHtmlForSpecialPrice(incl_tax_special);
		} else {
			if (price > -1) {
				special_price = getHtmlForSpecialPrice(price);
			}
		}

		// step 3 : display minimal price
		String mimimal_price_label = priceV2.getMinimalPriceLabel();
		float minimalPrice = priceV2.getMinimalPrice();
		if (minimalPrice > -1 && Utils.validateString(mimimal_price_label)) {
			minimal_price = getHtmlForPrice(minimalPrice, mimimal_price_label);
			holder.tv_minimal_price.setText(Html.fromHtml(minimal_price));
		} else {
			holder.tv_minimal_price.setVisibility(View.GONE);
		}

		if (Utils.validateString(special_price)) {
			holder.tv_regular_price.setPaintFlags(holder.tv_regular_price.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
			holder.tv_special_price.setText(Html.fromHtml(special_price));
		} else {
			holder.tv_special_price.setVisibility(View.GONE);
		}

		if (Utils.validateString(regular_price)) {
			holder.tv_regular_price.setText(Html.fromHtml(regular_price));
		}
	}

	public void getViewPriceType2(ViewHolder holder, Product mProduct) {
		PriceV2 priceV2 = mProduct.getPriceV2();

		String minimal_label = priceV2.getMinimalPriceLabel();
		if (Utils.validateString(minimal_label)) {
			// JSON of product has 'minimal_price_label' tag
			holder.tv_special_price.setVisibility(View.GONE);
			holder.tv_minimal_price.setVisibility(View.GONE);
			float excl_tax_minimal = priceV2.getExclTaxMinimal();
			float incl_tax_minimal = priceV2.getInclTaxMinimal();
			if (incl_tax_minimal > -1) {
				holder.tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
						incl_tax_minimal, minimal_label)));
			} else {
				holder.tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
						excl_tax_minimal, minimal_label)));
			}

		} else {

			holder.tv_special_price.setVisibility(View.GONE);
			// JSON of product doesn't has 'minimal_price_label' tag
			float incl_tax_from = priceV2.getInclTaxFrom();
			float incl_tax_to = priceV2.getInclTaxTo();
			float excl_tax_from = priceV2.getExclTaxFrom();
			float excl_tax_to = priceV2.getExclTaxTo();
			String from_text = Config.getInstance().getText("From");
			String to_text = Config.getInstance().getText("To");
			if (incl_tax_from > -1) {
				holder.tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
						incl_tax_from, from_text)));
				holder.tv_minimal_price.setText(Html.fromHtml(getHtmlForPrice(
						incl_tax_to, to_text)));
			} else {
				if (excl_tax_from > -1) {
					holder.tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
							excl_tax_from, from_text)));
					holder.tv_minimal_price.setText(Html.fromHtml(getHtmlForPrice(
							excl_tax_to, to_text)));
				}
			}
		}
	}

	public void getViewPriceType3(ViewHolder holder, Product mProduct) {
		holder.tv_special_price.setVisibility(View.GONE);
		holder.tv_minimal_price.setVisibility(View.GONE);

		PriceV2 priceV2 = mProduct.getPriceV2();

		float incl_tax_minimal = priceV2.getInclTaxMinimal();
		float minimal_price = priceV2.getMinimalPrice();
		String minimal_price_label = priceV2.getMinimalPriceLabel();
		if (incl_tax_minimal > -1) {
			holder.tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
					incl_tax_minimal, minimal_price_label)));
		} else {
			holder.tv_regular_price.setText(Html.fromHtml(getHtmlForPrice(
					minimal_price, minimal_price_label)));
		}
	}

	protected String getHtmlForPrice(float price) {
		return "<font color='" + Config.getInstance().getPrice_color() + "'>"
				+ Config.getInstance().getPrice("" + price) + "</font>";
	}

	protected String getHtmlForPrice(float price, String label) {
		return "<font color='" + Config.getInstance().getPrice_color() + "'>" + label
				+ ": </font><font color='" + Config.getInstance().getPrice_color() + "'>"
				+ Config.getInstance().getPrice("" + price) + "</font>";
	}

	protected String getHtmlForSpecialPrice(float price) {
		return "<font color='" + Config.getInstance().getSpecial_price_color()
				+ "'>" + Config.getInstance().getPrice("" + price) + "</font>";
	}
}
