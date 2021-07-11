package com.urielsepe.miniredis.delivery.command;

import com.urielsepe.miniredis.domain.action.Set;

public class SetCommand extends Command {
    private final Set set;

    public SetCommand(Set set) {
        this.set = set;
        this.name = "set";
    }

    @Override
    public String run(String[] arguments) {
        if (arguments.length == 3) {
            set.call(arguments[1], arguments[2], null);
            return "OK";
        } else if (arguments.length == 5 && arguments[3].equalsIgnoreCase("ex")) {
            try {
                set.call(arguments[1], arguments[2], Integer.valueOf(arguments[4]));
                return "OK";
            } catch (NumberFormatException e) {
                return "value is not an integer";
            }
        }

        return "wrong number of arguments for 'set' command";
    }
}
