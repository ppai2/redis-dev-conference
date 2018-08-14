package B;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisNoScriptException;
import workshop.AbstractRedisExample;

public class Lua103 extends AbstractRedisExample {

    public List<Object> pipelinedMultiLPop(String key, int count) {
        try (Jedis jedis = pool.getResource()) {
            Pipeline p = jedis.pipelined();
            for (int i = 0; i < count; i++) {
                p.lpop(key);
            }

            return p.syncAndReturnAll();
        }
    }

    public String loadLuaScript() {

    String script = "local key = KEYS[1] \n" +
            "local count = tonumber(ARGV[1]) \n" +
            "local reply = {} \n" +
            "for _ = 1, count do \n" +
            "   reply[#reply+1] = redis.call('LPOP', key) \n" +
            "end \n" +
            "return reply ";

        try(Jedis jedis = pool.getResource()) {
            return jedis.scriptLoad(script);
        }
    }


    public Object scriptedMultiLpop(String sha, String key, int count) {
        try(Jedis jedis = pool.getResource()) {
            return jedis.evalsha(sha, 1, key, Integer.toString(count));
        }
    }

    public void setUpData(String key, int count) {

        Map<String, Double> initData = new HashMap<>(count);
        for(int i=0; i< count; i++) {
            initData.put(Integer.toString(i), (double)i);
        }

        try(Jedis jedis = pool.getResource()) {
            Pipeline p = jedis.pipelined();
            p.del(key);
            p.zadd(key, initData);
            p.sync();
        }
    }

    public double clientSum(String key) {
        try(Jedis jedis = pool.getResource()) {
            Set<Tuple> tuples = jedis.zrangeWithScores(key, 0, -1);

            double sum = 0.0;
            for(Tuple t: tuples) {
                sum += t.getScore();
            }

            return sum;
        }
    }

    public float scriptedSum(String key) {
        String script = "local total = 0 \n" +
                "local data = redis.call('ZRANGE', KEYS[1], 0, -1, 'WITHSCORES') \n" +
                "while #data > 0 do \n" +
                "    -- ZRANGE replies with an array in which scores are at even indices \n" +
                "    table.remove(data, 1) \n" +
                "    total = total + tonumber(table.remove(data, 1)) \n" +
                "end \n" +
                "return total";

        try(Jedis jedis = pool.getResource()) {
            String shaSum = jedis.scriptLoad(script);

            Long result = (Long)jedis.evalsha(shaSum, 1, key);
            return result.floatValue();
        }
    }

    public void scriptedTransaction(int debit, int credit, float amount) throws Exception {
        String script =
                "local debitkey, creditkey, amount, fname = \n" +
                    "KEYS[1], \n" +
                    "KEYS[2], \n" +
                    "tonumber(ARGV[1]), \n" +
                    "'balance' \n" +
                "local balance = tonumber(redis.call('HGET', debitkey, fname)) \n " +
                "if balance >= amount then \n" +
                "  redis.call('HINCRBYFLOAT', debitkey, fname, -amount) \n" +
                "  redis.call('HINCRBYFLOAT', creditkey, fname, amount) \n" +
                "  return redis.status_reply('OK') \n" +
                "else \n " +
                "   return redis.error_reply('insufficent funds - call parents') \n" +
                "end \n";

        MessageDigest md = MessageDigest.getInstance("SHA-1");
        String sha1 = Hex.encodeHexString(md.digest(script.getBytes(Protocol.CHARSET)));

        try(Jedis jedis = pool.getResource()) {
            while (true) {
                try {
                    jedis.evalsha(sha1, 2, String.format("account:%d", debit), String.format("account:%d", credit), Float.toString(amount));
                    break;
                } catch (JedisNoScriptException e) {
                    String sha2 = jedis.scriptLoad(script);
                    System.out.println(sha2);
                }
            }
        }
    }

    public void nonDeterministicScript() {
        String script =
                "local t = redis.call('TIME') \n" +
                "redis.call('SET', KEYS[1], tostring(t[1]) .. '.' .. tostring(t[2])) \n";

        try(Jedis jedis = pool.getResource()) {

            jedis.eval(script, 1, "now");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {


        try {
            Lua103 lua = new Lua103();

            lua.init();

            String key = "bunchofnumbers";
            lua.loadLuaScript();
            lua.setUpData(key, 10);

            System.out.println(lua.clientSum(key));
            System.out.println(lua.scriptedSum(key));

            lua.scriptedTransaction(1, 2, 10.0f);
            lua.nonDeterministicScript();



            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
