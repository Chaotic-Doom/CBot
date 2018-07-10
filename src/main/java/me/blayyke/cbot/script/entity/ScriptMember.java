package me.blayyke.cbot.script.entity;

import me.blayyke.cbot.script.*;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScriptMember implements ScriptEntity, ScriptMentionable, ScriptNameHolder, ScriptMessageReceiver, ScriptColorHolder, ScriptIconHolder {
    private Member member;

    public ScriptMember(Member member) {
        this.member = member;
    }

    @Override
    public String getId() {
        return member.getUser().getId();
    }

    @Override
    public String mention() {
        return member.getAsMention();
    }

    @Override
    public void message(String message) {
        member.getUser().openPrivateChannel().queue(c -> c.sendMessage(message).queue());
    }

    @Override
    public void message(MessageEmbed embed) {
        member.getUser().openPrivateChannel().queue(c -> c.sendMessage(embed).queue());
    }

    @Override
    public String getName() {
        return member.getUser().getName();
    }

    public String getNickname() {
        return member.getNickname();
    }

    @Override
    public Color getColor() {
        return member.getColor();
    }

    public OffsetDateTime getJoinDate() {
        return member.getJoinDate();
    }

    public OnlineStatus getOnlineStatus() {
        return member.getOnlineStatus();
    }

    @Override
    public String getIcon() {
        return member.getUser().getEffectiveAvatarUrl();
    }

    public List<ScriptRole> getRoles() {
        List<ScriptRole> roles = new ArrayList<>();
        for (Role role : member.getRoles()) roles.add(new ScriptRole(role));
        return roles;
    }
}