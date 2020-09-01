package com.jifen.dandan.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jifen.dandan.R;
import com.jifen.dandan.common.base.BaseDialogFragment;
import com.jifen.dandan.common.utils.ScreenUtils;
import com.jifen.dandan.common.utils.span.SpanUtils;
import com.jifen.dandan.common.utils.span.URLSpanWrapper;
import com.zhangqiang.visiblehelper.OnVisibilityChangeListener;


public class PrivacyPoliceDialog extends BaseDialogFragment {

    private OnChoiceListener onChoiceListener;
    private TextView tvTitle;
    private TextView tvContent;
    private String pageName;

    private String defContent = "感谢您选择彩蛋视频纯净版产品和服务!<p>我们非常重视您的个人信息和隐私保护。为了更好地保障您的个人权益，在您使用我们产品前，请务必审慎阅读 <font color=\"#2395FF\"> <a href=\"quvideoopen://m.quvideo.com/WebView/WebViewActivity?webview_url=https://dd-video-h5.qtt3.cn/_quvideo/user-agreement.html\" >《用户协议》</a> </font>及<font color=\"#2395FF\"> <a  href=\"quvideoopen://m.quvideo.com/WebView/WebViewActivity?webview_url=https://dd-video-h5.qtt3.cn/_quvideo/privacy-police.html\" >《隐私政策》</a> </font> 内的所有条款，尤其是：<br/>1. 我们对您的个人信息的收集/保存/使用/对外提供/保护等规则条款，以及您的用户权利等条款；<br/>2. 约定我们的限制责任、免责条款；<br/>3. 其他以加粗或下划线进行标识的重要条款。</p><p>您点击“同意”的行为即表示您已阅读完毕并同意以上协议的全部内容。</p>";

    public static PrivacyPoliceDialog newInstance(String pageName) {
        Bundle bundle = new Bundle();
        bundle.putString("pageName",pageName);
        PrivacyPoliceDialog dialog = new PrivacyPoliceDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    public interface OnChoiceListener {

        void onAgree();

        void onDisagree();
    }

    @Override
    public int getTheme() {
        return R.style.CommonDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageName = getArguments().getString("pageName");
        getVisibleHelper().addVisibilityChangeListener(new OnVisibilityChangeListener() {
            @Override
            public void onVisibilityChange(boolean isVisible) {
                if (!isVisible) {
                    return;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_privacy_police, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        view.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChoiceListener != null) {
                    onChoiceListener.onAgree();
                }
                dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChoiceListener != null) {
                    onChoiceListener.onDisagree();
                }
                dismiss();
            }
        });

        String strContent = defContent;
        String strTitle = getResources().getString(R.string.privacy_popup_title);
        SpanUtils.setHtmlText(tvTitle,strTitle);
        SpanUtils.setHtmlText(tvContent, strContent, new URLSpanWrapper.OnClickListener() {
            @Override
            public boolean onClick(View widget, String url) {
                return false;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = (int) (ScreenUtils.getScreenWidth()*0.77f);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    public void setOnChoiceListener(OnChoiceListener onChoiceListener) {
        this.onChoiceListener = onChoiceListener;
    }
}
