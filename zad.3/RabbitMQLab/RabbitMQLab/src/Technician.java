
import com.rabbitmq.client.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Technician {

    public static void main(String[] argv) throws Exception {

        System.out.println("TECHNICIAN");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter 1 type: ");
        String testType1 = br.readLine();
        System.out.println("Enter 2 type: ");
        String testType2 = br.readLine();

        if(!(isTestTypeCorrect(testType1) && isTestTypeCorrect(testType2))) {
            System.exit(1);
        }

        // connection & channel
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicQos(1);
        // exchange
        String EXCHANGE_NAME = "exchange";
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // queues & binds
        String queueName = channel.queueDeclare(testType1, false, false, false, null).getQueue();
        String key = "request."+testType1;
        channel.queueBind(queueName, EXCHANGE_NAME, key);
        System.out.println("created queue nr 1: " + queueName);

        String queueName2 = channel.queueDeclare(testType2, false, false, false, null).getQueue();
        key = "request."+testType2;

        channel.queueBind(queueName2, EXCHANGE_NAME, key);
        System.out.println("created queue nr 2: " + queueName2);

        String queueForInfo = channel.queueDeclare().getQueue();
        channel.queueBind(queueForInfo, EXCHANGE_NAME, "info");

        // consumer (message handling)
        Consumer consumer1 = createConsumer(channel, EXCHANGE_NAME, testType1);
        Consumer consumer2 = createConsumer(channel, EXCHANGE_NAME, testType2);

        channel.basicConsume(queueForInfo, false, createConsumer2(channel));
        System.out.println("Waiting for messages...");
        channel.basicConsume(queueName, false, consumer1);
        channel.basicConsume(queueName2, false, consumer2);
    }

    private static boolean isTestTypeCorrect(String testType) {
        if(testType.equalsIgnoreCase("knee") || testType.equalsIgnoreCase("elbow")
                || testType.equalsIgnoreCase("hip")) {
            return true;
        }
        System.out.println("Wrong test type...");
        return false;
    }

    private static Consumer createConsumer(Channel channel, String EXCHANGE_NAME, String testType) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

               AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(properties.getCorrelationId())
                        .build();

                String receivedMsg = new String(body, "UTF-8");
                System.out.println("Received: " + receivedMsg);
                String msgToSend = receivedMsg +" "+ "done";
                channel.basicPublish("", properties.getReplyTo(), replyProps, msgToSend.getBytes("UTF-8"));
                channel.basicPublish(EXCHANGE_NAME, "toLog", null, msgToSend.getBytes("UTF-8"));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
    }

    private static Consumer createConsumer2(Channel channel) {
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
