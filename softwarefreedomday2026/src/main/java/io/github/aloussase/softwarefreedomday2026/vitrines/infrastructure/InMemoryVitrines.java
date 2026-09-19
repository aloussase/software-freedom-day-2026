package io.github.aloussase.softwarefreedomday2026.vitrines.infrastructure;

import io.github.aloussase.softwarefreedomday2026.vitrines.domain.Vitrine;
import io.github.aloussase.softwarefreedomday2026.vitrines.domain.VitrineId;
import io.github.aloussase.softwarefreedomday2026.vitrines.domain.VitrineSymantics;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryVitrines implements VitrineSymantics {
    private final ConcurrentHashMap<VitrineId, Vitrine> vitrines = new ConcurrentHashMap<>();

    @Override
    public List<Vitrine> homeVitrines() {
        return List.of();
    }

    @Override
    public List<Vitrine> vitrines() {
        return List.of();
    }
}
