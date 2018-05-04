package com.example.bigoder.wearandphone_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.DismissOverlayView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.bigoder.wearandphone.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Random;
import java.lang.Object;
import java.util.ArrayList;

public class MainActivity extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private DismissOverlayView mDismissOverlay;
    public String obj[]=new String[24];
    GoogleApiClient mGoogleAppiClient;

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;
    long t1 = 0;
    long t2 = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obj = testRandom();
        setContentView(R.layout.activity_main);
        buildStartDialog();
        initViewPager();
        mGoogleAppiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        // Obtain the DismissOverlayView element
        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(R.string.long_press_intro);
        mDismissOverlay.showIntroIfNecessary();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    public void buildStartDialog(){
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("————————————")
                .setMessage("你准备开始了么？")
                .setPositiveButton("开始", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendTextToPhone(" ");
                        dialog.dismiss();
                    }
                }).show();
    }


    private String[] testRandom(){
        Random random = new Random();
        int a[] = new int[24];
        String[] result = new String[a.length];
        for(int i=0; i<a.length; i++){
            a[i] = random.nextInt(24)+1;
            for(int j=0; j<i; j++){
                while(a[i]==a[j]){
                    i--;
                }
            }
        }
        for (int i=0; i<a.length; i++){
            result[i]=a[i]+"";
        }
        return result;
    }



    private void sendTextToPhone(String content){
        PutDataMapRequest request1 = PutDataMapRequest.create("/only_phone");
        DataMap dataMap1 = request1.getDataMap();
        dataMap1.putLong("time", System.currentTimeMillis());
        dataMap1.putString("content", content);
        Wearable.DataApi.putDataItem(mGoogleAppiClient, request1.asPutDataRequest());
    }

    public void onDataChanged(DataEventBuffer dataEventBuffer) {
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
            t1 = event.getEventTime();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            t2 = event.getEventTime();
            String xy = "(" + String.valueOf(x1) + "," + String.valueOf(y1) +
                    ")-(" + String.valueOf(x2) + "," + String.valueOf(y2) + ")";
            if(Math.abs(y1 - y2) < 30 && Math.abs(x1 - x2) < 30 && t2 - t1 > 3000){
                mDismissOverlay.show();
            }
            if(Math.abs(y1 - y2) < 30 && Math.abs(x1 - x2) < 30 && t2 - t1 <= 3000){
                sendTextToPhone("点击位置：" + "(" + String.valueOf(x1) + "," + String.valueOf(y1) + ")" );
            }
            if (Math.abs(y1 - y2) > 30) {
                sendTextToPhone(xy);
            } else if (Math.abs(x1 - x2) > 30) {
                sendTextToPhone(xy);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    protected void onStart() {
        mGoogleAppiClient.connect();
        super.onStart();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleAppiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop(){
        if(null != mGoogleAppiClient && mGoogleAppiClient.isConnected()){
            Wearable.DataApi.removeListener(mGoogleAppiClient,this);
            mGoogleAppiClient.disconnect();
        }
        super.onStop();
    }

    private void initViewPager(){
        final ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);

        View view1 = LayoutInflater.from(this).inflate(R.layout.activity_1st, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.activity_2nd, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.activity_3rd, null);

        final Button button1 = (Button) view1.findViewById(R.id.button1);
        final Button button2 = (Button) view1.findViewById(R.id.button2);
        final Button button3 = (Button) view1.findViewById(R.id.button3);
        final Button button4 = (Button) view1.findViewById(R.id.button4);
        final Button button5 = (Button) view1.findViewById(R.id.button5);
        final Button button6 = (Button) view1.findViewById(R.id.button6);
        final Button button7 = (Button) view1.findViewById(R.id.button7);
        final Button button8 = (Button) view1.findViewById(R.id.button8);

        final Button button9 = (Button) view2.findViewById(R.id.button9);
        final Button button10 = (Button) view2.findViewById(R.id.button10);
        final Button button11 = (Button) view2.findViewById(R.id.button11);
        final Button button12 = (Button) view2.findViewById(R.id.button12);
        final Button button13 = (Button) view2.findViewById(R.id.button13);
        final Button button14 = (Button) view2.findViewById(R.id.button14);
        final Button button15 = (Button) view2.findViewById(R.id.button15);
        final Button button16 = (Button) view2.findViewById(R.id.button16);

        final Button button17 = (Button) view3.findViewById(R.id.button17);
        final Button button18 = (Button) view3.findViewById(R.id.button18);
        final Button button19 = (Button) view3.findViewById(R.id.button19);
        final Button button20 = (Button) view3.findViewById(R.id.button20);
        final Button button21 = (Button) view3.findViewById(R.id.button21);
        final Button button22 = (Button) view3.findViewById(R.id.button22);
        final Button button23 = (Button) view3.findViewById(R.id.button23);
        final Button button24 = (Button) view3.findViewById(R.id.button24);

        button1.setText(obj[0]);
        button2.setText(obj[1]);
        button3.setText(obj[2]);
        button4.setText(obj[3]);
        button5.setText(obj[4]);
        button6.setText(obj[5]);
        button7.setText(obj[6]);
        button8.setText(obj[7]);

        button9.setText(obj[8]);
        button10.setText(obj[9]);
        button11.setText(obj[10]);
        button12.setText(obj[11]);
        button13.setText(obj[12]);
        button14.setText(obj[13]);
        button15.setText(obj[14]);
        button16.setText(obj[15]);

        button17.setText(obj[16]);
        button18.setText(obj[17]);
        button19.setText(obj[18]);
        button20.setText(obj[19]);
        button21.setText(obj[20]);
        button22.setText(obj[21]);
        button23.setText(obj[22]);
        button24.setText(obj[23]);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[0]);
                Drawable drawable = button1.getBackground();
                button1.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button1.setBackgroundDrawable(drawable);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[1]);
                Drawable drawable = button2.getBackground();
                button2.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button2.setBackgroundDrawable(drawable);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[2]);
                Drawable drawable = button3.getBackground();
                button3.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button3.setBackgroundDrawable(drawable);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[3]);
                Drawable drawable = button4.getBackground();
                button4.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button4.setBackgroundDrawable(drawable);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[4]);
                Drawable drawable = button5.getBackground();
                button5.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button5.setBackgroundDrawable(drawable);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[5]);
                Drawable drawable = button6.getBackground();
                button6.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button6.setBackgroundDrawable(drawable);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[6]);
                Drawable drawable = button7.getBackground();
                button7.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button7.setBackgroundDrawable(drawable);
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[7]);
                Drawable drawable = button8.getBackground();
                button8.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button8.setBackgroundDrawable(drawable);
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[8]);
                Drawable drawable = button9.getBackground();
                button9.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button9.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[9]);
                Drawable drawable = button10.getBackground();
                button10.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button10.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[10]);
                Drawable drawable = button11.getBackground();
                button11.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button11.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[11]);
                Drawable drawable = button12.getBackground();
                button12.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button12.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[12]);
                Drawable drawable = button13.getBackground();
                button13.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button13.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[13]);
                Drawable drawable = button14.getBackground();
                button14.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button14.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[14]);
                Drawable drawable = button15.getBackground();
                button15.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button15.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[15]);
                Drawable drawable = button16.getBackground();
                button16.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button16.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[16]);
                Drawable drawable = button17.getBackground();
                button17.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button17.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[17]);
                Drawable drawable = button18.getBackground();
                button18.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button18.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[18]);
                Drawable drawable = button19.getBackground();
                button19.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button19.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[19]);
                Drawable drawable = button20.getBackground();
                button20.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button20.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[20]);
                Drawable drawable = button21.getBackground();
                button21.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button21.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[21]);
                Drawable drawable = button22.getBackground();
                button22.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button22.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[22]);
                Drawable drawable = button23.getBackground();
                button23.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button23.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });
        button24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextToPhone(obj[23]);
                Drawable drawable = button24.getBackground();
                button24.setBackgroundColor(65280);
                try {Thread.sleep(400);} catch (InterruptedException e) {e.printStackTrace();}
                button24.setBackgroundDrawable(drawable);
                viewPager.setCurrentItem(0);
            }
        });


        ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);

        MYViewPagerAdapter adapter = new MYViewPagerAdapter();
        adapter.setViews(views);
        viewPager.setAdapter(adapter);
    }
}

class MYViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> views;

    public void setViews(ArrayList<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(views.get(position));
        return views.get(position);
    }
}
