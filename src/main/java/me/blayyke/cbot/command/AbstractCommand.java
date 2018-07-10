package me.blayyke.cbot.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public abstract class AbstractCommand {
    abstract String getName();

    public abstract void execute(GuildMessageReceivedEvent event, String[] args);
}