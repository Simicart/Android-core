package com.simicart.core.common.options.time;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.config.Config;

public class CacheOptionTimeView extends CacheOptionView {

	private boolean isselectedOptionTime = false;

	public CacheOptionTimeView(CacheOption cacheOption) {
		super(cacheOption);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createCacheOption() {
		final ProductOption option = mCacheOption.getAllOption().get(0);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		final TextView tv_time = new TextView(mContext);
		tv_time.setLayoutParams(param);
		tv_time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv_time.setGravity(Gravity.CENTER);
		tv_time.setTextColor(Color.parseColor("#000000"));
		ll_body.addView(tv_time);

		final TimePicker timePicker = new TimePicker(SimiManager.getIntance()
				.getCurrentActivity());
		timePicker.setSaveFromParentEnabled(false);
		timePicker.setSaveEnabled(true);

		if ((mCacheOption.getHour() > 0) && (mCacheOption.getMinute() > 0)) {
			isselectedOptionTime = true;
			String hour = "" + mCacheOption.getHour();
			if (mCacheOption.getHour() < 10) {
				hour = "0" + mCacheOption.getHour();
			}
			String minuteS = "" + mCacheOption.getMinute();
			if (mCacheOption.getMinute() < 10) {
				minuteS = "0" + mCacheOption.getMinute();
			}
			mCacheOption.setCompleteRequired(true);
			tv_required.setText(Config.getInstance().getPrice(
					"" + option.getOptionPrice()));
			tv_time.setText(hour + ":" + minuteS);
			timePicker.setCurrentHour(mCacheOption.getHour());
			timePicker.setCurrentMinute(mCacheOption.getMinute());
//			if (!isShowWhenStart) {
				updatePriceForParent(option, ADD_OPERATOR);
//			}
		}
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			boolean isAddPrice = true;

			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				mCacheOption.setHour(hourOfDay);
				mCacheOption.setMinute(minute);

				String hour = "" + hourOfDay;
				if (hourOfDay < 10) {
					hour = "0" + hourOfDay;
				}
				String minuteS = "" + minute;
				if (minute < 10) {
					minuteS = "0" + minute;
				}
				tv_time.setText(hour + ":" + minuteS);
				if (isAddPrice && !isselectedOptionTime) {
					mCacheOption.setCompleteRequired(true);
					tv_required.setText(Config.getInstance().getPrice(
							"" + option.getOptionPrice()));
					updatePriceForParent(option, ADD_OPERATOR);
					isAddPrice = false;
				}
			}
		});

		timePicker.setLayoutParams(param);
		ll_body.addView(timePicker);
	}

}
