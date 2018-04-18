import org.jgroups.*;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.*;
import org.jgroups.stack.ProtocolStack;
import java.io.*;
import java.net.InetAddress;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK2;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.util.Util;


public class DistributedMap implements SimpleStringMap {

    private Map<String, String> map = new HashMap<>();
    private JChannel channel;

    private void setProtocolStack() throws Exception {

        ProtocolStack stack = new ProtocolStack();
        channel.setProtocolStack(stack);
        stack.addProtocol(new UDP()
                        .setValue("mcast_group_addr", InetAddress.getByName("230.0.0.130")))
                .addProtocol(new PING())
                .addProtocol(new MERGE3())
                .addProtocol(new FD_SOCK())
                .addProtocol(new FD_ALL()
                        .setValue("timeout", 12000)
                        .setValue("interval", 3000))
                .addProtocol(new BARRIER())
                .addProtocol(new NAKACK2())
                .addProtocol(new UNICAST3())
                .addProtocol(new STABLE())
                .addProtocol(new GMS())
                .addProtocol(new UFC())
                .addProtocol(new MFC())
                .addProtocol(new STATE_TRANSFER())
                .addProtocol(new FRAG2())
                .addProtocol(new SEQUENCER());
                //.addProtocol(new FLUSH());

        stack.init();
    }

    private static void handleView(JChannel channel, View new_view) {
        if(new_view instanceof MergeView) {
            ViewHandler handler=new ViewHandler(channel, (MergeView)new_view);
            handler.start();
        }
    }

    private void setReceiver() {
        channel.setReceiver(new ReceiverAdapter() {
            @Override
            public void viewAccepted(View view) {
                handleView(channel, view);
            }

            public void receive(Message msg) {
                String operation = msg.getObject().toString();
                updateMap(operation);
            }

            public void getState(OutputStream out){

                synchronized(map) {
                    try {
                        Util.objectToStream(map, new DataOutputStream(out));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            public void setState(InputStream input) throws Exception {
                Map<String,String> temporaryMap =(Map<String,String>)Util.objectFromStream(new DataInputStream(input));

                synchronized(map) {
                    map.clear();
                    map.putAll(temporaryMap);
                }
                System.out.println("received map (" + map.size() + " messages in chat history):");

                for(String str: map.keySet()) {
                    System.out.println(str);
                }
            }
        });
    }

    DistributedMap(String canalName) {
        try {
            System.setProperty("java.net.preferIPv4Stack","true");
            channel = new JChannel(false);
            setProtocolStack();
            channel.connect(canalName);//, null, 0);
            setReceiver();
            channel.getState(null,0);
            String line = "My message";
            Message msg = new Message(null, null, line);
            channel.send(msg);

        } catch(IOException e) {
            System.out.println("Failed sending message to cluster");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finalize() {
        System.out.println("Map instance is getting destroyed");
        channel.close();
    }

    @Override
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public String put(String key, String value) {
        String result = map.put(key, value);
        try {
            String operation = "PUT "+ key +" "+ value;

            channel.send(new Message(null,null, operation));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String remove(String key) {
        try {
            String operation = "REMOVE "+ key;

            channel.send(new Message(null,null, operation));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map.remove(key);
    }

    @Override
    public String toString() {
        int counter = 0;
        String result = "";
        for(Map.Entry<String, String> entry: map.entrySet()){
            result += counter + ": " + entry.getKey() + " -> "+ entry.getValue() + "\n";
            counter++;
        }
        return result;
    }

    private void updateMap(String operation) {
        String[] splitedOperation = operation.split(" ");
        parseOperation(splitedOperation, this.map);
    }

    private void parseOperation(String[] operation, Map<String, String> map) {
        int size = operation.length;
        if(size == 3) {
            //----- check put operation syntax
            if(operation[0].equalsIgnoreCase("PUT")) {
                map.put(operation[1], operation[2]);
            }
        } else if(size == 2) {
            //----- check get operation syntax
            if(operation[0].equalsIgnoreCase("GET")) {

                String element = map.get(operation[1]);
                if(element == null) {
                    System.out.println("There is not an element with such key in the map.");
                } else {
                    System.out.println("The value of "+ operation[1]+ " : "+ element);
                }

            }
            else if(operation[0].equalsIgnoreCase("REMOVE")) {
                if(map.containsKey(operation[1])) {
                    String element = map.remove(operation[1]);
                    System.out.println("The element: "+ element+" removed");
                } else {
                    System.out.println("There is not an element with such key in the map.");
                }
            }
            else if(operation[0].equalsIgnoreCase("CONTAINS_KEY")) {
                if(map.containsKey(operation[1])) {
                    System.out.println("There is an element with such key in the map.");
                } else {
                    System.out.println("There is not an element with such key in the map.");
                }
            }
        } else  {
            System.out.println("Wrong instruction");
        }
    }
}
