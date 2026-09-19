package io.github.aloussase.softwarefreedomday2026.vitrines.domain;

import java.math.BigDecimal;
import java.util.List;

public record Vitrine(
        VitrineId id,
        String name,
        List<VItem> items
) {
    public record VItem(
            String id,
            String name,
            BigDecimal price
    ) {
    }
}
