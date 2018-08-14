package ch2;

import redis.clients.jedis.Jedis;

public class DownvoteItem extends AbstractStringExample
{
    public long downvoteItem(int itemId) {

        try (Jedis jedis = pool.getResource()) {

            String key = itemVoteKey(itemId);
            return jedis.decr(key);
        }

    }
    public static void main(String[] args) {

        try {
            DownvoteItem voter = new DownvoteItem();
            voter.init();
            voter.clearItemVote(20);

            long votes = voter.downvoteItem(20);
            System.out.println("Vote count for item 20: " + votes);

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
