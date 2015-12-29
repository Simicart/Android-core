package com.simicart.core.shortcutbadger.impl;

import android.content.Context;
import android.content.Intent;

import com.simicart.core.shortcutbadger.ShortcutBadgeException;
import com.simicart.core.shortcutbadger.ShortcutBadger;

/**
 * @author Gernot Pansy
 */
public class ApexHomeBadger extends ShortcutBadger {

	private static final String INTENT_UPDATE_COUNTER = "com.anddoes.launcher.COUNTER_CHANGED";
	private static final String PACKAGENAME = "package";
	private static final String COUNT = "count";
	private static final String CLASS = "class";

	public ApexHomeBadger(Context context) {
		super(context);
	}

	@Override
	protected void executeBadge(int badgeCount) throws ShortcutBadgeException {

		Intent intent = new Intent(INTENT_UPDATE_COUNTER);
		intent.putExtra(PACKAGENAME, getContextPackageName());
		intent.putExtra(COUNT, badgeCount);
		intent.putExtra(CLASS, getEntryActivityName());
		mContext.sendBroadcast(intent);
	}
}
