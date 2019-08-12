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
		//�Զ����ɶ�����,�ǳ־�,��ռ,�Զ�ɾ��
		String replyQueueName = ch.queueDeclare().getQueue();
		//���ɹ���id
		String corrid=UUID.randomUUID().toString().replace("-", "");
		
		//������������:
		//1. �������Ӧ�Ĺ���id
		//2. ������Ӧ���ݵ�queue
		BasicProperties properties=new BasicProperties().builder().correlationId(corrid).replyTo(replyQueueName).build();
		//�� rpc_queue ���з�����������, �����n��쳲�������
		ch.basicPublish("", "rpc_queue", properties, msg.getBytes("UTF-8"));
		
		//��������������������,ȡ����ʱ,û�����ݻ���ͣ�ȴ�	
		BlockingQueue<String> blockingQueue=new ArrayBlockingQueue<>(1);
		
		//������Ӧ���ݵĻص�����
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				//�����Ӧ��Ϣ�Ĺ���id,������Ĺ���id��ͬ,���������������Ӧ����
				if (message.getProperties().getCorrelationId().contentEquals(corrid)) {
					//���յ�����Ӧ����,������������
					blockingQueue.offer(new String(message.getBody(), "UTF-8"));
				}
			}
		};

		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		//��ʼ�Ӷ��н�����Ӧ����
		ch.basicConsume(replyQueueName, true, deliverCallback, cancelCallback);
		//���ر����ڼ����е���Ӧ����
		return blockingQueue.take();

	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		RPCClient client=new RPCClient();
		for(;;){
			System.out.println("��ڼ���쳲�������----");
			String index=new Scanner(System.in).nextLine();
			String result=client.call(index);
			System.out.println("�� "+index+" �� ----- "+result);
		
		}
	}

}
