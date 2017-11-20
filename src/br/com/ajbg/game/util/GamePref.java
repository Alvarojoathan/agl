package br.com.ajbg.game.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class GamePref {
	private static Hashtable prefs = new Hashtable ();

	public static final int getPref (String name) {
		Object pref;
		if ((pref = prefs.get(name)) != null)
			return ((Integer) pref).intValue();
		else
			return 0;
	}

	public static final int getPref (String name, int defaul) {
		Object pref;
		if ((pref = prefs.get(name)) != null)
			return ((Integer) pref).intValue();
		else
			return defaul;
	}

	public static final void setPref (String name, int value) {
		prefs.put(name, new Integer (value));
		savePrefs (toByteArray());
	}

	private static final byte[] toByteArray () {
		ByteArrayOutputStream bout = new ByteArrayOutputStream ();
		DataOutputStream out = new DataOutputStream (bout);
		try {
			for (Enumeration e = prefs.keys(); e.hasMoreElements(); ) {
				String key = (String) e.nextElement();
				int value = ((Integer)prefs.get(key)).intValue();
				out.writeUTF(key);
				out.writeInt(value);
			}
		} catch (IOException e) { }
		return bout.toByteArray();
	}

	protected static final void deserialize (byte[] dado) {
		DataInputStream in = new DataInputStream (new ByteArrayInputStream (dado));
		try {
			while (in.available() > 0) {
				String key = in.readUTF();
				int value = in.readInt();
				prefs.put(key, new Integer (value));
			}
		} catch (IOException e) { }
	}

	public static void loadPrefs () {
		// TODO ler dados da persistencia e chamar deserialize()
	}

	protected static void savePrefs (byte[] data) {
		// TODO salvar dados na persistencia
	}
}
