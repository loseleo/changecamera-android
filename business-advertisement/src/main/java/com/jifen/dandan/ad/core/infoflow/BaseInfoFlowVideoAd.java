package com.jifen.dandan.ad.core.infoflow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jifen.dandan.common.feed.bean.AdModel;
import com.jifen.dandan.ad.R;
import com.jifen.dandan.ad.core.infoflow.span.AdLogoSpan;
import com.jifen.dandan.ad.core.infoflow.video.VideoInfoOwner;
import com.jifen.dandan.common.utils.ScreenUtils;

public abstract class BaseInfoFlowVideoAd<T extends BaseInfoFlowVideoAd.InfoFlowVideoInfoOwner> extends InfoFlowVideoAd<T> {

    private static final int DOWNLOAD_BTN_SHOW_DELAY = 5000;
    private static final int DOWNLOAD_BTN_CHANGE_STATE_DELAY = 8000;
    private static final int REMIND_DOWNLOAD_DELAY = 13000;

    public BaseInfoFlowVideoAd(FrameLayout adContainer, AdModel adModel) {
        super(adContainer, adModel);
    }

    protected abstract int getLayoutResId();

    @Override
    protected void onSetupAdResource(FrameLayout adContainer, T ad) {

        Activity activity = (Activity) adContainer.getContext();
        Context appContext = activity.getApplicationContext();
        View adView = LayoutInflater.from(activity).inflate(getLayoutResId(), adContainer, false);
        AdViewHolder adViewHolder = new AdViewHolder(adView);
        adViewHolder.btDownload.setProgress(100);
        adViewHolder.btDownload.setSelected(false);
        AdModel adModel = getAdModel();
        String bottomMarginStr = adModel.getExtra("controller_bottom_margin");
        try {

            int bottomMargin = Integer.valueOf(bottomMarginStr);
            setMarginBottom(adViewHolder.llContainer, bottomMargin);
            setMarginBottom(adViewHolder.viewRightBar, bottomMargin);
            setSmartMarginBottom(adViewHolder.adDialogView,bottomMargin);
        } catch (Throwable e) {
            //ignore
        }

        adViewHolder.tvTitle.setText(ad.getTitle());
        SpannableStringBuilder ssb = new SpannableStringBuilder(ad.getDescription());
        int length = ssb.length();
        ssb.append("广告");
        ssb.setSpan(new AdLogoSpan(appContext), length, length + 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        adViewHolder.tvDesc.setText(ssb);
//        int appScore = ad.getAppScore();
//        adViewHolder.tvPraise.setText(String.valueOf(appScore));
//        int commentNum = (int) (appScore * 0.8f);
//        adViewHolder.tvCommentNum.setText(String.valueOf(commentNum));
//        adViewHolder.tvShareNum.setText(R.string.ad_share);
        Glide.with(appContext).asDrawable().load(ad.getIconUrl()).apply(new RequestOptions().circleCrop()).into(adViewHolder.ivAvatar);
        adViewHolder.ivAdLogo.setImageBitmap(ad.getLogo());

        Glide.with(appContext).asDrawable()
                .load(ad.getIconUrl())
                .apply(new RequestOptions()
                        .transform(new RoundedCorners(ScreenUtils.dpToPxInt(8)))).into(adViewHolder.ivDialogAvatar);
        adViewHolder.tvDialogTitle.setText(ad.getTitle());
        adViewHolder.tvDialogDesc.setText(ad.getDescription());
        adViewHolder.btDialogDownload.setProgress(100);
        adViewHolder.btDialogDownload.setSelected(true);
        adViewHolder.btDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(adViewHolder.adDialogView, "translationY", 0, adViewHolder.adDialogView.getHeight());
                objectAnimator.setDuration(300);
                objectAnimator.start();
                objectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        adViewHolder.adDialogView.setVisibility(View.GONE);
                    }
                });
            }
        });

        View.OnAttachStateChangeListener attachStateChangeListener = new View.OnAttachStateChangeListener() {

            Runnable remindDownloadTask = new Runnable() {
                @Override
                public void run() {
                    if (adViewHolder.adDialogView.getVisibility() == View.VISIBLE) {
                        return;
                    }
                    adViewHolder.adDialogView.setVisibility(View.VISIBLE);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(adViewHolder.adDialogView, "translationY", adViewHolder.adDialogView.getHeight(), 0);
                    objectAnimator.setDuration(300);
                    objectAnimator.start();
                }
            };

            Runnable showBtnTask = new Runnable() {
                @Override
                public void run() {
                    adViewHolder.flDownload.setVisibility(View.VISIBLE);
                }
            };

            Runnable changeBtnStateTask = new Runnable() {
                @Override
                public void run() {
                    adViewHolder.btDownload.setSelected(true);
                }
            };

            @Override
            public void onViewAttachedToWindow(View v) {
                adViewHolder.flDownload.setVisibility(View.GONE);
                adView.removeCallbacks(showBtnTask);
                adView.postDelayed(showBtnTask, DOWNLOAD_BTN_SHOW_DELAY);
                adViewHolder.btDownload.setSelected(false);
                adView.removeCallbacks(changeBtnStateTask);
                adView.postDelayed(changeBtnStateTask, DOWNLOAD_BTN_CHANGE_STATE_DELAY);
                adViewHolder.adDialogView.setVisibility(View.INVISIBLE);
                adView.removeCallbacks(remindDownloadTask);
                adView.postDelayed(remindDownloadTask, REMIND_DOWNLOAD_DELAY);

                onAdViewAttachedToWindow(ad, adViewHolder);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                adView.removeCallbacks(remindDownloadTask);
                adView.removeCallbacks(changeBtnStateTask);
                adView.removeCallbacks(showBtnTask);
            }
        };
        if (ViewCompat.isAttachedToWindow(adView)) {
            attachStateChangeListener.onViewAttachedToWindow(adView);
        }
        Object tag = adView.getTag(R.id.ad_tag_key_attach_listener);
        if (tag != null) {
            adView.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener) tag);
        }
        adView.setTag(R.id.ad_tag_key_attach_listener, attachStateChangeListener);
        adView.addOnAttachStateChangeListener(attachStateChangeListener);

        onSetupAdResource(ad, adViewHolder);

        adContainer.removeAllViews();
        adContainer.addView(adView);
    }

    private void setMarginBottom(View view, int bottomMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.bottomMargin = bottomMargin;
        view.setLayoutParams(layoutParams);
    }

    private void setSmartMarginBottom(View view, int bottomMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.bottomMargin += bottomMargin;
        view.setLayoutParams(layoutParams);
    }

    abstract void onSetupAdResource(T t, AdViewHolder adViewHolder);

    protected void onAdViewAttachedToWindow(T t, AdViewHolder adViewHolder) {

    }

    private static void setLeftDrawable(TextView textView, int drawableRes) {
        Drawable drawable;
        if (drawableRes == -1) {
            drawable = null;
        } else {
            drawable = ContextCompat.getDrawable(textView.getContext(), drawableRes);
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable,
                null,
                null,
                null);
    }

    static void setDownloadState(int stringRes, int leftDrawable, TextView... textView) {
        if (textView == null) {
            return;
        }
        for (TextView view : textView) {
            view.setText(stringRes);
            setLeftDrawable(view, leftDrawable);
        }
    }

    static void setDownloadState(String text, int leftDrawable, TextView... textView) {
        if (textView == null) {
            return;
        }
        for (TextView view : textView) {
            view.setText(text);
            setLeftDrawable(view, leftDrawable);
        }
    }

    public interface InfoFlowVideoInfoOwner extends VideoInfoOwner {


        String getTitle();

        CharSequence getDescription();

        int getAppScore();

        String getIconUrl();

        Bitmap getLogo();
    }

    class AdViewHolder {

        final View adView;
        final TextView tvTitle;
        final TextView tvDesc;
        final ProgressBar btDownload;
//        final TextView tvPraise;
//        final TextView tvShareNum;
//        final TextView tvCommentNum;
        final ImageView ivAvatar;
        final ImageView ivAdLogo;
        final TextView tvDownload;
        final View flDownload;
        final View adDialogView;
        final ImageView ivDialogAvatar;
        final TextView tvDialogTitle;
        final TextView tvDialogDesc;
        final ProgressBar btDialogDownload;
        final TextView tvDialogDownload;
        final View btDialogClose;
        final View llContainer;
        final View viewRightBar;

        AdViewHolder(View adView) {
            this.adView = adView;

            tvTitle = adView.findViewById(R.id.tv_title);
            tvDesc = adView.findViewById(R.id.tv_desc);
            btDownload = adView.findViewById(R.id.bt_download);
//            tvPraise = adView.findViewById(R.id.tv_like_num);
//            tvShareNum = adView.findViewById(R.id.tv_share_num);
//            tvCommentNum = adView.findViewById(R.id.tv_comment_num);
            ivAvatar = adView.findViewById(R.id.iv_avatar);
            ivAdLogo = adView.findViewById(R.id.iv_ad_logo);
            tvDownload = adView.findViewById(R.id.tv_download);
            flDownload = adView.findViewById(R.id.fl_download_container);


            adDialogView = adView.findViewById(R.id.ad_dialog_container);
            ivDialogAvatar = adView.findViewById(R.id.iv_dialog_avatar);
            tvDialogTitle = adView.findViewById(R.id.tv_dialog_title);
            tvDialogDesc = adView.findViewById(R.id.tv_dialog_desc);
            btDialogDownload = adView.findViewById(R.id.bt_dialog_download);
            tvDialogDownload = adView.findViewById(R.id.tv_dialog_download);
            btDialogClose = adView.findViewById(R.id.iv_dialog_close);

            llContainer = adView.findViewById(R.id.ll_container);
            viewRightBar = adView.findViewById(R.id.view_right_bar);
        }

        public <V extends View> V findViewById(int viewId) {
            return adView.findViewById(viewId);
        }
    }
}
