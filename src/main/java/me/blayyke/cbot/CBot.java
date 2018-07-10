package me.blayyke.cbot;

import me.blayyke.cbot.command.CommandHandler;
import me.blayyke.cbot.redis.Redis;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class CBot extends ListenerAdapter {
    private static CBot instance;
    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    private CBot() throws LoginException {
        new JDABuilder(AccountType.BOT)
                .setToken("NDY1NjM5NjkxNTEzNzU3NzA2.DiYBaQ.67_dDnMsWlAZn1Ig613VRfYr7VI")
                .addEventListener(this)
                .buildAsync();
        Redis.getInstance().connect();
    }

    public static void main(String[] args) throws LoginException {
        instance = new CBot();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.isWebhookMessage() || event.getAuthor().isBot() || event.getAuthor().isFake()) return;

        String prefix = DataManager.getInstance().getGuildData(event.getGuild()).getPrefix();
        String content = event.getMessage().getContentRaw();
        if (!content.toLowerCase().startsWith(prefix.toLowerCase())) {
            String selfId = event.getJDA().getSelfUser().getId();
            if (content.startsWith("<@" + selfId + "> ")) prefix = "<@" + selfId + "> ";
            else if (content.startsWith("<@!" + selfId + "> ")) prefix = "<@!" + selfId + "> ";
            else return;
        }
        content = content.substring(prefix.length());

        String[] split = content.split("\\s+", 2);
        String command = split[0];
        String[] args = split.length == 1 ? new String[0] : split[1].split("\\s+");

        CommandHandler.getInstance().findCommand(event, command, args).ifPresent(cmd -> {
            cmd.execute(event, args);
            logger.info(event.getAuthor() + "#" + event.getAuthor().getDiscriminator() + " executed command " + command + " with args " + Arrays.toString(args));
        });
    }

    public static CBot getInstance() {
        return instance;
    }

    public Logger getLogger() {
        return logger;
    }
}