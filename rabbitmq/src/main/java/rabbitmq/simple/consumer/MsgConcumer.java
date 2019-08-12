package rabbitmq.simple.consumer;

import java.io.IOException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class MsgConcumer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectionFactory cf=new ConnectionFactory();
		cf.setHost("192.168.52.132");
		cf.setVirtualHost("/pd");
		cf.setPort(5672);
		cf.setUsername("admin");
		cf.setPassword("admin");
		
		try {
			//建立连接
			Connection c=cf.newConnection();
			//建立信道
			Channel ch=c.createChannel();
			
			
			/*
			 * 声明队列,会在rabbitmq中创建一个队列
			 * 如果已经创建过该队列，就不能再使用其他参数来创建
			 * 
			 * 参数含义:
			 *   -queue: 队列名称
			 *   -durable: 队列持久化,true表示RabbitMQ重启后队列仍存在
			 *   -exclusive: 排他,true表示限制仅当前连接可用
			 *   -autoDelete: 当最后一个消费者断开后,是否删除队列
			 *   -arguments: 其他参数
			 */
			ch.queueDeclare("helloworld", false, false, false, null);
			System.out.println("等待接收数据");
			//收到消息后用来处理消息的回调对象
			DeliverCallback deliverCallback=new DeliverCallback() {
				
				@Override
				public void handle(String consumerTag, Delivery message) throws IOException {
					// TODO Auto-generated method stub
					String msg=new String(message.getBody(), "UTF-8");
					System.out.println("收到:  "+consumerTag+"  Content:"+msg);
				}
			};
			
			//消费者取消时的回调对象
			CancelCallback cancelCallback=new CancelCallback() {
				
				@Override
				public void handle(String consumerTag) throws IOException {
					// TODO Auto-generated method stub
					System.out.println(" CancelCallback:"+consumerTag);
				}
			};
			
			ch.basicConsume("helloworld", true, deliverCallback, cancelCallback);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
