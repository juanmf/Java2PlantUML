/*
 *  Copyright 2016 Juan Manuel Fernandez
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.juanmf.java2plant.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.SecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveFileHelper {

   private static final String TARGET_DIRECTORY = "target"; // default directory just in case one is not provided
   private static final String FILE_EXTENSION = "puml";     // default file extension that IDE and browser plugins like
   private static final String J2PUML = "j2puml";

   public static void save(StringBuilder pumlContent, String path) throws IOException {
      final File file = new File(getPathName(path));
      BufferedWriter bw;
      bw = new BufferedWriter(new FileWriter(file));
      bw.write(pumlContent.toString());

      bw.flush();
      bw.close();
   }

   private static String getPathName(String route) throws IOException {
      if (route == null) {
         route = TARGET_DIRECTORY;
      }
      File file = new File(route);
      if (file.exists() && !file.isDirectory()) {
         throw new IOException("File " + route + " exists but it is not a directory");
      }
      else if (!file.exists()) { // path does not yet exist
         try {
            if (!file.mkdir()) {
               throw new IOException("Error creating directory " + route);
            }
         }
         catch (SecurityException se) {
            throw new IOException("Security Exception creating " + route);
         }
      }
      StringBuilder path = new StringBuilder().append(route).append(File.separator).append(J2PUML);

      SimpleDateFormat instant = new SimpleDateFormat("ddMMyyyy_HM_mm", Locale.getDefault());
      Date now = new Date();
      StringBuilder fileName = new StringBuilder(instant.format(now));

      path.append(fileName).append("." + FILE_EXTENSION);
      return path.toString();
   }

}

