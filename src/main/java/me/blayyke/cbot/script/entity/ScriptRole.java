package me.blayyke.cbot.script.entity;

import me.blayyke.cbot.script.ScriptColorHolder;
import me.blayyke.cbot.script.ScriptEntity;
import me.blayyke.cbot.script.ScriptMentionable;
import me.blayyke.cbot.script.ScriptNameHolder;
import net.dv8tion.jda.core.entities.Role;

import java.awt.*;

public class ScriptRole implements ScriptEntity, ScriptNameHolder, ScriptMentionable, ScriptColorHolder {
    private final Role role;

    public ScriptRole(Role role) {
        this.role = role;
    }

    @Override
    public String getId() {
        return role.getId();
    }

    @Override
    public String mention() {
        return role.getAsMention();
    }

    @Override
    public String getName() {
        return role.getName();
    }

    @Override
    public Color getColor() {
        return role.getColor();
    }
}