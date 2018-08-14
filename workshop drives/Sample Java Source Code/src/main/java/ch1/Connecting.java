package ch1;

import redis.clients.jedis.Jedis;
import workshop.AbstractRedisExample;

/**
 * Created by tague on 3/12/17.
 */
public class Connecting extends AbstractRedisExample
{
    private String hostname = "redis-16464.c8.us-east-1-3.ec2.cloud.redislabs.com";
    private int port = 16464;
    private String password = "yaMUrDkMstxoWjUJ9";
    private int db = 0;

    public void run() {

        try (Jedis jedis = new Jedis(hostname, port)) {
            jedis.connect();
            jedis.auth(password);
            jedis.select(db);

            isConnected(jedis);
        }

    }

    public static void main(String[] argv) {

        Connecting c = new Connecting();
        c.run();

        System.exit(0);
    }
}
