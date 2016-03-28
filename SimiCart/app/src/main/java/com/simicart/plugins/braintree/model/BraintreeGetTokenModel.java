package com.simicart.plugins.braintree.model;

import android.util.Log;

import com.simicart.core.base.model.SimiModel;
import com.simicart.core.base.model.entity.SimiEntity;
import com.simicart.core.base.network.request.multi.SimiRequest;
import com.simicart.core.splashscreen.entity.CMSPageEntity;
import com.simicart.plugins.braintree.entity.TokenEntity;

import java.util.ArrayList;

/**
 * Created by James Crabby on 1/12/2016.
 */
public class BraintreeGetTokenModel extends SimiModel {

    @Override
    protected void setUrlAction() {
        addDataExtendURL("braintree/token");
    }

    @Override
    protected void setTypeMethod() {
        mTypeMethod = SimiRequest.Method.GET;
    }

    @Override
    protected void paserData() {
        super.paserData();

        Log.e("BraintreeGetTokenModel", mJSONResult.toString());
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setJSONObject(mJSONResult);
        tokenEntity.parse();

        ArrayList<SimiEntity> arrayEntity = new ArrayList<>();
        arrayEntity.add(tokenEntity);
        collection.setCollection(arrayEntity);
    }

}
