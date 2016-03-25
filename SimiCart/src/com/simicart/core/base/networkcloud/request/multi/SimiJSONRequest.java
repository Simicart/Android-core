package com.simicart.core.base.networkcloud.request.multi;

import com.simicart.core.base.networkcloud.delegate.NetWorkDelegate;
import com.simicart.core.base.networkcloud.response.CoreResponse;

public class SimiJSONRequest extends SimiRequest {

    String url;

    public SimiJSONRequest(String url, NetWorkDelegate delegate) {
        super(url, delegate);
        this.url = url;
    }


    @Override
    public CoreResponse parseNetworkResponse(SimiNetworkResponse response) {
        if (null != response) {
            byte[] data = response.getData();
            if (null != data && data.length > 0) {
                String content = new String(data);
                CoreResponse coreResponse = new CoreResponse();
                coreResponse.setData(content);
                return coreResponse;
            }
        }
        return null;
    }

    @Override
    public void deliveryCoreResponse(CoreResponse response) {
        if (null != response) {
            if (response.parse()) {
                mDelegate.callBack(response, true);
            } else {

                mRequestQueue.finish(this);

//                String message = response.getMessage();
//                if (!Utils.validateString(message) && isShowNotify) {
//                    message = Config.getInstance().getText(
//                            "Some errors occured. Please try again later");
//                }
//
//                if (isShowNotify) {
//                    SimiManager.getIntance().showNotify(message);
//                }
                mDelegate.callBack(response, false);
                // mRequestQueue.stop();
            }

        } else {
            mRequestQueue.finish(this);
            mDelegate.callBack(response, false);
        }
    }
}
