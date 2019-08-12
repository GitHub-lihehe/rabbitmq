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
		//创建连接工厂,并设置连接信息
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.52.132");
		f.setPort(5672);//可选,5672是默认端口
		f.setUsername("admin");
		f.setPassword("admin");
		
		/*
		 * 与rabbitmq服务器建立连接,
		 * rabbitmq服务器端使用的是nio,会复用tcp连接,
		 * 并开辟多个信道与客户端通信
		 * 以减轻服务器端建立连接的开销
		 */
		try {
			Connection c = f.newConnection();
			Channel ch = c.createChannel();
			
			/*
			 * 声明队列,会在rabbitmq中创建一个队列
			 * 如果已经创建过该队列，就不能再使用其他参数来创建
			 * 
			 * 参数含义:
			 *   -queue: 队列名称
			 *   -durable: 队列持久化,true表示RabbitMQ重启后队列仍存在
			 *   -exclusive: 排他,true表示限制仅当前连接可用
			 *   -autoDelete: 当最后一个消费者断开后,是否删除队列
			 *   -arguments: 其他参数
			 */
			ch.queueDeclare("helloworld", true,false,false,null);
			
			
			/*
			 * 发布消息
			 * 这里把消息向默认交换机发送.
			 * 默认交换机隐含与所有队列绑定,routing key即为队列名称
			 * 
			 * 参数含义:
			 * 	-exchange: 交换机名称,空串表示默认交换机"(AMQP default)",不能用 null 
			 * 	-routingKey: 对于默认交换机,路由键就是目标队列名称
			 * 	-props: 其他参数,例如头信息
			 * 	-body: 消息内容byte[]数组
			 */
			ch.basicPublish("", "helloworld", MessageProperties.PERSISTENT_TEXT_PLAIN, "Hello world!".getBytes());

			System.out.println("消息已发送");
			c.close();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//建立信道
		
	}

}
