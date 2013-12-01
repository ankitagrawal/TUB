package com.hk.db.seed.marketing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.constants.marketing.EnumGoogleBannedWord;
import com.hk.db.seed.BaseSeedData;
import com.hk.domain.marketing.GoogleBannedWord;


@Component
public class GoogleBannedWordSeedData extends BaseSeedData {

    public void insert(java.lang.String bannedWords, java.lang.Long id) {
        GoogleBannedWord googleBannedWord = new GoogleBannedWord();
        googleBannedWord.setBannedWord(bannedWords);
        googleBannedWord.setId(id);
        save(googleBannedWord);
    }

    public void invokeInsert() {
        List<Long> pkList = new ArrayList<Long>();

        for (EnumGoogleBannedWord enumGoogleBannedWord : EnumGoogleBannedWord.values()) {

            if (pkList.contains(enumGoogleBannedWord.getId()))
                throw new RuntimeException("Duplicate key " + enumGoogleBannedWord.getId());
            else
                pkList.add(enumGoogleBannedWord.getId());

            insert(enumGoogleBannedWord.getBannedWord(), enumGoogleBannedWord.getId());
        }
    }

}
