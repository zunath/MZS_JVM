package mzsJVM;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Duration;

@SuppressWarnings("unused")
public class Player_OnHeartbeat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {

        if (!NWScript.getIsPC(objSelf) || NWScript.getIsDM(objSelf)) {return;}

        NWObject oArea = NWScript.getArea(objSelf);

        if(NWScript.getLocalInt(oArea, "IGNORE_PC_TIMERS") == 1)
        {
            return;
        }

        NWObject oDatabase = NWScript.getItemPossessedBy(objSelf, Constants.PCDatabaseTag);
        int iHP = NWScript.getCurrentHitPoints(objSelf);
        int iMaxHP = NWScript.getMaxHitPoints(objSelf);
        int iHungerCountDown = NWScript.getLocalInt(oDatabase, "HUNGER_COUNT_DOWN");
        int iThirstCountDown = NWScript.getLocalInt(oDatabase, "THIRST_COUNT_DOWN");
        int iCurrentHunger = NWScript.getLocalInt(oDatabase, "CURRENT_HUNGER");
        int iCurrentThirst = NWScript.getLocalInt(oDatabase, "CURRENT_THIRST");

        // HP Regeneration
        // Checks player's HP regeneration bonuses and regenerates their mana.
        // Player restores HP one time a minute
        if (iHP < iMaxHP)
        {
            int iHPRegen = NWScript.getLocalInt(oDatabase, "HP_REGEN");
            int iHealTime = NWScript.getLocalInt(oDatabase, "HEAL_TIME");

            if (iHealTime <= 9)
            {
                iHealTime = iHealTime + 1;
                NWScript.setLocalInt(oDatabase, "HEAL_TIME", iHealTime);
            }
            else if (iHealTime >= 10)
            {
                if (NWScript.getDistanceBetween(objSelf, NWScript.getNearestObjectByTag("heal_station", objSelf, 0)) <= 6.0 && NWScript.getNearestObjectByTag("rotd_campfire", objSelf, 0) != NWObject.INVALID)
                {iHPRegen = iHPRegen + 2;}
                NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectHeal(iHPRegen), objSelf, 0.0f);
                NWScript.setLocalInt(oDatabase, "HEAL_TIME", 0);
                //SendMessageToPC(oPC, "HP Regen: " + IntToString(iHPRegen));
            }
        }

        // Food and thirst
        iHungerCountDown = iHungerCountDown - 1;
        iThirstCountDown = iThirstCountDown - 1;
        // If the hunger count down timer is 0, make the player a little more hungry
        // and reset the timer
        if (iHungerCountDown == 0)
        {
            //SendMessageToPC(oPC, "Decreasing Hunger"); // DEBUG
            iCurrentHunger = iCurrentHunger - 1;
            NWScript.setLocalInt(oDatabase, "CURRENT_HUNGER", iCurrentHunger);
            iHungerCountDown = 60;
            if (iCurrentHunger == 70 || iCurrentHunger == 60 || iCurrentHunger == 50 || iCurrentHunger == 40)
            {NWScript.floatingTextStringOnCreature("===================You are hungry.===================", objSelf, false);}
            else if (iCurrentHunger == 30 || iCurrentHunger == 20 || iCurrentHunger == 10)
            {NWScript.floatingTextStringOnCreature("===================You are starving!===================", objSelf, false);}
        }
        // If the thirst count down timer is 0, make the player a little more thirsty
        // and reset the timer
        if (iThirstCountDown == 0)
        {
            //SendMessageToPC(oPC, "Decreasing thirst"); // DEBUG
            iCurrentThirst = iCurrentThirst - 1;
            NWScript.setLocalInt(oDatabase, "CURRENT_THIRST", iCurrentThirst);
            iThirstCountDown = 40;
            if (iCurrentThirst == 70 || iCurrentThirst == 60 || iCurrentThirst == 50 || iCurrentThirst == 40)
            {NWScript.floatingTextStringOnCreature("===================You are thirsty.===================", objSelf, false);}
            else if (iCurrentThirst == 30 || iCurrentThirst == 20 || iCurrentThirst == 10)
            {NWScript.floatingTextStringOnCreature("===================You are dying of thirst!===================", objSelf, false);}
        }
        // Player's hunger or thirst level reached 0, kill them!
        if (iCurrentHunger == 0 || iCurrentThirst == 0)
        {
            NWScript.applyEffectToObject(Duration.TYPE_INSTANT, NWScript.effectDeath(false, true), objSelf, 0.0f);
            NWScript.floatingTextStringOnCreature("You have died due to lack of food or water!", objSelf, true);
        }

        // Now update their database item
        NWScript.setLocalInt(oDatabase, "THIRST_COUNT_DOWN", iThirstCountDown);
        NWScript.setLocalInt(oDatabase, "HUNGER_COUNT_DOWN", iHungerCountDown);

        // Section to prevent too many skill ups in a short amount of time
        // Ignore the variable name, I didn't want to create a new one just for
        // this section.
        iCurrentHunger = NWScript.getLocalInt(oDatabase, "SPELL_SKILL_COUNT_DOWN");
        if (iCurrentHunger > 0)
        {
            iCurrentHunger = iCurrentHunger - 1;
            NWScript.setLocalInt(oDatabase, "SPELL_SKILL_COUNT_DOWN", iCurrentHunger);
        }

        // Idle count down checks
        NWScript.executeScript("idle_on_pctick", objSelf);
    }

}
