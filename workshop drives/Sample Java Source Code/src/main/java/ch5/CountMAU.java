package ch5;

import java.util.Set;

import redis.clients.jedis.Jedis;

public class CountMAU extends AbstractSetExample
{

    public void computeMonthlyUsers(int year, int month) {

        Set<String> keys = getKeysFromSecondaryIndex();
        try (Jedis jedis = pool.getResource()) {
            for (String key : keys) {
                KeyComponents comps = convertKeyToComponents(key);

                if (comps.year == year && comps.month == month) {
                    String mauKey = mauKey(year, month);
                    String dayKey = dailyPageViewKey(comps.pageId, comps.year, comps.month, comps.day);

                    jedis.sunionstore(mauKey, mauKey, dayKey);
                }
            }
        }
    }

    public Long getMAUCount(int year, int month) {

        try (Jedis jedis = pool.getResource()) {

            return jedis.scard(mauKey(year, month));
        }
    }

    public static void main(String[] args) {
        try {
            CountMAU mau = new CountMAU();

            mau.init();
            mau.computeMonthlyUsers(2017, 3);
            System.out.println("Monthly Active Users (MAU) for March 2017: " +
                mau.getMAUCount(2017, 3));

            System.exit(0);

        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
