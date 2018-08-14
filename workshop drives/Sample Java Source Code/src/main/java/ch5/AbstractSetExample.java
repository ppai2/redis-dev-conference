package ch5;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;
import workshop.AbstractRedisExample;

public class AbstractSetExample extends AbstractRedisExample {

    public static class KeyComponents {

        int pageId;

        int year;

        int month;

        int day;

        public KeyComponents(int pageId, int year, int month, int day) {
            this.pageId = pageId;
            this.year = year;
            this.month = month;
            this.day = day;
        }

    }

    protected static Event[] samplePageViewEvents = {
            new Event("1ef81361-0071-11e7-bf3a-4c3275922049", 2017, 3, 4, 3001, 201),
            new Event("1ef81554-0071-11e7-b11b-4c3275922049", 2017, 3, 4, 3001, 202),
            new Event("1ef8164f-0071-11e7-80ec-4c3275922049", 2017, 3, 4, 3002, 201),
            new Event("1ef81717-0071-11e7-9791-4c3275922049", 2017, 3, 4, 3001, 202),
            new Event("1ef8188a-0071-11e7-a448-4c3275922049", 2017, 3, 4, 3001, 201),
            new Event("1ef81917-0071-11e7-9215-4c3275922049", 2017, 3, 4, 3003, 201),
            new Event("1ef81997-0071-11e7-ac0b-4c3275922049", 2017, 3, 4, 3004, 201),
            new Event("1ef81a2e-0071-11e7-a560-4c3275922049", 2017, 3, 4, 3003, 201),
            new Event("1ef81ac2-0071-11e7-9ffe-4c3275922049", 2017, 3, 4, 3001, 205),
            new Event("1ef81b59-0071-11e7-967a-4c3275922049", 2017, 3, 4, 3003, 202),
            new Event("4eb72d75-0072-11e7-b160-4c3275922049", 2017, 3, 5, 3001, 201),
            new Event("4eb72f57-0072-11e7-aa82-4c3275922049", 2017, 3, 5, 3002, 202),
            new Event("4eb732b0-0072-11e7-9153-4c3275922049", 2017, 3, 5, 3002, 201),
            new Event("4eb733c0-0072-11e7-b177-4c3275922049", 2017, 3, 5, 3001, 202),
            new Event("4eb734e1-0072-11e7-aeb5-4c3275922049", 2017, 3, 5, 3003, 204),
            new Event("4eb7358a-0072-11e7-a629-4c3275922049", 2017, 3, 5, 3003, 204),
            new Event("4eb7364a-0072-11e7-b999-4c3275922049", 2017, 3, 5, 3001, 204),
            new Event("4eb73780-0072-11e7-b7c1-4c3275922049", 2017, 3, 5, 3001, 202),
            new Event("4eb7385c-0072-11e7-a8c5-4c3275922049", 2017, 3, 5, 3003, 201),
            new Event("4eb73907-0072-11e7-9caf-4c3275922049", 2017, 3, 5, 3001, 202)
    };

    public String dailyPageViewKey(int pageId, int year, int month, int day) {

        return new StringBuilder()
                .append("page:")
                .append(pageId)
                .append(":unique:")
                .append(year)
                .append(":")
                .append(month)
                .append(":")
                .append(day)
                .toString();
    }

    public String secondaryPageIndex() {
        return "index:unique-page";
    }

    public void logPageViewEvent(String eventId, int pageId, int userId, int year, int month, int day) {
        System.out.println(String.format("%s PAGE_VIEW: %04d user %04d %04d-%02d-%02d",
                eventId, pageId, userId, year, month, day));
    }

    public void logPageView(int pageId, int year, int month, int day, int views) {
        System.out.println(String.format("Unique Page Views %04d %04d-%02d-%02d: %03d",
                pageId, year, month, day, views));
    }

    public KeyComponents convertKeyToComponents(String key) {
        String[] splits = key.split(":");

        return new KeyComponents(Integer.parseInt(splits[1]),
                Integer.parseInt(splits[3]),
                Integer.parseInt(splits[4]),
                Integer.parseInt(splits[5]));
    }

    public Set<String> getKeysFromSecondaryIndex() {

        try (Jedis jedis = pool.getResource()) {
            return jedis.smembers(secondaryPageIndex());
        }
    }

    public String mauKey(int year, int month) {
        return new StringBuilder("site:metrics:")
                .append(year)
                .append(":")
                .append(month)
                .toString();
    }

    public String allUserKey() {
        return "site:users:all_users";
    }

    public void createAllUsersSet() {

        Set<String> users = new HashSet<>();
        for(Event e: samplePageViewEvents) {
            users.add(Integer.toString(e.userId));
        }
        users.add(Integer.toString(1001));
        users.add(Integer.toString(1002));
        users.add(Integer.toString(1003));

        try (Jedis jedis = pool.getResource()) {

            String[] usersAsArray = new String[users.size() ];
            jedis.sadd(allUserKey(), users.toArray(usersAsArray));
        }
    }

    public String absentUserKey(int year, int month) {
        return new StringBuilder("site:users:absent_users:")
                .append(year)
                .append(":")
                .append(month)
                .toString();
    }
}
