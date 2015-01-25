package mzsJVM.Event;
import mzsJVM.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.*;
import org.nwnx.nwnx2.jvm.constants.Weather;

@SuppressWarnings("unused")
public class Module_OnHeartbeat implements IScriptEventHandler {
	@Override
	public void runScript(NWObject objSelf) {

		if (NWScript.getLocalInt(NWObject.MODULE, "sWeathersChange") == NWScript.getTimeHour()) return;

		NWScript.deleteLocalInt(NWObject.MODULE, "sWeathersChange");
		NWScript.setLocalInt(NWObject.MODULE, "sWeathersChange", NWScript.getTimeHour());

		if (NWScript.random(100) <= 30) // A percentile roll. DEFAULT: 30%.
		{
			NWScript.setWeather(NWObject.MODULE, Weather.RAIN);
		}
		else
		{
			NWScript.setWeather(NWObject.MODULE, Weather.CLEAR);
		}
	}
}

