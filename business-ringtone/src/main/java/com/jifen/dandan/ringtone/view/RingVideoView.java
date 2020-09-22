package com.beige.camera.advertisement.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.beige.camera.common.utils.MmkvUtil;
import com.beige.camera.advertisement.R;
import com.beige.camera.advertisement.manager.RingVideoManager;
import com.beige.camera.advertisement.phone.PhoneHolder;
import com.beige.camera.advertisement.utils.PhoneUtils;

public class RingVideoView extends FrameLayout implements IFloatView, View.OnClickListener {
    private FloatViewListener mListener;
    private FrameLayout mContainer;
    private PhoneHolder mPhoneHolder;
    private ImageView mImvEnd;
    private ImageView mImvAnswer;
    private TextView mTvNumber;
    private String mPhoneNumber;
    private TextView mTvAddress;

    public RingVideoView(@NonNull Context context, String phoneNumber) {
        super(context);
        this.mPhoneNumber = phoneNumber;
        init();
    }

    public RingVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RingVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Log.d("RingVideoView","initview");
        LayoutInflater.from(getContext()).inflate(R.layout.view_ring_video, this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mTvNumber = findViewById(R.id.tv_call_number);
        mTvAddress = findViewById(R.id.address);
        mImvEnd = findViewById(R.id.iv_hand_up);
        mContainer = findViewById(R.id.fl_view);
        mImvAnswer = findViewById(R.id.iv_pick_up);
        mImvEnd.setOnClickListener(this);
        mImvAnswer.setOnClickListener(this);
        RingVideoManager.getInstance().playVideo(getContext(), MmkvUtil.getInstance().getString("RingConstants.KEY_MP4_PATH"), mContainer);
        mPhoneHolder = new PhoneHolder();

        String showNumber = PhoneUtils.getContactName(getContext(), mPhoneNumber);
        mTvNumber.setText(showNumber);

        mTvAddress.setText("TelecomCallUtil.getCarrierAndGeo(mPhoneNumber)");
    }

    @Override
    public FloatViewParams getParams() {
        return null;
    }

    @Override
    public void setFloatViewListener(FloatViewListener listener) {
        this.mListener = listener;
    }

    @Override
    public void setParams(FloatViewParams params) {

    }

    @Override
    public void setWindowParams(WindowManager.LayoutParams wmParams) {

    }

    @Override
    public void onClick(View v) {
        RingVideoManager.getInstance().release();
        if (v.getId() == R.id.iv_back) {
            if (mListener != null) {
                mListener.onClose();
            }
        } else if (v.getId() == R.id.iv_hand_up) {
            if (mPhoneHolder != null) {
                mPhoneHolder.endCall();
            }
        } else if (v.getId() == R.id.iv_pick_up) {
            if (mPhoneHolder != null) {
                mPhoneHolder.answer();
            }
        }
    }
}
