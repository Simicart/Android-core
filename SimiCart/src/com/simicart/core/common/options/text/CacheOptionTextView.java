package com.simicart.core.common.options.text;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.simicart.core.catalog.product.entity.CacheOption;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.common.Utils;
import com.simicart.core.common.options.base.CacheOptionView;
import com.simicart.core.config.Config;

public class CacheOptionTextView extends CacheOptionView {

	private boolean ischeckedOptionText = false;

	public CacheOptionTextView(CacheOption cacheOption) {
		super(cacheOption);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void createCacheOption() {
		final ProductOption option = mCacheOption.getAllOption().get(0);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		final EditText edt_option = new EditText(mContext);
		edt_option.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		edt_option.setLayoutParams(param);
		edt_option.setPadding(Utils.getValueDp(5), Utils.getValueDp(10),
				Utils.getValueDp(5), Utils.getValueDp(10));
		edt_option.setHint(Config.getInstance().getText("Text"));
		edt_option.setHintTextColor(Color.GRAY);
		edt_option.setTextColor(Color.parseColor("#000000"));
		ll_body.addView(edt_option);
		edt_option.setBackgroundResource(0);

		if ((null != mCacheOption.getText())
				&& !mCacheOption.getText().equals("")) {
			updatePriceHeader(Config.getInstance().getPrice(
					"" + option.getOptionPrice()));
			edt_option.setText(mCacheOption.getText());
			// if (!isShowWhenStart) {
			updatePriceForParent(option, ADD_OPERATOR);
			// }
			ischeckedOptionText = true;

		}

		edt_option.addTextChangedListener(new TextWatcher() {
			boolean isAddPrice = true;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				String textOption = edt_option.getText().toString();
				if (textOption.length() != 0) {
					mCacheOption.setText(edt_option.getText().toString());
					mCacheOption.setCompleteRequired(true);
					updatePriceHeader(Config.getInstance().getPrice(
							"" + option.getOptionPrice()));
					if (isAddPrice && !ischeckedOptionText) {
						isAddPrice = false;
						updatePriceForParent(option, ADD_OPERATOR);
					}
				} else {
					mCacheOption.setText("");
					mCacheOption.setCompleteRequired(false);
					if (mCacheOption.isRequired()) {
						updatePriceHeader(Config.getInstance().getText(
								"required"));
					} else {
						updatePriceHeader("");
					}
					isAddPrice = true;
					ischeckedOptionText = false;
					updatePriceForParent(option, SUB_OPERATOR);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}
}
