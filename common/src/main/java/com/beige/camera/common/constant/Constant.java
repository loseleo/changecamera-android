/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.beige.camera.common.constant;


import com.beige.camera.common.BuildConfig;
import com.beige.camera.common.utils.AppUtils;
import com.beige.camera.common.utils.FileUtils;

/**
 * @author zhangning.
 * @date 16/8/5.
 */
public class Constant {


    public static String AppName = "android";
    public static String API_BASE_URL = BuildConfig.SERVER_URL;
    public static  String UMENG_APPKEY = BuildConfig.KEY_UMENG;
    public static  String BUGLY_APPID = BuildConfig.KEY_BUGLY;

    public static String PATH_DATA = FileUtils.createRootPath(AppUtils.getAppContext()) + "/cache";





    public static final String SUFFIX_ZIP = ".zip";

    public static int sScreenWidth ;

    public static int sScreenHeight ;

    public static int sTrialCount = 5 ;




}
