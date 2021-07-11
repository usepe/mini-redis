package com.urielsepe.miniredis.delivery.command;

public abstract class Command {
    protected String name;

    public abstract String run(String[] arguments);

    public boolean shouldCall(String command) {
        return command.equalsIgnoreCase(name);
    }
}
