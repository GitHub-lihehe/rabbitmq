package rabbitmq.publicsubscribe;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.52.132");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		try {
			Connection con=f.newConnection();
			Channel c=con.createChannel();
			
			c.exchangeDeclare("logs", "fanout");
			
			while(true) {
				System.out.println("����....");
				String msg = new Scanner(System.in).nextLine();
				if ("exit".equals(msg)) {
					break;
				}
				
				//��һ������,��ָ���Ľ�����������Ϣ
				//�ڶ�������,��ָ������,���������򽻻����󶨶���
				//�����û�ж��а󶨵�����������Ϣ�ͻᶪʧ��
				//�����������˵û������;��ʹû�������߽��գ�����Ҳ���԰�ȫ�ض�����Щ��Ϣ��
				c.basicPublish("logs", "", null, msg.getBytes("UTF-8"));
				System.out.println("��Ϣ�ѷ���: "+msg);
			}
			c.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}

}
