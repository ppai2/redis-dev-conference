package B;

import redis.clients.jedis.Jedis;
import workshop.AbstractRedisExample;

public class Lua101 extends AbstractRedisExample
{
    private static final String script = " -- The \"Hello, World!\" program, a Redis Lua example \n" +
            "local message = 'Hello, World!' \n" +
            "return message \n";

    public void oneOffScripts() {
        try (Jedis jedis = pool.getResource()) {
            System.out.println("Reply " + jedis.eval(script));
        }
    }

    public void cachedScripts() {
        try (Jedis jedis = pool.getResource()) {
            String sha = jedis.scriptLoad(script);
            Object reply = jedis.evalsha(sha, 0);

            System.out.println("SHA1: " + sha + ", Reply: " + reply);
        }
    }

    public void longRunningScripts() {
        try (Jedis jedis = pool.getResource()) {
            System.out.println(jedis.configGet("lua-time-limit"));
            System.out.println(jedis.configGet("used_memory_lua"));

        }
    }


    public static void main(String[] args) {

        try {
            Lua101 lua = new Lua101();
            lua.init();

            lua.oneOffScripts();
            lua.cachedScripts();
            lua.longRunningScripts();

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }

    }

}
