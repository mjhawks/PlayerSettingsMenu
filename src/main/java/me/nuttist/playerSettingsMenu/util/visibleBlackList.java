package me.nuttist.playerSettingsMenu.util;

import me.nuttist.playerSettingsMenu.core.PlayerSettingsMenu;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class visibleBlackList {
    public static Set<UUID> getVisibleExceptions(Player player, PlayerSettingsMenu psm) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        NamespacedKey key = psm.getHideBlackListKey();

        String raw = pdc.get(key, PersistentDataType.STRING);
        Set<UUID> result = new HashSet<>();

        if (raw == null || raw.isEmpty()) {
            return result; // none set yet
        }

        String[] parts = raw.split(",");
        for (String part : parts) {
            try {
                result.add(UUID.fromString(part));
            } catch (IllegalArgumentException ignored) {
                // skip bad data
            }
        }

        return result;
    }
    public static void saveVisibleExceptions(Player player, PlayerSettingsMenu psm, Set<UUID> uuids) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        NamespacedKey key = psm.getHideBlackListKey();

        if (uuids.isEmpty()) {
            pdc.remove(key);
            return;
        }

        String joined = uuids.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(","));

        pdc.set(key, PersistentDataType.STRING, joined);
    }
}
