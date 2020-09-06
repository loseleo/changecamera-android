package com.jifen.dandan.common.utils.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.jifen.dandan.common.R;
import com.jifen.dandan.common.utils.ScreenUtils;
import com.jifen.dandan.common.utils.imageloader.transform.RoundedCornersTransformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * 鍥剧墖涓嬭浇宸ュ叿绫�
 *
 * @author gaozhibin
 */
public class BitmapUtil {

    private final static int DefaultCornerRadius = 2;



    private static void setAnimationDrawable(ImageView imageView, @DrawableRes int errorDrawableId) {
        if (imageView == null) {
            return;
        }
        imageView.setImageResource(errorDrawableId);
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }
    }

    public interface LoadPicCallback {
        void onSuccess();

        void onFaild();
    }

    public static void loadImage(String url, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }

    public static void loadImage(int drawable, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context).load(drawable).into(imageView);
    }

    public static void loadImageWithErrorRes(String url, ImageView imageView, int errorResId) {
        if (imageView == null) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(errorResId);
            return;
        }
        Context context  = imageView.getContext();
        Glide.with(context).load(url).apply(new RequestOptions().error(errorResId)).into(imageView);
    }

    public static void loadImageWithServerScale(final Context context
            , final String imageUrl
            , ImageView to
            , int placeHolder
            , int err) {
        if (context == null) {
            return;
        }

        loadImageWithServerScale(context, imageUrl, to, placeHolder, err, DefaultCornerRadius);
    }

    /**
     * 服务器支持缩放
     *
     * @param context
     * @param imageUrl
     * @param imageView
     * @param placeholder
     * @param error
     * @param radius
     */
    public static void loadImageWithServerScale(final Context context
            , final String imageUrl
            , ImageView imageView
            , int placeholder
            , int error
            , int radius) {
        if (context == null) {
            return;
        }
        try {
            if (isOnMainThread()) {

                RequestOptions requestOptions;
                if (radius > 0) {
                    requestOptions = new RequestOptions()
                            .placeholder(placeholder)
                            .error(error)
                            .centerCrop()
                            .transform(new GlideRoundTransformCenterCrop(ScreenUtils.dpToPxInt(radius)))
                            .diskCacheStrategy(DiskCacheStrategy.ALL);

                } else {
                    requestOptions = new RequestOptions()
                            .placeholder(placeholder)
                            .error(error)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL);
                }


                Glide.with(context)
                        .load(imageUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }

                        })
                        .apply(requestOptions)
                        .into(new DrawableImageViewTarget(imageView) {
                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                super.setResource(resource);
                            }
                        });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadImage(final Context context, final String mImageUrl, ImageView to, int placeholder, int error) {
        try {
            if (context == null) {
                return;
            }
            RequestOptions requestOptions = new RequestOptions();
            if (placeholder > 0) {
                requestOptions = requestOptions.placeholder(placeholder);
            }

            if (error > 0) {
                requestOptions = requestOptions.error(error);
            }

            requestOptions = requestOptions.centerInside()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);


            Glide.with(context)
                    .load(mImageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .apply(requestOptions)
                    .into(to);
        } catch (Exception e) {
//            Glide.clear(to);
        }
    }

    public static void loadImageNoScale(final Context context, final String mImageUrl, ImageView to, int placeholder, int error) {
        try {
            if (context == null) {
                return;
            }
            Glide.with(context)
                    .load(mImageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .apply(new RequestOptions().placeholder(placeholder)
                            .fallback(error))
                    .into(to);
        } catch (Exception e) {
//            Glide.clear(to);
        }
    }

    public static void loadImage(Context context, File file, ImageView to) {
        try {
            if (context == null) {
                return;
            }
            Glide.with(context)
                    .load(file)
                    .apply(new RequestOptions().centerCrop()
                            .placeholder(R.drawable.common_bg_erroe_default)
                            .fallback(R.drawable.common_bg_erroe_default))
                    .into(to);
        } catch (Exception e) {
//            Glide.clear(to);
        }
    }

    public static void loadImageRound(final Context context, final String mImageUrl, final ImageView imageView, int placeholder, int error, int radius, RoundedCornersTransformation.CornerType cornerType) {

        try {
            if (isOnMainThread()) {
                Glide.with(context)
                        .load(mImageUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .apply(new RequestOptions().placeholder(placeholder)
                                .error(error)
                                .centerCrop()
                                .transform(new RoundedCornersTransformation(ScreenUtils.dpToPxInt(radius), 0, cornerType))
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(new DrawableImageViewTarget(imageView) {
                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                super.setResource(resource);
                            }
                        });

            }
        } catch (Exception e) {
            return;
        }
    }

    public static void loadImageRound(final Context context, final String mImageUrl, final ImageView imageView, int placeholder, int error) {
        loadImageRound(context, mImageUrl, imageView, placeholder, error, 2);
    }

    public static void loadImageRound(final Context context, final String mImageUrl, final ImageView imageView, int placeholder, int error, final int radius) {


        try {
            if (isOnMainThread()) {
                Glide.with(context)
                        .load(mImageUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .apply(new RequestOptions().placeholder(placeholder)
                                .error(error)
                                .centerCrop()
                                .transform(new GlideRoundTransformCenterCrop(ScreenUtils.dpToPxInt(radius)))
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(new DrawableImageViewTarget(imageView) {
                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                super.setResource(resource);
                            }
                        });

            }
        } catch (Exception e) {
            //
        }
    }

    public static void loadImageRound(final Context context, int resId, final ImageView imageView, final int radius) {

        try {
            if (isOnMainThread()) {
                Glide.with(context)
                        .load(resId)
                        .apply(new RequestOptions().placeholder(resId)
                                .error(resId)
                                .centerCrop()
                                .transform(new GlideRoundTransformCenterCrop(ScreenUtils.dpToPxInt(radius)))
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(new DrawableImageViewTarget(imageView) {
                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                super.setResource(resource);
                            }
                        });

            }
        } catch (Exception e) {
            //
        }
    }

    public static void loadImageRoundNoScale(final Context context, final String mImageUrl, final ImageView imageView, int placeholder, int error, final int radius) {


        try {
            if (isOnMainThread()) {
                Glide.with(context)
                        .load(mImageUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .apply(new RequestOptions().placeholder(placeholder)
                                .error(error)
                                .transform(new GlideRoundTransformCenterCrop(ScreenUtils.dpToPxInt(radius)))
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(new DrawableImageViewTarget(imageView) {
                            @Override
                            protected void setResource(@Nullable Drawable resource) {
                                super.setResource(resource);
                            }
                        });

            }
        } catch (Exception e) {
            //
        }
    }

    public static void loadImageRoundByListerner(final Context context, final String mImageUrl, final ImageView ivBookfaceBookPic, int placeholder, int error, final int radius, final LoadPicCallback mLoadPicCallback) {

        try {
            if (isOnMainThread()) {
                Glide.with(context)
                        .load(mImageUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                if (mLoadPicCallback != null) {
                                    mLoadPicCallback.onFaild();
                                }
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                if (mLoadPicCallback != null) {
                                    mLoadPicCallback.onSuccess();
                                }

                                return false;
                            }
                        })
                        .apply(new RequestOptions().placeholder(placeholder)
                                .error(error)
                                .centerCrop()
                                .transform(new GlideRoundTransformCenterCrop(ScreenUtils.dpToPxInt(radius)))
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(ivBookfaceBookPic);

            }
        } catch (Exception e) {
            return;
        }
    }

    /**
     * 加载图标 drawable
     *
     * @param context      上下文
     * @param mImageUrl    图片地址
     * @param placeholder  默认
     * @param error        错误
     * @param simpleTarget 目标
     */
    public static void loadImage(final Context context, final String mImageUrl, int placeholder, int error, SimpleTarget<Drawable> simpleTarget) {
        try {
            if (isOnMainThread()) {
                Glide.with(context)
                        .asDrawable()
                        .load(mImageUrl)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .apply(new RequestOptions().placeholder(placeholder)
                                .fallback(error)
                                .centerCrop())
                        .into(simpleTarget);
            }
        } catch (Exception e) {
            //
        }
    }


    /**
     * 圆形头像
     */
    public static void loadImageCircle(Context context, String imageUrl, int defaultResId, ImageView view) {
        if (context == null) {
            return;
        }
        try {
            Glide.with(context)
                    .load(imageUrl)
                    .apply(new RequestOptions()
                            .placeholder(defaultResId)
                            .error(defaultResId)
                            .centerCrop()
                            .transform(new CircleCrop()))
                    .into(view);
        } catch (Exception e) {
//            Glide.clear(view);
        }
    }


    public static Bitmap readBitMap(Context context, String path) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        return BitmapFactory.decodeFile(path, opt);
    }


//    public static class GlideCircleTransform extends BitmapTransformation {
//
//        public GlideCircleTransform(Context mContext) {
//            super(mContext);
//        }
//
//        @Override
//        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//            return circleCrop(pool, toTransform);
//        }
//
//        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
//            if (source == null)
//                return null;
//            int size = Math.min(source.getWidth(), source.getHeight());
//            int x = (source.getWidth() - size) / 2;
//            int y = (source.getHeight() - size) / 2;
//
//            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
//
//            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
//            if (result == null) {
//                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
//            }
//
//            Canvas canvas = new Canvas(result);
//            Paint paint = new Paint();
//            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
//            paint.setAntiAlias(true);
//            float r = size / 2f;
//            canvas.drawCircle(r, r, r, paint);
//            return result;
//        }
//
//        @Override
//        public String getId() {
//            return getClass().getName();
//        }
//    }


    public static boolean isOnMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static void loadImage(String url, ImageView imageView, final ImageLoadListener listener, boolean skipCacheMm) {
        try {

            RequestOptions requestOptions = new RequestOptions()
                    .centerCrop()
                    .skipMemoryCache(skipCacheMm)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(imageView.getContext())
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (listener != null) {
                                listener.onFail(e);
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (listener != null) {
                                ImageInfo imageInfo = new ImageInfo();
                                imageInfo.setWidth(resource.getIntrinsicWidth());
                                imageInfo.setHeight(resource.getIntrinsicHeight());
                                listener.onDisplay(imageInfo);
                            }
                            return false;
                        }
                    })
                    .apply(requestOptions)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 加载高斯模糊图片
     *
     * @param url       url
     * @param imageView imageView
     */


    /**
     * 加载本地URL图像
     */
    public static void loadUriImage(Context context, String mImageUrl, int resourceId, ImageView view) {
        try {
            Glide.with(context)
                    .load(Uri.parse(mImageUrl))
                    .apply(new RequestOptions()
                            .placeholder(resourceId)
                            .error(resourceId)
                            .centerCrop()
                            .transform(new CircleCrop()))
                    .into(view);
        } catch (Exception e) {
//            Glide.clear(view);
        }
    }

    /**
     * 保存bitmap到文件
     *
     * @param bitmap    bitmap
     * @param savedPath 文件路径，包括文件名
     * @return true 成功，false 失败
     */
    public static boolean saveBitmap(Bitmap bitmap, String savedPath) {

        FileOutputStream fos = null;
        try {

            File file = new File(savedPath);
            String parent = file.getParent();
            File dir = new File(parent);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return false;
                }
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            return true;
        } catch (Throwable e) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }


    private static class FitWidth extends FitCenter {

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
            int bWidth = toTransform.getWidth();
            int bHeight = toTransform.getHeight();
            float bFactor = (float) bWidth / bHeight;
            float factor = (float) outWidth / outHeight;
            if (bFactor < factor) {
                return TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
            } else {
                return TransformationUtils.fitCenter(pool, toTransform, outWidth, outHeight);
            }
        }
    }
}
