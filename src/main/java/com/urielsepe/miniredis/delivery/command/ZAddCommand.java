package com.urielsepe.miniredis.delivery.command;

import com.urielsepe.miniredis.domain.action.ZAdd;
import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;

public class ZAddCommand extends Command {
    private final ZAdd zAdd;

    public ZAddCommand(ZAdd zAdd) {
        this.zAdd = zAdd;
        this.name = "zadd";
    }

    @Override
    public String run(String[] arguments) {
        try {
            if (arguments.length == 4) {
                return "(integer) " + zAdd.call(arguments[1], Double.valueOf(arguments[2]), arguments[3]);
            }

            return "wrong number of arguments for 'zadd' command";
        } catch (WrongTypeException e) {
            return "operation against a key holding the wrong kind of value";
        } catch (NumberFormatException e) {
            return "value is not a valid float";
        }
    }
}
