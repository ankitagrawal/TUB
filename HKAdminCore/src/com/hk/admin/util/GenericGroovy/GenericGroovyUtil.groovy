package com.hk.admin.util.GenericGroovy

import groovy.sql.Sql
import org.slf4j.LoggerFactory
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException

/**
 * Created by IntelliJ IDEA.
 * User: Tarun
 * Date: April 13, 2012
 * Time: 01:28:07 PM
 * To change this template use File | Settings | File Templates.
 */



public class GenericGroovyUtil {
	private String hostName;
	private String dbName;
	private String serverUser;
	private String serverPassword;
	Sql sql;
	Sql busySql;

	GenericGroovyUtil(String hostName, String dbName, String serverUser, String serverPassword) {
		this.hostName = hostName;
		this.dbName = dbName;
		this.serverUser = serverUser;
		this.serverPassword = serverPassword;

		sql = Sql.newInstance("jdbc:mysql://" + hostName + ":3306/" + dbName, serverUser,
				serverPassword, "com.mysql.jdbc.Driver");

	}

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(GenericGroovyUtil.class);


	public void performScript() {
		String productOptionName;
		String productOptionValue;
		Long masterId;
		Long counter;
		List<Long> idsToBeDeleted = new ArrayList<Long>();
		sql.eachRow("""
                    select count(*), name , value  from product_option   group by name, value having count(*) > 1""") {
			duplicateOptions ->
			productOptionName = duplicateOptions.name;
			productOptionValue = duplicateOptions.value;

//			if(productOptionName.contains("'")){
////                logger.info(productOptionName);
//                productOptionName = productOptionName.replace("'", "\\'");
////                logger.info("after replace"+productOptionName);
//			}
//
//			if(productOptionValue.contains("'")){
//                productOptionValue = productOptionValue.replace("'", "\\'");
//			}
			masterId=null;
			counter=0;
			sql.eachRow("""Select * from product_option  where name= ${productOptionName} and value= ${productOptionValue} """){
				productOption ->
				if(masterId == null && counter ==0){
					masterId = productOption.id;
				}

				if(masterId != null && counter > 0){
//					    sql.executeUpdate("""Update product_variant_has_product_option Set product_option_id = ${masterId} where product_option_id=${productOption.id}""");
                    sql.eachRow("""Select * from product_variant_has_product_option where product_option_id=${productOption.id}""") {
                        variantOption ->
                        try{
                            sql.executeUpdate("""Update product_variant_has_product_option Set product_option_id = ${masterId} where product_variant_id=${variantOption.product_variant_id} AND product_option_id=${productOption.id}""");
                        } catch(MySQLIntegrityConstraintViolationException e){
                            logger.error(e.message);
                            sql.execute("""Delete pvpo from product_variant_has_product_option pvpo where product_variant_id=${variantOption.product_variant_id} AND product_option_id=${productOption.id} """)
                        }
                    }
					idsToBeDeleted.add(productOption.id);
//					sql.execute(""""Delete from product_option where id=${productOption.id}""");
				}
				counter++;
			}

		}
		for(Long poid : idsToBeDeleted){
			sql.execute("""Delete po from product_option po where id=${poid}""");
		}

	}
}