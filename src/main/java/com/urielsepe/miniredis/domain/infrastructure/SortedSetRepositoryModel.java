package com.urielsepe.miniredis.domain.infrastructure;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class SortedSetRepositoryModel {
    private final SortedSet<ScoreMemberPair> contents = new ConcurrentSkipListSet<>(new ScoreMemberPairComparator());

    public void addMember(Double score, String member) {
        contents.add(new ScoreMemberPair(score, member));
    }

    public int getCardinality() {
        return contents.size();
    }

    public Integer getMemberRank(String member) {
        int count = 0;
        for (ScoreMemberPair content : contents) {
            if (content.getMember().equals(member)) {
                return count;
            }
            count++;
        }
        return null;
    }

    public List<String> subSet(Integer start, Integer stop) {
        List<String> result = new LinkedList<>();

        List<String> list = contents.stream().map(ScoreMemberPair::getMember).collect(Collectors.toList());

        int from = start >= 0 ? start : list.size() + start;
        int to = stop >= 0 ? stop : list.size() + stop;

        if (to > list.size()) {
            to = list.size() - 1;
        }

        for (int count = from; count <= to; count++) {
            result.add(list.get(count));
        }

        return result;
    }
}
