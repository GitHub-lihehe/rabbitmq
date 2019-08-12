package rabbitmq.publicsubscribe.topicdirect;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producter {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.52.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection con=f.newConnection();
		Channel c=con.createChannel();
		
		//�������⽻����
		c.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);
		while (true) {
			System.out.print("������Ϣ: ");
			String msg = new Scanner(System.in).nextLine();
			if ("exit".contentEquals(msg)) {
				break;
			}
			System.out.print("����routingKey: ");
			String routingKey = new Scanner(System.in).nextLine();
			
			//����1: ��������
			//����2: routingKey, ·�ɼ�,������������־����,��"error","info","warning"
			//����3: ������������
			//����4: ��������Ϣ���� 
			c.basicPublish("topic_logs", routingKey, null, msg.getBytes());
			
			System.out.println("��Ϣ�ѷ���: "+routingKey+" - "+msg);
		}
		c.close();
	}

}
