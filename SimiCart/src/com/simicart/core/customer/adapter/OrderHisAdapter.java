package com.simicart.core.customer.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.entity.OrderHistory;

public class OrderHisAdapter extends BaseAdapter {

	protected ArrayList<OrderHistory> mOrderHis;
	protected Context context;
	protected LayoutInflater inflater;

	public OrderHisAdapter(Context context,
			ArrayList<OrderHistory> orderHistories) {
		this.context = context;
		this.mOrderHis = orderHistories;
		this.inflater = LayoutInflater.from(context);
		orderHistories = new ArrayList<OrderHistory>();
	}

	public ArrayList<OrderHistory> getOrderHisList() {
		return this.mOrderHis;
	}

	public void setOrderHis(ArrayList<OrderHistory> orderhis) {
		mOrderHis = orderhis;
	}

	@SuppressLint({ "DefaultLocale", "ViewHolder" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		convertView = this.inflater.inflate(
				Rconfig.getInstance().layout("core_listitem_order_layout"),
				null);
		if (DataLocal.isLanguageRTL) {
			convertView = this.inflater.inflate(
					Rconfig.getInstance().layout("rtl_listitem_order_layout"),
					null);
		}

		// label
		TextView lb_status = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("lb_status"));
		lb_status.setTextColor(Config.getInstance().getContent_color());
		lb_status.setText(Config.getInstance().getText("Order Status"));

		TextView lb_date = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("lb_date"));
		lb_date.setTextColor(Config.getInstance().getContent_color());
		lb_date.setText(Config.getInstance().getText("Order Date"));

		TextView lb_recipient = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("lb_recipient"));
		lb_recipient.setTextColor(Config.getInstance().getContent_color());
		lb_recipient.setText(Config.getInstance().getText("Recipient"));

		TextView lb_items = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("lb_items"));
		lb_items.setTextColor(Config.getInstance().getContent_color());
		lb_items.setText(Config.getInstance().getText("Items"));

		// text
		TextView tv_status = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_status"));
		tv_status.setTextColor(Config.getInstance().getContent_color());
		tv_status.setText(mOrderHis.get(position).getOrder_status()
				.toUpperCase());

		TextView tv_date = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_date"));
		tv_date.setTextColor(Config.getInstance().getContent_color());
		tv_date.setText(mOrderHis.get(position).getOrder_date());

		TextView tv_recipient = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("tv_recipient"));
		tv_recipient.setTextColor(Config.getInstance().getContent_color());
		tv_recipient.setText(mOrderHis.get(position).getRecipient());

		// item
		TextView item1 = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("item1"));
		TextView item2 = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("item2"));
		TextView item3 = (TextView) convertView.findViewById(Rconfig
				.getInstance().id("item3"));
		Drawable bullet = context.getResources().getDrawable(
				Rconfig.getInstance().drawable("core_bullet"));
		bullet.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		item1.setCompoundDrawablesWithIntrinsicBounds(bullet, null, null, null);
		item2.setCompoundDrawablesWithIntrinsicBounds(bullet, null, null, null);
		item3.setCompoundDrawablesWithIntrinsicBounds(bullet, null, null, null);
		item1.setTextColor(Config.getInstance().getContent_color());
		item2.setTextColor(Config.getInstance().getContent_color());
		item3.setTextColor(Config.getInstance().getContent_color());
		if (mOrderHis.get(position).getOrder_items().size() < 1) {
			item1.setVisibility(View.GONE);
			item2.setVisibility(View.GONE);
			item3.setVisibility(View.GONE);
		} else if (mOrderHis.get(position).getOrder_items().size() == 1) {
			item1.setText(mOrderHis.get(position).getOrder_items().get(0));
			item2.setVisibility(View.GONE);
			item3.setVisibility(View.GONE);
		} else if (mOrderHis.get(position).getOrder_items().size() == 2) {
			item1.setText(mOrderHis.get(position).getOrder_items().get(0));
			item2.setText(mOrderHis.get(position).getOrder_items().get(1));
			item3.setVisibility(View.GONE);
		} else {
			item1.setText(mOrderHis.get(position).getOrder_items().get(0));
			item2.setText(mOrderHis.get(position).getOrder_items().get(1));
			item3.setText(".....");
		}

		ImageView im_extend = (ImageView) convertView.findViewById(Rconfig
				.getInstance().id("im_extend"));
		Drawable icon = context.getResources().getDrawable(
				Rconfig.getInstance().drawable("ic_extend"));
		icon.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		im_extend.setImageDrawable(icon);

		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mOrderHis.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
