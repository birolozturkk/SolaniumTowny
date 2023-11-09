package dev.solanium.solaniumtowny.repository.impl;

import com.j256.ormlite.support.ConnectionSource;
import dev.solanium.solaniumtowny.SortedList;
import dev.solanium.solaniumtowny.repository.Repository;
import dev.solanium.solaniumtowny.town.Town;
import dev.solanium.solaniumtowny.town.TownRegion;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TownRegionRepository extends Repository<TownRegion, Integer> {

    private final SortedList<TownRegion> townRegions = new SortedList<>(Comparator.comparing(TownRegion::getTownId));

    public TownRegionRepository(ConnectionSource connectionSource) {
        super(connectionSource, TownRegion.class, Comparator.comparing(TownRegion::getId));
        townRegions.addAll(getEntries());
        townRegions.sort();
    }

    @Override
    public void addEntry(TownRegion townRegion) {
        super.addEntry(townRegion);
        townRegions.add(townRegion);
    }

    public List<TownRegion> getTownRegions(int townId) {
        int index = Collections.binarySearch(townRegions, new TownRegion(0, townId),
                Comparator.comparing(TownRegion::getTownId));
        if (index < 0) return Collections.emptyList();
        List<TownRegion> result = new ArrayList<>();
        result.add(townRegions.get(index));

        int currentIndex = index - 1;
        while (true) {
            if (currentIndex < 0) break;
            TownRegion townRegion = townRegions.get(currentIndex);
            if (townId == townRegion.getTownId()) {
                result.add(townRegions.get(currentIndex));
                currentIndex--;
            } else {
                break;
            }
        }

        currentIndex = index + 1;

        while (true) {
            if (currentIndex >= townRegions.size()) break;
            TownRegion townRegion = townRegions.get(currentIndex);
            if (townId == townRegion.getTownId()) {
                result.add(townRegions.get(currentIndex));
                currentIndex++;
            } else {
                break;
            }
        }
        return result;
    }

    public CompletableFuture<TownRegion> registerTownRegion(TownRegion townRegion) {
        return CompletableFuture.supplyAsync(() -> {
            TownRegion createdTownRegion = create(townRegion);
            addEntry(createdTownRegion);
            entries.sort();
            return createdTownRegion;
        });
    }
}