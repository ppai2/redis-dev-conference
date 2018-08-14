package ch2;

import redis.clients.jedis.Jedis;
import workshop.AbstractRedisExample;

public class AbstractStringExample extends AbstractRedisExample
{
    public String itemVoteKey(int itemId) {
        return new StringBuilder()
                .append("item:")
                .append(itemId)
                .append(":vote-count")
                .toString();
    }

    public void clearItemVote(int itemId) {
        try (Jedis jedis = pool.getResource()) {

            jedis.del(itemVoteKey(itemId));
        }
    }

    public int getItemVote(int itemId) {
        try (Jedis jedis = pool.getResource()) {

            String val = jedis.get(itemVoteKey(itemId));
            if (val == null) {
                return 0;
            } else {
                return Integer.parseInt(val);
            }
        }
    }

    public String usersVoteKey(int userId) {
        return new StringBuilder()
                .append("user:")
                .append(userId)
                .append(":voutes")
                .toString();
    }

    public void clearUsersVote(int userId) {
        try (Jedis jedis = pool.getResource()) {
            jedis.del(itemVoteKey(userId));
        }
    }
}
