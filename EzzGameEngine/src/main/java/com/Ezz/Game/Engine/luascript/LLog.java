package com.Ezz.Game.Engine.luascript;

import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.Varargs;
import com.Ezz.Game.Engine.Engine;
import org.luaj.vm2.lib.OneArgFunction;
import java.util.StringJoiner;

public class LLog extends TwoArgFunction {

	public static LLog LOG = new LLog();

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaTable cnsl = new LuaTable(0, 1);
		cnsl.set("m", new M());
		cnsl.set("w", new W());
		cnsl.set("e", new E());
		env.set("log", cnsl);
		if (!env.get("package").isnil()) env.get("package").get("loaded").set("log", cnsl);
		return cnsl;
	}

	public class M extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			StringJoiner jn = new StringJoiner(" ");
			for(int i=1; i <= args.narg(); i++)
				jn.add(args.arg(i).tojstring());
			Engine.logListener.message(jn.toString());
			return args;
		}
	}

	public class E extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			StringJoiner jn = new StringJoiner(" ");
			for(int i=1; i <= args.narg(); i++)
				jn.add(args.arg(i).tojstring());
			Engine.logListener.error(jn.toString());
			return args;
		}
	}
	
	public class W extends VarArgFunction {

		@Override
		public Varargs invoke(Varargs args) {
			StringJoiner jn = new StringJoiner(" ");
			for(int i=1; i <= args.narg(); i++)
				jn.add(args.arg(i).tojstring());
			Engine.logListener.warn(jn.toString());
			return args;
		}
	}
}
