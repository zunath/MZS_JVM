package mzsJVM.Event;

import mzsJVM.IScriptEventHandler;
import mzsJVM.NWNX.AreaScript;
import mzsJVM.NWNX.NWNX_Funcs;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class Module_OnLoad implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {

		NWScript.executeScript("x2_mod_def_load", objSelf);  // Bioware Default
		NWScript.executeScript("fky_chat_modload", objSelf); // SimTools & NWNX Initialization
		NWScript.executeScript("cnr_module_oml", objSelf);   // CNR Refinery System

		AddAreaEventHandlers();
	}

	private void AddAreaEventHandlers()
	{
		NWObject area = NWNX_Funcs.GetFirstArea();
		while(NWScript.getIsObjectValid(area))
		{
			String result = NWNX_Funcs.SetEventHandler(area, AreaScript.OnEnter, "area_on_enter");
			NWNX_Funcs.SetEventHandler(area, AreaScript.OnExit, "area_on_exit");
			NWNX_Funcs.SetEventHandler(area, AreaScript.OnHeartbeat, "area_on_heart");
			NWNX_Funcs.SetEventHandler(area, AreaScript.OnUserDefinedEvent, "area_on_userdef");

			area = NWNX_Funcs.GetNextArea();
		}
	}

}
