package workshop;

import ch5.ReportingPageViews;
import redis.clients.jedis.Jedis;

public class ClearDatabase extends AbstractRedisExample
{
    public void clearDatabase() {
        try(Jedis jedis = pool.getResource()) {
            jedis.flushDB();
        }
    }

    public static void main(String[] args) {

        try {
            ClearDatabase pv = new ClearDatabase();

            pv.init();
            pv.clearDatabase();

            System.exit(0);

        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
