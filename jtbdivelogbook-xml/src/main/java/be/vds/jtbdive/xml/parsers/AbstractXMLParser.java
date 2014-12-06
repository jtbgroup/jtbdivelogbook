package be.vds.jtbdive.xml.parsers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import be.vds.jtbdive.core.logging.Syslog;

public abstract class AbstractXMLParser {
	private static final Syslog LOGGER = Syslog
			.getLogger(AbstractXMLParser.class);
	protected static final String ATTRIBUTE_ID = "id";

	protected void detachElWithIdFromParent(Element parentEl, long elementId) {
		// Element elToDetach = null;
		// Element element = null;
		// for (Iterator iterator = parentEl.getChildren().iterator(); iterator
		// .hasNext();) {
		// element = (Element) iterator.next();
		// String id = element.getAttributeValue(ATTRIBUTE_ID);
		// if (null != id && Long.valueOf(id) == elementId) {
		// elToDetach = element;
		// break;
		// }
		// }
		//
		// if (elToDetach != null) {
		// parentEl.removeContent(elToDetach);
		// LOGGER.info("Tag " + elToDetach.getName() + " with id " + elementId
		// + " removed");
		// }

		detachElWithIdFromParent(parentEl, elementId, false);
	}

	/**
	 * 
	 * @param parentEl
	 * @param elementId
	 * @param recursive
	 *            true if all the tags with the given id must be removed from
	 *            the parent.
	 */
	protected void detachElWithIdFromParent(Element parentEl, long elementId,
			boolean recursive) {
		List<Element> elementsToDetach = new ArrayList<Element>();
		Element element = null;
		for (Iterator iterator = parentEl.getChildren().iterator(); iterator
				.hasNext();) {
			element = (Element) iterator.next();
			String id = element.getAttributeValue(ATTRIBUTE_ID);
			if (null != id && Long.valueOf(id) == elementId) {
				elementsToDetach.add(element);
				if (!recursive) {
					break;
				}
			}
		}

		for (Element detachEl : elementsToDetach) {
			LOGGER.info("Tag " + detachEl.getName() + " with id " + elementId
					+ " removed");
			parentEl.removeContent(detachEl);
		}
	}

	protected void createNodeIfNotExist(String... tags) {
		Element parentElement = getRootElement();
		Element currentElement = null;
		for (String tagName : tags) {
			currentElement = parentElement.getChild(tagName);
			if (null == currentElement) {
				currentElement = new Element(tagName);
				parentElement.addContent(currentElement);
			}
			parentElement = currentElement;
		}
	}

	protected abstract Element getRootElement();
}
