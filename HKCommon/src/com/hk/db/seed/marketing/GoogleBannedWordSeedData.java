package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumGoogleBannedWord;
import mhc.domain.GoogleBannedWord;
import mhc.service.dao.GoogleBannedWordDao;

import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class GoogleBannedWordSeedData {

  @Inject
  GoogleBannedWordDao googleBannedWordDao;

  public void insert(java.lang.String bannedWords, java.lang.Long id) {
    GoogleBannedWord googleBannedWord = new GoogleBannedWord();
      googleBannedWord.setBannedWord(bannedWords);
      googleBannedWord.setId(id);
    googleBannedWordDao.save(googleBannedWord);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumGoogleBannedWord enumGoogleBannedWord : EnumGoogleBannedWord.values()) {

      if (pkList.contains(enumGoogleBannedWord.getId())) throw new RuntimeException("Duplicate key "+ enumGoogleBannedWord.getId());
      else pkList.add(enumGoogleBannedWord.getId());

      insert(enumGoogleBannedWord.getBannedWord(), enumGoogleBannedWord.getId());
    }
  }

}
