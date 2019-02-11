import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.nio.*;
import java.util.*;

/**
 * Created by hbjycl on 2016/2/21.
 //客户端发送消息
 #msg 消息内容(base64)
 #name 名字(base64)
 #exit 退出理由(base64)
 #img 图片链接(base64)
 #xml xml信息内容(base64)
 获取用户名字
 #get username user
 获取用户等级
 #get userleve user
 
 //服务端发送消息
 ?msg 用户 消息内容(base64)
 ?name 用户 名字(base64)
 ?exit 用户 用户名(base64)
 ?img 用户 图片链接(base64)
 ?dialog 内容(base64)
 ?toast 内容(base64) 类型
 ?time 服务器提示文字(base64)
 
 */
public class Server {
    private Vector<ServerOne> mServerThreadList=new Vector<ServerOne>();;
    private ServerSocket mServerSocket;
    private boolean isRun;
	//检查超时
	private int maxTime=120000;
	
    public static void main(String[] args) {
		
        new Server().startup();
    }
	
	//获取在线人数
	public int getSize()
	{
		return mServerThreadList.size();
	}
	
	//发送全局消息给所有人(不包括请求者)
	public void sendEveryOne(ServerOne ser,String text)
	{
		int i=0;
		for(i=0;i<mServerThreadList.size();i++)
		{
			ServerOne s=mServerThreadList.get(i);
			if(s!=ser)
			s.send(text);
		}
	}
	
	//判断指定用户名是否在线
	public boolean isEnter(String user)
	{
		int i=0;
		for(i=0;i<mServerThreadList.size();i++ )
		{
			ServerOne ss=mServerThreadList.get(i);
			if(ss.getUser().equals(user)) //重复登录
			{
				
				
				return true;
			}
		}
		return false;
	}
	
	//获取用户的名字
	String getUserName(String user)
	{
		int i=0;
		for(i=0;i<mServerThreadList.size();i++)
		{
			ServerOne ss=mServerThreadList.get(i);
			if(ss.getUser().equals(user))
			{
				return ss.getName();
			}
		}
		return null;
	}
	

	//启动服务器
    public void startup() {
        //System.out.println("服务准备");
		isRun=true;
		
		try {
            try {
				mServerSocket = new ServerSocket(5858);
			    
				System.out.println("服务开始");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			new Timer().schedule(new tick(), maxTime / 10, maxTime / 10);
			
            while (isRun) {
				Socket s = mServerSocket.accept();
				//ip排重
				/*
				for(ServerThread l: mServerThreadList)
				{
					if(s.getInetAddress().getHostName().equals( l.ip))
					{
						s.close();
					}
				}
				*/
				if(mServerThreadList.size()>100)
				{
					s.close();
					//System.out.println("用户数量过高或已被攻击");
					continue;
				}
				
				try {
					ServerOne st = new ServerOne(this,s);
					st.run();
					mServerThreadList.add(st);
					}
				catch (IOException e) {
					//break;
				}
            }
        }
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
                if (mServerSocket != null)
                    mServerSocket.close();
            }
			catch (IOException e) {
                e.printStackTrace();
            }
        }
		
		
	}


    

    //定时器循环检测
	private class tick extends TimerTask {
		private int n=-1;
		@Override
		public void run() 
		{
			// TODO: Implement this method
			if (n != mServerThreadList.size())
			{
				n = mServerThreadList.size();
				
				
				System.out.println("当前有" + n + "人在线");
				
				
			}
			int i=0;
			while (i<mServerThreadList.size()) 
			{
				//发送一条消息询问
				ServerOne ss=mServerThreadList.get(i);
				
				if(ss!=null)
				{
					String inputWord="";
					if (System.currentTimeMillis() - ss.last > maxTime)
					{
						System.out.println(ss.getUser()+" "+ss.getName()+ "长时间无响应");
						ss.leave("已掉线");
						mServerThreadList.remove(ss);
						
						//ss.leave("长时间无响应，已被系统请离房间");
					}
					else
					{
							i++;
					}
					//不应该由服务器发心跳，而是客户端主动发
					/*
					try
					{
						ss.out.println( Base64.encode(("#" + inputWord).getBytes("utf-8")));

					}
					catch (UnsupportedEncodingException e)
					{}
					
					*/
				}
				else
				{
						i++;
					//mServerThreadList.remove(ss);
				}
				
			}
		}
	}
}
