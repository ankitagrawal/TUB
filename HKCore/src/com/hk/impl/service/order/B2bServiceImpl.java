package com.hk.impl.service.order;

import org.springframework.stereotype.Service;

import com.hk.domain.order.B2BOrderCheckList;
import com.hk.domain.order.Order;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.service.order.B2BOrderService;

@Service
public class B2bServiceImpl extends BaseDaoImpl implements B2BOrderService {

	@Override
	public boolean checkCForm(Order order) {
		if (order != null && order.isB2bOrder() == true) {
			Long id = order.getId();
			B2BOrderCheckList b2bOrderCheckList = (B2BOrderCheckList) getSession()
					.createQuery("from B2BOrderCheckList bc where bc.base_order_id = :id").setParameter("id", id).uniqueResult();
			if (b2bOrderCheckList != null)
				return b2bOrderCheckList.iscForm();
			else
				return false;

		} else
			return false;
	}

	@Override
	public void saveB2BOrder(B2BOrderCheckList checkList) {
		Long id = checkList.getBase_order_id();

		B2BOrderCheckList b2bOrderCheckList = (B2BOrderCheckList) getSession()
				.createQuery("from B2BOrderCheckList bc where bc.base_order_id = :id").setParameter("id", id).uniqueResult();

		if (b2bOrderCheckList != null) {
			b2bOrderCheckList.setcForm(checkList.iscForm());
			super.save(b2bOrderCheckList);
		}
		else
			super.save(checkList);
	}

}
