package com.bstek.ureport.exception;

/**
 * @author janson
 */
public class Errorcode {
    public final static int SUCCESS = 0;
    public final static int FAILED = 1;

    public static String SUCCESS_MSG = "success";
    public static String FAILED_MSG = "failed";

    public final static int UNEXISTED_ERROR = 2000;

    public final static int PASSWORD_ERROR = 2001;
    public final static int TOKEN_ERROR = 2002;
    public final static int EXISTED_ERROR = 2003;
    public final static int PARAMS_ERROR = 2004;

    public final static int LOCKED_ERROR = 2005;
    public final static int UNAUTH_ERROR = 2006;

    public final static int UNCONNECTED_ERROR = 2007;
    public final static int CONNECTED_ERROR = 2008;

    public final static int INTERNAL_ERROR = 2010;
    public final static int NETWORK_ERROR = 2011;
    public final static int TOKNE_EXPIRED = 2012;
    public final static int CODE_ERROR = 2013;
    public final static int CODE_EXPIRED = 2014;
    public final static int UNAUTHORITY_ERROR = 2015;
    public final static int CODEC_ERROR = 2020;
    public static int INTERNAL_ERROR_OLD = 4000;
    public final static int NO_DATA_PRIVILEGES = 3000;
    public static String NO_DATA_PRIVILEGES_MSG = "No data operation privileges";

    public final static int ERROR_APPID_SECRET 	= 6001;
    public final static int ERROR_PARAM 		= 6002;
    public final static int ERROR_CHARGER_ID 	= 6003;
    public final static int ERROR_OTHERS 		= 6600;
    public static int EVCS_SUCCESS = 0;
    public static String EVCS_SUCCESS_MSG = "request success";
    public static int EVCS_UNEXISTED_ERROR = -1;
    public static String EVCS_UNEXISTED_ERROR_MSG = "the system is busy, and the requester will try again later";
    public static int EVCS_SIGN_ERROR = 4001;
    public static String EVCS_SIGN_ERROR_MSG = "sign error";
    public static int EVCS_TOKEN_ERROR = 4002;
    public static String EVCS_TOKEN_ERROR_MSG = "token error";
    public static int EVCS_PARAMETER_ERROR = 4003;
    public static String EVCS_PARAMETER_ERROR_MSG = "POST parameter not legal";
    public static int EVCS_BUSINESS_PARAMETER_ERROR = 4004;
    public static String EVCS_BUSINESS_PARAMETER_ERROR_MSG = "request parameter not legal";
    public static int EVCS_SYSTEM_ERROR = 500;
    public static String EVCS_SYSTEM_ERROR_MSG = "system error";




}
