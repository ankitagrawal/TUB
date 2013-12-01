package com.hk.admin.impl.dao.ticket;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.akube.framework.util.BaseUtils;
import com.akube.framework.util.DateUtils;
import com.hk.admin.dto.ticket.TicketFilterDto;
import com.hk.admin.pact.dao.ticket.TicketDao;
import com.hk.domain.Ticket;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.service.UserService;

@Repository
public class TicketDaoImpl extends BaseDaoImpl implements TicketDao {

    @Autowired
    private UserService userService;

    @Transactional
    public Ticket save(Ticket ticket) {
        if (ticket != null) {
            if (ticket.getCreateDate() == null) {
                ticket.setCreateDate(BaseUtils.getCurrentTimestamp());
            }
            ticket.setUpdateDate(BaseUtils.getCurrentTimestamp());
        }
        return (Ticket) super.save(ticket);
    }

    @SuppressWarnings("unchecked")
    public Page search(TicketFilterDto ticketFilterDto, int page, int perPage) {
        ticketFilterDto.setCreateDateFrom(DateUtils.getStartOfDay(ticketFilterDto.getCreateDateFrom()));
        ticketFilterDto.setCreateDateTo(DateUtils.getEndOfDay(ticketFilterDto.getCreateDateTo()));

        DetachedCriteria criteria = DetachedCriteria.forClass(Ticket.class);
        if (ticketFilterDto.getTicketId() != null) {
            criteria.add(Restrictions.eq("id", ticketFilterDto.getTicketId()));
        }
        if (ticketFilterDto.getOwner() != null) {
            criteria.add(Restrictions.eq("owner", ticketFilterDto.getOwner()));
        }
        if (ticketFilterDto.getReporter() != null) {
            criteria.add(Restrictions.eq("reporter", ticketFilterDto.getReporter()));
        }
        if (ticketFilterDto.getAssociatedOrderId() != null) {
            criteria.add(Restrictions.eq("associatedOrder.id", ticketFilterDto.getAssociatedOrderId()));
        }
        if (ticketFilterDto.getTicketType() != null) {
            criteria.add(Restrictions.eq("ticketType", ticketFilterDto.getTicketType()));
        }
        if (ticketFilterDto.getTicketStatus() != null) {
            criteria.add(Restrictions.eq("ticketStatus", ticketFilterDto.getTicketStatus()));
        }
        if (StringUtils.isNotBlank(ticketFilterDto.getAssociatedLogin())) {
            //UserVO userVO = UserCache.getInstance().getUserByLogin(ticketFilterDto.getAssociatedLogin());
            User associatedUser = getUserService().findByLogin(ticketFilterDto.getAssociatedLogin());
            if (associatedUser != null) {
                criteria.add(Restrictions.eq("associatedUser", associatedUser));
                //criteria.add(Restrictions.eq("associatedUser.id", userVO.getId()));
            } else {
                criteria.add(Restrictions.eq("associatedEmail", ticketFilterDto.getAssociatedLogin()));
            }
        }
        if (StringUtils.isNotBlank(ticketFilterDto.getAssociatedPhone())) {
            criteria.add(Restrictions.eq("associatedPhone", ticketFilterDto.getAssociatedPhone()));
        }
        if (ticketFilterDto.getCreateDateFrom() != null) {
            criteria.add(Restrictions.ge("createDate", ticketFilterDto.getCreateDateFrom()));
        }
        if (ticketFilterDto.getCreateDateTo() != null) {
            criteria.add(Restrictions.le("createDate", ticketFilterDto.getCreateDateTo()));
        }
        if (StringUtils.isNotBlank(ticketFilterDto.getKeywords())) {
            criteria.add(Restrictions.or(Restrictions.like("shortDescription", "%" + ticketFilterDto.getKeywords() + "%"), Restrictions.like("fullDescription", "%"
                    + ticketFilterDto.getKeywords() + "%")));
        }

        return list(criteria, page, perPage);
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
