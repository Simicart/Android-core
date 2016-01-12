package com.simicart.core.checkout.adapter;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.simicart.core.checkout.entity.PaymentMethod;
import com.simicart.core.config.Rconfig;

public class CreditCardAdapter extends AbstractWheelTextAdapter {
	// Countries names
	private String cards[];
	// Countries flags
	private int flags[];

	/**
	 * Constructor
	 */
	public CreditCardAdapter(Context context, PaymentMethod mPaymentMethod) {
		super(context, Rconfig.getInstance().layout("core_item_ccard_layout"),
				NO_RESOURCE);
		JSONArray cc_types = null;
		try {
			cc_types = new JSONArray(mPaymentMethod.getData("cc_types"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (cc_types != null) {
			this.cards = new String[cc_types.length()];
			this.flags = new int[cc_types.length()];
			for (int i = 0; i < cc_types.length(); i++) {
				try {
					this.cards[i] = cc_types.getJSONObject(i).getString(
							"cc_name");
					this.flags[i] = this.getIcon(cc_types.getJSONObject(i)
							.getString("cc_code"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			setItemTextResource(Rconfig.getInstance().id("card_name"));
		}
	}

	public int getIcon(String code) {
		int back = Rconfig.getInstance().drawable("card_diners");
		switch (code) {
		case "VI":
			back = Rconfig.getInstance().drawable("vi");
			break;
		case "AE":
			back = Rconfig.getInstance().drawable("ae");
			break;
		case "MC":
			back = Rconfig.getInstance().drawable("mc");
			break;
		case "JCB":
			back = Rconfig.getInstance().drawable("jcb");
			break;
		case "DI":
			back = Rconfig.getInstance().drawable("di");
			break;
		default:
			break;
		}
		return back;
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		View view = super.getItem(index, cachedView, parent);
		ImageView img = (ImageView) view.findViewById(Rconfig.getInstance().id(
				"flag"));
		img.setImageResource(flags[index]);
		return view;
	}

	@Override
	public int getItemsCount() {
		return cards.length;
	}

	@Override
	public CharSequence getItemText(int index) {
		return cards[index];
	}
}
