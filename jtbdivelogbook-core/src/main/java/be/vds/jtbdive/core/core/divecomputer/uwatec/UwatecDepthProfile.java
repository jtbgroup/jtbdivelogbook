package be.vds.jtbdive.core.core.divecomputer.uwatec;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: container for one depth profile of an Aladin log file
 * 
 * source: http://pakuro.is.sci.toho-u.ac.jp/aladin/protocol-e.html inspiration:
 * JDive Log 2.2
 * 
 * 
 * 
 * <br>
 * Offset Description <br>
 * ==================================================== <br>
 * 0 Constant 0xff (marks the beginning) <br>
 * 1--22 Information for decompression <br>
 * Note 1: Aladin Nitrox (not O2) has extra two bytes. This is <br>
 * turned out to be the following memory map shifts by two bytes. <br>
 * Note 2: Aladin O2 has extra three bytes. This is turned out <br>
 * to be the following memory map shifts by three bytes. <br>
 * 1: Ambient temperature when this dive starts (at 1.25m) <br>
 * [1] / 4 degrees (Celsius). <br>
 * Note: This value is not surface temperature nor <br>
 * air temperature. The measurement is done when <br>
 * this dive starts, so it is not reached to stable state. <br>
 * 2--3: Tissue 1 ([3]*256 + [2]) kidney <br>
 * 4--5: Tissue 2 ([5]*256 + [4]) stomach,<br>
 * bowels, liver, central nervous system <br>
 * 6--7: Tissue 3 ([7]*256 + [6]) central nervous system, liver, stomach, bowels <br>
 * 8--9: Tissue 4 ([9]*256 + [8]) skin <br>
 * 10--11: Tissue 5 ([11]*256 + [10]) skin, muscles, heart <br>
 * 12--13: Tissue 6 ([13]*256 + [12]) muscles <br>
 * 14--15: Tissue 7 ([15]*256 + [14]) muscles, joints, bones, fat <br>
 * 16--17: Tissue 8 ([17]*256 + [16]) fat, joints, bones, rest <br>
 * 18-- higher nibble of 19: <br>
 * Microbubble danger in the arterial circulation <br>
 * ([19] & 0xf0)*16 + [18] <br>
 * 0x000 - 0x010: Level 0 <br>
 * 0x011 - 0x080: Level 1 <br>
 * 0x081 - 0x100: Level 2 <br>
 * 0x101 - 0x280: Level 3 <br>
 * 0x281 - 0x480: Level 4 <br>
 * 0x481 - 0x700: Level 5 <br>
 * 0x701 - 0xa00: Level 6 <br>
 * 0xa01 - 0xfff: Level 7 <br>
 * lower nibble of 19 --20: <br>
 * Intrapulmonary right-left shunt: Micro bubbles in the venous <br>
 * circulation migrate to the lungs, where they collect in the <br>
 * capillaries and obstruct the exchange of gas, and this <br>
 * effect is termed. <br>
 * ([19] & 0x0f)*256 + [20] <br>
 * 21-- higher nibble of 22: <br>
 * Estimated skin cool at dive start (([22] & 0xf0)*16 + [21])/64 <br>
 * > 30.7 : Level 0 <br>
 * >= 28.0 : Level 1 <br>
 * >= 26.0 : Level 2 <br>
 * >= 24.0 : Level 3 <br>
 * >= 23.0 : Level 4 <br>
 * >= 22.0 : Level 5 <br>
 * >= 21.0 : Level 6 <br>
 * < 21.0 : Level 7 <br>
 * lower nibble of 22: Always zero <br>
 * (Begin Nitrox only <br>
 * 23: Two times of CNS O2 rest saturation percentage at dive <br>
 * start. Aladin shows this value in 5% units as <br>
 * floor(([23] + 4) / 10) * 5, <br>
 * where floor(x) means the maximum integer not exceeding <br>
 * x. For example if [23] = 56 (28 %) then Aladin shows it as <br>
 * 30 %. <br>
 * (Begin Aladin Nitrox (not O2) only <br>
 * 24: Upper nibble: Max ppO2 warning of this dive <br>
 * 0x0*: 1.20 (bar) <br>
 * 0x1*: 1.25 (bar) <br>
 * 0x2*: 1.30 (bar) <br>
 * 0x3*: 1.35 (bar) <br>
 * .... <br>
 * 0x8*: 1.60 (bar) <br>
 * .... (ppO2 value should not be set <br>
 * higher than 1.60 bar) <br>
 * 0xf*: 1.95 (bar) <br>
 * Lower nibble: Nitrox O2 mix <br>
 * 0x*0: 21% O2 <br>
 * 0x*1: 22% O2 <br>
 * 0x*2: 24% O2 <br>
 * 0x*3: 26% O2 <br>
 * .... <br>
 * 0x*f: 50% O2 <br>
 * End Nitrox (not O2) only) <br>
 * (Begin Aladin O2 only <br>
 * 24: Nitrox O2 mix [24] % <br>
 * 25: Upper nibble: <br>
 * bit7: higher bit of Work load (vvO2 max) of this dive <br>
 * bit6: lower bit <br>
 * 3: very high (2.50 l/min O2) <br>
 * 2: high (2.25 l/min O2) <br>
 * 1: medium (2.00 l/min O2) <br>
 * 0: low (1.75 l/min O2) <br>
 * bit5: higher bit of SCR sensitivity of this dive <br>
 * bit4: lower bit <br>
 * 3: sensitive (1) <br>
 * 2: | (0) <br>
 * 1: | (-1) <br>
 * 0: insensitive (-2) <br>
 * Lower nibble: Max pp02 warning of this dive <br>
 * 0x*0: 1.20 (bar) <br>
 * 0x*1: 1.25 (bar) <br>
 * 0x*2: 1.30 (bar) <br>
 * 0x*3: 1.35 (bar) <br>
 * .... <br>
 * 0x*8: 1.60 (bar) <br>
 * .... (ppO2 value should not be set <br>
 * higher than 1.60 bar) <br>
 * 0x*f: 1.95 (bar) <br>
 * End O2 only) <br>
 * End Nitrox only) <br>
 * 23-- Body of depth profile; <br>
 * a word (16 bits) data for depth + warnings in every 20 seconds, <br>
 * a byte (8 bits) data for decompression in every one minute. <br>
 * <br>
 * (*) <br>
 * 23--24 upper 10bits --- depth at 0:00:20 (hour:min:sec) <br>
 * lower 6bits --- warnings at 0:00:20 <br>
 * 25--26 upper 10bits --- depth at 0:00:40 <br>
 * lower 6bits --- warnings at 0:00:40 <br>
 * 27--28 upper 10bits --- depth at 0:01:00 <br>
 * lower 6bits --- warnings at 0:01:00 <br>
 * 29 decompression information at 0:01:00 <br>
 * (Aladin O2 has extra one byte, which represents 02 mix %, here) <br>
 * (repeat from above (*) to here as many times) <br>
 * <br>
 * A depth is stored as [upper 10bits] * 10 / 64 (m). <br>
 * For example, the depth at 0:00:20 can be calculated as <br>
 * (([23] * 256 + [24]) >> 6) * 10 / 64 (m). <br>
 * Each bit of warning (lower 6bits) is <br>
 * bit 5: transmit error of air pressure (always 1 unless Air series), <br>
 * bit 4: work too hard (Air series only), <br>
 * bit 3: ceiling violation of deco stop, <br>
 * bit 2: ascent too fast, <br>
 * bit 1: remaining bottom time too short; 5 min to reserved bar <br>
 * [0x7de] (default: 40 bar). (Air series only), <br>
 * bit 0: deco stop. <br>
 * <br>
 * A decompression information of every minute is: <br>
 * Level of physical effort (min 0 -- max 7) <br>
 * ("Air" computer estimates it from air consumption; Uwatec applied <br>
 * this estimation procedure for the patent. Other computer sets <br>
 * it to Level one in underwater and to zero when surfacing.) <br>
 * bit 6: higher bit <br>
 * bit 5: | <br>
 * bit 4: lower bit <br>
 * Estimated skin cooling <br>
 * bit 7: cold level decrement by one <br>
 * bit 3: cold level increment by one <br>
 * Level of micro bubble danger in the arterial circulation <br>
 * (min 0 -- max 7; if this value is not less than 2 then Aladin <br>
 * enters in "Atn" mode) <br>
 * bit 2: higher bit <br>
 * bit 1: | <br>
 * bit 0: lower bit
 * 
 * @author Andr&eacute; Schenk
 * @author Gautier Vanderslyen
 **/

public class UwatecDepthProfile {
	private final Float startTemperature;
	private final List<UwatecDepthProfileEntry> depthProfileEntries = new ArrayList<UwatecDepthProfileEntry>();
	private int cnsBeforeDive;
	private double[] tissuesSaturation = new double[8];
	private double[] microBubbles = new double[2];
	private double estimatedSkinCoolLevel;
	private int nitroxMix;
	private int maxPPO2;

	public UwatecDepthProfile(int[] bytes, UwatecSettings settings) {
		if ((bytes == null) || (bytes.length < 1)) {
			throw new IllegalArgumentException(
					"byte array for depth profile is invalid");
		}
		if (settings == null) {
			throw new IllegalArgumentException(
					"need settings to generate log entry");
		}

		startTemperature = new Float((bytes[1] * 25) / 100.0);

		estimatedSkinCoolLevel = ((bytes[22] & 0xf0) * 16 + bytes[21]) / 64;

		cnsBeforeDive = ((bytes[23] + 4) / 10) * 5;

		fillTissuesSaturation(bytes);
		fillMicroBubbles(bytes);

		// skip information for decompression
		int index = 23;

		if ((settings.getAladinType() == UwatecConstants.ALADIN_AIR_Z_NITROX)
				|| (settings.getAladinType() == UwatecConstants.ALADIN_PRO_ULTRA)) {
			addNitroxMix(bytes);
			addMaxPPO2(bytes);
			// Aladin Nitrox (not O2)
			index += 2;
		} else if (settings.getAladinType() == UwatecConstants.ALADIN_AIR_Z_O2) {
			// Aladin O2
			index += 3;
		}

		// read body of depth profile
		while (index < bytes.length - 7) {
			int[] entryBuffer = new int[7];

			System.arraycopy(bytes, index, entryBuffer, 0, entryBuffer.length);
			depthProfileEntries.add(new UwatecDepthProfileEntry(entryBuffer));
			index += entryBuffer.length;

			// Aladin O2 has extra one byte, which represents 02 mix %
			if (settings.getAladinType() == 0xa4) {
				index++;
			}
		}
	}

	private void addMaxPPO2(int[] bytes) {
		maxPPO2 = (bytes[24] & 0xf0) / 16;
	}

	private void addNitroxMix(int[] bytes) {
		nitroxMix = (bytes[24] & 0x0f);
	}

	private void fillMicroBubbles(int[] bytes) {
		microBubbles[0] = (bytes[19] & 0xf0) * 16 + bytes[18];
		microBubbles[1] = (bytes[19] & 0x0f) * 256 + bytes[20];
	}

	private void fillTissuesSaturation(int[] bytes) {
		tissuesSaturation[0] = bytes[3] * 256 + bytes[2];
		tissuesSaturation[1] = bytes[5] * 256 + bytes[4];
		tissuesSaturation[2] = bytes[7] * 256 + bytes[6];
		tissuesSaturation[3] = bytes[9] * 256 + bytes[8];
		tissuesSaturation[4] = bytes[11] * 256 + bytes[10];
		tissuesSaturation[5] = bytes[13] * 256 + bytes[12];
		tissuesSaturation[6] = bytes[15] * 256 + bytes[14];
		tissuesSaturation[7] = bytes[17] * 256 + bytes[16];
	}

	public List<UwatecDepthProfileEntry> getProfileEntries() {
		return depthProfileEntries;
	}

	public String toString() {
		return "startTemperature: " + startTemperature + "\n"
				+ "depthProfileEntries: " + depthProfileEntries + "\n";
	}

	public int getCNSBeforeDive() {
		return cnsBeforeDive;
	}

	public double[] getTissuesSaturation() {
		return tissuesSaturation;
	}

	public double[] getMicroBubbles() {
		return microBubbles;
	}

	public double getEstimatedSkinCoolLevel() {
		return estimatedSkinCoolLevel;
	}

	public int getNitroxMix() {
		return nitroxMix;
	}

	public int getMaxPPO2() {
		return maxPPO2;
	}
}
