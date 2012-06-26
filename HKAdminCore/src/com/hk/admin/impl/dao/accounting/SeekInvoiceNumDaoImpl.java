package com.hk.admin.impl.dao.accounting;

import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.accounting.SeekInvoiceNumDao;
import com.hk.domain.accounting.SeekInvoiceNum;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

@Repository
public class SeekInvoiceNumDaoImpl extends BaseDaoImpl implements SeekInvoiceNumDao{

    
	public Long getInvoiceNum( String invoiceType, Warehouse warehouse) {

		String query = "from SeekInvoiceNum sin where sin.warehouse = :warehouse and sin.prefix = :invoiceType ";
		Session session = getSession(true);
		//Begining a transaction as a lock over the database is required
		Transaction transaction = session.beginTransaction();
		Query seekInvoiceQuery = session.createQuery(query).setEntity("warehouse", warehouse).setString("invoiceType", invoiceType);
		//Taking a lock before reading from database
		seekInvoiceQuery.setLockMode("sin", LockMode.UPGRADE);

		SeekInvoiceNum seekInvoiceNum = (SeekInvoiceNum) seekInvoiceQuery.uniqueResult();
		Long invoiceNum = seekInvoiceNum.getInvoiceNum();
		Long incrInvoiceNum = seekInvoiceNum.getInvoiceNum() + 1;
		//System.out.println("before increment:"+seekInvoiceNum.getInvoiceNum());

		seekInvoiceNum.setInvoiceNum(incrInvoiceNum);

		session.setFlushMode(FlushMode.AUTO);
		session.merge(seekInvoiceNum);
		transaction.commit();
		//System.out.println("now:"+ invoiceNum);
		return invoiceNum;
	}



	/*public static void main(String[] args) {

		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.ERROR);
		Logger.getLogger("db").setLevel(Level.DEBUG);
		Logger.getLogger("mhc.util").setLevel(Level.INFO);
		Logger.getLogger("app.script").setLevel(Level.DEBUG);
		Logger.getLogger("mhc.service").setLevel(Level.INFO);

		TestInjectorFactory.setupInjector(
				TestInjectorFactory.warpModuleTransactionalUnitOfWork,
				TestInjectorFactory.hibernateMasterModule,
				TestInjectorFactory.dummySessionFactoryForReadModule
		);

		BatchProcessWorkManager batchProcessWorkManager = TestServiceLocatorFactory.getService(BatchProcessWorkManager.class);


		batchProcessWorkManager.beginWork();
		System.out.println("hello");
		Provider<WarehouseDao> warehouseDaoProvider = TestServiceLocatorFactory.getService(WarehouseDao.class);
		Provider<SeekInvoiceNumDao> seekInvoiceNumDaoProvider = TestServiceLocatorFactory.getService(SeekInvoiceNumDao.class);
		String retail = "R";
		Warehouse warehouse = warehouseDaoProvider.get().find(1L);
		System.out.println(warehouse.getCity());

		//Provider<SeekInvoiceNumDao> seekInvoiceNumDaoProvider = new SeekInvoiceNumDao();
		seekInvoiceNumDaoProvider.get().getInvoiceNum(retail, warehouse);
		batchProcessWorkManager.endWork();

	}*/
}
