package com.chat.listviewdemo;

import android.app.*;
import android.os.*;
import android.widget.*;
import java.util.*;
import android.view.View.*;
import android.view.*;


/**
 * 参考链接：
 * http://android.amberfog.com/?p=296
 * http://www.cnblogs.com/devinzhang/archive/2012/07/02/2573554.html
 * http://stackoverflow.com/questions/4777272/android-listview-with-different-layout-for-each-row
 * 
 * */
public class MainActivity extends Activity implements OnReadListener,OnClickListener
{

	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch(p1.getId())
		{
			case R.id.btn_send:
				 String text=edit_msg.getText().toString();
				 service.sendText(text);
				 edit_msg.setText("");
		}
	}
	

	@Override
	public void onRead(int type,String text)
	{
		//Toast.makeText(this,"onRead",0).show();
		switch(type)
		{
			case 0: //time
				addMsg(type,text);
				break;
		    case 1: //text
				addMsg(type,text);
		}
	}
	

	private ListView lvData;
	private EditText edit_msg;
	private Button btn_send;
	List<ChatMessage> msgList = new ArrayList<ChatMessage>();
	ListAdapter adapter ;
	Thread chatthread;
	Client service;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_send= (Button)findViewById(R.id.btn_send);
		edit_msg=(EditText)findViewById(R.id.edit_msg);
		btn_send.setOnClickListener(this);
		lvData = (ListView)findViewById(R.id.lv_data);
		adapter=new ChatAdapter(this,msgList);
		lvData.setAdapter(adapter);
		 service=new Client();
		service.setOnReadListener(this);
		 chatthread=new Thread(service);
		chatthread.start();
		Toast.makeText(this,"风的影子 制作",0).show();
	}
	
	public void addMsg(int type,String text)
	{
		ChatMessage msg;
		msg = new ChatMessage();
		msg.setType(type);
		msg.setValue(text);
		msgList.add(msg);
		adapter = new ChatAdapter(this,msgList);
		lvData.setAdapter(adapter);
	}

	private BaseAdapter getAdapter(){
		
		return new ChatAdapter(this, getMyData());
	}
	
	private List<ChatMessage> getMyData(){
		
		List<ChatMessage> msgList = new ArrayList<ChatMessage>();
		ChatMessage msg;
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("100");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_TIME_TIP);
		msg.setValue("2012-12-23 下午2:23");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("99");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("98\n你在哪");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_TIME_TIP);
		msg.setValue("2012-12-23 下午2:25");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("97\n\n不告诉你😒");
		msgList.add(msg);

		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("96");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("95");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_TIME_TIP);
		msg.setValue("2012-12-23 下午3:25");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("94");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
		msg.setValue("93");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("92");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("91");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
		msg.setValue("0");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("1");
		msgList.add(msg);
		
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
		msg.setValue("2");
		msgList.add(msg);
		
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("3");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_AUDIO);
		msg.setValue("4");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("5");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("6");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("7");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("8");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_RIGHT_IMAGE);
		msg.setValue("9");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("10");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("11");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("12");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("13");
		msgList.add(msg);
		
		msg = new ChatMessage();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("14");
		msgList.add(msg);
		return msgList;
		
	}
	
	
}

