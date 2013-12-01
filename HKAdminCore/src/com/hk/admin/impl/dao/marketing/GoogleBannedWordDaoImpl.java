package com.hk.admin.impl.dao.marketing;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.hk.admin.dto.marketing.GoogleBannedWordDto;
import com.hk.admin.pact.dao.marketing.GoogleBannedWordDao;
import com.hk.impl.dao.BaseDaoImpl;


@Repository
public class GoogleBannedWordDaoImpl extends BaseDaoImpl implements GoogleBannedWordDao{



  public List<GoogleBannedWordDto> getGoogleBannedWordDtoList() {
    @SuppressWarnings("unused")
    List<GoogleBannedWordDto> googleBannedWordDtoList = new ArrayList<GoogleBannedWordDto>();

    String percentageSymbol = "%";
    String yes = "YES";
    String no = "NO";

     /*" case when p.name like concat(concat(:percentageSymbol,gbw.bannedWord),:percentageSymbol) then :yes else :no end as existsInProductName ," +
        " case when p.overview like concat(concat(:percentageSymbol,gbw.bannedWord),:percentageSymbol) then :yes else :no end as existsInProductOverview, " +
        " case when p.description like concat(concat(:percentageSymbol,gbw.bannedWord),:percentageSymbol) then :yes else :no end as existsInProductDescription, " +
        " case when pf.name like concat(concat(:percentageSymbol,gbw.bannedWord),:percentageSymbol) then :yes else :no end as existsInProductFeatureName, " +
        " case when pf.value like concat(concat(:percentageSymbol,gbw.bannedWord),:percentageSymbol) then :yes else :no end as existsInProductFeatureValue " +  */

    String query = "select DISTINCT gbw as googleBannedWord, p as product " + 
        " from GoogleBannedWord as gbw, Product as p  " +
        " left outer join p.productFeatures  as pf " +
        " where p.deleted != :deleted "+
        " and (p.name like concat(concat(:percentageSymbol,gbw.bannedWord),:percentageSymbol) " +
        " or p.overview like concat(concat(:percentageSymbol,gbw.bannedWord),:percentageSymbol)  " +
        " or p.description like concat(concat(:percentageSymbol,gbw.bannedWord),:percentageSymbol)  " +
        " or pf.name like concat(concat(:percentageSymbol,gbw.bannedWord),:percentageSymbol)  " +
        " or pf.value like concat(concat(:percentageSymbol,gbw.bannedWord),:percentageSymbol))  ";

    return getSession().createQuery(query)
       /* .setParameter("yes", yes)
        .setParameter("no", no)*/
        .setBoolean("deleted", true)
        .setParameter("percentageSymbol", percentageSymbol).setResultTransformer(Transformers.aliasToBean(GoogleBannedWordDto.class))
        .list();


  }


}

