
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerOne implements Runnable
{

	@Override
	public void run()
	{
		isRun=true;
		inThread.start();
		outThread.start();
	}

	
	
	
	private String name; //名字
	private String user; //用户名
	private String pass; //密码
	
	private Socket mSocket;
    private Server server;
	private boolean isRun;
	public long last=System.currentTimeMillis();
    private InThread inThread;
	private OutThread outThread;

	public ServerOne(Server server, Socket s) throws IOException 
	{
		this.mSocket = s;
		this.server=server;
		name=mSocket.getInetAddress().getHostName();
		user="游客"+ System.currentTimeMillis()%10000;
		

		inThread=new InThread(s);
        outThread=new OutThread(s);
      


		//欢迎name加入本聊天室
		/*
		 bytearray.clear();
		 bytearray.appendHex("e6aca2e8bf8e");
		 bytearray.append(name);
		 bytearray.appendHex("e58aa0e585a5e69cace8818ae5a4a9e5aea4efbc81");
		 sendToEveryOne(bytearray.getBytes());
		 //操作说明\n#name 名字 修改显示名称\n#quit 退出聊天室\n");
		 bytearray.clear();
		 bytearray.appendHex("e6938de4bd9ce8afb4e6988e");
		 bytearray.append("\n#name ");
		 bytearray.appendHex("e5908de5ad9720e4bfaee694b9e698bee7a4bae5908de7a7b0");
		 bytearray.append("\n#qult ");
		 bytearray.appendHex("e98080e587bae8818ae5a4a9e5aea40a");

		 sendToOne(bytearray.getBytes());
		 */
	}
	
	
	private class InThread extends Thread
	{
		private BufferedReader in = null;
		public InThread(Socket s)
		{
			try
			{
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			}
			catch (IOException e)
			{
				System.out.println("Socket io错误");
			}
		}
		
		@Override
		public void run() {
			try {
				readIn();
				//leave();
			}
			catch (SocketException e) {
				System.out.println(name + "连接错误");
				//leave("连接出错");
			}
			catch (IOException e) {
				System.out.println(name + "IO错误");
				//leave("已掉线");
			}
			catch (NullPointerException e)
			{
				System.out.println("空指针异常");
				e.printStackTrace();
				//leave("异常");
			}
			finally {
				//leave("未知错误");
				//System.out.println("未知错误");
			}
		}
		
		private byte[] readLine() throws IOException
		{
			//last=System.currentTimeMillis();

			String l=in.readLine();
			if (l == null || l.isEmpty())
				return null;

			return Base64.decode(l);


		}

		//读取用户发来的数据#
		public void readIn() throws IOException {
			byte[] inputWord;

			while ((inputWord = readLine()) != null || isRun)
			{
				last=System.currentTimeMillis();

				String text =new String(inputWord,"utf-8");
				//System.out.println(""+new String(inputWord));
				String items[]=text.split(" ");
				if(items.length>1)
				{
					if(items[0].equals("#enter"))
					{

						if(server.isEnter(items[1]))
						{
							sendToOne("?time "+Base64.encode((user+"重复登录，你被迫下线").getBytes("utf-8")));
							leave("重复登录");
							return;
						}
						else
						{
							user=items[1];
							name=items[1];
							pass=items[2];
						}
						System.out.println(name + "加入了房间");
						sendToOne("?time "+Base64.encode(("欢迎"+name+"加入聊天室\n"+"当前有" + server.getSize() + "人在线\n" + "操作说明\n#name 名字 修改显示名称\n").getBytes("utf-8")));
						sendToOtherOne("?time "+Base64.encode((name+"加入了房间，当前有"+server.getSize()+"人在线").getBytes("utf-8")));
					}
					if(items[0].equals("#name"))//修改名字
					{
						String old=name;
						name=new String(Base64.decode( items[1]));
						if(name.indexOf('\n')>0 || name.length()>16)
						{
							name=old;
							sendToOne("?time "+Base64.encode(("改名失败，名字中不能包含换行符，长度不能大于16个字").getBytes("utf-8")));
						}
						else
							sendToEveryOne("?time "+ Base64.encode( ("用户"+old+"改名为"+name).getBytes("utf-8") ));
					}
					if(items[0].equals("#get"))
					{
						if(items[1].equals("username"))//获取用户名字
						{
							String tempname=null;
							tempname=server.getUserName(items[2]);
							if(tempname!=null)
							{
								sendToOne("?name "+items[2]+" "+Base64.encode(tempname.getBytes("utf-8")));
								
							}

						}
					}
					else if(items[0].equals("#msg"))//消息
					{
						String msgitem[]=new String(Base64.decode( items[1])).split(" ");
						if(msgitem.length>1 && msgitem[0].equals("#name"))
						{
							String old=name;

							name=msgitem[1];
							if(name.indexOf('\n')>0 || name.length()>16)
							{
								name=old;
								sendToOne("?time "+Base64.encode(("改名失败，名字中不能包含换行符，长度不能大于16个字").getBytes("utf-8")));
							}
							else
							{
								sendToEveryOne("?time "+ Base64.encode( ("用户"+old+"改名为"+name).getBytes("utf-8") ));
								sendToEveryOne("?name "+user+" "+Base64.encode( name.getBytes("utf-8")));
							}
						}
						else
							sendToEveryOne("?msg "+  user+" "+ items[1]);
						System.out.println("收到消息："+new String(Base64.decode(items[1]),"utf-8"));
					}
					else if(items[0].equals("#img"))//图片
					{
						sendToEveryOne("?img "+ user+" "+items[1]);
					}
					else if(items[0].equals("url"))
					{
						sendToEveryOne("?url "+ user+" "+items[1]);
					}
					else if(items[0].equals("#exit"))
					{
						leave("离开了本房间");
					}
				}
				else if(text.equals("#"))
				{
					last=System.currentTimeMillis();
					sendToOne("#");
				}

				/*
				 bytearray.clear();
				 bytearray.append(name);
				 bytearray.append(":");
				 bytearray.append(inputWord);
				 System.out.println(bytearray.toString());
				 bytearray.clear();
				 bytearray.append(name);
				 bytearray.append(":");
				 bytearray.append(inputWord);
				 sendToEveryOne(bytearray.getBytes());
				 */
			}
			leave("已下线");

			System.out.println("客户端"+user+" "+name+"结束运行");
		}
		
	}
	
	private class OutThread extends Thread
	{
		private Vector<String> sendlist;
		private PrintWriter out = null;
		public OutThread(Socket s)
		{
			try
			{
				out = new PrintWriter(s.getOutputStream(), true);
			}
			catch (IOException e)
			{
				System.out.println("Socket io错误");
			}
			sendlist=new Vector<String>();
		}
	    
		public void send(String text)
		{
			sendlist.add(text);
		}
		
		public void run()
		{
			while(isRun)
			{
			if(sendlist.size()>0)
			{
				String text=sendlist.get(0);
				try
				{
					if (text != null)
						out.println(Base64.encode(text.getBytes("utf-8")));
				}
				catch (UnsupportedEncodingException e)
				{}
				sendlist.remove(0);
			}
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

	//比较byte是否以指定前缀

	
	
	public String getUser()
	{
		return user;
	}
	
	public String getName()
	{
		return name;
	}
	
	

	//主线程调用，发送数据 参数：用户名 文本(base64)
	public void sendMsg(String user,String text)
	{
		send("?msg "+user+" "+text);
	}
	
    //发送提示文字(base64)
	public void sendTime(String text)
	{
		send("?time "+text);
	}
	
	//发送数据
	public void send(String text)
	{
		outThread.send(text);
	}



	private void sendToOne(String inputWord)
	{
		send(inputWord);
	}
   /*
	private void sendToOne(byte[] inputWord) {
		try {
			PrintWriter  out = new PrintWriter(mSocket.getOutputStream(), true);
			out.println(Base64.encode(inputWord));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
/*
	private void sendToEveryOne(byte[] inputWord) 
	{
		int i=0;
        
		while (i< mServerThreadList.size()) 
		{
			ServerOne st=mServerThreadList.get(i);

			//PrintWriter out = new PrintWriter(st.mSocket.getOutputStream(), true);
			if(st!=null)
				st.out.println(Base64.encode(inputWord));
			i++;

		}
	}
*/
	private void sendToEveryOne(String inputWord) 
	{
		send(inputWord);
		server.sendEveryOne(this,inputWord);
		/*
		for (ServerOne st : mServerThreadList) 
		{

			//PrintWriter out = new PrintWriter(st.mSocket.getOutputStream(), true);
			try
			{
				if (st != null)
					st.out.println(Base64.encode(inputWord.getBytes("utf-8")));
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}


		}*/
	}

	//发送给其它人
	private void sendToOtherOne(String inputWord) 
	{
		server.sendEveryOne(this,inputWord);
		/*
		while (i< mServerThreadList.size()) 
		{
			ServerOne st=mServerThreadList.get(i);
			//PrintWriter out = new PrintWriter(st.mSocket.getOutputStream(), true);
			try
			{
				if (st != null)
					if(st!=this)
						st.out.println(Base64.encode(inputWord.getBytes("utf-8")));
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			i++;

		}
		*/
    }

	/*
	 private void sendToEveryOneHex(String inputWord) 
	 {
	 for (ServerThread st : mServerThreadList) 
	 {
	 bytearray.clear();
	 bytearray.appendHex(inputWord);
	 //PrintWriter out = new PrintWriter(st.mSocket.getOutputStream(), true);
	 st.out.println(Base64.encode(bytearray.getBytes()));


	 }
	 }
	 */

	public void leave(String text) {

        isRun=false;
		try
		{
			mSocket.close();
		}
		catch (IOException e)
		{

		}
		//mServerThreadList.remove(this);
		try
		{
			sendToOtherOne("?time " + Base64.encode((name + text).getBytes("utf-8")));
		}
		catch (UnsupportedEncodingException e)
		{}

		System.out.println(name + text);


	}

	
}
