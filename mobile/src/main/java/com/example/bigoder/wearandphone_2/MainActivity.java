package com.example.bigoder.wearandphone_2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bigoder.wearandphone.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

import android.media.AudioManager;
import android.media.SoundPool;

public class MainActivity extends ActionBarActivity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleAppiClient;
    public String obj[]=new String[24];
    private SoundPool sp;//声明一个SoundPool
    private int music;//定义一个整型用load（）；来设置suondID
    int sum = 0;
    int count = 0;
    String correctNum;
    String compareNum;
    int aBooleanStart = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obj = testRandom();
        setContentView(R.layout.activity_main);
        mGoogleAppiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        TextView text = (TextView) this.findViewById(R.id.textView);
        text.setText(obj[0]);
    }

    public void playSounds(){
        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.peiyin, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sp.play(music, 1, 1, 0, 0, 1);
    }

    public void playSoundsEnd(){
        sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        music = sp.load(this, R.raw.end, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sp.play(music, 1, 1, 0, 0, 1);
    }


    private void text() throws IOException {
        TextView text = (TextView) this.findViewById(R.id.textView);
        playSounds();
        text.setText(obj[count]);
        text.invalidate();
        textPrint();
    }

    private void textPrint() throws IOException {
        FileOutputStream phone_outStream = null;
        phone_outStream =openFileOutput("2.txt", Context.MODE_APPEND); //追加模式继续写
        phone_outStream.write((String.valueOf(correctNum) + ":" + String.valueOf(compareNum) + "\r\n").getBytes());
        if (count == 5 ){
            phone_outStream.write(("正确总数为：" + String.valueOf(sum) + "\r\n").getBytes());
        }
    }

    private void textPrintXY(String content) throws IOException {
        FileOutputStream phone_outStream = null;
        phone_outStream =openFileOutput("2_XY.txt", Context.MODE_APPEND); //追加模式继续写
        phone_outStream.write((content + "\r\n").getBytes());
    }

    public class CompareThread extends Thread{
        String info;
        public CompareThread(String mInfo) {
            this.info = mInfo;
        }
        public void run(){
            getCompareResult(info);
            super.run();
        }
    }

    private int getCompareResult(String info){
        int numCompareStr = 0;
        correctNum = String.valueOf(obj[count]);
        compareNum = info;
        if(correctNum.equals(compareNum)){
            numCompareStr = 1;
        }
        sum = sum + numCompareStr;
        count++;
        return sum;
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

    @Override
    protected void onStart() {
        mGoogleAppiClient.connect();
        super.onStart();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for(DataEvent event: dataEventBuffer){
            if(event.getType() == DataEvent.TYPE_DELETED){

            }else if(event.getType() == DataEvent.TYPE_CHANGED){
                DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                if(event.getDataItem().getUri().getPath().equals("/only_phone")){
                    String content = dataMap.get("content");
                    long currentTimeMillis = System.currentTimeMillis();
                    Date date = new Date(currentTimeMillis);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss SSS");
                    if(count == 0 && aBooleanStart == 0){
                        try {
                            textPrintXY("——！！开始！!—— at " + simpleDateFormat.format(date));
                            aBooleanStart = 1;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(count == 5){
                        buildStartDialog();
                    }else {
                        if(content.length() < 5){
                            new CompareThread(content).start();

                            try {
                                text();
                                textPrintXY("——点击—— at " + simpleDateFormat.format(date));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                textPrintXY(content);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public void buildStartDialog(){
        playSoundsEnd();
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("恭喜")
                .setMessage("恭喜你，完成了这一部分的任务")
                .setPositiveButton("结束", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
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

    protected void onStop(){
        if(null != mGoogleAppiClient && mGoogleAppiClient.isConnected()){
            Wearable.DataApi.removeListener(mGoogleAppiClient,this);
            mGoogleAppiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

