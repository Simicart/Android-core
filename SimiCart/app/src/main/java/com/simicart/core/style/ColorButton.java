package com.simicart.core.style;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.simicart.core.common.Utils;

public class ColorButton extends Button {
	private float radius = 7;
	private int color = Color.RED;

	public ColorButton(Context context) {
		super(context);
	}

	public static final int[] ColorItem = { 0x7f010000, 0x7f010001 };

	public ColorButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray typedArray = context
				.obtainStyledAttributes(attrs, ColorItem);
		radius = typedArray.getFloat(0, 7);
		color = typedArray.getColor(1, Color.RED);
		typedArray.recycle();

		setCustomBackground(getStateListDrawable());
	}

	public void setStyle(float radius, int color) {
		this.radius = radius;
		this.color = color;
		setCustomBackground(getStateListDrawable());
	}

	private LayerDrawable getDrawable(boolean tap) {
		Drawable color = color();
		Drawable gradient = gradient(tap);

		Drawable[] layers = { shadow(darker(this.color, 0.9f)), color, gradient };
		LayerDrawable layerDrawable = new LayerDrawable(layers);
		return layerDrawable;
	}

	private ShapeDrawable color() {
		ShapeDrawable shape = new ShapeDrawable(new RoundRectShape(corners(),
				null, null));
		shape.getPaint().setColor(color);
		return shape;
	}

	private GradientDrawable gradient(boolean tap) {
		int[] colors = null;
		if (tap) {
			colors = new int[] { darker(this.color, 0.9f),
					darker(this.color, 0.9f), darker(this.color, 0.9f) };
		} else {
			// colors = new int[] { Color.parseColor("#00000000"),
			// Color.parseColor("#00000000"),
			// Color.parseColor("#55000000") };
		}
		GradientDrawable mGradient = new GradientDrawable(
				Orientation.TOP_BOTTOM, colors);
		mGradient.setCornerRadii(corners());
		// mGradient.setStroke(2, Color.parseColor("#33000000"));//vien ngoai
		return mGradient;
	}

	private float[] corners() {
		float[] corners = new float[] { radius, radius, radius, radius, radius,
				radius, radius, radius };
		return corners;
	}

	private StateListDrawable getStateListDrawable() {
		LayerDrawable tap = getDrawable(true);
		LayerDrawable normal = getDrawable(false);

		StateListDrawable stateListDrawable = new StateListDrawable();
		stateListDrawable.addState(new int[] { android.R.attr.state_pressed },
				tap);
		stateListDrawable.addState(new int[] { android.R.attr.state_focused },
				tap);
		stateListDrawable.addState(new int[] { android.R.attr.state_enabled },
				normal);
		return stateListDrawable;
	}

	private void setCustomBackground(Drawable drawable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setCustomBackgroundJellyBean(drawable);
		} else {
			setCustomBackgroundDrawable(drawable);
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setCustomBackgroundJellyBean(Drawable drawable) {
		try {
			setBackground(drawable);
		} catch (NoSuchMethodError e) {
			setCustomBackgroundDrawable(drawable);
		}
	}

	@SuppressWarnings("deprecation")
	private void setCustomBackgroundDrawable(Drawable drawable) {
		setBackgroundDrawable(drawable);
	}

	private ShapeDrawable shadow(String color) {
		RoundRectShape rect = new RoundRectShape(corners(), null, null);
		ShapeDrawable shape = new ShapeDrawable(rect);
		shape.getPaint().setStrokeWidth(1);
		shape.getPaint().setColor(Color.parseColor(color));
		shape.setPadding(1, 0, 0, Utils.getValueDp(3));
		return shape;
	}

	private ShapeDrawable shadow(int color) {
		RoundRectShape rect = new RoundRectShape(corners(), null, null);
		ShapeDrawable shape = new ShapeDrawable(rect);
		shape.getPaint().setStrokeWidth(1);
		shape.getPaint().setColor(color);
		shape.setPadding(1, 0, 0, Utils.getValueDp(3));
		return shape;
	}

	public static int darker(int color, float factor) {
		int a = Color.alpha(color);
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);

		return Color.argb(a, Math.max((int) (r * factor), 0),
				Math.max((int) (g * factor), 0),
				Math.max((int) (b * factor), 0));
	}

}
