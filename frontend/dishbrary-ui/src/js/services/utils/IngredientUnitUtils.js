import MenuItem from "@material-ui/core/MenuItem";
import React from "react";

const GRAM_BASED_UNIT_SELECTION = [
    <MenuItem key="gram" value="g">g</MenuItem>,
    <MenuItem key="dkg" value="dkg">dkg</MenuItem>,
    <MenuItem key="kg" value="kg">kg</MenuItem>
]

const MILLILITRE_BASED_UNIT_SELECTION = [
    <MenuItem key="ml" value="ml">ml</MenuItem>,
    <MenuItem key="dl" value="dl">dl</MenuItem>,
    <MenuItem key="l" value="l">l</MenuItem>
]

const PIECE_UNIT = <MenuItem key="db" value="db">db</MenuItem>;

export function getUnitSelectionByUnit(unit) {
    switch (unit) {
        case "GRAM":
        case "g":
        case "dkg":
        case "kg":
            return GRAM_BASED_UNIT_SELECTION;

        case "MILLILITRE":
        case "ml":
        case "dl":
        case "l":
            return MILLILITRE_BASED_UNIT_SELECTION;

        default:
            return PIECE_UNIT;
    }
}

export function convertUnitToRenderable(unit) {
    switch (unit) {
        case "GRAM":
            return "g";
        case "MILLILITRE":
            return "ml";
        case "PIECE" :
            return "db";
    }

    return unit;
}

export function convertRenderableUnitToUnit(renderableUnit) {
    switch (renderableUnit) {
        case "GRAM":
        case "g":
        case "dkg":
        case "kg":
            return "GRAM";

        case "MILLILITRE":
        case "ml":
        case "dl":
        case "l":
            return "MILLILITRE";

        default:
            return "PIECE";
    }
}