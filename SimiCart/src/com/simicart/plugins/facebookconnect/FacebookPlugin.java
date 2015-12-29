package com.simicart.plugins.facebookconnect;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.block.ProductMorePluginBlock;
import com.simicart.core.catalog.product.entity.Product;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionButton;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionsMenu;

public class FacebookPlugin {
	
	private static String url_product ; 
	private static Context mContext;
	
	
	public FacebookPlugin(String method) {
		if(method.equals("resultfacebook")){
			SimiManager.getIntance().backPreviousFragment();
			FacebookConnectFragment fragment = FacebookConnectFragment
					.newInstance();
			fragment.setUrlProduct(url_product);
			fragment.setContext(mContext);
			FragmentTransaction ft = SimiManager.getIntance().getManager()
					.beginTransaction();
			ft.add(Rconfig.getInstance().id("container"), fragment);
			ft.addToBackStack(null);
			ft.commit();
		}
	}
	public FacebookPlugin(String method, CacheBlock cache) {
		if(method.equals("addLayoutConnect")){
		final Context context = cache.getContext();
		View view = cache.getView();
		this.mContext = cache.getContext();
		Product product = (Product) cache.getSimiEntity();
		ArrayList<FloatingActionButton> mListButtons = ((ProductMorePluginBlock) cache.getBlock()).getListButton();
		this.url_product = product.getData("product_url");
		FloatingActionsMenu mMultipleActions = (FloatingActionsMenu) view.findViewById(Rconfig.getInstance().id("more_plugins_action"));
		FloatingActionButton bt_facebook = new FloatingActionButton(context);
		bt_facebook.setStrokeVisible(false);
		bt_facebook.setColorNormal(Color.WHITE);
		bt_facebook.setColorNormal(Color.parseColor("#FFFFFF"));
		bt_facebook.setColorPressed(Color.parseColor("#f4f4f4"));
		bt_facebook.setIcon(Rconfig.getInstance().drawable("ic_facebook"));
		for (int i = 0; i < mListButtons.size(); i++) {
			mMultipleActions.removeButton(mListButtons.get(i));
		}
		mListButtons.add((mListButtons.size() - 1), bt_facebook);
		for (int i = 0; i < mListButtons.size(); i++) {
			mMultipleActions.addButton(mListButtons.get(i));
		}
		
		bt_facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SimiManager.getIntance().removeDialog();
				FacebookConnectFragment fragment = FacebookConnectFragment
						.newInstance();
				fragment.setUrlProduct(url_product);
				fragment.setContext(mContext);
				FragmentTransaction ft = SimiManager.getIntance().getManager()
						.beginTransaction();
				ft.add(Rconfig.getInstance().id("container"), fragment);
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		}
	}
	
	
}
