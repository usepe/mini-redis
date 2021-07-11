package com.urielsepe.miniredis.delivery.command;

import com.urielsepe.miniredis.domain.action.Incr;
import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;

public class IncrCommand extends Command {
    private final Incr incr;

    public IncrCommand(Incr incr) {
        this.incr = incr;
        this.name = "incr";
    }

    @Override
    public String run(String[] arguments) {
        if (arguments.length != 2) {
            return "wrong number of arguments for 'incr' command";
        }

        try {
            return "(integer) " + incr.call(arguments[1]);
        } catch (WrongTypeException e) {
            return "value is not an integer";
        }
    }
}
