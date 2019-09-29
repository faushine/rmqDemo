import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @author Yuxin Fan
 * @create 2019-09-28
 */
public class RPCClient {


  private Connection connection;
  private Channel channel;
  private String requestQueueName = "rpc_queue";

  public RPCClient() throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");

    connection = factory.newConnection();
    channel = connection.createChannel();
  }

  public static void main(String[] argv) throws IOException {
    RPCClient rpc = null;
    long startTime = System.currentTimeMillis();
    try {
      rpc = new RPCClient();
      byte[] buff = new byte[1];
      for (int i = 0; i < 100; i++) {
        System.out.println(" [x] Requesting with #" + i + " byte");
        rpc.call(buff);
        System.out.println(" [.] Got ack from server");
      }
    } catch (IOException | TimeoutException | InterruptedException e) {
      e.printStackTrace();
    }finally {
      if (rpc!=null){
        rpc.close();
        System.out.println("Average round trip time is "+(System.currentTimeMillis()-startTime)/100+" ms");
      }
    }
  }

  public String call(byte[] message) throws IOException, InterruptedException {
    final String corrId = UUID.randomUUID().toString();

    String replyQueueName = channel.queueDeclare().getQueue();
    AMQP.BasicProperties props = new AMQP.BasicProperties
        .Builder()
        .correlationId(corrId)
        .replyTo(replyQueueName)
        .build();

    channel.basicPublish("", requestQueueName, props, message);

    final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

    String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
      if (delivery.getProperties().getCorrelationId().equals(corrId)) {
        response.offer(new String(delivery.getBody(), "UTF-8"));
      }
    }, consumerTag -> {
    });

    String result = response.take();
    channel.basicCancel(ctag);
    return result;
  }

  public void close() throws IOException {
    connection.close();
  }

}
