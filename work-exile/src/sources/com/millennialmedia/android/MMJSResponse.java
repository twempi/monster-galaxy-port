package com.millennialmedia.android;

import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
class MMJSResponse {
    static final int ERROR = 0;
    static final int SUCCESS = 1;
    String className;
    byte[] dataResponse;
    String methodName;
    Object response;
    int result;

    MMJSResponse() {
    }

    static MMJSResponse responseWithSuccess() {
        return responseWithSuccess("Success.");
    }

    static MMJSResponse responseWithSuccess(String success) {
        MMJSResponse response = new MMJSResponse();
        response.result = 1;
        response.response = success;
        return response;
    }

    static MMJSResponse responseWithError() {
        return responseWithError("An unknown error occured.");
    }

    static MMJSResponse responseWithError(String error) {
        MMJSResponse response = new MMJSResponse();
        response.result = 0;
        response.response = error;
        return response;
    }

    String toJSONString() {
        try {
            if (this.className == null || this.methodName == null) {
                return "";
            }
            JSONObject object = new JSONObject();
            object.put("class", this.className);
            object.put("call", this.methodName);
            object.put("result", this.result);
            if (this.response != null) {
                object.put("response", this.response);
            } else if (this.dataResponse != null) {
                object.put("response", Base64.encodeToString(this.dataResponse, false));
            } else {
                return "";
            }
            return object.toString();
        } catch (JSONException jsonException) {
            MMAdViewSDK.Log.e(jsonException.getMessage());
            return "";
        }
    }
}
