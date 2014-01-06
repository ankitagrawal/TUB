package com.hk.web.action.admin.booking;

import java.util.*;

import com.hk.domain.sku.ForeignSkuItemCLI;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItemCLI;
import com.hk.domain.sku.SkuItemLineItem;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.error.AdminPermissionAction;

@Component
@Secure
public class AdminBookingAction extends BaseAction {

	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	OrderService orderService;
	@Autowired
	SkuItemLineItemService skuItemLineItemService;
	@Autowired
	InventoryService inventoryService;
    @Autowired
    SkuItemLineItemDao skuItemLineItemDao;

	private Long shippingOrderId;
	private Long baseOrderId;
    private Long cartLineItemId;

	private List<SkuItemCLI> skuCLIList;
	private List<SkuItemLineItem> skuLiList;
	private HashMap<ShippingOrder, List<SkuItemLineItem>> soSiLiMap;

    private List<ForeignSkuItemCLI> foreignSkuItemCLIList;

	public Resolution getSkuItemLineItems() {
		if (shippingOrderId != null) {
			ShippingOrder so = shippingOrderService.find(shippingOrderId);
			skuLiList = new ArrayList<SkuItemLineItem>();
			for (LineItem li : so.getLineItems()) {
				if (li.getSkuItemLineItems() != null && li.getSkuItemLineItems().size() > 0) {
					skuLiList.addAll(li.getSkuItemLineItems());
				}
			}
		}
		return new ForwardResolution("/pages/admin/bookingStatusForSO.jsp");
	}

	public Resolution getSkuCartItemLineItems() {
		if (baseOrderId != null) {
			soSiLiMap = new HashMap<ShippingOrder, List<SkuItemLineItem>>();
			Order bo = orderService.find(baseOrderId);
			skuCLIList = new ArrayList<SkuItemCLI>();
			for (CartLineItem cli : bo.getCartLineItems()) {
				if (cli.getSkuItemCLIs() != null && cli.getSkuItemCLIs().size() > 0)
					skuCLIList.addAll(cli.getSkuItemCLIs());
			}

			for (ShippingOrder shippingOrder : bo.getShippingOrders()) {
				List<SkuItemLineItem> boSoSiLi = new ArrayList<SkuItemLineItem>();
				for (LineItem li : shippingOrder.getLineItems()) {
					if (li.getSkuItemLineItems() != null && li.getSkuItemLineItems().size() > 0) {
						boSoSiLi.addAll(li.getSkuItemLineItems());
					}
				}
				if (boSoSiLi != null && boSoSiLi.size() > 0) {
					soSiLiMap.put(shippingOrder, boSoSiLi);
				}
			}
		}
		return new ForwardResolution("/pages/admin/bookingStatusForSO.jsp");
	}

    public Resolution getForeignBookingStatus(){
        if(baseOrderId!= null){
            Order bo = orderService.find(baseOrderId);
            Set<CartLineItem> cartLineItemList = bo.getCartLineItems();
            for(CartLineItem cartLineItem:cartLineItemList) {
                foreignSkuItemCLIList = skuItemLineItemDao.getForeignSkuItemCli(cartLineItem);
            }
        }
        return new ForwardResolution("/pages/admin/bookingStatusForFiCli.jsp");
    }

	@Secure(hasAnyRoles = { RoleConstants.GOD }, authActionBean = AdminPermissionAction.class)
	public Resolution freeBookingTable() {
		if (shippingOrderId != null) {
			ShippingOrder so = shippingOrderService.find(shippingOrderId);
      List <LineItem> problamaticItems = skuItemLineItemService.freeBooking(so);
      if (problamaticItems != null && problamaticItems.size() > 0){
        addRedirectAlertMessage(new SimpleMessage(" Failed to Freed Booking Table For Shipping Order: " + so.getId()));
      }else {
        addRedirectAlertMessage(new SimpleMessage("Freed Booking Table For Shipping Order: " + so.getId()));
      }
			return new RedirectResolution(AdminBookingAction.class).addParameter("getSkuItemLineItems").addParameter("shippingOrderId", shippingOrderId);
		} else if (baseOrderId != null) {
			Order bo = orderService.find(baseOrderId);

      boolean invnFreed = true;
      List<CartLineItem>  problamaticCartlineItems = new ArrayList<CartLineItem>();
      Set<CartLineItem> cartLineItems = bo.getCartLineItems();
      if (cartLineItems != null && cartLineItems.size() > 0){
        for (CartLineItem cli : cartLineItems){
        invnFreed =  skuItemLineItemService.freeBookingItem(cli.getId());
          if (!invnFreed){
            problamaticCartlineItems.add(cli);
          }

        }
      }
      if (problamaticCartlineItems != null && problamaticCartlineItems.size() > 0){
        addRedirectAlertMessage(new SimpleMessage("Failed to Freed Booking Table For Base Order: " + bo.getId()));
      }else {
        addRedirectAlertMessage(new SimpleMessage("Freed Booking Table For Base Order: " + bo.getId()));
      }
			return new RedirectResolution(AdminBookingAction.class).addParameter("getSkuCartItemLineItems").addParameter("baseOrderId", baseOrderId);
		}
		return null;
	}


  @Secure(hasAnyRoles = { RoleConstants.GOD }, authActionBean = AdminPermissionAction.class)
  public Resolution freeBookingLineItem() {
   boolean invFreed =  skuItemLineItemService.freeBookingItem(cartLineItemId);
   CartLineItem cartLineItem = getBaseDao().get(CartLineItem.class, cartLineItemId);
   inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
   if (!invFreed) {
     addRedirectAlertMessage(new SimpleMessage(" Failed to Free Booking  For item  " +  cartLineItemId));
   }else{
    addRedirectAlertMessage(new SimpleMessage("  Successfully  Freed Booking  For item  " +  cartLineItemId));
   }
    return new RedirectResolution(AdminBookingAction.class).addParameter("getSkuCartItemLineItems").addParameter("baseOrderId", baseOrderId);
  }


	public Resolution closeWindow() {
		return new ForwardResolution("/pages/close.jsp");
	}

	public Long getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(Long shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}

	public Long getBaseOrderId() {
		return baseOrderId;
	}

	public void setBaseOrderId(Long baseOrderId) {
		this.baseOrderId = baseOrderId;
	}

	public List<SkuItemCLI> getSkuCLIList() {
		return skuCLIList;
	}

	public void setSkuCLIList(List<SkuItemCLI> skuCLIList) {
		this.skuCLIList = skuCLIList;
	}

	public List<SkuItemLineItem> getSkuLiList() {
		return skuLiList;
	}

	public void setSkuLiList(List<SkuItemLineItem> skuLiList) {
		this.skuLiList = skuLiList;
	}

	public HashMap<ShippingOrder, List<SkuItemLineItem>> getSoSiLi() {
		return soSiLiMap;
	}

	public void setSoSiLi(HashMap<ShippingOrder, List<SkuItemLineItem>> soSiLi) {
		this.soSiLiMap = soSiLi;
	}

	public HashMap<ShippingOrder, List<SkuItemLineItem>> getSoSiLiMap() {
		return soSiLiMap;
	}

	public void setSoSiLiMap(HashMap<ShippingOrder, List<SkuItemLineItem>> soSiLiMap) {
		this.soSiLiMap = soSiLiMap;
	}

  public Long getCartLineItemId() {
    return cartLineItemId;
  }

  public void setCartLineItemId(Long cartLineItemId) {
    this.cartLineItemId = cartLineItemId;
  }

    public List<ForeignSkuItemCLI> getForeignSkuItemCLIList() {
        return foreignSkuItemCLIList;
    }

    public void setForeignSkuItemCLIList(List<ForeignSkuItemCLI> foreignSkuItemCLIList) {
        this.foreignSkuItemCLIList = foreignSkuItemCLIList;
    }

}
