package dev.solanium.solaniumtowny.managers;

import dev.solanium.solaniumtowny.repository.impl.TownRepository;
import dev.solanium.solaniumtowny.town.Town;

import java.awt.font.OpenType;
import java.util.Optional;

public class TownManager {

    private final TownRepository townRepository;

    public TownManager(TownRepository townRepository) {
        this.townRepository = townRepository;
    }

    public Optional<Town> getTownById(int id) {
        return townRepository.findTownById(id);
    }
}
