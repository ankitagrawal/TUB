package com.hk.admin.manager;

import java.io.File;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.pact.dao.marketing.AmazonFeedDao;
import com.hk.admin.util.AmazonXslParser;
import com.hk.domain.amazon.AmazonFeed;

@Component
public class AmazonManager {

    @Autowired
    private AmazonXslParser amazonXslParser;

    @Autowired
    private AmazonFeedDao   amazonFeedDaoProvider;

    @Transactional
    public void insertAmazonCatalogue(File inFile) throws Exception {
        Set<AmazonFeed> amazonFeedSet = getAmazonXslParser().readAmazonCatalog(inFile);

        for (AmazonFeed amazonFeed : amazonFeedSet) {
            if (amazonFeed.getId() == null) {
                AmazonFeed amazonFeedDb = new AmazonFeed();
                amazonFeedDb.setProductVariant(amazonFeed.getProductVariant());
                amazonFeedDb.setTitle(amazonFeed.getTitle());
                amazonFeedDb.setDescription(amazonFeed.getDescription());
                amazonFeedDb.setAmazonBrowseNode(amazonFeed.getAmazonBrowseNode());
                amazonFeedDb.setItemPackageQuantity(amazonFeed.getItemPackageQuantity());
                amazonFeedDb.setWarranty(amazonFeed.getWarranty());
                amazonFeedDb.setGender(amazonFeed.getGender());
                amazonFeedDb.setBatteriesIncluded(amazonFeed.getBatteriesIncluded());
                getAmazonFeedDaoProvider().save(amazonFeedDb);
            } else
                getAmazonFeedDaoProvider().save(amazonFeed);
        }
    }

    public AmazonXslParser getAmazonXslParser() {
        return amazonXslParser;
    }

    public void setAmazonXslParser(AmazonXslParser amazonXslParser) {
        this.amazonXslParser = amazonXslParser;
    }

    public AmazonFeedDao getAmazonFeedDaoProvider() {
        return amazonFeedDaoProvider;
    }

    public void setAmazonFeedDaoProvider(AmazonFeedDao amazonFeedDaoProvider) {
        this.amazonFeedDaoProvider = amazonFeedDaoProvider;
    }

}
