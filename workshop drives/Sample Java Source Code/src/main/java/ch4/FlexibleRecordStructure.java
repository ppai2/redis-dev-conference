package ch4;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;

public class FlexibleRecordStructure extends AbstractHashExample
{
    private VerifiedUser userFromMap(Map<String, String> map) throws Exception {

        VerifiedUser user = new VerifiedUser();
        BeanUtils.populate(user, map);
        return user;
    }

    public void createSampleUsers(User r1Info, VerifiedUser r2Info) throws Exception {
        try (Jedis jedis = pool.getResource()) {

            System.out.println(BeanUtils.describe(r1Info));
            jedis.hmset(r1Info.getKey(), BeanUtils.describe(r1Info));
            jedis.hmset(r2Info.getKey(), BeanUtils.describe(r2Info));
        }
    }

    public void getSampleData(String r1Key, String r2Key) throws Exception {
        try (Jedis jedis = pool.getResource()) {

            VerifiedUser r1 = userFromMap(jedis.hgetAll(r1Key));
            System.out.println("r1: " + r1.toString());

            VerifiedUser r2 = userFromMap(jedis.hgetAll(r2Key));
            System.out.println("r2: " + r2.toString());

            System.out.println("Verified (r1): " + r1.getVerified());
            System.out.println("Verified (r2): " + r2.getVerified());
       }
    }

    public static void main(String[] args) {

        try {
            FlexibleRecordStructure frs = new FlexibleRecordStructure();
            frs.init();

            User r1Info = new User(281, "ruser", "Redis", "User", "ruser@somedomain.net");
            VerifiedUser r2Info = new VerifiedUser(282, "ruser", "Redis", "User", "ruser@somedomain.net", true);

            frs.createSampleUsers(r1Info, r2Info);
            frs.getSampleData(r1Info.getKey(), r2Info.getKey());

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
