package com.chat.listviewdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;


/**
 * 参考链接：
 * http://android.amberfog.com/?p=296
 * http://www.cnblogs.com/devinzhang/archive/2012/07/02/2573554.html
 * http://stackoverflow.com/questions/4777272/android-listview-with-different-layout-for-each-row
 * 
 * */
public class MainActivity extends Activity {

	private ListView lvData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		lvData = (ListView)findViewById(R.id.lv_data);
		
		lvData.setAdapter(getAdapter());
	}

	private BaseAdapter getAdapter(){
		
		return new ChatAdapter(this, getMyData());
	}
	
	private List<Message> getMyData(){
		
		List<Message> msgList = new ArrayList<Message>();
		Message msg;
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("100");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_TIME_TIP);
		msg.setValue("2012-12-23 下午2:23");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("99");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("98");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_TIME_TIP);
		msg.setValue("2012-12-23 下午2:25");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("97");
		msgList.add(msg);

		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("96");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("95");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_TIME_TIP);
		msg.setValue("2012-12-23 下午3:25");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("94");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
		msg.setValue("93");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("92");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("91");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
		msg.setValue("0");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("1");
		msgList.add(msg);
		
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
		msg.setValue("2");
		msgList.add(msg);
		
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("3");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_AUDIO);
		msg.setValue("4");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("5");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("6");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("7");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("8");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_IMAGE);
		msg.setValue("9");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("10");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("11");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("12");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("13");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("14");
		msgList.add(msg);
		return msgList;
		
	}
	
	
}

