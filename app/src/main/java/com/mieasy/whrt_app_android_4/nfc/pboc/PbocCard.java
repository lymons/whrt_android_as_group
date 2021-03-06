/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package com.mieasy.whrt_app_android_4.nfc.pboc;

import android.content.res.Resources;
import android.nfc.tech.IsoDep;
import android.util.Log;
import android.widget.Toast;

import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.nfc.CardManager;
import com.mieasy.whrt_app_android_4.nfc.tech.Iso7816;
import com.mieasy.whrt_app_android_4.nfc.tech.Util;

import java.util.ArrayList;
import java.util.Arrays;

public class PbocCard {
	protected final static byte[] DFI_MF = { (byte) 0x3F, (byte) 0x00 };
	protected final static byte[] DFI_EP = { (byte) 0x10, (byte) 0x01 };

	protected final static byte[] DFN_PSE = { (byte) '1', (byte) 'P',
			(byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
			(byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F',
			(byte) '0', (byte) '1', };

	protected final static byte[] DFN_PXX = { (byte) 'P' };

	protected final static int MAX_LOG = 10;
	protected final static int SFI_EXTRA = 21;
	protected final static int SFI_LOG = 24;

	protected final static byte TRANS_CSU = 6;
	protected final static byte TRANS_CSU_CPX = 9;
	private static final String ATG ="PbocCard";

	protected String name;
	protected String id;
	protected String serl;
	protected String version;
	protected String date;
	protected String count;
	protected String cash;
	protected String log;

	public static String load(IsoDep tech, Resources res) {
		final Iso7816.Tag tag = new Iso7816.Tag(tech);

		tag.connect();

		PbocCard card = null;
		card = WuhanTong.load(tag, res);

//		do {
//			if ((card = WuhanTong.load(tag, res)) != null)
//				break;
//
//			if ((card = HardReader.load(tag, res)) != null)
//				break;
//
//		} while (false);

		tag.close();

//		Log.e("PbocCard",card.name);
		return (card != null) ? card.toString(res) : null;
	}

	protected PbocCard(Iso7816.Tag tag) {
		id = tag.getID().toString();
	}

	protected void parseInfo(Iso7816.Response data, int dec, boolean bigEndian) {
		if (!data.isOkey() || data.size() < 30) {
			serl = version = date = count = null;
			return;
		}

		final byte[] d = data.getBytes();
		if (dec < 1 || dec > 10) {
			serl = Util.toHexString(d, 10, 10);
		} else {
			final int sn = bigEndian ? Util.toIntR(d, 19, dec) : Util.toInt(d,
					20 - dec, dec);

			serl = String.format("%d", 0xFFFFFFFFL & sn);
		}

		version = (d[9] != 0) ? String.valueOf(d[9]) : null;
		date = String.format("%02X%02X.%02X.%02X - %02X%02X.%02X.%02X", d[20],
				d[21], d[22], d[23], d[24], d[25], d[26], d[27]);
		count = null;
	}

	protected static boolean addLog(final Iso7816.Response r,
			ArrayList<byte[]> l) {
		if (!r.isOkey())
			return false;

		final byte[] raw = r.getBytes();
		final int N = raw.length - 23;
		if (N < 0)
			return false;

		for (int s = 0, e = 0; s <= N; s = e) {
			l.add(Arrays.copyOfRange(raw, s, (e = s + 23)));
		}

		return true;
	}

	protected static ArrayList<byte[]> readLog(Iso7816.Tag tag, int sfi) {
		final ArrayList<byte[]> ret = new ArrayList<byte[]>(MAX_LOG);
		final Iso7816.Response rsp = tag.readRecord(sfi);
		if (rsp.isOkey()) {
			addLog(rsp, ret);
		} else {
			for (int i = 1; i <= MAX_LOG; ++i) {
				if (!addLog(tag.readRecord(sfi, i), ret))
					break;
			}
		}

		return ret;
	}

	protected void parseLog(ArrayList<byte[]>... logs) {
		final StringBuilder r = new StringBuilder();

		for (final ArrayList<byte[]> log : logs) {
			if (log == null)
				continue;

			if (r.length() > 0)
				r.append("<br />--------------");

			for (final byte[] v : log) {
				final int cash = Util.toInt(v, 5, 4);
				if (cash > 0) {
					r.append("<br />").append(
							String.format("%02X%02X.%02X.%02X %02X:%02X ",
									v[16], v[17], v[18], v[19], v[20], v[21],
									v[22]));

					final char t = (v[9] == TRANS_CSU || v[9] == TRANS_CSU_CPX) ? '-'
							: '+';

					// 三行空格
					r.append("   ").append(t).append(Util.toAmountString(cash / 100.0f));

					final int over = Util.toInt(v, 2, 3);
					if (over > 0)
						r.append(" [o:")
								.append(Util.toAmountString(over / 100.0f))
								.append(']');

					r.append(" [").append(Util.toHexString(v, 10, 6))
							.append(']');
				}
			}
		}

		this.log = r.toString();
	}

	protected void parseBalance(Iso7816.Response data) {
		if (!data.isOkey() || data.size() < 4) {
			cash = null;
			return;
		}
		Log.e(ATG,"data.getBytes()的值为:"+data.getBytes());
		Log.e(ATG,"data.isOkey()的值为:"+data.isOkey());
		int n = Util.toInt(data.getBytes(), 0, 4);
		Log.e(ATG,"1-n的值为:"+n);
		if (n > 100000 || n < -100000){
			n -= 0x80000000;
		}
		Log.e(ATG,"2-n的值为:"+n);

		cash = Util.toAmountString(n / 100.0f);
		Log.e(ATG,"cash的值为:"+cash);
	}

	protected String formatInfo(Resources res) {
		if (serl == null)
			return null;

		final StringBuilder r = new StringBuilder();

		if(serl != null){
		    r.append("<br/>").append(' ').append(serl);
		}

		if (version != null) {
			final String sv = res.getString(R.string.lab_ver);
			r.append("<br/>").append(' ').append(version);
		}

		if (date != null) {
			final String sd = res.getString(R.string.lab_date);
			r.append("<br/>").append(' ').append(date);
		}

		if (count != null) {
			final String so = res.getString(R.string.lab_op);
			final String st = res.getString(R.string.lab_op_time);
			r.append("<br/>").append(' ').append(count);
		}

		return r.toString();
	}

	protected String formatLog(Resources res) {
		if (log == null || log.length() < 1)
			return null;

		final StringBuilder ret = new StringBuilder();
		final String sl = res.getString(R.string.lab_log);
		ret.append("<br/>").append("<b>").append(sl).append("</b>");
		ret.append(log);

		return ret.toString();
	}

	protected String formatBalance(Resources res) {
		if (cash == null || cash.length() < 1)
			return null;

		final StringBuilder ret = new StringBuilder();

		final String s = res.getString(R.string.lab_balance);
		final String c = res.getString(R.string.lab_cur_cny);

		return ret.append("<br/>").append(' ').append(cash).toString();
	}

	protected String toString(Resources res) {
		final String info = formatInfo(res);
		final String hist = formatLog(res);
		final String cash = formatBalance(res);

		Log.e(ATG,"info:"+info+",hist:"+hist+",cash"+cash);

		return CardManager.buildResult(name, info, cash, hist);
	}

}
