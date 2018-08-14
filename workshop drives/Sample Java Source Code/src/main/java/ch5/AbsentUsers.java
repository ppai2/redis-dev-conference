package ch5;


import java.util.Set;

import redis.clients.jedis.Jedis;

public class AbsentUsers extends AbstractSetExample
{

    public void generateAbsentUsers(int year, int month) {

        try (Jedis jedis = pool.getResource()) {

            String mauKey = mauKey(year, month);
            String absentUserKey = absentUserKey(year, month);
            String allUsersKey = allUserKey();

            Set<String> users = jedis.smembers(allUsersKey);
            for (String user: users) {
                if (! jedis.sismember(mauKey, user)) {
                    jedis.sadd(absentUserKey, user);
                }
            }
        }
    }

    public Set<String> getAbsentUsers(int year, int month) {

        try (Jedis jedis = pool.getResource()) {
            String absentUsersKey = absentUserKey(year, month);
            return jedis.smembers(absentUsersKey);
        }
    }

    public static void main(String[] args) {
        try {
            AbsentUsers au = new AbsentUsers();

            au.init();
            au.createAllUsersSet();
            au.generateAbsentUsers(2017, 3);
            prettyPrint("Absent Users for March 2017= ", au.getAbsentUsers(2017, 3));

            System.exit(0);

        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
