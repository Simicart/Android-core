package com.simicart.core.common.options.date;

import java.util.Calendar;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.config.Config;

public class CacheOptionDateView extends CacheOptionView {

	private boolean isselectedOptionDate = false;

	public CacheOptionDateView(CacheOption cacheOption) {
		super(cacheOption);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createCacheOption() {

		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		Calendar today = Calendar.getInstance();
		final TextView tv_date = new TextView(mContext);
		tv_date.setLayoutParams(param);
		tv_date.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv_date.setGravity(Gravity.CENTER);
		tv_date.setTextColor(Color.parseColor("#000000"));
		ll_body.addView(tv_date);

		final DatePicker datePicker = new DatePicker(SimiManager.getIntance()
				.getCurrentActivity());
		datePicker.setSaveFromParentEnabled(false);
		datePicker.setSaveEnabled(true);
		OnDateChangedListener listener = new OnDateChangedListener() {
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
				if (isAddPrice && !isselectedOptionDate) {
					mCacheOption.setCompleteRequired(true);
					updatePriceHeader(Config.getInstance().getPrice(
							""
									+ mCacheOption.getAllOption().get(0)
											.getOptionPrice()));
					updatePriceForParent(mCacheOption.getAllOption().get(0),ADD_OPERATOR);
					isAddPrice = false;
				}
			}
		};
		if (mCacheOption.getDay() > 0 && mCacheOption.getMonth() > 0
				&& mCacheOption.getYear() > 0) {
			isselectedOptionDate = true;
			String day = "" + mCacheOption.getDay();
			if (mCacheOption.getMonth() < 10) {
				day = "0" + mCacheOption.getMonth();
			}
			String month = "" + mCacheOption.getMonth();
			if (mCacheOption.getMonth() < 10) {
				month = "0" + mCacheOption.getMonth();
			}
			tv_date.setText(day + "-" + month + "-" + mCacheOption.getYear());
			mCacheOption.setCompleteRequired(true);
//			if (!isShowWhenStart) {
			updatePriceHeader(Config.getInstance().getPrice(
					"" + mCacheOption.getAllOption().get(0).getOptionPrice()));
//			}
			updatePriceForParent(mCacheOption.getAllOption().get(0),ADD_OPERATOR);
			datePicker.init(mCacheOption.getYear(),
					mCacheOption.getMonth() - 1, mCacheOption.getDay(),
					listener);
		} else {
			datePicker.init(today.get(Calendar.YEAR),
					today.get(Calendar.MONTH),
					today.get(Calendar.DAY_OF_MONTH), listener);
		}
		datePicker.setLayoutParams(param);
		ll_body.addView(datePicker);
		datePicker.setCalendarViewShown(false);
	}

}
