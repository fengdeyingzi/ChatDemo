package com.xl.wocao;
/*
消息传送封装

*/
public class Msg
{
		int type; //消息类型，取值如下
		public static final int
		MSG_TEXT_LEFT=0,
		MSG_TEXT_RIGHT=1,
		MSG_DIALOG=2,
		MSG_TIME=3,
		MSG_TOAST=4,
		MSG_NAME=5,
		MSG_CLOSE=6
		;
		String user; //发送者帐号
		String name; //发送者名字
		String text; //发送内容
		public Msg(int type,String user,String name,String text)
		{
				this.type=type;
				this.user=user;
				this.name=name;
				this.text=text;
		}
		
		public int getType()
		{
				return type;
		}
		
		public String getUser()
		{
				return user;
		}
		
		public String getName()
		{
				return name;
		}
		
		public String getText()
		{
				return text;
		}
}
