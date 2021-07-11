package com.urielsepe.miniredis.delivery.command;

import com.urielsepe.miniredis.delivery.command.Command;
import com.urielsepe.miniredis.domain.action.ZRank;
import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;

import java.util.Objects;

public class ZRankCommand extends Command {
    private final ZRank zRank;

    public ZRankCommand(ZRank zRank) {
        this.zRank = zRank;
        this.name = "zrank";
    }

    @Override
    public String run(String[] arguments) {
        if (arguments.length == 3) {
            try {
                Integer rank = zRank.call(arguments[1], arguments[2]);

                if (Objects.isNull(rank)) {
                    return "(nil)";
                }

                return "(integer) " + rank;
            } catch (WrongTypeException e) {
                return "operation against a key holding the wrong kind of value";
            }
        }

        return "wrong number of arguments for 'zrank' command";
    }
}
