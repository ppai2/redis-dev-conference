package ch5;

import redis.clients.jedis.Jedis;

public class RecordingPageViewIdx extends RecordingPageView
{
   public Long recordUserPageView(int pageId, int year, int month, int day, int userId) {

       try (Jedis jedis = pool.getResource()) {

           // your code here
           String key = dailyPageViewKey(pageId, year, month, day);
           return jedis.sadd(key, Integer.toString(userId));
       }
   }

    public static void main(String[] args) {

        try {
            RecordingPageViewIdx pv = new RecordingPageViewIdx();

            pv.init();
            pv.processPageViewEvents(samplePageViewEvents);
            pv.showDatabaseState();

            System.exit(0);

        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
