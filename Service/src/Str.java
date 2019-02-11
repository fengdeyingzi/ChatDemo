//package com.xl.game.math;

/*

 字符处理
 */


public class Str
	{
		public static boolean checkCh(String text)
		{
			if(text.getBytes().length==text.length())//这句就是来判断 String是否含有中文字符。
			{
			 return false;
			}
			else
			{
				return true;
			}
		}
		//处理参数列表，生成int数组
		public static	void atoiEx(char[] text,int ptr,int[] per)
			{
				
				int i =0;
				int math = 0;
				for(;ptr<text.length;ptr++)
					{
						switch(text[ptr])
							{
								case '\n':
									per[i]=math;
									math=0;
									i++;
									return;

								//	break;
								case '0':
								case '1': 
								case '2': 
								case '3': 
								case '4': 
								case '5': 
								case '6': 
								case '7': 
								case '8': 
								case '9': 
									math=math*10+(text[ptr]-48);
									break;
								case ',':

									per[i]=math;
									math=0;
									i++;

									break;
								case '-':
									math=-math;
                  break;
								default:
									//per[i]=math;
									//i++;
									//math=0;

							}
						

					}
			}


 //将char[]转换为int
		public static int atoi(char[] text,int ptr)
			{
				
				int math = 0;
				if(text.length==0)return 0;
				//跳过0
				while(text[ptr]=='0')
					{
						ptr++;
					}

				while(ptr<text.length)
					{
						switch(text[ptr])
							{
								case '0': 
								case '1': 
								case '2': 
								case '3': 
								case '4': 
								case '5': 
								case '6': 
								case '7': 
								case '8': 
								case '9': 
									math=math*10+(text[ptr]-48);

									break;
								case ' ':

									break;
								case '-':
									math=-math;
									break;
								default:
									return math;
							}
						ptr++;
					}
				return math;
			}

	public static int atoi(String text)
	{

		int math = 0;
		int ptr=0;
		
		//跳过非数字
	/*	
		while(text.charAt(ptr)>'9' || text.charAt(ptr)<'0')
		{
			ptr++;
		}
*/
		while(ptr<text.length())
		{
			switch(text.charAt(ptr))
			{
				case '0': 
				case '1': 
				case '2': 
				case '3': 
				case '4': 
				case '5': 
				case '6': 
				case '7': 
				case '8': 
				case '9': 
					math=math*10+(text.charAt(ptr)-48);

					break;
				case ' ':

					break;
				case '-':
					math=-math;
					break;
				default:
					return math;
			}
			ptr++;
		}
		return math;
	}
			
			
		//在text中查找str	
		public static int strstr(char text[],int ptr,char str)
			{
				int i;
				
				for(i=ptr;i<text.length;i++)
					{
						if(text[i]==str)
							return i;
					}
				return -1;
			}
		
		//在text中查找text2
		public static int strstr(byte[] text, byte[] text2)
		{
			int type=0;
			
			
			for(int i=0;i<text.length;i++)
			{
				switch(type)
				{
					case 1:
						
					  break;
					case 2:
						break;
				}
				if(text[i]==text2[0])
				{
					//检测是否相同
					for(int t=0;t<text2.length;t++)
					{
						if(text[i+t]!=text2[t])
						break;
						if(t==text2.length-1)
						{
							return i;
						}
					}
				}
				
			}
		return -1;
		}

//从buf所指内存区域的前count个字节查找字符ch，当第一次遇到字符ch时停止查找。
//如果成功，返回指向字符ch的指针；否则返回NULL。
			public static int memchr(char text[],char c)
			{
				int ptr=0;
				while(ptr<text.length)
				{
					if(text[ptr]==c)
					{
						return ptr;
					}
					ptr++;
				}
				return -1;
			}
			
		//查找一个字符在字符串中最后一次出现的位置
		public static int strrchr(char text[],char c)
		{
			int ptr=text.length-1;
			while(ptr>=0)
			{
				if(text[ptr]==c)
				{
					return ptr;
				}
				ptr--;
			}
			
			return -1 ;
		}
		
		
		//将text2以0结尾的内容复制到text
		void strcpy(char text[],int ptr,char text2[],int ptr2)
		{
			int i=0;
			while(i<text2.length)
			{
				if(text2[i]!=0)
				text[ptr+i]=text2[ptr2+i];
			}
			
			
			
		}
		
		//删除ptr处size长度的字符
		public static void strremove(char text[], int ptr, int size)
		{
			int end=ptr+size;
			
			while(end<text.length)
			{
			text[ptr]=text[end];
			ptr++;
			end++;
			}
		}

		//计算char长度
		public static int strlen(char text[])
		{
			int len=0;
			for(len=0;len<text.length;len++)
			{
				if(text[len]==0)
					return len;
			}
			return len;
		}
		
		//比较字符串
		public static int strcmp(char s1[],int ptr1,char s2[],int ptr2)
		{
			int i=0;
			while(ptr2<s2.length)
			{
			if(s1[ptr1]!=s2[ptr2])
				return -1;
			ptr1++;ptr2++;
			}
			return 0;
		}
		
		//比较char[]和string，ptr为char处指针，成功返回true，失败返回false
		public static boolean strcmp(char[] text, int ptr,String str)
		{
			int i=0;
			int j=0;
			//检测字符串是否有足够长
			if(str.length()>text.length-ptr)
			{return false;}
			while(i<str.length())
			{
				if(text[ptr+i]==str.charAt(i))
				{
					i++;
				}
				else
				{
					return false;
				}
			}
		return true;
		}
		
	public static boolean strcmp(String text, int ptr,String str)
	{
		int i=0;
		int j=0;
		//检测字符串是否有足够长
		if(str.length()>text.length()-ptr)
		{return false;}
		while(i<str.length())
		{
			if(text.charAt(ptr+i)==str.charAt(i))
			{
				i++;
			}
			else
			{
				return false;
			}
		}
		return true;
	}
		
		
		
		//在char[]末尾加入字符
		public void strcat(char text[],String str)
		{
			int len=strlen(text);
			for(int i=0;i<str.length();i++)
			{
			text[len+i]=str.charAt(i);
			}
		}
		
		
		
		//测试字符串是否以指定后缀结束
		public static int strhz(char text[],char hz[])
		{
			int ptr=0;
			ptr=strrchr(text,'.');
			if(ptr==-1)return -1;
			return strcmp(text,ptr,hz,0);
				
		}
		
		
		//获取ptr处size长度字符
	public static char[] get(char text[],int ptr,int size)
	{
		char out[]=new char[size];
		for(int i=0;i<size;i++)
		{
			out[i]=text[ptr+i];
		}
		return out;
	}
		
	}
