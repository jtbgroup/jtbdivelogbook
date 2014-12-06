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
package be.vds.jtbdive.client.actions.download;

import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import be.vds.jtbdive.client.core.DiveSiteManagerFacade;
import be.vds.jtbdive.client.core.DiverManagerFacade;
import be.vds.jtbdive.client.core.LogBookManagerFacade;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveEquipment;
import be.vds.jtbdive.core.core.DiveProfile;
import be.vds.jtbdive.core.core.DiveSite;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.GazMix;
import be.vds.jtbdive.core.core.LogBook;
import be.vds.jtbdive.core.core.Palanquee;
import be.vds.jtbdive.core.core.PalanqueeEntry;
import be.vds.jtbdive.core.core.PhysiologicalStatus;
import be.vds.jtbdive.core.core.catalogs.DiverRole;
import be.vds.jtbdive.core.core.catalogs.Gaz;
import be.vds.jtbdive.core.core.material.DiveComputerEquipment;
import be.vds.jtbdive.core.core.material.DiveTankEquipment;
import be.vds.jtbdive.core.core.material.Equipment;
import be.vds.jtbdive.core.exceptions.DataStoreException;

public class ImportVersion2Action extends AbstractAction {

	private static final long serialVersionUID = -5611883702503599937L;
	private DiveSiteManagerFacade diveLocationManagerFacade;
    private DiverManagerFacade diverManagerFacade;
    private LogBookManagerFacade logBookManagerFacade;

    public ImportVersion2Action(LogBookManagerFacade logBookManagerFacade,
            DiverManagerFacade diverManagerFacade,
            DiveSiteManagerFacade diveLocationManagerFacade) {
        this.logBookManagerFacade = logBookManagerFacade;
        this.diverManagerFacade = diverManagerFacade;
        this.diveLocationManagerFacade = diveLocationManagerFacade;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Parser p = new Parser();

        SAXBuilder sb = new SAXBuilder();

        JFileChooser ch = new JFileChooser();
        int i = ch.showOpenDialog(null);
        if (i != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Document doc = null;
        try {
            doc = sb.build(new FileInputStream(ch.getSelectedFile()));
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (JDOMException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // read all the divelocations
        Map<Long, DiveSite> diveLocationMap = new HashMap<Long, DiveSite>();
        for (Iterator iterator = doc.getRootElement().getChild("divelocations").getChildren("divelocation").iterator(); iterator.hasNext();) {
            Element dlEl = (Element) iterator.next();
            DiveSite dl = p.readDiveLocationElement(dlEl);
            diveLocationMap.put(dl.getId(), dl);
        }

        // read all the divers
        Map<Long, Diver> diverMap = new HashMap<Long, Diver>();
        for (Iterator iterator = doc.getRootElement().getChild("divers").getChildren("diver").iterator(); iterator.hasNext();) {
            Element dEl = (Element) iterator.next();
            Diver d = p.readDiver(dEl);
            diverMap.put(d.getId(), d);
        }

        // read the logbook
        LogBook lb = p.readLogBook(doc.getRootElement(), diveLocationMap,
                diverMap);

        // set the divelocations' id and divers' id to -1 so that the are not
        // equals to a the persisted one.
        Map<Diver, Diver> newDiver = new HashMap<Diver, Diver>();
        for (Diver diver : diverMap.values()) {
            diver.setId(-1);
            try {
                Diver dd = diverManagerFacade.saveDiver(diver);
                newDiver.put(diver, dd);
            } catch (DataStoreException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        for (Dive d : lb.getDives()) {
            if (null != d.getPalanquee()) {
                for (PalanqueeEntry pe : d.getPalanquee().getPalanqueeEntries()) {
                    pe.setDiver(newDiver.get(pe.getDiver()));
                }
            }
        }

        Map<DiveSite, DiveSite> newDiveLoc = new HashMap<DiveSite, DiveSite>();
        for (DiveSite divelocation : diveLocationMap.values()) {
            divelocation.setId(-1);
            try {
                newDiveLoc.put(divelocation, diveLocationManagerFacade.saveDiveSite(divelocation));
            } catch (DataStoreException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        for (Dive d : lb.getDives()) {
            if (d.getDiveSite() != null) {
                d.setDiveSite(newDiveLoc.get(d.getDiveSite()));
            }
        }

        logBookManagerFacade.setCurrentLogBook(lb);

    }

    private class Parser {

        public DiveSite readDiveLocationElement(Element diveLocationElement) {
            DiveSite d = new DiveSite();
            d.setId(Long.parseLong(diveLocationElement.getAttributeValue("id")));

            d.setName(diveLocationElement.getChildText("name"));
            d.setDepth(Double.parseDouble(diveLocationElement.getChildText("depth")));
            return d;
        }

        public Diver readDiver(Element diverElement) {
            Diver d = new Diver(Long.parseLong(diverElement.getAttributeValue("id")));
            d.setFirstName(diverElement.getChildText("firstname"));
            d.setLastName(diverElement.getChildText("lastname"));
            return d;
        }

        public LogBook readLogBook(Element logBookElement,
                Map<Long, DiveSite> diveLocations, Map<Long, Diver> divers) {
            LogBook lb = new LogBook();
            lb.setName(logBookElement.getChildText("name"));

            if (logBookElement.getChild("dives") != null) {
                for (Iterator iterator = logBookElement.getChild("dives").getChildren("dive").iterator(); iterator.hasNext();) {
                    Element element = (Element) iterator.next();
                    lb.addDive(readDiveElement(element, diveLocations, divers));
                }
            }
            return lb;
        }

        private Dive readDiveElement(Element diveElement,
                Map<Long, DiveSite> diveLocations, Map<Long, Diver> divers) {
            Dive dive = new Dive();
            dive.setNumber(Integer.parseInt(diveElement.getAttributeValue("number")));

            if (diveElement.getChild("date") != null) {
                dive.setDate(new Date(Long.parseLong(diveElement.getChildText("date"))));
            }

            if (diveElement.getChild("divetime") != null) {
                dive.setDiveTime(Integer.parseInt(diveElement.getChildText("divetime")));
            }

            if (diveElement.getChild("maxdepth") != null) {
                dive.setMaxDepth(Double.parseDouble(diveElement.getChildText("maxdepth")));
            }

            if (diveElement.getChild("comment") != null) {
                dive.setComment(diveElement.getChildText("comment"));
            }

            if (diveElement.getChild("surfacetimebeforedive") != null) {
                dive.setSurfaceTime(Integer.parseInt(diveElement.getChildText("surfacetimebeforedive")));
            }

            if (diveElement.getChild("watertemperature") != null) {
                dive.setWaterTemperature(Double.parseDouble(diveElement.getChildText("watertemperature")));
            }

            if (diveElement.getChild("location") != null) {
                dive.setDiveSite(diveLocations.get(Long.parseLong(diveElement.getChildText("location"))));
            }

            if (diveElement.getChild("divers") != null) {
                dive.setPalanquee(readPalanquee(diveElement.getChild("divers"),
                        divers));
            }

            dive.setPhysiologicalStatus(readPhysisiologicalStatus(diveElement));

            if (diveElement.getChild("depthentries") != null) {
                dive.setDiveProfile(readDiveProfile(diveElement));
            }

//			if (diveElement.getChild("equipment") != null)
            dive.setDiveEquipment(readDiveEquipments(diveElement));

            return dive;
        }

        private DiveEquipment readDiveEquipments(Element child) {
            List<Equipment> els = new ArrayList<Equipment>();
            Element el = child.getChild("diveComputer");
            if (el != null) {
                DiveComputerEquipment dc = new DiveComputerEquipment();
//				dc.setSerialNumber(el.getChildText("serialnumber"));
//				int type = Integer.parseInt(el.getChildText("type"));
//				if (type == UwatecConstants.ALADIN_PRO_ULTRA)
//					dc.setDiveComputerType(DiveComputerType.ALADIN_PRO_ULTRA);
                dc.setRemainingBattery(Integer.parseInt(el.getChildText("remainingbattery")));
                els.add(dc);
            }

            Element tankEl = child.getChild("nitroxmix");
            if (tankEl != null) {
                DiveTankEquipment dt = new DiveTankEquipment();
                int type = Integer.parseInt(tankEl.getText());
//				Map<Gaz, Integer> gazMix = new HashMap<Gaz, Integer>();
                GazMix gazMix = new GazMix();
                if (1 == type) {
                    gazMix.addGaz(Gaz.GAZ_OXYGEN, 21);
                    gazMix.addGaz(Gaz.GAZ_NITROGEN, 79);
                    dt.setGazMix(gazMix);
                    els.add(dt);
                } else if (7 == type) {
                    gazMix.addGaz(Gaz.GAZ_OXYGEN, 32);
                    gazMix.addGaz(Gaz.GAZ_NITROGEN, 68);
                    dt.setGazMix(gazMix);
                    els.add(dt);
                } else if (9 == type) {
                    gazMix.addGaz(Gaz.GAZ_OXYGEN, 36);
                    gazMix.addGaz(Gaz.GAZ_NITROGEN, 64);
                    dt.setGazMix(gazMix);
                    els.add(dt);
                }
            }

            if (els.size() == 0) {
                return null;
            }

            DiveEquipment de = new DiveEquipment();
            de.setEquipments(els);
            return de;
        }

        private DiveProfile readDiveProfile(Element child) {
            DiveProfile profile = new DiveProfile();

            if (null != child.getChild("depthentries")) {
                String[] s = child.getChildText("depthentries").split(";");
                String[] warns = child.getChildText("warningentries").split(";");
                String[] decoEntry = child.getChildText("decoentry").split(";");

                Map<Double, Double> depthEntries = new HashMap<Double, Double>();
                Set<Double> ascentWarnings = new HashSet<Double>();
                Set<Double> decoWarnings = new HashSet<Double>();
                Set<Double> remainingBottomTimeWarnings = new HashSet<Double>();
                double sec = 20;
                for (int i = 0; i < s.length; i++) {
                    depthEntries.put(sec, Double.valueOf(s[i]));

                    if ((Integer.parseInt(warns[i]) & 2) == 2) {
                        remainingBottomTimeWarnings.add(sec);
                    }

                    if ((Integer.parseInt(warns[i]) & 4) == 4) {
                        ascentWarnings.add(sec);
                    }

                    if ((Integer.parseInt(warns[i]) & 1) == 1) {
                        decoWarnings.add(sec);
                    }

                    sec = sec + 20;
                }
                profile.setDepthEntries(depthEntries);
                profile.setAscentWarnings(ascentWarnings);
                profile.setDecoCeilingWarnings(decoWarnings);
                profile.setRemainingBottomTimeWarnings(remainingBottomTimeWarnings);

                int time = 0;
                Set<Double> decoEntries = new HashSet<Double>();
                for (String entry : decoEntry) {
                    if (Double.parseDouble(entry) != 0) {
                        decoEntries.add(Double.parseDouble(entry));
                    }
                    time += 60;
                }
                profile.setDecoEntries(decoEntries);
                return profile;
            }
            return null;
        }

        private PhysiologicalStatus readPhysisiologicalStatus(
                Element diveElement) {
            PhysiologicalStatus st = new PhysiologicalStatus();

            if (diveElement.getChild("arterialmicrobubblelevel") != null) {
                st.setArterialMicrobubbleLevel(Integer.parseInt(diveElement.getChildText("arterialmicrobubblelevel")));
            }

            if (diveElement.getChild("interpulmonaryshunt") != null) {
                st.setInterPulmonaryShunt(Double.parseDouble(diveElement.getChildText("interpulmonaryshunt")));
            }

            if (diveElement.getChild("maxppo2") != null) {
                st.setMaxPPO2(Double.parseDouble(diveElement.getChildText("maxppo2")));
            }

            if (diveElement.getChild("skincoollevel") != null) {
                double d = Double.parseDouble(diveElement.getChildText("skincoollevel"));
                if (d == 0) {
                    st.setSkinCoolTemperature(30.7);
                } else if (d == 1) {
                    st.setSkinCoolTemperature(28);
                } else if (d == 2) {
                    st.setSkinCoolTemperature(26);
                } else if (d == 3) {
                    st.setSkinCoolTemperature(24);
                } else if (d == 4) {
                    st.setSkinCoolTemperature(23);
                } else if (d == 5) {
                    st.setSkinCoolTemperature(22);
                } else if (d == 6) {
                    st.setSkinCoolTemperature(21);
                } else if (d == 7) {
                    st.setSkinCoolTemperature(20);
                }
            }

            if (diveElement.getChild("saturationindex") != null) {
                st.setSaturationIndexAfterDive(diveElement.getChildText(
                        "saturationindex").charAt(0));
            }

            return st;
        }

        private Palanquee readPalanquee(Element child, Map<Long, Diver> divers) {
            Palanquee p = new Palanquee();
            Set<DiverRole> roles = new HashSet<DiverRole>();
            roles.add(DiverRole.ROLE_PALANQUEE_SIMPLE_DIVER);

            String[] diversId = child.getText().split(";");
            for (String id : diversId) {
                Diver d = divers.get(Long.parseLong(id));
                p.addPalanqueeEntry(new PalanqueeEntry(d, roles));
            }

            return p;
        }
    }
}
