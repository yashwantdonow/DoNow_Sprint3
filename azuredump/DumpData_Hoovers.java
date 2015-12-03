package com.test.azuredump;

import java.io.ByteArrayInputStream;
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
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
//dump data from local to azure blob storage
public class DumpData_Hoovers {
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

    public CloudBlockBlob ConnectToAzure(String blobName) throws GeneralSecurityException, URISyntaxException, StorageException, FileNotFoundException, IOException{
		
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
		   
		  
		  return blob;
	 }
    
    public static void Azure_DB(String blobName)throws FileNotFoundException, GeneralSecurityException, URISyntaxException, StorageException, IOException
    {
    	System.out.println(" on the way to put the data inside blob");
    	DumpData_Hoovers dump=new DumpData_Hoovers();
		blob=dump.ConnectToAzure(blobName);
		//String path="E:\\input\\facebook_1.csv";
		System.out.println("start the dump process");
		
	    //output.write(data.);
		//blob.uploadText(data);
		String path="C:\\Users\\yashwant.chandrakar\\Desktop\\Hoovers\\Hoovers_Raw_Data_Set_New\\SFDC_DATA_SET.csv";
		blob.uploadFromFile(path);
		System.out.println("Completed the dump process");

    	
    }
    
	public static void main(String[] args) throws FileNotFoundException, GeneralSecurityException, URISyntaxException, StorageException, IOException {
		
		
	}

}
