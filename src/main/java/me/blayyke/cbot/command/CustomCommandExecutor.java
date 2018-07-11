package me.blayyke.cbot.command;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import me.blayyke.cbot.CBot;
import me.blayyke.cbot.MiscUtils;
import me.blayyke.cbot.redis.Redis;
import me.blayyke.cbot.redis.keys.hash.ccmd.CCFieldAction;
import me.blayyke.cbot.redis.keys.hash.ccmd.CCFieldCreator;
import me.blayyke.cbot.redis.keys.hash.ccmd.CCFieldDescription;
import me.blayyke.cbot.script.Data;
import me.blayyke.cbot.script.Embeds;
import me.blayyke.cbot.script.entity.ScriptChannel;
import me.blayyke.cbot.script.entity.ScriptGuild;
import me.blayyke.cbot.script.entity.ScriptMember;
import me.blayyke.cbot.script.entity.ScriptMessage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.time.Instant;

public class CustomCommandExecutor extends AbstractCommand {
    private final Guild guild;
    private final String name;
    private String action;
    private String code;
    private String description;
    private long creatorId;

    public CustomCommandExecutor(Guild guild, String name) {
        this.guild = guild;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();
        engine.put("user", new ScriptMember(event.getMember()));
        engine.put("message", new ScriptMessage(event.getMessage()));
        engine.put("channel", new ScriptChannel(event.getChannel()));
        engine.put("guild", new ScriptGuild(event.getGuild()));
        engine.put("time", Instant.now());
        engine.put("args", args);
        engine.put("input", MiscUtils.joinStringArray(args, " "));
        engine.put("data", new Data(event.getGuild()));
        engine.put("embeds", new Embeds(event.getGuild()));
        try {
            engine.eval("(function() {" + MiscUtils.getCode(action, code) + "})();");
            System.out.println("Executed code   : " + MiscUtils.getCode(action, code));
        } catch (ScriptException e) {
            event.getChannel().sendMessage("Failed to execute script: " + e.getMessage()).queue();
            CBot.getInstance().getLogger().warn("Failed to execute custom command " + name + " in guild " + guild.getName() + ":");
            e.printStackTrace();
        }
    }

    public Guild getGuild() {
        return guild;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public String getAction() {
        return action;
    }

    public String getDescription() {
        return description;
    }

    public void setAction(String action) {
        this.action = action;
        if (action != null)
            Redis.getInstance().hashSet(new CCFieldAction(guild, name), action);
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
        Redis.getInstance().hashSet(new CCFieldCreator(guild, name), String.valueOf(creatorId));
    }

    public void setDescription(String description) {
        this.description = description;
        if (description != null)
            Redis.getInstance().hashSet(new CCFieldDescription(guild, name), description);
    }

    public void setCode(String actionFromUrl) {
        this.code = actionFromUrl;
    }
}