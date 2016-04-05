package com.simicart.plugins.zopimchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.config.Config;
import com.simicart.core.config.Constants;
import com.simicart.core.config.Rconfig;
import com.simicart.core.event.slidemenu.SlideMenuData;
import com.simicart.core.slidemenu.entity.ItemNavigation;

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

    public SimiZopim(String method) {
        if (method.equals("clickitemleftmenu")) {
            String nameItem = Constants.itemName;
            if (nameItem.equals(ConstantZopim.ZOPIM)) {
                Intent intent = new Intent(SimiManager.getIntance().getCurrentActivity(), SimiChatActivity.class);
                SimiManager.getIntance().getCurrentActivity().startActivity(intent);
            }
        }

    }


}
