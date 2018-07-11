package me.blayyke.cbot.script;

import me.blayyke.cbot.redis.Redis;
import me.blayyke.cbot.redis.keys.hash.ccmd.CCCustomStore;
import net.dv8tion.jda.core.entities.Guild;

public class Data {
    private Guild guild;

    public Data(Guild guild) {
        this.guild = guild;
    }

    public void store(String key, String value) {
        Redis.getInstance().set(new CCCustomStore(guild, key), value);
    }

    public String get(String key) {
        return Redis.getInstance().get(new CCCustomStore(guild, key));
    }

    public boolean exists(String key) {
        return Redis.getInstance().keyExists(new CCCustomStore(guild, key));
    }

    public void delete(String key) {
        Redis.getInstance().delete(new CCCustomStore(guild, key));
    }
}