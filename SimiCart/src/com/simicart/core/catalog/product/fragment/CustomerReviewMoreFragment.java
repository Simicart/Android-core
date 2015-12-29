package com.simicart.core.catalog.product.fragment;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.catalog.product.entity.CustomerReview;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;

public class CustomerReviewMoreFragment extends SimiFragment {
	protected CustomerReview mCustomerReview;

	public void setCustomerReview(CustomerReview mCustomerReview) {
		this.mCustomerReview = mCustomerReview;
	}

	public static CustomerReviewMoreFragment newInstance() {
		CustomerReviewMoreFragment fragment = new CustomerReviewMoreFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(
				Rconfig.getInstance().layout(
						"core_information_customer_review_more"), container,
				false);

		RatingBar ratingBar = (RatingBar) convertView.findViewById(Rconfig
				.getInstance().id("rtb_review"));
		ratingBar.setRating(Float.parseFloat(mCustomerReview.getRate()));
		LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
		stars.getDrawable(2).setColorFilter(
				Config.getInstance().getColorMain(), PorterDuff.Mode.SRC_ATOP);

		TextView title = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_reviewTitle"));
		title.setTextColor(Config.getInstance().getContent_color());
		title.setText(mCustomerReview.getTitle());

		TextView content = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_reviewContent"));
		content.setTextColor(Config.getInstance().getContent_color());
		content.setText(mCustomerReview.getContent());

		TextView date = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_reviewDate"));
		date.setTextColor(Config.getInstance().getContent_color());
		date.setText(mCustomerReview.getTime());

		TextView review_customer = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_nameReviewCustomer"));
		review_customer.setTextColor(Config.getInstance().getContent_color());
		review_customer.setText(Config.getInstance().getText("by") + " "
				+ mCustomerReview.getCustomer_name());

		convertView.setBackgroundColor(Config.getInstance()
				.getApp_backrground());

		return convertView;
	}
}
