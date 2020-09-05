package com.jifen.dandan.ringtone.utils;

//import com.google.i18n.phonenumbers.NumberParseException;
//import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
//import com.google.i18n.phonenumbers.PhoneNumberUtil;
//import com.google.i18n.phonenumbers.Phonenumber;
//import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;


public class TelecomCallUtil {
    /*private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
    private static String LANGUAGE = "CN";

    //获取手机号码运营商
    public static String getCarrier(Context context, String phoneNumber, int countryCode) {
        Phonenumber.PhoneNumber referencePhonenumber = new Phonenumber.PhoneNumber();
        try {
            referencePhonenumber = phoneNumberUtil.parse(phoneNumber, LANGUAGE);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        //返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(referencePhonenumber, Locale.ENGLISH);
        String carrierZh = "";
        if (countryCode == 86 && Locale.CHINA.getCountry().equals(Locale.getDefault().getCountry())) {
            switch (carrierEn) {
                case "China Mobile":
                    carrierZh += "中国移动";
                    break;
                case "China Unicom":
                    carrierZh += "中国联通";
                    break;
                case "China Telecom":
                    carrierZh += "中国电信";
                    break;
                default:
                    break;
            }
            return carrierZh;
        } else {
            return carrierEn;
        }
    }

    //获取手机号码归属地
    public static String getGeo(String phoneNumber) {
        Phonenumber.PhoneNumber referencePhonenumber = null;
        try {
            referencePhonenumber = phoneNumberUtil.parse(phoneNumber, LANGUAGE);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        //手机号码归属城市 referenceRegion
        return geocoder.getDescriptionForNumber(referencePhonenumber, Locale.CHINA);
    }

    *//**
     * 获取运营商及归属地
     *
     * @param phoneNumber
     * @return
     *//*
    public static String getCarrierAndGeo(String phoneNumber) {
        Phonenumber.PhoneNumber referencePhonenumber = null;
        try {
            referencePhonenumber = phoneNumberUtil.parse(phoneNumber, LANGUAGE);

            String geo = geocoder.getDescriptionForNumber(referencePhonenumber, Locale.CHINA);
            if (!TextUtils.isEmpty(geo) && geo.endsWith("市")) {
                geo = geo.substring(0, geo.length() - 1);
            }

            //返回结果只有英文，自己转成成中文
            String carrierEn = carrierMapper.getNameForNumber(referencePhonenumber, Locale.ENGLISH);
            String carrierZh = "中国 ";
            if (Locale.CHINA.getCountry().equals(Locale.getDefault().getCountry())) {
                switch (carrierEn) {
                    case "China Mobile":
                        carrierZh += (geo + "移动");
                        break;
                    case "China Unicom":
                        carrierZh += (geo + "联通");
                        break;
                    case "China Telecom":
                        carrierZh += (geo + "电信");
                        break;
                    default:
                        carrierZh = "归属地未知";
                        break;
                }
                return carrierZh;
            } else {
                return carrierEn;
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    *//**
     * 卡1还是卡2
     *
     * @param context
     * @param phoneNumber
     * @return
     *//*
    public static String getCardNumber(Context context, String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return "";
        }
        TelecomManager telecomManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            telecomManager = (TelecomManager) context.getApplicationContext().getSystemService(Context.TELECOM_SERVICE);
            List<PhoneAccountHandle> mPhoneAccounts = telecomManager.getCallCapablePhoneAccounts();

            if (mPhoneAccounts != null && mPhoneAccounts.size() > 1) {
                for (int i = 0; i < mPhoneAccounts.size(); i++) {
                    PhoneAccount phoneAccount = null;
                    phoneAccount = telecomManager.getPhoneAccount(mPhoneAccounts.get(i));
                    if (phoneAccount != null) {
                        String tmp = phoneAccount.getSubscriptionAddress().getSchemeSpecificPart();
                        if (phoneNumber.equals(tmp)) {
                            return (i + 1) + "";
                        }
                    }
                }
            }
        }
        return null;
    }

    // Whether the call handle is an emergency number.
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isEmergencyCall(Call call) {
        Uri handle = call.getDetails().getHandle();
        return PhoneNumberUtils.isEmergencyNumber(handle == null ? "" : handle.getSchemeSpecificPart());
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getNumber(Call call) {
        if (call == null) {
            return null;
        }
        if (call.getDetails().getGatewayInfo() != null) {
            return call.getDetails().getGatewayInfo().getOriginalAddress().getSchemeSpecificPart();
        }
        Uri handle = getHandle(call);
        return handle == null ? null : handle.getSchemeSpecificPart();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static Uri getHandle(Call call) {
        return call == null ? null : call.getDetails().getHandle();
    }*/
}

