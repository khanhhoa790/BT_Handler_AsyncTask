package com.example.apphandlerandasynctask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Handler handler;
    private TextView txtNumber;
    private Button btnStart;
    private static final int UP_NUMBER = 10;
    private static final int NUMBER_DONE = 11;
    private boolean isUp;

    private ProgressBar progressBar;
    private Button btnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHandler:
                setContentView(R.layout.handler);
                getViewHandler();
                processHandler();
                break;
            case R.id.menuAsync:
                setContentView(R.layout.asynctask);
                getViewAsync();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getViewHandler(){
        txtNumber = findViewById(R.id.txtNumber);
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
    }

    private void getViewAsync(){
        progressBar = findViewById(R.id.progressBar);
        btnShow = findViewById(R.id.btnShow);
        btnShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Khi click button
        switch (v.getId()){
            case R.id.btnStart:
                if (!isUp)
                    upNumber();
                break;
            case R.id.btnShow:
                new progressAsyncTask();
                break;
            default:
                break;
        }
    }

    /*--------------------------Handler----------------------------*/
    private void processHandler(){
        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case UP_NUMBER:
                        //cap nhat gia tri lên UI
                        isUp = true;
                        //cap nhat UI voi gia tri moi
                        txtNumber.setText(String.valueOf(msg.arg1));
                        break;
                    case NUMBER_DONE:
                        //cap nhat lai giao dien hien thi ket thuc viec cap nhat
                        txtNumber.setText("END!");
                        isUp = false;
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void upNumber(){
        new Thread(new Runnable() {
            @Override
            public void run() {
            //xu ly cap nhat gia tri
            for (int i=0; i<=10; i++){
                Message msg = new Message();
                msg.what = UP_NUMBER;  //gan cong viec - start se cap nhat
                msg.arg1 = i;  //gan tham so
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //gui tin nhan vao message pool
            handler.sendEmptyMessage(NUMBER_DONE);
            }
        }).start();
    }

    /*----------------------------AsyncTask-----------------------------*/
    private class progressAsyncTask extends AsyncTask<Void, Integer, String>{

        @Override
        protected String doInBackground(Void... voids) {
            for(int i=0; i<=100; i++){
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "DONE!";
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
    }
}
