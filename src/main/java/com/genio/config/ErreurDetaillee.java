package com.genio.config;

import com.genio.utils.ErreurType;

public class ErreurDetaillee {
    private String champ;
    private String message;
    private ErreurType type;

    public ErreurDetaillee(String champ, String message, ErreurType type) {
        this.champ = champ;
        this.message = message;
        this.type = type;
    }

    public String getChamp() { return champ; }
    public String getMessage() { return message; }
    public ErreurType getType() { return type; }

    @Override
    public String toString() {
        return "[" + type + "] " + champ + " : " + message;
    }
}