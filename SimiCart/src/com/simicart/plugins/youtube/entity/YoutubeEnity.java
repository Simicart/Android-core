package com.simicart.plugins.youtube.entity;

import com.simicart.core.base.model.entity.SimiEntity;

public class YoutubeEnity extends SimiEntity{
	protected String mTitle;
	protected String mKey;
	
	public YoutubeEnity(String mTitle, String mKey) {
		this.mTitle = mTitle;
		this.mKey = mKey;
	}
	public YoutubeEnity() {
		// TODO Auto-generated constructor stub
	}
	
	public String getKey() {
		if(mKey == null){
			mKey = getData("key");
		}
		return mKey;
	}
	
	public String getTitle() {
		if(mTitle == null){
			mTitle = getData("title");
		}
		return mTitle;
	}
	
	public void setKey(String mKey) {
		this.mKey = mKey;
	}
	
	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
}
