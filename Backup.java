package com.test.azuredump;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobOutputStream;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
//dump data from local to azure blob storage
public class Backup {
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
		
		/* Properties properties = new Properties();
		 properties.load(new FileInputStream("E:\\input\\connectToAzure.properties"));
		 */      
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
	public static void main(String[] args) throws FileNotFoundException, GeneralSecurityException, URISyntaxException, StorageException, IOException {
		Backup dump=new Backup();
		blob=dump.ConnectToAzure("Hoovers_Raw_Data.csv");
		//String path="E:\\input\\facebook_1.csv";
		System.out.println("Taking the data from given below loaction");
		String path="C:\\Users\\yashwant.chandrakar\\Desktop\\Hoovers\\Hoovers_Raw_Data_Set_New\\Hoovers_Sales_Data.csv";
		System.out.println("Putting the data into blob");
		blob.uploadFromFile(path);
		
		//System.out.println(blob.getContainer());
			
		/*System.out.println(blob.getStorageUri());
		
		System.out.println(blob.getStorageUri());*/
		//	getUri(https://testdonow.blob.core.windows.net/donow/Acount);

		
		/*for (ListBlobItem blobItem : container.listBlobs()) {
            System.out.println(blobItem.getUri());
            CloudBlob blob = (CloudBlob) blobItem

		
		
	}
		
		/*try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

		    // Create the blob client.
		   CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

		   // Get a reference to a container.
		   // The container name must be lower case
		   CloudBlobContainer container = blobClient.getContainerReference("mycontainer");

		   // Create the container if it does not exist.
		    container.createIfNotExists();
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
		
		
	}*/
	}
}
