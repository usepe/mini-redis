package com.urielsepe.miniredis.domain.infrastructure;

import com.urielsepe.miniredis.domain.model.Clock;
import com.urielsepe.miniredis.domain.model.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryRepository implements Repository {
    private final Clock clock;
    private Map<String, ValueRepositoryModel> valueContents = new ConcurrentHashMap<>();
    private Map<String, SortedSetRepositoryModel> sortedSetContents = new ConcurrentHashMap<>();

    public InMemoryRepository(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void set(String key, String value, Integer expirationSeconds) {
        valueContents.put(key, new ValueRepositoryModel(value, expirationSeconds));
    }

    @Override
    public String get(String key) {
        ValueRepositoryModel storedData = valueContents.get(key);
        if (Objects.isNull(storedData) || storedData.isExpired(clock.currentTime())) {
            return null;
        }
        return storedData.getValue();
    }

    @Override
    public int del(String[] keys) {
        return Arrays
            .stream(keys)
            .map((key) -> {
                ValueRepositoryModel model = valueContents.remove(key);
                if (Objects.nonNull(model)) {
                    return 1;
                }
                return 0;
            })
            .reduce(0, Integer::sum);
    }

    @Override
    public int keyCount() {
        AtomicInteger count = new AtomicInteger();

        valueContents.forEach((key, model) -> {
            if (!model.isExpired(clock.currentTime())) {
                count.getAndIncrement();
            }
        });

        return count.get();
    }

    @Override
    public int incr(String key) {
        ValueRepositoryModel model = valueContents.get(key);
        if (Objects.nonNull(model)) {
            int value = Integer.parseInt(model.getValue());

            model.setValue(String.valueOf(value + 1));

            return Integer.parseInt(model.getValue());
        }

        valueContents.put(key, new ValueRepositoryModel("1", null));

        return 1;
    }

    @Override
    public int zAdd(String key, Double score, String member) {
        if (valueContents.containsKey(key)) {
            throw new IllegalArgumentException("Key is already set as string");
        }

        SortedSetRepositoryModel model = sortedSetContents.getOrDefault(key, new SortedSetRepositoryModel());

        model.addMember(score, member);

        sortedSetContents.put(key, model);

        return 1;
    }

    @Override
    public int zCard(String key) {
        if (valueContents.containsKey(key)) {
            throw new IllegalArgumentException("Key is already set as string");
        }

        SortedSetRepositoryModel model = sortedSetContents.get(key);

        if (Objects.isNull(model)) {
            return 0;
        }

        return model.getCardinality();
    }

    @Override
    public Integer zRank(String key, String member) {
        if (valueContents.containsKey(key)) {
            throw new IllegalArgumentException("Key is already set as string");
        }

        SortedSetRepositoryModel model = sortedSetContents.get(key);

        if (Objects.isNull(model)) {
            return null;
        }

        return model.getMemberRank(member);
    }

    @Override
    public List<String> zRange(String key, Integer start, Integer stop) {
        if (valueContents.containsKey(key)) {
            throw new IllegalArgumentException("Key is already set as string");
        }

        SortedSetRepositoryModel model = sortedSetContents.get(key);

        return model.subSet(start, stop);
    }
}
