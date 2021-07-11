package com.urielsepe.miniredis.delivery.command;

import com.urielsepe.miniredis.domain.action.Get;

import java.util.Objects;

public class GetCommand extends Command {
    private final Get get;

    public GetCommand(Get get) {
        this.get = get;
        this.name = "get";
    }

    @Override
    public String run(String[] arguments) {
        if (arguments.length != 2) {
            return "wrong number of arguments for 'get' command";
        }

        String value = get.call(arguments[1]);
        if (Objects.isNull(value)) {
            return "(nil)";
        }

        return "\"" + value + "\"";
    }
}
