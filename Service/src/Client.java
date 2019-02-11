import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.*;
import java.io.*;
/**
 * Created by hbjycl on 2016/2/21.
 */
public class Client {
    private Socket mSocket;
    private BufferedReader br;
    private PrintWriter pw;
    private String user;
		private String name;
    private BufferedReader consoleInput;
    private Random random = new Random();
    private boolean flag =true;
    boolean isServer=false;
    long maxTime=30000;
		
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
			String ip=(isServer == true) ?"123.206.45.47": "127.0.0.1";
            mSocket = new Socket(ip, 5858);
						userlist=new Vector<UserItem>();
            consoleInput = new BufferedReader(new InputStreamReader(System.in));
            br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            pw = new PrintWriter(mSocket.getOutputStream(), true);
            user = "游客" + random.nextInt(10000);
						name = user;
            pw.println(Base64.encode(("#enter "+user+" "+user+" "+user) .getBytes("utf-8")));
			//单独建立定时器发送心跳
			new Timer().schedule(new tick(), maxTime , maxTime);
            String inputWord;
            ClientThread ct = new ClientThread();
            new Thread(ct).start();
			while ((inputWord = consoleInput.readLine()) != null) 
				{
						String items[]=inputWord.split(" ");
						
					inputWord="#msg "+Base64.encode(inputWord.getBytes("utf-8"));
                pw.println(Base64.encode(inputWord.getBytes("utf-8")));
                if ("#quit".equalsIgnoreCase(inputWord)) {
                    flag = false;
                    break;
                }
            }

        }
		catch (SocketException e) {
            System.out.println("主机异常断开，请稍后重连！");
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
		

    private void read() throws IOException {
        String readWord;
        while ((readWord = br.readLine()) != null && flag) {
			try {
					String text=new String(Base64.decode(readWord),"utf-8");
					//System.out.println("服务器消息:"+text);
					if(text.equals("#"))
					{
							pw.println(Base64.encode("#".getBytes("utf-8")));
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
															break;
													}
											}
									System.out.println("消息>"+items[1]+"："+ new String(Base64.decode(items[2]),"utf-8"));
									//如果找不到，就向系统申请用户名字
									if(isSerch==false)
									{
											
									}
									}
							}
							else if(items[0].equals("?name"))
							{
									if(items[1].equals(user))
									{
											name= new String(Base64.decode(items[2]),"utf-8");
											System.out.println("成功修改名字："+name);
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
													}
													else
													{
															userlist.add(new UserItem(items[1],doBase64( items[2]) ));
													}
											}
									}
									
							}
							else if(items[0].equals("?time"))
							{
									System.out.println("系统提示>"+new String(Base64.decode(items[1]),"utf-8"));
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
							}
							else if(items[0].equals("?toast"))
							{
									System.out.println("系统toast>"+new String(Base64.decode(items[1]),"utf-8"));
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
			// TODO: Implement this method
			String text= "#";
			try
			{
				if(pw!=null)
				pw.println(Base64.encode(text.getBytes("utf-8")));
			}
			catch (UnsupportedEncodingException e)
			{}

		}
	}
}
