package rabbitmq.publicsubscribe.direct;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Consumer {
	public static void main(String[] args) {
		ConnectionFactory cf=new ConnectionFactory();
		cf.setHost("192.168.52.132");
		cf.setPort(5672);
		cf.setUsername("admin");
		cf.setPassword("admin");
		
		try {
			Connection c = cf.newConnection();
			Channel ch = c.createChannel();
			//定义名字为 logs 的交换机, 它的类型是 fanout
			ch.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
			
			//自动生成对列名,
			//非持久,独占,自动删除
			String queueName = ch.queueDeclare().getQueue();
			
			System.out.println("输入接收的日志级别,用空格隔开:");
			String[] a = new Scanner(System.in).nextLine().split("\\s");
			
			//把该队列,绑定到 direct_logs 交换机
			//允许使用多个 bindingKey
			for (String level : a) {
				ch.queueBind(queueName, "direct_logs", level);
			}
			
			System.out.println("等待接收数据");
			
			//收到消息后用来处理消息的回调对象
			DeliverCallback callback = new DeliverCallback() {
				@Override
				public void handle(String consumerTag, Delivery message) throws IOException {
					String msg = new String(message.getBody(), "UTF-8");
					String routingKey = message.getEnvelope().getRoutingKey();
					System.out.println("收到: "+routingKey+" - "+msg);
				}
			};
			
			//消费者取消时的回调对象
			CancelCallback cancel = new CancelCallback() {
				@Override
				public void handle(String consumerTag) throws IOException {
				}
			};
			
			ch.basicConsume(queueName, true, callback, cancel);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
