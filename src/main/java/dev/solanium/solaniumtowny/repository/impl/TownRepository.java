package dev.solanium.solaniumtowny.repository.impl;

import com.j256.ormlite.support.ConnectionSource;
import dev.solanium.solaniumtowny.repository.Repository;
import dev.solanium.solaniumtowny.town.Town;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TownRepository extends Repository<Town, Integer> {

    public TownRepository(ConnectionSource connectionSource) {
        super(connectionSource, Town.class, Comparator.comparing(Town::getId));
    }

    public Optional<Town> findTownById(int id) {
        return entries.getEntry(new Town(id));
    }

    public CompletableFuture<Town> registerTown(Town town) {
        return CompletableFuture.supplyAsync(() -> {
            addEntry(town);
            entries.sort();
            return create(town);
        });
    }
}
