package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button button_add;
    Button button_min;
    Button button_st;
    Button button_end;
    TextView textView;
    TextView textView_unit;
    RadioGroup radioGroup;
    Uri uri;//系统自带提示音
    Ringtone rt ;
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_add = findViewById(R.id.btn_add);
        button_min = findViewById(R.id.btn_min);
        button_st = findViewById(R.id.start);
        button_end = findViewById(R.id.end);
        button_st.setOnClickListener(this);
        button_add.setOnClickListener(this);
        button_min.setOnClickListener(this);
        button_end.setOnClickListener(this);
        radioGroup = findViewById(R.id.time);
        textView = findViewById(R.id.text);
        textView_unit = findViewById(R.id.unit);
        RadioButton radio_sec = findViewById(R.id.sec);
        RadioButton radio_min = findViewById(R.id.min);
        flag = true;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton r = findViewById(checkedId);
                textView_unit.setText(r.getText());
            }
        });
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        rt = RingtoneManager.getRingtone(getApplicationContext(), uri);
    }
    @Override
    public void onClick(View v) {
        int tmp;
        TimeTickLoad timeTickLoad = new TimeTickLoad();
        switch (v.getId()){
            case R.id.btn_add:
                if(button_st.getText().equals("运行中...")){
                    Toast.makeText(this,"正在运行中，请勿点击",Toast.LENGTH_SHORT).show();
                }
                else{
                    tmp = Integer.valueOf(textView.getText().toString());
                    tmp++;
                    textView.setText(tmp+"");
                }
                break;
            case R.id.btn_min:
                if(button_st.getText().equals("运行中...")){
                    Toast.makeText(this,"正在运行中，请勿点击",Toast.LENGTH_SHORT).show();
                }
                else{
                    tmp = Integer.valueOf(textView.getText().toString());
                    if(tmp > 0) tmp--;
                    else Toast.makeText(this,"时间不能为负值",Toast.LENGTH_SHORT).show();
                    textView.setText(tmp+"");
                }
                break;
            case R.id.start:
                if(button_st.getText().equals("开始定时")){
                    if(Integer.valueOf(textView.getText().toString())>0){
                        button_st.setText("运行中...");
                        int second = 0;
                        if(textView_unit.getText().equals("秒")){
                            second = Integer.valueOf(textView.getText().toString());
                        }
                        else{
                            second = Integer.valueOf(textView.getText().toString())*60;
                        }
                        textView_unit.setText("秒");
                        button_end.setText("强制刷新");
                        timeTickLoad.execute(second);
                    }
                   else{
                        Toast.makeText(this,"时间不能为空哦！",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(button_st.getText().equals("运行中...")){
                    Toast.makeText(this,"正在运行中，请勿打断",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.end:
                if(button_end.getText().equals("闹铃关闭")){
                    rt.stop();
                    button_end.setText("功能按钮");
                }
                else if(button_end.getText().equals("强制刷新")){
                    flag = false;
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
                else if(button_end.getText().equals("功能按钮")){
                    Toast.makeText(this,"开始定时之后我才有功能哦！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    class TimeTickLoad extends AsyncTask<Integer,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String s) {
            Log.i("x","onPostExecute");
            if(flag)
            rt.play();
            textView.setText(0+"");
            button_st.setText("开始定时");
            button_end.setText("闹铃关闭");
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("x","onProgressUpdate");
            textView.setText(values[0] + "");
            super.onProgressUpdate(values[0]);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            //费事操作不能撰写UI操作
            Log.i("x","doInBackground");
            for(int i = integers[0];i>=0;i--){
                if(isCancelled()) break;
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "结束";
        }
    }
}