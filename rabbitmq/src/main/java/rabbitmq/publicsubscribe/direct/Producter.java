package rabbitmq.publicsubscribe.direct;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = {"warning", "info", "error"};
		
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.52.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		try {
			Connection con=f.newConnection();
			Channel c=con.createChannel();
			 
			c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
			
			while(true) {
				System.out.println("输入 direct_logs:....");
				String msg = new Scanner(System.in).nextLine();
				if ("exit".equals(msg)) {
					break;
				}
				
				String routingKey=a[new Random().nextInt(a.length)];
				
				//第一个参数,向指定的交换机发送消息
				//第二个参数,不指定队列,由消费者向交换机绑定队列
				//如果还没有队列绑定到交换器，消息就会丢失，
				//但这对我们来说没有问题;即使没有消费者接收，我们也可以安全地丢弃这些信息。
				c.basicPublish("direct_logs", routingKey, null, msg.getBytes("UTF-8"));
				System.out.println("消息已发送: "+msg);
			}
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
