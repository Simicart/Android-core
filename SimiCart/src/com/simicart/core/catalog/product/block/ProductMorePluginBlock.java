package com.simicart.core.catalog.product.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Config;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.block.EventBlock;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionButton;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionsMenu;

public class ProductMorePluginBlock extends SimiBlock {
	protected Product mProduct;
	protected FloatingActionsMenu mMultipleActions;
	protected FloatingActionButton more_share;

	protected ArrayList<FloatingActionButton> mListButton;

	public ArrayList<FloatingActionButton> getListButton() {
		return mListButton;
	}

	public void setProduct(Product mProduct) {
		this.mProduct = mProduct;
	}

	public ProductMorePluginBlock(View view, Context context) {
		super(view, context);
	}

	public void setListenerMoreShare(OnClickListener onclick) {
		more_share.setOnClickListener(onclick);
	}

	@Override
	public void initView() {
		mListButton = new ArrayList<FloatingActionButton>();
		mMultipleActions = (FloatingActionsMenu) mView.findViewById(Rconfig
				.getInstance().id("more_plugins_action"));
		mMultipleActions.createButton(mContext, Config.getInstance()
				.getButton_background(), Config.getInstance()
				.getButton_background(), Config.getInstance()
				.getButton_text_color());
		more_share = new FloatingActionButton(mContext);
		more_share.setColorNormal(Color.parseColor("#FFFFFF"));
		more_share.setColorPressed(Color.parseColor("#f4f4f4"));
		more_share.setIcon(Rconfig.getInstance().drawable("ic_share"));
		mListButton.add(more_share);
		for (int i = 0; i < mListButton.size(); i++) {
			mMultipleActions.addButton(mListButton.get(i));
		}
		EventBlock event = new EventBlock();
		CacheBlock cacheBlock = new CacheBlock();
		cacheBlock.setBlock(this);
		cacheBlock.setView(mView);
		cacheBlock.setContext(mContext);
		cacheBlock.setSimiEntity(mProduct);
		event.dispatchEvent(
				"com.simicart.core.catalog.product.block.ProductMorePluginBlock",
				cacheBlock);
	}
}
