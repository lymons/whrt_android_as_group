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
import android.util.Log;


import com.mieasy.whrt_app_android_4.R;
import com.mieasy.whrt_app_android_4.nfc.tech.Iso7816;
import com.mieasy.whrt_app_android_4.nfc.tech.Util;

import java.util.ArrayList;

final class WuhanTong extends PbocCard {
	private final static int SFI_INFO = 5;
	private final static int SFI_SERL = 10;

	private final static byte[] DFN_SRV = { (byte) 0x41, (byte) 0x50,
			(byte) 0x31, (byte) 0x2E, (byte) 0x57, (byte) 0x48, (byte) 0x43,
			(byte) 0x54, (byte) 0x43, };
	private static final String TAG ="WuhanTong";

	private WuhanTong(Iso7816.Tag tag, Resources res) {
		super(tag);
		name = res.getString(R.string.name_wht);
	}

	@SuppressWarnings("unchecked")
	final static WuhanTong load(Iso7816.Tag tag, Resources res) {

		/*--------------------------------------------------------------*/
		// select PSF (1PAY.SYS.DDF01)
		/*--------------------------------------------------------------*/
		Log.e(TAG,"武汉通tag.selectByName(DFN_PSE).isOkey():"+tag.selectByName(DFN_PSE).isOkey());
		if (tag.selectByName(DFN_PSE).isOkey()) {

			Iso7816.Response SERL, INFO, CASH;

			/*--------------------------------------------------------------*/
			// read card info file, binary (5, 10)
			/*--------------------------------------------------------------*/
			if (!(SERL = tag.readBinary(SFI_SERL)).isOkey())
				return null;

			if (!(INFO = tag.readBinary(SFI_INFO)).isOkey())
				return null;

			CASH = tag.getBalance(true);

			/*--------------------------------------------------------------*/
			// select Main Application
			/*--------------------------------------------------------------*/
			if (tag.selectByName(DFN_SRV).isOkey()) {
				Log.e(TAG,"------------------1");

				/*--------------------------------------------------------------*/
				// read balance
				/*--------------------------------------------------------------*/
				// 解决查不出余额的卡片信息
//				if (!CASH.isOkey()){
//					Log.e(TAG,"------------------2");

				CASH = tag.getBalance(true);
//				}
				/*--------------------------------------------------------------*/
				// read log file, record (24)
				/*--------------------------------------------------------------*/
				ArrayList<byte[]> LOG = readLog(tag, SFI_LOG);

				/*--------------------------------------------------------------*/
				// build result string
				/*--------------------------------------------------------------*/
				final WuhanTong ret = new WuhanTong(tag, res);
				//余额
				ret.parseBalance(CASH);
				//SERL 卡号   INFO 信息
				ret.parseInfo(SERL, INFO);
				//消费记录
				ret.parseLog(LOG);
				return ret;
			}
		}
		Log.e(TAG,"数据为空");

		return null;
	}

	private void parseInfo(Iso7816.Response sn, Iso7816.Response info) {
		if (sn.size() < 27 || info.size() < 27) {
			serl = version = date = count = null;
			return;
		}

		final byte[] d = info.getBytes();
		serl = Util.toHexString(sn.getBytes(), 0, 5);
		Log.e(TAG,"serl的值为:"+serl);
		version = String.format("%02d", d[24]);
		date = String.format("%02X%02X.%02X.%02X - %02X%02X.%02X.%02X", d[20],
				d[21], d[22], d[23], d[16], d[17], d[18], d[19]);
		count = null;
	}

}
