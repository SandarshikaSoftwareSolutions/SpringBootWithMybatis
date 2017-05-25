package com.practice.sample;

import com.gap.selling.stores.snapintegration.model.transaction.Transaction;
import com.gap.selling.stores.snapintegration.model.transaction.TransactionStatus;
import com.gap.selling.stores.snapintegration.model.vo.StoreData;
import com.gap.selling.stores.snapintegration.model.vo.TransactionType;
import com.gap.selling.stores.snapintegration.service.SnapIntegrationServiceImpl;
import com.gap.selling.stores.snapintegration.shared.configuration.SnapIntegrationEnvironment;
import com.gap.selling.stores.snapintegration.shared.exception.SnapIntegrationException;
import com.gapinc.selling.stores.apps.snapinvoice.rest.service.TransactionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.String.valueOf;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author sjeenor
 */
@Controller
@RequestMapping("/Errors")
public class SearchController {
    private Logger logger = getLogger(getClass());
    private static final String STORE_DETAILS = "storeDetails";

    @Autowired
    private SnapIntegrationEnvironment snapIntegrationEnvironment;
    @Autowired
    @Qualifier("snapIntegrationService")
    private SnapIntegrationServiceImpl snapIntegrationService;
    @Autowired
    private TransactionService transactionService;

    private String recordsPerPage;
    private String pageNo;

    @PostConstruct
    public void init() {
        //TODO
        recordsPerPage = valueOf(snapIntegrationEnvironment.loadConfiguration(snapIntegrationEnvironment.getApps().iterator().next()).getAppConfiguration().getRecordsPerPage());
    }

    public void setRecordsPerPage(String recordsPerPage) {
        this.recordsPerPage = recordsPerPage;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String launchSearch(ModelMap model) {
        StoreData sd = new StoreData();
        model.addAttribute(STORE_DETAILS, sd);

        return "storeSearch";
    }

    @RequestMapping(value = "/searchStoreError", method = RequestMethod.GET)
    public ModelAndView search(
            @ModelAttribute(value = STORE_DETAILS) StoreData store,
            BindingResult result, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        pageNo = request.getParameter("d-149483-p");
        int offset = setOffsetValue(pageNo);
        if (!result.hasErrors()) {
            Transaction transaction = setSalesTransactionValues(
                    store, offset);
            int records = 0;
            List<Transaction> transactionListByLimit = null;
            try {
                records = snapIntegrationService.getErrorTransaction(transaction);
                transactionListByLimit = snapIntegrationService.getErrorTransactions(transaction);
            } catch (SnapIntegrationException se) {
                logger.error("The Controller cannot retrieve data for search... " + store.getStoreNo());
            }
            setValuesToModel(request, model, records, store, transactionListByLimit);
        }
        return new ModelAndView("displayErrorJsonList", model);
    }

    private int setOffsetValue(String pageNum) {
        int offset;
        if (pageNum == null) {
            offset = 0;
            pageNo = "0";
        } else {
            offset = Integer.parseInt(recordsPerPage)
                    * (Integer.parseInt(pageNo) - 1);
        }
        return offset;
    }

    private void setValuesToModel(HttpServletRequest request,
                                  Map<String, Object> model, int records, StoreData store,
                                  List<Transaction> transactionListByLimit) {
        List<StoreData> resultsList = setResultsList(transactionListByLimit, store);
        request.setAttribute("size", records);
        request.setAttribute("pageSize", Integer.parseInt(recordsPerPage));
        model.put("resultsList", resultsList);
    }

    private void setValuesToModel1(HttpServletRequest request,
                                   Map<String, Object> model, int records, StoreData store,
                                   List<Transaction> transactionListByLimit) {
        List<StoreData> resultsList = setResultsList1(transactionListByLimit, store);
        request.setAttribute("size", records);
        request.setAttribute("pageSize", Integer.parseInt(recordsPerPage));
        model.put("resultsList", resultsList);
    }

    private List<StoreData> setResultsList(
            List<Transaction> transactionListByLimit, StoreData store) {
        List<StoreData> resultsList = new ArrayList<StoreData>();
        if (CollectionUtils.isNotEmpty(transactionListByLimit)) {
            for (Transaction st : transactionListByLimit) {
                setResultsData(resultsList, st, store);
            }
        }
        return resultsList;
    }

    private List<StoreData> setResultsList1(
            List<Transaction> transactionListByLimit, StoreData store) {
        List<StoreData> resultsList = new ArrayList<StoreData>();
        if (CollectionUtils.isNotEmpty(transactionListByLimit)) {
            for (Transaction st : transactionListByLimit) {
                setResultsData1(resultsList, st, store);
            }
        }
        return resultsList;
    }

    @RequestMapping(value = "/getStoreErrorDetails", method = RequestMethod.GET)
    public ModelAndView getErrorDetails(@RequestParam String storeNo,
                                        @RequestParam String transId, @RequestParam String searchStore, @RequestParam String searchDate, @RequestParam String moduleName) {
        logger.info("get transPayLoad by" + storeNo + "...and.." + transId);
        List<Transaction> salesTransactionList = null;
        Transaction st = new Transaction();
        st.setStoreNumber(storeNo);
        st.setTransactionId(transId);
        st.setModuleName(moduleName);
        try {
            salesTransactionList = snapIntegrationService.getErrorTransactionPayLoadByTransactionId(st);
        } catch (SnapIntegrationException se) {
            logger.error("The Controller cannot retrieve data....for store.. "
                    + storeNo + "...and.." + transId);
        }
        StoreData sd = new StoreData();
        if (CollectionUtils.isNotEmpty(salesTransactionList)) {
            sd.setStoreNo(salesTransactionList.get(0).getStoreNumber());
            sd.setTxnId(salesTransactionList.get(0).getTransactionId());
            sd.setTxnData(salesTransactionList.get(0).getTransactionPayload());
            sd.setErrorDesc(salesTransactionList.get(0).getErrorDesc());
            sd.setErrorStack(salesTransactionList.get(0).getErrorStackText());
            sd.setTransactionEvents(salesTransactionList.get(0)
                    .getTransactionEvents());
            sd.setSigCapImage(salesTransactionList.get(0).getSigCapImage());
            sd.setTxnCapturedDate(getTransactionDateFormatted(salesTransactionList.get(0)
                    .getTransactionStartDateTime()));
        } else {
            sd.setStoreNo(storeNo);
            sd.setTxnId(transId);
        }
        sd.setSearchStoreNo(searchStore);
        sd.setSearchDate(searchDate);
        sd.setModuleName(moduleName);

        return new ModelAndView("displayErrorJson", "storeDetails", sd);
    }

    @RequestMapping(value = "/updateJson", method = RequestMethod.POST)
    public ModelAndView updateJson(
            @ModelAttribute(value = STORE_DETAILS) StoreData store,
            @RequestParam(required = false) String status,
            BindingResult result, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        pageNo = request.getParameter("d-149483-p");
        int offset = setOffsetValue(pageNo);
        int updateCount = 0;
        if (!result.hasErrors()) {
            Transaction newTransaction = new Transaction();
            setTransactionData(store, newTransaction, status);
            updateCount = updateJsonData(store, newTransaction);
            Transaction transaction = setSalesTransactionValues(store, offset);
            transaction.setStoreNumber(store.getSearchStoreNo());
            transaction.setTransactionDate(store.getSearchDate());
            transaction.setSearchDateRangeFrom(store.getSearchDate());
            transaction.setSearchDateRangeTo(store.getSearchDate());
            String updatedStatus = (StringUtils.equals(TransactionStatus.IGNORE.toString(),status))
                            ? TransactionStatus.IGNORE.toString() : TransactionStatus.REPROC.toString();
            String statusMessage = (updateCount >= 1) ? "Successfully updated status as "+ updatedStatus +" for "
                    + store.getTxnId() : "Failed to update Transaction ID = " + store.getTxnId();
            request.setAttribute("SUCCESS", statusMessage);
            try {
                setValuesToModel1(request, model, snapIntegrationService.getErrorTransaction(transaction),
                        store, snapIntegrationService.getErrorTransactions(transaction));
            } catch (SnapIntegrationException se) {
                logger.error("The Controller cannot retrieve data for search... " + store.getStoreNo());
            }
        }
        return new ModelAndView("displayErrorJsonList", model);
    }

    @RequestMapping(value = "/bulkUpdates", method = RequestMethod.POST)
    public ModelAndView bulkUpdates(@ModelAttribute(value = STORE_DETAILS) StoreData store,
            @RequestParam(required = false) String status, BindingResult result, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        pageNo = request.getParameter("d-149483-p");
        int offset = setOffsetValue(pageNo);
        if (!result.hasErrors()) {
            Transaction newTransaction = new Transaction();
            setTransactionData(store, newTransaction, status);
            int updateCount = snapIntegrationService.bulkTransactionStatusUpdates(store, status);
            String statusMessage = (updateCount >= 1) ? "Successfully updated status as "+status+" for selected Transaction Id's."
                    : "Failed to update status as "+status+" for selected Transaction Id's.";
            request.setAttribute("SUCCESS", statusMessage);
            Transaction transactionData = setSalesTransactionValues(store, offset);
            int records = 0;
            List<Transaction> transactionListByLimit = null;
            try {
                records = snapIntegrationService.getErrorTransaction(transactionData);
                transactionListByLimit = snapIntegrationService.getErrorTransactions(transactionData);
            } catch (SnapIntegrationException se) {
                logger.error("The Controller cannot retrieve data for search... " + store.getStoreNo());
            }
            setValuesToModel(request, model, records, store, transactionListByLimit);
        }
        return new ModelAndView("displayErrorJsonList", model);
    }

    @RequestMapping(value = "/submitAll", method = RequestMethod.POST)
    public ModelAndView submitAllErrorTransactions(@ModelAttribute(value = STORE_DETAILS) StoreData store,
                                                   @RequestParam(required = false) String status,
                                                   BindingResult result, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        pageNo = request.getParameter("d-149483-p");
        int offset = setOffsetValue(pageNo);
        if (!result.hasErrors()) {
            int updateCount = snapIntegrationService.submitAllErrorTransactions(store, status);
            String statusMessage = (updateCount >= 1) ? "Successfully updated status as "+status+" for all Transaction Id's."
                    : "Failed to update status as "+status+" for all Transaction Id's.";
            request.setAttribute("SUCCESS", statusMessage);
            Transaction transactionData = setSalesTransactionValues(store, offset);
            int records = 0;
            List<Transaction> transactionListByLimit = null;
            try {
                records = snapIntegrationService.getErrorTransaction(transactionData);
                transactionListByLimit = snapIntegrationService.getErrorTransactions(transactionData);
            } catch (SnapIntegrationException se) {
                logger.error("The Controller cannot retrieve data for search... " + store.getStoreNo());
            }
            setValuesToModel(request, model, records, store, transactionListByLimit);
        }
        return new ModelAndView("displayErrorJsonList", model);
    }

    /*
     ********************************************************
     ****************** TXPPE_TXN_ERR_T *********************
     ********************************************************
     */

    @RequestMapping(value = "/searchTransactionError", method = RequestMethod.GET)
    public String launchSearchForTxn(ModelMap model) {
        StoreData sd = new StoreData();
        model.addAttribute(STORE_DETAILS, sd);
        return "storeSearchForTxn";
    }

    @RequestMapping(value = "/searchStoreErrorForTxn", method = RequestMethod.GET)
    public ModelAndView searchTransactionError(
            @ModelAttribute(value = STORE_DETAILS) StoreData store,
            BindingResult result, HttpServletRequest request) {

        Map<String, Object> model = new HashMap<String, Object>();
        pageNo = request.getParameter("d-149483-p");
        int offset = setOffsetValue(pageNo);
        if (!result.hasErrors()) {
            Transaction transaction = setSalesTransactionValuesForTransaction(store, offset);
            int records = 0;
            List<Transaction> transactionListByLimit = null;
            try {
                records = snapIntegrationService.getErrorTransactionForTransaction(transaction);
                transactionListByLimit = snapIntegrationService.getErrorTransactionsForTransactions(transaction);
            } catch (SnapIntegrationException se) {
                logger.error("Error occured while retriving data for error search", se);
            }
            setValuesToModelForTxn(request, model, records, store, transactionListByLimit);
        }
        return new ModelAndView("displayErrorJsonListForTxn", model);
    }

    @RequestMapping(value = "/getStoreErrorDetailsForTxn", method = RequestMethod.GET)
    public ModelAndView getErrorDetailsForTxn(@RequestParam String txnUUId, @RequestParam String transId) {

        List<Transaction> salesTransactionList = null;
        Transaction st = new Transaction();
        st.setTransactionUUId(txnUUId);
        st.setTransactionId(transId);
        try {
            salesTransactionList = snapIntegrationService.getErrorTransactionPayLoadByTransactionIdForTxn(st);
        } catch (SnapIntegrationException se) {
            logger.error("Unable to retrieve data for txnUUId: "+ txnUUId + " and transId: "+ transId, se);
        }
        StoreData sd = new StoreData();
        if (CollectionUtils.isNotEmpty(salesTransactionList)) {
            sd = getStoreData(salesTransactionList.get(0));
        } else {
            sd.setTxnUUId(txnUUId);
            sd.setTxnId(transId);
        }
        return new ModelAndView("displayErrorJsonForTxn", "storeDetails", sd);
    }

    @RequestMapping(value = "/updateJsonForTxn", method = RequestMethod.POST)
    public ModelAndView updateJsonForTxn(@ModelAttribute(value = STORE_DETAILS) StoreData store,
             @RequestParam(required = false) String status, BindingResult result, HttpServletRequest request) {

        Map<String, Object> model = new HashMap<String, Object>();
        pageNo = request.getParameter("d-149483-p");
        int offset = setOffsetValue(pageNo);
        if (!result.hasErrors()) {
            Transaction newTransaction = new Transaction();
            setTransactionDataForTxn(store, newTransaction, status);
            StringBuilder statusMessage = new StringBuilder();
            updateJsonDataForTxn(store, newTransaction, statusMessage);
            Transaction transaction = setSalesTransactionValuesForTransaction(store, offset);
            transaction.setSearchDateRangeFrom(store.getSearchDateRangeFrom());
            transaction.setSearchDateRangeTo(store.getSearchDateRangeTo());
            request.setAttribute("SUCCESS", statusMessage.toString());
            try {
                setValuesToModel1(request, model, snapIntegrationService.getErrorTransactionForTransaction(transaction),
                        store, snapIntegrationService.getErrorTransactionsForTransactions(transaction));
            } catch (SnapIntegrationException se) {
                logger.error("The Controller cannot retrieve data for search... " + store.getStoreNo());
            }
        }
        return new ModelAndView("displayErrorJsonListForTxn", model);
    }

    private int updateJsonDataForTxn(StoreData store, Transaction transaction, StringBuilder statusMsg) {
        int count = 0;
        try {
            if(StringUtils.equalsIgnoreCase(TransactionStatus.POSTED.toString(), transaction.getTransactionStatus())) {
                ResponseEntity<String> response = transactionService.callEndpointAndPostTransaction(transaction);
                if ((HttpStatus.NO_CONTENT.value() == response.getStatusCode().value())
                        || (HttpStatus.OK.value() == response.getStatusCode().value())) {
                    transaction.setTransactionStatus(TransactionStatus.POSTED.toString());
                } else {
                    transaction.setTransactionStatus(TransactionStatus.ERROR.toString());
                }
            }
            count = snapIntegrationService.updateReprocessedTransactionForTxn(transaction);
        } catch (SnapIntegrationException se) {
            logger.error("Unable to update json data for transactionId: "+ store.getTxnId());
        }
        if(count >= 1){
            statusMsg.append("Successfully updated status as '").append(transaction.getTransactionStatus())
                    .append("' for Transaction ID = ").append(store.getTxnId());
        } else {
            statusMsg.append("Failed to update Transaction ID = ").append(store.getTxnId());
        }
        return count;
    }

    private Transaction setSalesTransactionValuesForTransaction(StoreData store, int offset) {
        Transaction transaction = new Transaction();
        transaction.setSearchDateRangeFrom(store.getSearchDateRangeFrom());
        transaction.setSearchDateRangeTo(store.getSearchDateRangeTo());
        transaction.setOffset(offset);
        transaction.setRecordsPerPage(Integer.parseInt(recordsPerPage));
        return transaction;
    }

    private void setValuesToModelForTxn(HttpServletRequest request,
                                        Map<String, Object> model, int records, StoreData store,
                                        List<Transaction> transactionListByLimit) {
        List<StoreData> resultsList = setResultsListForTxn(transactionListByLimit, store);
        request.setAttribute("size", records);
        request.setAttribute("pageSize", Integer.parseInt(recordsPerPage));
        model.put("resultsList", resultsList);
    }

    private void setTransactionDataForTxn(StoreData store, Transaction transaction, String status) {
        transaction.setTransactionId(store.getTxnId());
        transaction.setTransactionUUId(store.getTxnUUId());
        transaction.setTransactionPayload(store.getTxnData());
        transaction.setTransactionEvents(store.getTransactionEvents());
        transaction.setTransactionDateTime(store.getTxnCapturedDate());
        if(StringUtils.equalsIgnoreCase(TransactionStatus.POSTED.toString(), status)){
            transaction.setTransactionStatus(TransactionStatus.POSTED.toString());
        } else if(StringUtils.equalsIgnoreCase(TransactionStatus.IGNORE.toString(), status)) {
            transaction.setTransactionStatus(TransactionStatus.IGNORE.toString());
        }
        transaction.setErrorStackText(store.getErrorStack());
        transaction.setErrorDesc(store.getErrorDesc());
        transaction.setErrorType(store.getErrorType());
        if(StringUtils.isNotBlank(store.getTxnType()) && MapUtils.isNotEmpty(TransactionType.lookupType)
                && TransactionType.lookupType.containsKey(store.getTxnType())) {
            transaction.setTransactionType(TransactionType.lookupType.get(store.getTxnType()));
        }
    }

    private List<StoreData> setResultsListForTxn(
            List<Transaction> transactionListByLimit, StoreData store) {
        List<StoreData> resultsList = new ArrayList<StoreData>();
        if (CollectionUtils.isNotEmpty(transactionListByLimit)) {
            for (Transaction st : transactionListByLimit) {
                setResultsDataForTxn(resultsList, st, store);
            }
        }
        return resultsList;
    }

    private void setResultsDataForTxn(List<StoreData> resultsList, Transaction transaction, StoreData store) {
        StoreData data = getStoreData(transaction);
        resultsList.add(data);
    }

    private StoreData getStoreData(Transaction st) {
        StoreData data = new StoreData();
        data.setTxnUUId(st.getTransactionUUId());
        data.setTxnId(st.getTransactionId());
        data.setTxnData(st.getTransactionPayload());
        data.setTxnCapturedDate(st.getTransactionDate());
        data.setTxnPersistedDate(st.getTxnPersistedDate());
        if(null != st.getTransactionType()) {
            data.setTxnType(st.getTransactionType().toString());
        }
        data.setErrorType(st.getErrorType());
        data.setErrorStack(st.getErrorStackText());
        data.setTxnCapturedDate(getTransactionDateFormatted(st.getTransactionStartDateTime()));
        return data;
    }

    private int updateJsonData(StoreData store, Transaction transaction) {
        int count = 0;
        try {
            count = snapIntegrationService.updateReprocessedTransaction(transaction);
        } catch (SnapIntegrationException se) {
            logger.error("The Controller cannot update json data for.... "
                    + store.getStoreNo() + "...transactionid.."
                    + store.getTxnId());
        }
        return count;
    }

    private Transaction setSalesTransactionValues(StoreData store,
                                             int offset) {
        Transaction transaction = new Transaction();
        transaction.setStoreNumber(store.getStoreNo());
        transaction.setModuleName(store.getModuleName());
        transaction.setSearchDateRangeFrom(store.getSearchDateRangeFrom());
        transaction.setSearchDateRangeTo(store.getSearchDateRangeTo());
        transaction.setTransactionStatus(store.getTxnStatus());
        transaction.setTransactionType(store.getTxnType() != null && !store.getTxnType().isEmpty() ? TransactionType.valueOf(store.getTxnType()) : null);
        transaction.setExcludeTestRegisterNumbers(store.getExcludeTestRegisters());
        transaction.setOffset(offset);
        transaction.setRecordsPerPage(Integer.parseInt(recordsPerPage));

        return transaction;
    }

    private void setTransactionData(StoreData store,
                                    Transaction transaction, String status) {
        transaction.setStoreNumber(store.getStoreNo());
        transaction.setModuleName(store.getModuleName());
        transaction.setSigCapImage(store.getSigCapImage());
        transaction.setTransactionId(store.getTxnId());
        transaction.setTransactionPayload(store.getTxnData());// modified
        transaction.setTransactionEvents(store.getTransactionEvents());
            transaction.setTransactionDateTime(store.getTxnCapturedDate());
        if(StringUtils.equals(TransactionStatus.IGNORE.toString(), status)){
            transaction.setTransactionStatus(TransactionStatus.IGNORE.toString());
        }
        else{
            transaction.setTransactionStatus(TransactionStatus.REPROC.toString());
        }
    }

    private void setResultsData(List<StoreData> resultsList, Transaction st, StoreData store) {
        StoreData data = new StoreData();
        data.setStoreNo(st.getStoreNumber());
        data.setTxnId(st.getTransactionId());
        data.setTxnUUId(st.getTransactionUUId());
        data.setTxnData(st.getTransactionPayload());
        data.setTxnCapturedDate(st.getTransactionDate());
        data.setTxnPersistedDate(st.getTxnPersistedDate());
        data.setSearchStoreNo(store.getStoreNo());
        data.setModuleName(store.getModuleName());
        data.setSearchDate(store.getTxnCapturedDate());
        if(null != st.getTransactionType()) {
            data.setTxnType(st.getTransactionType().toString());
        }
        data.setErrorType(st.getErrorType());
        resultsList.add(data);
    }

    private void setResultsData1(List<StoreData> resultsList, Transaction st, StoreData store) {
        StoreData data = new StoreData();
        data.setStoreNo(st.getStoreNumber());
        data.setTxnId(st.getTransactionId());
        data.setTxnUUId(st.getTransactionUUId());
        data.setTxnData(st.getTransactionPayload());
        data.setTxnCapturedDate(st.getTransactionDate());
        data.setTxnPersistedDate(st.getTxnPersistedDate());
        data.setSearchStoreNo(store.getSearchStoreNo());
        data.setSearchDate(store.getSearchDate());
        data.setModuleName(store.getModuleName());
        if(null != st.getTransactionType()) {
            data.setTxnType(st.getTransactionType().toString());
        }
        data.setErrorType(st.getErrorType());
        resultsList.add(data);
    }

    private String getTransactionDateFormatted(Date transactionStartDateTime) {
        if (transactionStartDateTime != null) {
            SimpleDateFormat dfmillisecs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return dfmillisecs.format(transactionStartDateTime);
        }
        return null;
    }

}
