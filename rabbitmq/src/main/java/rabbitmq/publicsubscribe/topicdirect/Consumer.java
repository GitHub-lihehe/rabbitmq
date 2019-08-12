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
		//��������Ϊ logs �Ľ�����, ���������� fanout
		ch.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);
		
		//�Զ����ɶ�����,
		//�ǳ־�,��ռ,�Զ�ɾ��
		String queue = ch.queueDeclare().getQueue();
	
		System.out.println("������յ���־����,�ÿո����:");
		String[] a=new Scanner(System.in).nextLine().split("\\s");
		
		for(String key:a) {
			ch.queueBind(queue, "topic_logs", key);
		}
		System.out.println("�ȴ���������");
		
		//�յ���Ϣ������������Ϣ�Ļص�����
		DeliverCallback callback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg = new String(message.getBody(), "UTF-8");
				String routingKey = message.getEnvelope().getRoutingKey();
				System.out.println("�յ�: "+routingKey+" - "+msg);
				
				ch.basicAck(message.getEnvelope().getDeliveryTag(), false);
			}
		};
		
		//������ȡ��ʱ�Ļص�����
		CancelCallback cancel = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		ch.basicConsume(queue, false, callback, cancel);
	}

}
