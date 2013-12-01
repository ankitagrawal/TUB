package com.hk.web.action.report;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.report.dto.analytics.TrafficSrcPerformanceDto;
import com.hk.report.pact.service.analytics.ReportTrafficService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.List;

@Secure (hasAllRoles = {RoleConstants.MARKETING}, authActionBean = AdminPermissionAction.class)
@Component
public class AdvReportAction extends BaseAction {

	@Autowired
	ReportTrafficService reportTrafficService;

	private List<TrafficSrcPerformanceDto> srcPerformanceDtoList;

	@DefaultHandler
	@DontValidate
	public Resolution pre() {
		srcPerformanceDtoList = reportTrafficService.getTrafficSrcPerformanceDtoList();
		return new ForwardResolution("/pages/admin/advReport.jsp");
	}

	public List<TrafficSrcPerformanceDto> getSrcPerformanceDtoList() {
		return srcPerformanceDtoList;
	}
}