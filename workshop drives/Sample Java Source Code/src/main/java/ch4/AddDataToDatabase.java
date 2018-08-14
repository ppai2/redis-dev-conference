package ch4;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;


public class AddDataToDatabase extends AbstractHashExample
{
    public void storeUsersEmail() {
        try (Jedis jedis = pool.getResource()) {
            User sample = new EmptyUser().createSampleUser();
            Long res = jedis.hset(sample.getKey(), "email", sample.getEmail());

            System.out.println("Result: " + res);
        }
    }

    public void storeEntireUser() throws Exception {
        try (Jedis jedis = pool.getResource()) {
            User rInfo = new User(147, "ruser", "Redis", "User", "ruser@somedomain.net");

            Map<String, String> hmSetMap = BeanUtils.describe(rInfo);
            String res = jedis.hmset(rInfo.getKey(), hmSetMap);

            System.out.println("Result: " + res);
        }
    }

    public static void main(String[] args) {

        try {
            AddDataToDatabase add = new AddDataToDatabase();
            add.init();

            add.storeUsersEmail();
            add.storeEntireUser();

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
