package com.simicart.plugins.zopimchat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.block.CacheBlock;
import com.simicart.core.event.slidemenu.SlideMenuData;
import com.simicart.core.slidemenu.entity.ItemNavigation;
import com.simicart.plugins.rewardpoint.utils.Constant;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by truongtechno on 05/04/2016.
 */
public class SimiZopim {

    Context mContext;
    SlideMenuData mSlideMenuData;
    ArrayList<ItemNavigation> mItems;

    public SimiZopim(String method, SlideMenuData menuData) {
        this.mSlideMenuData = menuData;
        mContext = SimiManager.getIntance().getCurrentActivity();
        mItems = mSlideMenuData.getItemNavigations();
        if (method.equals("addzopimtoslidemenu")) {
            if (ConstantZopim.ZOPIM_ENABLE.equals("1")) {
                ItemNavigation item = new ItemNavigation();
                item.setType(ItemNavigation.TypeItem.NORMAL);
                item.setName(Config.getInstance().getText(
                        ConstantZopim.ZOPIM));
                int id_icon = Rconfig.getInstance().drawable("ic_livechat");
                Drawable icon = mContext.getResources().getDrawable(id_icon);
                icon.setColorFilter(Color.parseColor("#ffffff"),
                        PorterDuff.Mode.SRC_ATOP);
                item.setIcon(icon);
                mItems.add(item);
            }
        }
    }

    public SimiZopim(String method) {
        if (method.equals("clickitemleftmenu")) {
            String nameItem = Constants.itemName;
            if (nameItem.equals(ConstantZopim.ZOPIM)) {
                Intent intent = new Intent(SimiManager.getIntance().getCurrentActivity(), SimiChatActivity.class);
                SimiManager.getIntance().getCurrentActivity().startActivity(intent);
            }
        }

    }

    public SimiZopim(String method, CacheBlock block) {
        if (method.equals("configzopim")) {
            try {
                JSONObject object = block.getSimiEntity().getJSONObject();
                if (object.has("enable")) {
                    ConstantZopim.ZOPIM_ENABLE = object.getString("enable");
                }
                if (object.has("phone")) {
                    ConstantZopim.ZOPIM_PHONE = object.getString("phone");
                }
                if (object.has("account_key")) {
                    ConstantZopim.ZOPIM_ACCOUNT_KEY = object.getString("account_key");
                }
                if (object.has("email")) {
                    ConstantZopim.ZOPIM_EMAIL = object.getString("email");
                }
                if (object.has("show_profile")) {
                    ConstantZopim.ZOPIM_SHOWPROFILE = object.getString("show_profile");
                }
                if (object.has("name")) {
                    ConstantZopim.ZOPIM_NAME = object.getString("name");
                }
            } catch (Exception e) {
            }

        }
    }


}
