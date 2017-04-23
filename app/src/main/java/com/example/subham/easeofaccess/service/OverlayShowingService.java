package com.example.subham.easeofaccess.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class OverlayShowingService extends Service implements OnTouchListener, OnClickListener {

    private View topLeftView;

    private Button overlayedButton;
//    private Button search;

    public static EditText msg;

    public static String msg1="";

    private float offsetX;
    private float offsetY;
    private int originalXPos;
    private int originalYPos;
    private boolean moving;
    private WindowManager wm;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        overlayedButton = new Button(this);
        overlayedButton.setText("O");
        overlayedButton.setOnTouchListener(this);
        overlayedButton.setAlpha(0.0f);
        overlayedButton.setBackgroundColor(0x55fe4444);
        overlayedButton.setOnClickListener(this);

        msg=new EditText(this);
        msg.setAlpha(0.0f);
        msg.setText("Search");


        msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    Log.i("msg=", "" + msg.getText().toString());
                    msg1=msg.getText().toString();


                    wm.removeView(msg);

                    LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ALERT, LayoutParams.FLAG_NOT_FOCUSABLE
                            | LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
                    params.gravity = Gravity.LEFT | Gravity.TOP;
                    params.x = 0;
                    params.y = 0;
                    wm.addView(overlayedButton, params);





                    return true;
                }
                return false;
            }
        });

       /* search = new Button(this);
        search.setText("s");
        search.setAlpha(0.0f);
        search.setBackgroundColor(0x55fe4444);
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "msg=" + msg.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
*/


//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
  //      imm.showSoftInput(msg, InputMethodManager.SHOW_IMPLICIT);





        msg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(msg, InputMethodManager.SHOW_IMPLICIT);
                Log.i("haan haan", "1");
            }
        });



        msg.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getRawX();
                    float y = event.getRawY();

                    moving = false;

                    int[] location = new int[2];
                    msg.getLocationOnScreen(location);

                    originalXPos = location[0];
                    originalYPos = location[1];

                    offsetX = originalXPos - x;
                    offsetY = originalYPos - y;

                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int[] topLeftLocationOnScreen = new int[2];
                    topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

                    System.out.println("topLeftY="+topLeftLocationOnScreen[1]);
                    System.out.println("originalY="+originalYPos);

                    float x = event.getRawX();
                    float y = event.getRawY();

                    LayoutParams params = (LayoutParams) msg.getLayoutParams();

                    int newX = (int) (offsetX + x);
                    int newY = (int) (offsetY + y);

                    if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                        return false;
                    }

                    params.x = newX - (topLeftLocationOnScreen[0]);
                    params.y = newY - (topLeftLocationOnScreen[1]);

                    wm.updateViewLayout(msg, params);
                    moving = true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (moving) {
                        return true;
                    }
                }


                return false;
            }
        });




        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ALERT, LayoutParams.FLAG_NOT_FOCUSABLE
                | LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        wm.addView(overlayedButton, params);

        topLeftView = new View(this);
        LayoutParams topLeftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ALERT, LayoutParams.FLAG_NOT_FOCUSABLE
                | LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayedButton != null) {
            wm.removeView(overlayedButton);
            wm.removeView(topLeftView);
            overlayedButton = null;
            topLeftView = null;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getRawX();
            float y = event.getRawY();

            moving = false;

            int[] location = new int[2];
            overlayedButton.getLocationOnScreen(location);

            originalXPos = location[0];
            originalYPos = location[1];

            offsetX = originalXPos - x;
            offsetY = originalYPos - y;

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int[] topLeftLocationOnScreen = new int[2];
            topLeftView.getLocationOnScreen(topLeftLocationOnScreen);

            System.out.println("topLeftY="+topLeftLocationOnScreen[1]);
            System.out.println("originalY="+originalYPos);

            float x = event.getRawX();
            float y = event.getRawY();

            LayoutParams params = (LayoutParams) overlayedButton.getLayoutParams();

            int newX = (int) (offsetX + x);
            int newY = (int) (offsetY + y);

            if (Math.abs(newX - originalXPos) < 1 && Math.abs(newY - originalYPos) < 1 && !moving) {
                return false;
            }

            params.x = newX - (topLeftLocationOnScreen[0]);
            params.y = newY - (topLeftLocationOnScreen[1]);

            wm.updateViewLayout(overlayedButton, params);
            moving = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (moving) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
       // Toast.makeText(this, "Overlay button click event", Toast.LENGTH_SHORT).show();
        wm.removeView(overlayedButton);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ALERT, LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP;

        topLeftView = new View(this);
        LayoutParams topLeftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ALERT, LayoutParams.FLAG_NOT_FOCUSABLE
                | LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        topLeftParams.gravity = Gravity.LEFT | Gravity.TOP;
        topLeftParams.x = 0;
        topLeftParams.y = 0;
        topLeftParams.width = 0;
        topLeftParams.height = 0;
        wm.addView(topLeftView, topLeftParams);

        wm.addView(msg, params);




    }


}

