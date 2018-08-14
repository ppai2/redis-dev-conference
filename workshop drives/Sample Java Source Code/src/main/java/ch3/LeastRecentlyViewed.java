package ch3;

import redis.clients.jedis.Jedis;

public class LeastRecentlyViewed extends AbstractListExample
{
    public ViewedItem getLeastRecentForUser(int userId) {

        // get our key
        String key = userHistoryKey(userId);

        try (Jedis jedis = pool.getResource()) {

            String itemAsJson = jedis.rpop(key);
            if (itemAsJson != null) {
                return ViewedItem.from_serialized_json(itemAsJson);
            } else {
                return null;
            }
        }

    }

    public static void main(String[] args) {

        try {
            LeastRecentlyViewed lmr = new LeastRecentlyViewed();
            lmr.init();

            lmr.clearUserHistory(1001);
            lmr.clearUserHistory(3001);
            lmr.addUserHistory(3001);

            ViewedItem item = lmr.getLeastRecentForUser(1001);
            prettyPrint("UserId: 1001", item);
            item = lmr.getLeastRecentForUser(3001);
            prettyPrint("UserId: 3001", item);

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }


    }

}
