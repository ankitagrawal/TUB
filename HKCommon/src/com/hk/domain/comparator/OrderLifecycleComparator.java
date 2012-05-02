package com.hk.domain.comparator;

import java.util.Comparator;

import com.hk.domain.order.OrderLifecycle;


public class OrderLifecycleComparator implements Comparator<OrderLifecycle> {
    public int compare(OrderLifecycle o1, OrderLifecycle o2) {
      if (o1.getActivityDate() != null && o2.getActivityDate() != null) {
        return o1.getActivityDate().compareTo(o2.getActivityDate());
      }
      return -1;
    }
  }

