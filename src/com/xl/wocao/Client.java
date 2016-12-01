package com.xl.wocao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.*;
import java.io.*;
import com.xl.tool.*;
import java.io.*;
import com.chat.listviewdemo.*;
import android.os.*;
import com.xl.game.math.*;



/**
 * Created by hbjycl on 2016/2/21.
 */
public class Client implements Runnable
{
			


		@Override
		public void run()
		{
				isRun=true;
				startup();
		}

		public void setOnReadListener(OnReadListener lis)
		{
				this.listener=lis;
		}
		
		public boolean sendText(String text)
		{
				if(text.length()>255)
				{
						return false;
				}
				this.send=text;
				return true;
		}
		
		
		public Client()
		{
				handler = new Handler()
				{
						public void handleMessage(android.os.Message msg)
						{
								switch(msg.what)
								{
										
										case TEXT_MSG:
												Msg m=(Msg)msg.obj;
												if(listener!=null)
												{
														if(m.getType()==Msg.MSG_TEXT_LEFT)
														listener.onRead(Msg.MSG_TEXT_LEFT,m.getUser(),m.getName(),m.getText());
														else if(m.getType()==Msg.MSG_TEXT_RIGHT)
																listener.onRead(Msg.MSG_TEXT_RIGHT,m.user,m.name,m.text);
														else if(m.getType()==Msg.MSG_TIME)
																listener.onTime(m.text);
														else if(m.getType()==Msg.MSG_DIALOG)
																listener.onDialog(m.text);
														else if(m.getType()==Msg.MSG_TOAST)
																listener.onToast(m.text,0);
														else if(m.getType()==Msg.MSG_NAME)
																listener.onSetUserName(m.getUser(),m.getName());
														else if(m.getType()==Msg.MSG_CLOSE)
														    listener.onClose(m.getText());
												} 
												break;
												
										
								}
						}
				};
		}
		private String send; //待发送的文本
		boolean isRun;
		private OnReadListener listener;
		Handler handler;
		public static final int
		TEXT_MSG=1,
		TEXT_TIME=0,
		TEXT_DIALOG=2,
		TEXT_IMG=3,
		TEXT_URL=5;
		
    private Socket mSocket;
    private BufferedReader br;
    private PrintWriter pw;
    private String user;
		private String name;
    private BufferedReader consoleInput;
    private Random random = new Random();
    private boolean flag =true;
    boolean isServer=true;
    long maxTime=30000;
		long lasttime;
		
		Vector<UserItem> userlist;
		
		class UserItem
		{
				public String user;
				public String name;
				public UserItem(String user,String name)
				{
						this.user=user;
						this.name=name;
				}
		}
		
    public static void main(String[] args) {
        new Client().startup();
    }

    public void startup() {
        try {
						lasttime=System.currentTimeMillis();
			String ip=(isServer == true) ? BaseUrl.host: "127.0.0.1";
            mSocket = new Socket(ip, Str.atoi(BaseUrl.port));
						userlist=new Vector<UserItem>();
            consoleInput = new BufferedReader(new InputStreamReader(System.in));
            br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            pw = new PrintWriter(mSocket.getOutputStream(), true);
            user = "游客" + random.nextInt(100);
						name = user;
            pw.println(Base64.encode(("#enter "+user+" "+user+" "+user) .getBytes("utf-8")));
			//单独建立定时器发送心跳
			new Timer().schedule(new tick(), maxTime/2 , maxTime/2);
            String inputWord;
            ClientThread ct = new ClientThread();
            new Thread(ct).start();
			while (isRun) 
				{
						if(send!=null)
						{
						
						
					send="#msg "+Base64.encode(send.getBytes("utf-8"));
                pw.println(Base64.encode(send.getBytes("utf-8")));
                send=null;
								}
            }

        }
		catch (SocketException e) {
            System.out.println("主机异常断开，请稍后重连！");
						sendToHandler(new Msg(Msg.MSG_CLOSE,null,null,"主机异常断开，请稍候重连"));
				
		}
        catch (IOException e) {
            e.printStackTrace();
		}
		finally {
			if (consoleInput != null)
                try {

                    consoleInput.close();
                }
				catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
		//将byte转换为十六进制
		String  toHex(byte[] bit)
		{
				int i=0;
				int size;
				String text="";
				for(;i<bit.length;i++)
				{
						size=0xff&bit[i];

						if(size<0)size+=256;
						text+=String.format("%x",size);

				}
				return text;
		}
		
		//base64 解密
		private String doBase64(String basetext)
		{
				try
				{
						return new String(Base64.decode(basetext), "utf-8");
				}
				catch (IOException e)
				{}
				return null;
		}
		
		//向系统申请用户名字
		private void getUserName(String user)
		{
				try
				{
						pw.println(Base64.encode(("#get username " + user).getBytes("utf-8")));
				}
				catch (UnsupportedEncodingException e)
				{}
		}
		
		//向handler传送一条消息
		public void sendToHandler(Msg msg)
		{
				Message m=new Message();
				m.what=TEXT_MSG;

				m.obj=msg; //new Msg(Msg.MSG_TEXT_LEFT,useritem.user,useritem.name,new String(Base64.decode(items[2]),"utf-8"));
				handler.sendMessage(m);
				
		}
		

    private void read() throws IOException {
        String readWord;
        while ((readWord = br.readLine()) != null && flag) {
			try {
					String text=new String(Base64.decode(readWord),"utf-8");
					//System.out.println("服务器消息:"+text);
					if(text.equals("#")) //接收到返回信息，更新最后接收时间
					{
							//pw.println(Base64.encode("#".getBytes("utf-8")));
							lasttime=System.currentTimeMillis();
							continue;
					}
					String items[]=text.split(" ");
					if(items.length>1)
					{
							//?msg user text
							if(items[0].equals("?msg"))
							{
									if(items[1].equals(user))
									{
											System.out.println("自己发送的消息>"+name+"："+ new String(Base64.decode(items[2]),"utf-8"));
										  sendToHandler(new Msg(Msg.MSG_TEXT_RIGHT,user,name,new String(Base64.decode(items[2]),"utf-8")));
											
									}
									else
									{
											boolean isSerch=false;
											//从列表中查找用户名字
											for(UserItem useritem:userlist)
											{
													if(useritem.user.equals(items[1]))
													{
															isSerch=true;
															System.out.println("消息>"+useritem.name+"："+ new String(Base64.decode(items[2]),"utf-8"));
															sendToHandler(new Msg(Msg.MSG_TEXT_LEFT,useritem.user,useritem.name,new String(Base64.decode(items[2]),"utf-8")));
															break;
													}
											}
									System.out.println("消息>"+items[1]+"："+ new String(Base64.decode(items[2]),"utf-8"));
									//如果找不到，就向系统申请用户名字
									if(isSerch==false)
									{
											
											getUserName(items[1]);
											userlist.add(new UserItem(items[1],items[1]));
											sendToHandler(new Msg(Msg.MSG_TEXT_LEFT,items[1],items[1],new String(Base64.decode(items[2]),"utf-8")));
									}
									}
							}
							else if(items[0].equals("?name"))
							{
									if(items[1].equals(user)) //自己成功改名
									{
											name= new String(Base64.decode(items[2]),"utf-8");
											System.out.println("成功修改名字："+name);
											sendToHandler(new Msg(Msg.MSG_TIME,user,name,"成功修改名字："+name));
									}
									else //检测所有用户列表
									{
											for(UserItem item:userlist)
											{
													if(item.user.equals(items[1])) //列表中存在该用户
													{
															String oldname=item.name;
															item.name=doBase64(items[2]);
															System.out.println("用户"+oldname+"改名为"+item.name);
															sendToHandler(new Msg(Msg.MSG_TIME,item.user,item.name,"用户“"+oldname+"”改名为“"+item.name+"”"));
													}
													else
													{
															userlist.add(new UserItem(items[1],doBase64( items[2]) ));
													}
													
											}
									}
									//发送给activity
									sendToHandler(new Msg(Msg.MSG_NAME,items[1],doBase64(items[2]),null));
									
							}
							else if(items[0].equals("?time"))
							{
									System.out.println("系统提示>"+new String(Base64.decode(items[1]),"utf-8"));
									sendToHandler(new Msg(Msg.MSG_TIME,null,null,new String(Base64.decode(items[1]),"utf-8")));
							}
							else if(items[0].equals("?url"))
							{
									System.out.println(items[1]+"发来网址>"+new String(Base64.decode(items[2]),"utf-8"));
							}
							else if(items[0].equals("?img"))
							{
									System.out.println(items[1]+"发来图片>"+new String(Base64.decode(items[2]),"utf-8"));
							}
							else if(items[0].equals("?dialog"))
							{
									System.out.println("系统对话框提示>"+new String(Base64.decode(items[1]),"utf-8"));
									sendToHandler(new Msg(Msg.MSG_DIALOG,null,null,new String(Base64.decode(items[1]),"utf-8")));
							}
							else if(items[0].equals("?toast"))
							{
									System.out.println("系统toast>"+new String(Base64.decode(items[1]),"utf-8"));
									sendToHandler(new Msg(Msg.MSG_TOAST,null,null,new String(Base64.decode(items[1]),"utf-8")));
							}
							;
					}
					else
					{
							System.out.println("未知消息>"+text);
					}
					
				
				//System.out.println(toHex(Base64.decode(readWord)));
				
			}
			catch (IOException e) {
				System.out.println(readWord);

			}
		}
    }

    private class ClientThread implements Runnable {

        @Override
        public void run() {
			try {
				read();
			}
			catch (SocketException e) {
				System.out.println("断开了连接");
					sendToHandler(new Msg(Msg.MSG_TIME,null,null,"连接已断开"));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				if (mSocket != null)
					try {
						mSocket.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}
	private class tick extends TimerTask
	{
		private int n=-1;
		@Override
		public void run() 
		{
			// 主动向服务器发送数据，等待服务器返回
			String text= "#";
			try
			{
				if(pw!=null)
				pw.println(Base64.encode(text.getBytes("utf-8")));
			}
			catch (UnsupportedEncodingException e)
			{
					
			}
			
			//检查是否掉线
			if(System.currentTimeMillis()-lasttime>maxTime)
			{
					sendToHandler(new Msg(Msg.MSG_TIME,null,null,"你已掉线"));
					startup();
			}

		}
	}
}
