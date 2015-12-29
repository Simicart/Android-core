package com.simicart.core.event.block;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;

import com.simicart.core.base.block.SimiBlock;
import com.simicart.core.base.fragment.SimiFragment;
import com.simicart.core.base.model.collection.SimiCollection;
import com.simicart.core.base.model.entity.SimiEntity;

public class CacheBlock {

	private View view;
	private SimiEntity simiEntity;
	private SimiCollection simiCollection;
	private SimiBlock mBlock;
	private Context mContext;
	private String nameItem;// name item Navigation
	private ArrayList<SimiFragment> mListFragment;
	private ArrayList<String> mListName;

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	public SimiBlock getBlock() {
		return mBlock;
	}

	public void setBlock(SimiBlock mBlock) {
		this.mBlock = mBlock;
	}

	public View getView() {
		return view;
	}

	public String getNameItem() {
		return nameItem;
	}

	public void setView(View view) {
		this.view = view;
	}

	public SimiEntity getSimiEntity() {
		return simiEntity;
	}

	public void setSimiEntity(SimiEntity simiEntity) {
		this.simiEntity = simiEntity;
	}

	public SimiCollection getSimiCollection() {
		return simiCollection;
	}

	public void setSimiCollection(SimiCollection simiCollection) {
		this.simiCollection = simiCollection;
	}

	public void setName(String name) {
		this.nameItem = name;
	}

	public void setListFragment(ArrayList<SimiFragment> mListFragment) {
		this.mListFragment = mListFragment;
	}
	
	public ArrayList<SimiFragment> getListFragment() {
		return mListFragment;
	}
	
	public void setListName(ArrayList<String> mListName) {
		this.mListName = mListName;
	}
	
	public ArrayList<String> getListName() {
		return mListName;
	}
}
