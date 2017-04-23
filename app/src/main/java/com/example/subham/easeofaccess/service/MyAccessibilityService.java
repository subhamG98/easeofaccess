package com.example.subham.easeofaccess.service;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

/**
 * Created by my pc on 26-09-2016.
 */
public class MyAccessibilityService extends AccessibilityService {
    private static final String TAG = MyAccessibilityService.class
            .getSimpleName();


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i(TAG, "ACC::onAccessibilityEvent: " + event.getEventType());
        Log.i(TAG, "ACC::onsubhamyEvent: " + event.getPackageName());
        Log.i(TAG, "ACC::onsubhamyEvent: " + event.getSource());

        //TYPE_WINDOW_STATE_CHANGED == 32

    /*    AccessibilityNodeInfo nodeInf = null;
        AccessibilityNodeInfo nodeInfo = null;
        final int eventType = event.getEventType();
        String eventText = null;
        switch(eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "Clicked: ";
                nodeInf = this.getRootInActiveWindow();
                Log.d("AccessibilityNodeInfo", ""+ nodeInf.getChildCount());
                nodeInf.recycle();
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                AccessibilityRecordCompat record = AccessibilityEventCompat.asRecord(event);
                AccessibilityNodeInfoCompat source = record.getSource();

                ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", "TESTING TESTING");
                clipboard.setPrimaryClip(clip);

                source.performAction(AccessibilityNodeInfoCompat.ACTION_PASTE);
                //}

                Log.d("AccilityNodeIno subahm", ""+ source.getClassName());
                break;
        }


        eventText = eventText + event.getText();

        // Do something nifty with this text, like speak the composed string
        // back to the user.
        Log.d("Information", eventText);
*/


        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event
                .getEventType() || AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED == event
                .getEventType() || AccessibilityEvent.TYPE_VIEW_LONG_CLICKED == event.getEventType()) {

            Log.i("eventis=", "" + event.toString());


            AccessibilityNodeInfo nodeInfo1 = getRootInActiveWindow();

            Log.i(TAG, "ACC::nodeInfo=" + nodeInfo1);
            Log.i(TAG, "ACC::event" + event);


            if (nodeInfo1 == null) {
                return;
            }
            List<AccessibilityNodeInfo> list = nodeInfo1
                    .findAccessibilityNodeInfosByText("Paste");


            Log.i("Dekh abb sabb", "" + nodeInfo1.getCollectionInfo() + "child" + nodeInfo1.getChild(0) + "count=" + nodeInfo1.getViewIdResourceName());


            Log.i(TAG, "finding= " + OverlayShowingService.msg1);
            for (AccessibilityNodeInfo node : list) {
                Log.i(TAG, "ACC::onAccessibilityEvent: left_button " + node);
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                //MainActivity.flag=0;
            }


        }

    }

    @Override
    public void onServiceConnected() {
        Log.v(TAG, "onServiceConnected");

    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub

    }
}