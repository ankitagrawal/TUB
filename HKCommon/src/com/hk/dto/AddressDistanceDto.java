package com.hk.dto;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hk.domain.LocalityMap;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Nov 8, 2011
 * Time: 5:35:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddressDistanceDto {

  private LocalityMap localityMap;
  private Double distance;

  public LocalityMap getLocalityMap() {
    return localityMap;
  }

  public void setLocalityMap(LocalityMap localityMap) {
    this.localityMap = localityMap;
  }

  public Double getDistance() {
    return distance;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }

  public List<AddressDistanceDto> getSortedByDistanceList(List<AddressDistanceDto> addressDistanceDtos){
    Collections.sort(addressDistanceDtos, new AddressDistanceDto.DistanceComparator());
    return addressDistanceDtos;
  }

  public class DistanceComparator implements Comparator<AddressDistanceDto> {
    public int compare (AddressDistanceDto dist1, AddressDistanceDto dist2){
     if(dist1.getDistance() != null && dist2.getDistance() != null){
       return dist1.getDistance().compareTo(dist2.getDistance());
     }
      return -1;
    }
  }
}
