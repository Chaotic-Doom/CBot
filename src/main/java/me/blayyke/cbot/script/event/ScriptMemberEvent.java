package me.blayyke.cbot.script.event;

import me.blayyke.cbot.script.entity.ScriptMember;
import net.dv8tion.jda.core.entities.Member;

public class ScriptMemberEvent extends ScriptEvent {
    private final Member member;

    public ScriptMemberEvent(Member member) {
        super(member.getGuild());
        this.member = member;
    }

    public ScriptMember getMember() {
        return new ScriptMember(member);
    }
}