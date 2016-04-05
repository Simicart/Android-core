package com.simicart.plugins.zopimchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

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
public class SimiChatActivity extends ActionBarActivity implements ChatListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void startChat(){
        PreChatForm preChatForm = new PreChatForm.Builder()
                .name(PreChatForm.Field.REQUIRED_EDITABLE)
                .email(PreChatForm.Field.REQUIRED_EDITABLE)
                .phoneNumber(PreChatForm.Field.REQUIRED_EDITABLE)
                .department(PreChatForm.Field.REQUIRED_EDITABLE)
                .message(PreChatForm.Field.REQUIRED_EDITABLE)
                .build();
        // build chat config
        ZopimChat.SessionConfig config = new ZopimChat.SessionConfig()
                .preChatForm(preChatForm);
        // prepare chat fragment
        ZopimChatFragment fragment = ZopimChatFragment.newInstance(config);
        // show fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(com.zopim.android.sdk.R.id.chat_fragment_container, fragment, ZopimChatFragment.class.getName());
        transaction.commit();
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
    @Override
    public void onChatLoaded(Chat chat) {

    }

    @Override
    public void onChatInitialized() {

    }

    @Override
    public void onChatEnded() {

    }
}
