package com.myorg;

import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.elasticloadbalancingv2.*;
import software.amazon.awscdk.services.elasticloadbalancingv2.Protocol;
import software.amazon.awscdk.services.elasticloadbalancingv2.targets.InstanceTarget;
import software.constructs.Construct;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CdkStack extends Stack {
    public CdkStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public CdkStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);

        final Queue queue = Queue.Builder.create(this, "CdkQueue")
                .visibilityTimeout(Duration.seconds(300))
                .build();

        final Topic topic = Topic.Builder.create(this, "CdkTopic")
            .displayName("My First Topic Yeah")
            .build();

        InstanceType instanceType = InstanceType.of(InstanceClass.T2, InstanceSize.MICRO);

        IMachineImage machineImage = MachineImage.latestAmazonLinux();

        Vpc vpc = Vpc.Builder.create(this, "VPC")
                .maxAzs(2)
                .enableDnsHostnames(true)
                .enableDnsSupport(true)
                .build();

        SecurityGroup securityGroup = SecurityGroup.Builder.create(this, "SecurityGroup")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();

        SubnetSelection subnetSelection = SubnetSelection.builder()
                .subnetType(SubnetType.PUBLIC)
                .build();

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
        userDataScript.addCommands("touch docker-compose.yml");
        userDataScript.addCommands("echo 'version: \"2\"' >> docker-compose.yml");
        userDataScript.addCommands("echo 'services:' >> docker-compose.yml");
        userDataScript.addCommands("echo '  aygo-project:' >> docker-compose.yml");
        userDataScript.addCommands("echo '    image:  nontoa10/aygo-project-test:latest' >> docker-compose.yml");
        userDataScript.addCommands("echo '    ports:' >> docker-compose.yml");
        userDataScript.addCommands("echo '      - \"8080:8080\"' >> docker-compose.yml");

        userDataScript.addCommands("sudo docker-compose up -d");

        List<InstanceTarget> targets = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Instance instance = Instance.Builder.create(this, "aygo-project-" + i)
                    .instanceType(instanceType)
                    .machineImage(machineImage)
                    .vpc(vpc)
                    .vpcSubnets(subnetSelection)
                    .allowAllOutbound(true)
                    .securityGroup(securityGroup)
                    .userData(userDataScript)
                    .keyName("aygo-key")
                    .build();
            InstanceTarget instanceTarget = new InstanceTarget(instance);
            targets.add(instanceTarget);
        }

        HealthCheck healthCheck = HealthCheck
                .builder()
                .enabled(true)
                .protocol(Protocol.HTTP)
                .port("8080")
                .build();

        ApplicationTargetGroup targetGroup = ApplicationTargetGroup.Builder.create(this, "aygo-project-target-group")
                .targetGroupName("aygo-project-target-group")
                .targetType(TargetType.INSTANCE)
                .protocol(ApplicationProtocol.HTTP)
                .port(8080)
                .vpc(vpc)
                .protocolVersion(ApplicationProtocolVersion.HTTP1)
                .healthCheck(healthCheck)
                .targets(targets)
                .build();


        ApplicationLoadBalancer loadBalancer = ApplicationLoadBalancer.Builder.create(this, "aygo-project-load-balancer")
                .loadBalancerName("aygo-project-load-balancer")
                .ipAddressType(IpAddressType.IPV4)
                .vpc(vpc)
                .vpcSubnets(subnetSelection)
                .securityGroup(securityGroup)
                .build();


        ApplicationListener listener = loadBalancer.addListener("listener", BaseApplicationListenerProps.builder().port(8080).build());
        listener.addTargetGroups("targetGroup", AddApplicationTargetGroupsProps.builder()
                        .targetGroups(Arrays.asList(targetGroup))
                .build());


        topic.addSubscription(new SqsSubscription(queue));
    }
}
