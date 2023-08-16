package com.sanyue.pulsar.function.pulsarfunctiondemo.demo1;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.common.functions.FunctionConfig;
import org.apache.pulsar.functions.LocalRunner;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

import java.util.Collections;

/**
 * 部署此Jar包到Pulsar集群中：
 * <pre>
 * {@code
 * $ bin/pulsar-admin functions create \
 *   --jar target/my-jar-with-dependencies.jar \
 *   --classname org.example.functions.WordCountFunction \
 *   --tenant public \
 *   --namespace default \
 *   --name word-count \
 *   --inputs persistent://public/default/sentences \
 *   --output persistent://public/default/count
 * }
 * </pre>
 * @author Zhaopo Liu
 */
@Slf4j
public class HelloWorldFunction implements Function<String, String> {


    /**
     * 每次将消息发布到输入主题时，都会调用此函数
     *
     * @param input
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public String  process(String input, final Context context) throws Exception {
        log.info("收到来自 {} 的消息 {} "  ,context.getInputTopics() + input);
        for (String word : input.split(",")) {
            String wordUpperCase = word.toUpperCase();
            log.info(wordUpperCase);
        }
        log.info("input string: {}", input);
        context.publish(Consts.OUTPUT_TOPIC, "OUTPUT TOPIC RESULT");
        return null;
    }

    /**
     * <pre>
     * 设置localrun运行参数（包括pulsar地址、functionConfig、sourceConfig、sinkConfig等等），参考 LocalRunner {@link LocalRunner}类。
     * </pre>
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        FunctionConfig functionConfig = new FunctionConfig();
        // 设置pulsar function的名字
        functionConfig.setName("wordcount");
        // 设置pulsar function类的名字
        functionConfig.setClassName(HelloWorldFunction.class.getName());
        functionConfig.setRuntime(FunctionConfig.Runtime.JAVA);
        // input topic,output topic,log topic
        functionConfig.setInputs(Collections.singleton(Consts.INPUT_TOPIC));
        functionConfig.setOutput(Consts.OUTPUT_TOPIC);
        functionConfig.setLogTopic(Consts.LOG_TOPIC);

        // 设置localrun运行参数
        LocalRunner localRunner = LocalRunner.builder()
                // 默认是本地
                .brokerServiceUrl(Consts.SERVER_URL)
                // 设置functionConfig
                .functionConfig(functionConfig)
                .build();
//        org.apache.logging.log4j.LogBuilder

        // 非阻塞
        localRunner.start(false);
    }
}
