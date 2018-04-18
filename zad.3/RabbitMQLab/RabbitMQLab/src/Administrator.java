import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Administrator {

    public static void main(String[] argv) throws Exception {

        System.out.println("ADMINISTRATOR");

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // exchange
        String EXCHANGE_NAME = "exchange";
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // queues & binds
        String logQueueName = channel.queueDeclare().getQueue();
        channel.queueBind(logQueueName, EXCHANGE_NAME, "#");
        System.out.println("created queue to log: " + logQueueName);

        Consumer consumer = createConsumer(channel);

        System.out.println("Waiting for messages...");
        channel.basicConsume(logQueueName, true, consumer);

        while(true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Send message to everybody...");
            String msg = br.readLine();
            channel.basicPublish(EXCHANGE_NAME, "info", null, msg.getBytes("UTF-8"));
        }
    }

    private static Consumer createConsumer(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String receivedMsg = new String(body, "UTF-8");
                System.out.println("Received: " + receivedMsg);
            }
        };
    }
}
