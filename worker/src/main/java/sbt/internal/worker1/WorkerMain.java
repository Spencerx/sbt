/*
 * sbt
 * Copyright 2023, Scala center
 * Copyright 2011 - 2022, Lightbend, Inc.
 * Copyright 2008 - 2010, Mark Harrah
 * Licensed under Apache License 2.0 (see LICENSE)
 */

package sbt.internal.worker1;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Scanner;

public final class WorkerMain {
  private PrintStream originalOut;

  public static void main(final String[] args) throws Exception {
    try {
      if (args.length == 0) {
        WorkerMain app = new WorkerMain();
        app.consoleWork();
      } else {
        System.err.println("missing args");
        System.exit(1);
      }
    } catch (Throwable e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  WorkerMain() {
    this.originalOut = System.out;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    System.setOut(new PrintStream(baos));
  }

  void consoleWork() throws Exception {
    Scanner input = new Scanner(System.in);
    if (input.hasNextLine()) {
      String line = input.nextLine();
      process(line);
    }
  }

  void process(String json) throws Exception {
    JsonElement elem = JsonParser.parseString(json);
    JsonObject o = elem.getAsJsonObject();
    if (!o.has("jsonrpc")) {
      throw new RuntimeException("missing jsonprc element");
    }
    long id = o.getAsJsonPrimitive("id").getAsLong();
    String method = o.getAsJsonPrimitive("method").getAsString();
    JsonObject params = o.getAsJsonObject("params");
    switch (method) {
      case "run":
        Gson g = new Gson();
        RunInfo info = g.fromJson(params, RunInfo.class);
        run(info);
        break;
    }
  }

  void run(RunInfo info) throws Exception {
    if (info.jvm) {
      if (info.jvmRunInfo == null) {
        throw new RuntimeException("missing jvmRunInfo element");
      }
      RunInfo.JvmRunInfo jvmRunInfo = info.jvmRunInfo;
      URL[] urls =
          jvmRunInfo
              .classpath
              .stream()
              .map(
                  filePath -> {
                    try {
                      return filePath.path.toURL();
                    } catch (MalformedURLException e) {
                      throw new RuntimeException(e);
                    }
                  })
              .toArray(URL[]::new);
      URLClassLoader cl = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
      try {
        Class<?> mainClass = cl.loadClass(jvmRunInfo.mainClass);
        Method mainMethod = mainClass.getMethod("main", String[].class);
        String[] mainArgs = jvmRunInfo.args.stream().toArray(String[]::new);
        mainMethod.invoke(null, (Object) mainArgs);
      } finally {
        cl.close();
      }
    } else {
      throw new RuntimeException("only jvm is supported");
    }
  }
}
