package me.blayyke.cbot.redis.keys.guild;

import net.dv8tion.jda.core.entities.Guild;

public class KeyPrefix extends AbstractGuildKey {
    public KeyPrefix(Guild guild) {
        super(guild);
    }

    @Override
    public String getFormattedKey() {
        return getGuild().getId() + "_prefix";
    }
}