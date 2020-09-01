package com.jifen.dandan.ad.core.dismiss;

import java.util.ArrayList;
import java.util.List;

public class DismissedHelper {

    private List<OnDismissedListener> adDismissListeners = new ArrayList<>();

    public void addOnAdDismissListener(OnDismissedListener onDismissedListener){
        if (adDismissListeners.contains(onDismissedListener)) {
            return;
        }
        adDismissListeners.add(onDismissedListener);
    }

    public void removeOnAdDismissListener(OnDismissedListener onDismissedListener){
        adDismissListeners.remove(onDismissedListener);
    }

    public void notifyAdDismiss(){
        for (int i = adDismissListeners.size() - 1; i >= 0; i--) {
            adDismissListeners.get(i).onDismissed();
        }
    }

    public void clear(){
        adDismissListeners.clear();
    }
}
