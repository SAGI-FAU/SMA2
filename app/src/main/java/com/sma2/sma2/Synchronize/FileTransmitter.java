package com.sma2.sma2.Synchronize;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class FileTransmitter {

    // CAUTION: this is hard-coded for testing purposes. It needs to be adapted to the real server values
    public static final String SERVICE_URL = "http:10.2.3.0";//"http://localhost";
    public static final String FILE_SERVER_ROOT="http:10.2.3.0";//"http://localhost";
    public static final String LIBRARY="My Library"; // Find a name for the storage

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private static SeafileApi api = new SeafileApi(SERVICE_URL,FILE_SERVER_ROOT);

    // Username and password from the seafile server
    private String username;
    private String password;
    private String token;

    // Path variables for the seafile server
    private String rel_Path = "";
    private String parent_dir = "/";

    public FileTransmitter(String username, String password) throws IllegalArgumentException{
        this.username = username;
        this.password = password;
        // Get the authentication token for the seafile server
        this.token = api.obtainAuthToken(client, this.username, this.password);
        if (this.token == null) {
            throw new IllegalArgumentException("Obtaining the token did not work, check the username, " +
                    "password or client specifications");
        }
    }

    /**
     * Method to upload the file. Needs to list all libraries first to get the repo_id for the
     * upload link. Then it uses the upload link and token to upload the file
     *
     * @param file
     *
     * @return
     */
    public void uploadFile(File file) throws IllegalArgumentException{

        // Variables to store the directory id and the upload link we need to upload
        String repo_id = null;
        String uploadLink = null;

        // List all existing libraries on the server
        List<jsonObject.Library> libraries = api.listLibraries(client,this.token);

        // Check the list of libraries, and save the repository id if it exists.
        for(jsonObject.Library library:libraries) {
            if(library.getName().equals(LIBRARY)) {
                repo_id = library.getId();
                break;
            }
        }

        // Get the upload link and prepare if necessary
        if (repo_id != null) {
            uploadLink = api.getUploadLink(client, token, repo_id, "");
            if(uploadLink.contains("seafile.example.com")){
                uploadLink = uploadLink.replace("seafile.example.com","10.2.3.0");
            }
        } else {
            throw new IllegalArgumentException("The target repository was not found!");
        }
        // Upload file
        List<jsonObject.UploadFileRes> uploadedFile = api.uploadFile(client,token,uploadLink,parent_dir,rel_Path,file);
        if (uploadedFile == null) {
            throw new IllegalArgumentException("Upload failed!");
        }
    }
}
