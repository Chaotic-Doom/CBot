package me.blayyke.cbot.redis.keys;

import net.dv8tion.jda.core.entities.Guild;

public class KeyCustomCommand extends AbstractKey {
    private Guild guild;
    private String commandName;

    public KeyCustomCommand(Guild guild, String commandName) {
        this.guild = guild;
        this.commandName = commandName;
    }

    @Override
    public String getFormattedKey() {
        return guild.getId() + "_cc_" + getCommandName();
    }

    public Guild getGuild() {
        return guild;
    }

    private String getCommandName() {
        return commandName;
    }
}