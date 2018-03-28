import java.util.Map;

public class MessageParser {

    public void parseOperation(String[] operation, DistributedMap map) {
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
                    System.out.println("There is not an element with such key in map.");
                } else {
                    System.out.println("The value of "+ operation[1]+ " : "+ element);
                }

            }
            else if(operation[0].equalsIgnoreCase("REMOVE")) {
                if(map.containsKey(operation[1])) {
                    String element = map.remove(operation[1]);
                    System.out.println("The element: "+ element+" removed");
                } else {
                    System.out.println("There is not an element with such key in map.");
                }
            }
            else if(operation[0].equalsIgnoreCase("CONTAINS_KEY")) {
                if(map.containsKey(operation[1])) {
                    System.out.println("There is an element with such key in the map.");
                } else {
                    System.out.println("There is not an element with such key in the map.");
                }
            }
        } else  if (operation[0].equals("DISPLAY")){
            System.out.println(map.toString());
        } else {
            System.out.println("Wrong instruction");
        }
    }

}
