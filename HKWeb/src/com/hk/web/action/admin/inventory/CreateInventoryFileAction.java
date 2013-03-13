package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.dto.inventory.CreateInventoryFileDto;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.StateList;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Secure(hasAnyPermissions = {PermissionConstants.GRN_CREATION}, authActionBean = AdminPermissionAction.class)
@Component
public class CreateInventoryFileAction extends BaseAction {
    private static Logger logger = Logger.getLogger(CreateInventoryFileAction.class);
    int strLength = 20;
    String brand;
    File printBarcode;

    @Autowired
    private UserService userService;
    @Autowired
    private AdminProductVariantInventoryDao adminproductVariantInventoryDao;
    @Autowired
    // @Named (Keys.Env.barcodeGurgaon)
    // @Value("#{hkEnvProps['barcodeGurgaon']}")
    @Value("#{hkEnvProps['" + Keys.Env.barcodeGurgaon + "']}")
    private String barcodeGurgaon;
    @Autowired
    // @Named (Keys.Env.barcodeMumbai)
    @Value("#{hkEnvProps['" + Keys.Env.barcodeMumbai + "']}")
    private String barcodeMumbai;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/createInventoryFile.jsp");
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @ValidationMethod(on = {"createInventoryFile", "createDetailedInventoryFile"})
    public void validateCategoryAndBrand() {
        if (brand == null) {
            getContext().getValidationErrors().add("1", new SimpleError("Please enter Brand."));
        }
    }

    public Resolution createInventoryFile() {
        User user = null;
        Warehouse userWarehouse = null;
        StringBuffer data = new StringBuffer();

        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        if (userService.getWarehouseForLoggedInUser() != null) {
            userWarehouse = userService.getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(CreateInventoryFileAction.class);
        }
        try {
            List<CreateInventoryFileDto> inventoryFileList = adminproductVariantInventoryDao.getDetailsForUncheckedItems(brand, userWarehouse);
            Iterator<CreateInventoryFileDto> iterator = inventoryFileList.iterator();
            String barcodeFilePath = null;
            if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
                barcodeFilePath = barcodeGurgaon;
            } else {
                barcodeFilePath = barcodeMumbai;
            }
            barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + brand + "_" + user.getId() + "_" + user.getName() + "_"
                    + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";
            printBarcode = new File(barcodeFilePath);

            printBarcode.createNewFile();

            while (iterator.hasNext()) {
                CreateInventoryFileDto dto = iterator.next();
                String date = "";
                String name = dto.getName();
                String barcode = "";
                String groupBarcode = dto.getSkuGroup().getBarcode();
                if (groupBarcode == null) {
                    barcode = dto.getBarcode();
                } else {
                    barcode = groupBarcode;
                }

                Date expiryDate = dto.getExpiryDate();
                Long sumQty = dto.getSumQty();
                ProductVariant productVariant = dto.getProductVariant();
                String productOptionStringBuffer = productVariant.getOptionsSlashSeparated();
                Double markedPrice = 0D;

                if (dto.getSkuGroup().getMrp() != null) {
                    markedPrice = dto.getSkuGroup().getMrp();
                }
//	      } else {
//		      if (dto.getProductVariantInventory().getGrnLineItem() != null && dto.getProductVariantInventory().getGrnLineItem().getMrp() != null) {
//			      markedPrice = dto.getProductVariantInventory().getGrnLineItem().getMrp();
//		      } else if (dto.getProductVariantInventory().getStockTransferLineItem() != null && dto.getProductVariantInventory().getStockTransferLineItem().getMrp() != null) {
//			      markedPrice = dto.getProductVariantInventory().getStockTransferLineItem().getMrp();
//		      } else if (dto.getProductVariantInventory().getRvLineItem() != null && dto.getProductVariantInventory().getRvLineItem().getMrp() != null) {
//			      markedPrice = dto.getProductVariantInventory().getRvLineItem().getMrp();
//		      } else if (productVariant.getMarkedPrice() != null) {
//			      markedPrice = productVariant.getMarkedPrice();
//		      }
//	      }

                if (expiryDate == null) {
                    date = "NA";
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    date = sdf.format(expiryDate);
                }

                data.append(barcode).append("\t").append(getSubString(name, strLength)).append("\t").append(getSubString(productOptionStringBuffer, strLength)).append("\t").append(
                        date).append("\t").append(sumQty).append("\t").append(markedPrice).append("\r\n");

            }
            writeToFile(barcodeFilePath, data.toString());
        } catch (IOException e) {
            logger.error("Exception while appending on barcode file", e);
        } catch (Exception e) {
            logger.error("Error while creating inventory file: ", e);
            addRedirectAlertMessage(new SimpleMessage("Error occurred while creating inventory file"));
            return new RedirectResolution(CreateInventoryFileAction.class);
        }

        addRedirectAlertMessage(new SimpleMessage("Inventory file successfully created"));
        return new HTTPResponseResolution();

    }

    public Resolution createDetailedInventoryFile() {
        User user = null;
        Warehouse userWarehouse = null;
        StringBuffer data = new StringBuffer();

        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        if (userService.getWarehouseForLoggedInUser() != null) {
            userWarehouse = userService.getWarehouseForLoggedInUser();
        } else {
            addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
            return new RedirectResolution(CreateInventoryFileAction.class);
        }
        try {
            List<CreateInventoryFileDto> inventoryFileList = adminproductVariantInventoryDao.getDetailsForUncheckedItems(brand, userWarehouse);
            Iterator<CreateInventoryFileDto> iterator = inventoryFileList.iterator();

            String barcodeFilePath = null;
            if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
                barcodeFilePath = barcodeGurgaon;
            } else {
                barcodeFilePath = barcodeMumbai;
            }
            barcodeFilePath = barcodeFilePath + "/" + "printDetailedBarcode_" + brand + "_" + user.getId() + "_" + user.getName() + "_"
                    + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";
            printBarcode = new File(barcodeFilePath);

            printBarcode.createNewFile();

            while (iterator.hasNext()) {
                CreateInventoryFileDto dto = iterator.next();
                String date = "";
                String name = dto.getName();
                String barcode = "";
                String groupBarcode = dto.getSkuGroup().getBarcode();
                if (groupBarcode == null) {
                    barcode = dto.getBarcode();
                } else {
                    barcode = groupBarcode;
                }
                Date expiryDate = dto.getExpiryDate();
                Long sumQty = dto.getSumQty();
                ProductVariant productVariant = dto.getProductVariant();
                String productOptionStringBuffer = productVariant.getOptionsSlashSeparated();
                String batchNo = dto.getSkuGroup().getBatchNumber();
                Double markedPrice = 0D;

                if (dto.getSkuGroup().getMrp() != null) {
                    markedPrice = dto.getSkuGroup().getMrp();
                }
//                } else {
//                    if (dto.getProductVariantInventory().getGrnLineItem() != null && dto.getProductVariantInventory().getGrnLineItem().getMrp() != null) {
//                        markedPrice = dto.getProductVariantInventory().getGrnLineItem().getMrp();
//                    } else if (dto.getProductVariantInventory().getStockTransferLineItem() != null && dto.getProductVariantInventory().getStockTransferLineItem().getMrp() != null) {
//                        markedPrice = dto.getProductVariantInventory().getStockTransferLineItem().getMrp();
//                    } else if (dto.getProductVariantInventory().getRvLineItem() != null && dto.getProductVariantInventory().getRvLineItem().getMrp() != null) {
//                        markedPrice = dto.getProductVariantInventory().getRvLineItem().getMrp();
//                    } else if (productVariant.getMarkedPrice() != null) {
//                        markedPrice = productVariant.getMarkedPrice();
//                    }
//                }

                if (expiryDate == null) {
                    date = "NA";
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                    date = sdf.format(expiryDate);

                }

                data.append(productVariant.getId()).append("\t").append(barcode).append("\t").append(name).append("\t").append(productOptionStringBuffer)
                        .append("\t").append(date).append("\t").append(sumQty).append("\t").append(markedPrice).append("\t").append(batchNo).append("\r\n");

            }

            writeToFile(barcodeFilePath, data.toString());
        } catch (IOException e) {
            logger.error("Exception while appending on barcode file", e);
        } catch (Exception e) {
            logger.error("Error while creating inventory file: ", e);
            addRedirectAlertMessage(new SimpleMessage("Error occurred while creating inventory file"));
            return new RedirectResolution(CreateInventoryFileAction.class);
        }

        addRedirectAlertMessage(new SimpleMessage("Inventory file successfully created"));
        return new HTTPResponseResolution();

    }

    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            InputStream in = new BufferedInputStream(new FileInputStream(printBarcode));
            res.setContentType("text/plain");
            res.setCharacterEncoding("UTF-8");
            res.setContentLength((int) printBarcode.length());
            res.setHeader("Content-Disposition", "attachment; filename=\"" + printBarcode.getName() + "\";");
            OutputStream out = res.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[4096];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
        }
    }

    private String getSubString(String s, int length) {
        if (s.length() < length)
            return s;

        return s.substring(0, length);
    }

    private void writeToFile(String filePath, String data) throws IOException {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(filePath, false);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(data);
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();
            } catch (Exception ex) {

            }
        }

    }
}
