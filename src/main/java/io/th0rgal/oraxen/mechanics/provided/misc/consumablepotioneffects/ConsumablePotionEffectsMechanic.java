package io.th0rgal.oraxen.mechanics.provided.misc.consumablepotioneffects;

import io.th0rgal.oraxen.mechanics.Mechanic;
import io.th0rgal.oraxen.mechanics.MechanicFactory;
import io.th0rgal.oraxen.utils.commands.CommandsParser;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ConsumablePotionEffectsMechanic extends Mechanic {

    private final Set<PotionEffect> effects = new HashSet<>();
    private CommandsParser commandsParser;

    public ConsumablePotionEffectsMechanic(MechanicFactory mechanicFactory, ConfigurationSection section) {
        super(mechanicFactory, section);
        for (String effectSection : section.getKeys(false)) {
            if (effectSection.equalsIgnoreCase("commands")) {
                continue;
            }

            if (section.isConfigurationSection(effectSection))
                registersEffectFromSection(section.getConfigurationSection(effectSection));
        }

        ConfigurationSection commandsSection = section.getConfigurationSection("commands");
        if (commandsSection != null)
            commandsParser = new CommandsParser(commandsSection);
    }

    public void registersEffectFromSection(ConfigurationSection section) {
        PotionEffectType effectType = PotionEffectType.getByName(section.getName());
        int amplifier = 0;
        int duration = 20 * 30;
        boolean ambient = false;
        boolean particles = true;
        boolean icon = true;
        if (section.isInt("amplifier"))
            amplifier = section.getInt("amplifier");
        if (section.isBoolean("ambient"))
            ambient = section.getBoolean("ambient");
        if (section.isBoolean("particles"))
            particles = section.getBoolean("particles");
        if (section.isBoolean("icon"))
            icon = section.getBoolean("icon");
        if (section.isInt("duration"))
            duration = section.getInt("duration");
        PotionEffect potionEffect = new PotionEffect(effectType, duration, amplifier, ambient, particles,
                icon);
        effects.add(potionEffect);
    }

    public void onItemConsumed(Player player) {
        player.addPotionEffects(effects);

        if (commandsParser != null) {
            commandsParser.perform(player);
        }
    }

}
