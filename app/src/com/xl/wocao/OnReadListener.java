package com.xl.wocao;


/*
聊天记录监听器

*/

public interface OnReadListener
{
	public void onRead(int type,String user,String name,String text);
	
	public void onDialog(String text);
	
	public void onToast(String text,int type);
	
	public void onTime(String text);
	
	public void onImg(int type,String url);
	
	public void onSetUserName(String user,String name);
	
	public void onClose(String text);
	
	
}
