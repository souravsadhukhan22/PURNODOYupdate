package com.geniustechnoindia.purnodaynidhi.others;

public class APILinks {
    private static final String DOMAIN_NAME = "http://purnodayapp.geniustechnoindia.com";
    //private static final String DOMAIN_NAME = "http://192.168.225.20:8087";

    public static String INSERT_OR_UPDATE_MPIN = DOMAIN_NAME + "/InsertOrUpdateMpin?";
    public static String CHECK_MPIN_SET_OR_NOT = DOMAIN_NAME + "/CheckIfMPinIsSetOrNot?memberCode=";
    public static String EASY_PIN_LOGIN = DOMAIN_NAME + "/MemberLoginWithMpin";
    public static String FORGOT_PIN_API_LINK = DOMAIN_NAME + "/ForgetPIN?phone=";
    public static String IS_UPDATE_AVAILABLE = DOMAIN_NAME + "/IsUpdateAvailable?versionName=";

    public static String GET_LOAN_EMI_LATE_FINE = DOMAIN_NAME + "/GetCalculatedLoanEMILateFine?";
    public static String GET_RENEWAL_LATE_FINE = DOMAIN_NAME + "/GetCalulatedRenewalLatefine?";

    public static String CHECK_IF_ANY_RENEWAL_PENDING = DOMAIN_NAME + "/CheckAnyRenewalApprovalPending?policyCode=";
    public static String CHECK_IF_ANY_EMI_PENDING = DOMAIN_NAME + "/CheckAnyEmiApprovalPending?loanCode=";
    public static String GET_ACTIVE_LOAN_CODE_BY_MCODE = DOMAIN_NAME + "/Arranger/GetActiveLoanAccs?mcode=";

    public static String GET_OPERATOR_CODES = DOMAIN_NAME + "/GetOperatorCodes";
    public static String GET_SAVINGS_ACCOUNT_CODE = DOMAIN_NAME + "/MemberSavingsAccountList?M=";
    public static String RECHARGE_MOBILE = DOMAIN_NAME + "/rechargeMobile";
    public static String GET_SB_CURRENT_BALANCE = DOMAIN_NAME + "/GetCurrentBalance?accCode=";
    public static String CHECK_WALLET_EXISTS_OR_NOT = DOMAIN_NAME + "/CheckIfWalletExists";
    public static String OTP_CREATE_WALLET = DOMAIN_NAME + "/ValidateOTPCreateWallet";
    public static String OTP_ADD_BENEFICIARY = DOMAIN_NAME + "/ValidateOTPAddBeneficiary";
    public static String CREATE_WALLET = DOMAIN_NAME + "/CreateWallet";
    public static String GET_BENEFICIARY_LIST = DOMAIN_NAME + "/GetBeneficiaryList";
    public static String ADD_BENEFICIARY_LINK = DOMAIN_NAME + "/AddBeneficiary";
    public static String GET_SAVINGS_ACCOUNT_DETAILS = DOMAIN_NAME + "/GetMemberSavingsAccountSummaryView?";
    public static String REQUEST_MONEY_TRANSFER = DOMAIN_NAME + "/RequestMoneyTransfer";
    public static String MEMBER_UPDATE_PASSWORD_LINK = DOMAIN_NAME + "/ChangePasswordMember";
    public static String GET_MEMBER_LOAN_DETAILS = DOMAIN_NAME + "/getMemberLoanDetails?loanCode=";

    public static String ARRANGER_SET_NEW_PASS=DOMAIN_NAME + "/Arranger/UpdatePassword";

    public static String GET_INVESTMENT_PLAN_DETAILS=DOMAIN_NAME + "/All/GetInvestmentPlanDetails?sname=";

    public static String GET_NEWS_FLASH  = DOMAIN_NAME + "/GetOffersAndNews";

    public static String ARR_MEMBER_DETAILS_UPDATE = DOMAIN_NAME + "/Arranger/UpdateMemberProfile";

    public static String GET_MEMBER_DETAILS_UPDATE = DOMAIN_NAME + "/GetMemberProfileDetailsToUpdate?MCode=";
    public static String UPDATE_MEMBER_DETAILS = DOMAIN_NAME + "/UpdateMemberDetails";

    public static String ARRANGER_GET_LOAN_CODE = DOMAIN_NAME + "/getLoanCodesByNameOrAcc?type=";
    public static String ARRANGER_GET_MEMBER_LIST = DOMAIN_NAME + "/arrangerGetMemberDetails?type=";

    // # # # Member # # #

    // First time login
    public static String CHECK_IF_PASS_CHANGED = DOMAIN_NAME + "/CheckIfPassChangedOrNot?memberCode=";
    public static String SEND_OTP_BY_MEMBER_CODE = DOMAIN_NAME + "/SendOtpByMemberCode?memberCode=";
    public static String SEND_OTP_BY_MEMBER_CODE_AUTO_READ = DOMAIN_NAME + "/SendOtpByMemberCodeAutoRead?memberCode=";
    public static String UPDATE_PASSWORD_ONE_TIME = DOMAIN_NAME + "/UpdatePasswordOneTime";

    // SB to SB
    public static String GET_SAVINGS_ACCOUNT_SUM_BY_ACCOUNT_CODE = DOMAIN_NAME + "/GetSavingsAccountSummeryByAccCode?accountCode=";
    public static String SEND_MONEY_TO_SERVER = DOMAIN_NAME + "/InsertSbToSb";
    public static String GET_SAVINGS_ACCOUNT_INFO_BY_MEMBER_CODE = DOMAIN_NAME + "/GetSavingsAccountCodes?memberCode=";

    // Loan payment
    public static String GET_SB_BALANCE = DOMAIN_NAME + "/getSBCodesWithBalance?mcode=";
    public static String GET_LOAN_POLICY_ACC_CODE = DOMAIN_NAME + "/getCodesByMemberCode?type=";

    // Investment calculator
    public static String GET_PLAN_NAME_CODE = DOMAIN_NAME + "/GetPlanNameAndCode";
    public static String GET_PLAN_TABLE = DOMAIN_NAME + "/GetPlanTable?stable=";
    public static String GET_MATURITY_DATE = DOMAIN_NAME + "/GetMaturityDate?";

    // # # # Admin section # # #
    public static String ADMIN_LOGIN = DOMAIN_NAME + "/admin/login";
    public static String ADMIN_MOBILE_COLLECTION = DOMAIN_NAME + "/admin/getCollectionReport?code=";
    public static String GET_ADMIN_CASH_BOOK_DETAILS = DOMAIN_NAME + "/admin/GetBankBookUserWise?UserName=";
    public static String GET_ADMIN_CASH_SHEET_DETAILS = DOMAIN_NAME + "/admin/GetCashsheet?OfficeID=";
    public static String GET_ADMIN_ACC_USER_WISE = DOMAIN_NAME + "/admin/GetDepAccUserWise?UserName=";
    public static String GET_ADMIN_BANK_TRANS_REPORT = DOMAIN_NAME + "/admin/BankTransactionReport?OfficeId=";
    public static String GET_OFC_LIST = DOMAIN_NAME + "/admin/GetOfficeList";
    public static String ARRANGER_TEAM_CHAIN_DETAILS = DOMAIN_NAME + "/admin/GetArrangerTeamChain?searchType=";
    public static String ADMIN_EXECUTIVE_COLLECTION_REPORT = DOMAIN_NAME + "/admin/get/ExecutiveCollectionReport?acode=";
    public static String GET_ADMIN_LOAN_DETAILS_BY_OFC_CODE = DOMAIN_NAME + "/admin/LoanPaymentReport?SearchBy=";
    public static String GET_MEMBER_DETAILS = DOMAIN_NAME + "/admin/GetMemberDetailsByPhOrName?";
    public static String ARRANGER_NEW_RENEW_BUSINESS_DETAILS = DOMAIN_NAME + "/admin/GetArrangerRenewAndNewBusiness?officeId=";


    // Recharge Daddy
    public static String GET_OPERATOR_CODES_NEW = DOMAIN_NAME + "/GetOperatorCodesNew?type=";
    public static String PAY_BILL = DOMAIN_NAME + "/UtilityBillPayment";
    public static String FETCH_BILL = DOMAIN_NAME + "/UtilityBillInfo";

    
    public static String GET_MEMBER_POLICY_LIST = DOMAIN_NAME + "/getMemberPolicyList?memberCode=";
    public static String GET_MEMBER_LOAN_LIST = DOMAIN_NAME + "/getMemberLoanList?memberCode=";

    // create policy
    public static String GET_RELATION = DOMAIN_NAME + "/getRelation";
    public static String CREATE_POLICY = DOMAIN_NAME + "/InsertUpdatePolicy";
    public static String GET_MEMBER_DETAILS_POLICY_CREATE = DOMAIN_NAME + "/GetMemberDetails?mcode=";
    public static String GET_MEMBER_DETAILS_BY_MCODE = DOMAIN_NAME + "/GetMemberDetails?mcode=";
    public static String MEMBER_APPLY_FOR_LOAN = DOMAIN_NAME + "/memberRequestForLoan";

    public static String GET_VALUE_LOAN_TABLE_MASTER=DOMAIN_NAME + "/getValueLoanTableMaster";

    public static String INSERT_MANUAL_POLICY_COLL = DOMAIN_NAME + "/insertPolicyAmountManual";
    public static String INSERT_MANUAL_LOANEMI_COLL = DOMAIN_NAME + "/insertloanEMIAmountManual";
    public static String GET_ARR_PROFILE_DETAILS = DOMAIN_NAME + "/getArrProfileDetails?arrCode=";
    public static String GET_POLICY_LIST_BY_NAME = DOMAIN_NAME + "/getPolicyCodeByName?name=";
    // member policy pay
    //public static String GET_MEMBER_POLICY_CODES = DOMAIN_NAME + "/member/getPolicyCodes?mcode=";
    public static String GET_PAY_U_HASH = DOMAIN_NAME + "/payu/payugetkey.php";

    // money trans new
    public static String GET_CUSTOMER_BY_MOB_NO = DOMAIN_NAME + "/GetCustomer?mobileNo=";
    public static String ADD_CUSTOMER = DOMAIN_NAME + "/AddCustomer";
    public static String VALIDATE_CUS_CREATE_OTP = DOMAIN_NAME + "/ValidateCustomerOTP?mobileNo=";
    public static String GET_BANK_LIST = DOMAIN_NAME + "/GetBankList";
    public static String ADD_BENIFICIARY = DOMAIN_NAME + "/AddBeneficiaryNew";
    public static String ADD_BENIFICIARY_VALIDATE_WITH_OTP = DOMAIN_NAME + "/ValidateBeneficiaryOTP?customerNo=";
    public static String GET_ALL_BENIFICIARY = DOMAIN_NAME + "/GetAllBeneficiary?mobileNo=";
    public static String DELETE_BENIFICIARY = DOMAIN_NAME + "/DeleteBeneficiary?customerNo=";
    public static String MONEY_TRANS_PROCESS = DOMAIN_NAME + "/SendMoneyNew";
    public static String GET_MEM_MOBILE = DOMAIN_NAME + "/getMemberPh?mCode=";
    public static String GET_SAVINGS_ACCOUNT_INFO_BY_MEMBER_CODE_NEW = DOMAIN_NAME + "/GetSavingsAccountCodesNew?mCode=";


    public static String GEt_POLICY_BY_PCODE_NAME = DOMAIN_NAME + "/getPolicyByCodeOrName?policyCode=";
    public static String GEt_SB_BY_SB_NAME = DOMAIN_NAME + "/getSBByCodeOrName?sbCode=";
    public static String GET_MEMBER_PIC_BY_MEMBER_CODE = DOMAIN_NAME + "/getMemberPicByMCode?memberCode=";

    //public static String SAVINGS_DEPOSIT_CASH_TO_SB = DOMAIN_NAME + "/CashToSBTemp";
    public static String SAVINGS_DEPOSIT_CASH_TO_SB = DOMAIN_NAME + "/CashToSB";
    public static String GET_SAVINGS_LEDGER_DETAILS = DOMAIN_NAME + "/GetSavingsLedgerArranger?sbCode=";

    public static String GET_SAVINGS_SCHEME_MASTER = DOMAIN_NAME + "/GetSavingsSchemeMaster";

    public static String GET_COMPANY_DETAILS = DOMAIN_NAME + "/GetCompanyDetails";

    // account open
    /*public static String GET_PLAN_NAME_CODE = DOMAIN_NAME + "/GetPlanNameAndCode";
    public static String GET_PLAN_TABLE = DOMAIN_NAME + "/GetPlanTable?stable=";
    public static String GET_RELATION = DOMAIN_NAME + "/getRelation";
    public static String CREATE_POLICY = DOMAIN_NAME + "/InsertUpdatePolicy";
    public static String GET_MEMBER_DETAILS_POLICY_CREATE = DOMAIN_NAME + "/GetMemberDetails?mcode=";
    public static String GET_MATURITY_DATE = DOMAIN_NAME + "/GetMaturityDate?";*/

    public static String GET_ACTIVE_SB_ACCOUNTS = DOMAIN_NAME + "/Arranger/GetActiveSBAccs?mcode=";
    public static String GET_ACTIVE_LOAN_ACCOUNTS = DOMAIN_NAME + "/Arranger/GetActiveLoanAccs?mcode=";
    public static String GET_ACTIVE_POLICY_ACCOUNTS = DOMAIN_NAME + "/Arranger/GetActivePolicyAccs?mcode=";
    public static String GET_ACTIVE_MEMBER=DOMAIN_NAME+"/GetTotalActiveMember";
    public static String GET_ACTIVE_POLICY=DOMAIN_NAME+"/GetTotalActivePolicy";

    public static String GET_INTRO_NAME = DOMAIN_NAME + "/GetIntroName?introCode=";

    public static String IS_MEMBER_BLOCKED = DOMAIN_NAME + "/Arranger/checkIfmemberBlock?mcode=";

    // Arranger wallet
    public static String GET_ARRANGER_WALLET_BALANCE = DOMAIN_NAME + "/GetArrangerWalletbalance?aCode=";

    public static String GEt_LOAN_BY_PCODE_NAME = DOMAIN_NAME + "/getLoanByCodeOrName?loanCode=";
}
