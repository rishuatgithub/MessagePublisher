package com.project.auto.testing.messagepublisher.build.controllers;

import com.google.protobuf.ByteString;
import com.project.auto.testing.messagepublisher.build.services.GCPBucketService;
import com.project.auto.testing.messagepublisher.build.services.GCPPubSubService;
import com.project.auto.testing.messagepublisher.build.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@Slf4j
public class PublisherController {

    @Value("${gcp.project.id}")
    private String GCP_PROJECT_ID;
    @Value("${gcp.topic.name}")
    private String GCP_TOPIC_NAME;
    @Value("${gcp.subscription.name}")
    private String GCP_SUBSCRIPTION_NAME;

    @Value("${gcp.cloud.storage.payload.avro.schema.bucket.name}")
    private String GCP_AVRO_SCHEMA_BUCKET;
    @Value("${gcp.cloud.storage.payload.avro.schema.bucket.file}")
    private String GCP_AVRO_SCHEMA_FILENAME;
    @Value("${gcp.cloud.storage.payload.bucket.name}")
    private String GCP_PAYLOAD_BUCKET_NAME;

    @Autowired
    private Utils utils;
    @Autowired
    private GCPBucketService gcpBucketService;
    @Autowired
    private GCPPubSubService gcpPubSubService;

    //@PostMapping(name = "/publish")
    public List<String> publishMessages() throws InterruptedException, ExecutionException, IOException {

        List<String> payloadfiles = gcpBucketService.listbucketfilename(GCP_PAYLOAD_BUCKET_NAME);
        List<String> publishstatus = new ArrayList<>();

        log.info("Bucket Name :"+GCP_PAYLOAD_BUCKET_NAME);
        log.info("Topic Name :"+GCP_TOPIC_NAME);

        log.info("Testing if PubSub Topic Exists");
        if(gcpPubSubService.istopicexists(GCP_TOPIC_NAME)){
            log.info("Topic "+GCP_TOPIC_NAME+" exists in the PubSub.");
        }else {
            log.info("Topic "+GCP_TOPIC_NAME+" doesnot exist. Hence creating one.");
            gcpPubSubService.createTopic(GCP_TOPIC_NAME);
        }

        for(String files : payloadfiles){
            log.info("Reading content from File: "+files);
            String filecontent = gcpBucketService.readFileContent(GCP_PAYLOAD_BUCKET_NAME,files);

            ByteString avromessage = gcpPubSubService.serializeAvroMessage(filecontent);

            log.info("Publishing Message from file :"+files);
            List<String> publishMessage = gcpPubSubService.publishMessage(avromessage);

            publishstatus.add(publishMessage.get(0));
        }

        log.info("Publish Job Complete.");
        return publishstatus;
    }


}
