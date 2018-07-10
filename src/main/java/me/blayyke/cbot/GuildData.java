package me.blayyke.cbot;

import me.blayyke.cbot.command.CustomCommandExecutor;
import me.blayyke.cbot.redis.Redis;
import me.blayyke.cbot.redis.keys.guild.KeyCommands;
import me.blayyke.cbot.redis.keys.guild.KeyPrefix;
import me.blayyke.cbot.redis.keys.hash.ccmd.CCFieldAction;
import me.blayyke.cbot.redis.keys.hash.ccmd.CCFieldCreator;
import me.blayyke.cbot.redis.keys.hash.ccmd.CCFieldDescription;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.utils.Checks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GuildData {
    private final Guild guild;
    private Map<String, CustomCommandExecutor> customCommandMap = new HashMap<>();

    GuildData(Guild guild) {
        this.guild = guild;
        Redis.getInstance().loadGuild(guild);
    }

    public String getPrefix() {
        String prefix = Redis.getInstance().get(new KeyPrefix(guild));
        if (prefix == null) {
            prefix = ";";
            setPrefix(prefix);
        }
        return prefix;
    }

    public void setPrefix(String prefix) {
        Redis.getInstance().set(new KeyPrefix(guild), prefix);
    }

    public void loadCommands() {
        Set<String> commandNames = Redis.getInstance().getSet(new KeyCommands(guild));
        if (commandNames.isEmpty()) return;
        Checks.noneNull(commandNames, "commandNames");

        commandNames.forEach(name -> {
            Checks.notNull(name, "command name");
            CustomCommandExecutor customCommandExecutor = new CustomCommandExecutor(guild, name);

            String action = Redis.getInstance().hashGet(new CCFieldAction(guild, name));
            long creatorId = Long.parseLong(Redis.getInstance().hashGet(new CCFieldCreator(guild, name)));
            String desc = Redis.getInstance().hashGet(new CCFieldDescription(guild, name));

            customCommandExecutor.setAction(action);
            customCommandExecutor.setCreatorId(creatorId);
            customCommandExecutor.setDescription(desc);
            loadCommand(customCommandExecutor);
        });
    }

    private void loadCommand(CustomCommandExecutor customCommandExecutor) {
        customCommandMap.put(customCommandExecutor.getName().toLowerCase(), customCommandExecutor);
        CBot.getInstance().getLogger().info("Created/loaded custom command {} in guild {} (ID={})", customCommandExecutor.getName().toLowerCase(), guild.getName(), guild.getId());
    }

    public void createCommand(CustomCommandExecutor command) {
        Checks.notNull(command, "command");
        Redis.getInstance().appendToSet(new KeyCommands(command.getGuild()), command.getName().toLowerCase());
        loadCommand(command);
    }

    public CustomCommandExecutor getCommand(String commandName) {
        return customCommandMap.get(commandName.toLowerCase());
    }
}