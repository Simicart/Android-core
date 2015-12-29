package com.simicart.plugins.download.block;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.download.adapter.DownloadAdapter;
import com.simicart.plugins.download.delegate.DownloadDelegate;
import com.simicart.plugins.download.entity.DownloadEntity;

public class DownloadBlockTablet extends SimiBlock implements DownloadDelegate{
	protected GridView mListDownload;
	protected DownloadAdapter mAdapter;
	Context context;
	
	public DownloadBlockTablet(View view, Context context) {
		super(view, context);
		this.context = context;
	}

	@Override
	public void initView() {
		mListDownload = (GridView) mView.findViewById(Rconfig.getInstance().id("list_download"));
	}
	
	@Override
	public void drawView(SimiCollection collection) {
		ArrayList<SimiEntity> entity = collection.getCollection();
		if (null != entity && entity.size() > 0) {
			ArrayList<DownloadEntity> list_download = new ArrayList<DownloadEntity>();
			for (int i = 0; i < entity.size(); i++) {
				SimiEntity simiEntity = entity.get(i);
				DownloadEntity download = (DownloadEntity) simiEntity;
				list_download.add(download);
			}
			//new FileNameAsyncTask(list_download).execute();
			if(list_download.size() > 0){
				if(null == mAdapter){
					mAdapter = new DownloadAdapter(mContext, list_download);
					mListDownload.setAdapter(mAdapter);
				}else{
					mAdapter.setListDownload(list_download);
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	}
	
	/*protected class FileNameAsyncTask extends AsyncTask<Void, Void, Void> {

		ArrayList<DownloadEntity> list_download;
		ArrayList<ProductDownloadEntity> list_product_download;
		
		public FileNameAsyncTask(ArrayList<DownloadEntity> list_download) {
			super();
			this.list_download = list_download;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			if(list_product_download.size() > 0){
				if(null == mAdapter){
					mAdapter = new DownloadAdapter(mContext, list_product_download);
					mListDownload.setAdapter(mAdapter);
				}else{
					mAdapter.setListDownload(list_product_download);
					mAdapter.notifyDataSetChanged();
				}
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			list_product_download = new ArrayList<>();
			for(int i=0;i<list_download.size();i++) {
				Log.e("Download Block", "" + i);
				DownloadEntity item = list_download.get(i);
				URL url;
				HttpURLConnection connection = null;
				String fileName = "";
				try {
					url = new URL(Uri.parse(item.getOrderLink() + "email/"
							+ Base64.encodeToString(DataLocal.getEmail().getBytes(), Base64.DEFAULT)).toString());
					connection = (HttpURLConnection) url.openConnection();
					connection.connect();
					String raw = connection.getHeaderField("Content-Disposition");
					int b = raw.indexOf("filename=");
					if (raw != null && raw.indexOf("=") != -1) {
						fileName = raw.substring(b + 9);
					}
					Log.e("Download Block", "" + fileName);
					Log.e("Download Block", "" + isDownloaded(fileName));
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("Download Block", e.toString());
				} finally {
					if (connection != null)
						connection.disconnect();
					list_product_download.add(new ProductDownloadEntity(item, isDownloaded(fileName)));
				}
			}
			return null;
		}
		
		protected boolean isDownloaded(String filename) {
			File fileList = new File("/sdcard/" + context.getResources().getString(R.string.app_name));
			if (fileList != null && fileList.listFiles() != null) {
				File[] filenames = fileList.listFiles();
				if (filenames.length > 0)
					for (File tmpf : filenames) {
						if(filename.equals(tmpf.getName()))
							return true;
					}
			}
			return false;
		}
		
	}*/
	
	public void visiableView(String message) {
		((ViewGroup) mView).removeAllViewsInLayout();
		TextView tv_notify = new TextView(mContext);
		tv_notify.setText(Config.getInstance().getText(message));
		tv_notify.setTypeface(null, Typeface.BOLD);
		if (DataLocal.isTablet) {
			tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
		} else {
			tv_notify.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		tv_notify.setGravity(Gravity.CENTER);
		tv_notify.setLayoutParams(params);
		((ViewGroup) mView).addView(tv_notify);
	}

	@Override
	public void getMessage(String message) {
		visiableView(message);
	}
}
