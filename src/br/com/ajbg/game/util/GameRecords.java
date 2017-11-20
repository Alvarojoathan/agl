package br.com.ajbg.game.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

public class GameRecords {
	private static GameRecords instance;

	public int max;
	private Hashtable records;

	public static final GameRecords getInstance (int max) {
		if (instance == null)
			instance = new GameRecords (max);
		return instance;
	}

	private GameRecords (int max) {
		this.records = new Hashtable ();
		byte[] records = loadRecord();
		if (records == null)
			this.max = max;
		else {
			DataInputStream in = new DataInputStream (new ByteArrayInputStream (records));
			try {
				this.max = in.readInt();
				int[] value = new int[this.max];
				while (in.available() > 0) {
					String key = in.readUTF();
					for (int i=0; i<this.max; i++)
						value[i] = in.readInt();
					this.records.put(key, value);
				}
			} catch (IOException e) { }
		}
	}

	public final int[] getRecords (String level) {
		Object rec;
		if ((rec=records.get(level)) != null)
			return (int[]) rec;
		else
			return new int[max];
	}

	public final void setRecord (String level, int points) {
		Object rec;
		if ((rec=records.get(level)) != null) {
			int[] recs = (int[]) rec;
			for (int i=0; i<max; i++) {
				if (points > recs[i]) {
					for (int j=max-1; j>i; j--)
						recs[j] = recs[j-1];
					recs[i] = points;
				}
			}
		} else {
			int[] recs = new int[max];
			recs[0] = points;
			records.put(level, recs);
		}
		saveRecord(toByteArray());
	}

	private final byte[] toByteArray () {
		ByteArrayOutputStream bout = new ByteArrayOutputStream ();
		DataOutputStream out = new DataOutputStream (bout);
		try {
			out.writeInt(max);
			for (Enumeration e = records.keys() ; e.hasMoreElements() ;) {
				String key = (String) e.nextElement();
				int[] values = (int[]) records.get(key);
				out.writeUTF(key);
				for (int i=0; i<max; i++)
					out.writeInt(values[i]);
			}
		} catch (IOException e) { }
		return bout.toByteArray();
	}

	protected byte[] loadRecord () {
		// TODO ler dados da persistencia e retornar
		return null;
	}

	protected void saveRecord (byte[] data) {
		// TODO salvar dados na persistencia
	}
}
