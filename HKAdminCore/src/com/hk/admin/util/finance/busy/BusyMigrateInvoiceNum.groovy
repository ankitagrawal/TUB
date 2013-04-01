package com.hk.admin.util.finance.busy

import groovy.sql.Sql
import org.slf4j.LoggerFactory

/**
 * Created by IntelliJ IDEA.
 * User: Tarun
 * Date: April 13, 2012
 * Time: 01:28:07 PM
 * To change this template use File | Settings | File Templates.
 */



public class BusyMigrateInvoiceNum {
	private String hostName;
	private String dbName;
	private String serverUser;
	private String serverPassword;
	Sql sql;
	Sql busySql;

	BusyMigrateInvoiceNum(String hostName, String dbName, String serverUser, String serverPassword) {
		this.hostName = hostName;
		this.dbName = dbName;
		this.serverUser = serverUser;
		this.serverPassword = serverPassword;

		sql = Sql.newInstance("jdbc:mysql://" + hostName + ":3306/" + dbName, serverUser,
				serverPassword, "com.mysql.jdbc.Driver");

	}

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyMigrateInvoiceNum.class);

	public void updateSalesInvoiceNumber() {
	/*	sql.execute("""
								ALTER TABLE  `shipping_order` ADD  `accounting_invoice_number` VARCHAR( 20 ) NULL;
		""")*/



		sql.eachRow("""
                    Select so.id, if(so.drop_shipping =1,'DropShip',if(so.is_service_order =1,'Services',if(bo.is_b2b_order=1,'B2B','B2C'))) Order_type,
                    so.accounting_invoice_number_id as invoice_num
										From shipping_order so
										inner join base_order bo on so.base_order_id = bo.id
										where so.accounting_invoice_number_id is not null;
								""") {
			orders ->
			Long shippingOrderId = orders.id;
			String invoiceNumber = orders.invoice_num;
			String order_type = orders.Order_type
			 
			try {
				sql.executeUpdate("""
                    UPDATE shipping_order so SET accounting_invoice_number = ${invoiceNumber} WHERE id = ${shippingOrderId};
     """);
			}
			catch (Exception e) {
				logger.info("Error for shipping order id: ${shippingOrderId}", e);
			}

		}


	}
}