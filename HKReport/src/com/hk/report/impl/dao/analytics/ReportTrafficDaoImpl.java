package com.hk.report.impl.dao.analytics;

import com.hk.impl.dao.BaseDaoImpl;
import com.hk.report.dto.analytics.TrafficSrcPerformanceDto;
import com.hk.report.pact.dao.analytics.ReportTrafficDao;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: 25 Nov, 2012
 * Time: 7:26:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ReportTrafficDaoImpl extends BaseDaoImpl implements ReportTrafficDao {
	@Override
	public List<TrafficSrcPerformanceDto> getTrafficSrcPerformanceDtoList() {

		String hqlQuery = "select tt.trafficSrc as trafficSrc, tt.trafficSrcPaid as trafficSrcPaid, count(tt.id) as trafficCount, " +
				"count(distinct tt.orderId) as orderCount, sum(tt.firstOrder) as firstOrderCount " +
				"from TrafficTracking tt where date(tt.createDt) = :currentDate " +
				"group by date(tt.createDt), tt.trafficSrc,  tt.trafficSrcPaid order by tt.trafficSrc,  tt.trafficSrcPaid asc";

		return getSession().createQuery(hqlQuery).setParameter("currentDate", (new Date())).setResultTransformer(Transformers.aliasToBean(TrafficSrcPerformanceDto.class)).list();

	}
}
