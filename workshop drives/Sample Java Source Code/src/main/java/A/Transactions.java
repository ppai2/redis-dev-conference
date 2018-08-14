package A;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;
import workshop.AbstractRedisExample;

public class Transactions extends AbstractRedisExample
{
    public String createAccount(int accountId, float accountBalance) {
        try (Jedis jedis = pool.getResource()) {
            String key = String.format("account:%d", accountId);
            Map<String, String> dic = new HashMap<>();
            dic.put("accountid", Integer.toString(accountId));
            dic.put("status", "active");
            dic.put("balance", Float.toString(accountBalance));

            return jedis.hmset(key, dic);
        }
    }

    public List<Object> transferAmount(int debit, int credit, float amount) {
        try (Jedis jedis = pool.getResource()) {
            String debitKey = String.format("account:%d", debit);
            String creditKey = String.format("account:%d", credit);

            Transaction txn = jedis.multi();
            txn.hincrByFloat(debitKey, "balance", -amount);
            txn.hincrByFloat(creditKey, "balance", amount);

            return txn.exec();
        }
    }

    public List<Object> incorrectCheckBalanceAndTransferAmount(int debit, int credit, float amount) throws Exception {
        try (Jedis jedis = pool.getResource()) {
            String debitKey = String.format("account:%d", debit);
            String creditKey = String.format("account:%d", credit);
            String fname = "balance";

            float balance = Float.parseFloat(jedis.hget(debitKey, fname));

            // potential race condition - start
            if (balance >= amount) {
                Transaction txn = jedis.multi();
                txn.hincrByFloat(debitKey, fname, -amount);
                txn.hincrByFloat(creditKey, fname, amount);

                return txn.exec();
            } else {
                throw new Exception("insufficient funds - go and earn some");
            }
        }
    }

    public List<Object> checkBalanceAndTransferAmount(int debit, int credit, float amount) throws Exception {
        try (Jedis jedis = pool.getResource()) {
            String debitKey = String.format("account:%d", debit);
            String creditKey = String.format("account:%d", credit);
            String fname = "balance";

            while(true) {
                try {
                    Pipeline p = jedis.pipelined();
                    p.watch(debitKey);
                    Response<String> hgetResp = p.hget(debitKey, fname);
                    p.sync();

                    float balance = Float.valueOf(hgetResp.get());

                    p.multi();
                    if (balance >= amount) {
                        p.hincrByFloat(debitKey, fname, -amount);
                        p.hincrByFloat(creditKey, fname, amount);
                        return p.exec().get();
                    } else {
                        throw new Exception("insufficent funds - time to get a job");
                    }
                } catch (JedisException e) {
                   e.printStackTrace();
                }
            }
        }
    }

    public String mergeAccounts(int src, int dest) {
        try (Jedis jedis = pool.getResource()) {
            String debitKey = String.format("account:%d", src);
            String creditKey = String.format("account:%d", dest);

            // your code here
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            Transactions tr = new Transactions();
            tr.init();

            System.out.println(tr.createAccount(1, 0));
            System.out.println(tr.createAccount(2, 10));
            System.out.println(tr.transferAmount(2, 1, 1));

            System.out.println(tr.checkBalanceAndTransferAmount(2, 1, 10));
            System.out.println(tr.mergeAccounts(1, 2));

            System.exit(0);
        } catch (Exception e) {
            System.err.println("Failed to execute example");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
