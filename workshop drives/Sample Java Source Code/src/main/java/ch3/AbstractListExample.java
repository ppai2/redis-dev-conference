package ch3;

import java.util.Date;

import redis.clients.jedis.Jedis;
import workshop.AbstractRedisExample;

public class AbstractListExample extends AbstractRedisExample
{
    private static final ViewedItem[] sampleHistoryData = {
            new ViewedItem(new Date(1488573871), "Unveiling the New Redis Enterprise Cloud UI", "https://redislabs.com/blog/unveiling-new-redis-enterprise-cloud-ui/"),
            new ViewedItem(new Date(1488573971), "First Ever Redis Modules Hackaton", "https://redislabs.com/blog/first-ever-redis-modules-hackathon/"),
            new ViewedItem(new Date(1488574071), "Redis Pack v4.4 Release", "https://redislabs.com/blog/redis-pack-v4-4-release/"),
            new ViewedItem(new Date(1488574271), "Count Min Sketch: The Art and Science of Estimating Stuff", "https://redislabs.com/blog/count-min-sketch-the-art-and-science-of-estimating-stuff/"),
            new ViewedItem(new Date(1488574471), "Redis Cloud Integrates with Databricks Spark", "https://redislabs.com/blog/redis-cloud-integrates-with-databricks-spark/"),
            new ViewedItem(new Date(1488574571), "ZeroBrane Studio Plugin for Redis Lua Scripts", "https://redislabs.com/blog/zerobrane-studio-plugin-for-redis-lua-scripts/"),
            new ViewedItem(new Date(1488574771), "Connecting Spark and Redis: A Detailed Look", "https://redislabs.com/blog/connecting-spark-and-redis-a-detailed-look/"),
            new ViewedItem(new Date(1488574871), "Top Redis Headaches for DevOps - Client Buffers", "https://redislabs.com/blog/top-redis-headaches-for-devops-client-buffers/"),
            new ViewedItem(new Date(1488575071), "5^H 6^H 7 Methods for Tracing and Debuging Redis Lua Scripts", "https://redislabs.com/blog/5-6-7-methods-for-tracing-and-debugging-redis-lua-scripts/"),
            new ViewedItem(new Date(1488575871), "Redis is Beautiful: A Vizualization of Redis Commands", "https://redislabs.com/blog/red-is-beautiful-a-visualization-of-redis-commands/")
    };

   protected String userHistoryKey(int userId) {

        return new StringBuilder("user:history:")
                .append(userId).toString();
    }

    protected void clearUserHistory(int userId) {

        try (Jedis jedis = pool.getResource()) {

            jedis.del(userHistoryKey(userId));
        }
    }

    protected void addUserHistory(int userId) {


        try (Jedis jedis = pool.getResource()) {

            for(ViewedItem item: sampleHistoryData) {
                jedis.lpush(userHistoryKey(userId), item.to_serialized_json());
            }

            jedis.ltrim(userHistoryKey(userId), 0, 9);
        }
    }
}
