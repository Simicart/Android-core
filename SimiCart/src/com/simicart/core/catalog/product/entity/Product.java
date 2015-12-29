package com.simicart.core.catalog.product.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Constants;
import com.simicart.core.config.DataLocal;

public class Product extends SimiEntity {
	protected String mStock;
	protected String mID;
	protected int mMaxQty;
	protected int mReviewNumbers;
	protected ArrayList<Integer> mStars;
	protected float mPrice;
	protected float mMinPrice;
	protected float mMaxPrice;
	protected float mRegularPrice;
	protected float mRate;
	protected String mShowPriceV2;
	protected String[] mImages; // for product detail
	protected String mImage; // for product category
	protected String mMinimalPriceLabel;// for product category
	protected String mDecripition;
	protected String mShortDecripition;
	protected String mType;
	protected String mManufacturer;
	protected String mName;
	protected JSONArray mOtherInfor;
	protected ArrayList<CacheOption> mOptions;
	protected ArrayList<Attributes> mAttributes;
	protected String cachePriceOptionDependent = "";
	protected PriceV2 mPriceV2;
	protected boolean isAddedPriceDependent = false;

	public void setAddedPriceDependent(boolean isAddedPriceDependent) {
		this.isAddedPriceDependent = isAddedPriceDependent;
	}

	public boolean isAddedPriceDependent() {
		return isAddedPriceDependent;
	}

	public String getDependentPriceOption() {
		String priceOption = "";
		if (!isCompleteDependent()) {
			return priceOption;
		}
		String dependentId = getDependentID();
		if (!dependentId.equals("")) {
			int dem = 0;
			float price = 0;
			for (CacheOption cacheOption : getOptions()) {
				if (cacheOption.isDependence()) {
					if (cacheOption.getDependence_option_id_selected()
							.containsKey(dependentId)) {
						price += Float.parseFloat(cacheOption
								.getDependence_option_id_selected().get(
										dependentId));
						dem++;
					}
				}
			}
			if (dem > 0) {
				if (DataLocal.isCloud) {
					priceOption = "" + price / dem;
				} else {
					priceOption = "" + price;
				}
			}
		}
		return priceOption;

	}

	public void setCachePriceOptionDependent(String cachePriceOptionDependent) {
		this.cachePriceOptionDependent = cachePriceOptionDependent;
	}

	public String getCachePriceOptionDependent() {
		return cachePriceOptionDependent;
	}

	public boolean isCompleteDependent() {
		for (CacheOption cacheOption : getOptions()) {
			if (cacheOption.isDependence()) {
				if (!cacheOption.isChecked()) {
					return false;
				}
			}
		}
		return true;
	}

	private String getDependentID() {
		String dependentId = "";
		ArrayList<String> lisstDependent = currentListDependent();

		if (lisstDependent != null && lisstDependent.size() > 0) {
			dependentId = lisstDependent.get(0);
		}

		return dependentId;
	}

	public void updateCurrentListDependence() {
		ArrayList<String> lisstDependent = currentListDependent();
		if (lisstDependent != null) {
			for (CacheOption cacheOption : getOptions()) {
				if (cacheOption.isDependence()) {
					for (ProductOption option : cacheOption.getAllOption()) {
						option.setCurrent_list_dependence_option_id(lisstDependent);
					}
				}
			}
		}
	}

	private ArrayList<String> currentListDependent() {
		ArrayList<String> lisstDependent = null;
		for (CacheOption cacheOption : getOptions()) {
			if (cacheOption.isDependence()
					&& cacheOption.getDependence_option_id_selected() != null) {
				ArrayList<String> listDependentCache = new ArrayList<>();
				for (String dependentID : cacheOption
						.getDependence_option_id_selected().keySet()) {
					listDependentCache.add(dependentID);
				}
				if (null == lisstDependent) {
					lisstDependent = listDependentCache;
				} else {
					lisstDependent = mergerListDependent(lisstDependent,
							listDependentCache);
				}
			}
		}

		if (lisstDependent != null) {
			for (String string : lisstDependent) {
				// Log.e("LOOOOOOOOOOOOOOOOOOOOOO", "DEID: " + string);
			}
		}
		// Log.e("LOOOOOOOOOOOOOOOOOOOOOO", "DEID: //////////////////////////");
		return lisstDependent;
	}

	private ArrayList<String> mergerListDependent(
			ArrayList<String> lisstDependent,
			ArrayList<String> listDependentCache) {
		ArrayList<String> listResult = new ArrayList<>();
		for (String dependentID : lisstDependent) {
			for (String dependenetID2 : listDependentCache) {
				if (dependentID.equals(dependenetID2)) {
					listResult.add(dependentID);
				}
			}
		}
		return listResult;
	}

	public PriceV2 getPriceV2() {
		PriceV2 v2 = null;
		try {
			String value = getData(Constants.SHOW_PRICE_V2);
			if (null != value) {
				v2 = new PriceV2();
				v2.setJSONObject(new JSONObject(value));
			}
		} catch (JSONException e) {
			return null;
		}
		return v2;
	}

	public void setPriceV2(PriceV2 v2) {
		mPriceV2 = v2;
	}

	public Product() {
		mPrice = -1;
		mMinPrice = -1;
		mMaxPrice = -1;
		mRegularPrice = -1;
		mRate = -1;
		mMaxQty = -1;
		mReviewNumbers = -1;
		mStars = new ArrayList<Integer>();
	}

	protected String[] parseImages(JSONArray arr) throws JSONException {
		int n = arr.length();
		String[] images = new String[n];
		for (int i = 0; i < n; i++) {
			String image = arr.getString(i);
			if (null != image && !image.equals("")) {
				images[i] = image;
			}
		}
		return images;
	}

	private ArrayList<Attributes> parseAttributes(JSONArray json)
			throws JSONException {
		int n = json.length();
		ArrayList<Attributes> atts = new ArrayList<Attributes>();
		for (int i = 0; i < n; i++) {
			JSONObject js_option = json.getJSONObject(i);
			Attributes att = new Attributes();
			att.setTitle(js_option.getString(Constants.TITLE));
			att.setValue(js_option.getString(Constants.VALUE));
			atts.add(att);

		}
		return atts;
	}

	private ArrayList<CacheOption> parseOptions(JSONArray json)
			throws JSONException {
		int n = json.length();
		ArrayList<CacheOption> options = new ArrayList<CacheOption>(n);

		Log.e("Product parse Options ", json.toString());

		for (int i = 0; i < n; i++) {
			JSONObject js_option = json.getJSONObject(i);
			ProductOption option = new ProductOption();
			option.setJSONObject(js_option);
			this.checkOptions(options, option);
		}
		return options;
	}

	private void checkOptions(ArrayList<CacheOption> options,
			ProductOption option) {
		if (options.size() == 0) {
			CacheOption cache = this.createOption(option);
			options.add(cache);
		} else {
			int total = options.size();

			for (int i = 0; i < total; i++) {
				if (option.getOptionTypeId().contentEquals("-1")) {
					break;
				}
				if (option.getOptionTypeId().contentEquals(
						options.get(i).getOptionTypeId())) {
					// add to list option
					options.get(i).addOption(option);
					return;
				}
			}

			// creat new option and add to list
			CacheOption cache = this.createOption(option);
			options.add(cache);
		}
		return;
	}

	private CacheOption createOption(ProductOption option) {
		CacheOption cache = new CacheOption();
		cache.setOptionTypeId(option.getOptionTypeId());
		cache.setOptionTitle(option.getOptionTitle());
		cache.setOptionType(option.getOptionType());
		cache.setPosition(option.getPosition());
		cache.setRequired(option.isRequired());
		cache.addOption(option);
		return cache;
	}

	public boolean isShow_price_v2() {
		if (null == mShowPriceV2) {
			mShowPriceV2 = getData(Constants.IS_SHOW_PRICE);

		}
		if (null != mShowPriceV2 && mShowPriceV2.equals("TRUE")
				|| mShowPriceV2.equals("true") || mShowPriceV2.equals("1")) {
			return true;
		}
		return false;
	}

	public void setShow_price_v2(boolean show_price_v2) {
		mShowPriceV2 = String.valueOf(show_price_v2);
	}

	// qty for add to cart
	public int getQty() {
		return 1;
	}

	public JSONArray getOther_infor() {
		if (null == mOtherInfor || mOtherInfor.length() == 0) {
			try {
				String otherString = getData(Constants.OTHER_INFOR);
				if (null != otherString) {
					mOtherInfor = new JSONArray(otherString);
				}
			} catch (JSONException e) {
				return null;
			}
		}
		return mOtherInfor;
	}

	public void setOther_infor(JSONArray other_infor) {
		this.mOtherInfor = other_infor;
	}

	public void setAttributes(ArrayList<Attributes> attributes) {
		this.mAttributes = attributes;
	}

	public ArrayList<Attributes> getAttributes() {
		if ((null == mAttributes) || mAttributes.size() == 0) {
			try {
				String attribute = getData(Constants.PRODUCT_ATTRIBUTES);

				if (null != attribute && !attribute.equals("")) {
					mAttributes = parseAttributes(new JSONArray(attribute));
				}
			} catch (JSONException e) {
				return null;
			}
		}
		return this.mAttributes;
	}

	public void setOptions(ArrayList<CacheOption> options) {
		this.mOptions = options;
	}

	public ArrayList<CacheOption> getOptions() {

		if ((null == mOptions) || mOptions.size() == 0) {
			try {

				String option = getData(Constants.OPTIONS);
				if (Utils.validateString(option)) {
					mOptions = parseOptions(new JSONArray(option));
				}
			} catch (JSONException e) {
				return null;
			}
		}

		return this.mOptions;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getName() {
		if (null == mName) {
			mName = getData(Constants.PRODUCT_NAME);
		}

		return this.mName;
	}

	public void setManufacturer(String manufacturer) {
		this.mManufacturer = manufacturer;
	}

	public String getManufacturer() {
		if (null == mManufacturer) {
			mManufacturer = getData(Constants.MANUFACTURER_NAME);
		}
		return this.mManufacturer;
	}

	public void setType(String type) {
		this.mType = type;
	}

	public String getType() {
		if (null == mType) {
			mType = getData(Constants.PRODUCT_TYPE);
		}
		return this.mType;
	}

	public void setRegularPrice(float regular_price) {
		this.mRegularPrice = regular_price;
	}

	public float getRegularPrice() {

		if (mRegularPrice < 0) {
			String value = getData(Constants.PRODUCT_REGULAR_PRICE);
			if (null != value && !value.equals("")) {
				mRegularPrice = Float.parseFloat(value);
			}
		}

		return this.mRegularPrice;
	}

	public void setMaxPrice(float max_price) {
		this.mMaxPrice = max_price;
	}

	public float getMaxPrice() {

		if (mMaxPrice < 0) {
			String value = getData(Constants.MAX_PRICE);
			if (null != value && !value.equals("")) {
				mMaxPrice = Float.parseFloat(value);
			}
		}

		return this.mMaxPrice;
	}

	public void setMinPrice(float min_price) {
		this.mMinPrice = min_price;
	}

	public float getMinPrice() {

		if (mMinPrice < 0) {
			String value = getData(Constants.MIN_PRICE);
			if (null != value && !value.equals("")) {
				mMinPrice = Float.parseFloat(value);
			}
		}

		return this.mMinPrice;
	}

	public void setPrice(float price) {
		this.mPrice = price;
	}

	public float getPrice() {

		if (mPrice < 0) {
			String value = getData(Constants.PRODUCT_PRICE);
			if (null != value && !value.equals("")) {
				mPrice = Float.parseFloat(value);
			}
		}

		return this.mPrice;
	}

	public void setShortDecripition(String short_decripition) {
		this.mShortDecripition = short_decripition;
	}

	public String getShortDecripition() {
		if (null == mShortDecripition) {
			mShortDecripition = getData(Constants.PRODUCT_SHORT_DESCRIPTION);
		}

		return this.mShortDecripition;
	}

	public void setDecripition(String decripition) {
		this.mDecripition = decripition;
	}

	public String getDecripition() {
		if (null == mDecripition) {
			mDecripition = getData(Constants.PRODUCT_DESCRIPTION);
		}
		return this.mDecripition;
	}

	public void setImages(String[] img) {
		this.mImages = img;
	}

	public String[] getImages() {
		if (null == mImages || mImages.length < 0) {
			try {
				String value = getData(Constants.PRODUCT_IMAGES);
				if (null != value) {
					mImages = parseImages(new JSONArray(value));
				}
			} catch (JSONException e) {
				return null;
			}
		}
		return this.mImages;
	}

	public void setImage(String img) {
		this.mImage = img;
	}

	public String getImage() {
		if (null == mImage) {
			mImage = getData(Constants.PRODUCT_IMAGE);
		}
		return this.mImage;
	}

	public void setStar(ArrayList<Integer> star) {
		this.mStars = star;
	}

	public ArrayList<Integer> getStar() {

		if (null == mStars || mStars.size() <= 0) {
			try {
				parseStars(mJSON);
			} catch (JSONException e) {
				return null;
			}
		}
		return mStars;

	}

	private void parseStars(JSONObject json) throws JSONException {
		if (json.has(Constants.STAR_NUMBER1)) {
			mStars.add(json.getInt(Constants.STAR_NUMBER1));
		}
		if (json.has(Constants.STAR_NUMBER2)) {
			mStars.add(json.getInt(Constants.STAR_NUMBER2));
		}
		if (json.has(Constants.STAR_NUMBER3)) {
			mStars.add(json.getInt(Constants.STAR_NUMBER3));
		}
		if (json.has(Constants.STAR_NUMBER4)) {
			mStars.add(json.getInt(Constants.STAR_NUMBER4));
		}
		if (json.has(Constants.STAR_NUMBER5)) {
			mStars.add(json.getInt(Constants.STAR_NUMBER5));
		}
	}

	public void setReviewNumber(int review_number) {
		this.mReviewNumbers = review_number;
	}

	public int getReviewNumber() {
		if (mReviewNumbers < 0) {

			String value = getData(Constants.PRODUCT_REVIEW_NUMBER);
			if (null != value && !value.equals("")) {
				mReviewNumbers = Integer.parseInt(value);
			}

		}
		return this.mReviewNumbers;
	}

	public void setRate(float rate) {
		this.mRate = rate;
	}

	public float getRate() {
		if (mRate < 0) {
			String value = getData(Constants.PRODUCT_RATE);
			if (null != value && !value.equals("")) {
				mRate = Float.parseFloat(value);
			}
		}
		return this.mRate;
	}

	public void setMaxQty(int max_qty) {
		this.mMaxQty = max_qty;
	}

	public int getMaxQty() {
		if (mMaxQty < 0) {
			String value = getData(Constants.MAX_QTY);
			if (null != value && !value.equals("")) {
				mMaxQty = Integer.parseInt(value);
			}
		}

		return this.mMaxQty;
	}

	public void setId(String id) {
		this.mID = id;
	}

	public String getId() {

		if (null == mID) {
			mID = getData(Constants.PRODUCT_ID);
		}

		return this.mID;
	}

	public void setStock(boolean stock) {
		this.mStock = String.valueOf(stock);
	}

	public boolean getStock() {
		if (null == mStock) {
			mStock = getData(Constants.STOCK_STATUS);
		}
		if (null != mStock
				&& (mStock.equals("TRUE") || mStock.equals("true") || mStock
						.equals("1"))) {
			return true;
		}

		return false;
	}

	public void setMinimal_price_label(String minimal_price_label) {
		this.mMinimalPriceLabel = minimal_price_label;
	}

}
