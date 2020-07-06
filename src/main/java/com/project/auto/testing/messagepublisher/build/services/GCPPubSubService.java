package com.project.auto.testing.messagepublisher.build.services;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.*;
import com.project.auto.testing.messagepublisher.build.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class GCPPubSubService {

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

    @Autowired
    private Utils utils;
    @Autowired
    private GCPBucketService gcpBucketService;

    //create pub sub topic
    public Boolean createTopic(String topicID){
        try {
            TopicAdminClient topicAdminClient = TopicAdminClient.create();
            TopicName topicName = TopicName.of(GCP_PROJECT_ID, topicID);
            topicAdminClient.createTopic(topicName);

            topicAdminClient.shutdown();

        } catch (IOException e) {
            log.error("Error during creating topic in GCP PubSub");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // delete a pub sub topic
    public Boolean deleteTopic(String topic_id){
        try {
            TopicAdminClient topicAdminClient = TopicAdminClient.create();
            TopicName topicName = TopicName.of(GCP_PROJECT_ID, topic_id);
            log.info("Deleting Topic from PubSub :"+topicName);
            topicAdminClient.deleteTopic(topicName);

            topicAdminClient.shutdown();

        } catch (IOException e) {
            log.error("Error during deleting topic in GCP PubSub");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // search if a topic exists
    public Boolean istopicexists(String search_topicid){
        try {
            TopicAdminClient topicAdminClient = TopicAdminClient.create();
            ProjectName projectName = ProjectName.of(GCP_PROJECT_ID);
            for (Topic topic : topicAdminClient.listTopics(projectName).iterateAll()){
                String[] split_tname = topic.getName().split("/");
                String topicName = split_tname[split_tname.length - 1];

                if(topicName.equalsIgnoreCase(search_topicid)){
                    log.info("Found Topic in GCP "+search_topicid);
                    return true;
                }
            }

            topicAdminClient.shutdown();

        } catch (IOException e) {
            log.error("Error during searching for topic in GCP PubSub");
            e.printStackTrace();
            return false;
        }

        return false;
    }



    // serialize avro messages to protobuf
    public ByteString serializeAvroMessage(String message) throws IOException {

        String payload_schema = gcpBucketService.readFileContent(GCP_AVRO_SCHEMA_BUCKET,GCP_AVRO_SCHEMA_FILENAME);

        byte[] payloadavromessage = utils.convertJsontoAvro(message,payload_schema);

        return ByteString.copyFrom(payloadavromessage);

    }

    //publish message to pubsub
    public List<String> publishMessage(ByteString messages) throws IOException, ExecutionException, InterruptedException {
        List<ApiFuture<String>> futures = new ArrayList<>();
        List<String> publishstatus = new ArrayList<>();

        ProjectTopicName topicName = ProjectTopicName.of(GCP_PROJECT_ID, GCP_TOPIC_NAME);

        Publisher publisher = null;
        try {
            publisher = Publisher.newBuilder(topicName).build();
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(messages).build();
            ApiFuture<String> future = publisher.publish(pubsubMessage);
            futures.add(future);

        } finally {
            List<String> messageIds = ApiFutures.allAsList(futures).get();
            for (String messageId : messageIds) {
                publishstatus.add(messageId + "Published to Topic:"+topicName);
                log.info("Published Messages with MessagesID: " + messageId);
            }
            if (publisher != null) {
                publisher.shutdown();
            }
        }

        return publishstatus;
    }


}
