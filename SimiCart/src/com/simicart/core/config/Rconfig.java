package com.simicart.core.config;

import android.content.Context;

import com.simicart.core.base.manager.SimiManager;

public class Rconfig {

	public static String NAME_R = "com.magestore.simicart.R";
	private static Rconfig instance;
	private Context mContext;

	private Rconfig() {
		mContext = SimiManager.getIntance().getCurrentContext();
	}

	public static Rconfig getInstance() {
		if (null == instance) {
			instance = new Rconfig();
		}

		return instance;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public String getPackageName() {
		int count = NAME_R.length() - 2;
		return NAME_R.substring(0, count);
	}

	public int getIdLayout(String name) {
		return mContext.getResources().getIdentifier(name, "id",
				getPackageName());
	}

	public int layout(String name) {
		return mContext.getResources().getIdentifier(name, "layout",
				getPackageName());
	}

	public int getIdDraw(String name) {
		return mContext.getResources().getIdentifier(name, "drawable",
				getPackageName());
	}

	public int getId(String name, String res) {
		return mContext.getResources().getIdentifier(name, res,
				getPackageName());
	}

	public int[] getArrayStyleable(String res) {
		String fullName = getPackageName() + ".R$styleable";
		Class<?> style_class;
		try {
			style_class = Class.forName(fullName);
			int[] array = (int[]) style_class.getField(res).get(null);
			return array;
		} catch (ClassNotFoundException | IllegalAccessException
				| IllegalArgumentException | NoSuchFieldException e) {
		}

		return null;

	}

	public int getstyleable(String res) {
		return mContext.getResources().getIdentifier(res, "styleable",
				getPackageName());
	}

	/**
	 * This method is used by SplashBlock
	 * 
	 * @param context
	 * @param name
	 *            : name of resource
	 * @param res
	 *            : kind of resource : id or layout
	 * @return : id of resource
	 */
	public int getId(Context context, String name, String res) {
		return context.getResources()
				.getIdentifier(name, res, getPackageName());
	}

	public int id(String name) {
		return mContext.getResources().getIdentifier(name, "id",
				getPackageName());
	}

	public int getAttr(String name) {
		return mContext.getResources().getIdentifier(name, "attr",
				getPackageName());
	}

	public int drawable(String name) {
		return mContext.getResources().getIdentifier(name, "drawable",
				getPackageName());
	}

	public int string(String name) {
		return mContext.getResources().getIdentifier(name, "string",
				getPackageName());
	}

	public int idSupportV7search_mag_icon() {
		return android.support.v7.appcompat.R.id.search_mag_icon;
	}

	public int idSupportV7search_close_btn() {
		return android.support.v7.appcompat.R.id.search_close_btn;
	}
}
