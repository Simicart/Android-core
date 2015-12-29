package com.simicart.core.customer.block;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.delegate.SignInDelegate;
import com.simicart.core.material.ButtonRectangle;

public class SignInBlock extends SimiBlock implements SignInDelegate {

	private ButtonRectangle btn_SignIn;
	private EditText edt_Email;
	private EditText edt_Password;
	private ImageView iv_ForgotPassword;
	private LinearLayout ll_signInLayout;
	private CheckBox cb_remember_password;
	private TextView txt_label_create_account;

	private String mEmail = "";
	private String mPassword = "";

	private ImageView img_email;
	private ImageView img_password;

	public SignInBlock(View view, Context context) {
		super(view, context);
		// TODO Auto-generated constructor stub
	}

	public void setEmailWatcher(TextWatcher watcher) {
		edt_Email.addTextChangedListener(watcher);
	}

	public void setPasswordWatcher(TextWatcher watcher) {
		edt_Password.addTextChangedListener(watcher);
	}

	public void setForgotPassClicker(OnClickListener clicker) {
		iv_ForgotPassword.setOnClickListener(clicker);
	}

	public void setCreateAccClicker(OnTouchListener clicker) {
		txt_label_create_account.setOnTouchListener(clicker);
	}

	public void setSingInClicker(OnClickListener clicker) {
		btn_SignIn.setOnClickListener(clicker);
	}

	public void setOutSideClicker(OnClickListener clicker) {
		ll_signInLayout.setOnClickListener(clicker);
	}

	public void setOnCheckBox(OnCheckedChangeListener click) {
		cb_remember_password.setOnCheckedChangeListener(click);
	}

	@Override
	public void initView() {
		ll_signInLayout = (LinearLayout) mView.findViewById(Rconfig
				.getInstance().id("coreSignInLayout"));

		cb_remember_password = (CheckBox) mView.findViewById(Rconfig
				.getInstance().id("cb_re_password"));
		cb_remember_password.setText(Config.getInstance().getText(
				"Remember password"));
		cb_remember_password.setTextColor(Config.getInstance()
				.getContent_color());

		 txt_label_create_account = (TextView) mView.findViewById(Rconfig
				.getInstance().id("lable_createAccount"));
		 txt_label_create_account.setText(Config.getInstance().getText(
				"Don't have an account?"));

		btn_SignIn = (ButtonRectangle) mView.findViewById(Rconfig.getInstance()
				.id("bt_signIn"));
		btn_SignIn.setText(Config.getInstance().getText("Sign In"));
		btn_SignIn.setTextColor(Config.getInstance().getButton_text_color());
		btn_SignIn.setBackgroundColor(Config.getInstance()
				.getButton_background());
		btn_SignIn.setTextSize(Constants.SIZE_TEXT_BUTTON);
		// GradientDrawable gdDefault = new GradientDrawable();
		// gdDefault.setColor(Color.GRAY);
		// gdDefault.setCornerRadius(3);
		// btn_SignIn.setBackgroundDrawable(gdDefault);

		// initial Email Field
		edt_Email = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_email"));
		edt_Email.setHint(Config.getInstance().getText("Email"));

		edt_Email.setTextColor(Config.getInstance().getContent_color());
		edt_Email.setHintTextColor(Config.getInstance().getHintContent_color());

		// initial Password Field
		edt_Password = (EditText) mView.findViewById(Rconfig.getInstance().id(
				"et_pass"));
		edt_Password.setHint(Config.getInstance().getText("Password"));
		edt_Password.setTextColor(Config.getInstance().getContent_color());
		edt_Password.setHintTextColor(Config.getInstance()
				.getHintContent_color());
		// initial Forgot Password
		iv_ForgotPassword = (ImageView) mView.findViewById(Rconfig
				.getInstance().id("iv_forgot_pass"));

		if (DataLocal.getCheckRemember()) {
			cb_remember_password.setChecked(true);
			edt_Email.setText(DataLocal.getEmailRemember());
			edt_Password.setText(DataLocal.getPasswordRemember());
			btn_SignIn.setTextColor(Color.WHITE);
			btn_SignIn.setBackgroundColor(Config.getInstance().getColorMain());
		} else {
			if (mEmail != null && !mEmail.equals("")) {
				edt_Email.setText(mEmail);
			} else {
				edt_Email.setText("");
			}
			if (mPassword != null && !mPassword.equals("")) {
				edt_Password.setText(mPassword);
			} else {
				edt_Password.setText("");
			}
		}
		img_email = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"img_email"));
		img_password = (ImageView) mView.findViewById(Rconfig.getInstance().id(
				"iv_pass"));
		changeColorImageView(img_email, "ic_your_acc");
		changeColorImageView(img_password, "ic_your_pas");
		changeColorImageView(iv_ForgotPassword, "ic_forgot_pass");
	}

	private void changeColorImageView(ImageView imageView, String src) {
		Drawable icon = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable(src));
		icon.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		imageView.setImageDrawable(icon);
	}

	@Override
	public String getEmail() {
		return edt_Email.getText().toString();
	}

	@Override
	public String getPassword() {
		return edt_Password.getText().toString();
	}

	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	@Override
	public void showNotify(String message) {
		SimiManager.getIntance().showNotify(null, message, "OK");
	}

	@Override
	public ButtonRectangle getSignIn() {
		return btn_SignIn;
	}
}
