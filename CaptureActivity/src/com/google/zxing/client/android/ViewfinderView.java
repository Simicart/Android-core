/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.camera.CameraManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	private static final long ANIMATION_DELAY = 800L;
	private static final int CURRENT_POINT_OPACITY = 0xA0;
	private static final int MAX_RESULT_POINTS = 20;
	private static final int POINT_SIZE = 6;

	private CameraManager cameraManager;
	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int laserColor;
	private final int resultPointColor;
	private int scannerAlpha;
	private List<ResultPoint> possibleResultPoints;
	private List<ResultPoint> lastPossibleResultPoints;
	String ua;

	private Path mPath;

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		resultPointColor = resources.getColor(R.color.possible_result_points);
		scannerAlpha = 0;
		possibleResultPoints = new ArrayList<ResultPoint>(5);
		lastPossibleResultPoints = null;
		ua = new WebView(getContext()).getSettings().getUserAgentString();

	}

	public void setCameraManager(CameraManager cameraManager) {
		this.cameraManager = cameraManager;
	}

	@SuppressLint({ "NewApi", "DrawAllocation" })
	@Override
	public void onDraw(Canvas canvas) {
		if (cameraManager == null) {
			return; // not ready yet, early draw before done configuring
		}
		Rect frame = cameraManager.getFramingRect();
		Rect previewFrame = cameraManager.getFramingRectInPreview();
		if (frame == null || previewFrame == null) {
			return;
		}
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// Draw the exterior (i.e. outside the framing rect) darkened
		// paint.setColor(resultBitmap != null ? resultColor : maskColor);
		paint.setColor(maskColor);
		// left,top,right,bottom
		Bitmap bm_top_right = BitmapFactory.decodeResource(getResources(),
				R.drawable.topright_icon);
		Bitmap bm_top_left = BitmapFactory.decodeResource(getResources(),
				R.drawable.topleft_icon);
		Bitmap bm_bottom_right = BitmapFactory.decodeResource(getResources(),
				R.drawable.bottomright_icon);
		Bitmap bm_bottom_left = BitmapFactory.decodeResource(getResources(),
				R.drawable.bottomleft_icon);

		int middle = frame.height() / 2 + frame.top;
		 if (ua.contains("Mobile")) {
		canvas.drawRect(0, 0, width, middle - 1
				- (frame.right - frame.left - 3) / 2 - bm_top_left.getHeight()
				/ 2, paint);
		canvas.drawRect(0, middle + 2 + (frame.right - frame.left - 3) / 2
				+ bm_top_left.getHeight() / 2, width, height, paint);
		 } else {
			 canvas.drawRect(0, 0, width, middle - 1
						- (frame.right - frame.left - 3) / 2 - bm_top_left.getHeight()
						/ 2+200, paint);
				canvas.drawRect(0, middle + 2 + (frame.right - frame.left - 3) / 2
						+ bm_top_left.getHeight() / 2-200, width, height, paint);
		 }
		// paint.setStyle(Paint.Style.STROKE);
		// paint.setColor(Color.BLACK);
		// canvas.drawRect(0, 0, width, frame.top, paint);
		// canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		// canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
		// paint);
		// canvas.drawRect(0, frame.bottom + 1, width, height, paint);

		// paint.setColor(Color.TRANSPARENT);
		// canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		// canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
		// paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			// paint.setAlpha(CURRENT_POINT_OPACITY);
			// Matrix matrix = new Matrix();
			// matrix.postRotate(90);
			// Bitmap scaledBitmap =
			// Bitmap.createScaledBitmap(resultBitmap,resultBitmap.getWidth(),resultBitmap.getHeight(),true);
			// Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0,
			// scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix,
			// true);
			// Rect rectResult = new Rect();
			// rectResult.set(frame.left, frame.top + 100, frame.right,
			// frame.bottom -100);
			// canvas.drawBitmap(rotatedBitmap, null, rectResult, paint);
		} else {
			// Draw a red "laser scanner" line through the middle to show
			// decoding is active
			paint.setColor(laserColor);
			// paint.setColor(Color.TRANSPARENT);
			paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
			scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
			// int middle = frame.height() / 2 + frame.top;
			int x = frame.height() - frame.top - 2;
			if (ua.contains("Mobile")) {
				canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1,
						middle + 2, paint);
			} else {
				canvas.drawRect(frame.left +100, middle - 1,
						frame.right - 100, middle + 2, paint);
			}
			// canvas.drawRect(frame.left -100, middle-1, frame.right + 100,
			// middle + 2, paint);
			// 1,frame.bottom -2 );
			// canvas.drawRect(rect, paint);

			float scaleX = frame.width() / (float) previewFrame.width();
			float scaleY = frame.height() / (float) previewFrame.height();
			// add anything in here
			List<ResultPoint> currentPossible = possibleResultPoints;
			List<ResultPoint> currentLast = lastPossibleResultPoints;
			int frameLeft = frame.left;
			int frameTop = frame.top;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new ArrayList<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(CURRENT_POINT_OPACITY);
				paint.setColor(resultPointColor);
				// paint.setColor(Color.TRANSPARENT);
				synchronized (currentPossible) {
					for (ResultPoint point : currentPossible) {
						canvas.drawCircle(frameLeft
								+ (int) (point.getX() * scaleX), frameTop
								+ (int) (point.getY() * scaleY), POINT_SIZE,
								paint);
					}
				}
			}
			if (currentLast != null) {
				paint.setAlpha(CURRENT_POINT_OPACITY / 2);
				paint.setColor(resultPointColor);
				// paint.setColor(Color.TRANSPARENT);
				synchronized (currentLast) {
					float radius = POINT_SIZE / 2.0f;
					for (ResultPoint point : currentLast) {
						canvas.drawCircle(frameLeft
								+ (int) (point.getX() * scaleX), frameTop
								+ (int) (point.getY() * scaleY), radius, paint);
					}
				}
			}

			if (ua.contains("Mobile")) {
				paint.setColor(Color.WHITE);
				canvas.drawBitmap(bm_top_left,
						frame.left + 2 - bm_top_left.getWidth() / 2, middle - 1
								- (frame.right - frame.left - 3) / 2
								- bm_top_left.getHeight() / 2, paint);
				canvas.drawBitmap(bm_top_right,
						frame.right - 1 - bm_top_left.getWidth() / 2, middle
								- 1 - (frame.right - frame.left - 3) / 2
								- bm_top_left.getHeight() / 2, paint);
				canvas.drawBitmap(bm_bottom_right, frame.right - 1
						- bm_top_left.getWidth() / 2,
						middle + 2 + (frame.right - frame.left - 3) / 2
								- bm_top_left.getHeight() / 2, paint);
				canvas.drawBitmap(bm_bottom_left,
						frame.left + 2 - bm_top_left.getWidth() / 2, middle + 2
								+ (frame.right - frame.left - 3) / 2
								- bm_top_left.getHeight() / 2, paint);
				postInvalidateDelayed(ANIMATION_DELAY, frame.left - POINT_SIZE,
						frame.top - POINT_SIZE, frame.right + POINT_SIZE,
						frame.bottom + POINT_SIZE);
			} else {
				paint.setColor(Color.WHITE);
				canvas.drawBitmap(bm_top_left,
						frame.left + 2 - bm_top_left.getWidth() / 2 + 100, middle - 1
								- (frame.right - frame.left - 3) / 2
								- bm_top_left.getHeight() / 2  +200, paint);
				canvas.drawBitmap(bm_top_right,
						frame.right - 1 - bm_top_left.getWidth() / 2-100, middle
								- 1 - (frame.right - frame.left - 3) / 2
								- bm_top_left.getHeight() / 2+200, paint);
				canvas.drawBitmap(bm_bottom_right, frame.right - 1
						- bm_top_left.getWidth() / 2 -100,
						middle + 2 + (frame.right - frame.left - 3) / 2
								- bm_top_left.getHeight() / 2 -200, paint);
				canvas.drawBitmap(bm_bottom_left,
						frame.left + 2 - bm_top_left.getWidth() / 2+100, middle + 2
								+ (frame.right - frame.left - 3) / 2
								- bm_top_left.getHeight() / 2-200, paint);
				postInvalidateDelayed(ANIMATION_DELAY, frame.left - POINT_SIZE,
						frame.top - POINT_SIZE, frame.right + POINT_SIZE,
						frame.bottom + POINT_SIZE);
			}

		}
	}

	public void drawViewfinder() {
		Bitmap resultBitmap = this.resultBitmap;
		this.resultBitmap = null;
		if (resultBitmap != null) {
			resultBitmap.recycle();
		}
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 *
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		List<ResultPoint> points = possibleResultPoints;
		synchronized (points) {
			points.add(point);
			int size = points.size();
			if (size > MAX_RESULT_POINTS) {
				// trim it
				points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
			}
		}
	}

}
