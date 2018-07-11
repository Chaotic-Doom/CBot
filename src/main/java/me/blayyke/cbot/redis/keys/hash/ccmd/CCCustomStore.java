package me.blayyke.cbot.redis.keys.hash.ccmd;

import me.blayyke.cbot.redis.keys.guild.AbstractGuildKey;
import net.dv8tion.jda.core.entities.Guild;

public class CCCustomStore extends AbstractGuildKey {
    private String key;

    public CCCustomStore(Guild guild, String key) {
        super(guild);
        this.key = key;
    }

    @Override
    public String getFormattedKey() {
        return getGuild().getId() + "_cstore_" + key;
    }
}
