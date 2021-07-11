package com.urielsepe.miniredis.delivery.command;

import com.urielsepe.miniredis.domain.action.ZRange;
import com.urielsepe.miniredis.domain.exceptions.WrongTypeException;

import java.util.List;

public class ZRangeCommand extends Command {
    private final ZRange zRange;

    public ZRangeCommand(ZRange zRange) {
        this.zRange = zRange;
        this.name = "zrange";
    }

    @Override
    public String run(String[] arguments) {
        try {
            if (arguments.length == 4) {
                String key = arguments[1];
                Integer start = Integer.valueOf(arguments[2]);
                Integer stop = Integer.valueOf(arguments[3]);

                List<String> values = zRange.call(key, start, stop);

                if (values.size() == 0) {
                    return "(empty array)";
                } else {
                    StringBuilder response = new StringBuilder();
                    int order = 1;
                    for (String value : values) {
                        response
                                .append(order)
                                .append(") \"")
                                .append(value)
                                .append("\"\n");
                        order++;
                    }

                    return response.toString();
                }
            }
        } catch (WrongTypeException e) {
            return "operation against a key holding the wrong kind of value";
        } catch (NumberFormatException e) {
            return "value is not an integer";
        }

        return "wrong number of arguments for 'zrange' command";
    }
}
