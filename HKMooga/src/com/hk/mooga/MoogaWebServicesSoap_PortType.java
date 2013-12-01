/**
 * MoogaWebServicesSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public interface MoogaWebServicesSoap_PortType extends java.rmi.Remote {
    public String login(String p_User, String p_Application) throws java.rmi.RemoteException;
    public String directLogin(String p_User, int p_Key) throws java.rmi.RemoteException;
    public String logout(int p_Key, String p_Application) throws java.rmi.RemoteException;
    public int usersLoggedIn(String p_Application) throws java.rmi.RemoteException;
    public boolean isApplicationRunning(String p_Application) throws java.rmi.RemoteException;
    public String getApplicationLoadStatus(String p_Application) throws java.rmi.RemoteException;
    public boolean checkLoggedIn(int p_Key) throws java.rmi.RemoteException;
    public String userLoggedIn(int p_Key, String p_Application) throws java.rmi.RemoteException;
    public long getInternalID(int p_Key, String p_ExtID, String p_IDType) throws java.rmi.RemoteException;
    public String getTitle(int p_Key, String p_ExtID, String p_IDType) throws java.rmi.RemoteException;
    public long getInternalCatID(int p_Key, String p_ExtCategoryID, String p_CatIDType) throws java.rmi.RemoteException;
    public long getKeyID(int p_Key, String p_Keyword) throws java.rmi.RemoteException;
    public String getCategory(int p_Key, String p_ExtCategoryID) throws java.rmi.RemoteException;
    public long getRoleID(int p_Key, String p_Role) throws java.rmi.RemoteException;
    public long getServiceID(int p_Key, String p_ServiceName) throws java.rmi.RemoteException;
    public String refresh_EventsAndSchedules(int p_Key, String p_Application) throws java.rmi.RemoteException;
    public String refresh_IDS(int p_Key, String p_Application, String p_MoogaIDType) throws java.rmi.RemoteException;
    public String refresh_Services(int p_Key, String p_Application) throws java.rmi.RemoteException;
    public String refresh_DBFilters(int p_Key, String p_Application) throws java.rmi.RemoteException;
    public String trans_NotifyTransUsingList(int p_Key, String p_UDFName, String[] p_ArgumentList) throws java.rmi.RemoteException;
    public String trans_NotifyTrans(int p_Key, String p_UDFName, String p_Arguments) throws java.rmi.RemoteException;
    public String notify_NotifyDataUsingList(int p_Key, String p_UDFName, String[] p_ArgumentList) throws java.rmi.RemoteException;
    public String notify_NotifyData(int p_Key, String p_UDFName, String p_Arguments) throws java.rmi.RemoteException;
    public String generic_GetQueryResult(int p_Key, String p_ExtItemOrPartyID, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceNameNCode, boolean p_IsConvertToExternalID) throws java.rmi.RemoteException;
    public String item_GetRelatedItemsToItem(int p_Key, String p_ExtItemID, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String party_GetConsumersForItem(int p_Key, String p_ExtItemID, String p_ExtItemCategoryID, String p_ExtLocationID) throws java.rmi.RemoteException;
    public String party_GetConsumersMatchingWithRule(int p_Key, String p_BusinessRule, String p_ExtItemCategoryID) throws java.rmi.RemoteException;
    public String party_GenerateConsumerProfile(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtLocationID) throws java.rmi.RemoteException;
    public String item_GetTopRatedItems(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtLocationID) throws java.rmi.RemoteException;
    public String item_GetMostAccessedItems(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String item_GetMostSearchedItems(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String item_GetMostSearchedText(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String item_GetPopularItems(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String item_GetPopularKeywords(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String item_GetPopularBrands(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String item_GetPopularCategories(int p_Key, String p_ExtPartyID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String generic_GetPendingPopular(int p_Key, long p_PartyID, int p_ItemCategoryID) throws java.rmi.RemoteException;
    public String item_GetRecommendedItemsToItem(int p_Key, String p_ExtItemID, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String item_RunGenericCFInBatch(int p_Key, String p_ServiceCode, String p_DBCriteria) throws java.rmi.RemoteException;
    public String item_RunGenericLFInBatch(int p_Key, String p_ServiceCode, String p_DBCriteria) throws java.rmi.RemoteException;
    public String category_RunGenericCFInBatch(int p_Key, String p_ServiceCode, String p_ServiceName, String p_DBCriteria) throws java.rmi.RemoteException;
    public String category_RunGenericCFInBatchForCategory(int p_Key, String p_ServiceCode, String p_DBCriteria) throws java.rmi.RemoteException;
    public String item_RunGenericCFInBatchForKeywords(int p_Key, String p_ServiceCode, String p_DBCriteria) throws java.rmi.RemoteException;
    public String item_GetRecommendedKeywordsToKeyword(int p_Key, String p_Keyword, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String item_GetRelatedKeywordsToKeyword(int p_Key, String p_Keyword, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String party_GetMostRecentItems(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String party_GetPersonalisedItemsToConsumer(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtPartyLocationID, String p_TimeSlotID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String party_GetTopPersonalisedItemsToConsumer(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtPartyLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String party_GeneratePendingPersonalisedItemsToConsumer(int p_Key, String p_ExtItemCategoryID, String p_ExtPartyLocationID, String p_ServiceCode, int p_ServerID) throws java.rmi.RemoteException;
    public String party_GetPreferredtemsToConsumer(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ExtPartyLocationID, String p_ServiceCode) throws java.rmi.RemoteException;
    public String party_SetPartyStatus(int p_Key, String p_TempExtPartyID, String p_ExtPartyID, String p_PartyName, String p_Type, String p_Status) throws java.rmi.RemoteException;
    public String party_SetRegisteredPartyStatus(int p_Key, String p_TempExtPartyID, String p_ExtPartyID) throws java.rmi.RemoteException;
    public String[] category_GetAllCategoriesInText(int p_Key, boolean p_IsPrefixWithHID, String p_CategorySeparator) throws java.rmi.RemoteException;
    public String reload_Mooga(int p_Key) throws java.rmi.RemoteException;
    public float[] categoryRule_GetRule(int p_Key, String p_ExtItemCategoryID, String p_ServiceName, String p_FeatureName) throws java.rmi.RemoteException;
    public String category_RefreshFromDB(int p_Key) throws java.rmi.RemoteException;
    public String categoryRule_RefreshFromDB(int p_Key) throws java.rmi.RemoteException;
    public String generic_GetPopular(int p_Key, String p_ExtPartyID, String p_ExtItemCategoryID, String p_ServiceNameNCode, String p_ExtLocationID) throws java.rmi.RemoteException;
    public String generic_GenericPush(int p_Key, String p_ExtID, String p_ExtCategoryID, String p_ExtLocationID, String p_ServiceNameNCode, String p_MoogaBasicServiceType) throws java.rmi.RemoteException;
    public String executeService(int p_Key, String p_ExtPartyID, String p_ExtID, String p_ExtCategoryID, String p_ExtLocationID, String p_ExtTimeID, short p_ServiceID) throws java.rmi.RemoteException;
    public String executeServiceWithInternalIDs(int p_Key, String p_ExtPartyID, String p_ID, String p_CategoryID, String p_LocationID, String p_TimeID, short p_ServiceID) throws java.rmi.RemoteException;
    public String replaceIDsWithExtIDs(int p_Key, String p_StrWithInternalIDs, String p_MoogaIDType, String p_MoogaCatIDType, String p_ValueSeparator, String p_PercSeparator) throws java.rmi.RemoteException;
    public String executeWebServiceUsingList(int p_Key, String p_WebServiceName, Object[] p_Parameters) throws java.rmi.RemoteException;
    public String executeAllPendingServiceRequest(int p_Key) throws java.rmi.RemoteException;
    public String party_GetPartyItemRelevence(int p_Key, String p_ExtPartyID, String p_ExtItemID, String p_ExtCatID, String p_ServiceCode) throws java.rmi.RemoteException;
}
