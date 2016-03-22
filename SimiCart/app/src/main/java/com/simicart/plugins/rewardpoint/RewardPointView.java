package com.simicart.plugins.rewardpoint;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.simicart.MainActivity;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.entity.TotalPrice;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.common.ViewIdGenerator;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.others.TotalPriceViewData;
import com.simicart.core.event.slidemenu.SlideMenuData;
import com.simicart.core.material.LayoutRipple;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.core.slidemenu.entity.ItemNavigation.TypeItem;
import com.simicart.plugins.rewardpoint.controller.RewardPointSeerbarController;
import com.simicart.plugins.rewardpoint.fragment.RewardPointFragment;
import com.simicart.plugins.rewardpoint.utils.Constant;

public class RewardPointView {

	private View mView;
	private Context mContext;
	private RelativeLayout rlt_layout_rewardpoint;
	private static ArrayList<ItemNavigation> mItems;
	// cart
	RelativeLayout rlt_layout_cart;
	// review order
	private static RelativeLayout rlt_layout_reviewOrder;
	LinearLayout ll_layout_price;
	protected int mTextSize = 16;
	protected String mColorLabel = "#000000";
	protected String mColorPrice = "red";

	TextView txt_process;
	int point_step = 0;
	static int loy_spend = 0;

	// private static JSONObject jsonObject = new JSONObject();

	public RewardPointView(String method, CacheBlock cacheBlock) {
		mView = cacheBlock.getView();
		this.mContext = SimiManager.getIntance().getCurrentContext();
		if (method.equals("additem_reward_point")) {
			addRewardPoint();
		}
		JSONObject jsonObject = (JSONObject) cacheBlock.getSimiCollection()
				.getJSON();
		Log.e("RewardPointView Block ",
				"MEHTOD: " + method + jsonObject.toString());
		if (method.equals("additem_reward_detail_infor")) {
			addRewardToDetailInfor(jsonObject);
		}
		if (method.equals("additem_reward_card")) {
			addRewardToCart(jsonObject);
		}
		if (method.equals("additem_reward_revieworder")) {
			try {
				JSONArray array = jsonObject.getJSONArray("data");
				JSONObject object = array.getJSONObject(0);
				JSONObject objectFee = object.getJSONObject("fee");
				if (objectFee != null) {
					if (objectFee.has("loyalty_rules")) {
						addRewardToReviewOrder(objectFee, cacheBlock);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public RewardPointView(String methodName, SlideMenuData slideMenuData) {
		Log.e("RewardPointView ", "MEHTOD: " + methodName);
		if (methodName.equals("ad" + "ditem_reward_slidemenu")) {
			mItems = slideMenuData.getItemNavigations();
			ItemNavigation mItemNavigation = new ItemNavigation();
			mItemNavigation.setType(TypeItem.PLUGIN);
			Drawable iconContactUs = MainActivity.context.getResources()
					.getDrawable(
							Rconfig.getInstance().drawable(
									"plugin_reward_ic_menu"));
			iconContactUs.setColorFilter(Config.getInstance().getColorMenu(),
					PorterDuff.Mode.SRC_ATOP);
			mItemNavigation.setName(Constant.REWARDPOINT_MENU_ITEM);
			mItemNavigation.setIcon(iconContactUs);
			mItems.add(mItemNavigation);

			RewardPointFragment rewardPointFragment = new RewardPointFragment();
			slideMenuData.getPluginFragment().put(
					Constant.REWARDPOINT_MENU_ITEM,
					rewardPointFragment.getClass().getName());
		}
		if (methodName.equals("removeitem_reward_slidemenu")) {
			mItems = slideMenuData.getItemNavigations();
			for (ItemNavigation mItemNavigation : mItems) {
				if (mItemNavigation.getName().contains(
						Constant.REWARDPOINT_MENU_ITEM)) {
					mItems.remove(mItemNavigation);
				}
			}
		}
	}

	protected int checkElement(String name) {
		if (null != mItems || mItems.size() > 0) {
			for (int i = 0; i < mItems.size(); i++) {
				ItemNavigation item = mItems.get(i);
				if (item.getName().equals(name)) {
					return i;
				}
			}
			return -1;
		}
		return -1;
	}

	public RewardPointView(String method, String data) {
		if (method.equals("updateItemReward")) {
			if (Utils.validateString(data)) {
				try {
					JSONObject json = new JSONObject(data);
					if (json.has(Constants.DATA)) {
						JSONArray array = json.getJSONArray(Constants.DATA);
						JSONObject js_signIn = array.getJSONObject(0);
						if (js_signIn.has("loyalty_balance")) {
							String loyalty_balance = js_signIn
									.getString("loyalty_balance");
							if (!loyalty_balance.equals("0 Point")) {
								updateItemLeftMenu(mItems, loyalty_balance);
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public RewardPointView(TotalPriceViewData data, TableLayout tbLayout) {
		addRewardPay(data);
	}

	public RewardPointView(Context context, TotalPrice total_price,
			TableLayout tbl_price, String symbol, Integer text_size,
			String color_label, String color_price) {
		mContext = SimiManager.getIntance().getCurrentContext();
		JSONObject objectResult = total_price.getJSONObject();
		// Long discount = null;
		String loyalty_spend = "";
		String loyalty_discount = "";
		try {
			loyalty_spend = objectResult.getString("loyalty_spend");
			loyalty_discount = objectResult.getString("loyalty_discount");

		} catch (Exception e) {
			Log.d("Error:", e.getMessage());
		}

		if (!loyalty_spend.equals("") && !loyalty_spend.equals("0")) {
			TableRow tableRowPointDiscount = new TableRow(context);
			tableRowPointDiscount.setGravity(Gravity.RIGHT);
			String labelDiscount = "<font color='" + color_label + "'>"
					+ loyalty_spend
					+ Config.getInstance().getText(" Points Discount")
					+ ": </font>";
			TextView textview_label_discount = (TextView) showView(labelDiscount);
			String priceDiscount = "<font color='" + color_price + "'>"
					+ Config.getInstance().getPrice(loyalty_discount)
					+ "</font>";
			TextView textview_price_discount = (TextView) showView(priceDiscount);
			tbl_price.addView(tableRowPointDiscount);
			if (DataLocal.isLanguageRTL) {
				tableRowPointDiscount.addView(textview_price_discount);
				tableRowPointDiscount.addView(textview_label_discount);
			} else {
				tableRowPointDiscount.addView(textview_label_discount);
				tableRowPointDiscount.addView(textview_price_discount);
			}
		}

	}

	protected void addRewardPay(TotalPriceViewData data) {
		String pointEarning = "";
		String pointSpending = "";
		JSONObject jsonObject = data.getTotalPrice().getJSONObject();
		// this.jsonObject = data.getTotalPrice().getJSONObject();
		try {
			if (jsonObject != null) {
				pointEarning = jsonObject.getString("loyalty_earning");
				if (jsonObject.has("loyalty_spending")) {
					pointSpending = jsonObject.getString("loyalty_spending");
					addRewardPriceToReviewOrder(data, pointEarning,
							pointSpending);
				} else {
					addRewardPriceToReviewOrder(data, pointEarning, "");
				}
			}
		} catch (Exception e) {
			Log.d("Error:", e.getMessage());
		}
	}

	private void addRewardPriceToReviewOrder(TotalPriceViewData data,
			String pointEarning, String pointSpending) {

		mContext = SimiManager.getIntance().getCurrentContext();
		TableLayout tb_layout = (TableLayout) data.getView();
		Context context = data.getContext();
		if ((!pointSpending.equals("0 Point")) && (!pointSpending.equals(""))) {
			TableRow tableRowSpend = new TableRow(context);
			tableRowSpend.setGravity(Gravity.RIGHT);
			String labelSpend = "<font color='" + mColorLabel + "'>"
					+ Config.getInstance().getText("You will spend")
					+ ": </font>";
			TextView textview_label_spend = (TextView) showView(labelSpend);
			String priceSpend = "<font color='" + "#ff033e" + "'>"
					+ pointSpending + "</font>";
			TextView textview_price_spend = (TextView) showView(priceSpend);
			tb_layout.addView(tableRowSpend, 0);
			tableRowSpend.addView(textview_label_spend);
			tableRowSpend.addView(textview_price_spend);
		}

		TableRow tableRowEarn = new TableRow(context);
		tableRowEarn.setGravity(Gravity.RIGHT);
		String labelEarn = "<font color='" + mColorLabel + "'>"
				+ Config.getInstance().getText("You will earn") + ": </font>";
		TextView textview_label_earn = (TextView) showView(labelEarn);

		String priceEarn = "<font color='" + "#ff033e" + "'>" + pointEarning
				+ "</font>";
		TextView textview_price_earn = (TextView) showView(priceEarn);
		tb_layout.addView(tableRowEarn, 0);
		if (DataLocal.isLanguageRTL) {
			tableRowEarn.addView(textview_price_earn);
			tableRowEarn.addView(textview_label_earn);
		} else {
			tableRowEarn.addView(textview_label_earn);
			tableRowEarn.addView(textview_price_earn);
		}
	}

	// public RewardPointView(String method) {
	// if (method.equals("clickitem_reward_slidemenu")) {
	// if (Constants.itemName.contains(Constant.REWARDPOINT_MENU_ITEM)) {
	// RewardPointFragment rewardPoint = new RewardPointFragment();
	// if (DataLocal.isTablet) {
	// SimiManager.getIntance().addPopupFragment(rewardPoint);
	// } else {
	// SimiManager.getIntance().replaceFragment(rewardPoint);
	// }
	//
	// }
	// }
	// }

	private void addRewardToDetailInfor(JSONObject jsonObject) {
		String label = "";
		String image = "";
		rlt_layout_rewardpoint = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("plugin_rlt_rewardpoint"));
		rlt_layout_rewardpoint.setGravity(Gravity.CENTER_VERTICAL);
		try {
			label = jsonObject.getString("loyalty_label");
			image = jsonObject.getString("loyalty_image");
			rlt_layout_rewardpoint.setVisibility(View.VISIBLE);
		} catch (Exception e) {
		}
		addViewRewardToDetail(rlt_layout_rewardpoint, label, image);

	}

	private void addViewRewardToDetail(RelativeLayout rlt_layout_rewardpoint,
			String label, String image) {
		// RelativeLayout.LayoutParams paramsView = new
		// RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.MATCH_PARENT, 1);
		// paramsView.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		// View view = new View(mContext);
		// view.setBackgroundColor(Color.parseColor("#CACACA"));
		// view.setLayoutParams(paramsView);
		// rlt_layout_rewardpoint.addView(view);
		RelativeLayout.LayoutParams paramsImageview;
		if (DataLocal.isTablet) {
			paramsImageview = new RelativeLayout.LayoutParams(40, 40);
		} else {
			paramsImageview = new RelativeLayout.LayoutParams(50, 50);
		}

		paramsImageview.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		ImageView img_point = new ImageView(mContext);
		if (DataLocal.isTablet) {
			paramsImageview.setMargins(0, 10, 0, 0);
			img_point.setPadding(0, 0, 0, 10);
		} else {
			paramsImageview.setMargins(0, 30, 0, 0);
		}
		img_point.setLayoutParams(paramsImageview);
		DrawableManager.fetchDrawableOnThread(image, img_point);
		rlt_layout_rewardpoint.addView(img_point);

		@SuppressWarnings("unused")
		RelativeLayout.LayoutParams paramsTextview = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// paramsTextview.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
		// img_point.getId());

		if (DataLocal.isTablet) {
			paramsTextview.setMargins(70, 10, 0, 0);
		} else {
			paramsTextview.setMargins(70, 20, 0, 0);
		}

		TextView textView = new TextView(mContext);
		textView.setText(label);
		textView.setTextColor(Color.parseColor("#000000"));
		textView.setLayoutParams(paramsTextview);
		rlt_layout_rewardpoint.addView(textView);

	}

	protected void updateItemLeftMenu(ArrayList<ItemNavigation> mItems,
			String loyalty_redeem) {
		for (ItemNavigation mItemNavigation : mItems) {
			if (mItemNavigation.getName().equals(
					Config.getInstance()
							.getText(Constant.REWARDPOINT_MENU_ITEM)
							+ Constant.REWARDPOINT_REDEEM)) {
				Constant.REWARDPOINT_REDEEM = loyalty_redeem;
				mItemNavigation.setName(Config.getInstance().getText(
						Constant.REWARDPOINT_MENU_ITEM + " ")
						+ Constant.REWARDPOINT_REDEEM);
				return;
			}
		}
	}

	public void addRewardToCart(JSONObject jsonObject) {

		String label = "";
		String image = "";
		rlt_layout_cart = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rlt_reward_card"));
		try {
			JSONArray array = jsonObject.getJSONArray("data");
			JSONObject object = array.getJSONObject(0);
			if (object.has("loyalty_label")) {
				label = object.getString("loyalty_label");
				rlt_layout_cart.setVisibility(View.VISIBLE);
			} else {
				rlt_layout_cart.setVisibility(View.GONE);
			}
			if (object.has("loyalty_image")) {
				image = object.getString("loyalty_image");
				rlt_layout_cart.setVisibility(View.VISIBLE);
			} else {
				rlt_layout_cart.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			rlt_layout_cart.setVisibility(View.GONE);
		}

		rlt_layout_cart.removeAllViews();
		RelativeLayout.LayoutParams paramsImageview = new RelativeLayout.LayoutParams(
				Utils.getValueDp(20), Utils.getValueDp(20));
		paramsImageview.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		paramsImageview.addRule(RelativeLayout.CENTER_VERTICAL);
		paramsImageview.setMargins(Utils.getValueDp(10), 0,
				Utils.getValueDp(10), 0);
		ImageView img_point = new ImageView(mContext);
		img_point.setId(ViewIdGenerator.generateViewId());
		img_point.setLayoutParams(paramsImageview);
		DrawableManager.fetchDrawableOnThread(image, img_point);
		rlt_layout_cart.addView(img_point);

		RelativeLayout.LayoutParams paramsTextview = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsTextview.addRule(RelativeLayout.CENTER_VERTICAL);
		paramsTextview.addRule(RelativeLayout.RIGHT_OF, img_point.getId());
		paramsTextview.setMargins(0, Utils.getValueDp(5), 0,
				Utils.getValueDp(5));
		TextView textView = new TextView(mContext);
		textView.setId(123456);
		textView.setTextColor(Color.parseColor("#ff033e"));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		textView.setText("");
		String text = label;
		textView.setText(text);
		textView.setTextColor(Color.parseColor("#ff033e"));
		textView.setLayoutParams(paramsTextview);
		rlt_layout_cart.addView(textView);
	}

	private void addRewardToReviewOrder(JSONObject jsonObject,
			final CacheBlock cacheBlock) {
		String pointStepLabel = "";
		String pointStepDiscount = "";
		int maxPoints = 0;
		int minPoint = 0;
		int loyalty_spend = 0;

		try {
			if (jsonObject.has("loyalty_spend")) {
				loyalty_spend = jsonObject.getInt("loyalty_spend");
			}
			if (jsonObject.has("loyalty_rules")) {
				JSONArray array_loyalty_rules = jsonObject
						.getJSONArray("loyalty_rules");
				JSONObject object_rules = array_loyalty_rules.getJSONObject(0);
				if (object_rules.has("minPoints")) {
					minPoint = object_rules.getInt("minPoints");
				}
				if (object_rules.has("maxPoints")) {
					maxPoints = object_rules.getInt("maxPoints");
				}
				if (object_rules.has("pointStepLabel")) {
					pointStepLabel = object_rules.getString("pointStepLabel");
				}
				if (object_rules.has("pointStepDiscount")) {
					pointStepDiscount = object_rules
							.getString("pointStepDiscount");
				}
				if (object_rules.has("pointStep")) {
					point_step = object_rules.getInt("pointStep");
				}
			}

		} catch (Exception e) {
			Log.d("Error:", e.getMessage());
		}

		rlt_layout_reviewOrder = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rlt_layout_reward_revieworder"));
		rlt_layout_reviewOrder.removeAllViews();
		rlt_layout_reviewOrder.setBackgroundColor(Color.parseColor("#FFFFFF"));
		// add toplayout title
		RelativeLayout layoutTitle = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams paramTitle = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layoutTitle.setLayoutParams(paramTitle);
		layoutTitle.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutTitle.setBackgroundColor(Color.parseColor("#EBEBEB"));

		rlt_layout_reviewOrder.addView(layoutTitle);
		// add textview to toplayout
		TextView txt_title = new TextView(mContext);
		txt_title.setText(Config.getInstance().getText("SPEND MY POINTS"));
		RelativeLayout.LayoutParams param_txt_title = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		txt_title.setLayoutParams(param_txt_title);
		txt_title.setPadding(0, 15, 0, 15);
		txt_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		txt_title.setTextColor(Color.parseColor("#000000"));
		txt_title.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutTitle.addView(txt_title);

		// add content
		RelativeLayout layoutCenter = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams paramCenter = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutCenter.setLayoutParams(paramCenter);
		rlt_layout_reviewOrder.addView(layoutCenter);

		// add child for layout center
		TextView txt_content = new TextView(mContext);

		RelativeLayout.LayoutParams param_txt_conten = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_txt_conten.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		if (DataLocal.isTablet) {
			param_txt_conten.setMargins(0, Utils.getValueDp(60), 0, 0);
		} else {
			param_txt_conten.setMargins(0, Utils.getValueDp(45), 0, 0);
		}
		txt_content.setLayoutParams(param_txt_conten);
		txt_content.setTag("txt_content");
		txt_content.setId(Constant.ID_TEXTVIEW_CONTENT);
		// txt_content.setText("Each of 1 Point gets $1.00 discount");
		txt_content.setText(Config.getInstance().getText("Each of") + " "
				+ pointStepLabel + " " + Config.getInstance().getText("gets")
				+ " " + pointStepDiscount + " "
				+ Config.getInstance().getText("discount"));

		txt_content.setTextSize(17);
		txt_content.setTextColor(Color.parseColor("#000000"));
		txt_content.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutCenter.addView(txt_content);

		RelativeLayout.LayoutParams paramsSeekbar = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		paramsSeekbar.addRule(RelativeLayout.BELOW,
				Constant.ID_TEXTVIEW_CONTENT);
		final SeekBar seekBar = new SeekBar(mContext);
		seekBar.setId(Constant.ID_SEERBAR);
		seekBar.setMax(maxPoints);
		// seekBar.setVerticalFadingEdgeEnabled(true);
		// seekBar.setFadingEdgeLength(2);
		seekBar.setLayoutParams(paramsSeekbar);
		seekBar.setProgress(loyalty_spend);

		// seekBar.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.rewardpoint_seerbar));
		layoutCenter.addView(seekBar);

		RelativeLayout layout_process = new RelativeLayout(mContext);
		RelativeLayout.LayoutParams param_process = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_process.addRule(RelativeLayout.BELOW, Constant.ID_SEERBAR);
		// param_process.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layout_process.setLayoutParams(param_process);
		// layout_process.setGravity(RelativeLayout.CENTER_VERTICAL);
		layoutCenter.addView(layout_process);

		TextView txt_min = new TextView(mContext);
		txt_min.setId(Constant.ID_TEXT_MIN);
		RelativeLayout.LayoutParams param_txt_min = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_txt_min.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		txt_min.setLayoutParams(param_txt_min);
		txt_min.setText(minPoint + "");
		txt_min.setTextSize(17);
		txt_min.setPadding(30, 0, 0, 5);
		txt_min.setTextColor(Color.parseColor("#000000"));
		layout_process.addView(txt_min);

		TextView txt_max = new TextView(mContext);
		RelativeLayout.LayoutParams param_txt_max = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_txt_max.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		txt_max.setLayoutParams(param_txt_max);
		txt_max.setId(Constant.ID_TEXT_MAX);
		txt_max.setText(maxPoints + "");
		txt_max.setPadding(0, 0, 30, 5);
		txt_max.setTextColor(Color.parseColor("#000000"));
		layout_process.addView(txt_max);

		txt_process = new TextView(mContext);
		txt_process.setId(Constant.ID_TEXT_PROCESS);
		RelativeLayout.LayoutParams param_txt_process = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		param_txt_process.addRule(RelativeLayout.CENTER_HORIZONTAL);
		txt_process.setLayoutParams(param_txt_process);
		txt_process.setPadding(0, 0, 0, 5);
		txt_process.setTextColor(Color.parseColor("#000000"));
		txt_process.setText(Config.getInstance().getText("Spending") + ": "
				+ loyalty_spend);
		layout_process.addView(txt_process);

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int process = 0;

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				int end_process = handleProcess(process, point_step);
				System.out.println(loy_spend);
				txt_process.setText("");
				txt_process.setText(Config.getInstance().getText("Spending")
						+ ":" + end_process);
				seekBar.setProgress(end_process);
				if (end_process != loy_spend) {
					// send request to server
					// url : /loyalty/point/spend
					// param : {"ruleid":"rate","usepoint":26}
					loy_spend = end_process;
					RewardPointSeerbarController pointController = new RewardPointSeerbarController(
							end_process, cacheBlock);
					pointController.onStart();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				process = progress;
			}
		});

	}

	private int handleProcess(int process, int inputNumber) {
		int endProcess = 0;
		float progress = process;
		float number = inputNumber;
		if (inputNumber == 0) {
			number = 1;
			inputNumber = 1;
		}

		int result = process / inputNumber;
		float excess = progress - (result * number);

		float divide_number = number / 2;

		if (excess >= divide_number) {
			// cong them
			endProcess = (int) (result * number + inputNumber);
		} else {
			// tru di
			endProcess = result * inputNumber;
		}
		return endProcess;
	}

	protected View showView(String content) {
		TextView tv_price = new TextView(mContext);
		tv_price.setGravity(Gravity.RIGHT);
		tv_price.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
		tv_price.setText(Html.fromHtml(content));
		return tv_price;
	}

	protected void addRewardPoint() {
		RelativeLayout rel_RewardPoint = (RelativeLayout) mView
				.findViewById(Rconfig.getInstance().id("rel_RewardPoint"));
		View v = MainActivity.instance.getLayoutInflater().inflate(
				Rconfig.getInstance().layout("plugins_rewardpoint_in_profile"),
				null);
		rel_RewardPoint.addView(v);
		final LayoutRipple rl_rewardPoint = (LayoutRipple) v
				.findViewById(Rconfig.getInstance().id("rl_rewardPoint"));
		TextView lb_reward_point = (TextView) rl_rewardPoint
				.findViewById(Rconfig.getInstance().id("lb_reward_point"));
		if (DataLocal.isLanguageRTL) {
			lb_reward_point.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
		}
		ImageView im_reward_point = (ImageView) rl_rewardPoint
				.findViewById(Rconfig.getInstance().id("im_reward_point"));
		Drawable icon = mContext.getResources().getDrawable(
				Rconfig.getInstance().drawable("plugin_reward_ic_myacc"));
		icon.setColorFilter(Config.getInstance().getContent_color(),
				PorterDuff.Mode.SRC_ATOP);
		im_reward_point.setImageDrawable(icon);
		lb_reward_point.setText(Config.getInstance().getText("Reward Point"));
		lb_reward_point.setTextColor(Config.getInstance().getContent_color());
		rl_rewardPoint.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				RewardPointFragment fragmentRe = new RewardPointFragment();
				SimiManager.getIntance().replacePopupFragment(fragmentRe);
			}
		});
	}
}
