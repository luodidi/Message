package com.example.myapplication;



import android.os.Bundle;

import android.app.Activity;

import android.content.Intent;

import android.view.Menu;

import android.widget.ListView;

import android.widget.TextView;



public class ShowSessionMessagesActivity extends Activity {

    private TextView sessionMessagesListView;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_session_messages);



        //使用Intent对象得到短信会话的id

        Intent intent = getIntent();

        String threadId = intent.getStringExtra("threadId");
        String info = intent.getStringExtra("info");

        sessionMessagesListView = (TextView)this.findViewById(R.id.SessionMessageListView);
        sessionMessagesListView.setText(threadId);

        TextView tv1= this.findViewById(R.id.tv1);
        tv1.setText(info);
       /* ShowSessionMessagesAdapter sessionMessagesAdapter = new ShowSessionMessagesAdapter(this);

        sessionMessagesAdapter.getSessionMessages(threadId);

        sessionMessagesListView.setAdapter(sessionMessagesAdapter);

        //实时通知数据已更新
        sessionMessagesAdapter.notifyDataSetChanged();*/

    }



    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }



}
