package B;

import redis.clients.jedis.Jedis;
import workshop.AbstractRedisExample;

public class Lua102 extends AbstractRedisExample
{
    public void passingParameters() {
        try (Jedis jedis = pool.getResource()) {
            System.out.println(jedis.eval("-- some Lua code here", 1, "foo", "bar", "baz"));
        }
    }

    public void parsingParameters() {

        String script = " --[[ \n" +
                "Note that this script also demonstrates: \n" +
                "    * a multi-line comment \n" +
                "    * Lua's varlist assignment \n" +
                "     * String concatanation \n" +
                "]]-- \n" +
                "local key = KEYS[1] \n" +
                "local arg1, arg2 = ARGV[1], ARGV[2] \n" +
                "local reply = 'Called with key \"' .. key .. '\" and args \"' .. arg1 .. '\" and \"' .. arg2 .. '\"' " +
                "return reply \n";

        try (Jedis jedis = pool.getResource()) {
            System.out.println(jedis.eval(script, 1, "foo", "bar", "baz"));
        }
    }

    public void scriptPing() {
        String scriptPing = "local reply = redis.call('PING') \n" +
            "return reply \n";

        try (Jedis jedis = pool.getResource()) {
            System.out.println(jedis.eval(scriptPing, 0));
        }
    }

    public void shaMultiSadd() {
        String script = "local set1, set2 = KEYS[1], KEYS[2] \n" +
                "redis.call('SADD', set1, unpack(ARGV)) \n" +
                "redis.call('SADD', set2, unpack(ARGV)) \n" +
                "return 'OK' \n";
        try (Jedis jedis = pool.getResource()) {
            String shaMultiSadd = jedis.scriptLoad(script);
            System.out.println(jedis.evalsha(shaMultiSadd, 2, "characters", "hobbits", "Bilbo", "Frodo"));
            System.out.println(jedis.evalsha(shaMultiSadd, 2, "characters", "wizards", "Gandalf", "Saruman"));
        }
    }

    public static void main(String[] args) {
        try {
            Lua102 lua = new Lua102();
            lua.init();

            lua.passingParameters();
            lua.parsingParameters();
            lua.scriptPing();
            lua.shaMultiSadd();

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }

    }
}

