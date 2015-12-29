package com.simicart.plugins.youtube;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.simicart.core.catalog.product.block.ProductMorePluginBlock;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionButton;
import com.simicart.core.style.material.floatingactionbutton.FloatingActionsMenu;
import com.simicart.plugins.youtube.entity.YoutubeEnity;
import com.simicart.plugins.youtube.fragment.YoutubeFragment;

public class Youtube {
	protected ArrayList<YoutubeEnity> mYoutube;
	protected ViewPager mPager;
	protected CacheBlock mCacheBlock;

	public Youtube(String method, CacheBlock cacheBlock) {
		this.mCacheBlock = cacheBlock;
		if (method.equals("addTabVideo")) {
			mYoutube = new ArrayList<YoutubeEnity>();
			// mYoutube.add(new YoutubeEnity("AA", "HcQJzFzZfVI"));
			try {
				JSONArray array_youtube = new JSONArray(mCacheBlock
						.getSimiEntity().getData("youtube"));
				if (array_youtube.length() > 0) {
					for (int i = 0; i < array_youtube.length(); i++) {
						JSONObject obj_youtube = array_youtube.getJSONObject(i);
						YoutubeEnity youtube = new YoutubeEnity();
						youtube.setJSONObject(obj_youtube);
						mYoutube.add(youtube);
					}
				}
			} catch (JSONException e) {
			}
			YoutubeFragment fragment = YoutubeFragment.newInstanse();
			fragment.setYoutube(mYoutube);
			mCacheBlock.getListFragment().add(fragment);
			mCacheBlock.getListName().add("Video");
		}

		if (method.equals("addButtonMore")) {
			Context context = mCacheBlock.getContext();
			View view = mCacheBlock.getView();
			ArrayList<FloatingActionButton> mListButtons = ((ProductMorePluginBlock) mCacheBlock
					.getBlock()).getListButton();
			final FloatingActionsMenu mMultipleActions = (FloatingActionsMenu) view
					.findViewById(Rconfig.getInstance().id(
							"more_plugins_action"));
			FloatingActionButton bt_youtube = new FloatingActionButton(context);
			bt_youtube.setStrokeVisible(false);
			bt_youtube.setColorNormal(Color.WHITE);
			bt_youtube.setColorNormal(Color.parseColor("#FFFFFF"));
			bt_youtube.setColorPressed(Color.parseColor("#f4f4f4"));
			bt_youtube.setIcon(Rconfig.getInstance().drawable("ic_play"));

			bt_youtube.setVisibility(View.GONE);

			for (int i = 0; i < mListButtons.size(); i++) {
				mMultipleActions.removeButton(mListButtons.get(i));
			}
			mListButtons.add((mListButtons.size() - 1), bt_youtube);
			for (int i = 0; i < mListButtons.size(); i++) {
				mMultipleActions.addButton(mListButtons.get(i));
			}

			mPager = (ViewPager) view.findViewById(Rconfig.getInstance().id(
					"pager"));
			if (mPager.getAdapter() != null) {
				for (int i = 0; i < mPager.getAdapter().getCount(); i++) {
					if (mPager.getAdapter().getPageTitle(i).equals("Video")) {
						bt_youtube.setVisibility(View.VISIBLE);
					}
				}
			}
			bt_youtube.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (mPager.getAdapter() != null) {
						for (int i = 0; i < mPager.getAdapter().getCount(); i++) {
							if (mPager.getAdapter().getPageTitle(i)
									.equals("Video")) {
								mPager.setCurrentItem(i);
							}
						}
					}
					mMultipleActions.collapse();
				}
			});

		}
	}
}
