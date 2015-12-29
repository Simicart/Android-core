package com.simicart.core.base.model.collection;

import java.util.ArrayList;

import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;

public class SimiCollection {
	protected  ArrayList<SimiEntity> list = new ArrayList<>();
	protected JSONObject mJSON;
	protected JSONObject mJSONOther;
	
	
	
	public JSONObject getJSONOther() {
		return mJSONOther;
	}

	public void setJSONOther(JSONObject mJSONOther) {
		this.mJSONOther = mJSONOther;
	}

	public void setJSON(JSONObject json)
	{
		mJSON = json;
	}
	
	public JSONObject getJSON()
	{
		return mJSON;
	}

	public void addEntity(SimiEntity entity) {
		this.list.add(entity);
	}

	public ArrayList<SimiEntity> getCollection() {
		return this.list;
	}
	public void setCollection(ArrayList<SimiEntity> list)
	{
		this.list = list;
	}

	public SimiEntity loadById(int id) {
		return this.list.get(id);
	}
}
