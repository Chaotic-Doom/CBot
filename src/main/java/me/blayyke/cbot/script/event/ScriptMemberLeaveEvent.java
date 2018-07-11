package me.blayyke.cbot.script.event;

import net.dv8tion.jda.core.entities.Member;

public class ScriptMemberLeaveEvent extends ScriptMemberEvent {
    public ScriptMemberLeaveEvent(Member member) {
        super(member);
    }
}