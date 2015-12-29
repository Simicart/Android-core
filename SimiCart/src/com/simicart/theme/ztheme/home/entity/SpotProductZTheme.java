package com.simicart.theme.ztheme.home.entity;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.theme.ztheme.home.common.ConstantsZTheme;

public class SpotProductZTheme extends SimiEntity {

	protected String mID;
	protected String mName;
	protected String mImage;
	protected String mKey;

	public String getID() {
		if (null == mID) {
			mID = getData(ConstantsZTheme.SPOT_ID);
		}
		return mID;
	}

	public void setID(String mID) {
		this.mID = mID;
	}

	public String getName() {
		if (null == mName) {
			mName = getData(ConstantsZTheme.SPOT_NAME);
		}
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getImage() {
		if (null == mImage) {
			mImage = getData(ConstantsZTheme.SPOT_IMAGE);
		}
		return mImage;
	}

	public void setImage(String mImage) {
		this.mImage = mImage;
	}

	public String getKey() {
		if (null == mKey) {
			mKey = getData(ConstantsZTheme.SPOT_KEY);
		}
		return mKey;
	}

	public void setKey(String mKey) {
		this.mKey = mKey;
	}

}
