package A;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import workshop.AbstractRedisExample;


public class Pipelines extends AbstractRedisExample
{
    private List<String> keys;

    public Pipelines(int count) {
        keys = new ArrayList<>(count);
        for(int i=0; i<count; i++) {
            keys.add(Integer.toString(i));
        }
    }

    // don't actually use this -- example purposes only
    public void setOneByOne() {

        long start = System.currentTimeMillis();
        try (Jedis jedis = pool.getResource()) {
            int i=0;
            for(String key: keys) {
                jedis.set(key, Integer.toString(i));
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("setOneByOne: " + (end - start)/1000.0 + " seconds");
    }

    public void delVariadic() {

        long start = System.currentTimeMillis();
        try (Jedis jedis = pool.getResource()) {
            jedis.del(keys.toArray(new String[keys.size()]));
        }
        long end = System.currentTimeMillis();

        System.out.println("delVariadic: " + (end - start)/1000.0 + " seconds");
    }

    // don't actually use -- example purposes only
    public void getOneByOne() {
        long start = System.currentTimeMillis();
        try (Jedis jedis = pool.getResource()) {
            int i=0;
            for(String key: keys) {
                jedis.get(key);
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("getOneByOne: " + (end - start)/1000.0 + " seconds");
    }

    public void getVariadic() {
        long start = System.currentTimeMillis();
        try (Jedis jedis = pool.getResource()) {
            // add your code here
        }
        long end = System.currentTimeMillis();

        System.out.println("getVariadic: " + (end - start)/1000.0 + " seconds");
    }

    public void pipelinedSets() {
        long start = System.currentTimeMillis();
        try (Jedis jedis = pool.getResource()) {
            Pipeline p = jedis.pipelined();
            int i=0;
            for (String key: keys) {
                p.set(key, Integer.toString(i));
            }

            p.syncAndReturnAll();
        }
        long end = System.currentTimeMillis();

        System.out.println("pipelinedSets: " + (end - start)/1000.0 + " seconds");
    }

    public void pipelinedGets() {
        try (Jedis jedis = pool.getResource()) {
            Pipeline p = jedis.pipelined();
            for(String key: keys) {
                p.get(key);
            }

            List<Object> objects = p.syncAndReturnAll();
            System.out.println(objects);
        }
    }

    // error example - don't use
    public void prematurePipeline() {
        try (Jedis jedis = pool.getResource()) {
            Pipeline p = jedis.pipelined();
            Response<Long> somecounter = p.incrBy("somecounter", 1);
            p.set("somekey", somecounter.toString());
            p.sync();

            System.out.println(jedis.get("somekey"));
        }
    }

    protected void pipelinedPing(int count, int size) {
        long start = System.currentTimeMillis();
        try (Jedis jedis = pool.getResource()) {
            Pipeline p = jedis.pipelined();
            for (int i = 0; i < count; i++) {
                p.ping();
                if (i % size == 0) {
                    p.sync();
                }
            }
            p.sync();
        }
        long end = System.currentTimeMillis();

        System.out.println("pipelinedPing(1000, " + size + "): " + (end - start) + " ms");
    }

    public void pipelinedPings() {

        for(int i=2; i<14; i++) {
            pipelinedPing(1000, i*2);
        }
     }

    public static void main(String[] args) {

        try {
            Pipelines pl = new Pipelines(100);
            pl.init();

            pl.setOneByOne();
            pl.delVariadic();

            pl.getOneByOne();
            pl.getVariadic();

            pl.pipelinedSets();
            pl.pipelinedGets();

            pl.prematurePipeline();

            pl.pipelinedPings();

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
