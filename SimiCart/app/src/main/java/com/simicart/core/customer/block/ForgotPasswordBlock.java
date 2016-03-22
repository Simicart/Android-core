package com.simicart.core.customer.block;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.ForgotPasswordDelegate;
import com.simicart.core.material.ButtonRectangle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class ForgotPasswordBlock extends SimiBlock implements
		ForgotPasswordDelegate {

	protected ButtonRectangle btn_Send;
	protected EditText edt_Email;
	private TextView lable_email;

	public ForgotPasswordBlock(View view, Context context) {
		super(view, context);
		// TODO Auto-generated constructor stub
	}

	public void setOnClicker(OnClickListener clicker) {
		btn_Send.setOnClickListener(clicker);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void initView() {
		lable_email = (TextView) mView.findViewById(Rconfig
				.getInstance().id("lable_email"));
		lable_email.setTextColor(Color.GRAY);
		lable_email.setText(Config.getInstance().getText("Enter Your Email")
				.toUpperCase()
				+ ":");

		// button sent
		btn_Send = (ButtonRectangle) mView.findViewById(Rconfig.getInstance().id(
				"bt_send"));
		btn_Send.setText(Config.getInstance().getText("Reset my password"));
		btn_Send.setTextColor(Color.WHITE);
		btn_Send.setBackgroundColor(Config.getInstance().getColorMain());
		btn_Send.setTextSize(Constants.SIZE_TEXT_BUTTON);

		// Email Field
		edt_Email = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_email"));
		edt_Email.setHint(Config.getInstance().getText("Email"));
		
		lable_email.setTextColor(Config.getInstance().getContent_color());
		edt_Email.setTextColor(Config.getInstance().getContent_color());
		edt_Email.setHintTextColor(Config.getInstance().getHintContent_color());
	}

	@Override
	public String getEmail() {
		return edt_Email.getText().toString();
	}

	public void showNotify(String message) {
		SimiManager.getIntance().showNotify(message);
	}

}
