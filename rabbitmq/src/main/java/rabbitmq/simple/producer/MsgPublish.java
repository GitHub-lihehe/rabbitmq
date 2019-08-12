package rabbitmq.simple.producer;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class MsgPublish {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.52.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		try {
			Connection c = f.newConnection();
			Channel ch = c.createChannel();
			
			//�ڶ����������ö��г־û�
			ch.queueDeclare("helloworld", false,false,false,null);
			
			while (true) {
				System.out.print("������Ϣ: ");
				String msg = new Scanner(System.in).nextLine();
				if ("exit".equals(msg)) {
					break;
				}
				
				//����������������Ϣ�־û�
				ch.basicPublish("", "helloworld",null, msg.getBytes("UTF-8"));
				System.out.println("��Ϣ�ѷ���: "+msg);
			}

			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
