package com.verso.cordova.clipboard;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.ClipDescription;

public class Clipboard extends CordovaPlugin {

    private static final String actionCopy = "copy";
    private static final String actionPaste = "paste";

    @Override
    public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        final ClipboardManager clipboard = (ClipboardManager) cordova.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (action.equals(actionCopy)) {
            try {
                final String text = args.getString(0);
                final ClipData clip = ClipData.newPlainText("Text", text);
                clipboard.setPrimaryClip(clip);
                callbackContext.success(text);
                return true;
            } catch (final JSONException e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
            } catch (final Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
            }
        } else if (action.equals(actionPaste)) {
            try {
                if (!clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.NO_RESULT));
                } else {
                    final ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                    final String text = item.getText() == null? "" : item.getText().toString();
                    callbackContext.success(text);
                }
                return true;
            } catch (final Exception e) {
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
            }
        }

        return false;
    }
}
