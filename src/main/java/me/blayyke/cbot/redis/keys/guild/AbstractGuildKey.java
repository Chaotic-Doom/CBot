package me.blayyke.cbot.redis.keys.guild;

import me.blayyke.cbot.redis.keys.AbstractKey;
import net.dv8tion.jda.core.entities.Guild;

public abstract class AbstractGuildKey extends AbstractKey {
    private Guild guild;

    public AbstractGuildKey(Guild guild) {
        this.guild = guild;
    }

    public Guild getGuild() {
        return guild;
    }
}