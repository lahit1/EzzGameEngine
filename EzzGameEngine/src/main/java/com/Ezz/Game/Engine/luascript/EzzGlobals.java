package com.Ezz.Game.Engine.luascript;
import com.Ezz.Game.Engine.Engine;
import com.Ezz.Game.Engine.luascript.LLog;
import com.Ezz.Game.Engine.luascript.LNet;
import com.Ezz.Game.Engine.math.Vector;
import java.util.StringJoiner;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;
import org.luaj.vm2.lib.jse.JseStringLib;
import com.Ezz.Game.Engine.Universe;
import com.Ezz.Game.Engine.Input;

public final class EzzGlobals {

	public final static LuaValue
	lcVector = CoerceJavaToLua.coerce(Vector.class),
	lcInput = CoerceJavaToLua.coerce(Input.class);

    public static Globals standardGlobals() {
		final Globals jsg = new Globals();
		jsg.load(new JseBaseLib());
		jsg.load(new PackageLib());
		jsg.load(new Bit32Lib());
		jsg.load(new TableLib());
		jsg.load(new JseStringLib());
		jsg.load(new CoroutineLib());
		jsg.load(new JseMathLib());
		jsg.load(new JseIoLib());
		jsg.load(new JseOsLib());

		jsg.set("require", new VarArgFunction() {
				@Override
				public Varargs invoke(Varargs args) {
					for (int i=1; i <= args.narg(); i++)
						if (args.arg(i).tojstring().equalsIgnoreCase("ezz")) {
							jsg.set("Vector", lcVector);
							jsg.set("Input", lcInput);
							jsg.set("Assets", Engine.luaAssets);
							jsg.load(LNet.Net);
							jsg.load(LLog.LOG);
							break;
						}
						else if (args.arg(i).tojstring().equalsIgnoreCase("ezz.vector"))
							jsg.set("Vector", lcVector);
						else if (args.arg(i).tojstring().equalsIgnoreCase("ezz.input"))
							jsg.set("Input", lcInput);
						else if (args.arg(i).tojstring().equalsIgnoreCase("ezz.assets"))
							jsg.set("Assets", Engine.luaAssets);
						else if (args.arg(i).tojstring().equalsIgnoreCase("ezz.net"))
							jsg.load(LNet.Net);
						else if (args.arg(i).tojstring().equalsIgnoreCase("ezz.log"))
							jsg.load(LLog.LOG);

					return args;
				}
			});
			
		jsg.set("startUniverse", new OneArgFunction() {
				@Override
				public LuaValue call(LuaValue p1) {
					Engine.startUniverse(p1.tojstring());
					return LuaValue.NIL;
				}
			});
		LoadState.install(jsg);
		LuaC.install(jsg);
		return jsg;
	}

}
