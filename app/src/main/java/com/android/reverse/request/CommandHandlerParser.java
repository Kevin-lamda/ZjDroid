package com.android.reverse.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.reverse.request.InvokeScriptCommandHandler.ScriptType;
import com.android.reverse.util.Logger;

public class CommandHandlerParser {

	private static String ACTION_NAME_KEY = "action";

	private static String ACTION_DUMP_DEXINFO = "dump_dexinfo";
	private static String ACTION_DUMP_HEAP = "dump_heap";

	private static String ACTION_DUMP_DEXCLASS = "dump_class";
	private static String PARAM_MCOOKIE_DUMPDEXCLASS = "mCookie";

	private static String ACTION_DUMP_DEXFILE = "dump_dexfile";
	private static String ACTION_BACKSMALI_DEXFILE = "backsmali";
	private static String PARAM_MCOOKIE_DUMP_DEXFILE = "mCookie";

	private static String ACTION_DUMP_MEMERY = "dump_mem";
	private static String PARAM_START_DUMP_MEMERY = "start";
	private static String PARAM_LENGTH_DUMP_MEMERY = "length";

	private static String ACTION_INVOKE_SCRIPT = "invoke";
	private static String FILE_SCRIPT = "filepath";

	public static CommandHandler parserCommand(String cmd) {
		CommandHandler handler = null;
		try {
			JSONObject jsoncmd = new JSONObject(cmd);
			String action = jsoncmd.getString(ACTION_NAME_KEY);
			Logger.log("the cmd = " + action);
			if (ACTION_DUMP_DEXINFO.equals(action)) {
				handler = new DumpDexInfoCommandHandler();
			} else if (ACTION_DUMP_DEXFILE.equals(action)) {
				if (jsoncmd.has(PARAM_MCOOKIE_DUMP_DEXFILE)) {
					String mCookie = jsoncmd.getString(PARAM_MCOOKIE_DUMP_DEXFILE);
					handler = new DumpDexFileCommandHandler(mCookie);
				} else {
					Logger.log("please set the " + PARAM_MCOOKIE_DUMP_DEXFILE + " value");
				}
			} else if (ACTION_BACKSMALI_DEXFILE.equals(action)) {
				if (jsoncmd.has(PARAM_MCOOKIE_DUMP_DEXFILE)) {
					String mCookie = jsoncmd.getString(PARAM_MCOOKIE_DUMP_DEXFILE);
					handler = new BackSmaliCommandHandler(mCookie);
				} else {
					Logger.log("please set the " + PARAM_MCOOKIE_DUMP_DEXFILE + " value");
				}
			} else if (ACTION_DUMP_DEXCLASS.equals(action)) {// TODO
				if (jsoncmd.has(PARAM_MCOOKIE_DUMPDEXCLASS)) {
					String mCookie = jsoncmd.getString(PARAM_MCOOKIE_DUMPDEXCLASS);
					handler = new DumpClassCommandHandler(mCookie);
				} else {
					Logger.log("please set the " + PARAM_MCOOKIE_DUMPDEXCLASS + " value");
				}
			} else if (ACTION_DUMP_HEAP.equals(action)) {
				handler = new DumpHeapCommandHandler();
			} else if (ACTION_INVOKE_SCRIPT.equals(action)) {
				if (jsoncmd.has(FILE_SCRIPT)) {
					String filepath = jsoncmd.getString(FILE_SCRIPT);
					handler = new InvokeScriptCommandHandler(filepath, ScriptType.FILETYPE);
				} else {
					Logger.log("please set the " + FILE_SCRIPT);
				}

			} else if (ACTION_DUMP_MEMERY.equals(action)) {
				long start = jsoncmd.getLong(PARAM_START_DUMP_MEMERY);
				int length = jsoncmd.getInt(PARAM_LENGTH_DUMP_MEMERY);
				handler = new DumpMemCommandHandler(start, length);
			} else {
				Logger.log(action + " cmd is invalid! ");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return handler;
	}

}
