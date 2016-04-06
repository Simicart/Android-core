package com.simicart.plugins.zopimchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.simicart.core.config.Config;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZopimChat.init(ConstantZopim.ZOPIM_ACCOUNT_KEY);
        setContentView(Rconfig.getInstance().layout("plugin_chatactivity_layout"));
        Toolbar toolbar = (Toolbar) findViewById(Rconfig.getInstance().id("toolbar"));
        toolbar.setBackgroundColor(Config.getInstance().getColorMain());
        setSupportActionBar(toolbar);

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
        if (name.equals("0") && email.equals("0") && phone.equals("0")) {
            preChatForm = new PreChatForm.Builder()
                    .name(PreChatForm.Field.NOT_REQUIRED)
                    .email(PreChatForm.Field.NOT_REQUIRED)
                    .phoneNumber(PreChatForm.Field.NOT_REQUIRED)
                    .message(PreChatForm.Field.REQUIRED_EDITABLE)
                    .build();
        }
        if (name.equals("0") && email.equals("3") && phone.equals("4")) {
            preChatForm = new PreChatForm.Builder()
                    .name(PreChatForm.Field.NOT_REQUIRED)
                    .email(PreChatForm.Field.OPTIONAL_EDITABLE)
                    .phoneNumber(PreChatForm.Field.REQUIRED_EDITABLE)
                    .message(PreChatForm.Field.REQUIRED_EDITABLE)
                    .build();
        }
        if (name.equals("0") && email.equals("4") && phone.equals("3")) {
            preChatForm = new PreChatForm.Builder()
                    .name(PreChatForm.Field.NOT_REQUIRED)
                    .email(PreChatForm.Field.REQUIRED_EDITABLE)
                    .phoneNumber(PreChatForm.Field.OPTIONAL_EDITABLE)
                    .message(PreChatForm.Field.REQUIRED_EDITABLE)
                    .build();
        }
        if (name.equals("3") && email.equals("3") && phone.equals("3")) {
            preChatForm = new PreChatForm.Builder()
                    .name(PreChatForm.Field.OPTIONAL_EDITABLE)
                    .email(PreChatForm.Field.OPTIONAL_EDITABLE)
                    .phoneNumber(PreChatForm.Field.OPTIONAL_EDITABLE)
                    .message(PreChatForm.Field.REQUIRED_EDITABLE)
                    .build();
        }
        if (name.equals("3") && email.equals("0") && phone.equals("4")) {
            preChatForm = new PreChatForm.Builder()
                    .name(PreChatForm.Field.OPTIONAL_EDITABLE)
                    .email(PreChatForm.Field.NOT_REQUIRED)
                    .phoneNumber(PreChatForm.Field.REQUIRED_EDITABLE)
                    .message(PreChatForm.Field.REQUIRED_EDITABLE)
                    .build();
        }
        if (name.equals("3") && email.equals("4") && phone.equals("0")) {
            preChatForm = new PreChatForm.Builder()
                    .name(PreChatForm.Field.OPTIONAL_EDITABLE)
                    .email(PreChatForm.Field.REQUIRED)
                    .phoneNumber(PreChatForm.Field.NOT_REQUIRED)
                    .message(PreChatForm.Field.REQUIRED_EDITABLE)
                    .build();
        }
        if (name.equals("4") && email.equals("4") && phone.equals("4")) {
            preChatForm = new PreChatForm.Builder()
                    .name(PreChatForm.Field.REQUIRED_EDITABLE)
                    .email(PreChatForm.Field.REQUIRED_EDITABLE)
                    .phoneNumber(PreChatForm.Field.REQUIRED_EDITABLE)
                    .message(PreChatForm.Field.REQUIRED_EDITABLE)
                    .build();
        }
        if (name.equals("4") && email.equals("0") && phone.equals("3")) {
            preChatForm = new PreChatForm.Builder()
                    .name(PreChatForm.Field.REQUIRED_EDITABLE)
                    .email(PreChatForm.Field.NOT_REQUIRED)
                    .phoneNumber(PreChatForm.Field.OPTIONAL_EDITABLE)
                    .message(PreChatForm.Field.REQUIRED_EDITABLE)
                    .build();
        }
        if (name.equals("4") && email.equals("3") && phone.equals("0")) {
            preChatForm = new PreChatForm.Builder()
                    .name(PreChatForm.Field.REQUIRED_EDITABLE)
                    .email(PreChatForm.Field.OPTIONAL_EDITABLE)
                    .phoneNumber(PreChatForm.Field.NOT_REQUIRED)
                    .message(PreChatForm.Field.REQUIRED_EDITABLE)
                    .build();
        }
        return preChatForm;
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
    }
}
