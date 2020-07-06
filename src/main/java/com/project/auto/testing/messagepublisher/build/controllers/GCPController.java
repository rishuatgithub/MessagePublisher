package com.project.auto.testing.messagepublisher.build.controllers;

import com.google.protobuf.ByteString;
import com.project.auto.testing.messagepublisher.build.services.GCPBucketService;
import com.project.auto.testing.messagepublisher.build.services.GCPPubSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class GCPController {

    @Autowired
    private GCPBucketService gcpBucketService;
    @Autowired
    private GCPPubSubService gcpPubSubService;

    @GetMapping(path = "/pubsub/createtopic")
    public String createTopic(String topicID){
        if(gcpPubSubService.createTopic(topicID)){
            return "Topic "+topicID+" Created Successfully.";
        }else{
            return "Topic "+topicID+" Creation Failed. Please check log for more.";
        }

    }

    @GetMapping(path = "/pubsub/deletetopic")
    public String deleteTopic(String topicName){
        if(gcpPubSubService.deleteTopic(topicName)){
            return "Topic Deleted.";
        }else{
            return "Topic Deletion Failed. Please check log for more.";
        }
    }

    @GetMapping(path = "/pubsub/istopicexist")
    public String searchForTopic(String topicName){
        if(gcpPubSubService.istopicexists(topicName)){
            return "Topic Exists.";
        }else{
            return "Topic Doesn't Exist. You need to Create one.";
        }

    }

    @PostMapping(path = "/pubsub/publishmessage")
    public List<String> publishMessage(String message) throws IOException, ExecutionException, InterruptedException {
        ByteString avromessage = gcpPubSubService.serializeAvroMessage(message);
        List<String> publishstatus = gcpPubSubService.publishMessage(avromessage);
        return publishstatus;
    }

    @GetMapping(path = "/storage/listbuckets")
    public List<String> listBuckets(){
        return gcpBucketService.listbuckets();
    }

    @GetMapping(path = "/storage/listfiles")
    public List<String> listBucketFiles(String bucketname){
        return gcpBucketService.listbucketfilename(bucketname);
    }

    @GetMapping(path = "/storage/readfilecontent")
    public String readFiles(String bucketname, String filename){
        return gcpBucketService.readFileContent(bucketname,filename);
    }


}
