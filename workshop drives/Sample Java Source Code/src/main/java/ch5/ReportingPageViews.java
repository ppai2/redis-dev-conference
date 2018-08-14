package ch5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public class ReportingPageViews extends GetUniqueViews
{

    public List<String> scanKeys(String pattern) {

        try (Jedis jedis = pool.getResource()) {

            ScanParams params = new ScanParams();
            params.match(pattern);

            List<String> keys = new ArrayList<>();
            ScanResult<String> sres = jedis.scan("0", params);

            while (true) {
                keys.addAll(sres.getResult());

                if (sres.getStringCursor().equals("0")) {
                    break;
                } else {
                    sres = jedis.scan(sres.getStringCursor(), params);
                }
            }

            return keys;
        }
    }

    public void reportUniquePageViews() {

        List<String> keys = scanKeys("page:*:unique:*");
        Collections.sort(keys);

        for(String key: keys) {
            KeyComponents keyComponents = convertKeyToComponents(key);
            Long views = getUniqueViews(keyComponents.pageId, keyComponents.year,
                    keyComponents.month, keyComponents.day);
            logPageView(keyComponents.pageId, keyComponents.year,
                    keyComponents.month, keyComponents.day, views.intValue());
        }


    }

    public static void main(String[] args) {

        try {
            ReportingPageViews pv = new ReportingPageViews();

            pv.init();
            pv.reportUniquePageViews();

            System.exit(0);

        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }


}
