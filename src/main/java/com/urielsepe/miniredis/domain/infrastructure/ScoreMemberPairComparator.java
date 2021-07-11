package com.urielsepe.miniredis.domain.infrastructure;

import java.util.Comparator;

public class ScoreMemberPairComparator implements Comparator<ScoreMemberPair> {
    @Override
    public int compare(ScoreMemberPair a, ScoreMemberPair b) {
        if (a.getScore().compareTo(b.getScore()) == 0) {
            return a.getMember().compareTo(b.getMember());
        }
        return a.getScore().compareTo(b.getScore());
    }
}
