package com.simicart.core.event.block;

import android.support.v7.app.ActionBar;

import com.simicart.core.menutop.block.MenuTopBlock;

public class CacheMenuTopBlock {

	protected MenuTopBlock mBlock;
	protected ActionBar mActionBar;

	public ActionBar getmActionBar() {
		return mActionBar;
	}

	public void setmActionBar(ActionBar mActionBar) {
		this.mActionBar = mActionBar;
	}

	public MenuTopBlock getBlock() {
		return mBlock;
	}

	public void setBlock(MenuTopBlock mBlock) {
		this.mBlock = mBlock;
	}

}
