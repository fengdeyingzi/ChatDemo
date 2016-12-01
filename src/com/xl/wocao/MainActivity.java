package com.xl.wocao;

import android.app.*;
import android.os.*;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.BaseAdapter;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import java.util.*;
import android.view.View.*;
import android.view.*;
import com.chat.listviewdemo.*;
import android.content.*;


/**
 * 参考链接：
 * http://android.amberfog.com/?p=296
 * http://www.cnblogs.com/devinzhang/archive/2012/07/02/2573554.html
 * http://stackoverflow.com/questions/4777272/android-listview-with-different-layout-for-each-row
 * 
 * */
public class MainActivity extends BaseActivity implements OnReadListener,OnClickListener
{

		@Override
		public void onCheckOk()
		{
				service=new Client();
				service.setOnReadListener(this);
				chatthread=new Thread(service);
				chatthread.start();
				
		}
		
		public void ServerStop()
		{
				service.stop();
		}
		@Override
		public void onClose(String text)
		{
				// 
				new AlertDialog.Builder(this)
						.setTitle((CharSequence)"提示")
						.setMessage(text)
						.setPositiveButton((CharSequence)"确定", new DialogInterface.OnClickListener()
						{
								@Override
								public void onClick(DialogInterface dialogInterface, int n) 
								{
                  
								}
						})
            .setNeutralButton("取消",new DialogInterface.OnClickListener()
						{

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
										
								}
								
								
						})

						.setCancelable(false)
						.show();
		}


		@Override
		public void onSetUserName(String user, String name)
		{
				setUserName(user,name);
		}


		@Override
		public void onRead(int type, String user, String name, String text)
		{
				// TODO: Implement this method
				if(type==Msg.MSG_TEXT_LEFT)
				{
						addMsg(ChatAdapter.VALUE_LEFT_TEXT,user,name,text);
				}
				else if(type==Msg.MSG_TEXT_RIGHT)
				{
						addMsg(ChatAdapter.VALUE_RIGHT_TEXT,user,name,text);
				}
				//Toast.makeText(this,text,0).show();
		}

		@Override
		public void onDialog(String text)
		{
				// TODO: Implement this method
				new AlertDialog.Builder(this)
						.setTitle((CharSequence)"提示")
						.setMessage(text)
						.setPositiveButton((CharSequence)"确定", new DialogInterface.OnClickListener()
						{
								@Override
								public void onClick(DialogInterface dialogInterface, int n) {
										
								}
						})


						.setCancelable(false)
						.show();
		}

		@Override
		public void onToast(String text, int type)
		{
				Toast.makeText(this,text,type).show();
		}

		@Override
		public void onTime(String text)
		{
				addMsg(ChatAdapter.VALUE_TIME_TIP,null,null,text);
		}

		@Override
		public void onImg(int type, String url)
		{
				// TODO: Implement this method
		}


	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		switch(p1.getId())
		{
			case R.id.btn_send:
				 String text=edit_msg.getText().toString();
				 if(!service.sendText(text))
				 {
						 Toast.makeText(MainActivity.this,"消息发送失败",0).show();
				 }
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
				addMsg(type,null,null,text);
				break;
		    case 1: //text
				addMsg(type,null,null,text);
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
		//super.onCreate(savedInstanceState);
			//supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
			// TODO: Implement this method
			super.onCreate(savedInstanceState);
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			
			setContentView(R.layout.activity_main);
   
			// 设置ToolBar
			
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			// Title
			//toolbar.setTitle("神力云免流支付系统");
      
			if (toolbar != null) {
					setSupportActionBar(toolbar);
					if(Build.VERSION.SDK_INT>=11)
							getSupportActionBar().setDisplayHomeAsUpEnabled(true);
					
			}
			
			
		btn_send= (Button)findViewById(R.id.btn_send);
		edit_msg=(EditText)findViewById(R.id.edit_msg);
		btn_send.setOnClickListener(this);
		lvData = (ListView)findViewById(R.id.lv_data);
		adapter=new ChatAdapter(this,msgList);
		lvData.setAdapter(adapter);
		 		Toast.makeText(this,"风的影子 制作",0).show();
		
	}
	
	public void addMsg(int type,String user,String name,String text)
	{
		ChatMessage msg;
		msg = new ChatMessage();
		msg.setType(type);
		msg.setValue(text);
		msg.setUser(user);
		msg.setName(name);
		msgList.add(msg);
		adapter = new ChatAdapter(this,msgList);
		lvData.setAdapter(adapter);
	}
	
	public void setUserName(String user,String username)
	{
			//设置用户名
			for(ChatMessage m: msgList)
			{
					if(m.getUser()==null)
							continue;
					if(m.getUser().equals(user))
					{
							m.setName(username);
					}
			}
			//刷新列表
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

	@Override
	protected void onDestroy()
	{
			// TODO: Implement this method
			Update.isCheck=false;
			ServerStop();
			super.onDestroy();
	}
	
	
}

