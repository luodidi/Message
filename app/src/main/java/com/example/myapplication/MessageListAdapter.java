package com.example.myapplication;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
public class MessageListAdapter extends BaseAdapter {
    /**
     * 所有的短信
     */
    public static final String SMS_URI_ALL = "content://sms/";
    /**
     * 收件箱短信
     */
    public static final String SMS_URI_INBOX = "content://sms/inbox";
    /**
     * 已发送短信
     */
    public static final String SMS_URI_SEND = "content://sms/sent";
    /**
     * 草稿箱短信
     */
    public static final String SMS_URI_DRAFT = "content://sms/draft";
    private static final String MesTAG = "Reading Messages";
    private static final String ThreadTAG = "Reading from Thread Table";
    private static final String GetPhoneNumberTAG = "Getting phone number";
    private static final String GetContactByPhoneTAG = "Getting contact by phone number";
    List<SMSInfo> infos = new ArrayList<SMSInfo>();
    private LayoutInflater mInflater;
    //存储所有短信信息的列表
    private Context mContext = null;
    //MessageListAdapter初始化构造方法
    public MessageListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }
    /**
     * 从mms数据库中检索threads表
     */
    public void getMessageSessions() {
        Cursor sessionCursor = null;
        ContentResolver resolver = null;
        ContactData contact = null;
        try {

            Uri uri = Uri.parse(SMS_URI_ALL);

            resolver = mContext.getContentResolver();
            sessionCursor = resolver.query(uri, new String[]
                    {"* from threads--"}, null, null, null);
            if (sessionCursor == null) {
                return;
            }
            if (sessionCursor.getCount() == 0) {
                sessionCursor.close();
                sessionCursor = null;
                return;
            }
            sessionCursor.moveToFirst();
            while (sessionCursor.isAfterLast() == false) {  /*

                threads表各字段含义如下：
                1._id为会话id，他关联到sms表中的thread_id字段。
                2.recipient_ids为联系人ID,这个ID不是联系人表中的_id,而是指向表canonical_address里的id,
                canonical_address这个表同样位于mmssms.db,它映射了recipient_ids到一个电话号码,也就是说,
                最终获取联系人信息,还是得通过电话号码;
                3.mesage_count该会话的消息数量
                4.snippet为最后收到或发出的信息

                */
                int thread_idColumn = sessionCursor.getColumnIndex("_id");
                int dateColumn = sessionCursor.getColumnIndex("date");
                int message_countColumn = sessionCursor.getColumnIndex("message_count");
                int snippetColumn = sessionCursor.getColumnIndex("snippet");
                int typeColumn = sessionCursor.getColumnIndex("type");
                //格式化短信日期显示
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(Long.parseLong(sessionCursor.getString(dateColumn)));
                //String time = dateFormat.format(date)
                /*
                 *  获取短信的各项内容
                 *  phoneAndUnread[0]存放电话号码，phoneAndUnread[1]存放该会话中未读信息数*/
                //根据短信会话的threadId检索sms表，获取该短信会话人号码
                String threadId = sessionCursor.getString(thread_idColumn);
                String[] phoneAndUnread = getPhoneNum(threadId);
                String last_mms = sessionCursor.getString(snippetColumn);
                // String date_mms=dateFormat.format(date);
                String date_mms = date.toString();
                String count_mms = sessionCursor.getString(message_countColumn);
                String type = sessionCursor.getString(typeColumn);
                SMSInfo smsinfo = new SMSInfo();
                /*
                 * phoneAndUnread[0]存放电话号码
                 * 根据短信会话人号码查询手机联系人，获取会话人的名称和头像资料
                 */
                contact = getContactFromPhoneNum(mContext, phoneAndUnread[0]);
                //获得最后的未读短信与已读短信
                //String final_count="("+phoneAndUnread[1]+"/"+count_mms+")";
                //将会话信息添加到信息列表中
                //判断是否联系人，若为联系人显示其名称，若不是则显示号码
                if (contact.getContactName().equals("")) {
                    smsinfo.setContactMes(phoneAndUnread[0]);
                } else {
                    smsinfo.setContactMes(contact.getContactName());
                }
                //如果有该信息会话人头像，则设置已有头像，如果没有则给他设置一个默认的头像

  /*
                if (contact.getPhotoUri() == null){

                    smsinfo.setContactPhoto(BitmapFactory.decodeResource(

                            mContext.getResources(), R.drawable.ic_launcher_background));

                }else{

                    //Uri photoUri = Uri.parse(contact[1]);

                    Uri photoUri = contact.getPhotoUri();

                    InputStream input = ContactsContract.Contacts.

                            openContactPhotoInputStream(resolver, photoUri);
                    smsinfo.setContactPhoto(BitmapFactory.decodeStream(input));
                }
*/
                if(count_mms.equals("0")){
                    sessionCursor.moveToNext();
                    continue;
                }
                System.out.println(count_mms+"===============");
                smsinfo.setDate(date_mms);
                smsinfo.setSmsbody(last_mms);
                smsinfo.setType(type);
                smsinfo.setMessageCout(count_mms);
                infos.add(smsinfo);
                sessionCursor.moveToNext();
            }
            sessionCursor.close();
        } catch (Exception e) {
            Log.e(ThreadTAG, "E:" + e.toString());
        } finally {

            if (sessionCursor != null) {

                sessionCursor.close();

                sessionCursor = null;
            }
        }
    }


    /**
     * @param 根据thread_id 检索sms库， 获得对应的号码以及该号码的未读信息数
     * @return
     */
    public String[] getPhoneNum(String thread_id) {
        Cursor cursor = null;
        String PhoneNum = "";
        int noread_mms = 0;

        String[] phoneAndUnread = {"", ""};
        try {

            String[] projection = new String[]

                    {"thread_id", "address", "person", "body", "date", "type", "read"};


            //SMS_URI_ALL = "content://sms/";

            Uri uri = Uri.parse(SMS_URI_ALL);

            ContentResolver resolver = mContext.getContentResolver();
            cursor = resolver.query

                    (

                            uri,

                            projection,

                            "thread_id=?",

                            new String[]{thread_id},

                            null

                    );


            //计算该会话包含的未读短信数

            while (cursor.moveToNext()) {

                int phoneNumber = cursor.getColumnIndex("address");

                int isread = cursor.getColumnIndex("read");


                //sms表的read字段为0，表示该短信为未读短信

                if (cursor.getString(isread).equals("0")) {

                    noread_mms++;

                }


                PhoneNum = cursor.getString(phoneNumber);

            }

            phoneAndUnread[0] = PhoneNum;

            phoneAndUnread[1] = Integer.toString(noread_mms);


            cursor.close();

            cursor = null;

        } catch (Exception e) {

            Log.e(GetPhoneNumberTAG, "E:" + e.toString());

        } finally {

            if (cursor != null) {

                cursor.close();

                cursor = null;

            }

        }

        return phoneAndUnread;

    }


    //根据联系人号码从通讯录中获取联系人信息，包括名称和头像uri

    public ContactData getContactFromPhoneNum(Context context, String phoneNum) {

        String phone = phoneNum;

        ContactData contact = new ContactData();

        contact.setContactName("");

        //  contact.setPhotoUri(null);

        ContentResolver resolver = null;

        Cursor cursor = null;

        String contactName;

        //   Long photoId;

        Long contactId;
        try {

            resolver = mContext.getContentResolver();
            //根据电话号码号码查询联系人数据库，获取对应的联系人资料
            cursor = resolver.query(
                    Phone.CONTENT_URI,
                    null, Phone.NUMBER + " = ?",
                    new String[]{phone}, null);

            //如果查询结果为空，说明该短信会话人并非联系人，直接返回空联系人对象

            if (cursor == null) {

                return contact;

            }

            if (cursor.getCount() == 0) {

                cursor.close();

                cursor = null;

                return contact;

            }


            //若查询成功，说明该短信会话人是手机联系人，返回联系人号码和头像资料

            if (cursor.moveToFirst()) {

                contactName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));

                //    photoId = (Long) cursor.getLong(cursor.getColumnIndex(Phone.PHOTO_ID));

                contactId = cursor.getLong(cursor.getColumnIndex(Phone.CONTACT_ID));


                //photoId 大于0 表示联系人有头像
/*
                if(photoId > 0 ) {

                    contact.setPhotoUri(ContentUris.withAppendedId(

                            ContactsContract.Contacts.CONTENT_URI,contactId));

                }

*/

                contact.setContactName(contactName);

                cursor.close();

                cursor = null;

                return contact;

            }


        } catch (Exception e) {

            Log.e(GetContactByPhoneTAG, "E:" + e.toString());

        } finally {

            if (cursor != null) {

                cursor.close();

                cursor = null;

            }

        }

        return contact;

    }


    public Object getItem(int arg0) {

        // TODO Auto-generated method stub

        return arg0;

    }


    public long getItemId(int position) {

        // TODO Auto-generated method stub

        return position;

    }


    public int getCount() {

        // TODO Auto-generated method stub

        return infos.size();

    }


    public View getView(final int position, View convertView, android.view.ViewGroup parent) {

        MessageHolder messageHolder = null;

        //判断convertView是否已创建，若已存在则不必重新创建新视图，节省系统资源

        if (convertView == null) {

            // 和item_custom.xml脚本关联

            convertView = mInflater.inflate(R.layout.list_item, null);

            messageHolder = new MessageHolder();

            //加载控件到信息载体中

            //  messageHolder.setIvImage((ImageView) convertView.findViewById(R.id.index_image));

            messageHolder.setTvTitle((TextView) convertView.findViewById(R.id.titleTextView));

            messageHolder.setTvDesc((TextView) convertView.findViewById(R.id.descTextView));

            messageHolder.setTvCount((TextView) convertView.findViewById(R.id.countTextView));

            messageHolder.setTvTime((TextView) convertView.findViewById(R.id.timeTextView));
            //将联系人信息载体Holder放入convertView视图

            convertView.setTag(messageHolder);

        } else {

            //从convertView视图取出联系人信息载体Holder

            messageHolder = (MessageHolder) convertView.getTag();

        }

        // 通过短信Holder设置item中4个TextView的文本与联系人头像

        //绘制短信联系人信息，内容为其名称或号码

        messageHolder.getTvTitle().setText(infos.get(position).getContactMes());

        //在信息正文区域绘制信息内容

        messageHolder.getTvDesc().setText(infos.get(position).getSmsbody());

        messageHolder.getTvCount().setText("" + infos.get(position).getMessageCout());

        messageHolder.getTvTime().setText(infos.get(position).getDate());

        //    messageHolder.getIvImage().setImageBitmap(infos.get(position).getContactPhoto());

        /*

         * 在短信主界面为每个短信会话设置监听事件，当选择点击某条会话时，跳转到显示该会话包含的所有信息记录的页面

         * */

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent();

                //通过Intent向显示短信会话包含的信息的Activity传递会话id

                intent.putExtra("threadId", infos.get(position).getSmsbody());
                intent.putExtra("info",infos.get(position).getContactMes());
                String str = "短信："+infos.get(position).getSmsbody()+"  "+infos.get(position).getContactMes();
//                System.out.println(str+"===========================");
                intent.setClass(mContext, ShowSessionMessagesActivity.class);

                mContext.startActivity(intent);

            }

        });

        return convertView;

    }

}

