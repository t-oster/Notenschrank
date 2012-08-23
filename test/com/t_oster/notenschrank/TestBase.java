package com.t_oster.notenschrank;

import java.io.File;

/**
 *
 * @author Thomas Oster <thomas.oster@rwth-aachen.de>
 */
public class TestBase
{

  protected void deleteRecursively(File f)
  {
    if (".".equals(f.getName()) || "..".equals(f.getName()))
    {
      return;
    }
    if (f.isDirectory())
    {
      for (File ff : f.listFiles())
      {
        deleteRecursively(ff);
      }
      f.delete();
    }
    else
    {
      f.delete();
    }
  }
}
