package com.beige.camera.common.guide.lifecycle;

public interface FragmentLifecycle {
    void onStart();

    void onStop();

    void onDestroyView();

    void onDestroy();
}