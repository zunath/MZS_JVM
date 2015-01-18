package mzsJVM;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.SchedulerListener;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class EventReceiver implements SchedulerListener {
    public void postFlushQueues(int remainingTokens) {}
    public void missedToken(NWObject objSelf, String token) {}
    public void context(NWObject objSelf) {}

    public void event(NWObject objSelf, String event) {

        // Try to locate a matching class name based on the event passed in from NWN JVM_EVENT call.
        try {
            Class scriptClass = Class.forName("mzsJVM." + event);
            IScriptEventHandler script = (IScriptEventHandler)scriptClass.newInstance();
            script.runScript(objSelf);
        }
        catch(Exception ex) {
            NWScript.writeTimestampedLogEntry("EventReciever was unable to execute class method: mzsJVM." + event + " .runScript()");
        }
    }
}
