/*
 
 */
package com.xl.wocao;

import android.app.Dialog;
import android.content.*;
import android.net.*;
import android.os.*;
import android.widget.*;
import com.xl.game.math.*;
import com.xl.game.tool.*;
import java.io.*;
import java.net.*;
import com.xl.tool.*;
import android.support.v7.app.*;
import android.app.Activity;
/*
更新检测+数据解析，执行简单的脚本
toast("内容",0); 弹出toast
dialog("标题","内容","确定键","返回键","确定键url","返回键url");
snakebar("内容","按钮","按钮url");
updateUrl("");//更新链接
zhifuUrl(""); //支付链接
*/

public class Update 
{
    static String uu = "http://xlapp.coding.me/yzjlb/app/wocao/info.html";
    //static String updateurl = "http://xlapp.coding.me/yzjlb/app/wocao/index.html";
    static int version = 8;
		static boolean isCheck=false;
		public static Dialog dlg_pass;
		static OnUpdateListener listen;
		static boolean isUpdate=false; //是否升级，当升级时阻塞解析
		public static boolean isCheck()
		{
				return isCheck;
		}
		
		public static void setCheck(boolean check)
		{
				isCheck=check;
		}
		
		public static void setCheckListener(OnUpdateListener listener)
		{
				listen=listener;
		}
		
		
		
		public static void check(final Context context) 
		{
				if(isCheck)return;
				isUpdate=false;
				final Handler handler =new Handler()
				{
						public void handleMessage(Message message) 
						{
								if(dlg_pass.isShowing())
										dlg_pass.cancel();
								Log.e("","handler回调成功"+message.obj);
								isCheck=true;
								if (message.what == 2 && message.arg1 >= 0 && message.arg1 > Update.version) 
								{
										
								}
								if(message.what==2 && message.arg1>=0)
								{
										//解析数据
										Log.e("","解析数据");
										String head=null;
										String items[]=new String[16];
										int itemsize=0;
										String text="";
										if(message.obj!=null)
												text=(String)message.obj;
										char c=0;
										int type=0;
										int start=0;
										for(int i=0;i<text.length();i++)
										{
												c=text.charAt(i);
												switch(type)
												{
														case 0://判断首字母
														if(c>='a' && c<='z')
														{
																type=1;
																itemsize=0;
																start=i;
														}
														if(c=='/')
														{
																type=5;
														}
														if(c=='\n')
														{
																type=0;
														}
																break;
														case 1://判断左括号
														if(c=='(')
														{
																type=2;
																head=text.substring(start,i);
														}
																if(c=='/')
																{
																		type=5;
																}
																if(c=='\n')
																{
																		type=0;
																}
																
																break;
														case 2://判断引号
														if(c=='\"')
														{
																start=i+1;
																type=3;
														}
																if(c=='/')
																{
																		type=5;
																}
																if(c=='\n')
																{
																		type=0;
																}
																break;
														case 3://判断反引号
														if(c=='\\')
														{
																type=9;
																
														}
														if(c=='\"')
														{
																type=4;
																items[itemsize++]=text.substring(start,i);
														}
																break;
														case 4://判断反括号或逗号
														if(c==')')
														{
																type=6;
														}
														if(c==',')
														{
																type=2;
														}
														if(c=='\n')
																type=0;
														    if(c==';')
																{
																		type=0;
																}
																if(c=='/')
																{
																		type=5;
																}
																if(c=='\n')
																{
																		type=0;
																}
																break;
														case 5://检测注释 检测换行
														if(c=='\n')
														{
																
																type=0;
														}
																break;
														case 6: //解析
																//解析函数
																jiexi(context,head,items);
																type=0;
																break;
														case 9: //跳过一个引号
														type=3;
												}
										}
										
										
										
										
										if(listen!=null)
												listen.onCheckOk();
								}
							else //检测失败
							{
									new AlertDialog.Builder(context)
											.setTitle((CharSequence)"提示")
											.setMessage((CharSequence)"初始化失败，请检查网络连接是否正常，然后稍后再试。\nQQ群：370468001")
											.setPositiveButton((CharSequence)"确定", new DialogInterface.OnClickListener()
											{
													@Override
													public void onClick(DialogInterface dialogInterface, int n) {
															if(context instanceof Activity)
																	((Activity)context).finish();
													}
											})

											
											.setCancelable(false)
											.show();
							}
						}
				};


				Thread thread = new Thread()
				{
						public void run()
						{
								URL url =null;
								InputStream is=null;
								//OutputStream os=null;
								//StringBuffer buffer=null;
								//ByteBuffer buf=ByteBuffer.wrap(new byte[]{});
								Message m=new Message();
								m.what=2;
								try
								{
										url= new URL(uu);
								}
								catch (MalformedURLException e)
								{
										Message m1=new Message();
										
										m1.arg1=-1;
										handler.sendMessage(m1);
								}
								//        打开链接     
								URLConnection urlConnection =null;
								Log.e("","打开链接");
								try
								{
										urlConnection=  url.openConnection();

								}
								catch (IOException e)
								{
										
										m.arg1=-1;
										handler.sendMessage(m);
										return;
								}
								//        获得输入流
								urlConnection.setConnectTimeout(10000);
								urlConnection.setReadTimeout(10000);
								try
								{
										urlConnection.connect();
								}
								catch (IOException e)
								{
										
										m.arg1=-1;
										handler.sendMessage(m);
										return;
								}

								Log.e("", "获得输入流");
								try
								{
										is = urlConnection.getInputStream();

								}
								catch (IOException e)
								{
										m.arg1=-1;
										handler.sendMessage(m);
										return;
								}
								//        定义输出流

								//  buffer=new StringBuffer();

								byte[] buffer = new byte[4048];
								int ptr=0; //偏移
								int length;
								//        开始读入内存-写到磁盘
								Log.e("","开始读取");
								long time=System.currentTimeMillis();
								try
								{
										length = is.read(buffer);
										ptr+=length;
                    /*
										while ( (length = is.read(buffer, ptr, 2048-ptr))>=0) 
										{
												//buf.put(buffer,0,length);
												//os.write(buffer, 0, length);
												//if(buffer[ptr]>='0')
												ptr+=length;
												if(System.currentTimeMillis()-time>10000)
												{
														is.close();
														m.arg1=-1;
														handler.sendMessage(m);
														return;
												}
										}
										*/


										//os.close();
										is.close();
   /*
										byte buffer2[]=new byte[1024];
										//String text= new String(buffer,0,length,"utf-8");
										//System.out.println(text+" "+text.length());
										int j=0;	
										for(int i=0;i<ptr;i++)
										{
												if(buffer[i]>='0')
												{
														buffer2[j++]=buffer[i];
												}
										}
										String text=new String(buffer2,0,j,"utf-8");
										if(text.length()>=1)
										{
												if(text.charAt(0)>='0' && text.charAt(0)<='9')
												{
														m.arg1=Str.atoi(text);
												}
										}
										*/
										m.arg1=0;
										//byte[] buf=new 
										m.obj=new String(buffer,0,ptr,"utf-8");
										handler.sendMessage(m);
								}
								catch(ConnectException e)
								{
										m.arg1=-1;
										handler.sendMessage(m);
										return;
								}
								catch (IOException e)
								{
										m.arg1=-1;
										handler.sendMessage(m);
										return;
								}
								catch(IllegalArgumentException e)
								{
										m.arg1=-1;
										handler.sendMessage(m);
								}

								catch(Exception e)
								{
										Log.e("","未知错误");
										m.arg1=-1;
										handler.sendMessage(m);
								}

						}
				};
				thread.start();
				dlg_pass= ProgressDialog.show(context, null, "正在初始化...");
				Log.e("","初始化");
    }
		
		//解析参数
		static void jiexi(Context context,String head,String items[])
		{
				if(isUpdate)return;
				//解析参数
				if(head.equals("versionCode"))
				{
						int vercode = Str.atoi(items[0]);
						if(vercode>Update.version)
						{
								UpdateDialog(context,BaseUrl.updateUrl);
						}
				}
				else if(head.equals("toast"))
				{
						showToast(context,items[0],Str.atoi(items[1]));
				}
				else if(head.equals("host"))
				{
						BaseUrl.host=items[0];
						BaseUrl.port=items[1];
				}
				else if(head.equals("dialog"))
				{
						showDialog(context,items[0],items[1],items[2],items[3],items[4],items[5]);
				}
				else if(head.equals("snakebar"))
				{
						showToast(context,items[0],1);
				}
				else if(head.equals("updateUrl"))
				{
						setUpdateUrl(items[0]);
				}
				
		}


    //更新
		public static void UpdateDialog(final Context context,final String url)
		{
				isUpdate=true;
				new AlertDialog.Builder(context)
						.setTitle((CharSequence)"更新提醒")
						.setMessage((CharSequence)"检测到有新版本需要更新，是否立即更新？")
						.setPositiveButton((CharSequence)"前往下载", new DialogInterface.OnClickListener()
						{
								@Override
								public void onClick(DialogInterface dialogInterface, int n) {
										Intent intent = new Intent();
										intent.setAction("android.intent.action.VIEW");
										intent.setData(Uri.parse(url));
										try {
												context.startActivity(intent);
												if(context instanceof Activity)
												(((Activity)context)).finish();
												return;
										}
										catch (Exception var6_4)
										{
												Toast.makeText(context, (CharSequence)"请下载网页浏览器", (int)0).show();
												return;
										}
								}
						})

						.setNegativeButton((CharSequence)"取消", new DialogInterface.OnClickListener()
						{

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
										// TODO: Implement this method
										if(context instanceof Activity)
												((Activity)context).finish();
								}


						})
						.setCancelable(false)
						.show();
		}
		
		
		
		public static void setUpdateUrl(String url)
		{
				BaseUrl.updateUrl=url;
		}
		
		//弹出dialog 参数：标题 文本 确定键文字 返回键文字 确定键url 返回键url
		public static  void showDialog(final Context context,String title,String text,String ok,String back,final String okurl,final String backurl)
		{
				AlertDialog.Builder dialog= new AlertDialog.Builder(context)
						.setTitle(title)
						.setMessage(text);
						if(ok!=null && ok.length()!=0)
						dialog.setPositiveButton(ok, new DialogInterface.OnClickListener()
						{
								@Override
								public void onClick(DialogInterface dialogInterface, int n) {
										if(okurl!=null && okurl.length()!=0)
										{
										Intent intent = new Intent();
										intent.setAction("android.intent.action.VIEW");
										intent.setData(Uri.parse(okurl));
										try {
												context.startActivity(intent);
												return;
										}
										catch (Exception var6_4) {
												Toast.makeText(context, "请下载网页浏览器", 0).show();
												return;
										}
										}
								}
						});
            if(back!=null && back.length()!=0)
						dialog.setNegativeButton(back, new DialogInterface.OnClickListener()
						{

								@Override
								public void onClick(DialogInterface p1, int p2)
								{
										// TODO: Implement this method
										if(backurl!=null && backurl.length()!=0)
										{
										Intent intent = new Intent();
										intent.setAction("android.intent.action.VIEW");
										intent.setData(Uri.parse(backurl));
										try {
												context.startActivity(intent);
												return;
										}
										catch (Exception var6_4) {
												Toast.makeText(context, "请下载网页浏览器", 0).show();
												return;
										}
										}
								}


						});
						dialog.setCancelable(false)
						.show();
		}
		
		//弹出toast 参数：toast内容 toast类型
		public static void showToast(Context context,String text,int type)
		{
				Toast.makeText(context,text,type).show();
		}

    

}

