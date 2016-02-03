package com.simicart.plugins.rewardpoint.fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.customer.fragment.MyAccountFragment;
import com.simicart.core.customer.fragment.SignInFragment;
import com.simicart.plugins.rewardpoint.entity.ItemPointData;
import com.simicart.plugins.rewardpoint.model.ModelRewardPoint;

public class RewardPointFragment extends SimiFragment {

	private ModelRewardPoint mModel;
	private Context mContext;
	private View view;
	private RelativeLayout rlt_layout_card;
	private RelativeLayout rlt_layout_history;
	private RelativeLayout rlt_layout_setting;
	private Button btn_signin;
	private TextView txt_card;
	private TextView txt_history;
	private TextView txt_setting;
	private TextView txt_ours_policies;

	private LinearLayout layout_point;
	private ProgressBar progress_point;
	private TextView txt_maxpoint;

	private TextView txt_number_point;
	private TextView txt_number_redeem;
	private TextView txt_changemoney;
	private TextView txt_earnpoint;
	private TextView txt_earnpoint_content;
	private TextView txt_spendpoint;
	private TextView txt_spendpoint_content;
	private TextView txt_coint;
	private LinearLayout layout_policy_content;
	private RelativeLayout layout_policy;
	int spending_mint = 1;
	private boolean checkRequest = false;
	public JSONObject jsonResult;

	private TextView txt_availble_point;

	String passBookLogo = "";
	String loyalty_redeem = "";
	String passbook_text = "";
	String background_passbook = "";
	String passbook_foreground = "";
	String passbook_barcode = "";
	int is_notification;
	int expire_notification;

	private RelativeLayout rlt_earpoint_text;
	private RelativeLayout rlt_spendpoint_text;

	private RelativeLayout rlt_ours_policies;
	private LinearLayout linearLayout_top;
	private LinearLayout linearLayout_center;
	private LinearLayout linearLayout_bottom;

	protected ProgressDialog pd_loading;

	protected String mID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		pd_loading = ProgressDialog.show(mContext, null, null, true, false);
		pd_loading.setContentView(Rconfig.getInstance().layout(
				"core_base_loading"));
		pd_loading.setCanceledOnTouchOutside(false);
		pd_loading.setCancelable(false);
		pd_loading.show();
		if (checkRequest == false) {
			try {
				mModel = new ModelRewardPoint();
				ModelDelegate delegate = new ModelDelegate() {

					@Override
					public void callBack(String message, boolean isSuccess) {
						pd_loading.dismiss();
						if (isSuccess) {
							try {
								checkRequest = true;
								jsonResult = mModel.getJSON();
								if (view != null) {
									addDataToView(jsonResult);
								}
							} catch (Exception e) {
							}
						} else {

						}
					}
				};
				mModel.setDelegate(delegate);
				mModel.request();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(
				Rconfig.getInstance().layout(
						"plugins_rewardpoint_rewardpointfragment"), container,
				false);
		addView();
		handleEvent();
		return view;
	}

	private void addView() {
		rlt_layout_card = (RelativeLayout) view.findViewById(Rconfig
				.getInstance().id("layout_card"));
		rlt_layout_history = (RelativeLayout) view.findViewById(Rconfig
				.getInstance().id("layout_history"));
		rlt_layout_setting = (RelativeLayout) view.findViewById(Rconfig
				.getInstance().id("layout_setting"));
		txt_card = (TextView) view.findViewById(Rconfig.getInstance().id(
				"txt_card"));
		txt_history = (TextView) view.findViewById(Rconfig.getInstance().id(
				"txt_history"));
		txt_setting = (TextView) view.findViewById(Rconfig.getInstance().id(
				"txt_setting"));
		txt_ours_policies = (TextView) view.findViewById(Rconfig.getInstance()
				.id("txt_ours_policies"));

		txt_card.setText(Config.getInstance().getText("Rewards Card"));
		txt_history.setText(Config.getInstance().getText("Rewards History"));
		txt_setting.setText(Config.getInstance().getText("Settings"));
		txt_ours_policies.setText(Config.getInstance().getText("Our Policies"));

		layout_point = (LinearLayout) view.findViewById(Rconfig.getInstance()
				.id("layout_point"));
		progress_point = (ProgressBar) view.findViewById(Rconfig.getInstance()
				.id("progressBarPoint"));
		txt_maxpoint = (TextView) view.findViewById(Rconfig.getInstance().id(
				"txt_maxpoint"));

		txt_number_point = (TextView) view.findViewById(Rconfig.getInstance()
				.id("txt_number_point"));
		txt_number_redeem = (TextView) view.findViewById(Rconfig.getInstance()
				.id("txt_point_redeem"));
		txt_changemoney = (TextView) view.findViewById(Rconfig.getInstance()
				.id("txt_changemoney"));
		txt_earnpoint = (TextView) view.findViewById(Rconfig.getInstance().id(
				"txt_earnpoint"));
		txt_earnpoint_content = (TextView) view.findViewById(Rconfig
				.getInstance().id("txt_earnpoint_content"));
		txt_spendpoint = (TextView) view.findViewById(Rconfig.getInstance().id(
				"txt_spendpoint"));
		txt_spendpoint_content = (TextView) view.findViewById(Rconfig
				.getInstance().id("txt_spendpoint_content"));
		txt_coint = (TextView) view.findViewById(Rconfig.getInstance().id(
				"txt_coint"));
		layout_policy_content = (LinearLayout) view.findViewById(Rconfig
				.getInstance().id("layout_ours_policies_content"));
		rlt_ours_policies = (RelativeLayout) view.findViewById(Rconfig
				.getInstance().id("layout_ours_policies"));
		layout_policy = (RelativeLayout) view.findViewById(Rconfig
				.getInstance().id("layout_ours_policies"));
		linearLayout_bottom = (LinearLayout) view.findViewById(Rconfig
				.getInstance().id("layout_bottom"));

		linearLayout_top = (LinearLayout) view.findViewById(Rconfig
				.getInstance().id("layout_top1"));
		linearLayout_center = (LinearLayout) view.findViewById(Rconfig
				.getInstance().id("layout_center"));
		rlt_earpoint_text = (RelativeLayout) view.findViewById(Rconfig
				.getInstance().id("layout_earnpoint"));

		rlt_spendpoint_text = (RelativeLayout) view.findViewById(Rconfig
				.getInstance().id("layout_spendpoint"));
		txt_availble_point = (TextView) view.findViewById(Rconfig.getInstance()
				.id("txt_avaible_point"));
		txt_availble_point.setText(Config.getInstance().getText(
				"AVAILABLE POINTS"));

		progress_point.setMax(10);
		progress_point.setProgress(7);
		btn_signin = (Button) view.findViewById(Rconfig.getInstance().id(
				"btn_reward_signin"));
		GradientDrawable gdDefault = new GradientDrawable();
		gdDefault.setColor(Config.getInstance().getColorMain());
		gdDefault.setCornerRadius(15);
		if (!DataLocal.isSignInComplete()) {
			// btn_signin.setVisibility(View.VISIBLE);
			btn_signin.setBackgroundDrawable(gdDefault);
			btn_signin.setText(Config.getInstance().getText("Login or Signup"));
			LinearLayout layout_top = (LinearLayout) view.findViewById(Rconfig
					.getInstance().id("layout_top"));
			layout_top.setVisibility(View.GONE);
			LinearLayout layout_center = (LinearLayout) view
					.findViewById(Rconfig.getInstance().id("layout_center"));
			layout_center.setVisibility(View.GONE);
		} else {
			btn_signin.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		handleEvent();
		addDataToView(jsonResult);
	}

	private void addDataToView(JSONObject json) {
		if (json != null) {
			linearLayout_top.setVisibility(View.VISIBLE);
			if (!DataLocal.isSignInComplete()) {
				linearLayout_center.setVisibility(View.GONE);
			} else {
				linearLayout_center.setVisibility(View.VISIBLE);
			}
			linearLayout_bottom.setVisibility(View.VISIBLE);
			ItemPointData itempointData = new ItemPointData()
					.getItemPointFromJson(json);
			is_notification = itempointData.getIs_notification();
			expire_notification = itempointData.getExpire_notification();
			loyalty_redeem = itempointData.getLoyalty_redeem();
			try {
				passBookLogo = itempointData.getLoyalty_image();
				if (!Utils.validateString(itempointData.getSpending_policy())
						|| !Utils.validateString(itempointData
								.getSpending_label())) {
					rlt_spendpoint_text.setVisibility(View.GONE);
					txt_spendpoint_content.setVisibility(View.GONE);
				} else {
					rlt_spendpoint_text.setVisibility(View.VISIBLE);
					txt_spendpoint_content.setVisibility(View.VISIBLE);
					txt_spendpoint_content.setText(itempointData
							.getSpending_policy());
					txt_spendpoint.setText(itempointData.getSpending_label());
				}
				if (Utils
						.validateString("" + itempointData.getSpending_point())
						&& Utils.validateString(""
								+ itempointData.getSpending_discount())) {
					txt_changemoney.setVisibility(View.VISIBLE);
					txt_changemoney.setText(itempointData.getSpending_point()
							+ " = " + itempointData.getSpending_discount());
				} else {
					txt_changemoney.setVisibility(View.GONE);
				}

				// set content to view
				txt_number_point.setText(itempointData.getLoyalty_point() + "");
				txt_availble_point.setVisibility(View.VISIBLE);
				txt_number_redeem.setText(Config.getInstance().getText("Equal")
						+ " " + loyalty_redeem + " "
						+ Config.getInstance().getText("to redeem"));
				txt_coint.setText(itempointData.getSpending_discount());

				System.out.println("xx");
				String label = itempointData.getEarning_label();
				String policy = itempointData.getEarning_policy();
				if (!Utils.validateString(itempointData.getEarning_label())
						|| !Utils.validateString(itempointData
								.getEarning_policy())) {
					rlt_earpoint_text.setVisibility(View.GONE);
					txt_earnpoint_content.setVisibility(View.GONE);
				} else {
					rlt_earpoint_text.setVisibility(View.VISIBLE);
					txt_earnpoint_content.setVisibility(View.VISIBLE);
					txt_earnpoint.setText(itempointData.getEarning_label());
					txt_earnpoint_content.setText(itempointData
							.getEarning_policy());
				}

				passbook_text = itempointData.getPassbook_text();
				background_passbook = itempointData.getBackground_passbook();
				passbook_foreground = itempointData.getPassbook_foreground();
				if (passbook_foreground.equals("")) {
					rlt_layout_card.setVisibility(View.GONE);
				}
				passbook_barcode = itempointData.getPassbook_barcode();
				JSONArray array_policies = itempointData.getArray_policies();
				if (array_policies.length() > 0) {
					rlt_ours_policies.setVisibility(View.VISIBLE);
					layout_policy_content.removeAllViews();
					for (int i = 0; i < array_policies.length(); i++) {
						LinearLayout.LayoutParams paramsTextview = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						TextView textView = new TextView(mContext);
						textView.setPadding(Utils.getValueDp(10),
								Utils.getValueDp(10), Utils.getValueDp(10),
								Utils.getValueDp(10));
						textView.setText(array_policies.getString(i));
						textView.setTextColor(Color.parseColor("#000000"));
						textView.setLayoutParams(paramsTextview);
						layout_policy_content.addView(textView);
					}

					if (!DataLocal.isSignInComplete()) {
						btn_signin.setVisibility(View.VISIBLE);
					} else {
						btn_signin.setVisibility(View.GONE);
					}

				} else {
					layout_policy.setVisibility(View.GONE);
					layout_policy_content.setVisibility(View.GONE);
				}
				if (itempointData.getLoyalty_point() > spending_mint - 1
						|| (itempointData.getLoyalty_point() == 0)) {
					layout_point.setVisibility(View.GONE);
					txt_number_redeem.setVisibility(View.GONE);
				} else {
					layout_point.setVisibility(View.VISIBLE);
					progress_point.setMax(spending_mint);
					progress_point
							.setProgress(itempointData.getLoyalty_point());
					int until = spending_mint
							- itempointData.getLoyalty_point();
					txt_number_redeem.setText(Config.getInstance().getText(
							"Only")
							+ " "
							+ until
							+ " "
							+ Config.getInstance().getText("Points until")
							+ " " + itempointData.getStart_discount());
					txt_maxpoint.setText(spending_mint + "");
				}
			} catch (Exception e) {
				// System.out.println(e.getMessage());
			}
		}
	}

	private void handleEvent() {
		rlt_layout_history.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					rlt_layout_history.setBackgroundColor(Color
							.parseColor("#EBEBEB"));
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					rlt_layout_history.setBackgroundColor(Color
							.parseColor("#FFFFFF"));
					RewardHistoryFragment rewardHistory = new RewardHistoryFragment();
					SimiManager.getIntance()
							.replacePopupFragment(rewardHistory);
					break;
				case MotionEvent.ACTION_CANCEL:
					rlt_layout_history.setBackgroundColor(Color
							.parseColor("#FFFFFF"));
					break;
				}
				return true;
			}
		});
		rlt_layout_setting.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					rlt_layout_setting.setBackgroundColor(Color
							.parseColor("#EBEBEB"));
					break;
				case MotionEvent.ACTION_UP:
					rlt_layout_setting.setBackgroundColor(Color
							.parseColor("#FFFFFF"));
					// RewardSettingFragment rewardSetting = new
					// RewardSettingFragment(
					// is_notification, expire_notification);

					RewardSettingFragment fragment = RewardSettingFragment
							.newInstance(is_notification, expire_notification);
					SimiManager.getIntance().replacePopupFragment(fragment);
					break;
				case MotionEvent.ACTION_CANCEL:
					rlt_layout_setting.setBackgroundColor(Color
							.parseColor("#FFFFFF"));
					break;
				}
				return true;
			}
		});
		rlt_layout_card.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					rlt_layout_card.setBackgroundColor(Color
							.parseColor("#EBEBEB"));
					break;
				case MotionEvent.ACTION_UP:
					rlt_layout_card.setBackgroundColor(Color
							.parseColor("#FFFFFF"));
					// RewardCardFragment rewardCard = new RewardCardFragment(
					// passBookLogo, loyalty_redeem, passbook_text,
					// background_passbook, passbook_foreground,
					// passbook_barcode);
					RewardCardFragment rewardCard = RewardCardFragment
							.newInstance(passBookLogo, loyalty_redeem,
									passbook_text, background_passbook,
									passbook_foreground, passbook_barcode);
					SimiManager.getIntance().replacePopupFragment(rewardCard);
					break;
				case MotionEvent.ACTION_CANCEL:
					rlt_layout_card.setBackgroundColor(Color
							.parseColor("#FFFFFF"));
					break;
				}
				return true;
			}
		});

		btn_signin.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				GradientDrawable gdDefault = new GradientDrawable();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					gdDefault.setColor(Color.parseColor("#ABABAB"));
					gdDefault.setCornerRadius(15);
					btn_signin.setBackgroundDrawable(gdDefault);
					break;
				case MotionEvent.ACTION_UP:
					gdDefault.setColor(Config.getInstance().getColorMain());
					gdDefault.setCornerRadius(15);
					btn_signin.setBackgroundDrawable(gdDefault);
					if (DataLocal.isSignInComplete()) {
						MyAccountFragment fragment = new MyAccountFragment();
						SimiManager.getIntance().replacePopupFragment(fragment);
					} else {
						SignInFragment fragment = new SignInFragment();
						SimiManager.getIntance().replacePopupFragment(fragment);
					}
					break;
				case MotionEvent.ACTION_CANCEL:
					gdDefault.setColor(Config.getInstance().getColorMain());
					gdDefault.setCornerRadius(15);
					btn_signin.setBackgroundDrawable(gdDefault);
					break;
				}
				return true;
			}
		});

	}

//	public void setProductId(String id) {
//		this.mID = id;
//	}

	public String getProductId() {
		return mID;
	}

}
