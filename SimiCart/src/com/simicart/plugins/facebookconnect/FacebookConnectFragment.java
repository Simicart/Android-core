package com.simicart.plugins.facebookconnect;

import java.util.List;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.widget.LikeView;
import com.simicart.MainActivity;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Rconfig;

public class FacebookConnectFragment extends SimiFragment {

	private View rootView;
	private String urlProduct;
	private Context mContext;

	private ImageView img_share;
	private ImageView img_comment;
	FacebookLikesButton fbLikes;
	private static 	CallbackManager callbackManager;

	public void setUrlProduct(String urlProduct) {
		this.urlProduct = urlProduct;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public static FacebookConnectFragment newInstance() {
		FacebookConnectFragment fragment = new FacebookConnectFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
		callbackManager =  CallbackManager.Factory.create();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(
				Rconfig.getInstance().layout("plugin_fbconnect_fragment"),
				container, false);
		img_share = (ImageView) rootView.findViewById(Rconfig.getInstance().id(
				"btn_fbShare"));
		img_comment = (ImageView) rootView.findViewById(Rconfig.getInstance()
				.id("btn_fbComment"));
		handleEvent();
		int sizeLike = Utils.getValueDp(30);
		final int sizeComment_w = Utils.getValueDp(40);
		final int sizeComment_h = Utils.getValueDp(35);
		TextView tvCount = (TextView) rootView.findViewById(Rconfig
				.getInstance().id("fb_like_cloud_text"));
		ProgressBar progress = (ProgressBar) rootView.findViewById(Rconfig
				.getInstance().id("fb_like_cloud_progress"));
		fbLikes = new FacebookLikesButton(mContext, progress, tvCount);
		fbLikes.downloadLikes(urlProduct);
		LikeView btnLike = (LikeView) rootView.findViewById(Rconfig
				.getInstance().id("btn_fbLike"));
		btnLike.setObjectIdAndType(urlProduct, LikeView.ObjectType.OPEN_GRAPH);
		btnLike.setLikeViewStyle(LikeView.Style.BUTTON);
		btnLike.setHorizontalAlignment(LikeView.HorizontalAlignment.LEFT);
		rootView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SimiManager.getIntance().backPreviousFragment();
			}
		});
		return rootView;
	}

	private void handleEvent() {
		img_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareProduct(urlProduct);
			}
		});
		img_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager manager = SimiManager.getIntance()
						.getCurrentActivity().getFragmentManager();
				CommentFragment cm_fragment = new CommentFragment(urlProduct);
				cm_fragment.show(manager, "simi");
			}
		});
	}

	private void shareProduct(String url) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		// intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no
		// effect!
		intent.putExtra(Intent.EXTRA_TEXT, url);
		// See if official Facebook app is found
		boolean facebookAppFound = false;
		List<ResolveInfo> matches = MainActivity.instance.getPackageManager()
				.queryIntentActivities(intent, 0);
		for (ResolveInfo info : matches) {
			if (info.activityInfo.packageName.toLowerCase().startsWith(
					"com.facebook.katana")) {
				intent.setPackage(info.activityInfo.packageName);
				facebookAppFound = true;
				break;
			}
		}
		// As fallback, launch sharer.php in a browser
		if (!facebookAppFound) {
			String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u="
					+ url;
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
		}
		MainActivity.instance.startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
