package ch2;

import redis.clients.jedis.Jedis;

public class TrackingVotes extends AbstractStringExample
{
    public boolean userVoted(Jedis jedis, int userId, int itemId) {

        String key = usersVoteKey(userId);
        Boolean voted = jedis.getbit(key, itemId);

        return voted;
    }

    public boolean upvoteItem(int userId, int itemId) {

        try (Jedis jedis = pool.getResource()) {

            if (!userVoted(jedis, userId, itemId)) {
                jedis.setbit(usersVoteKey(userId), itemId, Boolean.TRUE);
                jedis.incrBy(itemVoteKey(itemId), 2);
                return true;
            } else {
                return false;
            }

        }
    }

    public boolean downvoteItem(int userId, int itemId) {

        try (Jedis jedis = pool.getResource()) {

            if (!userVoted(jedis, userId, itemId)) {
                jedis.setbit(usersVoteKey(userId), itemId, Boolean.TRUE);
                jedis.decr(itemVoteKey(itemId));
                return true;
            } else {
                return false;
            }

        }
    }

    public static void main(String[] args) {

        try {
            TrackingVotes voter = new TrackingVotes();
            voter.init();
            voter.clearItemVote(20);
            voter.clearItemVote(21);
            voter.clearUsersVote(3001);
            voter.clearUsersVote(3002);




            voter.upvoteItem(3001, 20);
            voter.upvoteItem(3001, 21);
            voter.upvoteItem(3001, 20);

            voter.downvoteItem(3002, 20);
            voter.downvoteItem(3002, 20);

            System.out.println("Vote count for item 20: " + voter.getItemVote(20));

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
