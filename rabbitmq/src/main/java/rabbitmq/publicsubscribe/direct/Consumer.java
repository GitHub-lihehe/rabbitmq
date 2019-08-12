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
			//��������Ϊ logs �Ľ�����, ���������� fanout
			ch.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
			
			//�Զ����ɶ�����,
			//�ǳ־�,��ռ,�Զ�ɾ��
			String queueName = ch.queueDeclare().getQueue();
			
			System.out.println("������յ���־����,�ÿո����:");
			String[] a = new Scanner(System.in).nextLine().split("\\s");
			
			//�Ѹö���,�󶨵� direct_logs ������
			//����ʹ�ö�� bindingKey
			for (String level : a) {
				ch.queueBind(queueName, "direct_logs", level);
			}
			
			System.out.println("�ȴ���������");
			
			//�յ���Ϣ������������Ϣ�Ļص�����
			DeliverCallback callback = new DeliverCallback() {
				@Override
				public void handle(String consumerTag, Delivery message) throws IOException {
					String msg = new String(message.getBody(), "UTF-8");
					String routingKey = message.getEnvelope().getRoutingKey();
					System.out.println("�յ�: "+routingKey+" - "+msg);
				}
			};
			
			//������ȡ��ʱ�Ļص�����
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
