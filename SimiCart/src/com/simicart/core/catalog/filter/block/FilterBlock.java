package com.simicart.core.catalog.filter.block;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.category.delegate.FilterRequestDelegate;
import com.simicart.core.catalog.filter.common.FilterAdapter;
import com.simicart.core.catalog.filter.common.SelectedFilterAdapter;
import com.simicart.core.catalog.filter.delegate.FilterDelegate;
import com.simicart.core.catalog.filter.entity.FilterEntity;
import com.simicart.core.catalog.filter.entity.FilterState;
import com.simicart.core.catalog.filter.entity.ValueFilterEntity;
import com.simicart.core.checkout.controller.ConfigCheckout;
import com.simicart.core.config.Config;
import com.simicart.core.config.DataLocal;
import com.simicart.core.config.Rconfig;

public class FilterBlock extends SimiBlock implements FilterDelegate {

	protected ListView lv_filter;
	protected FilterRequestDelegate mDelegate;
	protected ListView lv_seletedFilter;
	protected Button btn_clearAll;
	protected AlertDialog mDialog;
	private TextView txt_name_category;
	private String name_category = "";
	private RelativeLayout rlt_category_name;
	private TextView tv_label;

	public void setName_category(String name_category) {
		this.name_category = name_category;
	}

	public void setDelegate(FilterRequestDelegate delegate) {
		mDelegate = delegate;
	}

	public void setonItemClickFilterList(OnItemClickListener itemClick) {
		lv_filter.setOnItemClickListener(itemClick);
	}

	public FilterBlock(View view, Context context) {
		super(view, context);
	}

	@Override
	public void initView() {
		// selected filter list
		lv_seletedFilter = (ListView) mView.findViewById(Rconfig.getInstance()
				.id("lv_selected_filters"));

		// clear all button
		btn_clearAll = (Button) mView.findViewById(Rconfig.getInstance().id(
				"btn_clear_all"));
		btn_clearAll.setVisibility(View.GONE);

		// filter list
		lv_filter = (ListView) mView.findViewById(Rconfig.getInstance().id(
				"lv_filters"));
		// label select a filter
		tv_label = (TextView) mView.findViewById(Rconfig.getInstance().id(
				"tv_label"));
		tv_label.setText(Config.getInstance().getText("Select a filter"));
		txt_name_category = (TextView) mView.findViewById(Rconfig.getInstance()
				.id("txt_category_filter"));
		rlt_category_name = (RelativeLayout) mView.findViewById(Rconfig
				.getInstance().id("rlt_category_filter"));
		if (name_category.equals("") && name_category != null) {
			rlt_category_name.setVisibility(View.GONE);
		} else {
			rlt_category_name.setVisibility(View.VISIBLE);
			txt_name_category.setText(name_category);
		}
	}

	@Override
	public void onShowListFilter(ArrayList<FilterEntity> filters) {
		if (filters.size() < 1) {
			tv_label.setVisibility(View.GONE);
		}
		FilterAdapter adapter = new FilterAdapter(mContext, filters);
		lv_filter.setAdapter(adapter);
	}

	@Override
	public void onShowDetailFilter(final FilterEntity entity) {
		if (null != entity) {
			final ArrayList<ValueFilterEntity> values = entity
					.getmValueFilters();
			if (null != values && values.size() > 0) {
				String[] arr_str = new String[values.size()];
				for (int i = 0; i < values.size(); i++) {
					String label = values.get(i).getLabel();
					arr_str[i] = label;
				}
				AdapterDialog adapter = new AdapterDialog(mContext, arr_str,
						entity);
				mDialog = new AlertDialog.Builder(mContext)
						.setTitle(Config.getInstance().getText("Filter"))
						.setAdapter(adapter, null)
						.setPositiveButton(
								Config.getInstance().getText("Cancel"),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										dialog.dismiss();
									}
								}).create();
				mDialog.getListView()
						.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				mDialog.show();
			}
		}
	}

	public class AdapterDialog extends BaseAdapter {
		private Context mContext;
		private String[] datas;
		protected FilterEntity mEntity;

		public void setDialog(AlertDialog dialog) {
			mDialog = dialog;
		}

		public AdapterDialog(Context context, String[] content,
				FilterEntity entity) {
			mContext = context;
			datas = content;
			mEntity = entity;
		}

		@Override
		public int getCount() {
			return datas.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			RadioButton rdb_filter = new RadioButton(mContext);

			String label = datas[position];

			rdb_filter.setText(Html.fromHtml(label));

			rdb_filter
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								if (null != mDialog) {
									mDialog.dismiss();
								}
								if (null != mDelegate) {
									mEntity.getmValueFilters().get(position)
											.setSelected(true);
									mDelegate.requestFilter(mEntity);
									if (DataLocal.isTablet) {
										SimiManager.getIntance()
												.popFragmentDialog();
										SimiManager.getIntance().removeDialog();
										List<Fragment> list = SimiManager
												.getIntance().getManager()
												.getFragments();
										for (Fragment fragment : list) {
											if (fragment != null
													&& fragment.isVisible()
													&& fragment
															.getTargetRequestCode() == ConfigCheckout.TARGET_LISTPRODUCT) {
												fragment.onResume();
											}
										}
									} else {
										SimiManager.getIntance()
												.backPreviousFragment();
									}
								}
							}
						}
					});

			return rdb_filter;
		}

	}

	@Override
	public void onShowListSelectedFilter(ArrayList<FilterState> states) {
		LinearLayout ll_selectedFilter = (LinearLayout) mView
				.findViewById(Rconfig.getInstance().id("ll_selected_filter"));
		if (null != states && states.size() > 0) {
			btn_clearAll.setVisibility(View.VISIBLE);
			btn_clearAll.setText(Config.getInstance().getText("Clear All"));

			ll_selectedFilter.setVisibility(View.VISIBLE);
			// label
			TextView tv_label = (TextView) mView.findViewById(Rconfig
					.getInstance().id("tv_selected_filter"));
			tv_label.setText(Config.getInstance().getText("Selected Filter"));
			tv_label.setVisibility(View.VISIBLE);

			// selected filter list
			SelectedFilterAdapter adapter = new SelectedFilterAdapter(states,
					mContext);
			adapter.setDelegate(mDelegate);
			lv_seletedFilter.setAdapter(adapter);

			// clear all listener

			btn_clearAll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mDelegate.clearAllFilter();
					if (DataLocal.isTablet) {
						SimiManager.getIntance().popFragmentDialog();
						SimiManager.getIntance().removeDialog();
						List<Fragment> list = SimiManager.getIntance()
								.getManager().getFragments();
						for (Fragment fragment : list) {
							if (fragment != null
									&& fragment.isVisible()
									&& fragment.getTargetRequestCode() == ConfigCheckout.TARGET_LISTPRODUCT) {
								fragment.onResume();
							}
						}
					} else {
						SimiManager.getIntance().backPreviousFragment();
					}
				}
			});

		} else {
			ll_selectedFilter.setVisibility(View.GONE);
			btn_clearAll.setVisibility(View.GONE);
		}
	}

}
