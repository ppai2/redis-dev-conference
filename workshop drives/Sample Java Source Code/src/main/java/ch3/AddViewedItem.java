package ch3;

import java.util.Date;

import redis.clients.jedis.Jedis;

public class AddViewedItem extends AbstractListExample
{
    private static final String uri = "https://redislabs.com/blog/unveiling-new-redis-enterprise-cloud-ui/";
    private static final String title = "Unveiling the New Redis Enterprise Cloud UI";


    public void addViewedItem(int userId, ViewedItem item) {

        // get our key
        String key = userHistoryKey(userId);
        String serializedJson = item.to_serialized_json();

        try (Jedis jedis = pool.getResource()) {

            // remove from list if there
            jedis.lrem(key, 1, serializedJson);

            // store in Redis
            jedis.lpush(key, serializedJson);

            // trim the list to 10 items
            jedis.ltrim(key, 0, 9);
        }

    }

    public static void main(String[] args) {

        try {
            AddViewedItem avi = new AddViewedItem();
            ViewedItem item = new ViewedItem(new Date(), uri, title);

            avi.init();
            avi.addViewedItem(3001, item);
            avi.showDatabaseState();

            System.exit(0);

        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
