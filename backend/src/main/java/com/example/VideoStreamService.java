package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingRequest;
import software.amazon.awssdk.services.s3.model.GetObjectTaggingResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoStreamService {

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String VIDEO_CONTENT = "video/";
    private S3Client s3 = null;

    private S3Client getClient() {

        // Create the S3Client object.
        Region region = Region.US_EAST_1;
        s3 = S3Client.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(region)
                .build();

        return s3;
    }

    // Places a new video into an Amazon S3 bucket.
    public void putVideo(byte[] bytes, String bucketName, String fileName, String description) {
        s3 = getClient();

        try {
            // Set the tags to apply to the object.
            String theTags = "name=" + fileName + "&description=" + description;

            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .tagging(theTags)
                    .build();

            s3.putObject(putOb, RequestBody.fromBytes(bytes));

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    // Returns a schema that describes all tags for all videos in the given bucket.
    public List<Tag> getTags(String bucketName) {
        System.out.println("Getting tags for all videos in the bucket " + bucketName);
        s3 = getClient();

        try {

            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            List<S3Object> objects = res.contents();
            List<String> keys = new ArrayList<>();

            for (S3Object myValue : objects) {

                String key = myValue.key(); // We need the key to get the tags.

                //Get the tags.
                GetObjectTaggingRequest getTaggingRequest = GetObjectTaggingRequest
                        .builder()
                        .key(key)
                        .bucket(bucketName)
                        .build();

                GetObjectTaggingResponse tags = s3.getObjectTagging(getTaggingRequest);
                List<software.amazon.awssdk.services.s3.model.Tag> tagSet = tags.tagSet();
                for (software.amazon.awssdk.services.s3.model.Tag tag : tagSet) {
                    keys.add(tag.value());
                }
            }

            List<Tag> tagList = modList(keys);
            return tagList;

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return new ArrayList<>();
    }

    // Return a List where each element is a Tags object.
    private List<Tag> modList(List<String> myList) {

        // Get the elements from the collection.
        int count = myList.size();
        List<Tag> allTags = new ArrayList<>();
        Tag myTag;
        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        for (int index = 0; index < count; index++) {

            if (index % 2 == 0)
                keys.add(myList.get(index));
            else
                values.add(myList.get(index));
        }

        // Create a list where each element is a Tags object.
        for (int r = 0; r < keys.size(); r++) {

            myTag = new Tag();
            myTag.setName(keys.get(r));
            myTag.setDesc(values.get(r));
            allTags.add(myTag);
        }
        return allTags;
    }


    // Reads a video from a bucket and returns a ResponseEntity.
    public ResponseEntity<byte[]> getObjectBytes(String bucketName, String keyName) {

        s3 = getClient();

        try {
            // Create a GetObjectRequest instance.
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(bucketName)
                    .build();

            // Get the byte[] from this AWS S3 object and returns a ResponseEntity.
            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
            return ResponseEntity.status(HttpStatus.OK)
                    .header(CONTENT_TYPE, VIDEO_CONTENT + "mp4")
                    .header(CONTENT_LENGTH, String.valueOf(objectBytes.asByteArray().length))
                    .body(objectBytes.asByteArray());

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }

}
