package workshop;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

/**
 * Created by tague on 3/12/17.
 */
public abstract class AbstractRedisExample
{
    protected String hostname = null;
    protected int port;
    protected String password = null;
    protected int db;
    protected JedisPool pool = null;

    public void init() throws Exception {
        loadConnectionProps();
        setupPool();
    }

    protected void loadConnectionProps() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Redis.properties").getFile());

        Properties connProps = new Properties();
        connProps.load(new FileInputStream(file));

        hostname = connProps.getProperty("hostname");
        port = Integer.parseInt(connProps.getProperty("port"));
        password = connProps.getProperty("password");
        db = Integer.parseInt(connProps.getProperty("db"));
    }

    protected void setupPool() throws Exception {
        JedisPoolConfig poolCfg = new JedisPoolConfig();
        poolCfg.setMaxTotal(3);

        pool = new JedisPool(poolCfg, hostname, port, 500, password, false);
    }

    protected boolean isConnected(Jedis jedis) {

        if (jedis != null && jedis.isConnected()) {
            String msg = String.format("Connected to DB-%d@%s:%d", jedis.getClient().getDB(),
                    jedis.getClient().getHost(), jedis.getClient().getPort());
            System.out.println(msg);

            return true;
        } else {
            System.err.println("No connection to Redis");
            return false;
        }
    }

    protected void showDatabaseState() {

        Gson pPrint = new GsonBuilder().setPrettyPrinting().create();

        try (Jedis jedis = new Jedis(hostname, port)) {
            jedis.connect();
            jedis.auth(password);

            Set<String> keys = jedis.keys("*");
            for (String key: keys) {
                StringBuilder builder = new StringBuilder();
                builder.append(key).append("= ");

                String type = jedis.type(key);
                if ("string".equals(type)) {

                } else if ("list".equals(type)) {
                    List<String> list = jedis.lrange(key, 0, -1);
                    builder.append(pPrint.toJson(list));

                } else if ("hash".equals(type)) {
                    Map<String, String> map = jedis.hgetAll(key);
                    builder.append(pPrint.toJson(map));

                } else if ("set".equals(type)) {
                    Set<String> set = jedis.smembers(key);
                    builder.append(pPrint.toJson(set));

                } else if ("zset".equals(type)) {
                    Set<Tuple> tuples = jedis.zrangeWithScores(key, 0, -1);
                    builder.append(tuples);
                }

                builder.append("\n");
                System.out.println(builder.toString());
            }
        }
    }

    public static void prettyPrint(String msg, Object o) {
        StringBuilder builder = new StringBuilder(msg)
                .append("= ");

        Gson pprint = new GsonBuilder().setPrettyPrinting().create();
        builder.append(pprint.toJson(o));

        System.out.println(builder.toString());
    }
}
