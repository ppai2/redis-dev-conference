package ch5;

import java.util.Collections;
import java.util.List;

public class ReportingPageViewsIdx extends ReportingPageViews
{

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
            ReportingPageViewsIdx pv = new ReportingPageViewsIdx();

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
