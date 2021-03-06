/*
 * Copyright (C) 2020  Kikisito (Kyllian)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.kikisito.goldenheads.enums;

import com.github.kikisito.goldenheads.Utils;
import org.bukkit.configuration.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum GHConfig {
    // Notes
    GOLDENHEADS_NAME("goldenheads.display-name", "&eGolden Head"),
    GOLDENHEADS_LORE("goldenheads.lore", Collections.singletonList("&eIs this... an improved golden apple?")),
    GOLDENHEADS_MATERIAL("goldenheads.material", "PLAYER_HEAD"),
    GOLDENHEADS_SKULL_OWNER("goldenheads.skull-owner", "57a8704d-b3f4-4c8f-bea0-64675011fe7b"),
    // Format: POTION|DURATION IN SECONDS|LEVEL
    GOLDENHEADS_POTION_EFFECTS("goldenheads.potion-effects", Arrays.asList("ABSORPTION|120|1", "REGENERATION|5|2")),
    DROP_PLAYER_HEAD_ON_DEATH("drop-player-head-on-death", true);


    private static Configuration config;
    private final Object value;
    private final Object def;

    GHConfig(String value, String def) {
        this.value = value;
        this.def = def;
    }

    GHConfig(String value, int def){
        this.value = value;
        this.def = def;
    }

    GHConfig(String value, boolean def){
        this.value = value;
        this.def = def;
    }

    GHConfig(String value, List<String> def){
        this.value = value;
        this.def = def;
    }

    public String getString(){
        return Utils.parseMessage(config.getString((String) this.value, (String) this.def));
    }

    public List<String> getList(){ return config.getStringList((String) this.value); }

    public boolean getBoolean(){
        return config.getBoolean((String) this.value, (boolean) this.def);
    }

    public static void setConfig(Configuration config){
        GHConfig.config = config;
    }
}