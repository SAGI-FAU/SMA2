package com.sma2.sma2.Synchronize;

import java.io.File;
import java.util.List;

import jsonObject.Library;
import okhttp3.OkHttpClient;

public class FileTransmitter {

    public static final String SERVICE_URL = "http://localhost";
    public static final String FILE_SERVER_ROOT="http://localhost";
    public static final String LIBRARY="My Library"; // Find a name for the storage

    private OkHttpClient client = new OkHttpClient();
    private SeafileApi api = new SeafileApi(SERVICE_URL,FILE_SERVER_ROOT);

    private String username;
    private String password;
    private String token;

    public FileTransmitter(String username, String password) {
        this.username = username;
        this.password = password;
        token = api.obtainAuthToken(client, this.username, this.password);
    }


    public void uploadFile(File file) throws NullPointerException{

        // Variables to store the directory id and the upload link we need to upload
        String repo_id = null;
        String uploadLink = null;

        List<Library> libraries = api.listLibraries(client,token);

        // Check the list of libraries, and save the repository id if it exists.
        for(Library library:libraries) {
            if(library.getName().equals(LIBRARY)) {
                repo_id = library.getId();
                break;
            }
        }

        // Get the upload link and prepare if necessary
        if (repo_id != null) {
            uploadLink = api.getUploadLink(client, token, repo_id, "");
            if(uploadLink.contains("seafile.example.com")){
                uploadLink = uploadLink.replace("seafile.example.com","localhost");
            }
        } else {
            throw new NullPointerException("The target repository was not found!");
        }

        // Upload file
        List<jsonObject.UploadFileRes> uploadFileResList=api.uploadFile(client,token,uploadLink,"/","",file);

    }

}
