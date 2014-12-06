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
package be.vds.jtbdive.client.swing.text;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class RegexPlainDocument extends PlainDocument {

	private static final long serialVersionUID = -3381372412924865370L;

	private String[] regex;

	public static final String INTEGER = "[-+]?[\\d]*";
	public static final String DOUBLE = "[+-]?[\\d]*[\\.]?[\\d]*";

	public RegexPlainDocument(String[] regex) {
		super();
		this.regex = Arrays.copyOf(regex, regex.length);
	}

	/**
	 * Redefini la methode de la classe PlainDocument permttant ainsi
	 * d'autoriser uniquement les caracteres desires
	 */
	public void insertString(int offs, String str, AttributeSet a)
			throws BadLocationException {
		boolean b = false;
		String fullText = getText(0, getLength()) + str;

		for (int i = 0; i < regex.length; i++) {
			Pattern p = Pattern.compile(regex[i]);
			Matcher m = p.matcher(fullText);
			b = m.matches();
			if (b) {
				break;
			}
		}

		if (!b) {
			doNotInsertNewChar(offs, str, a);
		} else {
			insertNormally(offs, str, a, fullText);
		}
	}

	protected void insertNormally(int offs, String str, AttributeSet a,
			String fullText) throws BadLocationException {
		insertNewChar(offs, str, a);
	}

	protected void doNotInsertNewChar(int offs, String str, AttributeSet a)
			throws BadLocationException {
		int end = 0;
		if (str.length() > 1) {
			end = 1;
		}
		super.insertString(offs, str.substring(0, str.length() - end - 1), a);
	}

	protected void insertNewChar(int offs, String str, AttributeSet a)
			throws BadLocationException {
		super.insertString(offs, str, a);
	}
}
