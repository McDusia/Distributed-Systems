
import com.rabbitmq.client.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

public class Doctor {

    public static void main(String[] argv) throws Exception {

        System.out.println("DOCTOR");

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // exchange
        String EXCHANGE_NAME = "exchange";
       channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // queue & bind
        String callbackQueueName = channel.queueDeclare().getQueue();
        String queueForInfo = channel.queueDeclare().getQueue();
        channel.queueBind(queueForInfo, EXCHANGE_NAME, "info");
        final String corrId = UUID.randomUUID().toString();
        System.out.println("created callback queue: " + callbackQueueName);

        // consumer (message handling)
        channel.basicConsume(callbackQueueName, true, createConsumer(channel, corrId));
        channel.basicConsume(queueForInfo, false, createConsumerForInfo(channel));

        while (true) {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Test type: ");
            String testType = br.readLine();

            if ("exit".equals(testType)) {
                break;
            }
            if(!isTestTypeCorrect(testType)) {
                System.exit(1);
            }
            else {
                System.out.println("Patient name: ");
                String name = br.readLine();
                String msg = testType + "," + name;

                // publish
                String key = "request."+testType;

                channel.basicPublish(EXCHANGE_NAME, key, new AMQP.BasicProperties
                        .Builder()
                        .correlationId(corrId)
                        .replyTo(callbackQueueName)
                        .build(), msg.getBytes("UTF-8"));

                System.out.println("Sent: type - " +testType + ", name - " + name);

            }
        }
    }

    private static boolean isTestTypeCorrect(String testType) {
        if(testType.equalsIgnoreCase("knee") || testType.equalsIgnoreCase("elbow")
                || testType.equalsIgnoreCase("hip")) {
            return true;
        }
        System.out.println("Wrong test type...");
        return false;
    }

    private static Consumer createConsumer(Channel channel, String corrId) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                if(properties.getCorrelationId().equals(corrId)){
                    String message = new String(body, "UTF-8");
                    System.out.println("Received: " + message);
                }
            }
        };
    }

    private static Consumer createConsumerForInfo(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

                String receivedMsg = new String(body, "UTF-8");
                System.out.println("Received --- : " + receivedMsg);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
    }


}
