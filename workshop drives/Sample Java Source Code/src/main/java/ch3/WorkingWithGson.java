package ch3;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class WorkingWithGson
{
    private static class ConnectionInfo {

        @SerializedName("host")
        public String host;

        @SerializedName("port")
        public int port;

        @SerializedName("password")
        public String password;

        @SerializedName("db")
        public int db;


        public ConnectionInfo(String host, int port, String password, int db) {
            this.host = host;
            this.port = port;
            this.password = password;
            this.db = db;
        }
    }

    private static final Map<String, ConnectionInfo> connectionCfg = new HashMap<>();
    private static final String serializedCfg = "";


    public WorkingWithGson() {
        connectionCfg.put("prod", new ConnectionInfo("prod-redis.mydomain.com", 6379, "secret", 0));
        connectionCfg.put("staging", new ConnectionInfo("staging-redis.mydomain.com", 6379, "notAsSecret", 0));
    }


    public void serializeJson() {

        Gson gson = new Gson();
        String json = gson.toJson(connectionCfg);
        System.out.println("Serialized JSON: " + json);
    }

    public void deserializeJson() {
        Gson gson = new Gson();
        Map<String, ConnectionInfo> cfg = gson.fromJson(serializedCfg, HashMap.class);

        Gson pPrint = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(pPrint.toJson(cfg));
    }

    public static void main(String[] args) {

        WorkingWithGson wwg = new WorkingWithGson();

        wwg.serializeJson();

        // Add the serialized Json to the appropriate variable then uncomment
        // this call
        // wwg.deserializeJson();

    }
}
