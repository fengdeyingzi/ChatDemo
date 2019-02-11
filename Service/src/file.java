
import java.io.*;public class file
{
	
	public static void main(String[] args) {

        File file= new File("./test.txt");
		try
		{
			file.createNewFile();
			System.out.println("创建文件成功"+file.getName());
		}
		catch (IOException e)
		{
			System.out.println("创建文件失败");
		}
    }
}
