package com.mysqldatadump;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobOutputStream;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import com.test.DumpDataToBlob;
//dump data from local to azure blob storage
public class MySqlConnector {
	public static  String storageConnectionString;
	public static  String containerName;
	public static String DefaultEndpointsProtocol;
	public static String AccountName;
	public static String AccountKey ;
	public static PrintWriter out = null;
	public BlobOutputStream blobOutputStream;
	public ByteArrayInputStream inputStream;
	CloudStorageAccount storageAccount;
	CloudBlobClient blobClient;
	CloudBlobContainer container;
	static CloudBlockBlob blob ;

    public void ConnectToAzure(String blobName) throws GeneralSecurityException, URISyntaxException, StorageException, FileNotFoundException, IOException{
		
		// Properties properties = new Properties();
		// properties.load(new FileInputStream("E:\\input\\connectToAzure.properties"));
		       
		 /* DefaultEndpointsProtocol=properties.getProperty("DefaultEndpointsProtocol");
		  AccountName=properties.getProperty("AccountName");
		  containerName=properties.getProperty("containerName");
		  AccountKey=properties.getProperty("AccountKey");*/
		 
		 
		  DefaultEndpointsProtocol="http";
		  AccountName="testdonow";
		  containerName="donow";
		  AccountKey="vIHJ1u8RN04mMaAZFxuxGKTCYtvfXsAKHIgSEGNhz7L9vbTMRi8y3rgugjthe2US6CR5xHw0GVkTWsK31qhkIw==";
		 
		 
		  storageConnectionString = 
				    "DefaultEndpointsProtocol="+DefaultEndpointsProtocol+";" +
				    "AccountName="+AccountName+";" + 
				    "AccountKey="+AccountKey;
		  
		  storageAccount = CloudStorageAccount.parse(storageConnectionString);
		  blobClient = storageAccount.createCloudBlobClient();
		  container = blobClient.getContainerReference(containerName);
		  blob = container.getBlockBlobReference(blobName);
		  
	    	CloudBlobContainer container = blobClient.getContainerReference("donow");
	        for (ListBlobItem blobItem : container.listBlobs()) {
	           //System.out.println(blobItem.toString());
	           // System.out.println(blobItem.getUri());
	            if(blobItem.getUri().toString().contains("Lead.csv"))
	            { 
	            	System.out.println("inside container");
	            	CloudBlob blob = (CloudBlob) blobItem ;
	                blob.downloadToFile("C://Users//yashwant.chandrakar//Desktop//MYSQL/lead.csv");
	            	 
	            }

	            
		 // return sdfc.csv;
	 }
    }
    /*public static void Azure_DB(String blobName)throws FileNotFoundException, GeneralSecurityException, URISyntaxException, StorageException, IOException
    {
    	//System.out.println(" on the way to put the data inside blob");
    
		//String path="E:\\input\\facebook_1.csv";
		//System.out.println("start the dump process");
		
	    //output.write(data.);
		//blob.uploadText(data);
		//String path="C:\\Users\\yashwant.chandrakar\\Desktop\\SEDC\\API_CODE_DATA_DUMP\\SFDC_DATA_SET.csv";
		
		//System.out.println(blob.getContainer());
		
		blob.downloadToFile("testdonow.blob.core.windows.net/donow/Lead.csv");
		System.out.println("Completed the dump process");

    	for (ListBlobItem blobItem : container.listBlobs()) {
            System.out.println(blobItem.getUri());
            CloudBlob blob = (CloudBlob) blobItem

    	

    	
    	
    	
    }
    */
	public static void main(String[] args) throws FileNotFoundException, GeneralSecurityException, URISyntaxException, StorageException, IOException {
		
		System.out.println("main method");
		MySqlConnector dump=new MySqlConnector();
		dump.ConnectToAzure("MYSQL_CSV");
		
		MySqlDataDumpFromBlob.Run();
		
		
	}

}
