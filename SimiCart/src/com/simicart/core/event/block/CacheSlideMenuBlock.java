package com.simicart.core.event.block;

import com.simicart.core.slidemenu.block.SlideMenuBlock;
import com.simicart.core.slidemenu.controller.PhoneSlideMenuController;

public class CacheSlideMenuBlock {

	protected SlideMenuBlock mBlock;
	protected PhoneSlideMenuController mController;

	public SlideMenuBlock getBlock() {
		return mBlock;
	}

	public void setBlock(SlideMenuBlock mBlock) {
		this.mBlock = mBlock;
	}

	public PhoneSlideMenuController getController() {
		return mController;
	}

	public void setController(PhoneSlideMenuController mController) {
		this.mController = mController;
	}

}
