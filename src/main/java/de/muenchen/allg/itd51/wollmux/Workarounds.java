/*
 * Dateiname: Workarounds.java
 * Projekt  : WollMux
 * Funktion : Referenziert alle temporären Workarounds an einer zentralen Stelle
 * 
 * Copyright (c) 2009-2015 Landeshauptstadt München
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the European Union Public Licence (EUPL),
 * version 1.0 (or any later version).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 *
 * You should have received a copy of the European Union Public Licence
 * along with this program. If not, see
 * http://ec.europa.eu/idabc/en/document/7330
 *
 * Änderungshistorie:
 * Datum      | Wer | Änderungsgrund
 * -------------------------------------------------------------------
 * 01.04.2009 | LUT | Erstellung
 * -------------------------------------------------------------------
 *
 * @author Christoph Lutz (D-III-ITD-D101)
 * @version 1.0
 * 
 */package de.muenchen.allg.itd51.wollmux;

import java.net.URL;
import java.net.URLClassLoader;

import com.sun.star.drawing.XDrawPageSupplier;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextSectionsSupplier;
import com.sun.star.text.XTextTablesSupplier;
import com.sun.star.uno.UnoRuntime;

import de.muenchen.allg.afid.UNO;
import de.muenchen.allg.itd51.wollmux.core.util.L;
import de.muenchen.allg.itd51.wollmux.core.util.Logger;
import de.muenchen.allg.itd51.wollmux.core.util.Utils;

/**
 * Diese Klasse referenziert alle temporären Workarounds, die im WollMux aufgenommen
 * wurden, an einer zentralen Stelle. Sie definiert Methoden, die die Steuerung
 * übernehmen, ob ein Workaround anzuwenden ist oder nicht.
 * 
 * @author Christoph Lutz (D-III-ITD-D101)
 */
public class Workarounds
{
  private static Boolean workaround89783 = null;

  private static Boolean workaround103137 = null;

  private static Boolean workaround102164 = null;

  private static Boolean workaround73229 = null;

  private static Boolean workaround96281 = null;

  private static Boolean workaround102619 = null;

  private static ClassLoader workaround102164CL = null;

  private static Boolean workaroundSunJavaBug4737732 = null;

  public static Boolean applyWorkaround(String issueNumber)
  {
    Logger.debug("Workaround für Issue "
      + issueNumber
      + " aktiv. Bestimmte Features sind evtl. nicht verfügbar. Die Performance kann ebenfalls leiden.");
    return Boolean.TRUE;
  }

  /**
   * Issue #73229 betrifft den WollMux-Seriendruck in ein Gesamtdokument und ist
   * aktuell für OOo Later priorisiert - wird also nicht in absehbarer Zeit behoben
   * sein.
   * 
   * @author Christoph Lutz (D-III-ITD-D101)
   */
  public static boolean applyWorkaroundForOOoIssue73229()
  {
    if (workaround73229 == null)
    {
      workaround73229 = applyWorkaround("73229");
    }
    return workaround73229.booleanValue();
  }

  /**
   * Issue #102164 betrifft OOo 3.2. Es ist unklar, wann der Workaround entfernt
   * werden kann, da er aufgrund eines Bugs in der Swing-Implementierung von Java 6
   * zurückgeht.
   * 
   * @author Matthias Benkmann (D-III-ITD-D101)
   */
  public static void applyWorkaroundForOOoIssue102164()
  {
    if (workaround102164 == null)
    {
      String version = Utils.getOOoVersion();
      if (version != null && !version.startsWith("3.1") && !version.startsWith("2")
        && !version.startsWith("3.0"))
      {
        workaround102164 = applyWorkaround("102164");
      }
      else
        workaround102164 = Boolean.FALSE;
    }

    if (workaround102164.booleanValue())
    {
      if (workaround102164CL == null)
        workaround102164CL = Thread.currentThread().getContextClassLoader();
      if (workaround102164CL == null)
        workaround102164CL = Workarounds.class.getClassLoader();
      if (workaround102164CL == null)
        workaround102164CL = ClassLoader.getSystemClassLoader();
      if (workaround102164CL == null)
        workaround102164CL = new URLClassLoader(new URL[] {});
      Thread.currentThread().setContextClassLoader(workaround102164CL);
    }
  }

  /**
   * Issue #103137 betrifft OOo 3.0 und 3.1. Der Workaround kann entfernt werden,
   * wenn keine Dokumente mehr im Umlauf sind, deren Generator OOo 2 ist und in denen
   * Textstellen mindestens einmal aus- und wieder eingeblendet wurden. Notfalls muss
   * man vor der Deaktivierung einen Mechanismus über die Dokumentablagen der
   * Referate laufen lassen der dafür sorgt, dass der Altbestand der von OOo 2
   * erzeugten Dokumente von sämtlichen text:display="none"-Stellen befreit wurde.
   * 
   * @author Christoph Lutz (D-III-ITD-D101)
   */
  public static boolean applyWorkaroundForOOoIssue103137()
  {
    if (workaround103137 == null)
    {
      String version = Utils.getOOoVersion();
      if (version != null
        && (version.startsWith("3.0") || version.startsWith("3.1")))
      {
        workaround103137 = applyWorkaround("103137");
      }
      else
        workaround103137 = Boolean.FALSE;
    }

    return workaround103137.booleanValue();
  }

  /**
   * Issue #96281 betrifft OOo 3.1 und 3.2. Ob es in 3.3 gelöst sein wird wissen wir
   * nicht. Seien wir einfach mal pessimistisch.
   * 
   * @author Matthias Benkmann (D-III-ITD-D101)
   */
  public static boolean applyWorkaroundForOOoIssue96281()
  {
    if (workaround96281 == null)
    {
      String version = Utils.getOOoVersion();
      if (version != null
        && (version.startsWith("3.1") || version.startsWith("3.2") || version.startsWith("3.3")))
      {
        workaround96281 = applyWorkaround("96281");
      }
      else
        workaround96281 = Boolean.FALSE;
    }

    return workaround96281.booleanValue();
  }

  /**
   * Folgender Workaround bezieht sich auf das SUN-JRE Issue
   * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4737732 und ist in der Klasse
   * d.m.a.i.wollmux.dialog.FormGUI implementiert. Der Workaround ist notwendig, wenn
   * WollMux mit einem SunJRE < 1.7 gestartet wird. Den Java-Bug konnten wir bislang
   * vor allem unter Linux mit KDE feststellen. Der Java-Bug ist in Oracle JRE 1.7
   * behoben und auch mit dem aktuellen openJDK 1.7 tritt er nicht mehr auf. Der
   * Workaround war von 2006 bis 2014 immer standardmäßig aktiv, führt aber mit
   * Basisclient 5.0 verstärkt zum Issue trac#11494. Deshalb wird der Workaround 
   * jetzt nur noch mit SunJRE <= 1.6 angewendet. Er kann vermutlich komplett
   * entfernt werden (wenn WollMux unter Linux nicht mehr mit SunJRE 1.6 genutzt wird)
   */
  public static boolean applyWorkaroundForSunJavaBug4737732()
  {
    if(workaroundSunJavaBug4737732 == null)
    {
      String javaVendor = System.getProperty("java.vendor");
      String javaVersion = System.getProperty("java.version");
      if(javaVendor.equals("Sun Microsystems Inc.") && (javaVersion.startsWith("1.6.") || javaVersion.startsWith("1.5.")))
      {
        Logger.debug("Workaround für Sun-JRE Issue 4737732 aktiv.");
        workaroundSunJavaBug4737732  = Boolean.TRUE;
      }
      else
      {
        workaroundSunJavaBug4737732 = Boolean.FALSE;
      }
    }
    return workaroundSunJavaBug4737732;
  }
  
  /**
   * Wegen https://bugs.documentfoundation.org/show_bug.cgi?id=89783 muss der
   * OOoMailMerge in mehrere Pakete aufgeteilt werden, wenn das
   * Seriendruck-Hauptdokument doc viele der im Issue genannten Elemente (z.B.
   * Rahmen, PageStyles, ...) enthält. Betroffen davon sind alle aktuell bekannten
   * Versionen von OOo, AOO und LO.
   * 
   * @param doc
   *         Das Seriendruck-Hauptdokument
   * 
   * @return Der Rückgabewert dieser Methode beschreibt, wie viele Datensätze zu doc
   *         ohne Einfrierer von der aktuell genutzen Office-Version verarbeitet
   *         werden können. Der Rückgabewert kann auch null sein, dann soll der der
   *         Workaround nicht angewendet werden.
   * 
   * @author Christoph Lutz (CIB software GmbH)
   */
  public static Integer workaroundForTDFIssue89783(XTextDocument doc)
  {
    if (workaround89783 == null)
    {
      Logger.debug(L.m("Workaround für TDF Issue 89783 aktiv."));
      workaround89783 = true;
    }

    if(workaround89783)
    {
      int maxCritElements = 1;
      // zähle Sections:
      XTextSectionsSupplier tss = UNO.XTextSectionsSupplier(doc);
      if (tss != null)
      {
        String[] names = tss.getTextSections().getElementNames();
        if (names.length > maxCritElements) maxCritElements = names.length;
      }

      // zähle DrawPage-Objekte (TextFrames + Pictures + DrawObjects):
      XDrawPageSupplier dps = UNO.XDrawPageSupplier(doc);
      if (dps != null)
      {
        int drawPageElements = dps.getDrawPage().getCount();
        if (drawPageElements > maxCritElements) maxCritElements = drawPageElements;
      }

      // count TextTables
      XTextTablesSupplier tts =
        UnoRuntime.queryInterface(XTextTablesSupplier.class, doc);
      if (tts != null)
      {
        String[] names = tts.getTextTables().getElementNames();
        if (names.length > maxCritElements) maxCritElements = names.length;
      }

      // Maximalwert des mit 16-Bit adressierbaren Bereichs / maxCritElements - 1
      // (zu Sicherheit)
      return ((1 << 16) / maxCritElements) - 1;
    }
    
    return null;
  }

}