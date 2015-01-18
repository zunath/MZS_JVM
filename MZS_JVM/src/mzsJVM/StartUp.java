package mzsJVM;
import org.nwnx.nwnx2.jvm.*;

public class StartUp {

	/**
	 * Called before any configured classes and methods
	 * are looked up in jni. You can use this to setup
	 * class pathes and load plugins/custom classes that
	 * are required to start up.
	*/
	@SuppressWarnings("unused")
	private static void setup() {
	}

	/**
	 * Called just before continuing startup inside NWNX.
	 * Use this to do your usual initialisation - do not
	 * use setup() for that.
	*/
	@SuppressWarnings("unused")
	private static void init() {
		setUpSchedulerListeners();

		/* Add some default handlers that don't do any
		 * custom wrapping at all.
		 */
		NWObject.registerObjectHandler(new NWObject.ObjectHandler() {
			public NWObject handleObjectClass(NWObject obj, boolean valid,
					int objectType, String resRef, String tag) {
				return obj;
			}
		});
		NWEffect.registerEffectHandler(new NWEffect.EffectHandler() {
			public NWEffect handleEffectClass(NWEffect eff) {
				return eff;
			}
		});
		NWItemProperty.registerItemPropertyHandler(new NWItemProperty.ItemPropertyHandler() {
			public NWItemProperty handleItemPropertyClass(NWItemProperty prp) {
				return prp;
			}
		});

	}

	private static void setUpSchedulerListeners()
	{
		Scheduler.addSchedulerListener(new Module_OnAcquireItem());
		Scheduler.addSchedulerListener(new Module_OnActivateItem());
		Scheduler.addSchedulerListener(new Module_OnClientEnter());
		Scheduler.addSchedulerListener(new Module_OnClientLeave());
		Scheduler.addSchedulerListener(new Module_OnCutsceneAbort());
		Scheduler.addSchedulerListener(new Module_OnHeartbeat());
		Scheduler.addSchedulerListener(new Module_OnLoad());
		Scheduler.addSchedulerListener(new Module_OnPlayerChat());
		Scheduler.addSchedulerListener(new Module_OnPlayerDeath());
		Scheduler.addSchedulerListener(new Module_OnPlayerDying());
		Scheduler.addSchedulerListener(new Module_OnPlayerEquipItem());
		Scheduler.addSchedulerListener(new Module_OnPlayerLevelUp());
		Scheduler.addSchedulerListener(new Module_OnPlayerRespawn());
		Scheduler.addSchedulerListener(new Module_OnPlayerRest());
		Scheduler.addSchedulerListener(new Module_OnPlayerUnEquipItem());
		Scheduler.addSchedulerListener(new Module_OnUnAcquireItem());
		Scheduler.addSchedulerListener(new Module_OnUserDefined());

		Scheduler.addSchedulerListener(new Test());
	}
	
	/**
	 * Called just before terminating the JVM. No NWN context
	 * is available. Not called on errors or crashes.
	 */
	@SuppressWarnings("unused")
	private static void shutdown() {
	}
}
