Six Hourly Category Performance Report From ${startSixHour} to ${endSixHour}
<html>
<head>
    <title>Category Sales Report </title>
</head>
  <body>
  <strong> From ${startSixHour} to ${endSixHour}</strong>
     <strong>Six Hour's Count </strong><br><br>
    <strong>Six Hour's Orders# ${sixHourlyCategoryOrderReportDtosMap["SixHourlyCategoriesReportDto"].totalOrders}</strong> &nbsp;|&nbsp;
    <strong>Six Hour's Sale#
       ${sixHourlyCategoryOrderReportDtosMap.SixHourlyCategoriesReportDto.totalSumOfMrp}</strong> &nbsp;|&nbsp;
    <strong>Six Hour's Distinct Orders# ${sixHourlyCategoryOrderReportDtosMap["SixHourlyCategoriesReportDto"].totalDistinctOrders}</strong> &nbsp;|&nbsp;
    <strong>Six Hour's Mix Orders# ${sixHourlyCategoryOrderReportDtosMap["SixHourlyCategoriesReportDto"].totalMixedOrder}</strong>

    <br><br><br>
    <strong>Today's Count</strong><br><br>
    <strong>Total Orders# ${sixHourlyCategoryOrderReportDtosMap.DailyCategoriesReportDto.totalOrders}</strong> &nbsp;|&nbsp;
    <strong>Total Sale#
        ${sixHourlyCategoryOrderReportDtosMap.DailyCategoriesReportDto.totalSumOfMrp}</strong> &nbsp;|&nbsp;
    <strong>Total Distinct Orders# ${sixHourlyCategoryOrderReportDtosMap.DailyCategoriesReportDto.totalDistinctOrders}</strong> &nbsp;|&nbsp;
    <strong>Total Mix Orders# ${sixHourlyCategoryOrderReportDtosMap.DailyCategoriesReportDto.totalMixedOrder}</strong><br/><br/>



    <table align="center"  border = "1">
            <thead bgcolor="#ccccc">
      <tr>
        <th>CATEGORY</th>
        <th>TODAY'S COUNT </th>
        <th>TODAY'S  SALES</th>
        <th>SIX HOUR'S COUNT</th>
        <th>SIX HOUR'S SALES</th>
      </tr>
      </thead>
      <#assign ctr=0>
            <#list sixHourlyCategoryOrderReportDtosMap.DailyCategoriesReportDto.categoryPerformanceDtoList as categoryPerformanceDto>
        <tr>
          <td>
              ${categoryPerformanceDto.category.displayName}
          </td>
          <td>
              ${categoryPerformanceDto.totalOrders}
          </td>
          <td>
             ${categoryPerformanceDto.sumOfMrp}
          </td>
          <td>
              ${sixHourlyCategoryOrderReportDtosMap.SixHourlyCategoriesReportDto.categoryPerformanceDtoList[ctr].totalOrders}
          </td>
          <td>
              ${sixHourlyCategoryOrderReportDtosMap.SixHourlyCategoriesReportDto.categoryPerformanceDtoList[ctr].sumOfMrp}
          </td>
           <#assign ctr=ctr+1>
        </tr>
        </#list>
    </table>
  </body>
</html>