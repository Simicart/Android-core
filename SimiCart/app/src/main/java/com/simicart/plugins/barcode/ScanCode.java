package com.simicart.plugins.barcode;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.simicart.MainActivity;
import com.simicart.core.base.delegate.ModelDelegate;
import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.catalog.product.fragment.ProductDetailParentFragment;
import com.simicart.core.common.Utils;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.activity.CacheActivity;
import com.simicart.core.event.slidemenu.SlideMenuData;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.core.slidemenu.entity.ItemNavigation.TypeItem;

import java.util.ArrayList;

public class ScanCode {

    Context mContext;
    SlideMenuData mSlideMenuData;
    ArrayList<ItemNavigation> mItems;
    private ScanCodeModel mModel;

    public ScanCode(String method, SlideMenuData menuData) {
        this.mSlideMenuData = menuData;
        mContext = SimiManager.getIntance().getCurrentActivity();
        mItems = mSlideMenuData.getItemNavigations();
        if (method.equals("addbarcodetoleftmenu")) {
            ItemNavigation item = new ItemNavigation();
            item.setType(TypeItem.NORMAL);
            item.setName(Config.getInstance().getText(
                    ConstantBarcode.QR_BAR_CODE));
            int id_icon = Rconfig.getInstance().drawable("ic_barcode");
            Drawable icon = mContext.getResources().getDrawable(id_icon);
            icon.setColorFilter(Color.parseColor("#ffffff"),
                    PorterDuff.Mode.SRC_ATOP);
            item.setIcon(icon);
            mItems.add(item);
        }
    }

    public ScanCode(String method) {
        if (method.equals("checkentrycount")) {
            checkEntryCount();
        }
        if (method.equals("clickitemleftmenu")) {
            clickItemLeftMenuCode();
        }
        if (method.equals("backtoscan")) {
            clickItemLeftMenuCode();
        }
        if (method.equals("checkdirectdetail")) {
            String nameFragment = Constants.NAME_FRAGMENT;
            if (nameFragment
                    .contains("com.simicart.core.catalog.product.fragment.ProductDetailParentFragment")) {
                Constants.DIRECT_TODETAIL = true;
            } else {
                Constants.DIRECT_TODETAIL = false;
            }
        }
    }

    public ScanCode(String method, CacheActivity cacheActivity) {
        if (method.equals("resultbarcode")) {
            checkResultBarcode(cacheActivity);
        }
    }

    private void checkResultBarcode(CacheActivity cacheActivity) {
        int requestCode = cacheActivity.getRequestCode();
        int resultCode = cacheActivity.getResultCode();
        Intent data = cacheActivity.getData();
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String content = data.getStringExtra(
                    "SCAN_RESULT");
            String fomat = data.getStringExtra(
                    "SCAN_RESULT_FORMAT");
            String type = "";
            if (fomat.equals("QR_CODE")) {
                type = "1";
            } else {
                type = "0";
            }
            if (type != "" && content != "") {
                requestResultBarcode(type, content);
            }
        }
    }

    private void requestResultBarcode(String type, String content) {
        mModel = new ScanCodeModel();

        final ProgressDialog pd_loading = ProgressDialog
                .show(SimiManager.getIntance().getCurrentActivity(), null,
                        null, true, false);
        pd_loading.setContentView(Rconfig.getInstance().layout(
                "core_base_loading"));
        pd_loading.setCanceledOnTouchOutside(false);
        pd_loading.setCancelable(false);
        pd_loading.show();

        mModel.setDelegate(new ModelDelegate() {

            @Override
            public void callBack(String message, boolean isSuccess) {
                pd_loading.dismiss();
                if (isSuccess) {
                    ArrayList<String> listID = new ArrayList<String>();
                    String product_id = mModel.getProductID();
                    if (!Utils.validateString(product_id)) {
                        SimiManager.getIntance().showToast(
                                "Result products is empty");
                        return;
                    }
                    listID.add(product_id);
                    ProductDetailParentFragment fragment = ProductDetailParentFragment
                            .newInstance(product_id, listID);
                    fragment.setTargetFragment(fragment,
                            Constants.TARGET_PRODUCTDETAIL);
                    SimiManager.getIntance().addFragment(fragment);
                    MainActivity.mCheckToDetailAfterScan = true;
                    MainActivity.mBackEntryCountDetail = SimiManager
                            .getIntance().getManager()
                            .getBackStackEntryCount();
                } else {
                    SimiManager.getIntance().showToast(
                            "Result products is empty");
                    return;
                }
            }
        });
        mModel.addParam("code", content);
        mModel.addParam("type", type);
        mModel.request();
    }


    private void clickItemLeftMenuCode() {
        String nameItem = Constants.itemName;
        if (nameItem.equals(Config.getInstance().getText(
                ConstantBarcode.QR_BAR_CODE))) {
            new IntentIntegrator(MainActivity.context).initiateScan();
        }
    }

    private void checkEntryCount() {
        int backEntryCountDetail = SimiManager.getIntance().getManager()
                .getBackStackEntryCount();
        if (MainActivity.mBackEntryCountDetail == backEntryCountDetail) {
            if (Constants.DIRECT_TODETAIL == true) {
                if (SimiManager.getIntance().getManager().getFragments().size() > 0) {
                    try {
                        for (Fragment fragment : SimiManager.getIntance()
                                .getManager().getFragments()) {
                            if (fragment == null) {
                                // SimiManager.getIntance().getManager()
                                // .getFragments().remove(fragment);
                                break;
                            } else {
                                if (fragment.getTargetRequestCode() == Constants.TARGET_PRODUCTDETAIL
                                        && MainActivity.mCheckToDetailAfterScan == true) {
                                    MainActivity.checkBackScan = true;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

}
