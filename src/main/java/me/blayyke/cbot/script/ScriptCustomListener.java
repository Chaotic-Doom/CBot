package me.blayyke.cbot.script;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import me.blayyke.cbot.CBot;
import me.blayyke.cbot.MiscUtils;
import me.blayyke.cbot.script.entity.ScriptGuild;
import me.blayyke.cbot.script.event.ScriptEvent;
import net.dv8tion.jda.core.entities.Guild;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.time.Instant;

public class ScriptCustomListener {
    private Guild guild;
    private Class<? extends ScriptEvent> event;

    private String action;
    private String code;

    public ScriptCustomListener(Guild guild, Class<? extends ScriptEvent> event) {
        this.guild = guild;
        this.event = event;
    }

    void fireEvent() {
        ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();
        engine.put("guild", new ScriptGuild(getGuild()));
        engine.put("time", Instant.now());
        try {
            engine.eval("(function() {" + getCode() + "})();");
            System.out.println("Executed code: " + getCode());
        } catch (ScriptException e) {
            CBot.getInstance().getLogger().warn("Failed to execute event script " + event.getClass().getSimpleName() + " in guild " + guild.getName() + ":");
            e.printStackTrace();
        }
    }

    public Guild getGuild() {
        return guild;
    }

    public Class<? extends ScriptEvent> getEvent() {
        return event;
    }

    public String getAction() {
        return action;
    }

    public String getCode() {
        code = MiscUtils.getCode(action, code);
        return code;
    }

    public void setAction(String action) {
        this.action = action;
//        if (action != null)
//            Redis.getInstance().hashSet(new CCFieldAction(guild, ), action);
    }
}