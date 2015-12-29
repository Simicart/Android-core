package com.simicart.plugins.youtube.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.DrawableManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.youtube.activity.YoutubePlayerActivity;
import com.simicart.plugins.youtube.entity.YoutubeEnity;

public class YoutubeAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<YoutubeEnity> mListYoutube;
	private static ThumbnailListener thumbnailListener;
	String url_thumb = "http://img.youtube.com/vi/";

	public YoutubeAdapter(Context context, ArrayList<YoutubeEnity> mListYoutube) {
		this.mContext = context;
		this.mListYoutube = mListYoutube;
		thumbnailListener = new ThumbnailListener();
	}

	public void setListYoutube(ArrayList<YoutubeEnity> mListYoutube) {
		this.mListYoutube = mListYoutube;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListYoutube.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mListYoutube.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater.inflate(
				Rconfig.getInstance().layout("plugins_youtube_item_layout"),
				null);
		ViewHolder holder = null;
		if (convertView != null) {
			holder = new ViewHolder();
			holder.txt_title = (TextView) convertView.findViewById(Rconfig
					.getInstance().id("youtube_title"));
			holder.imageView = (ImageView) convertView.findViewById(Rconfig
					.getInstance().id("img_default"));
			holder.rl_youtube_fragment = (RelativeLayout) convertView
					.findViewById(Rconfig.getInstance().id(
							"rl_youtube_fragment"));
		}
		holder.txt_title.setText(mListYoutube.get(position).getTitle());
		try {
			DrawableManager.fetchDrawableOnThread(
					url_thumb + mListYoutube.get(position).getKey()
							+ "/mqdefault.jpg", holder.imageView);
			// maxresdefault
		} catch (Exception e) {
			Log.e("Load Image Youtube:", e.getMessage());
		}

		holder.rl_youtube_fragment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListYoutube.get(position).getKey() != null) {
					String uri = mListYoutube.get(position).getKey();
					if (uri != null) {
						Intent i = new Intent(SimiManager.getIntance()
								.getCurrentActivity(),
								YoutubePlayerActivity.class);
						i.putExtra("KeyYoutube", uri);
						SimiManager.getIntance().getCurrentActivity()
								.startActivity(i);
					}
				} else {
					SimiManager.getIntance().showNotify("Error",
							"Can not play video", "Ok");
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView txt_title;
		RelativeLayout rl_youtube_fragment;
	}

	public static class ThumbnailListener implements
			YouTubeThumbnailView.OnInitializedListener,
			YouTubeThumbnailLoader.OnThumbnailLoadedListener {

		@Override
		public void onInitializationSuccess(YouTubeThumbnailView view,
				YouTubeThumbnailLoader loader) {
			RelativeLayout.LayoutParams params_load = new LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					Utils.getValueDp(200));
			loader.setOnThumbnailLoadedListener(this);
			view.setLayoutParams(params_load);
			String videoId = (String) view.getTag();
			loader.setVideo(videoId);
		}

		@Override
		public void onInitializationFailure(YouTubeThumbnailView view,
				YouTubeInitializationResult loader) {
			RelativeLayout.LayoutParams params_faile = new LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					Utils.getValueDp(300));
			view.setLayoutParams(params_faile);
		}

		@Override
		public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
		}

		@Override
		public void onThumbnailError(YouTubeThumbnailView view,
				ErrorReason errorReason) {
			RelativeLayout.LayoutParams params_error = new LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					Utils.getValueDp(300));
			view.setLayoutParams(params_error);
		}
	}
}
