package me.blayyke.cbot.redis;

import me.blayyke.cbot.CBot;
import me.blayyke.cbot.DataManager;
import me.blayyke.cbot.redis.keys.AbstractKey;
import me.blayyke.cbot.redis.keys.guild.KeyCommands;
import me.blayyke.cbot.redis.keys.hash.AbstractHashKey;
import net.dv8tion.jda.core.entities.Guild;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Redis {
    private static Redis instance;
    private Jedis jedis;
    private boolean connected = false;

    public void connect() {
        if (connected) throw new IllegalStateException("already connected!");
        this.jedis = new Jedis("localhost");
        jedis.clientSetname(CBot.class.getSimpleName() + "-DBClient");
        jedis.connect();

        connected = true;
        System.out.println("Redis connection established!");
    }

    public void loadGuild(Guild guild) {
        DataManager.getInstance().getGuildData(guild).loadCommands();
        CBot.getInstance().getLogger().info("Finished setting up guild {} ({})", guild.getName(), guild.getId());
    }

    public String get(AbstractKey key) {
        return jedis.get(key.getFormattedKey());
    }

    public void set(AbstractKey key, String value) {
        jedis.set(key.getFormattedKey(), value);
    }

    public boolean keyExists(AbstractKey key) {
        return jedis.exists(key.getFormattedKey());
    }

    public void setExpiry(AbstractKey key, TimeUnit timeUnit, int amount) {
        jedis.expire(key.getFormattedKey(), Math.toIntExact(timeUnit.toSeconds(amount)));
    }

    public Set<String> getSet(AbstractKey key) {
        return jedis.smembers(key.getFormattedKey());
    }

    public void appendToSet(AbstractKey key, String value) {
        jedis.sadd(key.getFormattedKey(), value);
    }

    public boolean hashExists(AbstractHashKey key) {
        return jedis.hexists(key.getFormattedKey(), key.getField());
    }

    public String hashGet(AbstractHashKey key) {
        return jedis.hget(key.getFormattedKey(), key.getField());
    }

    public void hashSet(AbstractHashKey key, String value) {
        jedis.hset(key.getFormattedKey(), key.getField(), value);
    }

    public void delete(AbstractKey key) {
        jedis.del(key.getFormattedKey());
    }

    public void removeFromSet(KeyCommands keyCommands, String name) {
        jedis.srem(keyCommands.getFormattedKey(), name);
    }

    public static Redis getInstance() {
        return instance;
    }

    static {
        instance = new Redis();
    }
}