package com.urielsepe.miniredis.delivery.command;

import com.urielsepe.miniredis.domain.action.Del;

public class DelCommand extends Command {
    private final Del del;

    public DelCommand(Del del) {
        this.del = del;
        this.name = "del";
    }

    @Override
    public String run(String[] arguments) {
        if (arguments.length != 2) {
            return "wrong number of arguments for 'del' command";
        }

        return "(integer) " + del.call(arguments[1]);
    }
}
