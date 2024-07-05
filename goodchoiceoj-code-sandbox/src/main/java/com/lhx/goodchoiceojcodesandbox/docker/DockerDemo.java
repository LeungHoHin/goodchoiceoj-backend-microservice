package com.lhx.goodchoiceojcodesandbox.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.List;

public class DockerDemo {


    public static void main(String[] args) throws InterruptedException {
        //获取默认的Docker Client
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();

        String image = "nginx:latest";
//        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
//        PullImageResultCallback pullImageResultCallback = new PullImageResultCallback(){
//            @Override
//            public void onNext(PullResponseItem item) {
//                System.out.println("下载镜像： " + item.getStatus());
//                super.onNext(item);
//            }
//        };
//
//        pullImageCmd.exec(pullImageResultCallback)
//                .awaitCompletion();
//        System.out.println("下载完成");
//        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(image);
//        CreateContainerResponse createContainerResponse = createContainerCmd.exec();
//        System.out.println(createContainerResponse);

        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();
        List<Container> containerList = listContainersCmd.withShowAll(true).exec();
        for (Container container : containerList) {
            System.out.println("容器" + container);
        }
    }


}
