package me.blayyke.cbot.script.event;

import me.blayyke.cbot.script.entity.ScriptGuild;
import net.dv8tion.jda.core.entities.Guild;

public abstract class ScriptEvent {
    private final Guild guild;

    public ScriptEvent(Guild guild) {
        this.guild = guild;
    }

    public final ScriptGuild getGuild() {
        return new ScriptGuild(guild);
    }
}