package com.simicart.core.common;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Typeface;

public final class FontsOverride {

	public static void setDefaultFont(Context context,
			String staticTypefaceFieldName, String fontAssetName) {
		try {
			final Typeface regular = Typeface.createFromAsset(
					context.getAssets(), fontAssetName);
			replaceFont(staticTypefaceFieldName, regular, regular, regular,
					regular);
		} catch (Exception e) {
		}
	}

	public static void setDefaultFont(Context context,
			String staticTypefaceFieldName, String fontRegular,
			String fontBold, String fontItalic, String fontBoldItalic) {
		try {
			final Typeface regular = Typeface.createFromAsset(
					context.getAssets(), fontRegular);
			final Typeface bold = Typeface.createFromAsset(context.getAssets(),
					fontRegular);
			final Typeface italic = Typeface.createFromAsset(
					context.getAssets(), fontRegular);
			final Typeface bolditalic = Typeface.createFromAsset(
					context.getAssets(), fontRegular);
			replaceFont(staticTypefaceFieldName, regular, bold, italic,
					bolditalic);
		} catch (Exception e) {
		}
	}

	protected static void replaceFont(String staticTypefaceFieldName,
			final Typeface regular, final Typeface bold, final Typeface italic,
			final Typeface boldItalic) {
		try {
			final Field StaticField = Typeface.class
					.getDeclaredField(staticTypefaceFieldName);
			StaticField.setAccessible(true);
			StaticField.set(null, regular);

			// change font bold/italic
			Field sDefaults = Typeface.class.getDeclaredField("sDefaults");
			sDefaults.setAccessible(true);
			sDefaults.set(null, new Typeface[] { regular, bold, italic,
					boldItalic });
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}