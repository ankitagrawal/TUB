Daily Google Banned Words Report
<html>
<head>
    <title>Google Banned Words Report</title>
</head>
<body>
<strong></strong>
<strong>Yesterday's (${yesterdayDate?date}) Google Banned Words Report</strong><br><br>

<br><br><br>


<table border="1">
    <thead bgcolor="#ccccc">
    <tr>
        <th>BANNED WORD</th>
        <th>PRODUCT NAME</th>
        <th>PRODUCT ID</th>
        <#--<th>EXISTS IN PRODUCT NAME</th>
        <th>EXISTS IN PRODUCT OVERVIEW</th>
        <th>EXISTS IN PRODUCT DESCRIPTION</th>
        <th>EXISTS IN PRODUCT FEATURE NAME</th>
        <th>EXISTS IN PRODUCT FEATURE VALUE</th>-->
    </tr>
    </thead>

    <#list googleBannedWordList as googleBannedWordDto>
    <tr>
        <td>
            ${googleBannedWordDto.googleBannedWord.bannedWord}
        </td>
        <td>
            ${googleBannedWordDto.product.name}
        </td>
        <td>
            ${googleBannedWordDto.product.id}
        </td>
        <#--<td>
            ${googleBannedWordDto.existsInProductName}
        </td>
        <td>
            ${googleBannedWordDto.existsInProductOverview}
        </td>
        <td>
            ${googleBannedWordDto.existsInProductDescription}
        </td>
        <td>
            ${googleBannedWordDto.existsInProductFeatureName}
        </td>
        <td>
            ${googleBannedWordDto.existsInProductFeatureValue}
        </td>-->

    </tr>
    </#list>
</table>


</body>
</html>