package com.hk.admin.util;

import com.hk.admin.pact.dao.warehouse.BinDao;
import com.hk.domain.inventory.Bin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: May 18, 2012
 * Time: 4:17:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class BinLocationGenerator {


	@Autowired
	BinDao binDao;
	private static Logger logger = LoggerFactory.getLogger(BinLocationGenerator.class);

	public void createBins(Integer maxAisle, Integer maxRack, Integer maxShelf) {
		for (int i = 0; i <= maxAisle; i++) {
			for (int j = 0; j <= maxRack; j++) {
				for (int k = 0; k <= maxShelf; k++) {
					Bin bin = new Bin();
					if (i < 9) {
						bin.setAisle("A" + "0" + i);
					} else {
						bin.setAisle("A" + i);
					}
					if (j < 9) {
						bin.setRack("R" + "0" + j);

					} else {
						bin.setRack("R" + j);
					}
					if (k < 9) {
						bin.setShelf("S" + "0" + k);
					} else {
						bin.setShelf("S" + k);
					}
					bin = binDao.createBin(bin, null);
					logger.info(bin.getBarcode());
				}
			}
		}
	}


}
