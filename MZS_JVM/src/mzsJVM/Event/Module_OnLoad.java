package mzsJVM.Event;

import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class Module_OnLoad implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {

		NWScript.executeScript("x2_mod_def_load", objSelf);  // Bioware Default
		NWScript.executeScript("fky_chat_modload", objSelf); // SimTools & NWNX Initialization
		NWScript.executeScript("cnr_module_oml", objSelf);   // CNR Refinery System

	}
}
