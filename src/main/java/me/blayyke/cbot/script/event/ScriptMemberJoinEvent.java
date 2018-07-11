package me.blayyke.cbot.script.event;

import net.dv8tion.jda.core.entities.Member;

public class ScriptMemberJoinEvent extends ScriptMemberEvent {
    public ScriptMemberJoinEvent(Member member) {
        super(member);
    }
}