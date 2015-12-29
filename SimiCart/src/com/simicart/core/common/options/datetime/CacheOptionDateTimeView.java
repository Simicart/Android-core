package com.simicart.core.common.options.datetime;

import java.util.Calendar;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.config.Config;

public class CacheOptionDateTimeView extends CacheOptionView {

	private boolean isselectedOptionDateTime = false;

	public CacheOptionDateTimeView(CacheOption cacheOption) {
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
		tv_time.setTextColor(Color.parseColor("#ffffff"));
		ll_body.addView(tv_time);

		final LinearLayout ll_time = new LinearLayout(mContext);
		ll_time.setLayoutParams(param);
		ll_time.setOrientation(LinearLayout.HORIZONTAL);
		ll_body.addView(ll_time);

		final TextView tv_date = new TextView(mContext);
		tv_date.setLayoutParams(param);
		tv_date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv_date.setGravity(Gravity.CENTER);
		tv_date.setTextColor(Color.parseColor("#000000"));
		ll_body.addView(tv_date);

		final LinearLayout ll_date = new LinearLayout(mContext);
		ll_date.setLayoutParams(param);
		ll_date.setOrientation(LinearLayout.HORIZONTAL);
		ll_body.addView(ll_date);

		Calendar today = Calendar.getInstance();

		final TimePicker timePicker = new TimePicker(SimiManager.getIntance()
				.getCurrentActivity());
		timePicker.setSaveFromParentEnabled(false);
		timePicker.setSaveEnabled(true);

		final DatePicker datePicker = new DatePicker(SimiManager.getIntance()
				.getCurrentActivity());
		datePicker.setSaveFromParentEnabled(false);
		datePicker.setSaveEnabled(true);

		OnDateChangedListener dateListener = new OnDateChangedListener() {
			boolean isAddPrice = true;

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				mCacheOption.setDay(dayOfMonth);
				mCacheOption.setMonth(monthOfYear + 1);
				mCacheOption.setYear(year);

				String day = "" + dayOfMonth;
				if (dayOfMonth < 10) {
					day = "0" + dayOfMonth;
				}
				String month = "" + (monthOfYear + 1);
				if ((monthOfYear + 1) < 10) {
					month = "0" + (monthOfYear + 1);
				}
				tv_date.setText(day + "-" + month + "-" + year);
				if (mCacheOption.getHour() == -1) {
					isAddPrice = false;
				}
				if (isAddPrice && mCacheOption.getHour() != -1
						&& !isselectedOptionDateTime) {
					mCacheOption.setCompleteRequired(true);
					updatePriceHeader(Config.getInstance().getPrice(
							"" + option.getOptionPrice()));
					updatePriceForParent(option, ADD_OPERATOR);
					isAddPrice = false;
				}
			}
		};

		if (mCacheOption.getHour() > 0 && mCacheOption.getDay() > 0) {
			// time
			String hour = "" + mCacheOption.getHour();
			if (mCacheOption.getHour() < 10) {
				hour = "0" + mCacheOption.getHour();
			}
			String minuteS = "" + mCacheOption.getMinute();
			if (mCacheOption.getMinute() < 10) {
				minuteS = "0" + mCacheOption.getMinute();
			}
			timePicker.setCurrentHour(mCacheOption.getHour());
			timePicker.setCurrentMinute(mCacheOption.getMinute());

			tv_time.setText(hour + ":" + minuteS);

			// date
			String day = "" + mCacheOption.getDay();
			if (mCacheOption.getDay() < 10) {
				day = "0" + mCacheOption.getDay();
			}
			String month = "" + mCacheOption.getMonth();
			if (mCacheOption.getMonth() < 10) {
				month = "0" + mCacheOption.getMonth();
			}
			tv_date.setText(day + "-" + month + "-" + mCacheOption.getYear());
			datePicker.init(mCacheOption.getYear(),
					mCacheOption.getMonth() - 1, mCacheOption.getDay(),
					dateListener);

			isselectedOptionDateTime = true;
			mCacheOption.setCompleteRequired(true);
			updatePriceHeader(Config.getInstance().getPrice(
					"" + option.getOptionPrice()));
				updatePriceForParent(option, ADD_OPERATOR);
		} else {
			datePicker.init(today.get(Calendar.YEAR),
					today.get(Calendar.MONTH),
					today.get(Calendar.DAY_OF_MONTH), dateListener);
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
				if (mCacheOption.getDay() == -1) {
					isAddPrice = false;
				}
				if (isAddPrice && mCacheOption.getDay() != -1
						&& !isselectedOptionDateTime) {
					mCacheOption.setCompleteRequired(true);
					updatePriceHeader(Config.getInstance().getPrice(
							"" + option.getOptionPrice()));
					updatePriceForParent(option, ADD_OPERATOR);
					isAddPrice = false;
				}
			}
		});

		timePicker.setLayoutParams(param);
		ll_time.addView(timePicker);

		datePicker.setLayoutParams(param);
		ll_date.addView(datePicker);
		datePicker.setCalendarViewShown(false);
	}

}
