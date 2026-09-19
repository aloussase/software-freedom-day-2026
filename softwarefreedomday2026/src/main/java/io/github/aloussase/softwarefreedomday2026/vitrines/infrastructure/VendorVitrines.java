package io.github.aloussase.softwarefreedomday2026.vitrines.infrastructure;

import io.github.aloussase.softwarefreedomday2026.vitrines.domain.Vitrine;
import io.github.aloussase.softwarefreedomday2026.vitrines.domain.VitrineSymantics;

import java.net.http.HttpClient;
import java.util.List;

public class VendorVitrines implements VitrineSymantics {
    private final HttpClient client;

    public VendorVitrines(HttpClient client) {
        this.client = client;
    }

    @Override
    public List<Vitrine> homeVitrines() {
        // .....
        return List.of();
    }

    @Override
    public List<Vitrine> vitrines() {
        // ....
        return List.of();
    }
}
