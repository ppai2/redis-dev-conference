package ch4;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;

public class GetDataFromDatabase extends AbstractHashExample
{
    public void createRedisUser(User rInfo) throws Exception {
        try (Jedis jedis = pool.getResource()) {

            Map<String, String> hmSetMap = BeanUtils.describe(rInfo);
            jedis.hmset(rInfo.getKey(), hmSetMap);
        }
    }

    public void getUserEmail(String key) {
        try (Jedis jedis = pool.getResource()) {

            List<String> res = jedis.hmget(key, "email");
            if (res.size() == 0) {
                System.out.println("Result: empty");
            } else {
                System.out.println("Result: empty");
            }
        }
    }

    public void getNameFields(String key) {
        try (Jedis jedis = pool.getResource()) {

            List<String> res = jedis.hmget(key, "fName", "lName");
            if (res.size() == 0) {
                System.out.println("Result: empty");
            } else {
                for (String element: res) {
                    System.out.print(res);
                }
            }
        }
    }

    public void getAllFields(String key) throws Exception {

        try (Jedis jedis = pool.getResource()) {

            Map<String, String> res = jedis.hgetAll(key);
            User user = new User();
            BeanUtils.populate(user, res);

            System.out.println(user.toString());
        }
    }


    public static void main(String[] args) {

        try {
            GetDataFromDatabase gdfd = new GetDataFromDatabase();
            gdfd.init();

            User rInfo = new User(300, "ruser", "Redis", "User", "ruser@somedomain.net");
            String key = rInfo.getKey();

            gdfd.createRedisUser(rInfo);
            gdfd.getUserEmail(key);
            gdfd.getNameFields(key);
            gdfd.getAllFields(key);

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
