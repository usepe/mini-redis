package com.urielsepe.miniredis.delivery.command;

import com.urielsepe.miniredis.domain.action.ZCard;
import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;

public class ZCardCommand extends Command {
    private final ZCard zCard;

    public ZCardCommand(ZCard zCard) {
        this.zCard = zCard;
        this.name = "zcard";
    }

    @Override
    public String run(String[] arguments) {
        if (arguments.length != 2) {
            return "wrong number of arguments for 'zcard' command";
        }

        try {
            return "(integer) " + zCard.call(arguments[1]);
        } catch (WrongTypeException e) {
            return "operation against a key holding the wrong kind of value";
        }
    }
}
