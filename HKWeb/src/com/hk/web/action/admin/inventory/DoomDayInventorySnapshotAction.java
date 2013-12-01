package com.hk.web.action.admin.inventory;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.pact.dao.DoomDayDao;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.service.inventory.InventoryService;

@Component
public class DoomDayInventorySnapshotAction extends BaseAction {
    @Autowired
    ProductVariantDao            productVariantDao;
    @Autowired
    InventoryService             inventoryService;
    @Autowired
    DoomDayDao                   doomDayDao;

    private String               barcode;
    private String               qty;

    private List<ProductVariant> productVariants;
    private ProductVariant       productVariant;

    @DontValidate
    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/inventorySnapshot.jsp");
    }

    public Resolution saveSnapshot() {
        if (StringUtils.isBlank(barcode)) {
            addRedirectAlertMessage(new SimpleMessage("Please enter the barcode"));
        }
        if (StringUtils.isBlank(qty)) {
            addRedirectAlertMessage(new SimpleMessage("Please enter the qty"));
        }

        productVariants = productVariantDao.findVariantFromBarcode(barcode);
        if (productVariants == null || productVariants.isEmpty()) {
            addRedirectAlertMessage(new SimpleMessage("No product found"));
        } else if (productVariants.size() == 1) {
            if (barcode.equals(qty)) {
                doomDayDao.saveSnapShot(barcode, 1L);
                addRedirectAlertMessage(new SimpleMessage("SAVED!!!"));
                String productVariantSaved = productVariants.get(0).getProduct().getName() + "\t" + productVariants.get(0).getOptionsCommaSeparated();
                return new RedirectResolution(DoomDayInventorySnapshotAction.class).addParameter("productVariantSaved", productVariantSaved).addParameter("variantSaved", "true");
            } else {
                try {
                    doomDayDao.saveSnapShot(barcode, Long.valueOf(qty));
                    addRedirectAlertMessage(new SimpleMessage("SAVED!!!"));
                    String productVariantSaved = productVariants.get(0).getProduct().getName() + "\t" + productVariants.get(0).getOptionsCommaSeparated();
                    return new RedirectResolution(DoomDayInventorySnapshotAction.class).addParameter("productVariantSaved", productVariantSaved).addParameter("variantSaved",
                            "true");
                } catch (NumberFormatException e) {
                    addRedirectAlertMessage(new SimpleMessage("Quantity should be a number or should be equal to the barcode"));
                }
            }
        } else {

            if (productVariant != null) {
                try {
                    if (barcode.equals(qty)) {
                        doomDayDao.saveSnapShot(productVariant.getId(), 1L);
                        addRedirectAlertMessage(new SimpleMessage("SAVED!!!"));
                    } else {
                        doomDayDao.saveSnapShot(productVariant.getId(), Long.valueOf(qty));
                        addRedirectAlertMessage(new SimpleMessage("SAVED!!!"));
                    }
                } catch (NumberFormatException e) {
                    addRedirectAlertMessage(new SimpleMessage("Quantity should be a number or should be equal to the barcode"));
                }
            } else {
                addRedirectAlertMessage(new SimpleMessage("Multiple variants available. Please select appropriate variant."));
                RedirectResolution redirectResolution = new RedirectResolution(DoomDayInventorySnapshotAction.class).addParameter("qty", qty).addParameter("barcode", barcode).addParameter(
                        "showVariants", "true");
                for (ProductVariant productVariant : productVariants) {
                    redirectResolution.addParameter("productVariants", productVariant.getId());
                }
                return redirectResolution;
            }

        }
        return new RedirectResolution(DoomDayInventorySnapshotAction.class);
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<ProductVariant> getProductVariants() {
        return productVariants;
    }

    public void setProductVariants(List<ProductVariant> productVariants) {
        this.productVariants = productVariants;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
