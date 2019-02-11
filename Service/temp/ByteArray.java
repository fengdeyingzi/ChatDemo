
import java.io.*;

public class ByteArray
{
	public byte [] array;
	int ptr; //当前偏移
	//int size; //长度
	int len; //总长度
	
	public ByteArray(int len)
	{
		this.len=len;
		array=new byte[len];
		ptr=0;
		//size=0;
	}
	
	//清空数据
	void clear()
	{
		ptr=0;
		//size=0;
	}
	//添加十六进制字节
	public void appendHex(String hextext)
	{
		int i=0;
		int num=0;
		char text[]=hextext.toCharArray();
		byte retext[]=new byte[text.length];
		int start=0;
		int type=0;
		for(i=0;i<text.length;i++)
		{
			switch(text[i])
			{
				case '0':
					num=num*16+0;
					type++;
					break;
				case '1':
					num=num*16+1;
					type++;
					break;
				case '2':
					num=num*16+2;
					type++;
					break;
				case '3':
					num=num*16+3;
					type++;
					break;
				case '4':
					num=num*16+4;
					type++;
					break;
				case '5':
					num=num*16+5;
					type++;
					break;
				case '6':
					num=num*16+6;
					type++;
					break;
				case '7':
					num=num*16+7;
					type++;
					break;
				case '8':
					num=num*16+8;
					type++;
					break;
				case '9':
					num=num*16+9;
					type++;
					break;
				case 'a':
				case 'A':
					num=num*16+10;
					type++;
					break;
				case 'b':
				case 'B':
					num=num*16+11;
					type++;
					break;
				case 'c':
				case 'C':
					num=num*16+12;
					type++;
					break;
				case 'd':
				case 'D':
					num=num*16+13;
					type++;
					break;
				case 'e':
				case 'E':
					num=num*16+14;
					type++;
					break;
				case 'f':
				case 'F':
					num=num*16+15;
					type++;
					break;
				default:

					break;
			}
			if(type==2)
			{
				type=0;
				array[ptr++]=(byte)num;
				num=0;
			}
	    }
		
		
	}
	
	//判断字符串是否以指定前缀
	public boolean startWitch(byte[] text)
	{
		for(int i=0;i<text.length;i++)
		{
			if(array[i]!=text[i])
				return false;
		}
		return true;
	}
	
	//
	public void append(byte[] buf)
	{
		if(buf==null)
			return;
		for(int i=0;i<buf.length;i++)
		array[ptr+i]=buf[i];
		ptr+=buf.length;
	}
	
	public void append(byte[] buf,int start)
	{
		if(buf==null)
			return;
	    int len=buf.length-start;
		for(int i=0;i<len;i++)
			array[ptr+i]=buf[start+i];
		ptr+=len;
	}
	
	//
	public void append(String text)
	{
		byte[] buf=null;
		try
		{
			buf=text.getBytes("utf-8");
			for(int i=0;i<buf.length;i++)
			{
				array[ptr++]=buf[i];
			}
		}
		catch (UnsupportedEncodingException e)
		{}
		
	}
	
	//输出为byte
	public byte[] getBytes()
	{
		byte [] buf=new byte[ptr];
		System.arraycopy(array,0,buf,0,buf.length);
		return buf;
	}
	
	public byte[] sub(int start)
	{
		byte[] buf=new byte[ptr-start];
		System.arraycopy(array,start,buf,0,buf.length);
		return buf;
	}
	
	public byte[] sub(int start,int end)
	{
		byte[] buf=new byte[end-start];
		System.arraycopy(array,start,buf,0,buf.length);
		return buf;
	}
	
	public String toString()
	{
		try
		{
			return new String(array, 0, ptr, "utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
	}
	
	
}
