package rabbitmq.publicsubscribe.topicdirect;

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

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ConnectionFactory cf=new ConnectionFactory();
		cf.setHost("192.168.52.132");
		cf.setPort(5672);
		cf.setUsername("admin");
		cf.setPassword("admin");
		
		Connection c = cf.newConnection();
		Channel ch = c.createChannel();
		//定义名字为 logs 的交换机, 它的类型是 fanout
		ch.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);
		
		//自动生成对列名,
		//非持久,独占,自动删除
		String queue = ch.queueDeclare().getQueue();
	
		System.out.println("输入接收的日志级别,用空格隔开:");
		String[] a=new Scanner(System.in).nextLine().split("\\s");
		
		for(String key:a) {
			ch.queueBind(queue, "topic_logs", key);
		}
		System.out.println("等待接收数据");
		
		//收到消息后用来处理消息的回调对象
		DeliverCallback callback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg = new String(message.getBody(), "UTF-8");
				String routingKey = message.getEnvelope().getRoutingKey();
				System.out.println("收到: "+routingKey+" - "+msg);
				
				ch.basicAck(message.getEnvelope().getDeliveryTag(), false);
			}
		};
		
		//消费者取消时的回调对象
		CancelCallback cancel = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		ch.basicConsume(queue, false, callback, cancel);
	}

}
