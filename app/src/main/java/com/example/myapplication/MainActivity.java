package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity   {

    private ListView messageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //获取用于显示短信会话列表的ListView控件
        messageListView = (ListView)this.findViewById(R.id.messageListView);
        //新建并为ListView设置自定义适配器，为控件加载需要显示的数据
        MessageListAdapter adapter = new MessageListAdapter(this);
        adapter.getMessageSessions();
        messageListView.setAdapter(adapter);        //实时通知数据已更新
           adapter.notifyDataSetChanged();

/*
        messageListView = (ListView)this.findViewById(R.id.messageListView);
        MessageListAdapter adapter = new MessageListAdapter(this);
        //获取短信记录
        adapter.getSMSInfo();
        messageListView.setAdapter(adapter);
        //实时通知数据已更新
         adapter.notifyDataSetChanged();
*/



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SendActivity.class);
                startActivity(intent);
            }
        });
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
