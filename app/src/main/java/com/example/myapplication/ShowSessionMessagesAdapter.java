package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ShowSessionMessagesAdapter extends BaseAdapter{

    private LayoutInflater mInflater;

    private Context mContext = null;

    /** 所有的短信*/

    public static final String SMS_URI_ALL = "content://sms/";

    private static final String GetMessagesByThreadIdTAG = "Getting messages by thread id";

    //存储信息会话中所有来往短信的列表

    List<SMSInfo> infos = new ArrayList<SMSInfo>();

    //ShowSessionMessagesAdapter初始化构造方法

    public ShowSessionMessagesAdapter(Context context) {

        mContext = context;

        mInflater = LayoutInflater.from(mContext);

    }

    public void getSessionMessages(String thread_id){

        Cursor sessionMessagesCursor = null;

        ContentResolver resolver = null;
        try{

            String[] projection = new String[]

                    { "thread_id", "address", "person", "body", "date", "type","read" };
            Uri uri = Uri.parse(SMS_URI_ALL);

            resolver = mContext.getContentResolver();

            sessionMessagesCursor = resolver.query
                    (
                            uri,
                            projection,
                            "thread_id=?",
                            new String[] { thread_id } ,
                            null

                    );
            if (sessionMessagesCursor == null) {
                return;
            }
            if (sessionMessagesCursor.getCount() == 0){
                sessionMessagesCursor.close();
                sessionMessagesCursor = null;
                return;

            }
            int nameColumn = sessionMessagesCursor.getColumnIndex("person");

            int phoneNumberColumn = sessionMessagesCursor.getColumnIndex("address");

            int smsbodyColumn = sessionMessagesCursor.getColumnIndex("body");

            int dateColumn = sessionMessagesCursor.getColumnIndex("date");

            int typeColumn = sessionMessagesCursor.getColumnIndex("type");

            sessionMessagesCursor.moveToFirst();

            while (sessionMessagesCursor.isAfterLast() == false){

                SMSInfo smsinfo = new SMSInfo();

                //将信息会话的信息内容和信息类型（收到或发出）存入infos中

                smsinfo.setSmsbody(sessionMessagesCursor.getString(smsbodyColumn));

                smsinfo.setType(sessionMessagesCursor.getString(typeColumn));

                infos.add(smsinfo);

                sessionMessagesCursor.moveToNext();

            }
            sessionMessagesCursor.close();

            sessionMessagesCursor = null;

        }catch(Exception e){

            Log.e(GetMessagesByThreadIdTAG,"E:" + e.toString());

        }finally{

            if (sessionMessagesCursor != null){
                sessionMessagesCursor.close();
                sessionMessagesCursor = null;
            }

        }
    }
    @Override

    public int getCount() {

        // TODO Auto-generated method stub

        return infos.size();

    }
    @Override
    public Object getItem(int position) {

        // TODO Auto-generated method stub
        return infos.get(position);
    }
    @Override
    public long getItemId(int position) {

        // TODO Auto-generated method stub
        return position;
    }
@Override

    public View getView(int position, View convertView, ViewGroup parent) {
        MessageHolder receivedMessageHolder = null;
        MessageHolder sendMessageHolder = null;



        if (convertView == null) {
            switch(Integer.parseInt(infos.get(position).getType())){
                //若从sms表提取的信息type为1，说明这是收到的信息
                case 1:
                    //为收到的信息关联格式文件，设置显示格式
                    convertView = mInflater.inflate(R.layout.message_session_list_received_item, null);

                    receivedMessageHolder = new MessageHolder();

                    receivedMessageHolder.setTvDesc((TextView) convertView.findViewById(

                            R.id.ReceivedSessionMessageTextView));
                    //将联系人信息载体Holder放入convertView视图

                    convertView.setTag(receivedMessageHolder);

                    break;
                //若从sms表提取的信息type为其他，说明这是发出的信息
                default:
                    //为发出的信息关联格式文件，设置显示格式

                    convertView = mInflater.inflate(R.layout.message_session_list_send_item, null);

                    sendMessageHolder = new MessageHolder();

                    sendMessageHolder.setTvDesc((TextView) convertView.findViewById(
                            R.id.SendSessionMessageTextView));
                    convertView.setTag(sendMessageHolder);
                    break;
            }

        }else{

            //有convertView，按样式，从convertView视图取出联系人信息载体Holder

            switch(Integer.parseInt(infos.get(position).getType()))
            {
                case 1:
                    receivedMessageHolder = (MessageHolder)convertView.getTag();

                    break;
                default:
                    sendMessageHolder = (MessageHolder)convertView.getTag();
                    break;
            }
        }
        //设置资源

        switch(Integer.parseInt(infos.get(position).getType()))
        {
            case 1:
                receivedMessageHolder.getTvDesc().setText(infos.get(position).getSmsbody());
                break;
              default:
                sendMessageHolder.getTvDesc().setText(infos.get(position).getSmsbody());

                break;
        }
        return convertView;

    }

}
