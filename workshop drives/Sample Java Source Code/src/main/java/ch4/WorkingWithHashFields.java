package ch4;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;


public class WorkingWithHashFields extends AbstractHashExample
{
    public void createRedisUser(VerifiedUser rInfo) throws Exception {
        try (Jedis jedis = pool.getResource()) {

            Map<String, String> hmSetMap = BeanUtils.describe(rInfo);
            jedis.hmset(rInfo.getKey(), hmSetMap);
        }
    }

    public void modifyFields(String key) throws Exception {
        try (Jedis jedis = pool.getResource()) {

            System.out.println("Initial keys: " + jedis.hkeys(key));
            System.out.println("Delete 'verified': " + jedis.hdel(key, "verified"));
            System.out.println("'verified' exists: " + jedis.hexists(key, "verified"));
        }
    }


    public static void main(String[] args) {

        try {
            WorkingWithHashFields wwhf = new WorkingWithHashFields();
            wwhf.init();

            VerifiedUser rInfo = new VerifiedUser(400, "ruser", "Redis", "User", "ruser@somedomain.net", true);
            String key = rInfo.getKey();

            wwhf.createRedisUser(rInfo);
            wwhf.modifyFields(key);

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
