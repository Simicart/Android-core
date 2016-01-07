package com.simicart.core.home.model.page;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simicart.core.cms.entity.Cms;
import com.simicart.core.config.DataLocal;

public class CMSModel {
	public void setData(JSONObject jobs){
		DataLocal.listCms.clear();		
		JSONArray cmsOAj;
		try {
			cmsOAj = jobs.getJSONArray("home_cms");
			for (int i = 0; i < cmsOAj.length(); i++) {
				Cms cms = new Cms();
				cms.setJSONObject(cmsOAj.getJSONObject(i));
				DataLocal.listCms.add(cms);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
