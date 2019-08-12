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
			//��������
			Connection c=cf.newConnection();
			//�����ŵ�
			Channel ch=c.createChannel();
			
			
			/*
			 * ��������,����rabbitmq�д���һ������
			 * ����Ѿ��������ö��У��Ͳ�����ʹ����������������
			 * 
			 * ��������:
			 *   -queue: ��������
			 *   -durable: ���г־û�,true��ʾRabbitMQ����������Դ���
			 *   -exclusive: ����,true��ʾ���ƽ���ǰ���ӿ���
			 *   -autoDelete: �����һ�������߶Ͽ���,�Ƿ�ɾ������
			 *   -arguments: ��������
			 */
			ch.queueDeclare("helloworld", false, false, false, null);
			System.out.println("�ȴ���������");
			//�յ���Ϣ������������Ϣ�Ļص�����
			DeliverCallback deliverCallback=new DeliverCallback() {
				
				@Override
				public void handle(String consumerTag, Delivery message) throws IOException {
					// TODO Auto-generated method stub
					String msg=new String(message.getBody(), "UTF-8");
					System.out.println("�յ�:  "+consumerTag+"  Content:"+msg);
				}
			};
			
			//������ȡ��ʱ�Ļص�����
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
