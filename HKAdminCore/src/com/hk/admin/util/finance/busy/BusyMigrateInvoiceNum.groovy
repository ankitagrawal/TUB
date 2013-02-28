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

		busySql = Sql.newInstance("jdbc:mysql://" + hostName + ":3306/healthkart_busy", serverUser,
				serverPassword, "com.mysql.jdbc.Driver");
	}

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyMigrateInvoiceNum.class);

	public void updateSalesInvoiceNumber() {
	/*	sql.execute("""
								ALTER TABLE  `shipping_order` ADD  `accounting_invoice_number` VARCHAR( 20 ) NULL;
		""")*/
		
		busySql.eachRow("""
                    SELECT hk_ref_no AS so_id, vch_no AS invoice_num 
                    FROM transaction_header
                    WHERE vch_type=9
                      """) {
			busyOrders ->
			Long shippingOrderId = busyOrders.so_id;
			String invoiceNumber = busyOrders.invoice_num;

			try {
				sql.executeUpdate("""
                    UPDATE shipping_order so SET accounting_invoice_num = ${invoiceNumber} WHERE id = ${shippingOrderId};
     """);
			}
			catch (Exception e) {
				logger.info("Error for shipping order id: ${shippingOrderId}", e);
			}

		}


	}
}