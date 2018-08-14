package ch3;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;

public class GetRecentlyViewed extends AbstractListExample
{
    public List<ViewedItem> getRecentlyViewedItems(int userId) {

        String key = userHistoryKey(userId);

        try (Jedis jedis = pool.getResource()) {

            List<String> resultsAsJson = jedis.lrange(key, 0, 9);
            List<ViewedItem> results = new ArrayList<>();

            for (String json: resultsAsJson) {
                results.add(ViewedItem.from_serialized_json(json));
            }

            return results;
        }

    }

    public static void main(String[] args) {

        try {
            GetRecentlyViewed grv = new GetRecentlyViewed();
            grv.init();

            grv.clearUserHistory(3001);
            grv.addUserHistory(3001);

            prettyPrint("Recently viewed items for user 3001", grv.getRecentlyViewedItems(3001));
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
