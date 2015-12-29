package com.simicart.plugins.rewardpoint.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.config.Rconfig;
import com.simicart.plugins.rewardpoint.adapter.AdapterListviewHistory;
import com.simicart.plugins.rewardpoint.entity.ItemHistory;
import com.simicart.plugins.rewardpoint.model.ModelRewardHistory;

public class FragmentRewardHistory extends SimiFragment {

	private ModelRewardHistory mModel;
	private JSONObject jsonObject = new JSONObject();
	private List<ItemHistory> listItemHistory = new ArrayList<ItemHistory>();
	private Context mContext;
	private ListView listView;
	private TextView txt_message;

	protected ProgressDialog pd_loading;
	private View view;
	

	public FragmentRewardHistory() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		pd_loading = ProgressDialog.show(mContext, null, null, true, false);
		pd_loading.setContentView(Rconfig.getInstance().layout(
				"core_base_loading"));
		pd_loading.setCanceledOnTouchOutside(false);
		pd_loading.setCancelable(false);
		pd_loading.show();
		try {
			mModel = new ModelRewardHistory();
			ModelDelegate delegate = new ModelDelegate() {

				@Override
				public void callBack(String message, boolean isSuccess) {
					pd_loading.dismiss();
					if (isSuccess) {
						jsonObject = mModel.getJSON();
						addJsonToListView(jsonObject);
					} else {
					}
				}
			};
			mModel.setDelegate(delegate);
			mModel.request();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(
				Rconfig.getInstance().layout(
						"plugins_rewardpoint_historyfragment"), container,
				false);
		listView = (ListView) view.findViewById(Rconfig.getInstance().id(
				"reward_listview_history"));
		txt_message = (TextView) view.findViewById(Rconfig.getInstance().id("txt_message"));
		return view;
	}

	@SuppressWarnings("unused")
	private void addJsonToListView(JSONObject jsonObject) {
		try {
			JSONArray arrayData = jsonObject.getJSONArray("data");
			for (int i = 0; i < arrayData.length(); i++) {
				JSONObject object = arrayData.getJSONObject(i);
				ItemHistory itemHistory = new ItemHistory();
				listItemHistory.add(itemHistory.getHistoryFromJson(object));
			}
			if(listItemHistory.size() > 0){
				listView.setVisibility(View.VISIBLE);
				txt_message.setVisibility(View.GONE);
				AdapterListviewHistory adapter = new AdapterListviewHistory(
						mContext, listItemHistory);
				listView.setAdapter(adapter);
			}else{
				txt_message.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			Log.e("errorr:", e.getMessage());
		}
	}

}
