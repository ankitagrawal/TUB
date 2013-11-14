package com.hk.admin.util.finance.busy

import groovy.sql.Sql
import org.slf4j.LoggerFactory
import org.slf4j.Logger

/**
 * Created by IntelliJ IDEA.
 * User: Tarun
 * Date: April 13, 2012
 * Time: 01:28:07 PM
 * To change this template use File | Settings | File Templates.
 */


public class MenuTest {

  private static String hostName;
  private static String dbName;
  private static String serverUser;
  private static String serverPassword;
  static Sql sql;
  static Sql qaSql;
	public static void main(String[] args){
  //MenuTest(String hostName, String dbName, String serverUser, String serverPassword){
    hostName = "192.168.1.112";
    dbName = "healthkart_catalog_wb";
    serverUser = "hkadmin";
    serverPassword = "admin2K11!";

		String allForFourth, allForThird, allForSecond, allForFirst;

    sql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbName, serverUser,
            serverPassword, "com.mysql.jdbc.Driver");

    qaSql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/healthkart_qa_backup", serverUser,
            serverPassword, "com.mysql.jdbc.Driver");

		try{
			qaSql.execute(""" ALTER TABLE product ADD COLUMN tracked integer default 0; """);

		} catch(Exception  e){
			System.out.println("column already exists");
		}

		try{
			qaSql.execute(""" ALTER TABLE product ADD COLUMN lowest_cat varchar(100); """);

		} catch(Exception  e){
			System.out.println("column already exists");
		}

		try {
			qaSql.execute(""" ALTER TABLE product ADD COLUMN all_cat varchar(300); """);

		} catch (Exception e) {
			System.out.println("column already exists");
		}

		qaSql.execute(""" update product set tracked = 0, all_cat = '', lowest_cat = ''; """);

		System.out.println("hello world");
		//for 4th level
		sql.eachRow(""" select * from my_menu """){
			meraMenu ->

			String l1,l2,l3,l4,l5;
			l1 = meraMenu.l1;
			l2 = meraMenu.l2;
			l3 = meraMenu.l3;
			l4 = meraMenu.l4;
			l5 = meraMenu.l5;
			//System.out.println(l1+" "+l2+" "+l3+" "+l4+" "+l5);

			qaSql.eachRow(""" select p.id as p_id from product p,
						category_has_product chp,
						category c
						where p.id = chp.product_id
						and c.name = chp.category_name
						and ${l1} is not null and ${l2} is not null
						and ${l3} is not null and ${l4} is not null
						and c.name in (${l1},${l2},${l3},${l4})
						and p.tracked = 0
						group by p.id having count(*) = 4; """){

				fourthLevelProduct ->

				allForFourth = l1+"-"+l2+"-"+l3+"-"+l4;
				System.out.println(l1+" "+l2+" "+l3+" "+l4+" "+l5);
				System.out.println(fourthLevelProduct.p_id);
				qaSql.execute(""" update product set tracked = 4 , lowest_cat = ${l4} , all_cat = ${allForFourth}
													where id = ${fourthLevelProduct.p_id} """);

			
			}

			
		}

		//for 3rd level
		sql.eachRow(""" select * from my_menu """){
			meraMenu ->

			String l1,l2,l3,l4,l5;
			l1 = meraMenu.l1;
			l2 = meraMenu.l2;
			l3 = meraMenu.l3;
			l4 = meraMenu.l4;
			l5 = meraMenu.l5;
			//System.out.println(l1+" "+l2+" "+l3+" "+l4+" "+l5);

			qaSql.eachRow(""" select p.id as p_id from product p,
						category_has_product chp,
						category c
						where p.id = chp.product_id
						and c.name = chp.category_name
						and ${l1} is not null and ${l2} is not null
						and ${l3} is not null
						and c.name in (${l1},${l2},${l3})
						and p.tracked = 0
						group by p.id having count(*) = 3; """){

				thirdLevelProduct ->

				allForThird = l1+"-"+l2+"-"+l3;
				System.out.println(l1+" "+l2+" "+l3+" "+l4+" "+l5);
				System.out.println(thirdLevelProduct.p_id);
				qaSql.execute(""" update product set tracked = 3 , lowest_cat = ${l3}  , all_cat = ${allForThird}
													where id = ${thirdLevelProduct.p_id} """);


			}


		}

		//for 2nd level
		sql.eachRow(""" select * from my_menu """){
			meraMenu ->

			String l1,l2,l3,l4,l5;
			l1 = meraMenu.l1;
			l2 = meraMenu.l2;
			l3 = meraMenu.l3;
			l4 = meraMenu.l4;
			l5 = meraMenu.l5;
			//System.out.println(l1+" "+l2+" "+l3+" "+l4+" "+l5);

			qaSql.eachRow(""" select p.id as p_id from product p,
						category_has_product chp,
						category c
						where p.id = chp.product_id
						and c.name = chp.category_name
						and ${l1} is not null and ${l2} is not null

						and c.name in (${l1},${l2})
						and p.tracked = 0
						group by p.id having count(*) = 2; """){

				secondLevelProduct ->

				allForSecond = l1+"-"+l2;
				System.out.println(l1+" "+l2+" "+l3+" "+l4+" "+l5);
				System.out.println(secondLevelProduct.p_id);
				qaSql.execute(""" update product set tracked = 2 , lowest_cat = ${l2} , all_cat = ${allForSecond}
													where id = ${secondLevelProduct.p_id} """);


			}


		}

				//for 1st level
		sql.eachRow(""" select * from my_menu """){
			meraMenu ->

			String l1,l2,l3,l4,l5;
			l1 = meraMenu.l1;
			l2 = meraMenu.l2;
			l3 = meraMenu.l3;
			l4 = meraMenu.l4;
			l5 = meraMenu.l5;
			//System.out.println(l1+" "+l2+" "+l3+" "+l4+" "+l5);

			qaSql.eachRow(""" select p.id as p_id from product p,
						category_has_product chp,
						category c
						where p.id = chp.product_id
						and c.name = chp.category_name
						and ${l1} is not null

						and c.name in (${l1})
						and p.tracked = 0
						group by p.id having count(*) = 1; """){

				firstLevelProduct ->
				allForFirst = l1;
				System.out.println(l1+" "+l2+" "+l3+" "+l4+" "+l5);
				System.out.println(firstLevelProduct.p_id);
				qaSql.execute(""" update product set tracked = 1 , lowest_cat = ${l1} , all_cat = ${allForFirst}
													where id = ${firstLevelProduct.p_id} """);


			}


		}

  }
  private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyPopulateItemData.class);


}
