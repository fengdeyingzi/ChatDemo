import java.util.*;
public class time
{
    public static void main(String[] args)
	{
        int year=0;
        int month=0;
        int day=0;
        Calendar c=Calendar.getInstance();//获得系统当前日期
        year=c.get(Calendar.YEAR);
        month=c.get(Calendar.MONTH)+1;//系统日期从0开始算起
        day=c.get(Calendar.DAY_OF_MONTH);
		System.out.println(String.format("%d_%02d_%02d", year,month,day));
	}
}
