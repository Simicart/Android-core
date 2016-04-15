package com.simicart.plugins.zopimchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.simicart.core.config.Rconfig;
import com.zopim.android.sdk.api.Chat;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.chatlog.ZopimChatLogFragment;
import com.zopim.android.sdk.embeddable.ChatActions;
import com.zopim.android.sdk.prechat.ChatListener;
import com.zopim.android.sdk.prechat.PreChatForm;
import com.zopim.android.sdk.prechat.ZopimChatFragment;
import com.zopim.android.sdk.widget.ChatWidgetService;

/**
 * Created by truongtechno on 05/04/2016.
 */
public class SimiChatActivity extends ActionBarActivity implements ChatListener {


    PreChatForm.Field Name = PreChatForm.Field.REQUIRED_EDITABLE;
    PreChatForm.Field REQUIRE_EDITABLE;
    PreChatForm.Field OPTION_EDITABLE;
    PreChatForm.Field NOT_REQUIRE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZopimChat.init(ConstantZopim.ZOPIM_ACCOUNT_KEY);
        setContentView(Rconfig.getInstance().layout("plugin_chatactivity_layout"));
        Toolbar toolbar = (Toolbar) findViewById(Rconfig.getInstance().id("toolbar"));
        toolbar.setBackgroundColor(Color.parseColor("#FF7E00"));
        setSupportActionBar(toolbar);
        REQUIRE_EDITABLE = PreChatForm. Field.REQUIRED_EDITABLE;
        OPTION_EDITABLE = PreChatForm.Field.OPTIONAL_EDITABLE;
        NOT_REQUIRE = PreChatForm.Field.NOT_REQUIRED;

        if (savedInstanceState != null) {
            return;
        }

        boolean widgetWasActive = stopService(new Intent(this, ChatWidgetService.class));
        if (widgetWasActive) {
            resumeChat();
            return;
        }

        /**
         * We've received an intent request to resume the existing chat.
         * Resume the chat via {@link com.zopim.android.sdk.api.ZopimChat#resume(android.support.v4.app.FragmentActivity)} and
         * start the {@link ZopimChatLogFragment}
         */
        if (getIntent() != null) {
            String action = getIntent().getAction();
            if (ChatActions.ACTION_RESUME_CHAT.equals(action)) {
                resumeChat();
                return;
            }
        }
        startChat();
    }

    private void startChat() {
        // build chat config
        PreChatForm preChatForm = getPrechatConfig(ConstantZopim.ZOPIM_NAME, ConstantZopim.ZOPIM_EMAIL, ConstantZopim.ZOPIM_PHONE);
        ZopimChat.SessionConfig config;
        if (ConstantZopim.ZOPIM_SHOWPROFILE.contains("1")) {
            config = new ZopimChat.SessionConfig()
                    .preChatForm(preChatForm);
        } else {
            config = new ZopimChat.SessionConfig()
                    .preChatForm(null);
        }
        // prepare chat fragment
        ZopimChatFragment fragment = ZopimChatFragment.newInstance(config);
        // show fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(com.zopim.android.sdk.R.id.chat_fragment_container, fragment, ZopimChatFragment.class.getName());
        transaction.commit();

    }


    private PreChatForm getPrechatConfig(String name, String email, String phone) {
        PreChatForm preChatForm = null;
        preChatForm = new PreChatForm.Builder()
                .name(getFeild(name))
                .email(getFeild(email))
                .phoneNumber(getFeild(phone))
                .message(PreChatForm.Field.REQUIRED_EDITABLE)
                .build();
        return preChatForm;
    }

    private PreChatForm.Field getFeild(String input) {
        PreChatForm.Field feild = null;
        switch (input) {
            case "0":
                feild = NOT_REQUIRE;
                break;
            case "3":
                feild = OPTION_EDITABLE;
                break;
            case "4":
                feild = REQUIRE_EDITABLE;
                break;
        }
        return feild;
    }

    private void resumeChat() {
        FragmentManager manager = getSupportFragmentManager();
        // find the retained fragment
        if (manager.findFragmentByTag(ZopimChatLogFragment.class.getName()) == null) {
            ZopimChatLogFragment chatLogFragment = new ZopimChatLogFragment();

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(com.zopim.android.sdk.R.id.chat_fragment_container, chatLogFragment, ZopimChatLogFragment.class.getName());
            transaction.commit();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onChatLoaded(Chat chat) {
        Log.e("SimiChatActivity========================>", "OnChat load");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onChatInitialized() {
        Log.e("SimiChatActivity========================>", "OnchatInited");
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onChatEnded() {
        Log.e("SimiChatActivity========================>", "OnChatEnd");
        onBackPressed();
    }
}
