package rabbitmq.rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RPCServer {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ConnectionFactory cf=new ConnectionFactory();
		cf.setHost("192.168.52.132");
		cf.setPort(5672);
		cf.setUsername("admin");
		cf.setPassword("admin");
		
		Connection c = cf.newConnection();
		Channel ch = c.createChannel();
		
		/*
		 * ������� rpc_queue, ����������������Ϣ
		 * 
		 * ����:
		 * 1. queue, ������
		 * 2. durable, �־û�
		 * 3. exclusive, ����
		 * 4. autoDelete, �Զ�ɾ��
		 * 5. arguments, ������������
		 */
		ch.queueDeclare("rpc_queue",false,false,false,null);
		ch.queuePurge("rpc_queue");//��������е�����
		
		ch.basicQos(1);//һ��ֻ����һ����Ϣ
		
		//�յ�������Ϣ��Ļص�����
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				//�����յ�������(Ҫ��ڼ���쳲�������)
				String msg = new String(message.getBody(), "UTF-8");
				int n = Integer.parseInt(msg);
				//�����n��쳲�������
				int r = fbnq(n);
				String response = String.valueOf(r);
				
				//���÷�����Ӧ��id, ������idһ��, �����ͻ��˿��԰Ѹ���Ӧ������������ж�Ӧ
				BasicProperties replyProps =new BasicProperties().builder().correlationId(message.getProperties().getCorrelationId()).build();
				
				
				/*
				 * ������Ӧ��Ϣ
				 * 1. Ĭ�Ͻ�����
				 * 2. �ɿͻ���ָ����,����������Ӧ��Ϣ�Ķ�����
				 * 3. ����(����id)
				 * 4. ���ص���Ӧ��Ϣ
				 */
				ch.basicPublish("",message.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
				//����ȷ����Ϣ
				ch.basicAck(message.getEnvelope().getDeliveryTag(), false);
			}
		};
		
		//
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		//�����߿�ʼ������Ϣ, �ȴ��� rpc_queue����������Ϣ, ���Զ�ȷ��
		ch.basicConsume("rpc_queue", false, deliverCallback, cancelCallback);
	}
	protected static int fbnq(int n) {
		if(n == 1 || n == 2) return 1;
		
		return fbnq(n-1)+fbnq(n-2);
	}
}
