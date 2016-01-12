package com.simicart.core.catalog.search.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.common.price.ProductPriceViewProductGridV03;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.EventBlock;

public class GridViewProductListApdapter extends BaseAdapter {

	class ViewHolder {
		private TextView tv_name;
		private ImageView img_avartar;
		private LinearLayout layout_stock;
		private RelativeLayout layout_header;
		private LinearLayout ll_price;
		private TextView txt_outstock;
		private TextView txt_price_tablet;
	}

	private Context mContext;
	private ArrayList<Product> mListProduct;
	private LayoutInflater mInflater;
	private ArrayList<String> listId;
	private int numColum = 0;
	private RelativeLayout layout_image;
	private float withScreen;
	private RelativeLayout layout_header;

	public GridViewProductListApdapter(Context context,
			ArrayList<Product> listProduct, ArrayList<String> listId,
			int numcolumn) {
		mContext = context;
		mListProduct = listProduct;
		this.listId = listId;
		this.numColum = numcolumn;
		getDimension();
	}

	void getDimension() {
		Display display = SimiManager.getIntance().getCurrentActivity()
				.getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		float density = SimiManager.getIntance().getCurrentActivity()
				.getResources().getDisplayMetrics().density;
		float dpHeight = outMetrics.heightPixels / density;
		float dpWidth = outMetrics.widthPixels / density;
		this.withScreen = dpWidth;
	}

	public ArrayList<Product> getListProduct() {
		return mListProduct;
	}

	public void setListProduct(ArrayList<Product> products) {
		mListProduct = products;
	}

	@Override
	public int getCount() {
		return mListProduct.size();
	}

	@Override
	public Object getItem(int position) {
		return mListProduct.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;
		if (convertView == null) {
			if (numColum == 4) {
				if (DataLocal.isTablet) {
					convertView = mInflater.inflate(Rconfig.getInstance()
							.layout("core_item_gridview_productcategory"),
							null, false);
				} else {
					convertView = mInflater
							.inflate(
									Rconfig.getInstance()
											.layout("core_item_gridview_productcategory_change"),
									null, false);
				}
			} else {
				if (DataLocal.isTablet) {
					convertView = mInflater
							.inflate(
									Rconfig.getInstance()
											.layout("core_item_gridview_productcategory_change"),
									null, false);
				} else {
					convertView = mInflater.inflate(Rconfig.getInstance()
							.layout("core_item_gridview_productcategory"),
							null, false);
				}
			}
			layout_image = (RelativeLayout) convertView.findViewById(Rconfig
					.getInstance().id("rel_product_list"));
			LinearLayout.LayoutParams paramsLayout2 = new LinearLayout.LayoutParams(
					Utils.getValueDp((int) ((withScreen - 22) / 2)),
					Utils.getValueDp((int) ((withScreen - 22) / 2)));
			LinearLayout.LayoutParams paramsLayout4Phone = new LinearLayout.LayoutParams(
					Utils.getValueDp((int) ((withScreen - 20) / 4)),
					Utils.getValueDp((int) ((withScreen - 20) / 4)));
			LinearLayout.LayoutParams paramsLayout4Tablet = new LinearLayout.LayoutParams(
					Utils.getValueDp((int) ((withScreen - 20) / 4)),
					Utils.getValueDp((int) ((withScreen - 20) / 4))
							+ Utils.getValueDp(100));
			LinearLayout.LayoutParams paramsLayout6 = new LinearLayout.LayoutParams(
					Utils.getValueDp((int) ((withScreen - 20) / 6)),
					Utils.getValueDp((int) ((withScreen - 20) / 6)));
			if (numColum == 2) {
				layout_image.setLayoutParams(paramsLayout2);
			} else if (numColum == 4) {
				if (DataLocal.isTablet) {
					layout_image.setLayoutParams(paramsLayout4Phone);
				} else {
					layout_image.setLayoutParams(paramsLayout4Phone);
				}
			} else {
				layout_image.setLayoutParams(paramsLayout6);
			}
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("tv_name"));
			holder.tv_name.setTextColor(Config.getInstance()
					.getContent_color());
			holder.ll_price = (LinearLayout) convertView.findViewById(Rconfig
					.getInstance().id("ll_price"));
			holder.img_avartar = (ImageView) convertView.findViewById(Rconfig
					.getInstance().id("img_avartar"));
			holder.layout_stock = (LinearLayout) convertView
					.findViewById(Rconfig.getInstance().id("layout_stock"));
			holder.layout_stock.setBackgroundColor(Config.getInstance()
					.getOut_stock_background());

			holder.layout_header = (RelativeLayout) convertView
					.findViewById(Rconfig.getInstance()
							.id("layout_header_grid"));
			holder.txt_outstock = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("txt_out_stock"));
			holder.txt_outstock.setTextColor(Config.getInstance()
					.getOut_stock_text());
			if (DataLocal.isTablet) {
				holder.txt_price_tablet = (TextView) convertView
						.findViewById(Rconfig.getInstance().id("tv_excl"));
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (numColum == 2) {
			if (pos < 2) {
				holder.layout_header.setVisibility(View.VISIBLE);
			} else {
				holder.layout_header.setVisibility(View.GONE);
			}
		} else if (numColum == 4) {
			if (pos < 4) {
				holder.layout_header.setVisibility(View.VISIBLE);
			} else {
				holder.layout_header.setVisibility(View.GONE);
			}
		} else if (numColum == 6) {
			if (pos < 6) {
				holder.layout_header.setVisibility(View.VISIBLE);
			} else {
				holder.layout_header.setVisibility(View.GONE);
			}
		}

		Product product = mListProduct.get(pos);
		if (DataLocal.isTablet) {
			holder.txt_price_tablet.setText(Config.getInstance().getPrice(
					product.getPrice() + ""));
		}

		if (product.getStock() == true) {
			holder.layout_stock.setVisibility(View.GONE);
		} else {
			holder.layout_stock.setVisibility(View.VISIBLE);
			holder.txt_outstock.setText(Config.getInstance().getText(
					"Out Stock"));
		}

		ProductPriceViewProductGridV03 price_view = new ProductPriceViewProductGridV03(
				product);
		price_view.setShowOnePrice(true);
		View view = price_view.getViewPrice();
		if (null != view && null != holder.ll_price) {
			holder.ll_price.removeAllViewsInLayout();
			holder.ll_price.addView(view);
		}

		String name = product.getName();
		if (null != name) {
			name.trim();
			holder.tv_name.setText(name.toUpperCase());
		} else {
			holder.tv_name.setText("SimiCart");
		}

		if (product.getImage() != null) {
			DrawableManager.fetchDrawableOnThread(product.getImage(),
					holder.img_avartar);
		}

		RelativeLayout rl_product_list = (RelativeLayout) convertView
				.findViewById(Rconfig.getInstance().id("rel_product_list"));

		EventBlock eventBlock = new EventBlock();
		if(numColum == 4) {
			eventBlock.dispatchEvent("com.simicart.image.product.grid4col",
					rl_product_list, mListProduct.get(pos));
		} else {
			eventBlock.dispatchEvent("com.simicart.image.product.grid",
					rl_product_list, mListProduct.get(pos));
		}
		return convertView;
	}
}
