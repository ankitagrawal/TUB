/**
 * MoogaWebServicesSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hk.mooga;

public interface MoogaWebServicesSoap_PortType extends java.rmi.Remote {
    public java.lang.String login(java.lang.String p_User, java.lang.String p_Application) throws java.rmi.RemoteException;
    public java.lang.String directLogin(java.lang.String p_User, int p_Key) throws java.rmi.RemoteException;
    public java.lang.String logout(int p_Key, java.lang.String p_Application) throws java.rmi.RemoteException;
    public int usersLoggedIn(java.lang.String p_Application) throws java.rmi.RemoteException;
    public boolean isApplicationRunning(java.lang.String p_Application) throws java.rmi.RemoteException;
    public java.lang.String getApplicationLoadStatus(java.lang.String p_Application) throws java.rmi.RemoteException;
    public boolean checkLoggedIn(int p_Key) throws java.rmi.RemoteException;
    public java.lang.String userLoggedIn(int p_Key, java.lang.String p_Application) throws java.rmi.RemoteException;
    public long getInternalID(int p_Key, java.lang.String p_ExtID, java.lang.String p_IDType) throws java.rmi.RemoteException;
    public java.lang.String getTitle(int p_Key, java.lang.String p_ExtID, java.lang.String p_IDType) throws java.rmi.RemoteException;
    public long getInternalCatID(int p_Key, java.lang.String p_ExtCategoryID, java.lang.String p_CatIDType) throws java.rmi.RemoteException;
    public long getKeyID(int p_Key, java.lang.String p_Keyword) throws java.rmi.RemoteException;
    public java.lang.String getCategory(int p_Key, java.lang.String p_ExtCategoryID) throws java.rmi.RemoteException;
    public long getRoleID(int p_Key, java.lang.String p_Role) throws java.rmi.RemoteException;
    public long getServiceID(int p_Key, java.lang.String p_ServiceName) throws java.rmi.RemoteException;
    public java.lang.String refresh_EventsAndSchedules(int p_Key, java.lang.String p_Application) throws java.rmi.RemoteException;
    public java.lang.String refresh_IDS(int p_Key, java.lang.String p_Application, java.lang.String p_MoogaIDType) throws java.rmi.RemoteException;
    public java.lang.String refresh_Services(int p_Key, java.lang.String p_Application) throws java.rmi.RemoteException;
    public java.lang.String refresh_DBFilters(int p_Key, java.lang.String p_Application) throws java.rmi.RemoteException;
    public java.lang.String trans_NotifyTransUsingList(int p_Key, java.lang.String p_UDFName, java.lang.String[] p_ArgumentList) throws java.rmi.RemoteException;
    public java.lang.String trans_NotifyTrans(int p_Key, java.lang.String p_UDFName, java.lang.String p_Arguments) throws java.rmi.RemoteException;
    public java.lang.String notify_NotifyDataUsingList(int p_Key, java.lang.String p_UDFName, java.lang.String[] p_ArgumentList) throws java.rmi.RemoteException;
    public java.lang.String notify_NotifyData(int p_Key, java.lang.String p_UDFName, java.lang.String p_Arguments) throws java.rmi.RemoteException;
    public java.lang.String generic_GetQueryResult(int p_Key, java.lang.String p_ExtItemOrPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceNameNCode, boolean p_IsConvertToExternalID) throws java.rmi.RemoteException;
    public java.lang.String item_GetRelatedItemsToItem(int p_Key, java.lang.String p_ExtItemID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String party_GetConsumersForItem(int p_Key, java.lang.String p_ExtItemID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID) throws java.rmi.RemoteException;
    public java.lang.String party_GetConsumersMatchingWithRule(int p_Key, java.lang.String p_BusinessRule, java.lang.String p_ExtItemCategoryID) throws java.rmi.RemoteException;
    public java.lang.String party_GenerateConsumerProfile(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID) throws java.rmi.RemoteException;
    public java.lang.String item_GetTopRatedItems(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID) throws java.rmi.RemoteException;
    public java.lang.String item_GetMostAccessedItems(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String item_GetMostSearchedItems(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String item_GetMostSearchedText(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String item_GetPopularItems(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String item_GetPopularKeywords(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String item_GetPopularBrands(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String item_GetPopularCategories(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String generic_GetPendingPopular(int p_Key, long p_PartyID, int p_ItemCategoryID) throws java.rmi.RemoteException;
    public java.lang.String item_GetRecommendedItemsToItem(int p_Key, java.lang.String p_ExtItemID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String item_RunGenericCFInBatch(int p_Key, java.lang.String p_ServiceCode, java.lang.String p_DBCriteria) throws java.rmi.RemoteException;
    public java.lang.String item_RunGenericLFInBatch(int p_Key, java.lang.String p_ServiceCode, java.lang.String p_DBCriteria) throws java.rmi.RemoteException;
    public java.lang.String category_RunGenericCFInBatch(int p_Key, java.lang.String p_ServiceCode, java.lang.String p_ServiceName, java.lang.String p_DBCriteria) throws java.rmi.RemoteException;
    public java.lang.String category_RunGenericCFInBatchForCategory(int p_Key, java.lang.String p_ServiceCode, java.lang.String p_DBCriteria) throws java.rmi.RemoteException;
    public java.lang.String item_RunGenericCFInBatchForKeywords(int p_Key, java.lang.String p_ServiceCode, java.lang.String p_DBCriteria) throws java.rmi.RemoteException;
    public java.lang.String item_GetRecommendedKeywordsToKeyword(int p_Key, java.lang.String p_Keyword, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String item_GetRelatedKeywordsToKeyword(int p_Key, java.lang.String p_Keyword, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String party_GetMostRecentItems(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String party_GetPersonalisedItemsToConsumer(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtPartyLocationID, java.lang.String p_TimeSlotID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String party_GetTopPersonalisedItemsToConsumer(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtPartyLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String party_GeneratePendingPersonalisedItemsToConsumer(int p_Key, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtPartyLocationID, java.lang.String p_ServiceCode, int p_ServerID) throws java.rmi.RemoteException;
    public java.lang.String party_GetPreferredtemsToConsumer(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ExtPartyLocationID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
    public java.lang.String party_SetPartyStatus(int p_Key, java.lang.String p_TempExtPartyID, java.lang.String p_ExtPartyID, java.lang.String p_PartyName, java.lang.String p_Type, java.lang.String p_Status) throws java.rmi.RemoteException;
    public java.lang.String party_SetRegisteredPartyStatus(int p_Key, java.lang.String p_TempExtPartyID, java.lang.String p_ExtPartyID) throws java.rmi.RemoteException;
    public java.lang.String[] category_GetAllCategoriesInText(int p_Key, boolean p_IsPrefixWithHID, java.lang.String p_CategorySeparator) throws java.rmi.RemoteException;
    public java.lang.String reload_Mooga(int p_Key) throws java.rmi.RemoteException;
    public float[] categoryRule_GetRule(int p_Key, java.lang.String p_ExtItemCategoryID, java.lang.String p_ServiceName, java.lang.String p_FeatureName) throws java.rmi.RemoteException;
    public java.lang.String category_RefreshFromDB(int p_Key) throws java.rmi.RemoteException;
    public java.lang.String categoryRule_RefreshFromDB(int p_Key) throws java.rmi.RemoteException;
    public java.lang.String generic_GetPopular(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemCategoryID, java.lang.String p_ServiceNameNCode, java.lang.String p_ExtLocationID) throws java.rmi.RemoteException;
    public java.lang.String generic_GenericPush(int p_Key, java.lang.String p_ExtID, java.lang.String p_ExtCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ServiceNameNCode, java.lang.String p_MoogaBasicServiceType) throws java.rmi.RemoteException;
    public java.lang.String executeService(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtID, java.lang.String p_ExtCategoryID, java.lang.String p_ExtLocationID, java.lang.String p_ExtTimeID, short p_ServiceID) throws java.rmi.RemoteException;
    public java.lang.String executeServiceWithInternalIDs(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ID, java.lang.String p_CategoryID, java.lang.String p_LocationID, java.lang.String p_TimeID, short p_ServiceID) throws java.rmi.RemoteException;
    public java.lang.String replaceIDsWithExtIDs(int p_Key, java.lang.String p_StrWithInternalIDs, java.lang.String p_MoogaIDType, java.lang.String p_MoogaCatIDType, java.lang.String p_ValueSeparator, java.lang.String p_PercSeparator) throws java.rmi.RemoteException;
    public java.lang.String executeWebServiceUsingList(int p_Key, java.lang.String p_WebServiceName, java.lang.Object[] p_Parameters) throws java.rmi.RemoteException;
    public java.lang.String executeAllPendingServiceRequest(int p_Key) throws java.rmi.RemoteException;
    public java.lang.String party_GetPartyItemRelevence(int p_Key, java.lang.String p_ExtPartyID, java.lang.String p_ExtItemID, java.lang.String p_ExtCatID, java.lang.String p_ServiceCode) throws java.rmi.RemoteException;
}
