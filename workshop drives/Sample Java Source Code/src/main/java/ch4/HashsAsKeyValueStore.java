package ch4;


import redis.clients.jedis.Jedis;

public class HashsAsKeyValueStore extends AbstractHashExample
{
    public void upvoteItem(int itemId) {
        try (Jedis jedis = pool.getResource()) {
            jedis.hincrBy("item_vote_counters", Integer.toString(itemId), 1);
        }
    }

    public void showVotes() {
        try (Jedis jedis = pool.getResource()) {
            System.out.println(jedis.hgetAll("item_vote_counters"));
        }
    }

    public void getHashMaxZipList() {
        try (Jedis jedis = pool.getResource()) {
            System.out.println(jedis.configGet("hash-max-ziplist*"));
        }
    }

    public static void main(String[] args) {
        try {
            HashsAsKeyValueStore hvvs = new HashsAsKeyValueStore();
            hvvs.init();

            hvvs.upvoteItem(1);
            hvvs.upvoteItem(2);
            hvvs.upvoteItem(2);

            hvvs.showVotes();

            hvvs.getHashMaxZipList();

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
