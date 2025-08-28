//package gaji.service.config;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import jakarta.annotation.PostConstruct;
//import lombok.Getter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
////@Configuration
//@Getter
//@ConditionalOnProperty(name = "cloud.aws.enable", havingValue = "true")
//
//public class AmazonConfig {
//
//
//    private AWSCredentials awsCredentials;
//
//    @Value("${cloud.aws.credentials.accessKey}")
//    private String accessKey;
//
//    @Value("${cloud.aws.credentials.secretKey}")
//    private String secretKey;
//
//    @Value("${cloud.aws.region.static}")
//    private String region;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    @Value("${cloud.aws.s3.path.blog}")
//    private String blogPath;
//
//    @Value("${cloud.aws.s3.path.project}")
//    private String projectPath;
//
//    @Value("${cloud.aws.s3.path.question}")
//    private String questionPath;
//
//    @Value("${cloud.aws.s3.path.study}")
//    private String studyPath;
//
//    @PostConstruct
//    public void init() {
//        this.awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//    }
//
//    @Bean
//    public AmazonS3 amazonS3() {
//        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
//        return AmazonS3ClientBuilder.standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                .build();
//    }
//
//    @Bean
//    public AWSCredentialsProvider awsCredentialsProvider() {
//        return new AWSStaticCredentialsProvider(awsCredentials);
//    }
//}