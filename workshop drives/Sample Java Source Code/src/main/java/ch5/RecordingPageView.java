package ch5;

import redis.clients.jedis.Jedis;

public class RecordingPageView extends AbstractSetExample
{
    public Long recordUserPageView(int pageId, int year, int month, int day, int userId) {

        try (Jedis jedis = pool.getResource()) {
            String key = dailyPageViewKey(pageId, year, month, day);
            return jedis.sadd(key, Integer.toString(userId));
        }
    }

    public void processPageViewEvents(Event[] events) {

        int cnt = 0;
        try (Jedis jedis = pool.getResource()) {
            for(Event e: events) {
                logPageViewEvent(e.id, e.pageId, e.userId, e.year, e.month, e.day);
                recordUserPageView(e.pageId, e.year, e.month, e.day, e.userId);
            }
        }

        System.out.println("Events processed: " + cnt);
    }

    public static void main(String[] args) {

        try {
            RecordingPageView pv = new RecordingPageView();

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
