package com.simicart.plugins.braintree.entity;

import com.simicart.core.base.model.entity.SimiEntity;

/**
 * Created by James Crabby on 1/12/2016.
 */
public class TokenEntity extends SimiEntity {
    String token;
    String google_merchant;

    @Override
    public void parse() {
        if(mJSON.has("token")) {
            token = getData("token");
        }

        if(mJSON.has("google_merchant")) {
            google_merchant = getData("google_merchant");
        }
    }

    public String getGoogle_merchant() {
        return google_merchant;
    }

    public void setGoogle_merchant(String google_merchant) {
        this.google_merchant = google_merchant;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
