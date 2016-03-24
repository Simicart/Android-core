package com.simicart.core.event.activity;

import android.app.Activity;
import android.content.Intent;

public class CacheActivity {
    private Activity activity;
    private int requestCode;
    private int resultCode;
    private Intent data;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setData(Intent data) {
        this.data = data;
    }

    public Intent getData() {
        return data;
    }
}
