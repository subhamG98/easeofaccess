/*
 * Copyright 2013 Tristan Waddington
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.subham.easeofaccess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.subham.easeofaccess.service.ClipboardMonitorService;
import com.example.subham.easeofaccess.service.MyAccessibilityService;


public class LauncherActivity extends Activity
{

    TextView textcopied;
    Cursor c;
    private SQLiteDatabase db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: Show the contents of the clipboard history.

        textcopied=(TextView)findViewById(R.id.textcopied);
        startService(new Intent(this, ClipboardMonitorService.class));
        db=openOrCreateDatabase("EaseOfAccess", Context.MODE_PRIVATE, null);
        textcopied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = db.rawQuery("select * from Copied", null);
                if(c.moveToFirst()){
                    do{

                        Log.d("Values are=","="+c.getString(1));

                    }while (c.moveToNext());
                }
            }
        });

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1);
            }
        }
*/
        // Code to start the Service
        startService(new Intent(getApplication(), MyAccessibilityService.class));


        //finish();
    }
}
