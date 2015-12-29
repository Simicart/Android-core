package com.simicart.plugins.download.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.plugins.download.common.DownloadConstant;

public class DownloadEntity extends SimiEntity{
	private String mOrderID;
	private String mOrderDate;
	private String mOrderName;
	private String mOrderLink;
	private String mOrderStatus;
	private String mOrderRemain;
	private String mFileName;
	
	public String getOrderID() {
		if(mOrderID == null){
			mOrderID = getData(DownloadConstant.ORDER_ID);
		}
		return mOrderID;
	}
	
	public void setOrderID(String mOrderID) {
		this.mOrderID = mOrderID;
	}
	
	public String getOrderDate() {
		if(mOrderDate == null){
			mOrderDate = getData(DownloadConstant.ORDER_DATE);
		}
		return mOrderDate;
	}
	
	public void setOrderDate(String mOrderDate) {
		this.mOrderDate = mOrderDate;
	}
	
	public String getOrderName() {
		if(mOrderName == null){
			mOrderName = getData(DownloadConstant.ORDER_NAME);
		}
		return mOrderName;
	}
	
	public void setOrderName(String mOrderName) {
		this.mOrderName = mOrderName;
	}
	
	public String getOrderLink() {
		if(mOrderLink == null){
			mOrderLink = getData(DownloadConstant.ORDER_LINK);
		}
		return mOrderLink;
	}
	
	public void setOrderLink(String mOrderLink) {
		this.mOrderLink = mOrderLink;
	}
	
	public String getOrderStatus() {
		if(mOrderStatus == null){
			mOrderStatus = getData(DownloadConstant.ORDER_STATUS);
		}
		return mOrderStatus;
	}
	
	public void setOrderStatus(String mOrderStatus) {
		this.mOrderStatus = mOrderStatus;
	}
	
	public String getOrderRemain() {
		if(mOrderRemain == null){
			mOrderRemain = getData(DownloadConstant.ORDER_REMAIN);
		}
		return mOrderRemain;
	}
	
	public void setOrderRemain(String mOrderRemain) {
		this.mOrderRemain = mOrderRemain;
	}
	
	public String getOrderFile() {
		if(mFileName == null){
			mFileName = getData(DownloadConstant.FILE_NAME);
		}
		return mFileName;
	}
	
	public void setOrderFile(String mFileName) {
		this.mFileName = mFileName;
	}

	public String toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	        jsonObject.put("orderID", getOrderID());
	        jsonObject.put("orderDate", getOrderDate());
	        jsonObject.put("orderLink", getOrderLink());
	        jsonObject.put("orderName", getOrderName());
	        jsonObject.put("orderRemain", getOrderRemain());
	        jsonObject.put("orderStatus", getOrderStatus());
	        jsonObject.put("orderFile", getOrderFile());
	        return jsonObject.toString();
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return "";
	    }

	}
}
