package ch1;

import redis.clients.jedis.Jedis;
import workshop.AbstractRedisExample;

/**
 * Created by tague on 3/12/17.
 */
public class HelloWorld extends AbstractRedisExample
{

    private String hostname = "redis-16464.c8.us-east-1-3.ec2.cloud.redislabs.com";
    private int port = 16464;
    private String password = "yaMUrDkMstxoWjUJ9";
    private int db = 0;


    public void run() {

        try (Jedis jedis = new Jedis(hostname, port) ) {
            jedis.connect();
            jedis.auth(password);
            jedis.select(db);

            jedis.set("workshop_message", "Hello World!");
            String msg = jedis.get("workshop_message");

            System.out.println(msg);
        }
    }

    public static void main(String[] argv) {

        HelloWorld hw = new HelloWorld();
        hw.run();

        System.exit(0);
    }
}
