package mzsJVM;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("UnusedDeclaration")
public class Module_OnClientLeaving implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {

        NWObject oDatabase = NWScript.getItemPossessedBy(objSelf, Constants.PCDatabaseTag);
        int iHP = NWScript.getCurrentHitPoints(objSelf);
        NWScript.setLocalInt(oDatabase, "PHP_STORED_HP", iHP);
    }
}
