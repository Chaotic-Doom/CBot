package me.blayyke.cbot.redis.keys.hash;

import me.blayyke.cbot.redis.keys.AbstractKey;

public abstract class AbstractHashKey extends AbstractKey {
    public abstract String getField();
}