package mzsJVM;

import org.nwnx.nwnx2.jvm.*;

public class Module_OnClientEnter implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWScript.executeScript("x3_mod_def_enter", objSelf);
    }
}
