package com.beige.camera.common.utils.span;

import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.widget.TextView;

/**
 * Created by zhangqiang on 2018/7/19.
 */
public class SpanUtils {


    /**
     * 为textView设置html文本
     */
    public static void setHtmlText(TextView textView, String source) {

        setHtmlText(textView, source, null);
    }

    /**
     * 为textView设置html文本
     */
    public static void setHtmlText(TextView textView, String source, URLSpanWrapper.OnClickListener onClickListener) {

        if (TextUtils.isEmpty(source)) {
            textView.setText(source);
            return;
        }
        Spanned spanned = new HtmlTextHelper().fromHtml(source, textView.getContext(), onClickListener);
        if (spanned != null) {
            Object[] spans = spanned.getSpans(0, spanned.length(), ClickableSpan.class);
            if (spans != null && spans.length > 0) {
                textView.setMovementMethod(FixedLinkMovementMethod.getInstance());
            }
        }
        textView.setText(spanned);
    }

}
