package com.beige.camera.ringtone.phone;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;

import com.beige.camera.common.utils.AppUtils;
import com.beige.camera.ringtone.interfaces.ICallAcceptor;

public class Above19CallAcceptor implements ICallAcceptor {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void answer() {
        AudioManager audioManager = (AudioManager) AppUtils.getAppContext().getSystemService(Context.AUDIO_SERVICE);
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
        KeyEvent keyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
        audioManager.dispatchMediaKeyEvent(keyEvent);
        audioManager.dispatchMediaKeyEvent(keyEvent2);
    }
}
