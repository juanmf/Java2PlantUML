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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaveFileHelper {

   private static final String J2PUML = "j2puml";

   public static void save(StringBuilder pumlContent, String path) throws IOException {
      final File file = new File(getPathName(path));
      BufferedWriter bw;
      bw = new BufferedWriter(new FileWriter(file));
      bw.write(pumlContent.toString());

      bw.flush();
      bw.close();
   }

   private static String getPathName(String rout) {
      final StringBuilder path;
      if (rout != null) {
         // TODO: It must be checked what we got in the rout (path) to validate
         // it...
         path = new StringBuilder(rout.concat(J2PUML));
      } else {
         path = new StringBuilder(J2PUML);
      }

      SimpleDateFormat instant = new SimpleDateFormat("ddMMyyyy_HM_mm", Locale.getDefault());
      Date now = new Date();
      StringBuilder fullName = new StringBuilder(instant.format(now));

      path.append(fullName).append(".txt");

      return path.toString();
   }

}
