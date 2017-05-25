package com.practice.sample;

import com.gap.selling.stores.snapintegration.model.transaction.Transaction;
import com.gap.selling.stores.snapintegration.model.transaction.TransactionStatus;
import com.gap.selling.stores.snapintegration.service.SnapIntegrationServiceImpl;
import com.gap.selling.stores.snapintegration.shared.configuration.SnapIntegrationEnvironment;
import com.gap.selling.stores.snapintegration.shared.exception.SnapIntegrationException;
import com.gapinc.selling.stores.apps.snapinvoice.controllers.SearchController;
import com.gap.selling.stores.snapintegration.model.vo.StoreData;
import com.gapinc.selling.stores.apps.snapinvoice.rest.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SnapIntegrationServiceImpl.class)
public class SearchControllerTest {

    @InjectMocks
    private SearchController searchController;
    @Mock
    private SnapIntegrationEnvironment snapIntegrationEnvironment;
    private SnapIntegrationServiceImpl snapIntegrationService;
    @Mock
    private Transaction transaction;
    @Mock
    private TransactionService transactionService;
    private Transaction salesTransaction;
    @Mock
    private BindingResult result;
    @Mock
    private HttpServletRequest request;

    @Before
    public void setup() {
        salesTransaction = new Transaction();
        searchController.setRecordsPerPage("25");
        MockitoAnnotations.initMocks(this);
        snapIntegrationService = PowerMockito.mock(SnapIntegrationServiceImpl.class);
        ReflectionTestUtils.setField(searchController, "snapIntegrationService", snapIntegrationService);
        ReflectionTestUtils.setField(searchController, "transactionService", transactionService);
        ReflectionTestUtils.setField(searchController, "snapIntegrationEnvironment", snapIntegrationEnvironment);
    }

    @Test
    public void launchSearchTest() {
        final String viewId = searchController.launchSearch(new ModelMap());
        final String expectedViewId = "storeSearch";
        assertEquals("View Ids are matching", viewId, expectedViewId);

    }

    @Test
    public void searchTest() throws SnapIntegrationException {
        when(snapIntegrationService.getErrorTransaction(any(Transaction.class))).thenReturn(0);
        when(snapIntegrationService.getErrorTransactions(any(Transaction.class))).thenReturn(asList(new Transaction()));


        ModelAndView viewId = searchController.search(new StoreData(), result, request);

        assertEquals(snapIntegrationService.getErrorTransaction(salesTransaction), 0);
        assertEquals("View Ids are matching", viewId.getViewName(), "displayErrorJsonList");
    }

    @Test
    public void searchTestException() throws Exception {
        doThrow(new SnapIntegrationException("search failed"))
                .when(snapIntegrationService).getErrorTransaction(any(Transaction.class));

        searchController.search(new StoreData(), result, request);
    }

    @Test
    public void getErrorDetailsTest() throws SnapIntegrationException {
        final String expectedViewId = "displayErrorJson";

        when(snapIntegrationService.getErrorTransactionPayLoadByTransactionId(any(Transaction.class)))
                .thenReturn(asList(new Transaction()));

        ModelAndView viewId = searchController.getErrorDetails("08028", "1234567890", "", "", "");

        assertEquals("View Ids are matching", viewId.getViewName(), expectedViewId);
    }

    @Test
    public void getErrorDetailsTestException() throws Exception {
        doThrow(new SnapIntegrationException("get failed"))
                .when(snapIntegrationService).getErrorTransactionPayLoadByTransactionId(any(Transaction.class));

        searchController.getErrorDetails("08028", "1234567890", "", "", "");
    }


    @Test
    public void updateJsonForIgnoreTest() throws Exception {
        final String expectedViewId = "displayErrorJsonList";
        when(snapIntegrationService.getErrorTransaction(new Transaction())).thenReturn(0);
        when(snapIntegrationService.getErrorTransactions(any(Transaction.class))).thenReturn(asList(new Transaction()));

        ModelAndView viewId = searchController.updateJson(new StoreData(), TransactionStatus.IGNORE.toString(), result, request);

        assertEquals("View Ids are matching", viewId.getViewName(), expectedViewId);
    }

    @Test
    public void updateJsonTest() throws Exception {
        final String expectedViewId = "displayErrorJsonList";
        when(snapIntegrationService.getErrorTransaction(new Transaction())).thenReturn(0);
        when(snapIntegrationService.getErrorTransactions(any(Transaction.class))).thenReturn(asList(new Transaction()));

        ModelAndView viewId = searchController.updateJson(new StoreData(), null, result, request);

        assertEquals("View Ids are matching", viewId.getViewName(), expectedViewId);
    }

    @Test
    public void updateJsonTestException() throws Exception {
        final String expectedViewId = "searchResults";
        doThrow(new SnapIntegrationException("get failed")).when(
                snapIntegrationService).getErrorTransactionPayLoadByTransactionId(
                any(Transaction.class));
        searchController.getErrorDetails("08028", "1234567890", "", "","");
    }

    //Test cases for Transaction error

    @Test
    public void launchSearchForTxnTest(){
        final String viewId = searchController.launchSearchForTxn(new ModelMap());
        final String expectedViewId = "storeSearchForTxn";
        assertEquals("View Ids are matching", viewId, expectedViewId);
    }

    @Test
    public void searchTransactionErrorTest() throws SnapIntegrationException {
        when(snapIntegrationService.getErrorTransactionForTransaction(new Transaction())).thenReturn(0);
        when(snapIntegrationService.getErrorTransactionsForTransactions(any(Transaction.class))).thenReturn(asList(new Transaction()));
        ModelAndView viewId = searchController.searchTransactionError(new StoreData(), result, request);
        assertEquals(snapIntegrationService.getErrorTransactionForTransaction(salesTransaction), 0);
        assertEquals("View Ids are matching", viewId.getViewName(), "displayErrorJsonListForTxn");
    }

    @Test
    public void searchTransactionErrorTestException(){
        doThrow(new SnapIntegrationException("Search failed"))
                .when(snapIntegrationService).getErrorTransactionForTransaction(any(Transaction.class));
        searchController.searchTransactionError(new StoreData(), result, request);
    }

    @Test
    public void getErrorDetailsTestForTxn() throws SnapIntegrationException {
        final String expectedViewId = "displayErrorJsonForTxn";

        when(snapIntegrationService.getErrorTransactionPayLoadByTransactionIdForTxn(any(Transaction.class)))
                .thenReturn(asList(new Transaction()));

        ModelAndView viewId = searchController.getErrorDetailsForTxn("08028", "1234567890");

        assertEquals("View Ids are matching", viewId.getViewName(), expectedViewId);
    }

    @Test
    public void getErrorDetailsTestExceptionForTxn() throws Exception {
        doThrow(new SnapIntegrationException("get failed"))
                .when(snapIntegrationService).getErrorTransactionPayLoadByTransactionIdForTxn(any(Transaction.class));

        searchController.getErrorDetailsForTxn("08028", "1234567890");
    }

    @Test
    public void updateJsonTestForTxn() throws Exception {
        final String expectedViewId = "displayErrorJsonListForTxn";
        when(snapIntegrationService.getErrorTransactionForTransaction(new Transaction())).thenReturn(0);
        when(snapIntegrationService.getErrorTransactionsForTransactions(any(Transaction.class))).thenReturn(asList(new Transaction()));
        when(transactionService.callEndpointAndPostTransaction(any(Transaction.class))).thenReturn(new ResponseEntity<String>(HttpStatus.OK));
        ModelAndView viewId = searchController.updateJsonForTxn(new StoreData(),"POSTED",result, request);
        assertEquals("View Ids are matching", viewId.getViewName(), expectedViewId);
    }

    @Test
    public void updateJsonTestExceptionForTxn() throws Exception {
        final String expectedViewId = "searchResultsForTxn";
        doThrow(new SnapIntegrationException("get failed")).when(
                snapIntegrationService).getErrorTransactionPayLoadByTransactionIdForTxn(
                any(Transaction.class));
        searchController.getErrorDetailsForTxn("08028", "1234567890");
    }

    @Test
    public void bulkUpdatesSuccessTest(){
        final String expectedViewId = "displayErrorJsonList";
        doReturn(1).when(snapIntegrationService).bulkTransactionStatusUpdates(any(StoreData.class),anyString());
        doReturn(0).when(snapIntegrationService).getErrorTransaction(any(Transaction.class));
        doReturn(asList(new Transaction())).when(snapIntegrationService).getErrorTransactions(any(Transaction.class));
        ModelAndView viewId = searchController.bulkUpdates(new StoreData(), TransactionStatus.REPROC.toString(), result, request);
        assertEquals("View Ids are matching", viewId.getViewName(), expectedViewId);
    }

    @Test
    public void bulkUpdatesExceptionTest(){
        doReturn(1).when(snapIntegrationService).bulkTransactionStatusUpdates(any(StoreData.class),anyString());
        doThrow(new SnapIntegrationException("Bulk Update Failed")).when(snapIntegrationService).getErrorTransaction(any(Transaction.class));
        searchController.bulkUpdates(new StoreData(), TransactionStatus.REPROC.toString(), result, request);
    }

    @Test
    public void submitAllErrorTransactionsSuccessTest(){
        final String expectedViewId = "displayErrorJsonList";
        doReturn(1).when(snapIntegrationService).submitAllErrorTransactions(any(StoreData.class),anyString());
        doReturn(0).when(snapIntegrationService).getErrorTransaction(any(Transaction.class));
        doReturn(asList(new Transaction())).when(snapIntegrationService).getErrorTransactions(any(Transaction.class));
        ModelAndView viewId = searchController.submitAllErrorTransactions(new StoreData(),TransactionStatus.NEW.toString(), result, request);
        assertEquals("View Ids are matching", viewId.getViewName(), expectedViewId);
    }

    @Test
    public void submitAllErrorTransactionsExceptionTest(){
        doReturn(1).when(snapIntegrationService).submitAllErrorTransactions(any(StoreData.class),anyString());
        doThrow(new SnapIntegrationException("Submitting All Error Transactions Failed")).when(snapIntegrationService).getErrorTransaction(any(Transaction.class));
        searchController.submitAllErrorTransactions(new StoreData(),TransactionStatus.NEW.toString(), result, request);
    }

}
