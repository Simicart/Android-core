package com.simicart.core.base.controller;

import java.util.ArrayList;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.entity.SimiEntity;

public abstract class SimiController {

	protected ArrayList<SimiEntity> mCollections;
	protected SimiModel mModel;


	public abstract  void onStart();

	public abstract void onResume();


	public ArrayList<SimiEntity> getCollections() {
		return mCollections;
	}

	public void setCollections(ArrayList<SimiEntity> mCollections) {
		this.mCollections = mCollections;
	}

}
