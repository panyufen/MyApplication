package org.apache.cordova;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PAN on 2016/8/11.
 */
public class MyCordovaPlugIn extends CordovaPlugin {

    private String ACTION = "show";

    private CallbackContext mCallbackContext;

    @Override
    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException {
        if (ACTION.equals(action)) {
            mCallbackContext = callbackContext;
            PluginResult dataResult = new PluginResult(PluginResult.Status.OK, "{'a':123,'b':234}");
            dataResult.setKeepCallback(true);
            callbackContext.sendPluginResult(dataResult);

            return true;
        }
        return false;
    }

    public void sendCmd(JSONObject cmd) {
        if (mCallbackContext != null) {
            PluginResult dataResult = new PluginResult(PluginResult.Status.ERROR, cmd);
            dataResult.setKeepCallback(true);//非常重要
            mCallbackContext.sendPluginResult(dataResult);
        }
    }

    public interface Abc{
        void showToast();
    }


    /**
     * Hook for blocking the loading of external resources.
     * <p>
     * This will be called when the WebView's shouldInterceptRequest wants to
     * know whether to open a connection to an external resource. Return false
     * to block the request: if any plugin returns false, Cordova will block
     * the request. If all plugins return null, the default policy will be
     * enforced. If at least one plugin returns true, and no plugins return
     * false, then the request will proceed.
     * <p>
     * Note that this only affects resource requests which are routed through
     * WebViewClient.shouldInterceptRequest, such as XMLHttpRequest requests and
     * img tag loads. WebSockets and media requests (such as <video> and <audio>
     * tags) are not affected by this method. Use CSP headers to control access
     * to such resources.
     *
     * @param url
     */
    @Override
    public Boolean shouldAllowRequest(String url) {
        return true;
    }

    @Override
    public Boolean shouldAllowNavigation(String url) {
        return true;
    }
}