package be.vds.jtbdive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UncommentSearch {

	private static final String COMMENT = "Jt'B Dive Logbook - Electronic dive logbook.";
	private static final String COMMENT2 = "Copyright (C) 2010  Gautier Vanderslyen";
		
	private String rootFolder = "C:\\Users\\vanderslyen.g\\Local Documents\\workspaces\\ws-juno\\ws-jtbdivelogbook-branch-2_7_0";

	public static void main(String[] args) {
		UncommentSearch s = new UncommentSearch();
		s.search();
	}

	private void search() {
		List<File> paths = new ArrayList<File>();
		paths.addAll(searchInFolder(new File(rootFolder)));
		for (File file : paths) {
			System.out.println(file.getAbsolutePath());
		}
		System.out.println(paths.size());
	}

	private Collection<File> searchInFolder(File folder) {
		List<File> paths = new ArrayList<File>();
		for (File file : folder.listFiles()) {
			if (file.isDirectory() && !file.getName().startsWith(".")) {
				paths.addAll(searchInFolder(file));
			} else if (file.getName().endsWith(".java") && !isCommented(file)) {
				paths.add(file);
			}
		}
		return paths;
	}

	private boolean isCommented(File file) {
		try {
			byte[] b = new byte[500];
			FileInputStream is = new FileInputStream(file);
			is.read(b);
			is.close();

			String s = new String(b);
			if (s.contains(COMMENT) && s.contains(COMMENT2)) {
				return true;
			}
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
