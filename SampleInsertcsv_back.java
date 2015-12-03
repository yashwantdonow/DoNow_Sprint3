package com.test.azuredump;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import org.json.JSONException;
import org.json.JSONObject;

import com.microsoft.azure.storage.StorageException;
import com.test.DumpDataToBlob;
import com.test.DumpDataToBlob.*;

public class SampleInsertcsv_back {
 /* --------- Login Credentials ---------- */
 private static String userName;
 private static String password;
 
 private static String OAUTH_ENDPOINT = "/services/oauth2/token";
 private static String REST_ENDPOINT = "/services/data";
 
 String baseUri;
 int Lead_Count ;
 int Opportunity_Count ;
 int Account_Count ;
 String Account_DB ="Acount";
 String Opportunity_DB ="Opportunity";
 String Lead_DB ="Lead";
 
 Header oauthHeader;
 
 Header prettyPrintHeader = new BasicHeader("X-PrettyPrint", "1");
 
 /* Id of inserted Employee */
 private static String employeeId;

  private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

  /* Main Method */
 public static void main(String[] args) {
  new SampleInsertcsv_back();
 }

  /* Constructor */
 public SampleInsertcsv_back() {
  getLoginCredentials();
  this.oauth2Login();
 // this.insertEmployee();
   this.retrieveRecordsLead();
  // this.retrieveRecordsOpportunity();
  // this.retrieveRecordsAccount();
 }

  /* Authentication using oauth2 */
 public HttpResponse oauth2Login() {
  System.out.println("Logging in... Kindly Wait...");
  OAuth2Response oauth2Response = null;
  HttpResponse response = null;
  UserCredentials userCredentials = new UserCredentials();
  String loginHostUri = "https://" + 
   userCredentials.loginInstanceDomain + OAUTH_ENDPOINT;
  
  try {
   HttpClient httpClient = new DefaultHttpClient();
   HttpPost httpPost = new HttpPost(loginHostUri);
   StringBuffer requestBodyText = new StringBuffer("grant_type=password");
   requestBodyText.append("&username=");
   requestBodyText.append("DoNow_dev2@brillio.com");
   requestBodyText.append("&password=");
   requestBodyText.append("donow@dev2k9n6IQFyBYvDeGM2g0nVmFHpg");
   requestBodyText.append("&client_id=");
   requestBodyText.append(userCredentials.consumerKey);
   requestBodyText.append("&client_secret=");
   requestBodyText.append(userCredentials.consumerSecret);
   StringEntity requestBody = new StringEntity(requestBodyText.toString());
   requestBody.setContentType("application/x-www-form-urlencoded");
   httpPost.setEntity(requestBody);
   httpPost.addHeader(prettyPrintHeader);
   
   response = httpClient.execute(httpPost);
    
   if (  response.getStatusLine().getStatusCode() == 200 ) {
    String response_string = EntityUtils.toString(response.getEntity());
    try {
     JSONObject json = new JSONObject(response_string);
     oauth2Response = new OAuth2Response(json);
     System.out.println("JSON returned by response: +\n" + json.toString(1));
    } catch (JSONException je) {
     je.printStackTrace();
    }  
    baseUri = oauth2Response.instance_url + REST_ENDPOINT + "/v" + userCredentials.apiVersion +".0";
    oauthHeader = new BasicHeader("Authorization", "OAuth " + oauth2Response.access_token);
    System.out.println("\nSuccessfully logged in to instance: " + baseUri);
   } else {
    System.out.println("An error has occured. Http status: " + response.getStatusLine().getStatusCode());
    System.out.println(getBody(response.getEntity().getContent()));
    System.exit(-1);
   }
  } catch (UnsupportedEncodingException uee) {
   uee.printStackTrace();
  } catch (IOException ioe) {
   ioe.printStackTrace();
  } catch (NullPointerException npe) {
   npe.printStackTrace();
  }
  return response;
 }
 
 
 
 
 
 /* Method to insert Employee */
/* public void insertEmployee() {
  String uri = baseUri + "/sobjects/Lead/";
  try {
   String First_name;
   String Last_name;
   String Email ;
   String Company;
   JSONObject employee = new JSONObject();
   First_name = getUserInput("Enter the first name: ");
   Last_name = getUserInput("Enter the last name: ");
   Email = getUserInput("Enter Email ID");
   Company = getUserInput("Enter Company"); 
   employee.put("FirstName", First_name);
   employee.put("LastName", Last_name);  
   employee.put("Email", Email);
   employee.put("Company", Company); 
   System.out.println("\nEmployee record to be inserted:\n" + employee.toString(1));

    DefaultHttpClient httpClient = new DefaultHttpClient();
   HttpPost httpPost = new HttpPost(uri);
   httpPost.addHeader(oauthHeader);
   httpPost.addHeader(prettyPrintHeader);
   StringEntity body = new StringEntity(employee.toString(1));
   body.setContentType("application/json");
   httpPost.setEntity(body);

    HttpResponse response = httpClient.execute(httpPost);

    int statusCode = response.getStatusLine().getStatusCode();
   if (statusCode == 201) {
    String response_string = EntityUtils.toString(response.getEntity());
    JSONObject json = new JSONObject(response_string);
    employeeId = json.getString("id");
    System.out.println("New Employee id from response: " + employeeId);        
   } else {
    System.out.println("Insertion unsuccessful. Status code returned is " + statusCode);
   }
  } catch (JSONException e) {
   System.out.println("Issue creating JSON or processing results");
   e.printStackTrace();
  } catch (IOException ioe) {
   ioe.printStackTrace();
  } catch (NullPointerException npe) {
   npe.printStackTrace();
  }
 }
 */

 //Read Records SFDC
 public void retrieveRecordsLead() {
	  System.out.println("\nQuerying Example\n");
	  try {
		  
			   System.out.println("Gets the records counts for Lead table");
			   String  queryStr_ld_cnt = "/query?q=select+count(Id)+from+Lead";		
			   System.out.println("\nThe query is " + queryStr_ld_cnt);
			   HttpClient httpClient_ld_cnt = new DefaultHttpClient();
			   String uri_ld_cnt = baseUri + queryStr_ld_cnt;
			   System.out.println("\nQuery URL: " + uri_ld_cnt + "\n");
			   HttpGet httpGet_ld_cnt = new HttpGet(uri_ld_cnt);
			   httpGet_ld_cnt.addHeader(oauthHeader);
			   httpGet_ld_cnt.addHeader(prettyPrintHeader);
			   HttpResponse response_ld_cnt = httpClient_ld_cnt.execute(httpGet_ld_cnt);
			   
			  /* System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			   
			   System.out.println(response_ld_cnt);
			   
			   System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			   */
			   String response_string_ld_cnt = EntityUtils.toString(response_ld_cnt.getEntity());
			   System.out.println(" Total number of records available inside Lead table is :" + response_string_ld_cnt);
			   
			   try {
				   
			   JSONObject json_ld_cnt = new JSONObject(response_string_ld_cnt);
			   
			   Lead_Count = json_ld_cnt.getJSONArray("records").getJSONObject(0).getInt("expr0") ;
			  
			   System.out.println(json_ld_cnt.getJSONArray("records").getJSONObject(0).getInt("expr0"));
			      
			   }
			   
			   catch (JSONException je) {
				     je.printStackTrace();
				    }   
			   System.out.println("Records counts check complete");
		  
		
		  
		  
      // String  queryStr_ld = "/query?q=SELECT+Id,FirstName,LastName,Company,Email+FROM+Lead";
	  // String  queryStr_ld = "/query?q=select+FirstName,LastName,Name,LeadSource,Status,Industry,AnnualRevenue,NumberOfEmployees,CreatedDate,LastModifiedDate,CreatedById+from+Lead";	   
	   
	 //  String  queryStr_ld ="/query?q=select+FirstName,LastName,Name,LeadSource,Status,Industry,AnnualRevenue,NumberOfEmployees,CreatedDate,LastModifiedDate,LastModifiedById,LastActivityDate,LastViewedDate,LastReferencedDate,CleanStatus,DandbCompanyId,EmailBouncedReason,EmailBouncedDate+from+Lead";
	  
	  String  queryStr_ld ="/query?q=select+Id,IsDeleted,MasterRecordId,LastName,FirstName,Salutation,Name,Title,Company,Street,City,State,PostalCode,Country,Latitude,Longitude,Address,Phone,MobilePhone,Fax,Email,Website,PhotoUrl,Description,LeadSource,Status,Industry,Rating,AnnualRevenue,NumberOfEmployees,OwnerId,IsConverted,ConvertedDate,ConvertedAccountId,ConvertedContactId,ConvertedOpportunityId,IsUnreadByOwner,CreatedDate,CreatedById,LastModifiedDate,LastModifiedById,SystemModstamp,LastActivityDate,LastViewedDate,LastReferencedDate,Jigsaw,JigsawContactId,CleanStatus,CompanyDunsNumber,DandbCompanyId,EmailBouncedReason,EmailBouncedDate,SICCode__c,ProductInterest__c,Primary__c,CurrentGenerators__c,NumberofLocations__c+from+Lead";
	   
	   System.out.println("\nThe query is " + queryStr_ld);
	   HttpClient httpClient_ld = new DefaultHttpClient();
	   String uri_ld = baseUri + queryStr_ld;
	   System.out.println("\nQuery URL: " + uri_ld + "\n");
	   HttpGet httpGet_ld = new HttpGet(uri_ld);
	   
	   
	   httpGet_ld.addHeader(oauthHeader);
	   httpGet_ld.addHeader(prettyPrintHeader);
	   
	   HttpResponse response_ld = httpClient_ld.execute(httpGet_ld);
	   
	   
	   
	   int statusCode_ld = response_ld.getStatusLine().getStatusCode();
	   System.out.println("\nStatus code returned is " + statusCode_ld + "\n\n");
	   if (statusCode_ld == 200) {
	    String response_string_ld = EntityUtils.toString(response_ld.getEntity());
	    try {
	     JSONObject json_ld = new JSONObject(response_string_ld);
	     
	        System.out.println("+++++++++++++ Before Json +++++++++++++++++++");
		     
		     System.out.println(response_string_ld);
		     
		 
		     
	/*	     System.out.println("+++++++++++++ After Json ++++++++++++++++++++");
		     
		     
		     System.out.println("+++++++++++++ Test Start +++++++++++++++++");
		     
		     System.out.println(json_ld);
		     
		     System.out.println("++++++++++++++ Test End +++++++++++++++");
	     
	*/     
	     
	     System.out.println("Id\t\t\t\tName");
//	     for(Integer i = 0; i < json.length() - 1; i++){
	    	 for(Integer i = 0; i < Lead_Count - 1; i++){
	    
	     // Dummy one  		 
	     /* System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("Id") + ", ");
	      System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("FirstName") + ", ");
	      System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("LastName") + ", ");
	      System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("Company") + ", ");
	      System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("Email"));
	      System.out.println();*/
	     
	    		 List<String> students = new ArrayList<String>();
	    		 
 	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("Id") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getBoolean("IsDeleted") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("MasterRecordId") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("LastName") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("FirstName") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Salutation") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("Name") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Title") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("Company") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Street") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("City") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("State") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("PostalCode") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Country") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Latitude") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Longitude") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Address") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Phone") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("MobilePhone") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Fax") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("Email") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Website") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("PhotoUrl") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Description") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("LeadSource") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Status") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Industry") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Rating") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("AnnualRevenue") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("NumberOfEmployees") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("OwnerId") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getBoolean("IsConverted") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("ConvertedDate") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("ConvertedAccountId") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("ConvertedContactId") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("ConvertedOpportunityId") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("IsUnreadByOwner") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("CreatedDate") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("CreatedById") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("LastModifiedDate") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("LastModifiedById") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("SystemModstamp") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("LastActivityDate") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("LastViewedDate") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("LastReferencedDate") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Jigsaw") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("JigsawContactId") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).getString("CleanStatus") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("CompanyDunsNumber") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("DandbCompanyId") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("EmailBouncedReason") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("EmailBouncedDate") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("SICCode__c") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("ProductInterest__c") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("Primary__c") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("CurrentGenerators__c") + ", ");
	    		 System.out.print(json_ld.getJSONArray("records").getJSONObject(i).get("NumberofLocations__c") + ", "); 
	    		 System.out.println(); 
	     
	    	 
	    		String  FILE_HEADER = "Hotel_Name,Numbers_of_Reviews,Star_Rating,Hotel_Address";
	    		 
	    		    try {
	   			     DumpData.Azure_DB(Lead_DB, FILE_HEADER);
	   			     
	   			     System.out.println(" Enter inside try Block");
	   			     }
	   			     catch (Exception Aex) {
	   				     Aex.printStackTrace();
	   				    }
	   		     
	    	 
	    	 
	    	 }
	     System.out.println("\nEnd of the execution");
	    } catch (JSONException je) {
	     je.printStackTrace();
	    }           
	   } else {
	    System.out.println("Query was unsuccessful. Status code returned is " + statusCode_ld);
	   }
	  } catch (IOException ioe) {
	   ioe.printStackTrace();
	  } catch (NullPointerException npe) {
	   npe.printStackTrace();
	  }
	 }


 
 /*public void retrieveRecordsOpportunity() {
	  System.out.println("\nQuerying Example\n");
	  try {
//	String  queryStr = getUserInput("Enter the query: ");
	  
	System.out.println("Gets the records counts for Opportunity table");
	   String  queryStr_op_cnt = "/query?q=select+count(Id)+from+Opportunity";		
	   System.out.println("\nThe query is " + queryStr_op_cnt);
	   HttpClient httpClient_op_cnt = new DefaultHttpClient();
	   String uri_op_cnt = baseUri + queryStr_op_cnt;
	   System.out.println("\nQuery URL: " + uri_op_cnt + "\n");
	   HttpGet httpGet_op_cnt = new HttpGet(uri_op_cnt);
	   httpGet_op_cnt.addHeader(oauthHeader);
	   httpGet_op_cnt.addHeader(prettyPrintHeader);
	   HttpResponse responseac_op_cnt = httpClient_op_cnt.execute(httpGet_op_cnt);
	   String response_string_op_cnt = EntityUtils.toString(responseac_op_cnt.getEntity());
	   System.out.println(" Total number of records available inside opportunity table is :" + response_string_op_cnt);
	   
	   try {
	   JSONObject json_op_cnt = new JSONObject(response_string_op_cnt);
	   
	   Opportunity_Count = json_op_cnt.getJSONArray("records").getJSONObject(0).getInt("expr0") ;
	   System.out.println(json_op_cnt.getJSONArray("records").getJSONObject(0).getInt("expr0"));
	      
	   }
	   
	   catch (JSONException je) {
		     je.printStackTrace();
		    }   
	   System.out.println("Records counts check complete");
	   
 	  // String  queryStr_op = "/query?q=select+Id,Name,StageName,Amount,Probability,CloseDate+from+Opportunity";	
	   
	   String  queryStr_op = "/query?q=select+Id,IsDeleted,AccountId,IsPrivate,Name,StageName,Amount,Probability,ExpectedRevenue,CloseDate,TotalOpportunityQuantity,Type,NextStep,LeadSource,IsClosed,IsWon,ForecastCategory,ForecastCategoryName,CampaignId,HasOpportunityLineItem,Pricebook2Id,OwnerId,CreatedDate,CreatedById,LastModifiedDate,LastModifiedById,SystemModstamp,LastActivityDate,FiscalQuarter,FiscalYear,Fiscal,LastViewedDate,LastReferencedDate,DeliveryInstallationStatus__c,TrackingNumber__c,OrderNumber__c,CurrentGenerators__c,MainCompetitors__c+from+Opportunity";

	   System.out.println("\nThe query is " + queryStr_op);
	   HttpClient httpClient = new DefaultHttpClient();
	   String uri_op = baseUri + queryStr_op;
	   System.out.println("\nQuery URL: " + uri_op + "\n");
	   HttpGet httpGet_op = new HttpGet(uri_op);
	   httpGet_op.addHeader(oauthHeader);
	   httpGet_op.addHeader(prettyPrintHeader);
	   
	   HttpResponse response_op = httpClient.execute(httpGet_op);
	   
	   int statusCode_op = response_op.getStatusLine().getStatusCode();
	   System.out.println("\nStatus code returned is " + statusCode_op + "\n\n");
	   if (statusCode_op == 200) {
	    String response_string_op = EntityUtils.toString(response_op.getEntity());
	    try {
	     JSONObject json_op = new JSONObject(response_string_op);
	     
	     
        System.out.println("+++++++++++++ Before Json +++++++++++++++++++");
	     
	     System.out.println(response_string_op);
	     
	     
	     try {
		     DumpDataToBlob.Azure_DB(Opportunity_DB, response_string_op);
		     
		     System.out.println(" Enter inside try Block");
		     }
		     catch (Exception Aex) {
			     Aex.printStackTrace();
			    }
	     
	     
	     System.out.println("+++++++++++++ After Json ++++++++++++++++++++");
	     
	     
	     System.out.println("+++++++++++++ Test Start +++++++++++++++++");
	     
	     System.out.println(json_op);
	     
	     System.out.println("++++++++++++++ Test End +++++++++++++++");
	     
	     
	     System.out.println("Id\t\t\t\tName");
//	     for(Integer i = 0; i < json.length() - 1; i++){
	    	 for(Integer i = 0; i < Opportunity_Count - 1; i++){
	    
	      System.out.print(json_op.getJSONArray("records").getJSONObject(i).getString("Id") + ", ");		 
	      System.out.print(json_op.getJSONArray("records").getJSONObject(i).getString("Name") + ", ");
	      System.out.print(json_op.getJSONArray("records").getJSONObject(i).getString("StageName") + ", ");
	      System.out.print(json_op.getJSONArray("records").getJSONObject(i).getDouble("Amount") + ", ");
	      System.out.print(json_op.getJSONArray("records").getJSONObject(i).getDouble("Probability") + ", ");
	      System.out.print(json_op.getJSONArray("records").getJSONObject(i).getString("CloseDate"));
	      System.out.println();
	     
	     }
	     System.out.println("\nEnd of the execution");
	    } catch (JSONException je) {
	     je.printStackTrace();
	    }           
	   } else {
	    System.out.println("Query was unsuccessful. Status code returned is " + statusCode_op);
	   }
	  } catch (IOException ioe) {
	   ioe.printStackTrace();
	  } catch (NullPointerException npe) {
	   npe.printStackTrace();
	  }
	 }


 public void retrieveRecordsAccount() {
	 
	 
	  System.out.println("\nQuerying Example\n");
	  try {
	   //String  queryStr = getUserInput("Enter the query: ");
	   System.out.println("Gets the records counts for Account table");
	   String  queryStr_ac_cnt = "/query?q=select+count(Id)+from+Account";		
	   System.out.println("\nThe query is " + queryStr_ac_cnt);
	   HttpClient httpClient_ac_cnt = new DefaultHttpClient();
	   String uri_ac_cnt = baseUri + queryStr_ac_cnt;
	   System.out.println("\nQuery URL: " + uri_ac_cnt + "\n");
	   HttpGet httpGet_ac_cnt = new HttpGet(uri_ac_cnt);
	   httpGet_ac_cnt.addHeader(oauthHeader);
	   httpGet_ac_cnt.addHeader(prettyPrintHeader);
	   HttpResponse responseac_cnt = httpClient_ac_cnt.execute(httpGet_ac_cnt);
	   String response_string_cnt = EntityUtils.toString(responseac_cnt.getEntity());
	   System.out.println(" Total number of records available inside account table is :" + response_string_cnt);
	   
	   try {
	   JSONObject json_ac_cnt = new JSONObject(response_string_cnt);
	   
	   Account_Count = json_ac_cnt.getJSONArray("records").getJSONObject(0).getInt("expr0") ;
	   System.out.println(json_ac_cnt.getJSONArray("records").getJSONObject(0).getInt("expr0"));
	      
	   }
	   
	   catch (JSONException je) {
		     je.printStackTrace();
		    }   
	   System.out.println("Records counts check complete");
	   
	   
	   
	   System.out.println("Fetch the records for Account table");
	   //String  queryStr_ac = "/query?q=select+Id,Name,Phone,BillingState+from+Account";		
	   String  queryStr_ac ="/query?q=select+Id,IsDeleted,Name,MasterRecordId,Type,ParentId,Phone,BillingState,BillingStreet,BillingCity,BillingPostalCode,BillingCountry,BillingLatitude,BillingLongitude,BillingAddress,ShippingStreet,ShippingCity,ShippingCountry,ShippingLatitude,ShippingLongitude,ShippingAddress,Fax,AccountNumber,Website,PhotoUrl,Sic,Industry,AnnualRevenue,NumberOfEmployees,Ownership,TickerSymbol,Description,Rating,Site,OwnerId,CreatedDate,CreatedById,LastModifiedDate,LastModifiedById,SystemModstamp,LastActivityDate,LastViewedDate,LastReferencedDate,Jigsaw,JigsawCompanyId,CleanStatus,AccountSource,DunsNumber,Tradestyle,NaicsCode,NaicsDesc,YearStarted,SicDesc,DandbCompanyId,CustomerPriority__c,SLA__c,Active__c,NumberofLocations__c,UpsellOpportunity__c,SLASerialNumber__c,SLAExpirationDate__c+from+Account";	   
	   System.out.println("\nThe query is " + queryStr_ac);
	   HttpClient httpClient_ac = new DefaultHttpClient();
	   String uri_ac = baseUri + queryStr_ac;
	   System.out.println("\nQuery URL: " + uri_ac + "\n");
	   HttpGet httpGet_ac = new HttpGet(uri_ac);
	   httpGet_ac.addHeader(oauthHeader);
	   httpGet_ac.addHeader(prettyPrintHeader);
	   
	   HttpResponse response_ac = httpClient_ac.execute(httpGet_ac);
	   
	   int statusCode = response_ac.getStatusLine().getStatusCode();
	   System.out.println("\nStatus code returned is " + statusCode + "\n\n");
	   if (statusCode == 200) {
	    String response_string = EntityUtils.toString(response_ac.getEntity());
	    try {
	     JSONObject json_ac = new JSONObject(response_string);
	     
	     
	     
	     System.out.println("+++++++++++++ Before Json +++++++++++++++++++");
	     
	     System.out.println(response_string);
	     
	     try {
	     DumpDataToBlob.Azure_DB(Account_DB, response_string);
	     
	     System.out.println(" Enter inside try Block");
	     }
	     catch (Exception Aex) {
		     Aex.printStackTrace();
		    }
	     
	     System.out.println("+++++++++++++ After Json ++++++++++++++++++++");
	     
	     
	     System.out.println("+++++++++++++ Test Start +++++++++++++++++");
	     
	     System.out.println(json_ac);
	     
	     System.out.println("++++++++++++++ Test End +++++++++++++++");
	     
	     System.out.println("Id\t\t\t\tName");
//	     for(Integer i = 0; i < json.length() - 1; i++){
	    	 for(Integer i = 0; i < Account_Count - 1; i++){
	      
	      System.out.print(json_ac.getJSONArray("records").getJSONObject(i).getString("Id") + ", ");		     
	      System.out.print(json_ac.getJSONArray("records").getJSONObject(i).getString("Name") + ", ");
	     // System.out.print(json.getJSONArray("records").getJSONObject(i).getString("MasterRecordId") + ", ");
	     // System.out.print(json.getJSONArray("records").getJSONObject(i).get("Type") + ", ");
	     // System.out.print(json.getJSONArray("records").getJSONObject(i).getDouble("ParentId") + ", ");
	      System.out.print(json_ac.getJSONArray("records").getJSONObject(i).getString("Phone"));
	      System.out.print(json_ac.getJSONArray("records").getJSONObject(i).getString("BillingState"));
	      System.out.println();
	     
	     }
	     System.out.println("\nEnd of the execution");
	    } catch (JSONException je) {
	     je.printStackTrace();
	    }           
	   } else {
	    System.out.println("Query was unsuccessful. Status code returned is " + statusCode);
	   }
	  } catch (IOException ioe) {
	   ioe.printStackTrace();
	  } catch (NullPointerException npe) {
	   npe.printStackTrace();
	  }
	 }

 */
 
 
 static class OAuth2Response {
  String id;
  String issued_at;
  String instance_url;
  String signature;
  String access_token;

   public OAuth2Response() {
  }
  public OAuth2Response(JSONObject json) {
   try {
    id =json.getString("id");
    issued_at = json.getString("issued_at");
    instance_url = json.getString("instance_url");
    signature = json.getString("signature");
    access_token = json.getString("access_token");

    } catch (JSONException e) {
    e.printStackTrace();
   }
  }
 }

  /* Class to hold user credentials */ 
 class UserCredentials {
  String loginInstanceDomain = "ap2.salesforce.com";
  String apiVersion = "35";
  String userName = SampleInsertcsv_back.userName;
  String password = SampleInsertcsv_back.password;
  String consumerKey = "3MVG9ZL0ppGP5UrC4rjQFkEhUnYTSNP_Tvanu8b30_TqkLH7cOg8UC9zHKCsX.mgW_hFVY2J0jRyO.Ev_VsH0";  
  String consumerSecret = "1975032834009986449";
  String grantType = "password";
 }

  private String getBody(InputStream inputStream) {
  String result = "";
  try {
   BufferedReader in = new BufferedReader(
     new InputStreamReader(inputStream)
   );
   String inputLine;
   while ( (inputLine = in.readLine() ) != null ) {
    result += inputLine;
    result += "\n";
   }
   in.close();
  } catch (IOException ioe) {
   ioe.printStackTrace();
  }
  return result;
 }
  
  

  /* Method to get user input */
 private String getUserInput(String prompt) {
  String result = "";
  try {
   System.out.print(prompt);
   result = reader.readLine();
  } catch (IOException ioe) {
   ioe.printStackTrace();
  }
  return result;
 }
 
 /* Method to get login credentials */
 private void getLoginCredentials() {
 // userName = getUserInput("Enter the username: ");
 // password = getUserInput("Enter the password: ");
 }

}
