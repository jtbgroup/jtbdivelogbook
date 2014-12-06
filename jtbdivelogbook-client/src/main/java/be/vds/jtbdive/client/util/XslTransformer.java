/*
* Jt'B Dive Logbook - Electronic dive logbook.
* 
* Copyright (C) 2010  Gautier Vanderslyen
* 
* Jt'B Dive Logbook is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package be.vds.jtbdive.client.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;
import org.xml.sax.InputSource;

import be.vds.jtbdive.core.logging.Syslog;

public class XslTransformer {

    public static final String ISO_8859 = "iso-8859-1";
    public static final String UTF_8 = "UTF-8";
    public static final String UTF_16 = "UTF-16";
    private static final Syslog LOGGER = Syslog.getLogger(XslTransformer.class);

    public String transform(byte[] data, InputStream xslName) {
        return transform(data, xslName, null);
    }

     public String transform(InputStream xmlStream, InputStream xslName) {
        return transform(xmlStream, xslName, null);
    }

    public String transform(InputStream xmlStream, InputStream xslStream, String encoding) {
        String result = null;

        if (xmlStream == null) {
            return null;
        }

        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (transform(xmlStream, xslStream, os, encoding)) {
                result = os.toString();
            }
            os.close();
            xslStream.close();
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage());
        }

        return result;
    }


    public String transform(byte[] data, InputStream xslStream, String encoding) {
        String result = null;

        if (data == null) {
            return null;
        }

        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            if (transform(is, xslStream, os, encoding)) {
                result = os.toString();
            }
            os.close();
            xslStream.close();
        } catch (IOException e1) {
            LOGGER.error(e1.getMessage());
        }

        return result;
    }



    public boolean transform(InputStream is, InputStream xs, OutputStream os, String encoding) {
        boolean bOk = false;
        try {
            SAXBuilder builder = new SAXBuilder();
            InputSource sis = new InputSource(is);
            if (encoding != null) {
                sis.setEncoding(encoding);
            }

            Document doc = builder.build(sis);

            // Use JAXP to get a transformer
            Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xs));

            // Run the transformation
            JDOMSource source = new JDOMSource(doc);
            JDOMResult result = new JDOMResult();
            transformer.transform(source, result);
            Document doc2 = result.getDocument();

            // Display the results
            XMLOutputter outp = new XMLOutputter();
            Format xmlFormat = Format.getPrettyFormat();
            if (encoding != null) {
                xmlFormat.setEncoding(encoding);
            }
            xmlFormat.setTextMode(Format.TextMode.NORMALIZE);
            outp.setFormat(xmlFormat);
            outp.output(doc2, os);

            bOk = true;
        } catch (JDOMException e1) {
            LOGGER.error("JDOMException catched: " + e1.getMessage());
        } catch (IOException e2) {
            LOGGER.error("IOException catched: " + e2.getMessage());
        } catch (TransformerException e3) {
            LOGGER.error("TransformerException catched: " + e3.getMessage());
        }
        return bOk;
    }

    public boolean transformThrowingExceptions(InputStream is, InputStream xs, OutputStream os) throws JDOMException, IOException, TransformerFactoryConfigurationError, TransformerException {
        return transformThrowingExceptions(is, xs, os, null);
    }

    public boolean transformThrowingExceptions(InputStream is, InputStream xs, OutputStream os, String encoding) throws JDOMException, IOException, TransformerFactoryConfigurationError, TransformerException {
        boolean bOk = false;

        SAXBuilder builder = new SAXBuilder();
        InputSource sis = new InputSource(is);
        if (encoding != null) {
            sis.setEncoding(encoding);
        }

        Document doc = builder.build(sis);

        // Use JAXP to get a transformer
        Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xs));

        // Run the transformation
        JDOMSource source = new JDOMSource(doc);
        JDOMResult result = new JDOMResult();
        transformer.transform(source, result);
        Document doc2 = result.getDocument();

        // Display the results
        XMLOutputter outp = new XMLOutputter();
        Format xmlFormat = Format.getPrettyFormat();
        if (encoding != null) {
            xmlFormat.setEncoding(encoding);
        }
        xmlFormat.setTextMode(Format.TextMode.NORMALIZE);
        outp.setFormat(xmlFormat);
        outp.output(doc2, os);

        bOk = true;

        return bOk;
    }
}
