package me.blayyke.cbot.command;

import me.blayyke.cbot.DataManager;
import me.blayyke.cbot.MiscUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandPrefix extends AbstractCommand {
    @Override
    String getName() {
        return "prefix";
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        if (args.length == 0) {
            event.getChannel().sendMessage("The current prefix is `" + DataManager.getInstance().getGuildData(event.getGuild()).getPrefix() + "`").queue();
            return;
        }
        String s = MiscUtils.joinStringArray(args, " ");
        DataManager.getInstance().getGuildData(event.getGuild()).setPrefix(s);
        event.getChannel().sendMessage("Prefix updated. New prefix: `" + s.replace("`", "\\`") + "`").queue();
    }
}