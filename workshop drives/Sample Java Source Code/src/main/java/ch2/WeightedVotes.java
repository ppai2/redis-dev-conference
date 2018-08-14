package ch2;

import redis.clients.jedis.Jedis;

public class WeightedVotes extends AbstractStringExample
{
    public Long upvoteItem(int itemId) {
        try (Jedis jedis = pool.getResource()) {

            String key = itemVoteKey(itemId);
            return jedis.incrBy(key, 2);

        }
    }

    public long downvoteItem(int itemId) {

        try (Jedis jedis = pool.getResource()) {

            String key = itemVoteKey(itemId);
            return jedis.decr(key);
        }

    }

    public static void main(String[] args) {

        try {
            WeightedVotes voter = new WeightedVotes();
            voter.init();
            voter.clearItemVote(20);

            voter.upvoteItem(20);
            voter.upvoteItem(20);
            voter.upvoteItem(20);
            voter.downvoteItem(20);

            System.out.println("Vote count for item 20: " + voter.getItemVote(20));
            System.out.println("Vote count for item 21: " + voter.getItemVote(21));
            System.out.println("Vote count for item 22: " + voter.getItemVote(22));

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
