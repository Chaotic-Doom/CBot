package me.blayyke.cbot.redis.keys.user;

import me.blayyke.cbot.redis.keys.AbstractKey;
import net.dv8tion.jda.core.entities.User;

public abstract class AbstractUserKey extends AbstractKey {
    private User user;

    public AbstractUserKey(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}