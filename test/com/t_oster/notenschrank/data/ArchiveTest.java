package com.t_oster.notenschrank.data;

import com.t_oster.notenschrank.TestBase;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArchiveTest extends TestBase
{

  private String[] songs = new String[]
  {
    "Adebars Reisen", "Martini", "1984 George Orwell"
  };
  ;
	private String[][] voices = new String[][]
  {
    new String[]
    {
      "1. Klarinette in Bb", "1. Trompete in Bb"
    },
    new String[]
    {
      "1. Eb-Alt Saxophon"
    },
    new String[]
    {
      "1. Klarinette in Bb", "2. Eb-Alt Saxophon"
    }
  };
  private File testdir = new File("tmp//archivetest");

  @Before
  public void setUp() throws IOException
  {
    if (testdir.exists())
    {
      fail("Testdir '" + testdir.getAbsolutePath() + "' exists!");
    }
    testdir.mkdirs();
    SettingsManager.getInstance().setArchivePath(testdir.getAbsolutePath());
    File arc = SettingsManager.getInstance().getArchivePath();
    for (int i = 0; i < songs.length; i++)
    {
      File dir = new File(arc, songs[i]);
      dir.mkdir();
      for (String s : voices[i])
      {
        new File(dir, s + "." + Sheet.FILEEXTENSION).createNewFile();
      }
    }
  }

  @Test
  public void equalsTest()
  {
    for (String s : songs)
    {
      Song a = new Song(s);
      Song b = new Song(s);
      assertEquals(a, b);
    }
    for (String[] va : voices)
    {
      for (String v : va)
      {
        Voice a = new Voice(v);
        Voice b = new Voice(v);
        assertEquals(a, b);
      }
    }
  }

  @Test
  public void testGetAvailableSongs()
  {
    Song[] result = Archive.getInstance().getAvailableSongs();
    assertEquals(result.length, songs.length);
    for (Song so : result)
    {
      boolean found = false;
      for (String st : songs)
      {
        if (so.toString().equals(st))
        {
          found = true;
          break;
        }
      }
      assertTrue(found);
    }
  }

  @Test
  public void testGetAvailableVoices()
  {

    Voice[] result = Archive.getInstance().getAvailableVoices();
    Set<String> vset = new HashSet<String>();
    for (String[] a : voices)
    {
      vset.addAll(Arrays.asList(a));
    }
    assertEquals(vset.size(), result.length);
    for (Voice v : result)
    {
      assertTrue(vset.contains(v.toString()));
    }
  }

  @Test
  public void testVoiceHashSetCompatibility()
  {
    Set<String> string_set = new HashSet<String>();
    Set<Voice> voice_set = new HashSet<Voice>();
    for (String[] a : voices)
    {
      string_set.addAll(Arrays.asList(a));
      for (String s : a)
      {
        voice_set.add(new Voice(s));
      }
    }
    assertEquals(string_set.size(), voice_set.size());
  }

  @Test
  public void testSongTreeSetCompatibility()
  {
    Set<Song> song_set = new TreeSet<Song>();
    List<String> song_list = new LinkedList<String>();
    for (String s : songs)
    {
      song_list.add(s);
      song_set.add(new Song(s));
    }
    Collections.sort(song_list, new Comparator()
    {
      @Override
      public int compare(Object t, Object t1)
      {
        if (t == null)
        {
          return t1 == null ? 0 : 1;
        }
        return t.toString().compareToIgnoreCase(t1.toString());
      }
    });
    int i = 0;
    for (Song s : song_set)
    {
      assertEquals(song_list.get(i++), s.toString());
    }
  }
  
  @Test
  public void testVoiceTreeSetCompatibility()
  {
    Set<Voice> voice_set = new TreeSet<Voice>();
    Set<String> string_set = new TreeSet<String>();
    for (String[] a : voices)
    {
      for (String v:a)
      {
        voice_set.add(new Voice(v));
        string_set.add(v);
      }
    }
    assertEquals(voice_set.size(), string_set.size());
    Iterator<String> i1 = string_set.iterator();
    Iterator<Voice> i2 = voice_set.iterator();
    assertEquals(i1.hasNext(), i2.hasNext());
    while (i1.hasNext())
    {
      assertEquals(i1.next(), i2.next().toString());
      assertEquals(i1.hasNext(), i2.hasNext());
    }
  }

  @After
  public void tearDown()
  {
    deleteRecursively(testdir);
  }
}
