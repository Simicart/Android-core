package com.simicart.core.common.price;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.catalog.product.entity.PriceV2;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.catalog.product.entity.ProductOption;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;

public class ProductPriceUtils {

	protected PriceV2 mPriceV2;
	protected Product mProduct;
	protected String color_Label = Config.getInstance()
			.getContent_color_string();
	protected String color_Tire = "#528F94";
	protected String color_Price = Config.getInstance().getPrice_color();
	protected String color_Price_Special = Config.getInstance()
			.getSpecial_price_color();
	protected String color_Other = "#000000";
	protected boolean isShowZero = true;

	public final String PRICE = Config.getInstance().getText("Price");
	public final String FROM = Config.getInstance().getText("From");
	public final String TO = Config.getInstance().getText("To");
	public final String CONFIG = Config.getInstance().getText(
			"Price as configured");
	public final String SPECIAL_PRICE = Config.getInstance().getText(
			"Special Price");
	public final String EXCL = Config.getInstance().getText("Excl.Tax");
	public final String INCL = Config.getInstance().getText("Incl.Tax");

	public ProductPriceUtils() {
		isShowZero = Config.getInstance().isShow_zero_price();
	}

	public String getNormalPrice() {
		String price = "";
		if (mPriceV2.getRegularPrice() > -1.00f) {
			if (mPriceV2.getRegularPrice() > mPriceV2.getPrice()
					&& mPriceV2.getPrice() != -1) {

				price = "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getPrice())
						+ "</font>    <strike><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getRegularPrice())
						+ "</font></strike>";

			} else {
				price = "<font color='"
						+ color_Label
						+ "'>"
						+ PRICE
						+ ": </font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getRegularPrice()) + "</font>";
			}
		} else {
			if (mPriceV2.getPrice() > -1) {
				price = "<font color='"
						+ color_Label
						+ "'>"
						+ PRICE
						+ ": </font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getPrice()) + "</font>";
			}
		}
		return price;
	}

	public String updateNormalPriceWithOption(ProductOption option,
			boolean isAdd) {
		String content = "";
		float regularPrice = mPriceV2.getRegularPrice();
		float price = mPriceV2.getPrice();
		float optionPrice = option.getOptionPrice();
		String optionQty = option.getOptionQty();
		int i_qty = 0;
		if (null != optionQty && !optionQty.equals("")
				&& !optionQty.equals("null")) {
			i_qty = (int) Float.parseFloat(optionQty);
		}

		if (i_qty > 1) {
			optionPrice = optionPrice * i_qty;
		}
		if (regularPrice > -1.00f) {
			if (regularPrice > price && price != -1) {

				if (isAdd) {
					price += optionPrice;
					regularPrice += optionPrice;
				} else {
					price -= optionPrice;
					regularPrice -= optionPrice;
				}

				if (price < 0) {
					price = 0;
				}
				if (regularPrice < 0) {
					regularPrice = 0;
				}

				mPriceV2.setPrice(price);
				mPriceV2.setRegularPrice(regularPrice);

				content = "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getPrice())
						+ "</font><strike><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getRegularPrice())
						+ "</font></strike>";

			} else {
				if (isAdd) {
					regularPrice += optionPrice;
				} else {
					regularPrice -= optionPrice;
				}

				if (regularPrice < 0) {
					regularPrice = 0;
				}

				mPriceV2.setRegularPrice(regularPrice);
				mPriceV2.setPrice(regularPrice);

				content = "<font color='"
						+ color_Label
						+ "'>"
						+ PRICE
						+ ": </font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getRegularPrice()) + "</font>";
			}
		} else {
			if (price > -1) {

				if (isAdd) {
					price += optionPrice;
				} else {
					price -= optionPrice;
				}

				if (price < 0) {
					price = 0;
				}

				mPriceV2.setPrice(price);

				content = "<font color='"
						+ color_Label
						+ "'>"
						+ PRICE
						+ ": </font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getPrice()) + "</font>";
			}
		}
		return content;
	}

	public String updateNormalPriceWithOption(String priceDependentOp,
			boolean isAdd) {
		String content = "";
		float regularPrice = mPriceV2.getRegularPrice();
		float price = mPriceV2.getPrice();
		if (priceDependentOp.equals("")) {
			priceDependentOp = "0";
		}
		float optionPrice = Float.parseFloat(priceDependentOp);
		if (regularPrice > -1.00f) {
			if (regularPrice > price && price != -1) {

				if (isAdd) {
					price += optionPrice;
					regularPrice += optionPrice;
				} else {
					price -= optionPrice;
					regularPrice -= optionPrice;
				}

				if (price < 0) {
					price = 0;
				}
				if (regularPrice < 0) {
					regularPrice = 0;
				}

				mPriceV2.setPrice(price);
				mPriceV2.setRegularPrice(regularPrice);

				content = "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getPrice())
						+ "</font><strike><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getRegularPrice())
						+ "</font></strike>";

			} else {
				if (isAdd) {
					regularPrice += optionPrice;
				} else {
					regularPrice -= optionPrice;
				}

				if (regularPrice < 0) {
					regularPrice = 0;
				}

				mPriceV2.setRegularPrice(regularPrice);
				mPriceV2.setPrice(regularPrice);

				content = "<font color='"
						+ color_Label
						+ "'>"
						+ PRICE
						+ ": </font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getRegularPrice()) + "</font>";
			}
		} else {
			if (price > -1) {

				if (isAdd) {
					price += optionPrice;
				} else {
					price -= optionPrice;
				}

				if (price < 0) {
					price = 0;
				}

				mPriceV2.setPrice(price);

				content = "<font color='"
						+ color_Label
						+ "'>"
						+ PRICE
						+ ": </font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getPrice()) + "</font>";
			}
		}
		return content;
	}

	public String getSpecialPrice() {
		String price = "";
		String excl_tax = "";
		String incl_tax = "";
		if ((mPriceV2.getInclTaxSpecial() > -1.00f || mPriceV2
				.getExclTaxSpecial() > -1.00f)) {

			price = "<font color='" + color_Label + "'>" + SPECIAL_PRICE
					+ ": </font>";

			if (mPriceV2.getExclTaxSpecial() > -1.00f) {
				excl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ EXCL
						+ ": "
						+ "</font><font color='"
						+ color_Price_Special
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getExclTaxSpecial()) + "</font>";
			}

			if (mPriceV2.getInclTaxSpecial() > -1.00f) {
				incl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ INCL
						+ ": "
						+ "</font><font color='"
						+ color_Price_Special
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getInclTaxSpecial()) + "</font>";
			}

			price = price + excl_tax + incl_tax;
		}

		return price;
	}

	public String updateSpecialPriceWithOption(ProductOption option,
			boolean isAdd) {
		String content = "";
		String excl_tax = "";
		String incl_tax = "";

		float optionPrice = option.getOptionPrice();
		float inclOptionPrice = option.getOption_price_incl_tax();
		float exclTaxSpecial = mPriceV2.getExclTaxSpecial();
		float inclTaxSpecial = mPriceV2.getInclTaxSpecial();
		String optionQty = option.getOptionQty();
		int i_qty = 0;
		if (null != optionQty && !optionQty.equals("")
				&& !optionQty.equals("null")) {
			i_qty = (int) Float.parseFloat(optionQty);
		}

		if (i_qty > 1) {
			optionPrice = optionPrice * i_qty;
			inclOptionPrice = inclOptionPrice * i_qty;
		}

		if (optionPrice < 0) {
			optionPrice = 0;
		}
		if (inclOptionPrice < 0) {
			inclOptionPrice = 0;
		}

		if ((inclTaxSpecial > -1.00f || exclTaxSpecial > -1.00f)) {

			content = "<font color='" + color_Label + "'>" + SPECIAL_PRICE
					+ ": </font>";

			if (exclTaxSpecial > -1.00f) {

				if (isAdd) {
					exclTaxSpecial += optionPrice;
				} else {
					exclTaxSpecial -= optionPrice;
				}

				if (exclTaxSpecial < 0) {
					exclTaxSpecial = 0;
				}

				mPriceV2.setExclTaxSpecial(exclTaxSpecial);

				excl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ EXCL
						+ ": "
						+ "</font><font color='"
						+ color_Price_Special
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getExclTaxSpecial()) + "</font>";
			}

			if (inclTaxSpecial > -1.00f) {

				if (isAdd) {
					inclTaxSpecial += inclOptionPrice;
				} else {
					inclTaxSpecial -= inclOptionPrice;
				}

				if (inclTaxSpecial < 0) {
					inclTaxSpecial = 0;
				}

				mPriceV2.setInclTaxSpecial(inclTaxSpecial);

				incl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ INCL
						+ ": "
						+ "</font><font color='"
						+ color_Price_Special
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getInclTaxSpecial()) + "</font>";
			}

			content = content + excl_tax + incl_tax;
		}
		return content;

	}

	public String updateSpecialPriceWithOption(String dependentOptionPrice,
			boolean isAdd) {
		String content = "";
		String excl_tax = "";
		String incl_tax = "";

		if (dependentOptionPrice.equals("")) {
			dependentOptionPrice = "0";
		}
		float optionPrice = Float.parseFloat(dependentOptionPrice);
		float inclOptionPrice = optionPrice;
		float exclTaxSpecial = mPriceV2.getExclTaxSpecial();
		float inclTaxSpecial = mPriceV2.getInclTaxSpecial();

		if (optionPrice < 0) {
			optionPrice = 0;
		}
		if (inclOptionPrice < 0) {
			inclOptionPrice = 0;
		}

		if ((inclTaxSpecial > -1.00f || exclTaxSpecial > -1.00f)) {

			content = "<font color='" + color_Label + "'>" + SPECIAL_PRICE
					+ ": </font>";

			if (exclTaxSpecial > -1.00f) {

				if (isAdd) {
					exclTaxSpecial += optionPrice;
				} else {
					exclTaxSpecial -= optionPrice;
				}

				if (exclTaxSpecial < 0) {
					exclTaxSpecial = 0;
				}

				mPriceV2.setExclTaxSpecial(exclTaxSpecial);

				excl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ EXCL
						+ ": "
						+ "</font><font color='"
						+ color_Price_Special
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getExclTaxSpecial()) + "</font>";
			}

			if (inclTaxSpecial > -1.00f) {

				if (isAdd) {
					inclTaxSpecial += inclOptionPrice;
				} else {
					inclTaxSpecial -= inclOptionPrice;
				}

				if (inclTaxSpecial < 0) {
					inclTaxSpecial = 0;
				}

				mPriceV2.setInclTaxSpecial(inclTaxSpecial);

				incl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ INCL
						+ ": "
						+ "</font><font color='"
						+ color_Price_Special
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getInclTaxSpecial()) + "</font>";
			}

			content = content + excl_tax + incl_tax;
		}
		return content;

	}

	public String getRegularPrice() {

		String price = "";
		if (mPriceV2.getRegularPrice() > -1.00f) {
			price = "<html><font color='"
					+ color_Price
					+ "'>"
					+ Config.getInstance().getPrice(
							"" + mPriceV2.getRegularPrice()) + "</font></html>";
		}

		return price;
	}

	public String getRegularPriceStrike() {
		String price = "";
		if (mPriceV2.getRegularPrice() > -1.00f) {
			price = "<html><strike><font color='"
					+ color_Price
					+ "'>"
					+ Config.getInstance().getPrice(
							"" + mPriceV2.getRegularPrice())
					+ "</font></strike></html>";
		}

		return price;
	}

	public String updateRegularPriceStrikeWithOption(ProductOption option,
			boolean isAdd) {
		String content = "";
		float regularPrice = mPriceV2.getRegularPrice();
		float optionPrice = option.getOptionPrice();
		String optionQty = option.getOptionQty();
		int i_qty = 0;
		if (null != optionQty && !optionQty.equals("")
				&& !optionQty.equals("null")) {
			i_qty = (int) Float.parseFloat(optionQty);
		}

		if (i_qty > 1) {
			optionPrice = optionPrice * i_qty;
		}
		if (regularPrice > -1.00f) {

			if (isAdd) {
				regularPrice += optionPrice;
			} else {
				regularPrice -= optionPrice;
			}

			if (regularPrice < 0) {
				regularPrice = 0;
			}
			mPriceV2.setRegularPrice(regularPrice);

			content = "<html><strike><font color='"
					+ color_Price
					+ "'>"
					+ Config.getInstance().getPrice(
							"" + mPriceV2.getRegularPrice())
					+ "</font></strike></html>";
		}

		return content;
	}

	public String getPriceTax() {
		String price = "";
		String excl_tax = "";
		String incl_tax = "";
		if ((mPriceV2.getExclTax() > -1.00f)
				|| (mPriceV2.getInclTax() > -1.00f && mPriceV2.getInclTax() > 0)) {
			if (mPriceV2.getExclTax() > -1.00f) {
				excl_tax = "<font color='"
						+ color_Label
						+ "'>"
						+ EXCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getExclTax()) + "</font>";
			}

			if (mPriceV2.getInclTax() > -1.00f) {
				incl_tax = "<font color='"
						+ color_Label
						+ "'>"
						+ INCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getInclTax()) + "</font>";
			}

			if (excl_tax.equals("") || incl_tax.equals("")) {
				price = price + excl_tax + incl_tax;
			} else {
				price = price + excl_tax + "<br>" + incl_tax;
			}
		}

		return price;
	}

	public String getExclTax() {
		String excl_tax = "";
		if (mPriceV2.getExclTax() > -1.00f) {
			excl_tax = "<font color='" + color_Label + "'>" + EXCL + ": "
					+ "</font><font color='" + color_Price + "'>"
					+ Config.getInstance().getPrice("" + mPriceV2.getExclTax())
					+ "</font>";
		}

		return excl_tax;
	}

	public String getInclTax() {
		String incl_tax = "";
		if (mPriceV2.getInclTax() > -1.00f) {
			incl_tax = "<font color='" + color_Label + "'>" + INCL + ": "
					+ "</font><font color='" + color_Price + "'>"
					+ Config.getInstance().getPrice("" + mPriceV2.getInclTax())
					+ "</font>";
		}
		return incl_tax;
	}

	public String updatePriceTaxWithOption(ProductOption option, boolean isAdd) {
		String content = "";
		String excl_tax = "";
		String incl_tax = "";

		float exclTax = mPriceV2.getExclTax();
		float inclTax = mPriceV2.getInclTax();
		float optionPrice = option.getOptionPrice();
		float inclOptionPrice = option.getOption_price_incl_tax();
		String optionQty = option.getOptionQty();
		int i_qty = 0;
		if (null != optionQty && !optionQty.equals("")
				&& !optionQty.equals("null")) {
			i_qty = (int) Float.parseFloat(optionQty);
		}

		if (inclOptionPrice < 0) {
			inclOptionPrice = 0;
		}

		if (optionPrice < 0) {
			optionPrice = 0;
		}

		if (i_qty > 1) {
			optionPrice = optionPrice * i_qty;
			inclOptionPrice = inclOptionPrice * i_qty;
		}
		if ((exclTax > -1) || inclTax > -1) {
			if (exclTax > -1.00f) {

				if (isAdd) {
					exclTax += optionPrice;
				} else {
					exclTax -= optionPrice;
				}

				if (exclTax < 0) {
					exclTax = 0;
				}

				mPriceV2.setExclTax(exclTax);

				excl_tax = "<font color='"
						+ color_Label
						+ "'>"
						+ EXCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getExclTax()) + "</font>";
			}

			if (inclTax > -1) {

				if (isAdd) {
					inclTax += inclOptionPrice;
				} else {
					inclTax -= inclOptionPrice;
				}

				if (inclTax < 0) {
					inclTax = 0;
				}

				mPriceV2.setInclTax(inclTax);

				incl_tax = "<font color='"
						+ color_Label
						+ "'>"
						+ INCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getInclTax()) + "</font>";
			}

			if (excl_tax.equals("") || incl_tax.equals("")) {
				content = content + excl_tax + incl_tax;
			} else {
				content = content + excl_tax + "<br>" + incl_tax;
			}
		}

		return content;
	}

	public String updatePriceTaxWithOption(String optionDependent, boolean isAdd) {
		String content = "";
		String excl_tax = "";
		String incl_tax = "";

		float exclTax = mPriceV2.getExclTax();
		float inclTax = mPriceV2.getInclTax();
		if (optionDependent.equals("")) {
			optionDependent = "0";
		}
		float optionPrice = Float.parseFloat(optionDependent);
		float inclOptionPrice = optionPrice;

		if (inclOptionPrice < 0) {
			inclOptionPrice = 0;
		}

		if (optionPrice < 0) {
			optionPrice = 0;
		}

		if ((exclTax > -1) || inclTax > -1) {
			if (exclTax > -1.00f) {

				if (isAdd) {
					exclTax += optionPrice;
				} else {
					exclTax -= optionPrice;
				}

				if (exclTax < 0) {
					exclTax = 0;
				}

				mPriceV2.setExclTax(exclTax);

				excl_tax = "<font color='"
						+ color_Label
						+ "'>"
						+ EXCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getExclTax()) + "</font>";
			}

			if (inclTax > -1) {

				if (isAdd) {
					inclTax += inclOptionPrice;
				} else {
					inclTax -= inclOptionPrice;
				}

				if (inclTax < 0) {
					inclTax = 0;
				}

				mPriceV2.setInclTax(inclTax);

				incl_tax = "<font color='"
						+ color_Label
						+ "'>"
						+ INCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getInclTax()) + "</font>";
			}

			if (excl_tax.equals("") || incl_tax.equals("")) {
				content = content + excl_tax + incl_tax;
			} else {
				content = content + excl_tax + "<br>" + incl_tax;
			}
		}

		return content;
	}

	public String getTaxFromTo() {
		String price_from = getTaxFrom();
		String price_to = getTaxTo();

		if (price_from.equals("") || price_to.equals("")) {
			return price_from + price_to;
		} else {
			return price_from + "<br>" + price_to;
		}
	}

	public String getTaxFrom() {
		String price_from = "";

		if (mPriceV2.getExclTaxFrom() > -1.00f
				|| mPriceV2.getInclTaxFrom() > -1.00f) {
			price_from = "<font color='" + color_Label + "'>" + FROM
					+ ": </font>";
			String excl_tax = "";
			String incl_tax = "";
			if (mPriceV2.getExclTaxFrom() > -1.00f) {
				excl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ EXCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getExclTaxFrom()) + "</font>";
			}

			if (mPriceV2.getInclTaxFrom() > -1.00f) {
				incl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ INCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getInclTaxFrom()) + "</font>";
			}

			price_from = price_from + excl_tax + incl_tax;
		}

		return price_from;
	}

	public String getTaxTo() {
		String price_to = "";
		if (mPriceV2.getExclTaxTo() > -1.00f
				|| mPriceV2.getInclTaxTo() > -1.00f) {
			price_to = "<font color='" + color_Label + "'>" + TO + ": </font>";
			String excl_tax = "";
			String incl_tax = "";
			if (mPriceV2.getExclTaxTo() > -1.00f) {
				excl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ EXCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getExclTaxTo()) + "</font>";
			}

			if (mPriceV2.getInclTaxTo() > -1.00f) {
				incl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ Config.getInstance().getText(INCL)
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getInclTaxTo()) + "</font>";
			}

			price_to = price_to + excl_tax + incl_tax;
		}

		return price_to;
	}

	public String getPriceMinimal() {
		if (mPriceV2.getMinimalPrice() > -1.00f) {
			String minimal_label = "";
			if (mPriceV2.getMinimalPriceLabel() != null) {
				minimal_label = "<font color='"
						+ color_Label
						+ "'>"
						+ Config.getInstance().getText(
								mPriceV2.getMinimalPriceLabel()) + ": </font>";
			}

			return (minimal_label + getAsLowAsPrice());
		} else {
			return getTaxMinimal();
		}
	}

	public String getAsLowAsPrice() {
		if (mPriceV2.getMinimalPrice() > -1.00f) {
			String minimal = "<font color='"
					+ color_Price
					+ "'>"
					+ Config.getInstance().getPrice(
							"" + mPriceV2.getMinimalPrice()) + "</font>";
			return minimal;
		}

		return "";
	}

	public String getTaxMinimal() {
		String minimal_label = "";
		if (mPriceV2.getMinimalPriceLabel() != null) {
			minimal_label = "<font color='"
					+ color_Label
					+ "'>"
					+ Config.getInstance().getText(
							mPriceV2.getMinimalPriceLabel()) + ": </font>";
		}

		if (mPriceV2.getExclTaxMinimal() > -1.00f
				|| mPriceV2.getInclTaxMinimal() > -1.00f) {
			String incl_tax = "";
			String excl_tax = "";
			if (mPriceV2.getExclTaxMinimal() > -1.00f) {
				excl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ EXCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getExclTaxMinimal()) + "</font>";
			}
			if (mPriceV2.getInclTaxMinimal() > -1.00f) {
				incl_tax = "<br><font color='"
						+ color_Label
						+ "'>"
						+ INCL
						+ ": "
						+ "</font><font color='"
						+ color_Price
						+ "'>"
						+ Config.getInstance().getPrice(
								"" + mPriceV2.getInclTaxMinimal()) + "</font>";
			}
			return minimal_label + excl_tax + incl_tax;
		}

		return "";

	}

	public String getTaxSpecial() {
		String taxSpecial = "";
		String excl_tax = "";
		String incl_tax = "";
		if (mPriceV2.getExclTaxSpecial() > -1.00f) {
			excl_tax = "<br><font color='"
					+ color_Label
					+ "'>"
					+ EXCL
					+ ": "
					+ "</font><font color='"
					+ color_Price_Special
					+ "'>"
					+ Config.getInstance().getPrice(
							"" + mPriceV2.getExclTaxSpecial()) + "</font>";
		}

		if (mPriceV2.getInclTaxSpecial() > -1.00f) {
			incl_tax = "<br><font color='"
					+ color_Label
					+ "'>"
					+ INCL
					+ ": "
					+ "</font><font color='"
					+ color_Price_Special
					+ "'>"
					+ Config.getInstance().getPrice(
							"" + mPriceV2.getInclTaxSpecial()) + "</font>";
		}
		taxSpecial = excl_tax + incl_tax;
		return taxSpecial;
	}

	public String getPriceConfig() {
		String price = "";
		String excl_tax = "";
		String incl_tax = "";
		if (mPriceV2.getProductPriceConfig() > -1.00f) {
			price = "<br><font color='"
					+ color_Label
					+ "'>"
					+ CONFIG
					+ ": "
					+ "</font><font color='"
					+ color_Price
					+ "'>"
					+ Config.getInstance().getPrice(
							"" + mPriceV2.getProductPriceConfig()) + "</font>";
			return price;

		} else {
			if (mPriceV2.getExclTaxConfig() > -1.00f
					|| mPriceV2.getInclTaxConfig() > -1.00f) {
				price = "<font color='" + color_Label + "'>" + CONFIG
						+ ": </font>";

				if (mPriceV2.getExclTaxConfig() > -1.00f) {
					excl_tax = "<br><font color='"
							+ color_Label
							+ "'>"
							+ EXCL
							+ ": "
							+ "</font><font color='"
							+ color_Price
							+ "'>"
							+ Config.getInstance().getPrice(
									"" + mPriceV2.getExclTaxConfig())
							+ "</font>";
				}

				if (mPriceV2.getInclTaxConfig() > -1.00f) {
					incl_tax = "<br><font color='"
							+ color_Label
							+ "'>"
							+ INCL
							+ ": "
							+ "</font><font color='"
							+ color_Price
							+ "'>"
							+ Config.getInstance().getPrice(
									"" + mPriceV2.getInclTaxConfig())
							+ "</font>";
				}

				price = price + excl_tax + incl_tax;
			}
		}
		return price;
	}

	public String updatePriceConfigWithOption(ProductOption option,
			boolean isAdd) {
		String content = "";
		String excl_tax = "";
		String incl_tax = "";

		float exclTaxConfig = mPriceV2.getExclTaxConfig();
		float inclTaxConfig = mPriceV2.getInclTaxConfig();
		float optionPrice = option.getOptionPrice();
		float inclOptionPrice = option.getOption_price_incl_tax();
		float priceConfig = mPriceV2.getProductPriceConfig();

		String optionQty = option.getOptionQty();
		int i_qty = 0;
		if (null != optionQty && !optionQty.equals("")
				&& !optionQty.equals("null")) {
			i_qty = (int) Float.parseFloat(optionQty);
		}

		if (i_qty > 1) {
			optionPrice = optionPrice * i_qty;
			inclOptionPrice = inclOptionPrice * i_qty;
		}
		if (inclOptionPrice > -1) {
			if (exclTaxConfig > -1.00f || inclTaxConfig > -1.00f) {
				content = "<font color='" + color_Label + "'>" + CONFIG
						+ ": </font>";

				if (exclTaxConfig > -1.00f) {

					if (isAdd) {
						exclTaxConfig += optionPrice;
					} else {
						exclTaxConfig -= optionPrice;
					}

					if (exclTaxConfig < 0) {
						exclTaxConfig = 0;
					}

					mPriceV2.setExclTaxConfig(exclTaxConfig);

					excl_tax = "<br><font color='"
							+ color_Label
							+ "'>"
							+ EXCL
							+ ": "
							+ "</font><font color='"
							+ color_Price
							+ "'>"
							+ Config.getInstance().getPrice(
									"" + mPriceV2.getExclTaxConfig())
							+ "</font>";
				}

				if (inclTaxConfig > -1.00f) {

					if (isAdd) {
						inclTaxConfig += inclOptionPrice;
					} else {
						inclTaxConfig -= inclOptionPrice;
					}

					if (inclTaxConfig < 0) {
						inclTaxConfig = 0;
					}

					mPriceV2.setInclTaxConfig(inclTaxConfig);

					incl_tax = "<br><font color='"
							+ color_Label
							+ "'>"
							+ INCL
							+ ": "
							+ "</font><font color='"
							+ color_Price
							+ "'>"
							+ Config.getInstance().getPrice(
									"" + mPriceV2.getInclTaxConfig())
							+ "</font>";
				}

				content = content + excl_tax + incl_tax;
			}
		} else {
			if (isAdd) {
				priceConfig += optionPrice;
			} else {
				priceConfig -= optionPrice;
			}

			if (priceConfig < 0) {
				priceConfig = 0;
			}

			mPriceV2.setProductPriceConfig(priceConfig);

			content = "<br><font color='"
					+ color_Label
					+ "'>"
					+ CONFIG
					+ ": "
					+ "</font><font color='"
					+ color_Price
					+ "'>"
					+ Config.getInstance().getPrice(
							"" + mPriceV2.getProductPriceConfig()) + "</font>";
		}

		return content;

	}

	public String updateProductPriceConfigWithOption(ProductOption option,
			boolean isAdd) {
		String price = "";
		float configPrice = mPriceV2.getProductPriceConfig();
		Log.e("ProductPriceUtils updateProductprice ", "" + configPrice);
		float optionPrice = option.getOptionPrice();
		String optionQty = option.getOptionQty();
		int i_qty = 0;
		if (null != optionQty && !optionQty.equals("")
				&& !optionQty.equals("null")) {
			i_qty = (int) Float.parseFloat(optionQty);
		}

		if (i_qty > 1) {
			optionPrice = optionPrice * i_qty;
		}

		if (configPrice > -1) {
			if (isAdd) {
				configPrice += optionPrice;
			} else {
				configPrice -= optionPrice;
			}

			if (configPrice < 0) {
				configPrice = 0;
			}

			mPriceV2.setProductPriceConfig(configPrice);
			price = "<font color='"
					+ color_Label
					+ "'>"
					+ Config.getInstance().getText("Price as configured")
					+ ": </font> "
					+ "<font color='"
					+ color_Price
					+ "'>"
					+ Config.getInstance().getPrice(
							"" + mPriceV2.getProductPriceConfig()) + "</font>";
		}
		return price;
	}

	public String getPriceTireShort() {
		String price = "";
		String[] tier = mPriceV2.getTierPrice();
		if (tier != null && tier.length > 0) {
			price = "<font color='" + color_Tire + "'>" + tier[0] + "</font>";
			if (tier.length > 1) {
				price += "<br><font color='" + color_Tire + "'>" + "......"
						+ ": </font>";
			}
		}

		return price;
	}

	public String getPriceTireFull() {
		String price = "";
		String[] tier = mPriceV2.getTierPrice();
		if (tier != null && tier.length > 0) {
			for (int i = 0; i < tier.length; i++) {
				if (i == 0) {
					price = "<font color='" + color_Tire + "'>" + tier[0]
							+ "</font>";
				} else {
					price += "<br><font color='" + color_Tire + "'>" + tier[i]
							+ "</font>";
				}
			}
		}
		return price;
	}

	public String getPriceOther() {
		String price = "";
		if (mProduct.getOther_infor() != null) {
			int n = mProduct.getOther_infor().length();
			try {
				for (int i = 0; i < n; i++) {
					JSONObject js_other = mProduct.getOther_infor()
							.getJSONObject(i);

					if (i == 0) {
						if (!js_other.getString(Constants.LABEL).equals("")) {
							price += "<font color='" + color_Other + "'>"
									+ js_other.getString(Constants.LABEL)
									+ "</font>";
						}
						if (!js_other.getString(Constants.VALUE).equals("")) {
							price += "<font color='" + color_Other + "'>"
									+ js_other.getString(Constants.VALUE)
									+ "</font>";
						}
					} else {
						if (!js_other.getString(Constants.LABEL).equals("")) {
							price += "<br><font color='" + color_Other + "'>"
									+ js_other.getString(Constants.LABEL)
									+ "</font>";
						}

						if (!js_other.getString(Constants.VALUE).equals("")) {
							price += "<br><font color='" + color_Other + "'>"
									+ js_other.getString(Constants.VALUE)
									+ "</font>";
						}
					}
				}
			} catch (JSONException e) {
				return price;
			}
		}
		return price;
	}

	public String getColorLabel() {
		return color_Label;
	}

	public void setColorLabel(String color_Label) {
		this.color_Label = color_Label;
	}

	public String getColorTire() {
		return color_Tire;
	}

	public void setColorTire(String color_Tire) {
		this.color_Tire = color_Tire;
	}

	public String getColorPrice() {
		return color_Price;
	}

	public void setColorPrice(String color_Price) {
		this.color_Price = color_Price;
	}

	public String getColorOther() {
		return color_Other;
	}

	public void setColorOther(String color_Other) {
		this.color_Other = color_Other;
	}

	public void setPriceV2(PriceV2 priceV2) {
		this.mPriceV2 = priceV2;
	}

	public PriceV2 getPriceV2() {
		return this.mPriceV2;
	}

	public void setProduct(Product product) {
		this.mProduct = product;
		if (null != product.getPriceV2()) {
			this.mPriceV2 = product.getPriceV2();
		} else {
			this.mPriceV2 = new PriceV2();
		}
	}

	public Product getProduct() {
		return this.mProduct;
	}

	public String updateNormalPriceConfigableWithOption(ProductOption option,
			boolean isAdd) {
		// TODO Auto-generated method stub
		return null;
	}

}
