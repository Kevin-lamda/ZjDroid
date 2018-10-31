package com.android.reverse.apimonitor;

import java.lang.reflect.Method;
import java.util.Iterator;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import com.android.reverse.hook.HookParam;
import com.android.reverse.util.Logger;
import com.android.reverse.util.RefInvoke;

public class ContextImplHook extends ApiMonitorHook{
	
	@Override
	public void startHook() {
		Method registerReceivermethod = RefInvoke.findMethodExact(
				"android.app.ContextImpl", ClassLoader.getSystemClassLoader(),
				"registerReceiver", BroadcastReceiver.class,IntentFilter.class);
		hookhelper.hookMethod(registerReceivermethod, new AbstractBahaviorHookCallBack() {
			
			@Override
			public void descParam(HookParam param) {
				Logger.log_behavior("Register BroatcastReceiver");
				Logger.log_behavior("The BroatcastReceiver ClassName = "+param.args[0].getClass().toString());
				if(param.args[1] != null){
				   String intentstr = descIntentFilter((IntentFilter) param.args[1]);
				   Logger.log_behavior("Intent Action = ["+intentstr+"]");
				}
			}
		});


		Method unRegisterReceivermethod = RefInvoke.findMethodExact(
				"android.app.ContextImpl", ClassLoader.getSystemClassLoader(),
				"unregisterReceiver", BroadcastReceiver.class);
		hookhelper.hookMethod(unRegisterReceivermethod, new AbstractBahaviorHookCallBack() {

			@Override
			public void descParam(HookParam param) {
				Logger.log_behavior("UnRegister BroatcastReceiver");
				Logger.log_behavior("The BroatcastReceiver ClassName = "+param.args[0].getClass().toString());
			}
		});

	}
	
	public String descIntentFilter(IntentFilter intentFilter){
		StringBuilder sb = new StringBuilder(); 
		Iterator<String> actions =intentFilter.actionsIterator();
		String action = null;
		while(actions.hasNext()){
			action = actions.next();
			sb.append(action+",");
		}
		return sb.toString();
		
	}

}
