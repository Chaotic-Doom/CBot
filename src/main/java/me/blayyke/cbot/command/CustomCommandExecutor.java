package me.blayyke.cbot.command;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import me.blayyke.cbot.CBHttp;
import me.blayyke.cbot.CBot;
import me.blayyke.cbot.MiscUtils;
import me.blayyke.cbot.redis.Redis;
import me.blayyke.cbot.redis.keys.hash.ccmd.CCFieldAction;
import me.blayyke.cbot.redis.keys.hash.ccmd.CCFieldCreator;
import me.blayyke.cbot.redis.keys.hash.ccmd.CCFieldDescription;
import me.blayyke.cbot.script.entity.ScriptChannel;
import me.blayyke.cbot.script.entity.ScriptGuild;
import me.blayyke.cbot.script.entity.ScriptMember;
import me.blayyke.cbot.script.entity.ScriptMessage;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomCommandExecutor extends AbstractCommand {
    private final Guild guild;
    private final String name;
    private String action;
    private String code;
    private String description;
    private long creatorId;
    private final Pattern urlPattern = Pattern.compile("(https?://(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]\\.[^\\s]{2,})");

    //Optional Http(s):// -> optional prefix (www. ect) -> Website name (google ect) -> .domain (com, org ect)/letters
    private final Pattern pastebinPattern = Pattern.compile("^(https?://)?(www.)?pastebin.com/([a-zA-Z0-9]+)$");
    private final Pattern pastebinRawPattern = Pattern.compile("(https?://)?(www.)?pastebin.com/raw/([a-zA-Z0-9]+)$");
    private final Pattern gistPattern = Pattern.compile("^(https?://)?gist.githubusercontent.com/([A-Za-z0-9]+)/[a-zA-Z0-9]+/raw/[A-Za-z0-9]+/[a-zA-Z]+$");

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
        try {
            engine.eval("(function() {" + getCode() + "})();");
            System.out.println("Executed code   : " + getCode());
        } catch (ScriptException e) {
            event.getChannel().sendMessage("Failed to execute script: " + e.getMessage()).queue();
            CBot.getInstance().getLogger().warn("Failed to execute custom command " + name + " in guild " + guild.getName() + ":");
            e.printStackTrace();
        }
    }

    private String getCode() {
        if (actionIsUrl()) {
            if (code == null) cacheActionFromURL();
            return code;
        } else return action;
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

    boolean actionIsUrl() {
        return urlPattern.matcher(action).matches();
    }

    void cacheActionFromURL() {
        if (actionIsUrl()) {
            Matcher matcher = pastebinPattern.matcher(action);
            if (pastebinRawPattern.matcher(action).matches()) code = CBHttp.getInstance().get(action);
            else if (gistPattern.matcher(action).matches()) code = CBHttp.getInstance().get(action);
            else if (matcher.matches()) {
                code = CBHttp.getInstance().get("https://pastebin.com/raw/" + matcher.group(3));
            } else throw new IllegalArgumentException("Action URL is not of supported type!");
        }
    }
}