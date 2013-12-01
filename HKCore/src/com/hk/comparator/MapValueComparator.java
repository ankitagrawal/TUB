package com.hk.comparator;

import java.util.Comparator;
import java.util.Map;


@SuppressWarnings("unchecked")
public class MapValueComparator implements Comparator {

  Map base;

  public MapValueComparator(Map base) {
    this.base = base;
  }

  public int compare(Object a, Object b) {
    if ((Long) base.get(a) < (Long) base.get(b)) {
      return 1;
    } else if ((Long) base.get(a) == (Long) base.get(b)) {
      return 0;
    } else {
      return -1;
    }
  }
}
