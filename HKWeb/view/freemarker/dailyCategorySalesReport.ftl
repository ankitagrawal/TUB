Daily Category Performance Report
<html>
<head>
    <title>Category Sales Report</title>
</head>
  <body>
    <strong></strong>
    <strong>Yesterday's (${yesterdayDate?date}) Count</strong><br><br>
    <strong>Yesterday Orders# ${categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.totalOrders}</strong> &nbsp;|&nbsp;
    <strong>Yesterday Sale ${categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.totalSumOfMrp?string(",##0")}</strong> &nbsp;|&nbsp;
    <strong>Yesterday Distinct Orders# ${categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.totalDistinctOrders}</strong> &nbsp;|&nbsp;
    <strong>Yesterday Mix Orders# ${categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.totalMixedOrder}</strong>

    <br><br><br>
    <strong>Monthly Count</strong><br><br>
    <strong>Total Orders# ${categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.totalOrders}</strong> &nbsp;|&nbsp;
    <strong>Total Sale ${categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.totalSumOfMrp?string(",##0")}</strong> &nbsp;|&nbsp;
    <strong>Total Distinct Orders# ${categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.totalDistinctOrders}</strong> &nbsp;|&nbsp;
    <strong>Total Mix Orders# ${categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.totalMixedOrder}</strong><br><br>


    <table>
      <tr>
        <td>
          <table  border = "1">
            <thead bgcolor="#ccccc">
            <tr>
              <th>CATEGORY</th>
              <th>MTD AVG #</th>
              <th>MTD AVG SALES</th>
              <th>TARGET #</th>
              <th>DAILY TARGET</th>
              <th>YEST #</th>
              <th>YEST SALES</th>
              <th>MTD SALES</th>
              <th>PROJ SALES</th>
              <th>TARGET SALES</th>
              <th>TARGET vs PROJECTED &Delta;</th>

            </tr>
            </thead>
            <#assign ctr=0>
            <#list categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.categoryPerformanceDtoList as categoryPerformanceDto>
              <tr>
                <#assign catName = categoryPerformanceDto.category.name>
                <td>
                    ${categoryPerformanceDto.category.displayName}
                </td>
                <td>
                    ${categoryPerformanceDto.avgOfTotalOrders?string(",##0")}
                </td>
                <td>
                    ${categoryPerformanceDto.avgOfMrp?string(",##0")}
                </td>
                <td>
                    ${targetOrderCountMap[catName]}
                </td>
                <td>
                    ${targetDailyMrpSalesMap[catName]}
                </td>
                <td>
                    ${categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList[ctr].totalOrders}
                    <#if (categoryPerformanceDto.avgOfTotalOrders == categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList[ctr].totalOrders)>
                      &cong;
                    <#elseif (categoryPerformanceDto.avgOfTotalOrders < categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList[ctr].totalOrders) >
                      &uarr;
                    <#else>
                      &darr;
                    </#if>
                </td>
                <td>
                    ${categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList[ctr].sumOfMrp?string(",##0")}
                    <#if (categoryPerformanceDto.avgOfMrp == categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList[ctr].sumOfMrp)>
                      &cong;
                    <#elseif (categoryPerformanceDto.avgOfMrp < categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList[ctr].sumOfMrp) >
                      &uarr;
                    <#else>
                      &darr;
                    </#if>

                </td>
                <td>
                   ${categoryPerformanceDto.sumOfMrp?string(",##0")}
                </td>
                <td>
                    ${categoryPerformanceDto.projectedMrp?string(",##0")}
                </td>
                <td>
                    ${targetMrpSalesMap[catName]}
                </td>

                <td align="center">
                  <#if (categoryPerformanceDto.projectedMrp == targetMrpSalesMap[catName])>
                    &cong;
                  <#elseif (categoryPerformanceDto.projectedMrp > targetMrpSalesMap[catName])>
                    &uarr; ${((categoryPerformanceDto.projectedMrp - targetMrpSalesMap[catName])/targetMrpSalesMap[catName]*100)?string(",##0")}% &uarr;
                  <#else>
                    &darr; ${((targetMrpSalesMap[catName] - categoryPerformanceDto.projectedMrp)/targetMrpSalesMap[catName]*100)?string(",##0")}% &darr;
                  </#if>
                </td>
              <#assign ctr=ctr+1>
              </tr>
            </#list>
          </table>
        </td>
        <#--<td>
          <table  border = "2" >
            <thead>
            <tr>
              <th>YEST #</th>
              <th>YEST SALES</th>
            </tr>
            </thead>

            <#list categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList as categoryPerformanceDto>
              <tr>

                <td>
                    ${categoryPerformanceDto.totalOrders}
                </td>
                <td>
                   ${categoryPerformanceDto.sumOfMrp?string(",##0")}
                </td>

              </tr>
            </#list>
          </table>
        </td>-->
        <#--<td>
          <table  border = "1">
            <thead>
            <tr>
              <th>MTD SALES</th>
              <th>PROJ SALES</th>
              <th>TARGET SALES</th>
            </tr>
            </thead>

            <#list categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.categoryPerformanceDtoList as categoryPerformanceDto>
              <tr>
                  <td>
                      ${categoryPerformanceDto.sumOfMrp?string(",##0")}
                  </td>
                  <td>
                      ${categoryPerformanceDto.projectedMrp?string(",##0")}
                  </td>
                  <#assign catName = categoryPerformanceDto.category.name>
                  <td>
                      ${targetMrpSalesMap[catName]}
                  </td>
              </tr>
            </#list>
          </table>
        </td>-->
      </tr>
    </table>


  </body>
</html>