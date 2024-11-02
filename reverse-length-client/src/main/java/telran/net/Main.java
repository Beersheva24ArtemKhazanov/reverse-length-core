package telran.net;

import java.util.HashSet;
import java.util.List;
import org.json.JSONObject;

import telran.view.*;

public class Main {
    final static String[] TYPES = {"reverse", "length"};
    public static ReverseLengthClient client;
    public static void main(String[] args) {
        Item[] items = {
            Item.of("Start Session", Main::startSession),
            Item.of("Exit", Main::exit, true)
        };
        Menu menu = new Menu("Reverse Length Application", items);
        menu.perform(new StandardInputOutput());
    }

    static void startSession(InputOutput io) {
        String host = io.readString("Enter hostname");
      int port = io.readNumberRange("Enter Port", "Wrong Port", 3000, 50000).intValue();
        if (client != null) {
            client.close();
        }
        client = new ReverseLengthClient(host, port);
        Item[] items = {
            Item.of("Enter String and Type", Main::enteringData),
            Item.ofExit()
        };
        Menu menu = new Menu("Run Session", items);
        menu.perform(io);
    }
    
    static void enteringData(InputOutput io) {
        String string = io.readString("Enter any string");
        HashSet<String> typesSet = new HashSet<>(List.of(TYPES));
        String type = io.readStringOptions("Enter type from" + typesSet, "type must be from" + typesSet, typesSet);
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("string", string);
        String response = client.sendAndReceive(json.toString());
        io.writeLine(response);
    }

    static void exit(InputOutput io) {
        if (client != null) {
            client.close();
        }
    }
        

}