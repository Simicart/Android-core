package com.simicart.theme.matrixtheme.home.block;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Rconfig;
import com.simicart.theme.matrixtheme.home.adapter.CategoryCustomAdapter;
import com.simicart.theme.matrixtheme.home.adapter.Theme1CustomScrollView;
import com.simicart.theme.matrixtheme.home.entity.OrderProduct;

public class SpotProductHomeTheme1Block extends SimiBlock {

	private Theme1CustomScrollView lv_CategoryBottom;

	public SpotProductHomeTheme1Block(View view, Context context) {
		super(view, context);
		lv_CategoryBottom = (Theme1CustomScrollView) mView.findViewById(Rconfig
				.getInstance().id("lv_category_bottom"));
	}

	private void setBannerCategoryBottom(ArrayList<OrderProduct> categories) {
		if (categories == null) {
			return;
		}
		if (mContext != null) {
			CategoryCustomAdapter adapter = new CategoryCustomAdapter(mContext,
					categories);
			lv_CategoryBottom.setAdapter(mContext, adapter);
			YoYo.with(Techniques.Shake).duration(2000)
					.playOn(lv_CategoryBottom);
		}

	}

	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		ArrayList<OrderProduct> categories = new ArrayList<OrderProduct>();
		if (null != entity && entity.size() > 0) {
			for (SimiEntity simiEntity : entity) {
				OrderProduct category = (OrderProduct) simiEntity;
				categories.add(category);
			}
		} else {
			OrderProduct category = new OrderProduct(new JSONObject());
			category.setSpotId("fake");
			category.setSpotKey("Feature Products");
			category.setSpotName("Feature Products");
			categories.add(category);
		}
		if (categories.size() > 0) {
			setBannerCategoryBottom(categories);
		}
	}
}
