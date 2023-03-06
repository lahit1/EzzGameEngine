package com.Ezz.Game.Engine.luascript;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import org.luaj.vm2.lib.OneArgFunction;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import android.os.AsyncTask;
import com.Ezz.Game.Engine.Engine;
import android.app.Activity;

public class LNet extends TwoArgFunction {

	public static LNet Net = new LNet();
	public static ArrayList<Socket> sl = new ArrayList<Socket>();

	@Override
	public LuaValue call(LuaValue modname, LuaValue env) {
		LuaValue net = new LuaTable(0, 1);
		net.set("newConnection", new Connection());

		env.set("net", net);
		if (!env.get("package").isnil()) env.get("package").get("loaded").set("net", net);
		return net;
	}


    public class Connection extends TwoArgFunction {

		private ArrayList<String> queue = new ArrayList<String>();
		private String host;
		private int port;
		private Socket s;
		private LuaValue lt, osL, dataL, dcL, errL;

		@Override
		public LuaValue call(final LuaValue host, final LuaValue port) {
			this.host = host.tojstring();
			this.port = port.toint();
			LuaTable conn = new LuaTable(0, 3);
			conn.set("on", new on());
			conn.set("write", new write());
			conn.set("start", new start());
			return lt = conn;
		}

		public class on extends TwoArgFunction {

			@Override
			public LuaValue call(LuaValue p1, LuaValue p2) {
				String evn = p1.tojstring();
				if (evn.equals("start"))
					osL = p2;
				else if (evn.equals("data"))
					dataL = p2;
				else if (evn.equals("disconnect"))
					dcL = p2;
				else if (evn.equals("error"))
					errL = p2;
				return lt;
			}
		}

		public class write extends OneArgFunction {

			@Override
			public LuaValue call(LuaValue data) {
				queue.add(data.tojstring());
				return lt;
			}

		}

		public class start extends ZeroArgFunction {

			@Override
			public LuaValue call() {
				try {
					new AsyncTask() {

						@Override
						public Object doInBackground(Object[] args) {
							try {
								s = new Socket(InetAddress.getByName(host), port);
								if (!s.isConnected())
									return null;
								sl.add(s);
								if (osL != null && osL != LuaValue.NIL)
									((Activity)Engine.context).runOnUiThread(new Runnable() {

											@Override
											public void run() {
												osL.call();
											}
										});
								InputStream in = s.getInputStream();
								OutputStream out = s.getOutputStream();

								while (s.isConnected()) {
									int av = in.available();
									if (av > 0) {
										final byte[] buff = new byte[av];
										in.read(buff);
										if (dataL != null && dataL != LuaValue.NIL)
											((Activity)Engine.context).runOnUiThread(new Runnable() {
													@Override
													public void run() {
														dataL.call(valueOf(buff));
													}
												});
									}
									while (queue.size() > 0)
										out.write(queue.remove(0).getBytes());
								}
								sl.remove(s);
								if (dcL != null && dcL != LuaValue.NIL)
									((Activity)Engine.context).runOnUiThread(new Runnable() {
											@Override
											public void run() {
												dcL.call();
											}
										});
							} catch (final Exception e) {
								if (errL != null && errL != LuaValue.NIL)
									((Activity)Engine.context).runOnUiThread(new Runnable() {
											@Override
											public void run() {
												errL.call(valueOf(e.getMessage()));
											}
										});
							}
							return null;
						}

					}.execute();
				} catch (Exception e) {
					if (errL != null && errL != LuaValue.NIL)
						errL.call(valueOf(e.getMessage()));
				}
				return lt;
			}

		}
	}
}
