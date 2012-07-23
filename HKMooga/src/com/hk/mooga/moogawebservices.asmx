<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions xmlns:s="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:tns="http://ikensolution.com/webservices/" xmlns:s1="http://ikensolution.com/webservices/AbstractTypes" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:tm="http://microsoft.com/wsdl/mime/textMatching/" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" targetNamespace="http://ikensolution.com/webservices/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
  <wsdl:types>
    <s:schema elementFormDefault="qualified" targetNamespace="http://ikensolution.com/webservices/">
      <s:element name="Login">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="p_User" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Application" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="LoginResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="LoginResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="DirectLogin">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="p_User" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="DirectLoginResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="DirectLoginResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Logout">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Application" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="LogoutResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="LogoutResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="UsersLoggedIn">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="p_Application" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="UsersLoggedInResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="UsersLoggedInResult" type="s:int" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="IsApplicationRunning">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="p_Application" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="IsApplicationRunningResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="IsApplicationRunningResult" type="s:boolean" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetApplicationLoadStatus">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="p_Application" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetApplicationLoadStatusResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetApplicationLoadStatusResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CheckLoggedIn">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CheckLoggedInResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="CheckLoggedInResult" type="s:boolean" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="UserLoggedIn">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Application" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="UserLoggedInResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="UserLoggedInResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetInternalID">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_IDType" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetInternalIDResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="GetInternalIDResult" type="s:long" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetTitle">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_IDType" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetTitleResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetTitleResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetInternalCatID">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_CatIDType" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetInternalCatIDResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="GetInternalCatIDResult" type="s:long" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetKeyID">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Keyword" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetKeyIDResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="GetKeyIDResult" type="s:long" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetCategory">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtCategoryID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetCategoryResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetCategoryResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetRoleID">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Role" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetRoleIDResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="GetRoleIDResult" type="s:long" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetServiceID">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceName" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetServiceIDResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="GetServiceIDResult" type="s:long" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Refresh_EventsAndSchedules">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Application" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Refresh_EventsAndSchedulesResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Refresh_EventsAndSchedulesResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Refresh_IDS">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Application" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_MoogaIDType" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Refresh_IDSResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Refresh_IDSResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Refresh_Services">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Application" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Refresh_ServicesResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Refresh_ServicesResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Refresh_DBFilters">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Application" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Refresh_DBFiltersResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Refresh_DBFiltersResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Trans_NotifyTransUsingList">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_UDFName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ArgumentList" type="tns:ArrayOfString" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="ArrayOfString">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="unbounded" name="string" nillable="true" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="Trans_NotifyTransUsingListResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Trans_NotifyTransUsingListResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Trans_NotifyTrans">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_UDFName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Arguments" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Trans_NotifyTransResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Trans_NotifyTransResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Notify_NotifyDataUsingList">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_UDFName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ArgumentList" type="tns:ArrayOfString" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Notify_NotifyDataUsingListResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Notify_NotifyDataUsingListResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Notify_NotifyData">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_UDFName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Arguments" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Notify_NotifyDataResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Notify_NotifyDataResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Generic_GetQueryResult">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemOrPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceNameNCode" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="p_IsConvertToExternalID" type="s:boolean" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Generic_GetQueryResultResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Generic_GetQueryResultResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetRelatedItemsToItem">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetRelatedItemsToItemResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetRelatedItemsToItemResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetConsumersForItem">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetConsumersForItemResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_GetConsumersForItemResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetConsumersMatchingWithRule">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_BusinessRule" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetConsumersMatchingWithRuleResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_GetConsumersMatchingWithRuleResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GenerateConsumerProfile">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GenerateConsumerProfileResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_GenerateConsumerProfileResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetTopRatedItems">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetTopRatedItemsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetTopRatedItemsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetMostAccessedItems">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetMostAccessedItemsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetMostAccessedItemsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetMostSearchedItems">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetMostSearchedItemsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetMostSearchedItemsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetMostSearchedText">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetMostSearchedTextResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetMostSearchedTextResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetPopularItems">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetPopularItemsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetPopularItemsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetPopularKeywords">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetPopularKeywordsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetPopularKeywordsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetPopularBrands">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetPopularBrandsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetPopularBrandsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetPopularCategories">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetPopularCategoriesResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetPopularCategoriesResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Generic_GetPendingPopular">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="1" maxOccurs="1" name="p_PartyID" type="s:long" />
            <s:element minOccurs="1" maxOccurs="1" name="p_ItemCategoryID" type="s:int" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Generic_GetPendingPopularResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Generic_GetPendingPopularResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetRecommendedItemsToItem">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetRecommendedItemsToItemResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetRecommendedItemsToItemResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_RunGenericCFInBatch">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_DBCriteria" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_RunGenericCFInBatchResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_RunGenericCFInBatchResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_RunGenericLFInBatch">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_DBCriteria" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_RunGenericLFInBatchResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_RunGenericLFInBatchResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Category_RunGenericCFInBatch">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_DBCriteria" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Category_RunGenericCFInBatchResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Category_RunGenericCFInBatchResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Category_RunGenericCFInBatchForCategory">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_DBCriteria" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Category_RunGenericCFInBatchForCategoryResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Category_RunGenericCFInBatchForCategoryResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_RunGenericCFInBatchForKeywords">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_DBCriteria" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_RunGenericCFInBatchForKeywordsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_RunGenericCFInBatchForKeywordsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetRecommendedKeywordsToKeyword">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Keyword" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetRecommendedKeywordsToKeywordResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetRecommendedKeywordsToKeywordResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetRelatedKeywordsToKeyword">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Keyword" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Item_GetRelatedKeywordsToKeywordResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Item_GetRelatedKeywordsToKeywordResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetMostRecentItems">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetMostRecentItemsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_GetMostRecentItemsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetPersonalisedItemsToConsumer">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_TimeSlotID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetPersonalisedItemsToConsumerResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_GetPersonalisedItemsToConsumerResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetTopPersonalisedItemsToConsumer">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetTopPersonalisedItemsToConsumerResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_GetTopPersonalisedItemsToConsumerResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GeneratePendingPersonalisedItemsToConsumer">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="p_ServerID" type="s:int" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GeneratePendingPersonalisedItemsToConsumerResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_GeneratePendingPersonalisedItemsToConsumerResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetPreferredtemsToConsumer">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetPreferredtemsToConsumerResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_GetPreferredtemsToConsumerResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_SetPartyStatus">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_TempExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_PartyName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Type" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Status" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_SetPartyStatusResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_SetPartyStatusResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_SetRegisteredPartyStatus">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_TempExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_SetRegisteredPartyStatusResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_SetRegisteredPartyStatusResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Category_GetAllCategoriesInText">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="1" maxOccurs="1" name="p_IsPrefixWithHID" type="s:boolean" />
            <s:element minOccurs="0" maxOccurs="1" name="p_CategorySeparator" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Category_GetAllCategoriesInTextResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Category_GetAllCategoriesInTextResult" type="tns:ArrayOfString" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Reload_Mooga">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Reload_MoogaResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Reload_MoogaResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CategoryRule_GetRule">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_FeatureName" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CategoryRule_GetRuleResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CategoryRule_GetRuleResult" type="tns:ArrayOfFloat" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="ArrayOfFloat">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="unbounded" name="float" type="s:float" />
        </s:sequence>
      </s:complexType>
      <s:element name="Category_RefreshFromDB">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Category_RefreshFromDBResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Category_RefreshFromDBResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CategoryRule_RefreshFromDB">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CategoryRule_RefreshFromDBResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CategoryRule_RefreshFromDBResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Generic_GetPopular">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceNameNCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Generic_GetPopularResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Generic_GetPopularResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Generic_GenericPush">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceNameNCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_MoogaBasicServiceType" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Generic_GenericPushResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Generic_GenericPushResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ExecuteService">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtCategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtLocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtTimeID" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="p_ServiceID" type="s:short" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ExecuteServiceResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="ExecuteServiceResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ExecuteServiceWithInternalIDs">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_CategoryID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_LocationID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_TimeID" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="p_ServiceID" type="s:short" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ExecuteServiceWithInternalIDsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="ExecuteServiceWithInternalIDsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ReplaceIDsWithExtIDs">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_StrWithInternalIDs" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_MoogaIDType" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_MoogaCatIDType" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ValueSeparator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_PercSeparator" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ReplaceIDsWithExtIDsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="ReplaceIDsWithExtIDsResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ExecuteWebServiceUsingList">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_WebServiceName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_Parameters" type="tns:ArrayOfAnyType" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="ArrayOfAnyType">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="unbounded" name="anyType" nillable="true" />
        </s:sequence>
      </s:complexType>
      <s:element name="ExecuteWebServiceUsingListResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="ExecuteWebServiceUsingListResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ExecuteAllPendingServiceRequest">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ExecuteAllPendingServiceRequestResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="ExecuteAllPendingServiceRequestResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetPartyItemRelevence">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="1" maxOccurs="1" name="p_Key" type="s:int" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtPartyID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtItemID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ExtCatID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="p_ServiceCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="Party_GetPartyItemRelevenceResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="Party_GetPartyItemRelevenceResult" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="string" nillable="true" type="s:string" />
      <s:element name="int" type="s:int" />
      <s:element name="boolean" type="s:boolean" />
      <s:element name="long" type="s:long" />
      <s:element name="ArrayOfString" nillable="true" type="tns:ArrayOfString" />
      <s:element name="ArrayOfFloat" nillable="true" type="tns:ArrayOfFloat" />
    </s:schema>
    <s:schema targetNamespace="http://ikensolution.com/webservices/AbstractTypes">
      <s:import namespace="http://schemas.xmlsoap.org/soap/encoding/" />
      <s:complexType name="StringArray">
        <s:complexContent mixed="false">
          <s:restriction base="soapenc:Array">
            <s:sequence>
              <s:element minOccurs="0" maxOccurs="unbounded" name="String" type="s:string" />
            </s:sequence>
          </s:restriction>
        </s:complexContent>
      </s:complexType>
    </s:schema>
  </wsdl:types>
  <wsdl:message name="LoginSoapIn">
    <wsdl:part name="parameters" element="tns:Login" />
  </wsdl:message>
  <wsdl:message name="LoginSoapOut">
    <wsdl:part name="parameters" element="tns:LoginResponse" />
  </wsdl:message>
  <wsdl:message name="DirectLoginSoapIn">
    <wsdl:part name="parameters" element="tns:DirectLogin" />
  </wsdl:message>
  <wsdl:message name="DirectLoginSoapOut">
    <wsdl:part name="parameters" element="tns:DirectLoginResponse" />
  </wsdl:message>
  <wsdl:message name="LogoutSoapIn">
    <wsdl:part name="parameters" element="tns:Logout" />
  </wsdl:message>
  <wsdl:message name="LogoutSoapOut">
    <wsdl:part name="parameters" element="tns:LogoutResponse" />
  </wsdl:message>
  <wsdl:message name="UsersLoggedInSoapIn">
    <wsdl:part name="parameters" element="tns:UsersLoggedIn" />
  </wsdl:message>
  <wsdl:message name="UsersLoggedInSoapOut">
    <wsdl:part name="parameters" element="tns:UsersLoggedInResponse" />
  </wsdl:message>
  <wsdl:message name="IsApplicationRunningSoapIn">
    <wsdl:part name="parameters" element="tns:IsApplicationRunning" />
  </wsdl:message>
  <wsdl:message name="IsApplicationRunningSoapOut">
    <wsdl:part name="parameters" element="tns:IsApplicationRunningResponse" />
  </wsdl:message>
  <wsdl:message name="GetApplicationLoadStatusSoapIn">
    <wsdl:part name="parameters" element="tns:GetApplicationLoadStatus" />
  </wsdl:message>
  <wsdl:message name="GetApplicationLoadStatusSoapOut">
    <wsdl:part name="parameters" element="tns:GetApplicationLoadStatusResponse" />
  </wsdl:message>
  <wsdl:message name="CheckLoggedInSoapIn">
    <wsdl:part name="parameters" element="tns:CheckLoggedIn" />
  </wsdl:message>
  <wsdl:message name="CheckLoggedInSoapOut">
    <wsdl:part name="parameters" element="tns:CheckLoggedInResponse" />
  </wsdl:message>
  <wsdl:message name="UserLoggedInSoapIn">
    <wsdl:part name="parameters" element="tns:UserLoggedIn" />
  </wsdl:message>
  <wsdl:message name="UserLoggedInSoapOut">
    <wsdl:part name="parameters" element="tns:UserLoggedInResponse" />
  </wsdl:message>
  <wsdl:message name="GetInternalIDSoapIn">
    <wsdl:part name="parameters" element="tns:GetInternalID" />
  </wsdl:message>
  <wsdl:message name="GetInternalIDSoapOut">
    <wsdl:part name="parameters" element="tns:GetInternalIDResponse" />
  </wsdl:message>
  <wsdl:message name="GetTitleSoapIn">
    <wsdl:part name="parameters" element="tns:GetTitle" />
  </wsdl:message>
  <wsdl:message name="GetTitleSoapOut">
    <wsdl:part name="parameters" element="tns:GetTitleResponse" />
  </wsdl:message>
  <wsdl:message name="GetInternalCatIDSoapIn">
    <wsdl:part name="parameters" element="tns:GetInternalCatID" />
  </wsdl:message>
  <wsdl:message name="GetInternalCatIDSoapOut">
    <wsdl:part name="parameters" element="tns:GetInternalCatIDResponse" />
  </wsdl:message>
  <wsdl:message name="GetKeyIDSoapIn">
    <wsdl:part name="parameters" element="tns:GetKeyID" />
  </wsdl:message>
  <wsdl:message name="GetKeyIDSoapOut">
    <wsdl:part name="parameters" element="tns:GetKeyIDResponse" />
  </wsdl:message>
  <wsdl:message name="GetCategorySoapIn">
    <wsdl:part name="parameters" element="tns:GetCategory" />
  </wsdl:message>
  <wsdl:message name="GetCategorySoapOut">
    <wsdl:part name="parameters" element="tns:GetCategoryResponse" />
  </wsdl:message>
  <wsdl:message name="GetRoleIDSoapIn">
    <wsdl:part name="parameters" element="tns:GetRoleID" />
  </wsdl:message>
  <wsdl:message name="GetRoleIDSoapOut">
    <wsdl:part name="parameters" element="tns:GetRoleIDResponse" />
  </wsdl:message>
  <wsdl:message name="GetServiceIDSoapIn">
    <wsdl:part name="parameters" element="tns:GetServiceID" />
  </wsdl:message>
  <wsdl:message name="GetServiceIDSoapOut">
    <wsdl:part name="parameters" element="tns:GetServiceIDResponse" />
  </wsdl:message>
  <wsdl:message name="Refresh_EventsAndSchedulesSoapIn">
    <wsdl:part name="parameters" element="tns:Refresh_EventsAndSchedules" />
  </wsdl:message>
  <wsdl:message name="Refresh_EventsAndSchedulesSoapOut">
    <wsdl:part name="parameters" element="tns:Refresh_EventsAndSchedulesResponse" />
  </wsdl:message>
  <wsdl:message name="Refresh_IDSSoapIn">
    <wsdl:part name="parameters" element="tns:Refresh_IDS" />
  </wsdl:message>
  <wsdl:message name="Refresh_IDSSoapOut">
    <wsdl:part name="parameters" element="tns:Refresh_IDSResponse" />
  </wsdl:message>
  <wsdl:message name="Refresh_ServicesSoapIn">
    <wsdl:part name="parameters" element="tns:Refresh_Services" />
  </wsdl:message>
  <wsdl:message name="Refresh_ServicesSoapOut">
    <wsdl:part name="parameters" element="tns:Refresh_ServicesResponse" />
  </wsdl:message>
  <wsdl:message name="Refresh_DBFiltersSoapIn">
    <wsdl:part name="parameters" element="tns:Refresh_DBFilters" />
  </wsdl:message>
  <wsdl:message name="Refresh_DBFiltersSoapOut">
    <wsdl:part name="parameters" element="tns:Refresh_DBFiltersResponse" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransUsingListSoapIn">
    <wsdl:part name="parameters" element="tns:Trans_NotifyTransUsingList" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransUsingListSoapOut">
    <wsdl:part name="parameters" element="tns:Trans_NotifyTransUsingListResponse" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransSoapIn">
    <wsdl:part name="parameters" element="tns:Trans_NotifyTrans" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransSoapOut">
    <wsdl:part name="parameters" element="tns:Trans_NotifyTransResponse" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataUsingListSoapIn">
    <wsdl:part name="parameters" element="tns:Notify_NotifyDataUsingList" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataUsingListSoapOut">
    <wsdl:part name="parameters" element="tns:Notify_NotifyDataUsingListResponse" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataSoapIn">
    <wsdl:part name="parameters" element="tns:Notify_NotifyData" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataSoapOut">
    <wsdl:part name="parameters" element="tns:Notify_NotifyDataResponse" />
  </wsdl:message>
  <wsdl:message name="Generic_GetQueryResultSoapIn">
    <wsdl:part name="parameters" element="tns:Generic_GetQueryResult" />
  </wsdl:message>
  <wsdl:message name="Generic_GetQueryResultSoapOut">
    <wsdl:part name="parameters" element="tns:Generic_GetQueryResultResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedItemsToItemSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetRelatedItemsToItem" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedItemsToItemSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetRelatedItemsToItemResponse" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersForItemSoapIn">
    <wsdl:part name="parameters" element="tns:Party_GetConsumersForItem" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersForItemSoapOut">
    <wsdl:part name="parameters" element="tns:Party_GetConsumersForItemResponse" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersMatchingWithRuleSoapIn">
    <wsdl:part name="parameters" element="tns:Party_GetConsumersMatchingWithRule" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersMatchingWithRuleSoapOut">
    <wsdl:part name="parameters" element="tns:Party_GetConsumersMatchingWithRuleResponse" />
  </wsdl:message>
  <wsdl:message name="Party_GenerateConsumerProfileSoapIn">
    <wsdl:part name="parameters" element="tns:Party_GenerateConsumerProfile" />
  </wsdl:message>
  <wsdl:message name="Party_GenerateConsumerProfileSoapOut">
    <wsdl:part name="parameters" element="tns:Party_GenerateConsumerProfileResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetTopRatedItemsSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetTopRatedItems" />
  </wsdl:message>
  <wsdl:message name="Item_GetTopRatedItemsSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetTopRatedItemsResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostAccessedItemsSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetMostAccessedItems" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostAccessedItemsSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetMostAccessedItemsResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedItemsSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetMostSearchedItems" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedItemsSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetMostSearchedItemsResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedTextSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetMostSearchedText" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedTextSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetMostSearchedTextResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularItemsSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetPopularItems" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularItemsSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetPopularItemsResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularKeywordsSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetPopularKeywords" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularKeywordsSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetPopularKeywordsResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularBrandsSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetPopularBrands" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularBrandsSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetPopularBrandsResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularCategoriesSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetPopularCategories" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularCategoriesSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetPopularCategoriesResponse" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPendingPopularSoapIn">
    <wsdl:part name="parameters" element="tns:Generic_GetPendingPopular" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPendingPopularSoapOut">
    <wsdl:part name="parameters" element="tns:Generic_GetPendingPopularResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedItemsToItemSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetRecommendedItemsToItem" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedItemsToItemSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetRecommendedItemsToItemResponse" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchSoapIn">
    <wsdl:part name="parameters" element="tns:Item_RunGenericCFInBatch" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchSoapOut">
    <wsdl:part name="parameters" element="tns:Item_RunGenericCFInBatchResponse" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericLFInBatchSoapIn">
    <wsdl:part name="parameters" element="tns:Item_RunGenericLFInBatch" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericLFInBatchSoapOut">
    <wsdl:part name="parameters" element="tns:Item_RunGenericLFInBatchResponse" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchSoapIn">
    <wsdl:part name="parameters" element="tns:Category_RunGenericCFInBatch" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchSoapOut">
    <wsdl:part name="parameters" element="tns:Category_RunGenericCFInBatchResponse" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchForCategorySoapIn">
    <wsdl:part name="parameters" element="tns:Category_RunGenericCFInBatchForCategory" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchForCategorySoapOut">
    <wsdl:part name="parameters" element="tns:Category_RunGenericCFInBatchForCategoryResponse" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchForKeywordsSoapIn">
    <wsdl:part name="parameters" element="tns:Item_RunGenericCFInBatchForKeywords" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchForKeywordsSoapOut">
    <wsdl:part name="parameters" element="tns:Item_RunGenericCFInBatchForKeywordsResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedKeywordsToKeywordSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetRecommendedKeywordsToKeyword" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedKeywordsToKeywordSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetRecommendedKeywordsToKeywordResponse" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedKeywordsToKeywordSoapIn">
    <wsdl:part name="parameters" element="tns:Item_GetRelatedKeywordsToKeyword" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedKeywordsToKeywordSoapOut">
    <wsdl:part name="parameters" element="tns:Item_GetRelatedKeywordsToKeywordResponse" />
  </wsdl:message>
  <wsdl:message name="Party_GetMostRecentItemsSoapIn">
    <wsdl:part name="parameters" element="tns:Party_GetMostRecentItems" />
  </wsdl:message>
  <wsdl:message name="Party_GetMostRecentItemsSoapOut">
    <wsdl:part name="parameters" element="tns:Party_GetMostRecentItemsResponse" />
  </wsdl:message>
  <wsdl:message name="Party_GetPersonalisedItemsToConsumerSoapIn">
    <wsdl:part name="parameters" element="tns:Party_GetPersonalisedItemsToConsumer" />
  </wsdl:message>
  <wsdl:message name="Party_GetPersonalisedItemsToConsumerSoapOut">
    <wsdl:part name="parameters" element="tns:Party_GetPersonalisedItemsToConsumerResponse" />
  </wsdl:message>
  <wsdl:message name="Party_GetTopPersonalisedItemsToConsumerSoapIn">
    <wsdl:part name="parameters" element="tns:Party_GetTopPersonalisedItemsToConsumer" />
  </wsdl:message>
  <wsdl:message name="Party_GetTopPersonalisedItemsToConsumerSoapOut">
    <wsdl:part name="parameters" element="tns:Party_GetTopPersonalisedItemsToConsumerResponse" />
  </wsdl:message>
  <wsdl:message name="Party_GeneratePendingPersonalisedItemsToConsumerSoapIn">
    <wsdl:part name="parameters" element="tns:Party_GeneratePendingPersonalisedItemsToConsumer" />
  </wsdl:message>
  <wsdl:message name="Party_GeneratePendingPersonalisedItemsToConsumerSoapOut">
    <wsdl:part name="parameters" element="tns:Party_GeneratePendingPersonalisedItemsToConsumerResponse" />
  </wsdl:message>
  <wsdl:message name="Party_GetPreferredtemsToConsumerSoapIn">
    <wsdl:part name="parameters" element="tns:Party_GetPreferredtemsToConsumer" />
  </wsdl:message>
  <wsdl:message name="Party_GetPreferredtemsToConsumerSoapOut">
    <wsdl:part name="parameters" element="tns:Party_GetPreferredtemsToConsumerResponse" />
  </wsdl:message>
  <wsdl:message name="Party_SetPartyStatusSoapIn">
    <wsdl:part name="parameters" element="tns:Party_SetPartyStatus" />
  </wsdl:message>
  <wsdl:message name="Party_SetPartyStatusSoapOut">
    <wsdl:part name="parameters" element="tns:Party_SetPartyStatusResponse" />
  </wsdl:message>
  <wsdl:message name="Party_SetRegisteredPartyStatusSoapIn">
    <wsdl:part name="parameters" element="tns:Party_SetRegisteredPartyStatus" />
  </wsdl:message>
  <wsdl:message name="Party_SetRegisteredPartyStatusSoapOut">
    <wsdl:part name="parameters" element="tns:Party_SetRegisteredPartyStatusResponse" />
  </wsdl:message>
  <wsdl:message name="Category_GetAllCategoriesInTextSoapIn">
    <wsdl:part name="parameters" element="tns:Category_GetAllCategoriesInText" />
  </wsdl:message>
  <wsdl:message name="Category_GetAllCategoriesInTextSoapOut">
    <wsdl:part name="parameters" element="tns:Category_GetAllCategoriesInTextResponse" />
  </wsdl:message>
  <wsdl:message name="Reload_MoogaSoapIn">
    <wsdl:part name="parameters" element="tns:Reload_Mooga" />
  </wsdl:message>
  <wsdl:message name="Reload_MoogaSoapOut">
    <wsdl:part name="parameters" element="tns:Reload_MoogaResponse" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_GetRuleSoapIn">
    <wsdl:part name="parameters" element="tns:CategoryRule_GetRule" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_GetRuleSoapOut">
    <wsdl:part name="parameters" element="tns:CategoryRule_GetRuleResponse" />
  </wsdl:message>
  <wsdl:message name="Category_RefreshFromDBSoapIn">
    <wsdl:part name="parameters" element="tns:Category_RefreshFromDB" />
  </wsdl:message>
  <wsdl:message name="Category_RefreshFromDBSoapOut">
    <wsdl:part name="parameters" element="tns:Category_RefreshFromDBResponse" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_RefreshFromDBSoapIn">
    <wsdl:part name="parameters" element="tns:CategoryRule_RefreshFromDB" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_RefreshFromDBSoapOut">
    <wsdl:part name="parameters" element="tns:CategoryRule_RefreshFromDBResponse" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPopularSoapIn">
    <wsdl:part name="parameters" element="tns:Generic_GetPopular" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPopularSoapOut">
    <wsdl:part name="parameters" element="tns:Generic_GetPopularResponse" />
  </wsdl:message>
  <wsdl:message name="Generic_GenericPushSoapIn">
    <wsdl:part name="parameters" element="tns:Generic_GenericPush" />
  </wsdl:message>
  <wsdl:message name="Generic_GenericPushSoapOut">
    <wsdl:part name="parameters" element="tns:Generic_GenericPushResponse" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceSoapIn">
    <wsdl:part name="parameters" element="tns:ExecuteService" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceSoapOut">
    <wsdl:part name="parameters" element="tns:ExecuteServiceResponse" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceWithInternalIDsSoapIn">
    <wsdl:part name="parameters" element="tns:ExecuteServiceWithInternalIDs" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceWithInternalIDsSoapOut">
    <wsdl:part name="parameters" element="tns:ExecuteServiceWithInternalIDsResponse" />
  </wsdl:message>
  <wsdl:message name="ReplaceIDsWithExtIDsSoapIn">
    <wsdl:part name="parameters" element="tns:ReplaceIDsWithExtIDs" />
  </wsdl:message>
  <wsdl:message name="ReplaceIDsWithExtIDsSoapOut">
    <wsdl:part name="parameters" element="tns:ReplaceIDsWithExtIDsResponse" />
  </wsdl:message>
  <wsdl:message name="ExecuteWebServiceUsingListSoapIn">
    <wsdl:part name="parameters" element="tns:ExecuteWebServiceUsingList" />
  </wsdl:message>
  <wsdl:message name="ExecuteWebServiceUsingListSoapOut">
    <wsdl:part name="parameters" element="tns:ExecuteWebServiceUsingListResponse" />
  </wsdl:message>
  <wsdl:message name="ExecuteAllPendingServiceRequestSoapIn">
    <wsdl:part name="parameters" element="tns:ExecuteAllPendingServiceRequest" />
  </wsdl:message>
  <wsdl:message name="ExecuteAllPendingServiceRequestSoapOut">
    <wsdl:part name="parameters" element="tns:ExecuteAllPendingServiceRequestResponse" />
  </wsdl:message>
  <wsdl:message name="Party_GetPartyItemRelevenceSoapIn">
    <wsdl:part name="parameters" element="tns:Party_GetPartyItemRelevence" />
  </wsdl:message>
  <wsdl:message name="Party_GetPartyItemRelevenceSoapOut">
    <wsdl:part name="parameters" element="tns:Party_GetPartyItemRelevenceResponse" />
  </wsdl:message>
  <wsdl:message name="LoginHttpGetIn">
    <wsdl:part name="p_User" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="LoginHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="DirectLoginHttpGetIn">
    <wsdl:part name="p_User" type="s:string" />
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="DirectLoginHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="LogoutHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="LogoutHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="UsersLoggedInHttpGetIn">
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="UsersLoggedInHttpGetOut">
    <wsdl:part name="Body" element="tns:int" />
  </wsdl:message>
  <wsdl:message name="IsApplicationRunningHttpGetIn">
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="IsApplicationRunningHttpGetOut">
    <wsdl:part name="Body" element="tns:boolean" />
  </wsdl:message>
  <wsdl:message name="GetApplicationLoadStatusHttpGetIn">
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetApplicationLoadStatusHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="CheckLoggedInHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CheckLoggedInHttpGetOut">
    <wsdl:part name="Body" element="tns:boolean" />
  </wsdl:message>
  <wsdl:message name="UserLoggedInHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="UserLoggedInHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="GetInternalIDHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtID" type="s:string" />
    <wsdl:part name="p_IDType" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetInternalIDHttpGetOut">
    <wsdl:part name="Body" element="tns:long" />
  </wsdl:message>
  <wsdl:message name="GetTitleHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtID" type="s:string" />
    <wsdl:part name="p_IDType" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetTitleHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="GetInternalCatIDHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtCategoryID" type="s:string" />
    <wsdl:part name="p_CatIDType" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetInternalCatIDHttpGetOut">
    <wsdl:part name="Body" element="tns:long" />
  </wsdl:message>
  <wsdl:message name="GetKeyIDHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Keyword" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetKeyIDHttpGetOut">
    <wsdl:part name="Body" element="tns:long" />
  </wsdl:message>
  <wsdl:message name="GetCategoryHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtCategoryID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetCategoryHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="GetRoleIDHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Role" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetRoleIDHttpGetOut">
    <wsdl:part name="Body" element="tns:long" />
  </wsdl:message>
  <wsdl:message name="GetServiceIDHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceName" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetServiceIDHttpGetOut">
    <wsdl:part name="Body" element="tns:long" />
  </wsdl:message>
  <wsdl:message name="Refresh_EventsAndSchedulesHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_EventsAndSchedulesHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_IDSHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
    <wsdl:part name="p_MoogaIDType" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_IDSHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_ServicesHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_ServicesHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_DBFiltersHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_DBFiltersHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransUsingListHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_UDFName" type="s:string" />
    <wsdl:part name="p_ArgumentList" type="s1:StringArray" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransUsingListHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_UDFName" type="s:string" />
    <wsdl:part name="p_Arguments" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataUsingListHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_UDFName" type="s:string" />
    <wsdl:part name="p_ArgumentList" type="s1:StringArray" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataUsingListHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_UDFName" type="s:string" />
    <wsdl:part name="p_Arguments" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetQueryResultHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemOrPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceNameNCode" type="s:string" />
    <wsdl:part name="p_IsConvertToExternalID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetQueryResultHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedItemsToItemHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedItemsToItemHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersForItemHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersForItemHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersMatchingWithRuleHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_BusinessRule" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersMatchingWithRuleHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GenerateConsumerProfileHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GenerateConsumerProfileHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetTopRatedItemsHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetTopRatedItemsHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostAccessedItemsHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostAccessedItemsHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedItemsHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedItemsHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedTextHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedTextHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularItemsHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularItemsHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularKeywordsHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularKeywordsHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularBrandsHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularBrandsHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularCategoriesHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularCategoriesHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPendingPopularHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_PartyID" type="s:string" />
    <wsdl:part name="p_ItemCategoryID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPendingPopularHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedItemsToItemHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedItemsToItemHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_DBCriteria" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericLFInBatchHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_DBCriteria" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericLFInBatchHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_ServiceName" type="s:string" />
    <wsdl:part name="p_DBCriteria" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchForCategoryHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_DBCriteria" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchForCategoryHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchForKeywordsHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_DBCriteria" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchForKeywordsHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedKeywordsToKeywordHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Keyword" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedKeywordsToKeywordHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedKeywordsToKeywordHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Keyword" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedKeywordsToKeywordHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetMostRecentItemsHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetMostRecentItemsHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPersonalisedItemsToConsumerHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtPartyLocationID" type="s:string" />
    <wsdl:part name="p_TimeSlotID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPersonalisedItemsToConsumerHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetTopPersonalisedItemsToConsumerHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtPartyLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetTopPersonalisedItemsToConsumerHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GeneratePendingPersonalisedItemsToConsumerHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtPartyLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_ServerID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GeneratePendingPersonalisedItemsToConsumerHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPreferredtemsToConsumerHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtPartyLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPreferredtemsToConsumerHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_SetPartyStatusHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_TempExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_PartyName" type="s:string" />
    <wsdl:part name="p_Type" type="s:string" />
    <wsdl:part name="p_Status" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_SetPartyStatusHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_SetRegisteredPartyStatusHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_TempExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_SetRegisteredPartyStatusHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Category_GetAllCategoriesInTextHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_IsPrefixWithHID" type="s:string" />
    <wsdl:part name="p_CategorySeparator" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Category_GetAllCategoriesInTextHttpGetOut">
    <wsdl:part name="Body" element="tns:ArrayOfString" />
  </wsdl:message>
  <wsdl:message name="Reload_MoogaHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Reload_MoogaHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_GetRuleHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ServiceName" type="s:string" />
    <wsdl:part name="p_FeatureName" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_GetRuleHttpGetOut">
    <wsdl:part name="Body" element="tns:ArrayOfFloat" />
  </wsdl:message>
  <wsdl:message name="Category_RefreshFromDBHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Category_RefreshFromDBHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_RefreshFromDBHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_RefreshFromDBHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPopularHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ServiceNameNCode" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPopularHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GenericPushHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtID" type="s:string" />
    <wsdl:part name="p_ExtCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceNameNCode" type="s:string" />
    <wsdl:part name="p_MoogaBasicServiceType" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GenericPushHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtID" type="s:string" />
    <wsdl:part name="p_ExtCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ExtTimeID" type="s:string" />
    <wsdl:part name="p_ServiceID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceWithInternalIDsHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ID" type="s:string" />
    <wsdl:part name="p_CategoryID" type="s:string" />
    <wsdl:part name="p_LocationID" type="s:string" />
    <wsdl:part name="p_TimeID" type="s:string" />
    <wsdl:part name="p_ServiceID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceWithInternalIDsHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="ReplaceIDsWithExtIDsHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_StrWithInternalIDs" type="s:string" />
    <wsdl:part name="p_MoogaIDType" type="s:string" />
    <wsdl:part name="p_MoogaCatIDType" type="s:string" />
    <wsdl:part name="p_ValueSeparator" type="s:string" />
    <wsdl:part name="p_PercSeparator" type="s:string" />
  </wsdl:message>
  <wsdl:message name="ReplaceIDsWithExtIDsHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteAllPendingServiceRequestHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteAllPendingServiceRequestHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPartyItemRelevenceHttpGetIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemID" type="s:string" />
    <wsdl:part name="p_ExtCatID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPartyItemRelevenceHttpGetOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="LoginHttpPostIn">
    <wsdl:part name="p_User" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="LoginHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="DirectLoginHttpPostIn">
    <wsdl:part name="p_User" type="s:string" />
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="DirectLoginHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="LogoutHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="LogoutHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="UsersLoggedInHttpPostIn">
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="UsersLoggedInHttpPostOut">
    <wsdl:part name="Body" element="tns:int" />
  </wsdl:message>
  <wsdl:message name="IsApplicationRunningHttpPostIn">
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="IsApplicationRunningHttpPostOut">
    <wsdl:part name="Body" element="tns:boolean" />
  </wsdl:message>
  <wsdl:message name="GetApplicationLoadStatusHttpPostIn">
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetApplicationLoadStatusHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="CheckLoggedInHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CheckLoggedInHttpPostOut">
    <wsdl:part name="Body" element="tns:boolean" />
  </wsdl:message>
  <wsdl:message name="UserLoggedInHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="UserLoggedInHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="GetInternalIDHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtID" type="s:string" />
    <wsdl:part name="p_IDType" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetInternalIDHttpPostOut">
    <wsdl:part name="Body" element="tns:long" />
  </wsdl:message>
  <wsdl:message name="GetTitleHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtID" type="s:string" />
    <wsdl:part name="p_IDType" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetTitleHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="GetInternalCatIDHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtCategoryID" type="s:string" />
    <wsdl:part name="p_CatIDType" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetInternalCatIDHttpPostOut">
    <wsdl:part name="Body" element="tns:long" />
  </wsdl:message>
  <wsdl:message name="GetKeyIDHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Keyword" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetKeyIDHttpPostOut">
    <wsdl:part name="Body" element="tns:long" />
  </wsdl:message>
  <wsdl:message name="GetCategoryHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtCategoryID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetCategoryHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="GetRoleIDHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Role" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetRoleIDHttpPostOut">
    <wsdl:part name="Body" element="tns:long" />
  </wsdl:message>
  <wsdl:message name="GetServiceIDHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceName" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetServiceIDHttpPostOut">
    <wsdl:part name="Body" element="tns:long" />
  </wsdl:message>
  <wsdl:message name="Refresh_EventsAndSchedulesHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_EventsAndSchedulesHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_IDSHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
    <wsdl:part name="p_MoogaIDType" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_IDSHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_ServicesHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_ServicesHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_DBFiltersHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Application" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Refresh_DBFiltersHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransUsingListHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_UDFName" type="s:string" />
    <wsdl:part name="p_ArgumentList" type="s1:StringArray" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransUsingListHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_UDFName" type="s:string" />
    <wsdl:part name="p_Arguments" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Trans_NotifyTransHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataUsingListHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_UDFName" type="s:string" />
    <wsdl:part name="p_ArgumentList" type="s1:StringArray" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataUsingListHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_UDFName" type="s:string" />
    <wsdl:part name="p_Arguments" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Notify_NotifyDataHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetQueryResultHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemOrPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceNameNCode" type="s:string" />
    <wsdl:part name="p_IsConvertToExternalID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetQueryResultHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedItemsToItemHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedItemsToItemHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersForItemHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersForItemHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersMatchingWithRuleHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_BusinessRule" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetConsumersMatchingWithRuleHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GenerateConsumerProfileHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GenerateConsumerProfileHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetTopRatedItemsHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetTopRatedItemsHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostAccessedItemsHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostAccessedItemsHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedItemsHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedItemsHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedTextHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetMostSearchedTextHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularItemsHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularItemsHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularKeywordsHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularKeywordsHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularBrandsHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularBrandsHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularCategoriesHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetPopularCategoriesHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPendingPopularHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_PartyID" type="s:string" />
    <wsdl:part name="p_ItemCategoryID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPendingPopularHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedItemsToItemHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedItemsToItemHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_DBCriteria" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericLFInBatchHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_DBCriteria" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericLFInBatchHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_ServiceName" type="s:string" />
    <wsdl:part name="p_DBCriteria" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchForCategoryHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_DBCriteria" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Category_RunGenericCFInBatchForCategoryHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchForKeywordsHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_DBCriteria" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_RunGenericCFInBatchForKeywordsHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedKeywordsToKeywordHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Keyword" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRecommendedKeywordsToKeywordHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedKeywordsToKeywordHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_Keyword" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Item_GetRelatedKeywordsToKeywordHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetMostRecentItemsHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetMostRecentItemsHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPersonalisedItemsToConsumerHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtPartyLocationID" type="s:string" />
    <wsdl:part name="p_TimeSlotID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPersonalisedItemsToConsumerHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetTopPersonalisedItemsToConsumerHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtPartyLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetTopPersonalisedItemsToConsumerHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GeneratePendingPersonalisedItemsToConsumerHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtPartyLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
    <wsdl:part name="p_ServerID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GeneratePendingPersonalisedItemsToConsumerHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPreferredtemsToConsumerHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ExtPartyLocationID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPreferredtemsToConsumerHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_SetPartyStatusHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_TempExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_PartyName" type="s:string" />
    <wsdl:part name="p_Type" type="s:string" />
    <wsdl:part name="p_Status" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_SetPartyStatusHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_SetRegisteredPartyStatusHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_TempExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_SetRegisteredPartyStatusHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Category_GetAllCategoriesInTextHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_IsPrefixWithHID" type="s:string" />
    <wsdl:part name="p_CategorySeparator" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Category_GetAllCategoriesInTextHttpPostOut">
    <wsdl:part name="Body" element="tns:ArrayOfString" />
  </wsdl:message>
  <wsdl:message name="Reload_MoogaHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Reload_MoogaHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_GetRuleHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ServiceName" type="s:string" />
    <wsdl:part name="p_FeatureName" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_GetRuleHttpPostOut">
    <wsdl:part name="Body" element="tns:ArrayOfFloat" />
  </wsdl:message>
  <wsdl:message name="Category_RefreshFromDBHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Category_RefreshFromDBHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_RefreshFromDBHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CategoryRule_RefreshFromDBHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPopularHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemCategoryID" type="s:string" />
    <wsdl:part name="p_ServiceNameNCode" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GetPopularHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GenericPushHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtID" type="s:string" />
    <wsdl:part name="p_ExtCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ServiceNameNCode" type="s:string" />
    <wsdl:part name="p_MoogaBasicServiceType" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Generic_GenericPushHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtID" type="s:string" />
    <wsdl:part name="p_ExtCategoryID" type="s:string" />
    <wsdl:part name="p_ExtLocationID" type="s:string" />
    <wsdl:part name="p_ExtTimeID" type="s:string" />
    <wsdl:part name="p_ServiceID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceWithInternalIDsHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ID" type="s:string" />
    <wsdl:part name="p_CategoryID" type="s:string" />
    <wsdl:part name="p_LocationID" type="s:string" />
    <wsdl:part name="p_TimeID" type="s:string" />
    <wsdl:part name="p_ServiceID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteServiceWithInternalIDsHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="ReplaceIDsWithExtIDsHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_StrWithInternalIDs" type="s:string" />
    <wsdl:part name="p_MoogaIDType" type="s:string" />
    <wsdl:part name="p_MoogaCatIDType" type="s:string" />
    <wsdl:part name="p_ValueSeparator" type="s:string" />
    <wsdl:part name="p_PercSeparator" type="s:string" />
  </wsdl:message>
  <wsdl:message name="ReplaceIDsWithExtIDsHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteAllPendingServiceRequestHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
  </wsdl:message>
  <wsdl:message name="ExecuteAllPendingServiceRequestHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPartyItemRelevenceHttpPostIn">
    <wsdl:part name="p_Key" type="s:string" />
    <wsdl:part name="p_ExtPartyID" type="s:string" />
    <wsdl:part name="p_ExtItemID" type="s:string" />
    <wsdl:part name="p_ExtCatID" type="s:string" />
    <wsdl:part name="p_ServiceCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="Party_GetPartyItemRelevenceHttpPostOut">
    <wsdl:part name="Body" element="tns:string" />
  </wsdl:message>
  <wsdl:portType name="MoogaWebServicesSoap">
    <wsdl:operation name="Login">
      <wsdl:input message="tns:LoginSoapIn" />
      <wsdl:output message="tns:LoginSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="DirectLogin">
      <wsdl:input message="tns:DirectLoginSoapIn" />
      <wsdl:output message="tns:DirectLoginSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Logout">
      <wsdl:input message="tns:LogoutSoapIn" />
      <wsdl:output message="tns:LogoutSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="UsersLoggedIn">
      <wsdl:input message="tns:UsersLoggedInSoapIn" />
      <wsdl:output message="tns:UsersLoggedInSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="IsApplicationRunning">
      <wsdl:input message="tns:IsApplicationRunningSoapIn" />
      <wsdl:output message="tns:IsApplicationRunningSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetApplicationLoadStatus">
      <wsdl:input message="tns:GetApplicationLoadStatusSoapIn" />
      <wsdl:output message="tns:GetApplicationLoadStatusSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CheckLoggedIn">
      <wsdl:input message="tns:CheckLoggedInSoapIn" />
      <wsdl:output message="tns:CheckLoggedInSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="UserLoggedIn">
      <wsdl:input message="tns:UserLoggedInSoapIn" />
      <wsdl:output message="tns:UserLoggedInSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetInternalID">
      <wsdl:input message="tns:GetInternalIDSoapIn" />
      <wsdl:output message="tns:GetInternalIDSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetTitle">
      <wsdl:input message="tns:GetTitleSoapIn" />
      <wsdl:output message="tns:GetTitleSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetInternalCatID">
      <wsdl:input message="tns:GetInternalCatIDSoapIn" />
      <wsdl:output message="tns:GetInternalCatIDSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetKeyID">
      <wsdl:input message="tns:GetKeyIDSoapIn" />
      <wsdl:output message="tns:GetKeyIDSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetCategory">
      <wsdl:input message="tns:GetCategorySoapIn" />
      <wsdl:output message="tns:GetCategorySoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetRoleID">
      <wsdl:input message="tns:GetRoleIDSoapIn" />
      <wsdl:output message="tns:GetRoleIDSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetServiceID">
      <wsdl:input message="tns:GetServiceIDSoapIn" />
      <wsdl:output message="tns:GetServiceIDSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_EventsAndSchedules">
      <wsdl:input message="tns:Refresh_EventsAndSchedulesSoapIn" />
      <wsdl:output message="tns:Refresh_EventsAndSchedulesSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_IDS">
      <wsdl:input message="tns:Refresh_IDSSoapIn" />
      <wsdl:output message="tns:Refresh_IDSSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_Services">
      <wsdl:input message="tns:Refresh_ServicesSoapIn" />
      <wsdl:output message="tns:Refresh_ServicesSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_DBFilters">
      <wsdl:input message="tns:Refresh_DBFiltersSoapIn" />
      <wsdl:output message="tns:Refresh_DBFiltersSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTransUsingList">
      <wsdl:input message="tns:Trans_NotifyTransUsingListSoapIn" />
      <wsdl:output message="tns:Trans_NotifyTransUsingListSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTrans">
      <wsdl:input message="tns:Trans_NotifyTransSoapIn" />
      <wsdl:output message="tns:Trans_NotifyTransSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyDataUsingList">
      <wsdl:input message="tns:Notify_NotifyDataUsingListSoapIn" />
      <wsdl:output message="tns:Notify_NotifyDataUsingListSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyData">
      <wsdl:input message="tns:Notify_NotifyDataSoapIn" />
      <wsdl:output message="tns:Notify_NotifyDataSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GetQueryResult">
      <wsdl:input message="tns:Generic_GetQueryResultSoapIn" />
      <wsdl:output message="tns:Generic_GetQueryResultSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedItemsToItem">
      <wsdl:input message="tns:Item_GetRelatedItemsToItemSoapIn" />
      <wsdl:output message="tns:Item_GetRelatedItemsToItemSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersForItem">
      <wsdl:input message="tns:Party_GetConsumersForItemSoapIn" />
      <wsdl:output message="tns:Party_GetConsumersForItemSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersMatchingWithRule">
      <wsdl:input message="tns:Party_GetConsumersMatchingWithRuleSoapIn" />
      <wsdl:output message="tns:Party_GetConsumersMatchingWithRuleSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GenerateConsumerProfile">
      <wsdl:input message="tns:Party_GenerateConsumerProfileSoapIn" />
      <wsdl:output message="tns:Party_GenerateConsumerProfileSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetTopRatedItems">
      <wsdl:input message="tns:Item_GetTopRatedItemsSoapIn" />
      <wsdl:output message="tns:Item_GetTopRatedItemsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostAccessedItems">
      <wsdl:input message="tns:Item_GetMostAccessedItemsSoapIn" />
      <wsdl:output message="tns:Item_GetMostAccessedItemsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedItems">
      <wsdl:input message="tns:Item_GetMostSearchedItemsSoapIn" />
      <wsdl:output message="tns:Item_GetMostSearchedItemsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedText">
      <wsdl:input message="tns:Item_GetMostSearchedTextSoapIn" />
      <wsdl:output message="tns:Item_GetMostSearchedTextSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularItems">
      <wsdl:input message="tns:Item_GetPopularItemsSoapIn" />
      <wsdl:output message="tns:Item_GetPopularItemsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularKeywords">
      <wsdl:input message="tns:Item_GetPopularKeywordsSoapIn" />
      <wsdl:output message="tns:Item_GetPopularKeywordsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularBrands">
      <wsdl:input message="tns:Item_GetPopularBrandsSoapIn" />
      <wsdl:output message="tns:Item_GetPopularBrandsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularCategories">
      <wsdl:input message="tns:Item_GetPopularCategoriesSoapIn" />
      <wsdl:output message="tns:Item_GetPopularCategoriesSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPendingPopular">
      <wsdl:input message="tns:Generic_GetPendingPopularSoapIn" />
      <wsdl:output message="tns:Generic_GetPendingPopularSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedItemsToItem">
      <wsdl:input message="tns:Item_GetRecommendedItemsToItemSoapIn" />
      <wsdl:output message="tns:Item_GetRecommendedItemsToItemSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatch">
      <wsdl:input message="tns:Item_RunGenericCFInBatchSoapIn" />
      <wsdl:output message="tns:Item_RunGenericCFInBatchSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericLFInBatch">
      <wsdl:input message="tns:Item_RunGenericLFInBatchSoapIn" />
      <wsdl:output message="tns:Item_RunGenericLFInBatchSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatch">
      <wsdl:input message="tns:Category_RunGenericCFInBatchSoapIn" />
      <wsdl:output message="tns:Category_RunGenericCFInBatchSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatchForCategory">
      <wsdl:input message="tns:Category_RunGenericCFInBatchForCategorySoapIn" />
      <wsdl:output message="tns:Category_RunGenericCFInBatchForCategorySoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatchForKeywords">
      <wsdl:input message="tns:Item_RunGenericCFInBatchForKeywordsSoapIn" />
      <wsdl:output message="tns:Item_RunGenericCFInBatchForKeywordsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedKeywordsToKeyword">
      <wsdl:input message="tns:Item_GetRecommendedKeywordsToKeywordSoapIn" />
      <wsdl:output message="tns:Item_GetRecommendedKeywordsToKeywordSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedKeywordsToKeyword">
      <wsdl:input message="tns:Item_GetRelatedKeywordsToKeywordSoapIn" />
      <wsdl:output message="tns:Item_GetRelatedKeywordsToKeywordSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetMostRecentItems">
      <wsdl:input message="tns:Party_GetMostRecentItemsSoapIn" />
      <wsdl:output message="tns:Party_GetMostRecentItemsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetPersonalisedItemsToConsumer">
      <wsdl:input message="tns:Party_GetPersonalisedItemsToConsumerSoapIn" />
      <wsdl:output message="tns:Party_GetPersonalisedItemsToConsumerSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetTopPersonalisedItemsToConsumer">
      <wsdl:input message="tns:Party_GetTopPersonalisedItemsToConsumerSoapIn" />
      <wsdl:output message="tns:Party_GetTopPersonalisedItemsToConsumerSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GeneratePendingPersonalisedItemsToConsumer">
      <wsdl:input message="tns:Party_GeneratePendingPersonalisedItemsToConsumerSoapIn" />
      <wsdl:output message="tns:Party_GeneratePendingPersonalisedItemsToConsumerSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetPreferredtemsToConsumer">
      <wsdl:input message="tns:Party_GetPreferredtemsToConsumerSoapIn" />
      <wsdl:output message="tns:Party_GetPreferredtemsToConsumerSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_SetPartyStatus">
      <wsdl:input message="tns:Party_SetPartyStatusSoapIn" />
      <wsdl:output message="tns:Party_SetPartyStatusSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_SetRegisteredPartyStatus">
      <wsdl:input message="tns:Party_SetRegisteredPartyStatusSoapIn" />
      <wsdl:output message="tns:Party_SetRegisteredPartyStatusSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_GetAllCategoriesInText">
      <wsdl:input message="tns:Category_GetAllCategoriesInTextSoapIn" />
      <wsdl:output message="tns:Category_GetAllCategoriesInTextSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Reload_Mooga">
      <wsdl:input message="tns:Reload_MoogaSoapIn" />
      <wsdl:output message="tns:Reload_MoogaSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_GetRule">
      <wsdl:input message="tns:CategoryRule_GetRuleSoapIn" />
      <wsdl:output message="tns:CategoryRule_GetRuleSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_RefreshFromDB">
      <wsdl:input message="tns:Category_RefreshFromDBSoapIn" />
      <wsdl:output message="tns:Category_RefreshFromDBSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_RefreshFromDB">
      <wsdl:input message="tns:CategoryRule_RefreshFromDBSoapIn" />
      <wsdl:output message="tns:CategoryRule_RefreshFromDBSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPopular">
      <wsdl:input message="tns:Generic_GetPopularSoapIn" />
      <wsdl:output message="tns:Generic_GetPopularSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GenericPush">
      <wsdl:input message="tns:Generic_GenericPushSoapIn" />
      <wsdl:output message="tns:Generic_GenericPushSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="ExecuteService">
      <wsdl:input message="tns:ExecuteServiceSoapIn" />
      <wsdl:output message="tns:ExecuteServiceSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="ExecuteServiceWithInternalIDs">
      <wsdl:input message="tns:ExecuteServiceWithInternalIDsSoapIn" />
      <wsdl:output message="tns:ExecuteServiceWithInternalIDsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="ReplaceIDsWithExtIDs">
      <wsdl:input message="tns:ReplaceIDsWithExtIDsSoapIn" />
      <wsdl:output message="tns:ReplaceIDsWithExtIDsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="ExecuteWebServiceUsingList">
      <wsdl:input message="tns:ExecuteWebServiceUsingListSoapIn" />
      <wsdl:output message="tns:ExecuteWebServiceUsingListSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="ExecuteAllPendingServiceRequest">
      <wsdl:input message="tns:ExecuteAllPendingServiceRequestSoapIn" />
      <wsdl:output message="tns:ExecuteAllPendingServiceRequestSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetPartyItemRelevence">
      <wsdl:input message="tns:Party_GetPartyItemRelevenceSoapIn" />
      <wsdl:output message="tns:Party_GetPartyItemRelevenceSoapOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:portType name="MoogaWebServicesHttpGet">
    <wsdl:operation name="Login">
      <wsdl:input message="tns:LoginHttpGetIn" />
      <wsdl:output message="tns:LoginHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="DirectLogin">
      <wsdl:input message="tns:DirectLoginHttpGetIn" />
      <wsdl:output message="tns:DirectLoginHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Logout">
      <wsdl:input message="tns:LogoutHttpGetIn" />
      <wsdl:output message="tns:LogoutHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="UsersLoggedIn">
      <wsdl:input message="tns:UsersLoggedInHttpGetIn" />
      <wsdl:output message="tns:UsersLoggedInHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="IsApplicationRunning">
      <wsdl:input message="tns:IsApplicationRunningHttpGetIn" />
      <wsdl:output message="tns:IsApplicationRunningHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetApplicationLoadStatus">
      <wsdl:input message="tns:GetApplicationLoadStatusHttpGetIn" />
      <wsdl:output message="tns:GetApplicationLoadStatusHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CheckLoggedIn">
      <wsdl:input message="tns:CheckLoggedInHttpGetIn" />
      <wsdl:output message="tns:CheckLoggedInHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="UserLoggedIn">
      <wsdl:input message="tns:UserLoggedInHttpGetIn" />
      <wsdl:output message="tns:UserLoggedInHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetInternalID">
      <wsdl:input message="tns:GetInternalIDHttpGetIn" />
      <wsdl:output message="tns:GetInternalIDHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetTitle">
      <wsdl:input message="tns:GetTitleHttpGetIn" />
      <wsdl:output message="tns:GetTitleHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetInternalCatID">
      <wsdl:input message="tns:GetInternalCatIDHttpGetIn" />
      <wsdl:output message="tns:GetInternalCatIDHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetKeyID">
      <wsdl:input message="tns:GetKeyIDHttpGetIn" />
      <wsdl:output message="tns:GetKeyIDHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetCategory">
      <wsdl:input message="tns:GetCategoryHttpGetIn" />
      <wsdl:output message="tns:GetCategoryHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetRoleID">
      <wsdl:input message="tns:GetRoleIDHttpGetIn" />
      <wsdl:output message="tns:GetRoleIDHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetServiceID">
      <wsdl:input message="tns:GetServiceIDHttpGetIn" />
      <wsdl:output message="tns:GetServiceIDHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_EventsAndSchedules">
      <wsdl:input message="tns:Refresh_EventsAndSchedulesHttpGetIn" />
      <wsdl:output message="tns:Refresh_EventsAndSchedulesHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_IDS">
      <wsdl:input message="tns:Refresh_IDSHttpGetIn" />
      <wsdl:output message="tns:Refresh_IDSHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_Services">
      <wsdl:input message="tns:Refresh_ServicesHttpGetIn" />
      <wsdl:output message="tns:Refresh_ServicesHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_DBFilters">
      <wsdl:input message="tns:Refresh_DBFiltersHttpGetIn" />
      <wsdl:output message="tns:Refresh_DBFiltersHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTransUsingList">
      <wsdl:input message="tns:Trans_NotifyTransUsingListHttpGetIn" />
      <wsdl:output message="tns:Trans_NotifyTransUsingListHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTrans">
      <wsdl:input message="tns:Trans_NotifyTransHttpGetIn" />
      <wsdl:output message="tns:Trans_NotifyTransHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyDataUsingList">
      <wsdl:input message="tns:Notify_NotifyDataUsingListHttpGetIn" />
      <wsdl:output message="tns:Notify_NotifyDataUsingListHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyData">
      <wsdl:input message="tns:Notify_NotifyDataHttpGetIn" />
      <wsdl:output message="tns:Notify_NotifyDataHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GetQueryResult">
      <wsdl:input message="tns:Generic_GetQueryResultHttpGetIn" />
      <wsdl:output message="tns:Generic_GetQueryResultHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedItemsToItem">
      <wsdl:input message="tns:Item_GetRelatedItemsToItemHttpGetIn" />
      <wsdl:output message="tns:Item_GetRelatedItemsToItemHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersForItem">
      <wsdl:input message="tns:Party_GetConsumersForItemHttpGetIn" />
      <wsdl:output message="tns:Party_GetConsumersForItemHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersMatchingWithRule">
      <wsdl:input message="tns:Party_GetConsumersMatchingWithRuleHttpGetIn" />
      <wsdl:output message="tns:Party_GetConsumersMatchingWithRuleHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GenerateConsumerProfile">
      <wsdl:input message="tns:Party_GenerateConsumerProfileHttpGetIn" />
      <wsdl:output message="tns:Party_GenerateConsumerProfileHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetTopRatedItems">
      <wsdl:input message="tns:Item_GetTopRatedItemsHttpGetIn" />
      <wsdl:output message="tns:Item_GetTopRatedItemsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostAccessedItems">
      <wsdl:input message="tns:Item_GetMostAccessedItemsHttpGetIn" />
      <wsdl:output message="tns:Item_GetMostAccessedItemsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedItems">
      <wsdl:input message="tns:Item_GetMostSearchedItemsHttpGetIn" />
      <wsdl:output message="tns:Item_GetMostSearchedItemsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedText">
      <wsdl:input message="tns:Item_GetMostSearchedTextHttpGetIn" />
      <wsdl:output message="tns:Item_GetMostSearchedTextHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularItems">
      <wsdl:input message="tns:Item_GetPopularItemsHttpGetIn" />
      <wsdl:output message="tns:Item_GetPopularItemsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularKeywords">
      <wsdl:input message="tns:Item_GetPopularKeywordsHttpGetIn" />
      <wsdl:output message="tns:Item_GetPopularKeywordsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularBrands">
      <wsdl:input message="tns:Item_GetPopularBrandsHttpGetIn" />
      <wsdl:output message="tns:Item_GetPopularBrandsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularCategories">
      <wsdl:input message="tns:Item_GetPopularCategoriesHttpGetIn" />
      <wsdl:output message="tns:Item_GetPopularCategoriesHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPendingPopular">
      <wsdl:input message="tns:Generic_GetPendingPopularHttpGetIn" />
      <wsdl:output message="tns:Generic_GetPendingPopularHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedItemsToItem">
      <wsdl:input message="tns:Item_GetRecommendedItemsToItemHttpGetIn" />
      <wsdl:output message="tns:Item_GetRecommendedItemsToItemHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatch">
      <wsdl:input message="tns:Item_RunGenericCFInBatchHttpGetIn" />
      <wsdl:output message="tns:Item_RunGenericCFInBatchHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericLFInBatch">
      <wsdl:input message="tns:Item_RunGenericLFInBatchHttpGetIn" />
      <wsdl:output message="tns:Item_RunGenericLFInBatchHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatch">
      <wsdl:input message="tns:Category_RunGenericCFInBatchHttpGetIn" />
      <wsdl:output message="tns:Category_RunGenericCFInBatchHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatchForCategory">
      <wsdl:input message="tns:Category_RunGenericCFInBatchForCategoryHttpGetIn" />
      <wsdl:output message="tns:Category_RunGenericCFInBatchForCategoryHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatchForKeywords">
      <wsdl:input message="tns:Item_RunGenericCFInBatchForKeywordsHttpGetIn" />
      <wsdl:output message="tns:Item_RunGenericCFInBatchForKeywordsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedKeywordsToKeyword">
      <wsdl:input message="tns:Item_GetRecommendedKeywordsToKeywordHttpGetIn" />
      <wsdl:output message="tns:Item_GetRecommendedKeywordsToKeywordHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedKeywordsToKeyword">
      <wsdl:input message="tns:Item_GetRelatedKeywordsToKeywordHttpGetIn" />
      <wsdl:output message="tns:Item_GetRelatedKeywordsToKeywordHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetMostRecentItems">
      <wsdl:input message="tns:Party_GetMostRecentItemsHttpGetIn" />
      <wsdl:output message="tns:Party_GetMostRecentItemsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetPersonalisedItemsToConsumer">
      <wsdl:input message="tns:Party_GetPersonalisedItemsToConsumerHttpGetIn" />
      <wsdl:output message="tns:Party_GetPersonalisedItemsToConsumerHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetTopPersonalisedItemsToConsumer">
      <wsdl:input message="tns:Party_GetTopPersonalisedItemsToConsumerHttpGetIn" />
      <wsdl:output message="tns:Party_GetTopPersonalisedItemsToConsumerHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GeneratePendingPersonalisedItemsToConsumer">
      <wsdl:input message="tns:Party_GeneratePendingPersonalisedItemsToConsumerHttpGetIn" />
      <wsdl:output message="tns:Party_GeneratePendingPersonalisedItemsToConsumerHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetPreferredtemsToConsumer">
      <wsdl:input message="tns:Party_GetPreferredtemsToConsumerHttpGetIn" />
      <wsdl:output message="tns:Party_GetPreferredtemsToConsumerHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_SetPartyStatus">
      <wsdl:input message="tns:Party_SetPartyStatusHttpGetIn" />
      <wsdl:output message="tns:Party_SetPartyStatusHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_SetRegisteredPartyStatus">
      <wsdl:input message="tns:Party_SetRegisteredPartyStatusHttpGetIn" />
      <wsdl:output message="tns:Party_SetRegisteredPartyStatusHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_GetAllCategoriesInText">
      <wsdl:input message="tns:Category_GetAllCategoriesInTextHttpGetIn" />
      <wsdl:output message="tns:Category_GetAllCategoriesInTextHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Reload_Mooga">
      <wsdl:input message="tns:Reload_MoogaHttpGetIn" />
      <wsdl:output message="tns:Reload_MoogaHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_GetRule">
      <wsdl:input message="tns:CategoryRule_GetRuleHttpGetIn" />
      <wsdl:output message="tns:CategoryRule_GetRuleHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_RefreshFromDB">
      <wsdl:input message="tns:Category_RefreshFromDBHttpGetIn" />
      <wsdl:output message="tns:Category_RefreshFromDBHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_RefreshFromDB">
      <wsdl:input message="tns:CategoryRule_RefreshFromDBHttpGetIn" />
      <wsdl:output message="tns:CategoryRule_RefreshFromDBHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPopular">
      <wsdl:input message="tns:Generic_GetPopularHttpGetIn" />
      <wsdl:output message="tns:Generic_GetPopularHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GenericPush">
      <wsdl:input message="tns:Generic_GenericPushHttpGetIn" />
      <wsdl:output message="tns:Generic_GenericPushHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="ExecuteService">
      <wsdl:input message="tns:ExecuteServiceHttpGetIn" />
      <wsdl:output message="tns:ExecuteServiceHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="ExecuteServiceWithInternalIDs">
      <wsdl:input message="tns:ExecuteServiceWithInternalIDsHttpGetIn" />
      <wsdl:output message="tns:ExecuteServiceWithInternalIDsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="ReplaceIDsWithExtIDs">
      <wsdl:input message="tns:ReplaceIDsWithExtIDsHttpGetIn" />
      <wsdl:output message="tns:ReplaceIDsWithExtIDsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="ExecuteAllPendingServiceRequest">
      <wsdl:input message="tns:ExecuteAllPendingServiceRequestHttpGetIn" />
      <wsdl:output message="tns:ExecuteAllPendingServiceRequestHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetPartyItemRelevence">
      <wsdl:input message="tns:Party_GetPartyItemRelevenceHttpGetIn" />
      <wsdl:output message="tns:Party_GetPartyItemRelevenceHttpGetOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:portType name="MoogaWebServicesHttpPost">
    <wsdl:operation name="Login">
      <wsdl:input message="tns:LoginHttpPostIn" />
      <wsdl:output message="tns:LoginHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="DirectLogin">
      <wsdl:input message="tns:DirectLoginHttpPostIn" />
      <wsdl:output message="tns:DirectLoginHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Logout">
      <wsdl:input message="tns:LogoutHttpPostIn" />
      <wsdl:output message="tns:LogoutHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="UsersLoggedIn">
      <wsdl:input message="tns:UsersLoggedInHttpPostIn" />
      <wsdl:output message="tns:UsersLoggedInHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="IsApplicationRunning">
      <wsdl:input message="tns:IsApplicationRunningHttpPostIn" />
      <wsdl:output message="tns:IsApplicationRunningHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetApplicationLoadStatus">
      <wsdl:input message="tns:GetApplicationLoadStatusHttpPostIn" />
      <wsdl:output message="tns:GetApplicationLoadStatusHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CheckLoggedIn">
      <wsdl:input message="tns:CheckLoggedInHttpPostIn" />
      <wsdl:output message="tns:CheckLoggedInHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="UserLoggedIn">
      <wsdl:input message="tns:UserLoggedInHttpPostIn" />
      <wsdl:output message="tns:UserLoggedInHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetInternalID">
      <wsdl:input message="tns:GetInternalIDHttpPostIn" />
      <wsdl:output message="tns:GetInternalIDHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetTitle">
      <wsdl:input message="tns:GetTitleHttpPostIn" />
      <wsdl:output message="tns:GetTitleHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetInternalCatID">
      <wsdl:input message="tns:GetInternalCatIDHttpPostIn" />
      <wsdl:output message="tns:GetInternalCatIDHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetKeyID">
      <wsdl:input message="tns:GetKeyIDHttpPostIn" />
      <wsdl:output message="tns:GetKeyIDHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetCategory">
      <wsdl:input message="tns:GetCategoryHttpPostIn" />
      <wsdl:output message="tns:GetCategoryHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetRoleID">
      <wsdl:input message="tns:GetRoleIDHttpPostIn" />
      <wsdl:output message="tns:GetRoleIDHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetServiceID">
      <wsdl:input message="tns:GetServiceIDHttpPostIn" />
      <wsdl:output message="tns:GetServiceIDHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_EventsAndSchedules">
      <wsdl:input message="tns:Refresh_EventsAndSchedulesHttpPostIn" />
      <wsdl:output message="tns:Refresh_EventsAndSchedulesHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_IDS">
      <wsdl:input message="tns:Refresh_IDSHttpPostIn" />
      <wsdl:output message="tns:Refresh_IDSHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_Services">
      <wsdl:input message="tns:Refresh_ServicesHttpPostIn" />
      <wsdl:output message="tns:Refresh_ServicesHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Refresh_DBFilters">
      <wsdl:input message="tns:Refresh_DBFiltersHttpPostIn" />
      <wsdl:output message="tns:Refresh_DBFiltersHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTransUsingList">
      <wsdl:input message="tns:Trans_NotifyTransUsingListHttpPostIn" />
      <wsdl:output message="tns:Trans_NotifyTransUsingListHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTrans">
      <wsdl:input message="tns:Trans_NotifyTransHttpPostIn" />
      <wsdl:output message="tns:Trans_NotifyTransHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyDataUsingList">
      <wsdl:input message="tns:Notify_NotifyDataUsingListHttpPostIn" />
      <wsdl:output message="tns:Notify_NotifyDataUsingListHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyData">
      <wsdl:input message="tns:Notify_NotifyDataHttpPostIn" />
      <wsdl:output message="tns:Notify_NotifyDataHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GetQueryResult">
      <wsdl:input message="tns:Generic_GetQueryResultHttpPostIn" />
      <wsdl:output message="tns:Generic_GetQueryResultHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedItemsToItem">
      <wsdl:input message="tns:Item_GetRelatedItemsToItemHttpPostIn" />
      <wsdl:output message="tns:Item_GetRelatedItemsToItemHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersForItem">
      <wsdl:input message="tns:Party_GetConsumersForItemHttpPostIn" />
      <wsdl:output message="tns:Party_GetConsumersForItemHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersMatchingWithRule">
      <wsdl:input message="tns:Party_GetConsumersMatchingWithRuleHttpPostIn" />
      <wsdl:output message="tns:Party_GetConsumersMatchingWithRuleHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GenerateConsumerProfile">
      <wsdl:input message="tns:Party_GenerateConsumerProfileHttpPostIn" />
      <wsdl:output message="tns:Party_GenerateConsumerProfileHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetTopRatedItems">
      <wsdl:input message="tns:Item_GetTopRatedItemsHttpPostIn" />
      <wsdl:output message="tns:Item_GetTopRatedItemsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostAccessedItems">
      <wsdl:input message="tns:Item_GetMostAccessedItemsHttpPostIn" />
      <wsdl:output message="tns:Item_GetMostAccessedItemsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedItems">
      <wsdl:input message="tns:Item_GetMostSearchedItemsHttpPostIn" />
      <wsdl:output message="tns:Item_GetMostSearchedItemsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedText">
      <wsdl:input message="tns:Item_GetMostSearchedTextHttpPostIn" />
      <wsdl:output message="tns:Item_GetMostSearchedTextHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularItems">
      <wsdl:input message="tns:Item_GetPopularItemsHttpPostIn" />
      <wsdl:output message="tns:Item_GetPopularItemsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularKeywords">
      <wsdl:input message="tns:Item_GetPopularKeywordsHttpPostIn" />
      <wsdl:output message="tns:Item_GetPopularKeywordsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularBrands">
      <wsdl:input message="tns:Item_GetPopularBrandsHttpPostIn" />
      <wsdl:output message="tns:Item_GetPopularBrandsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularCategories">
      <wsdl:input message="tns:Item_GetPopularCategoriesHttpPostIn" />
      <wsdl:output message="tns:Item_GetPopularCategoriesHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPendingPopular">
      <wsdl:input message="tns:Generic_GetPendingPopularHttpPostIn" />
      <wsdl:output message="tns:Generic_GetPendingPopularHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedItemsToItem">
      <wsdl:input message="tns:Item_GetRecommendedItemsToItemHttpPostIn" />
      <wsdl:output message="tns:Item_GetRecommendedItemsToItemHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatch">
      <wsdl:input message="tns:Item_RunGenericCFInBatchHttpPostIn" />
      <wsdl:output message="tns:Item_RunGenericCFInBatchHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericLFInBatch">
      <wsdl:input message="tns:Item_RunGenericLFInBatchHttpPostIn" />
      <wsdl:output message="tns:Item_RunGenericLFInBatchHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatch">
      <wsdl:input message="tns:Category_RunGenericCFInBatchHttpPostIn" />
      <wsdl:output message="tns:Category_RunGenericCFInBatchHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatchForCategory">
      <wsdl:input message="tns:Category_RunGenericCFInBatchForCategoryHttpPostIn" />
      <wsdl:output message="tns:Category_RunGenericCFInBatchForCategoryHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatchForKeywords">
      <wsdl:input message="tns:Item_RunGenericCFInBatchForKeywordsHttpPostIn" />
      <wsdl:output message="tns:Item_RunGenericCFInBatchForKeywordsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedKeywordsToKeyword">
      <wsdl:input message="tns:Item_GetRecommendedKeywordsToKeywordHttpPostIn" />
      <wsdl:output message="tns:Item_GetRecommendedKeywordsToKeywordHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedKeywordsToKeyword">
      <wsdl:input message="tns:Item_GetRelatedKeywordsToKeywordHttpPostIn" />
      <wsdl:output message="tns:Item_GetRelatedKeywordsToKeywordHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetMostRecentItems">
      <wsdl:input message="tns:Party_GetMostRecentItemsHttpPostIn" />
      <wsdl:output message="tns:Party_GetMostRecentItemsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetPersonalisedItemsToConsumer">
      <wsdl:input message="tns:Party_GetPersonalisedItemsToConsumerHttpPostIn" />
      <wsdl:output message="tns:Party_GetPersonalisedItemsToConsumerHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetTopPersonalisedItemsToConsumer">
      <wsdl:input message="tns:Party_GetTopPersonalisedItemsToConsumerHttpPostIn" />
      <wsdl:output message="tns:Party_GetTopPersonalisedItemsToConsumerHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GeneratePendingPersonalisedItemsToConsumer">
      <wsdl:input message="tns:Party_GeneratePendingPersonalisedItemsToConsumerHttpPostIn" />
      <wsdl:output message="tns:Party_GeneratePendingPersonalisedItemsToConsumerHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetPreferredtemsToConsumer">
      <wsdl:input message="tns:Party_GetPreferredtemsToConsumerHttpPostIn" />
      <wsdl:output message="tns:Party_GetPreferredtemsToConsumerHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_SetPartyStatus">
      <wsdl:input message="tns:Party_SetPartyStatusHttpPostIn" />
      <wsdl:output message="tns:Party_SetPartyStatusHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_SetRegisteredPartyStatus">
      <wsdl:input message="tns:Party_SetRegisteredPartyStatusHttpPostIn" />
      <wsdl:output message="tns:Party_SetRegisteredPartyStatusHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_GetAllCategoriesInText">
      <wsdl:input message="tns:Category_GetAllCategoriesInTextHttpPostIn" />
      <wsdl:output message="tns:Category_GetAllCategoriesInTextHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Reload_Mooga">
      <wsdl:input message="tns:Reload_MoogaHttpPostIn" />
      <wsdl:output message="tns:Reload_MoogaHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_GetRule">
      <wsdl:input message="tns:CategoryRule_GetRuleHttpPostIn" />
      <wsdl:output message="tns:CategoryRule_GetRuleHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Category_RefreshFromDB">
      <wsdl:input message="tns:Category_RefreshFromDBHttpPostIn" />
      <wsdl:output message="tns:Category_RefreshFromDBHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_RefreshFromDB">
      <wsdl:input message="tns:CategoryRule_RefreshFromDBHttpPostIn" />
      <wsdl:output message="tns:CategoryRule_RefreshFromDBHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPopular">
      <wsdl:input message="tns:Generic_GetPopularHttpPostIn" />
      <wsdl:output message="tns:Generic_GetPopularHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Generic_GenericPush">
      <wsdl:input message="tns:Generic_GenericPushHttpPostIn" />
      <wsdl:output message="tns:Generic_GenericPushHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="ExecuteService">
      <wsdl:input message="tns:ExecuteServiceHttpPostIn" />
      <wsdl:output message="tns:ExecuteServiceHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="ExecuteServiceWithInternalIDs">
      <wsdl:input message="tns:ExecuteServiceWithInternalIDsHttpPostIn" />
      <wsdl:output message="tns:ExecuteServiceWithInternalIDsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="ReplaceIDsWithExtIDs">
      <wsdl:input message="tns:ReplaceIDsWithExtIDsHttpPostIn" />
      <wsdl:output message="tns:ReplaceIDsWithExtIDsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="ExecuteAllPendingServiceRequest">
      <wsdl:input message="tns:ExecuteAllPendingServiceRequestHttpPostIn" />
      <wsdl:output message="tns:ExecuteAllPendingServiceRequestHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="Party_GetPartyItemRelevence">
      <wsdl:input message="tns:Party_GetPartyItemRelevenceHttpPostIn" />
      <wsdl:output message="tns:Party_GetPartyItemRelevenceHttpPostOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:binding name="MoogaWebServicesSoap" type="tns:MoogaWebServicesSoap">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="Login">
      <soap:operation soapAction="http://ikensolution.com/webservices/Login" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DirectLogin">
      <soap:operation soapAction="http://ikensolution.com/webservices/DirectLogin" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Logout">
      <soap:operation soapAction="http://ikensolution.com/webservices/Logout" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="UsersLoggedIn">
      <soap:operation soapAction="http://ikensolution.com/webservices/UsersLoggedIn" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="IsApplicationRunning">
      <soap:operation soapAction="http://ikensolution.com/webservices/IsApplicationRunning" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetApplicationLoadStatus">
      <soap:operation soapAction="http://ikensolution.com/webservices/GetApplicationLoadStatus" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CheckLoggedIn">
      <soap:operation soapAction="http://ikensolution.com/webservices/CheckLoggedIn" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="UserLoggedIn">
      <soap:operation soapAction="http://ikensolution.com/webservices/UserLoggedIn" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetInternalID">
      <soap:operation soapAction="http://ikensolution.com/webservices/GetInternalID" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetTitle">
      <soap:operation soapAction="http://ikensolution.com/webservices/GetTitle" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetInternalCatID">
      <soap:operation soapAction="http://ikensolution.com/webservices/GetInternalCatID" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetKeyID">
      <soap:operation soapAction="http://ikensolution.com/webservices/GetKeyID" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetCategory">
      <soap:operation soapAction="http://ikensolution.com/webservices/GetCategory" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetRoleID">
      <soap:operation soapAction="http://ikensolution.com/webservices/GetRoleID" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetServiceID">
      <soap:operation soapAction="http://ikensolution.com/webservices/GetServiceID" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_EventsAndSchedules">
      <soap:operation soapAction="http://ikensolution.com/webservices/Refresh_EventsAndSchedules" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_IDS">
      <soap:operation soapAction="http://ikensolution.com/webservices/Refresh_IDS" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_Services">
      <soap:operation soapAction="http://ikensolution.com/webservices/Refresh_Services" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_DBFilters">
      <soap:operation soapAction="http://ikensolution.com/webservices/Refresh_DBFilters" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTransUsingList">
      <soap:operation soapAction="http://ikensolution.com/webservices/Trans_NotifyTransUsingList" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTrans">
      <soap:operation soapAction="http://ikensolution.com/webservices/Trans_NotifyTrans" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyDataUsingList">
      <soap:operation soapAction="http://ikensolution.com/webservices/Notify_NotifyDataUsingList" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyData">
      <soap:operation soapAction="http://ikensolution.com/webservices/Notify_NotifyData" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetQueryResult">
      <soap:operation soapAction="http://ikensolution.com/webservices/Generic_GetQueryResult" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedItemsToItem">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetRelatedItemsToItem" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersForItem">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_GetConsumersForItem" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersMatchingWithRule">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_GetConsumersMatchingWithRule" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GenerateConsumerProfile">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_GenerateConsumerProfile" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetTopRatedItems">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetTopRatedItems" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostAccessedItems">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetMostAccessedItems" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedItems">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetMostSearchedItems" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedText">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetMostSearchedText" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularItems">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetPopularItems" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularKeywords">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetPopularKeywords" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularBrands">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetPopularBrands" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularCategories">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetPopularCategories" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPendingPopular">
      <soap:operation soapAction="http://ikensolution.com/webservices/Generic_GetPendingPopular" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedItemsToItem">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetRecommendedItemsToItem" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatch">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_RunGenericCFInBatch" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericLFInBatch">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_RunGenericLFInBatch" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatch">
      <soap:operation soapAction="http://ikensolution.com/webservices/Category_RunGenericCFInBatch" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatchForCategory">
      <soap:operation soapAction="http://ikensolution.com/webservices/Category_RunGenericCFInBatchForCategory" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatchForKeywords">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_RunGenericCFInBatchForKeywords" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedKeywordsToKeyword">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetRecommendedKeywordsToKeyword" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedKeywordsToKeyword">
      <soap:operation soapAction="http://ikensolution.com/webservices/Item_GetRelatedKeywordsToKeyword" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetMostRecentItems">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_GetMostRecentItems" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPersonalisedItemsToConsumer">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_GetPersonalisedItemsToConsumer" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetTopPersonalisedItemsToConsumer">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_GetTopPersonalisedItemsToConsumer" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GeneratePendingPersonalisedItemsToConsumer">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_GeneratePendingPersonalisedItemsToConsumer" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPreferredtemsToConsumer">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_GetPreferredtemsToConsumer" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_SetPartyStatus">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_SetPartyStatus" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_SetRegisteredPartyStatus">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_SetRegisteredPartyStatus" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_GetAllCategoriesInText">
      <soap:operation soapAction="http://ikensolution.com/webservices/Category_GetAllCategoriesInText" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Reload_Mooga">
      <soap:operation soapAction="http://ikensolution.com/webservices/Reload_Mooga" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_GetRule">
      <soap:operation soapAction="http://ikensolution.com/webservices/CategoryRule_GetRule" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RefreshFromDB">
      <soap:operation soapAction="http://ikensolution.com/webservices/Category_RefreshFromDB" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_RefreshFromDB">
      <soap:operation soapAction="http://ikensolution.com/webservices/CategoryRule_RefreshFromDB" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPopular">
      <soap:operation soapAction="http://ikensolution.com/webservices/Generic_GetPopular" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GenericPush">
      <soap:operation soapAction="http://ikensolution.com/webservices/Generic_GenericPush" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteService">
      <soap:operation soapAction="http://ikensolution.com/webservices/ExecuteService" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteServiceWithInternalIDs">
      <soap:operation soapAction="http://ikensolution.com/webservices/ExecuteServiceWithInternalIDs" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ReplaceIDsWithExtIDs">
      <soap:operation soapAction="http://ikensolution.com/webservices/ReplaceIDsWithExtIDs" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteWebServiceUsingList">
      <soap:operation soapAction="http://ikensolution.com/webservices/ExecuteWebServiceUsingList" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteAllPendingServiceRequest">
      <soap:operation soapAction="http://ikensolution.com/webservices/ExecuteAllPendingServiceRequest" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPartyItemRelevence">
      <soap:operation soapAction="http://ikensolution.com/webservices/Party_GetPartyItemRelevence" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="MoogaWebServicesSoap12" type="tns:MoogaWebServicesSoap">
    <soap12:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="Login">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Login" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DirectLogin">
      <soap12:operation soapAction="http://ikensolution.com/webservices/DirectLogin" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Logout">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Logout" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="UsersLoggedIn">
      <soap12:operation soapAction="http://ikensolution.com/webservices/UsersLoggedIn" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="IsApplicationRunning">
      <soap12:operation soapAction="http://ikensolution.com/webservices/IsApplicationRunning" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetApplicationLoadStatus">
      <soap12:operation soapAction="http://ikensolution.com/webservices/GetApplicationLoadStatus" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CheckLoggedIn">
      <soap12:operation soapAction="http://ikensolution.com/webservices/CheckLoggedIn" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="UserLoggedIn">
      <soap12:operation soapAction="http://ikensolution.com/webservices/UserLoggedIn" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetInternalID">
      <soap12:operation soapAction="http://ikensolution.com/webservices/GetInternalID" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetTitle">
      <soap12:operation soapAction="http://ikensolution.com/webservices/GetTitle" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetInternalCatID">
      <soap12:operation soapAction="http://ikensolution.com/webservices/GetInternalCatID" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetKeyID">
      <soap12:operation soapAction="http://ikensolution.com/webservices/GetKeyID" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetCategory">
      <soap12:operation soapAction="http://ikensolution.com/webservices/GetCategory" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetRoleID">
      <soap12:operation soapAction="http://ikensolution.com/webservices/GetRoleID" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetServiceID">
      <soap12:operation soapAction="http://ikensolution.com/webservices/GetServiceID" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_EventsAndSchedules">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Refresh_EventsAndSchedules" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_IDS">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Refresh_IDS" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_Services">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Refresh_Services" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_DBFilters">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Refresh_DBFilters" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTransUsingList">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Trans_NotifyTransUsingList" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTrans">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Trans_NotifyTrans" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyDataUsingList">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Notify_NotifyDataUsingList" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyData">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Notify_NotifyData" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetQueryResult">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Generic_GetQueryResult" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedItemsToItem">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetRelatedItemsToItem" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersForItem">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_GetConsumersForItem" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersMatchingWithRule">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_GetConsumersMatchingWithRule" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GenerateConsumerProfile">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_GenerateConsumerProfile" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetTopRatedItems">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetTopRatedItems" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostAccessedItems">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetMostAccessedItems" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedItems">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetMostSearchedItems" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedText">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetMostSearchedText" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularItems">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetPopularItems" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularKeywords">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetPopularKeywords" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularBrands">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetPopularBrands" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularCategories">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetPopularCategories" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPendingPopular">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Generic_GetPendingPopular" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedItemsToItem">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetRecommendedItemsToItem" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatch">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_RunGenericCFInBatch" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericLFInBatch">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_RunGenericLFInBatch" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatch">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Category_RunGenericCFInBatch" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatchForCategory">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Category_RunGenericCFInBatchForCategory" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatchForKeywords">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_RunGenericCFInBatchForKeywords" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedKeywordsToKeyword">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetRecommendedKeywordsToKeyword" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedKeywordsToKeyword">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Item_GetRelatedKeywordsToKeyword" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetMostRecentItems">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_GetMostRecentItems" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPersonalisedItemsToConsumer">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_GetPersonalisedItemsToConsumer" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetTopPersonalisedItemsToConsumer">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_GetTopPersonalisedItemsToConsumer" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GeneratePendingPersonalisedItemsToConsumer">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_GeneratePendingPersonalisedItemsToConsumer" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPreferredtemsToConsumer">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_GetPreferredtemsToConsumer" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_SetPartyStatus">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_SetPartyStatus" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_SetRegisteredPartyStatus">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_SetRegisteredPartyStatus" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_GetAllCategoriesInText">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Category_GetAllCategoriesInText" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Reload_Mooga">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Reload_Mooga" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_GetRule">
      <soap12:operation soapAction="http://ikensolution.com/webservices/CategoryRule_GetRule" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RefreshFromDB">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Category_RefreshFromDB" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_RefreshFromDB">
      <soap12:operation soapAction="http://ikensolution.com/webservices/CategoryRule_RefreshFromDB" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPopular">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Generic_GetPopular" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GenericPush">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Generic_GenericPush" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteService">
      <soap12:operation soapAction="http://ikensolution.com/webservices/ExecuteService" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteServiceWithInternalIDs">
      <soap12:operation soapAction="http://ikensolution.com/webservices/ExecuteServiceWithInternalIDs" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ReplaceIDsWithExtIDs">
      <soap12:operation soapAction="http://ikensolution.com/webservices/ReplaceIDsWithExtIDs" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteWebServiceUsingList">
      <soap12:operation soapAction="http://ikensolution.com/webservices/ExecuteWebServiceUsingList" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteAllPendingServiceRequest">
      <soap12:operation soapAction="http://ikensolution.com/webservices/ExecuteAllPendingServiceRequest" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPartyItemRelevence">
      <soap12:operation soapAction="http://ikensolution.com/webservices/Party_GetPartyItemRelevence" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="MoogaWebServicesHttpGet" type="tns:MoogaWebServicesHttpGet">
    <http:binding verb="GET" />
    <wsdl:operation name="Login">
      <http:operation location="/Login" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DirectLogin">
      <http:operation location="/DirectLogin" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Logout">
      <http:operation location="/Logout" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="UsersLoggedIn">
      <http:operation location="/UsersLoggedIn" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="IsApplicationRunning">
      <http:operation location="/IsApplicationRunning" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetApplicationLoadStatus">
      <http:operation location="/GetApplicationLoadStatus" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CheckLoggedIn">
      <http:operation location="/CheckLoggedIn" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="UserLoggedIn">
      <http:operation location="/UserLoggedIn" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetInternalID">
      <http:operation location="/GetInternalID" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetTitle">
      <http:operation location="/GetTitle" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetInternalCatID">
      <http:operation location="/GetInternalCatID" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetKeyID">
      <http:operation location="/GetKeyID" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetCategory">
      <http:operation location="/GetCategory" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetRoleID">
      <http:operation location="/GetRoleID" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetServiceID">
      <http:operation location="/GetServiceID" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_EventsAndSchedules">
      <http:operation location="/Refresh_EventsAndSchedules" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_IDS">
      <http:operation location="/Refresh_IDS" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_Services">
      <http:operation location="/Refresh_Services" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_DBFilters">
      <http:operation location="/Refresh_DBFilters" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTransUsingList">
      <http:operation location="/Trans_NotifyTransUsingList" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTrans">
      <http:operation location="/Trans_NotifyTrans" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyDataUsingList">
      <http:operation location="/Notify_NotifyDataUsingList" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyData">
      <http:operation location="/Notify_NotifyData" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetQueryResult">
      <http:operation location="/Generic_GetQueryResult" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedItemsToItem">
      <http:operation location="/Item_GetRelatedItemsToItem" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersForItem">
      <http:operation location="/Party_GetConsumersForItem" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersMatchingWithRule">
      <http:operation location="/Party_GetConsumersMatchingWithRule" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GenerateConsumerProfile">
      <http:operation location="/Party_GenerateConsumerProfile" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetTopRatedItems">
      <http:operation location="/Item_GetTopRatedItems" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostAccessedItems">
      <http:operation location="/Item_GetMostAccessedItems" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedItems">
      <http:operation location="/Item_GetMostSearchedItems" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedText">
      <http:operation location="/Item_GetMostSearchedText" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularItems">
      <http:operation location="/Item_GetPopularItems" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularKeywords">
      <http:operation location="/Item_GetPopularKeywords" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularBrands">
      <http:operation location="/Item_GetPopularBrands" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularCategories">
      <http:operation location="/Item_GetPopularCategories" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPendingPopular">
      <http:operation location="/Generic_GetPendingPopular" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedItemsToItem">
      <http:operation location="/Item_GetRecommendedItemsToItem" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatch">
      <http:operation location="/Item_RunGenericCFInBatch" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericLFInBatch">
      <http:operation location="/Item_RunGenericLFInBatch" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatch">
      <http:operation location="/Category_RunGenericCFInBatch" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatchForCategory">
      <http:operation location="/Category_RunGenericCFInBatchForCategory" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatchForKeywords">
      <http:operation location="/Item_RunGenericCFInBatchForKeywords" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedKeywordsToKeyword">
      <http:operation location="/Item_GetRecommendedKeywordsToKeyword" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedKeywordsToKeyword">
      <http:operation location="/Item_GetRelatedKeywordsToKeyword" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetMostRecentItems">
      <http:operation location="/Party_GetMostRecentItems" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPersonalisedItemsToConsumer">
      <http:operation location="/Party_GetPersonalisedItemsToConsumer" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetTopPersonalisedItemsToConsumer">
      <http:operation location="/Party_GetTopPersonalisedItemsToConsumer" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GeneratePendingPersonalisedItemsToConsumer">
      <http:operation location="/Party_GeneratePendingPersonalisedItemsToConsumer" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPreferredtemsToConsumer">
      <http:operation location="/Party_GetPreferredtemsToConsumer" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_SetPartyStatus">
      <http:operation location="/Party_SetPartyStatus" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_SetRegisteredPartyStatus">
      <http:operation location="/Party_SetRegisteredPartyStatus" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_GetAllCategoriesInText">
      <http:operation location="/Category_GetAllCategoriesInText" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Reload_Mooga">
      <http:operation location="/Reload_Mooga" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_GetRule">
      <http:operation location="/CategoryRule_GetRule" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RefreshFromDB">
      <http:operation location="/Category_RefreshFromDB" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_RefreshFromDB">
      <http:operation location="/CategoryRule_RefreshFromDB" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPopular">
      <http:operation location="/Generic_GetPopular" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GenericPush">
      <http:operation location="/Generic_GenericPush" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteService">
      <http:operation location="/ExecuteService" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteServiceWithInternalIDs">
      <http:operation location="/ExecuteServiceWithInternalIDs" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ReplaceIDsWithExtIDs">
      <http:operation location="/ReplaceIDsWithExtIDs" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteAllPendingServiceRequest">
      <http:operation location="/ExecuteAllPendingServiceRequest" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPartyItemRelevence">
      <http:operation location="/Party_GetPartyItemRelevence" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="MoogaWebServicesHttpPost" type="tns:MoogaWebServicesHttpPost">
    <http:binding verb="POST" />
    <wsdl:operation name="Login">
      <http:operation location="/Login" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DirectLogin">
      <http:operation location="/DirectLogin" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Logout">
      <http:operation location="/Logout" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="UsersLoggedIn">
      <http:operation location="/UsersLoggedIn" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="IsApplicationRunning">
      <http:operation location="/IsApplicationRunning" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetApplicationLoadStatus">
      <http:operation location="/GetApplicationLoadStatus" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CheckLoggedIn">
      <http:operation location="/CheckLoggedIn" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="UserLoggedIn">
      <http:operation location="/UserLoggedIn" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetInternalID">
      <http:operation location="/GetInternalID" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetTitle">
      <http:operation location="/GetTitle" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetInternalCatID">
      <http:operation location="/GetInternalCatID" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetKeyID">
      <http:operation location="/GetKeyID" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetCategory">
      <http:operation location="/GetCategory" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetRoleID">
      <http:operation location="/GetRoleID" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetServiceID">
      <http:operation location="/GetServiceID" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_EventsAndSchedules">
      <http:operation location="/Refresh_EventsAndSchedules" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_IDS">
      <http:operation location="/Refresh_IDS" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_Services">
      <http:operation location="/Refresh_Services" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Refresh_DBFilters">
      <http:operation location="/Refresh_DBFilters" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTransUsingList">
      <http:operation location="/Trans_NotifyTransUsingList" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Trans_NotifyTrans">
      <http:operation location="/Trans_NotifyTrans" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyDataUsingList">
      <http:operation location="/Notify_NotifyDataUsingList" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Notify_NotifyData">
      <http:operation location="/Notify_NotifyData" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetQueryResult">
      <http:operation location="/Generic_GetQueryResult" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedItemsToItem">
      <http:operation location="/Item_GetRelatedItemsToItem" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersForItem">
      <http:operation location="/Party_GetConsumersForItem" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetConsumersMatchingWithRule">
      <http:operation location="/Party_GetConsumersMatchingWithRule" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GenerateConsumerProfile">
      <http:operation location="/Party_GenerateConsumerProfile" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetTopRatedItems">
      <http:operation location="/Item_GetTopRatedItems" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostAccessedItems">
      <http:operation location="/Item_GetMostAccessedItems" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedItems">
      <http:operation location="/Item_GetMostSearchedItems" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetMostSearchedText">
      <http:operation location="/Item_GetMostSearchedText" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularItems">
      <http:operation location="/Item_GetPopularItems" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularKeywords">
      <http:operation location="/Item_GetPopularKeywords" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularBrands">
      <http:operation location="/Item_GetPopularBrands" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetPopularCategories">
      <http:operation location="/Item_GetPopularCategories" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPendingPopular">
      <http:operation location="/Generic_GetPendingPopular" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedItemsToItem">
      <http:operation location="/Item_GetRecommendedItemsToItem" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatch">
      <http:operation location="/Item_RunGenericCFInBatch" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericLFInBatch">
      <http:operation location="/Item_RunGenericLFInBatch" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatch">
      <http:operation location="/Category_RunGenericCFInBatch" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RunGenericCFInBatchForCategory">
      <http:operation location="/Category_RunGenericCFInBatchForCategory" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_RunGenericCFInBatchForKeywords">
      <http:operation location="/Item_RunGenericCFInBatchForKeywords" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRecommendedKeywordsToKeyword">
      <http:operation location="/Item_GetRecommendedKeywordsToKeyword" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Item_GetRelatedKeywordsToKeyword">
      <http:operation location="/Item_GetRelatedKeywordsToKeyword" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetMostRecentItems">
      <http:operation location="/Party_GetMostRecentItems" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPersonalisedItemsToConsumer">
      <http:operation location="/Party_GetPersonalisedItemsToConsumer" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetTopPersonalisedItemsToConsumer">
      <http:operation location="/Party_GetTopPersonalisedItemsToConsumer" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GeneratePendingPersonalisedItemsToConsumer">
      <http:operation location="/Party_GeneratePendingPersonalisedItemsToConsumer" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPreferredtemsToConsumer">
      <http:operation location="/Party_GetPreferredtemsToConsumer" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_SetPartyStatus">
      <http:operation location="/Party_SetPartyStatus" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_SetRegisteredPartyStatus">
      <http:operation location="/Party_SetRegisteredPartyStatus" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_GetAllCategoriesInText">
      <http:operation location="/Category_GetAllCategoriesInText" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Reload_Mooga">
      <http:operation location="/Reload_Mooga" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_GetRule">
      <http:operation location="/CategoryRule_GetRule" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Category_RefreshFromDB">
      <http:operation location="/Category_RefreshFromDB" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CategoryRule_RefreshFromDB">
      <http:operation location="/CategoryRule_RefreshFromDB" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GetPopular">
      <http:operation location="/Generic_GetPopular" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Generic_GenericPush">
      <http:operation location="/Generic_GenericPush" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteService">
      <http:operation location="/ExecuteService" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteServiceWithInternalIDs">
      <http:operation location="/ExecuteServiceWithInternalIDs" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ReplaceIDsWithExtIDs">
      <http:operation location="/ReplaceIDsWithExtIDs" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ExecuteAllPendingServiceRequest">
      <http:operation location="/ExecuteAllPendingServiceRequest" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="Party_GetPartyItemRelevence">
      <http:operation location="/Party_GetPartyItemRelevence" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="MoogaWebServices">
    <wsdl:port name="MoogaWebServicesSoap" binding="tns:MoogaWebServicesSoap">
      <soap:address location="http://110.234.176.23/HealthKart_Mooga/moogawebservices.asmx" />
    </wsdl:port>
    <wsdl:port name="MoogaWebServicesSoap12" binding="tns:MoogaWebServicesSoap12">
      <soap12:address location="http://110.234.176.23/HealthKart_Mooga/moogawebservices.asmx" />
    </wsdl:port>
    <wsdl:port name="MoogaWebServicesHttpGet" binding="tns:MoogaWebServicesHttpGet">
      <http:address location="http://110.234.176.23/HealthKart_Mooga/moogawebservices.asmx" />
    </wsdl:port>
    <wsdl:port name="MoogaWebServicesHttpPost" binding="tns:MoogaWebServicesHttpPost">
      <http:address location="http://110.234.176.23/HealthKart_Mooga/moogawebservices.asmx" />
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>