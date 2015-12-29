package com.simicart.plugins.instantcontact.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simicart.core.config.Rconfig;
import com.simicart.plugins.instantcontact.entity.ContactUsEntity;

public class ContactUsAdapter extends BaseAdapter {
	private ArrayList<ContactUsEntity> arrContactUs;
	private ContactUsEntity contactUsEntity;
	private Context mContext;
	protected String mColor;

	public ContactUsAdapter(ArrayList<ContactUsEntity> arrContactUs,
			Context mContext) {
		super();
		this.arrContactUs = arrContactUs;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return arrContactUs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrContactUs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater
				.inflate(
						Rconfig.getInstance().layout(
								"plugins_contactus_layout_custom"), parent,
						false);
		View v = convertView.findViewById(Rconfig.getInstance().id(
				"rlt_contactUs"));
		ImageView img_contactUs = (ImageView) v.findViewById(Rconfig
				.getInstance().id("img_contactUs"));
		TextView tv_contactUs = (TextView) v.findViewById(Rconfig.getInstance()
				.id("tv_contactUs"));
		ImageView img_contactUsList = (ImageView) v.findViewById(Rconfig
				.getInstance().id("img_contactUsList"));
		TextView tv_contactUsList = (TextView) v.findViewById(Rconfig
				.getInstance().id("tv_contactUsList"));
		contactUsEntity = arrContactUs.get(position);

		if (contactUsEntity.getStyle().equals("2")) {
			v.setPadding(0, 0, 0, 30);
			img_contactUsList.setVisibility(ImageView.GONE);
			tv_contactUsList.setVisibility(TextView.GONE);
			img_contactUs.setVisibility(ImageView.VISIBLE);
			tv_contactUs.setVisibility(TextView.VISIBLE);
			mColor = contactUsEntity.getActiveColor();
			Drawable ic_message = mContext.getResources().getDrawable(
					contactUsEntity.getImageContactUs());
			if (!mColor.contains("#")) {
				mColor = "#" + mColor;
			}
			ic_message.setColorFilter(Color.parseColor(mColor),
					PorterDuff.Mode.SRC_ATOP);
			img_contactUs.setImageDrawable(ic_message);
			img_contactUs.setBackgroundResource(contactUsEntity
					.getImageContactUs());
			tv_contactUs.setText(contactUsEntity.getNameContactUs());
		} else {
			img_contactUs.setVisibility(ImageView.GONE);
			tv_contactUs.setVisibility(TextView.GONE);
			img_contactUsList.setVisibility(ImageView.VISIBLE);
			tv_contactUsList.setVisibility(TextView.VISIBLE);
			mColor = contactUsEntity.getActiveColor();
			Drawable ic_message = mContext.getResources().getDrawable(
					contactUsEntity.getImageContactUs());
			if (!mColor.contains("#")) {
				mColor = "#" + mColor;
			}
			ic_message.setColorFilter(Color.parseColor(mColor),
					PorterDuff.Mode.SRC_ATOP);
			img_contactUsList.setImageDrawable(ic_message);
			img_contactUsList.setBackgroundResource(contactUsEntity
					.getImageContactUs());
			tv_contactUsList.setText(contactUsEntity.getNameContactUs());
		}
		return convertView;
	}

}
