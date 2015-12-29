package com.simicart.plugins.instantcontact.block;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.instantcontact.adapter.ContactUsAdapter;
import com.simicart.plugins.instantcontact.delegate.ContactUsDelegate;
import com.simicart.plugins.instantcontact.entity.ContactUsEntity;

public class ContactUsBlock extends SimiBlock implements ContactUsDelegate {
	protected ContactUsEntity mContactUsEntity;
	protected ArrayList<ContactUsEntity> listContactUs = new ArrayList<>();
	protected int mStyle;
	protected String mColor;
	protected GridView grid_contactUs;
	protected ContactUsAdapter adapter;
	protected OnItemClickListener gridViewOnItemClick;

	public ContactUsBlock(View view, Context context) {
		super(view, context);
	}

	public void setGridViewOnItemClick(OnItemClickListener gridViewOnItemClick) {
		this.gridViewOnItemClick = gridViewOnItemClick;
	}

	@Override
	public void initView() {

		grid_contactUs = (GridView) mView.findViewById(Rconfig.getInstance()
				.id("grid_contactUs"));
	}

	@Override
	public void drawView(SimiCollection collection) {
		ContactUsEntity contact = (ContactUsEntity) collection.getCollection()
				.get(0);
		if (null != contact) {
			// showContactUs(contact);
			if (contact.getStyle().equals("2")) {
				grid_contactUs.setNumColumns(2);// 2
				showContactUsCustomize2(contact);
			} else {
				grid_contactUs.setNumColumns(1);// 1
				showContactUsCustomize1(contact);
			}

		} else {
			((RelativeLayout) mView).removeAllViewsInLayout();
			TextView tv_message = new TextView(mView.getContext());
			tv_message.setText(Config.getInstance().getText(
					"No information about contact"));
			((RelativeLayout) mView).addView(tv_message);
		}
	}

	protected void showContactUsCustomize1(final ContactUsEntity contact) {
		if (contact.getEmail().size() > 0) {
			listContactUs.add(new ContactUsEntity(contact.getEmail(), contact
					.getPhone(), contact.getMessage(), contact.getWebsite(),
					contact.getActiveColor(), contact.getStyle(), Rconfig
							.getInstance().drawable(
									"plugins_contactusemail_list"), "Email"));
		}
		if (contact.getMessage().size() > 0) {
			listContactUs
					.add(new ContactUsEntity(contact.getEmail(), contact
							.getPhone(), contact.getMessage(), contact
							.getWebsite(), contact.getActiveColor(), contact
							.getStyle(), Rconfig.getInstance().drawable(
							"plugins_contactusmessage_list"), "Message"));
		}
		ArrayList<String> a = contact.getPhone();
		System.out.println(a);
		if (contact.getPhone().size() > 0) {
			listContactUs.add(new ContactUsEntity(contact.getEmail(), contact
					.getPhone(), contact.getMessage(), contact.getWebsite(),
					contact.getActiveColor(), contact.getStyle(), Rconfig
							.getInstance().drawable(
									"plugins_contactusphone_list"), "Call"));
		}
		if (contact.getWebsite().length() > 0) {
			listContactUs.add(new ContactUsEntity(contact.getEmail(), contact
					.getPhone(), contact.getMessage(), contact.getWebsite(),
					contact.getActiveColor(), contact.getStyle(), Rconfig
							.getInstance()
							.drawable("plugins_contactusweb_list"), "Website"));
		}
		if (listContactUs != null) {
			adapter = new ContactUsAdapter(listContactUs, mContext);
			grid_contactUs.setAdapter(adapter);
			grid_contactUs.setOnItemClickListener(gridViewOnItemClick);
		}
	}

	protected void showContactUsCustomize2(final ContactUsEntity contact) {
		if (contact.getEmail().size() > 0) {
			listContactUs.add(new ContactUsEntity(contact.getEmail(), contact
					.getPhone(), contact.getMessage(), contact.getWebsite(),
					contact.getActiveColor(), contact.getStyle(), Rconfig
							.getInstance().drawable("plugins_contactus_email"),
					"Email"));
		}
		if (contact.getMessage().size() > 0) {
			listContactUs.add(new ContactUsEntity(contact.getEmail(), contact
					.getPhone(), contact.getMessage(), contact.getWebsite(),
					contact.getActiveColor(), contact.getStyle(), Rconfig
							.getInstance()
							.drawable("plugins_contactus_message"), "Message"));
		}
		ArrayList<String> a = contact.getPhone();
		System.out.println(a);
		if (contact.getPhone().size() > 0) {
			listContactUs.add(new ContactUsEntity(contact.getEmail(), contact
					.getPhone(), contact.getMessage(), contact.getWebsite(),
					contact.getActiveColor(), contact.getStyle(), Rconfig
							.getInstance().drawable("plugins_contactus_call"),
					"Call"));
		}
		if (contact.getWebsite().length() > 0) {
			listContactUs.add(new ContactUsEntity(contact.getEmail(), contact
					.getPhone(), contact.getMessage(), contact.getWebsite(),
					contact.getActiveColor(), contact.getStyle(), Rconfig
							.getInstance().drawable("plugins_contactus_web"),
					"Website"));
		}
		if (listContactUs != null) {
			adapter = new ContactUsAdapter(listContactUs, mContext);
			grid_contactUs.setAdapter(adapter);
			grid_contactUs.setOnItemClickListener(gridViewOnItemClick);
		}
	}

	@Override
	public List<ContactUsEntity> getListContactUs() {
		return listContactUs;
	}

}
