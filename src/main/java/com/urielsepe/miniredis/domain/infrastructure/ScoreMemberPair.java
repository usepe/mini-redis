package com.urielsepe.miniredis.domain.infrastructure;

public class ScoreMemberPair {
    private final Double score;
    private final String member;

    public ScoreMemberPair(Double score, String member) {
        this.score = score;
        this.member = member;
    }

    public Double getScore() {
        return score;
    }

    public String getMember() {
        return member;
    }
}
