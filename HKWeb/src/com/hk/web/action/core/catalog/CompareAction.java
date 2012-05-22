package com.hk.web.action.core.catalog;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.Product;
import com.hk.pact.dao.catalog.product.ProductDao;
import com.hk.report.dto.Table;

@Component
public class CompareAction extends BaseAction {

    Table                   table;
    List<Product>           products = new ArrayList<Product>();

    @Autowired
    ProductDao              productDao;


    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Resolution createTable() {
        Table table = new Table();
        int productNo = 1;
        for (Product product : products) {
            table.addToTable(product, productNo);
            productNo++;
        }
        this.table = table;
        return new ForwardResolution("/pages/comparison.jsp");
    }
}
