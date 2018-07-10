package me.blayyke.cbot.command;

import me.blayyke.cbot.CBot;
import me.blayyke.cbot.DataManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.utils.Checks;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandHandler {
    private static CommandHandler instance;
    private Map<String, AbstractCommand> commandMap = new HashMap<>();
    private Logger logger = CBot.getInstance().getLogger();

    private CommandHandler() {
        loadCommands();
    }

    public static CommandHandler getInstance() {
        return instance;
    }

    static {
        instance = new CommandHandler();
    }

    public Optional<AbstractCommand> findCommand(GuildMessageReceivedEvent event, String command, String[] args) {
        AbstractCommand abstractCommand = commandMap.get(command.toLowerCase());
        if (abstractCommand != null) return Optional.of(abstractCommand);
        CustomCommandExecutor executor = DataManager.getInstance().getGuildData(event.getGuild()).getCommand(command.toLowerCase());
        if (executor != null) return Optional.of(executor);
        return Optional.empty();
    }

    private void loadCommands() {
        registerCommand(new CommandPing());
        registerCommand(new CommandPrefix());
        registerCommand(new CommandCustom());
    }

    private void registerCommand(AbstractCommand command) {
        Checks.notNull(command, "command");
        Checks.notNull(command.getName(), "command name");
        commandMap.put(command.getName(), command);
        logger.info("Registered command " + command.getName() + "!");
    }
}