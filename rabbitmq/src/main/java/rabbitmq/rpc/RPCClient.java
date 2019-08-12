package rabbitmq.rpc;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RPCClient {
	Connection con;
	Channel ch;
	
	public RPCClient() throws Exception {
		ConnectionFactory cf=new ConnectionFactory();
		cf.setHost("192.168.52.132");
		cf.setPort(5672);
		cf.setUsername("admin");
		cf.setPassword("admin");
		con = cf.newConnection();
		ch = con.createChannel();
	}
	
	public String call(String msg) throws Exception {
		//自动生成对列名,非持久,独占,自动删除
		String replyQueueName = ch.queueDeclare().getQueue();
		//生成关联id
		String corrid=UUID.randomUUID().toString().replace("-", "");
		
		//设置两个参数:
		//1. 请求和响应的关联id
		//2. 传递响应数据的queue
		BasicProperties properties=new BasicProperties().builder().correlationId(corrid).replyTo(replyQueueName).build();
		//向 rpc_queue 队列发送请求数据, 请求第n个斐波那契数
		ch.basicPublish("", "rpc_queue", properties, msg.getBytes("UTF-8"));
		
		//用来保存结果的阻塞集合,取数据时,没有数据会暂停等待	
		BlockingQueue<String> blockingQueue=new ArrayBlockingQueue<>(1);
		
		//接收响应数据的回调对象
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				//如果响应消息的关联id,与请求的关联id相同,我们来处理这个响应数据
				if (message.getProperties().getCorrelationId().contentEquals(corrid)) {
					//把收到的响应数据,放入阻塞集合
					blockingQueue.offer(new String(message.getBody(), "UTF-8"));
				}
			}
		};

		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		//开始从队列接收响应数据
		ch.basicConsume(replyQueueName, true, deliverCallback, cancelCallback);
		//返回保存在集合中的响应数据
		return blockingQueue.take();

	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		RPCClient client=new RPCClient();
		for(;;){
			System.out.println("求第几个斐波那契数----");
			String index=new Scanner(System.in).nextLine();
			String result=client.call(index);
			System.out.println("第 "+index+" 个 ----- "+result);
		
		}
	}

}
