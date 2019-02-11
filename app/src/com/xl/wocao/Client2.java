package com.xl.wocao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import com.xl.tool.*;
import java.io.*;
import com.chat.listviewdemo.*;
import android.os.*;
/**
 * Created by hbjycl on 2016/2/21.
 */
public class Client2 implements Runnable
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
	
	public Client2()
	{
		 handler = new Handler()
		{
			public void handleMessage(android.os.Message msg)
			{/*
				switch(msg.what)
				{
					case TEXT_TIME:
						if(listener!=null)
							listener.onRead(TEXT_TIME,(String)msg.obj);
						break;
					case TEXT_MSG:
						if(listener!=null)
							listener.onRead(TEXT_MSG,(String)msg.obj);
						break;
				}*/
			}
		};
	}
	public static final int
	TEXT_MSG=1,
	TEXT_TIME=0;
    private Socket mSocket;
    private BufferedReader br;
    private PrintWriter pw;
    private String name;
    private BufferedReader consoleInput;
    private Random random = new Random();
    private boolean flag =true;
    boolean isServer=true;
	private String send; //待发送的文本
	private boolean isRun;
	private OnReadListener listener;
	Handler handler;
    public static void main(String[] args) {
        new Client2().startup();
    }

    public void startup() {
        try {
			String ip=(isServer == true) ?"119.29.215.145": "127.0.0.1";
            mSocket = new Socket(ip, 5858);
            consoleInput = new BufferedReader(new InputStreamReader(System.in));
            br = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            pw = new PrintWriter(mSocket.getOutputStream(), true);
            name = "游客" + random.nextInt(100);
            pw.println(Base64.encode(name.getBytes()));
            String inputWord;
            ClientThread ct = new ClientThread();
            new Thread(ct).start();
			while(isRun)
			{
				if(send!=null)
				{
					write(send);
					send=null;
				}
			}
			/*
			while ((inputWord = consoleInput.readLine()) != null) 
				{
                pw.println(Base64.encode(inputWord.getBytes()));
                if ("#quit".equalsIgnoreCase(inputWord)) {
                    flag = false;
                    break;
                }
				
            }*/

        }
		catch (SocketException e) {
            System.out.println("主机异常断开，请稍后重连！");
			toast("主机异常断开，请退出软件重新进入");
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

    private void read() throws IOException {
        String readWord;
        while ((readWord = br.readLine()) != null && flag) {
			try {
				String readtext=new String(Base64.decode(readWord),"utf-8");
			
				Message m=new Message();
				m.what=TEXT_MSG;
				m.obj=readtext;
				handler.sendMessage(m);
				//ystem.out.println(new String(Base64.decode(readWord)));
			}
			catch (IOException e) {
				System.out.println(readWord);

			}
		}
    }
	
	private void write(String text)
	{
		try
		{
			pw.println(Base64.encode(text.getBytes("utf-8")));
		}
		catch (UnsupportedEncodingException e)
		{}

	}
	
	public void sendText(String text)
	{
		this.send=text;
	}
	
	public void toast(String text)
	{
		Message m=new Message();
		m.what=TEXT_TIME;
		m.obj=text;
		handler.sendMessage(m);
	}

    private class ClientThread implements Runnable {

        @Override
        public void run() {
			try {
				read();
			}
			catch (SocketException e) {
				System.out.println("断开了连接");
				toast("断开了连接");
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

}
