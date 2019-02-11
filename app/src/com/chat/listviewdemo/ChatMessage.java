package com.chat.listviewdemo;

public class ChatMessage {

	private int type;//指定是哪种类型
	private String value;//值
	private String username;
	private String user;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public void setName(String user)
	{
			this.username=user;
	}
	
	public String getName()
	{
			if(username==null)
					return user;
			return username;
	}
	
	public void setUser(String user)
	{
			this.user=user;
	}
	
	public String getUser()
	{
			return user;
	}
	
}
