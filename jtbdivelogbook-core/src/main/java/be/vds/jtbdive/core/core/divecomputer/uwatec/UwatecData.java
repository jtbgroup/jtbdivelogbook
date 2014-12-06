/*
 * Project: JDiveLog: A Dive Logbook written in Java
 * File: AladinData.java
 * 
 * @author Andr&eacute; Schenk <andre@melior.s.bawue.de>
 * 
 * This file is part of JDiveLog.
 * JDiveLog is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * JDiveLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JDiveLog; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package be.vds.jtbdive.core.core.divecomputer.uwatec;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: container for the contents of an Aladin log file
 * 
 * For further information see <a href="http://pakuro.is.sci.toho-u.ac.jp/aladin/protocol-e.html">Protocol and Data Structure of Uwatec Aladin Dive Computers</a>
 * @author Andr&eacute; Schenk
 * @version $Revision: 1.3 $
 */
public class UwatecData
{
    private List<UwatecDepthProfile> depthProfiles = new ArrayList<UwatecDepthProfile> ();
    private List<UwatecLogEntry>     logbook       = new ArrayList<UwatecLogEntry> ();
    private UwatecSettings           settings      = null;
    private UwatecCurrentStatus      currentStatus = null;

    public UwatecData (int [] readData)
    {
        parseData (readData);
    }

    public UwatecData (String fileName)
        throws IOException
    {
        if (fileName == null) {
            throw new IllegalArgumentException ("file name = null");
        }

        DataInputStream in = new DataInputStream
            (new BufferedInputStream (new FileInputStream (fileName)));
        int [] readData = new int [2046];

        for (int index = 0; index < readData.length; index++) {
            readData [index] = in.read () & 0xFF;
        }
        parseData (readData);
        in.close ();
    }

    public UwatecCurrentStatus getCurrentStatus ()
    {
        return currentStatus;
    }

    public List<UwatecDepthProfile> getDepthProfiles ()
    {
        return depthProfiles;
    }

    public List<UwatecLogEntry> getLogbook ()
    {
        return logbook;
    }

    public UwatecSettings getSettings ()
    {
        return settings;
    }

    private void parseData (int [] readData)
    {
        if ((readData == null) || (readData.length < 2046)) {
            throw new IllegalArgumentException
                ("need an array with at least 2046 bytes");
        }

        // read settings
        int [] bytes = new int [52];

        System.arraycopy (readData, 0x7bc, bytes, 0, bytes.length);
        settings = new UwatecSettings (bytes);

        // read current status
        bytes = new int [14];
        System.arraycopy (readData, 0x7f0, bytes, 0, bytes.length);
        currentStatus = new UwatecCurrentStatus (bytes);

        // read depth profiles

        // find start of newest depth profile
        int profileOffset = currentStatus.getEndOfProfileBuffer();

        for (;;) {
            if (profileOffset == 0x600) {
                profileOffset = 0x0;
            }
            if (readData [profileOffset] == 0xff) {
                break;
            }
            profileOffset++;
        }

        int offset = profileOffset;

        for (int index = 1; index <= currentStatus.getNumberOfDiveProfiles();
             index++) {
            // copy next depth profile into buffer
            int [] buffer       = new int [0x600];
            int    bufferOffset = 0;

            for (;;) {
                if (offset == 0x600) {
                    offset = 0x0;
                }
                if ((bufferOffset >= buffer.length) ||
                    (offset == currentStatus.getEndOfProfileBuffer()) ||
                    ((bufferOffset > 0) && (readData [offset] == 0xff))) {
                    break;
                }
                buffer [bufferOffset++] = readData [offset++];
            }

            int [] result = new int [bufferOffset];

            System.arraycopy (buffer, 0, result, 0, result.length);

            UwatecDepthProfile depthProfile = new UwatecDepthProfile (result, settings);
            depthProfiles.add (depthProfile);
        }

        // read log book
        int newestLogbook = readData [0x7f4];

        if (newestLogbook == 0) {
            newestLogbook = 37;
        }

        int numberOfDives = currentStatus.getTotalDiveNumbers();

        if (numberOfDives > 37) {
            numberOfDives = 37;
        }
        for (int index = 1; index <= numberOfDives; index++) {
            int logOffset = ((newestLogbook - numberOfDives + index + 36) % 37)
                * 12 + 0x600;

            bytes = new int [12];
            System.arraycopy (readData, logOffset, bytes, 0, bytes.length);
            logbook.add (new UwatecLogEntry (bytes, settings));
        }
    }

    public String toString ()
    {
        return
            "depthProfiles: " + depthProfiles + "\n" +
            "logbook: "       + logbook       + "\n" +
            "settings: "      + settings      + "\n" +
            "currentStatus: " + currentStatus + "\n";
    }
}
