package mzsJVM.Event;

import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.*;

@SuppressWarnings("UnusedDeclaration")
public class Area_OnEnter implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        if (NWScript.getTag(objSelf).equals("mzs2oocstart")) return;

        NWObject oPC = NWScript.getEnteringObject();
        if (NWScript.getIsPC(oPC))
        {
            applySanctuaryEffects(oPC);
            // This is added in to make sure that a PC can search after crashing out of NWN.
            NWScript.setLocalInt(oPC, "noSearch", 0);

            // Shows the area to the PC if they have an item
            // with the tag 'mzs2_area_map'
            if (NWScript.getItemPossessedBy(oPC, "mzs2_area_map") != NWObject.INVALID &&
                    !NWScript.getIsAreaInterior(objSelf))
            {
                NWScript.exploreAreaForPlayer(objSelf, oPC, true);
            }
        }
    }

    private void applySanctuaryEffects(NWObject oPC)
    {
        NWEffect eSafe = NWScript.effectSanctuary(99);
        NWEffect eSlashing = NWScript.effectDamageResistance(DamageType.SLASHING, 100, DamagePower.NORMAL);
        NWEffect eBludgeoning = NWScript.effectDamageResistance(DamageType.BLUDGEONING, 100, DamagePower.NORMAL);
        NWEffect ePiercing = NWScript.effectDamageResistance(DamageType.PIERCING, 100, DamagePower.NORMAL);
        NWEffect eArmor = NWScript.effectACIncrease(30, Ac.DODGE_BONUS, Ac.VS_DAMAGE_TYPE_ALL);
        NWEffect eConceal = NWScript.effectConcealment(100, Miss.CHANCE_TYPE_NORMAL);

        NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, eSafe, oPC, 15.0f);
        NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, eSlashing, oPC, 15.0f);
        NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, eBludgeoning, oPC, 15.0f);
        NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, ePiercing, oPC, 15.0f);
        NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, eArmor, oPC, 15.0f);
        NWScript.applyEffectToObject(Duration.TYPE_TEMPORARY, eConceal, oPC, 15.0f);

    }

}
