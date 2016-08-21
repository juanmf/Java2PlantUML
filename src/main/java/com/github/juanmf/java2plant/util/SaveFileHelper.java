package com.github.juanmf.java2plant.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveFileHelper {

	private static final String J2PUML = "j2PUML";

	public static void save(StringBuilder pumlContent, String path) throws IOException {
		final File file;

		if (path == null) {
			file = new File(getPathName(path));
		} else {
			file = new File(getPathName(null));
		}

		BufferedWriter bw;
		bw = new BufferedWriter(new FileWriter(file));
		bw.write(pumlContent.toString());

		bw.flush();
		bw.close();
	}

	private static String getPathName(String rout) {
		final StringBuilder path;
		if (rout == null) {
			path = new StringBuilder(J2PUML);
		} else {
			//TODO: It must be checked what we got in the rout (path) to validate it...
			path = new StringBuilder(rout + J2PUML);
		}
		SimpleDateFormat instant = new SimpleDateFormat("ddMMyyyy_HM_mm", Locale.getDefault());
		Date now = new Date();

		path.append(instant.format(now)).append(".txt");

		return path.toString();
	}

}
