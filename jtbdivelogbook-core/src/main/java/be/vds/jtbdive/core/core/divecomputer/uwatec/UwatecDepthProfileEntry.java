package be.vds.jtbdive.core.core.divecomputer.uwatec;

/**
 * Description: container for one depth profile entry of an Aladin log file
 * 
 * source: http://pakuro.is.sci.toho-u.ac.jp/aladin/protocol-e.html inspiration:
 * JDive Log 2.2
 * 
 * @author Andr&eacute; Schenk
 * @author Gautier Vanderslyen
 * 
 */

public class UwatecDepthProfileEntry {
	private final Float depth20;
	private final int warnings20;
	private final Float depth40;
	private final int warnings40;
	private final Float depth00;
	private final int warnings00;
	private final int decompression;

	public UwatecDepthProfileEntry(int[] bytes) {
		if ((bytes == null) || (bytes.length < 7)) {
			throw new IllegalArgumentException(
					"byte array for depth profile entry is invalid");
		}
		depth20 = new Float(
				(((bytes[0] * 256 + bytes[1]) >> 6) * 1000 / 64) / 100.0);
		warnings20 = bytes[1] & 0x3f;
		depth40 = new Float(
				(((bytes[2] * 256 + bytes[3]) >> 6) * 1000 / 64) / 100.0);
		warnings40 = bytes[3] & 0x3f;
		depth00 = new Float(
				(((bytes[4] * 256 + bytes[5]) >> 6) * 1000 / 64) / 100.0);
		warnings00 = bytes[5] & 0x3f;
		decompression = bytes[6] & 0x20;
	}

	public String toString() {
		return "depth20: " + depth20 + " m\n" + "warnings20: " + warnings20
				+ "\n" + "depth40: " + depth40 + " m\n" + "warnings40: "
				+ warnings40 + "\n" + "depth00: " + depth00 + " m\n"
				+ "warnings00: " + warnings00 + "\n" + "decompression: "
				+ decompression + "\n";
	}

	public Float getDepth20() {
		return depth20;
	}

	public Float getDepth40() {
		return depth40;
	}

	public Float getDepth00() {
		return depth00;
	}

	public int getWarnings00() {
		return warnings00;
	}

	public int getWarnings20() {
		return warnings20;
	}

	public int getWarnings40() {
		return warnings40;
	}

	public int getDecompression() {
		return decompression;
	}
}
