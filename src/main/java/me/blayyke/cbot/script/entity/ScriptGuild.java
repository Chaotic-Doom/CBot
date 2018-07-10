package me.blayyke.cbot.script.entity;

import me.blayyke.cbot.script.ScriptEntity;
import me.blayyke.cbot.script.ScriptIconHolder;
import me.blayyke.cbot.script.ScriptNameHolder;
import net.dv8tion.jda.core.entities.*;

public class ScriptGuild implements ScriptEntity, ScriptNameHolder, ScriptIconHolder {
    private final Guild guild;

    public ScriptGuild(Guild guild) {
        this.guild = guild;
    }

    @Override
    public String getId() {
        return guild.getId();
    }

    @Override
    public String getName() {
        return guild.getName();
    }

    public ScriptChannel getChannel(String id) {
        TextChannel channel = guild.getTextChannelById(id);
        return channel == null ? null : new ScriptChannel(channel);
    }

    public ScriptMember getMember(String id) {
        Member member = guild.getMemberById(id);
        return member == null ? null : new ScriptMember(member);
    }

    public ScriptRole getRole(String id) {
        Role role = guild.getRoleById(id);
        return role == null ? null : new ScriptRole(role);
    }

    public ScriptChannel getDefaultChannel() {
        return new ScriptChannel(guild.getDefaultChannel());
    }

    @Override
    public String getIcon() {
        return guild.getIconUrl();
    }
}