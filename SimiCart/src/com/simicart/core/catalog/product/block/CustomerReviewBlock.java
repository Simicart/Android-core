package com.simicart.core.catalog.product.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.simicart.core.adapter.CustomerReviewAdapter;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.catalog.product.delegate.CustomerReviewDelegate;
import com.simicart.core.catalog.product.entity.CustomerReview;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class CustomerReviewBlock extends SimiBlock implements
		CustomerReviewDelegate {

	protected ListView lv_customerReview;
	CustomerReviewAdapter mAdapter;
	protected View mLoad;
	protected ArrayList<CustomerReview> mReviews;
	protected Product mProduct;

	public void setProduct(Product mProduct) {
		this.mProduct = mProduct;
	}

	public CustomerReviewBlock(View view, Context context) {
		super(view, context);
	}

	public void setonScroll(OnScrollListener scroller) {
		lv_customerReview.setOnScrollListener(scroller);
	}

	@Override
	public void initView() {
		lv_customerReview = (ListView) mView.findViewById(Rconfig.getInstance()
				.id("lv_customerReview"));
		ColorDrawable sage = new ColorDrawable(Config.getInstance()
				.getLine_color());
		lv_customerReview.setDivider(sage);
		lv_customerReview.setDividerHeight(1);
		mReviews = new ArrayList<CustomerReview>();
	}

	@Override
	public void drawView(SimiCollection collection) {
		super.drawView(collection);

		ArrayList<SimiEntity> entiy = collection.getCollection();

		for (int i = mReviews.size(); i < entiy.size(); i++) {
			CustomerReview review = (CustomerReview) entiy.get(i);
			mReviews.add(review);
		}

		if (null == mAdapter) {
			mAdapter = new CustomerReviewAdapter(mContext, 0, mReviews);
			lv_customerReview.setAdapter(mAdapter);
		} else {
			// mAdapter.setmCustomerReview(mReviews);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onUpdateHeaderView(ArrayList<Integer> mRatingStar) {
		View header = SimiManager
				.getIntance()
				.getCurrentActivity()
				.getLayoutInflater()
				.inflate(
						Rconfig.getInstance()
								.layout("core_information_customer_review_header_layout"),
						null, false);
		RatingBar ratingBar = (RatingBar) header.findViewById(Rconfig
				.getInstance().id("rtb_reviewHeader"));
		// change star color
		LayerDrawable starcolor = (LayerDrawable) ratingBar
				.getProgressDrawable();
		starcolor.getDrawable(2).setColorFilter(
				Config.getInstance().getColorMain(), PorterDuff.Mode.SRC_ATOP);

		int starQuantity1 = mRatingStar.get(0);
		int starQuantity2 = mRatingStar.get(1);
		int starQuantity3 = mRatingStar.get(2);
		int starQuantity4 = mRatingStar.get(3);
		int starQuantity5 = mRatingStar.get(4);
		float rating = 0;
		int total = starQuantity1 + starQuantity2 + starQuantity3
				+ starQuantity4 + starQuantity5;
		if (total != 0) {
			rating = (float) ((float) (starQuantity1 + starQuantity2 * 2
					+ starQuantity3 * 3 + starQuantity4 * 4 + starQuantity5 * 5) / total);
		}
		ratingBar.setRating(rating);

		// ProgressBar 5
		ProgressBar pr5 = (ProgressBar) header.findViewById(Rconfig
				.getInstance().id("progressBar5"));
		// set color
		LayerDrawable process5 = (LayerDrawable) pr5.getProgressDrawable();
		process5.getDrawable(2).setColorFilter(
				Config.getInstance().getColorMain(), PorterDuff.Mode.SRC_ATOP);
		TextView tv_starQuantity5 = (TextView) header.findViewById(Rconfig
				.getInstance().id("tv_quantity5"));

		tv_starQuantity5.setText("" + starQuantity5);
		pr5.setProgress(total < 1 ? 0 : starQuantity5 * 100 / total);

		// ProgressBar 4
		ProgressBar pr4 = (ProgressBar) header.findViewById(Rconfig
				.getInstance().id("progressBar4"));
		LayerDrawable process4 = (LayerDrawable) pr4.getProgressDrawable();
		process4.getDrawable(2).setColorFilter(
				Config.getInstance().getColorMain(), PorterDuff.Mode.SRC_ATOP);

		TextView tv_starQuantity4 = (TextView) header.findViewById(Rconfig
				.getInstance().id("tv_quantity4"));
		tv_starQuantity4.setText("" + starQuantity4);
		pr4.setProgress(total < 1 ? 0 : starQuantity4 * 100 / total);

		// ProgressBar 3
		ProgressBar pr3 = (ProgressBar) header.findViewById(Rconfig
				.getInstance().id("progressBar3"));
		// set color
		LayerDrawable process3 = (LayerDrawable) pr3.getProgressDrawable();
		process3.getDrawable(2).setColorFilter(
				Config.getInstance().getColorMain(), PorterDuff.Mode.SRC_ATOP);

		TextView tv_starQuantity3 = (TextView) header.findViewById(Rconfig
				.getInstance().id("tv_quantity3"));
		tv_starQuantity3.setText("" + starQuantity3);
		pr3.setProgress(total < 1 ? 0 : starQuantity3 * 100 / total);

		// ProgressBar 2
		ProgressBar pr2 = (ProgressBar) header.findViewById(Rconfig
				.getInstance().id("progressBar2"));
		// set color
		LayerDrawable process2 = (LayerDrawable) pr2.getProgressDrawable();
		process2.getDrawable(2).setColorFilter(
				Config.getInstance().getColorMain(), PorterDuff.Mode.SRC_ATOP);
		TextView tv_quantity2 = (TextView) header.findViewById(Rconfig
				.getInstance().id("tv_quantity2"));
		tv_quantity2.setText("" + starQuantity2);
		pr2.setProgress(total < 1 ? 0 : starQuantity2 * 100 / total);

		// ProgressBar 1
		ProgressBar pr1 = (ProgressBar) header.findViewById(Rconfig
				.getInstance().id("progressBar1"));
		// set color
		LayerDrawable process1 = (LayerDrawable) pr1.getProgressDrawable();
		process1.getDrawable(2).setColorFilter(
				Config.getInstance().getColorMain(), PorterDuff.Mode.SRC_ATOP);
		TextView tv_quantity1 = (TextView) header.findViewById(Rconfig
				.getInstance().id("tv_quantity1"));
		tv_quantity1.setText("" + starQuantity1);

		TextView star5 = (TextView) header.findViewById(Rconfig.getInstance()
				.id("tv_star5"));
		star5.setText("5 " + Config.getInstance().getText("Star"));

		TextView star4 = (TextView) header.findViewById(Rconfig.getInstance()
				.id("tv_star4"));
		star4.setText("4 " + Config.getInstance().getText("Star"));

		TextView star3 = (TextView) header.findViewById(Rconfig.getInstance()
				.id("tv_star3"));
		star3.setText("3 " + Config.getInstance().getText("Star"));

		TextView star2 = (TextView) header.findViewById(Rconfig.getInstance()
				.id("tv_star2"));
		star2.setText("2 " + Config.getInstance().getText("Star"));

		TextView star1 = (TextView) header.findViewById(Rconfig.getInstance()
				.id("tv_star1"));
		star1.setText("1 " + Config.getInstance().getText("Star"));

		TextView tv_title = (TextView) header.findViewById(Rconfig
				.getInstance().id("tv_titleHeader"));
		tv_title.setTextColor(Config.getInstance().getContent_color());
		tv_title.setText(mProduct.getName());

		TextView txt_stock = (TextView) header.findViewById(Rconfig
				.getInstance().id("txt_stock"));
		txt_stock.setTextColor(Config.getInstance().getContent_color());
		if (mProduct.getStock()) {
			txt_stock.setText(Config.getInstance().getText("In Stock"));
		} else {
			txt_stock.setText(Config.getInstance().getText("Out Stock"));
		}

		TextView tvTitle = (TextView) header.findViewById(Rconfig.getInstance()
				.id("txt_titleReview"));
		tvTitle.setText(Config.getInstance().getText("Customer Reviews"));
		tvTitle.setTextColor(Config.getInstance().getContent_color());

		lv_customerReview.addHeaderView(header);
	}

	@Override
	public void addFooterView() {
		LayoutInflater inflater = (LayoutInflater) lv_customerReview
				.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLoad = inflater.inflate(
				Rconfig.getInstance().layout("core_loading_list"), null, false);
		removeFooterView();
		lv_customerReview.post(new Runnable() {
			@Override
			public void run() {
				lv_customerReview.addFooterView(mLoad);

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
					int lastViewedPosition = lv_customerReview
							.getFirstVisiblePosition();
					View v = lv_customerReview.getChildAt(0);
					int topOffset = (v == null) ? 0 : v.getTop();

					lv_customerReview.setAdapter(mAdapter);
					lv_customerReview.setSelectionFromTop(lastViewedPosition,
							topOffset);
				}
			}
		});
	}

	@Override
	public void removeFooterView() {
		lv_customerReview.post(new Runnable() {
			@Override
			public void run() {
				while (lv_customerReview.getFooterViewsCount() > 0) {
					lv_customerReview.removeFooterView(mLoad);
				}
			}
		});
	}
}
