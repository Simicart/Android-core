package com.simicart.plugins.barcode;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.config.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ScanCodeModel extends SimiModel {

    private String mProductID;

    public String getProductID() {
        return mProductID;
    }

    @Override
    protected void paserData() {
        try {
            JSONArray jsonData = this.mJSON.getJSONArray(Constants.DATA);
            JSONObject jsonObject = jsonData.getJSONObject(0);
            if (jsonObject.has("product_id")) {
                mProductID = jsonObject.getString("product_id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setUrlAction() {
        url_action = "simibarcode/index/checkCode";
    }

}
