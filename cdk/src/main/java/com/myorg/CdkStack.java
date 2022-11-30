package com.myorg;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.apigatewayv2.alpha.*;
import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpUrlIntegration;
import software.amazon.awscdk.services.autoscaling.AutoScalingGroup;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.elasticloadbalancingv2.Protocol;
import software.amazon.awscdk.services.elasticloadbalancingv2.*;
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.s3.ObjectOwnership;
import software.constructs.Construct;

import java.util.Arrays;
import java.util.Collections;


public class CdkStack extends Stack {
    public CdkStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public CdkStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);


        InstanceType instanceType = InstanceType.of(InstanceClass.T2, InstanceSize.MICRO);

        IMachineImage machineImage = MachineImage.latestAmazonLinux();

        Vpc vpc = Vpc.Builder.create(this, "VPC").maxAzs(2).enableDnsHostnames(true).enableDnsSupport(true).build();

        SecurityGroup securityGroup = SecurityGroup.Builder.create(this, "SecurityGroup").vpc(vpc).allowAllOutbound(true).build();

        SubnetSelection subnetSelection = SubnetSelection.builder().subnetType(SubnetType.PUBLIC).build();

        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(22), "allow SSH access");
        securityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(8080), "allow HTTP access");


        UserData userDataScript = UserData.forLinux();
        userDataScript.addCommands("sudo su");
        userDataScript.addCommands("yum update -y");
        userDataScript.addCommands("yum install docker -y");
        userDataScript.addCommands("service docker start");
        userDataScript.addCommands("curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/bin/docker-compose");
        userDataScript.addCommands("chmod +x /usr/bin/docker-compose");
        userDataScript.addCommands("cd /home/ec2-user");


        userDataScript.addCommands("yum install java-1.8.0");
        userDataScript.addCommands("yum install -y java-1.8.0-openjdk-devel");
        userDataScript.addCommands("alternatives --set java /usr/lib/jvm/jre-1.8.0-openjdk.x86_64/bin/java");

        userDataScript.addCommands("wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo");
        userDataScript.addCommands("sed -i s/\\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo");
        userDataScript.addCommands("yum install -y apache-maven");
        userDataScript.addCommands("yum install git -y");
        userDataScript.addCommands("git clone https://github.com/julian36alvarez/Aygo-Project.git");
        userDataScript.addCommands("cd Aygo-Project/backend");
        userDataScript.addCommands("mvn clean install");
        userDataScript.addCommands("export AWS_ACCESS_KEY_ID=xx");
        userDataScript.addCommands("export AWS_SECRET_ACCESS_KEY=xx");
        userDataScript.addCommands("java -jar target/SpringVideoApp-1.0-SNAPSHOT.jar --server.port=8080");


        Bucket.Builder.create(this, "aygo-bucket-project-test").bucketName("aygo-bucket-project-test").versioned(false).removalPolicy(RemovalPolicy.DESTROY).encryption(BucketEncryption.UNENCRYPTED).autoDeleteObjects(false).blockPublicAccess(BlockPublicAccess.BLOCK_ACLS) // Block all public access ??
                .objectOwnership(ObjectOwnership.BUCKET_OWNER_PREFERRED).build();

        //auto scaling group
        AutoScalingGroup autoScalingGroup = AutoScalingGroup.Builder.create(this, "AutoScalingGroup").instanceType(instanceType).machineImage(machineImage).vpc(vpc).vpcSubnets(subnetSelection).allowAllOutbound(true).securityGroup(securityGroup).userData(userDataScript).keyName("aygo-key").minCapacity(1).maxCapacity(3).build();


        HealthCheck healthCheck = HealthCheck.builder().enabled(true).protocol(Protocol.HTTP).port("8080").build();

        // 👇 add target to the ALB listener autoScalingGroup as target
        ApplicationTargetGroup targetGroup = ApplicationTargetGroup.Builder.create(this, "aygo-project-target-group").targetGroupName("aygo-project-target-group").targetType(TargetType.INSTANCE).protocol(ApplicationProtocol.HTTP).port(8080).vpc(vpc).protocolVersion(ApplicationProtocolVersion.HTTP1).healthCheck(healthCheck).targets(Collections.singletonList(autoScalingGroup)).build();


        ApplicationLoadBalancer loadBalancer = ApplicationLoadBalancer.Builder.create(this, "aygo-project-load-balancer").loadBalancerName("aygo-project-load-balancer").ipAddressType(IpAddressType.IPV4).vpc(vpc).internetFacing(true).vpcSubnets(subnetSelection).securityGroup(securityGroup).build();


        ApplicationListener listener = loadBalancer.addListener("listener", BaseApplicationListenerProps.builder().port(8080).build());
        listener.addTargetGroups("targetGroup", AddApplicationTargetGroupsProps.builder().targetGroups(Arrays.asList(targetGroup)).build());


        HttpApi appi = HttpApi.Builder.create(this, "aygo-project-http-api")
                .apiName("aygo-project-http-api")
                .corsPreflight(CorsPreflightOptions.builder()
                        .allowOrigins(Arrays.asList("*"))
                        .allowMethods(Arrays.asList(CorsHttpMethod.GET, CorsHttpMethod.POST, CorsHttpMethod.OPTIONS))
                        .allowHeaders(Arrays.asList("*"))
                        .build()).build();

        //HttpUrlIntegration  for /
        HttpUrlIntegration httpUrlIntegration = new HttpUrlIntegration("root", "http://" + loadBalancer.getLoadBalancerDnsName() + ":8080");

        //HttpUrlIntegration for /items
        HttpUrlIntegration httpUrlIntegrationItems = new HttpUrlIntegration("items", "http://" + loadBalancer.getLoadBalancerDnsName() + ":8080/items");

        //http integration for /fileupload post
        HttpUrlIntegration urlIntegrationFileUpLoad = new HttpUrlIntegration("fileupload", "http://" + loadBalancer.getLoadBalancerDnsName() + ":8080/fileupload");

        //http integration for /{id}/stream get
        HttpUrlIntegration urlIntegrationStream = new HttpUrlIntegration("stream", "http://" + loadBalancer.getLoadBalancerDnsName() + ":8080/{id}/stream");


        appi.addRoutes(AddRoutesOptions.builder().path("/")
                .methods(Arrays.asList(HttpMethod.GET, HttpMethod.OPTIONS))
                .integration(httpUrlIntegration)
                .build());

        appi.addRoutes(AddRoutesOptions.builder().path("/items")
                .methods(Arrays.asList(HttpMethod.GET, HttpMethod.OPTIONS))
                .integration(httpUrlIntegrationItems)
                .build());

        appi.addRoutes(AddRoutesOptions.builder().path("/fileupload")
                .methods(Arrays.asList(HttpMethod.POST, HttpMethod.OPTIONS))
                .integration(urlIntegrationFileUpLoad)
                .build());

        appi.addRoutes(AddRoutesOptions.builder().path("/{id}/stream")
                .methods(Arrays.asList(HttpMethod.GET, HttpMethod.OPTIONS))
                .integration(urlIntegrationStream)
                .build());


    }
}
