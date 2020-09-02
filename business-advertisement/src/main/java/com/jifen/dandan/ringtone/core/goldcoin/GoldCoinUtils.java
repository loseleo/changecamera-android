package com.jifen.dandan.ringtone.core.goldcoin;

import com.iclicash.advlib.core.ICliBundle;

public class GoldCoinUtils {

    public static int getGoldCoin(ICliBundle iCliBundle) {
        if (iCliBundle != null && iCliBundle.tbundle != null) {
            return iCliBundle.tbundle.getInt("non_standard_auto_coin", 0);
        }
        return 0;
    }
}
