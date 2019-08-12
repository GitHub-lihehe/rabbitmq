package rabbitmq.publicsubscribe.direct;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] a = {"warning", "info", "error"};
		
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.52.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		try {
			Connection con=f.newConnection();
			Channel c=con.createChannel();
			 
			c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
			
			while(true) {
				System.out.println("���� direct_logs:....");
				String msg = new Scanner(System.in).nextLine();
				if ("exit".equals(msg)) {
					break;
				}
				
				String routingKey=a[new Random().nextInt(a.length)];
				
				//��һ������,��ָ���Ľ�����������Ϣ
				//�ڶ�������,��ָ������,���������򽻻����󶨶���
				//�����û�ж��а󶨵�����������Ϣ�ͻᶪʧ��
				//�����������˵û������;��ʹû�������߽��գ�����Ҳ���԰�ȫ�ض�����Щ��Ϣ��
				c.basicPublish("direct_logs", routingKey, null, msg.getBytes("UTF-8"));
				System.out.println("��Ϣ�ѷ���: "+msg);
			}
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
