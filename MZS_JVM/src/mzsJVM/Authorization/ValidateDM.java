package mzsJVM.Authorization;

import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class ValidateDM implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {

        // TODO: Access database and check to see if objSelf (the player)
        //       is a DM.

        NWScript.setLocalInt(objSelf, "AUTH_IS_DM", 0);
    }
}
