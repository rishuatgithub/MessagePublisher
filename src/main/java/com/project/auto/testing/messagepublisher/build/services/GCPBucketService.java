package com.project.auto.testing.messagepublisher.build.services;

import com.google.api.gax.paging.Page;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GCPBucketService {

    @Value("${gcp.project.id}")
    private String GCP_PROJECT_ID;

    // list the total buckets in a project
    public List<String> listbuckets(){
        Storage storage = getGCPStorageSession();
        Page<Bucket> buckets = storage.list();
        ArrayList<String> bucketlist = new ArrayList<>();

        for(Bucket bucket: buckets.iterateAll()){
            bucketlist.add(bucket.getName());
        }
        return bucketlist;

    }

    // read the list of files from bucket
    public List<String> listbucketfilename(String bucketName){
        Storage storage = getGCPStorageSession();
        Bucket bucket = storage.get(bucketName);
        ArrayList<String> fileslist = new ArrayList<>();

        Page<Blob> blobs = bucket.list();
        for (Blob blob: blobs.iterateAll()){
            fileslist.add(blob.getName());
        }

        return fileslist;
    }

    // read the files from bucket
    public String readFileContent(String bucketName, String filename_url){
        Storage storage = getGCPStorageSession();
        Blob blob = storage.get(bucketName,filename_url);

        String content = new String(blob.getContent());
        return content;
    }

    // get the storage session for cloud storage
    private Storage getGCPStorageSession(){
        return StorageOptions.newBuilder().setProjectId(GCP_PROJECT_ID).build().getService();
    }




}
