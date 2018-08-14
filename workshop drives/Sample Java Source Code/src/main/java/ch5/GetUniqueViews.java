package ch5;

import redis.clients.jedis.Jedis;

public class GetUniqueViews extends AbstractSetExample
{
    public Long recordUserPageView(int pageId, int year, int month, int day, int userId) {

        try (Jedis jedis = pool.getResource()) {
            String key = dailyPageViewKey(pageId, year, month, day);
            return jedis.sadd(key, Integer.toString(userId));
        }
    }

    public Long getUniqueViews(int pageId, int year, int month, int day) {

        try (Jedis jedis = pool.getResource()) {
            String key = dailyPageViewKey(pageId, year, month, day);

            return jedis.scard(key);
        }
    }


    public static void main(String[] args) {

        try {
            GetUniqueViews pv = new GetUniqueViews();

            pv.init();

            System.out.println("Page 201 Unique views for March 4 2017: " +
                    pv.getUniqueViews(201, 2017, 03, 04));


            System.exit(0);

        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
