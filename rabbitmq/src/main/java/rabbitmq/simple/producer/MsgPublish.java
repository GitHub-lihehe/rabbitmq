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
			
			//第二个参数设置队列持久化
			ch.queueDeclare("helloworld", false,false,false,null);
			
			while (true) {
				System.out.print("输入消息: ");
				String msg = new Scanner(System.in).nextLine();
				if ("exit".equals(msg)) {
					break;
				}
				
				//第三个参数设置消息持久化
				ch.basicPublish("", "helloworld",null, msg.getBytes("UTF-8"));
				System.out.println("消息已发送: "+msg);
			}

			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
