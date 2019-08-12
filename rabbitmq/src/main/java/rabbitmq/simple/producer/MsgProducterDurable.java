package rabbitmq.simple.producer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class MsgProducterDurable {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//�������ӹ���,������������Ϣ
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.52.132");
		f.setPort(5672);//��ѡ,5672��Ĭ�϶˿�
		f.setUsername("admin");
		f.setPassword("admin");
		
		/*
		 * ��rabbitmq��������������,
		 * rabbitmq��������ʹ�õ���nio,�Ḵ��tcp����,
		 * �����ٶ���ŵ���ͻ���ͨ��
		 * �Լ���������˽������ӵĿ���
		 */
		try {
			Connection c = f.newConnection();
			Channel ch = c.createChannel();
			
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
			ch.queueDeclare("helloworld", true,false,false,null);
			
			
			/*
			 * ������Ϣ
			 * �������Ϣ��Ĭ�Ͻ���������.
			 * Ĭ�Ͻ��������������ж��а�,routing key��Ϊ��������
			 * 
			 * ��������:
			 * 	-exchange: ����������,�մ���ʾĬ�Ͻ�����"(AMQP default)",������ null 
			 * 	-routingKey: ����Ĭ�Ͻ�����,·�ɼ�����Ŀ���������
			 * 	-props: ��������,����ͷ��Ϣ
			 * 	-body: ��Ϣ����byte[]����
			 */
			ch.basicPublish("", "helloworld", MessageProperties.PERSISTENT_TEXT_PLAIN, "Hello world!".getBytes());

			System.out.println("��Ϣ�ѷ���");
			c.close();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//�����ŵ�
		
	}

}
