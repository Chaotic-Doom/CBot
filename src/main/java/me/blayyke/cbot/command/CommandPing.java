package me.blayyke.cbot.command;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandPing extends AbstractCommand {
    @Override
    String getName() {
        return "ping";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        long start = System.currentTimeMillis();
        event.getChannel().sendMessage("Ping!").queue(m -> {
            long time = System.currentTimeMillis() - start;
            m.editMessage("Ping! (" + time + "ms)").queue();
        });
    }
}