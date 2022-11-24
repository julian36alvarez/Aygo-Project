package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.SqsSubscription;
import software.amazon.awscdk.services.sqs.Queue;

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


        //TODO: It's missing to download de docker-compose.yml
        UserData userDataSCript = UserData.forLinux();
        userDataSCript.addCommands("sudo yum install docker -y");
        userDataSCript.addCommands("sudo service docker start");
        userDataSCript.addCommands("sudo curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/bin/docker-compose");
        userDataSCript.addCommands("sudo chmod +x /usr/bin/docker-compose");
        userDataSCript.addCommands("sudo docker-compose up -d");

        topic.addSubscription(new SqsSubscription(queue));
    }
}
