package ch2;


import redis.clients.jedis.Jedis;

public class UpvoteItem extends AbstractStringExample
{
    public Long upvoteItem(int itemId) {
        try (Jedis jedis = pool.getResource()) {

            String key = itemVoteKey(itemId);
            return jedis.incr(key);

        }
    }

    public static void main(String[] args) {

        try {
            UpvoteItem voter = new UpvoteItem();
            voter.init();

            long votes = voter.upvoteItem(20);
            System.out.println("Vote count for item 20: " + votes);

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
