package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Component
public class ChooseCourseTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseCourseTask.class);
    @Autowired
    TaskService taskService;
    //每隔1分钟扫描消息表，向mq发送消息
//    @Scheduled(cron="0/3 * * * * *")
    @Scheduled(fixedDelay = 60000)
    public void sendChoosecourseTask() {
        //取出当前时间1分钟之前的时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(GregorianCalendar.MINUTE, -1);
        Date time = calendar.getTime();
        List<XcTask> taskList = taskService.findTaskList(time, 1000);
        //调用service发布消息,将选课的任务发送给mq
        for (XcTask xcTask : taskList) {
            //取出任务
            if(taskService.getTask(xcTask.getId(),xcTask.getVersion())>0){
                String mqExchange = xcTask.getMqExchange();//要发送的交换机
                String mqRoutingkey = xcTask.getMqRoutingkey();//发消息需要带着routingKey
                taskService.publish(xcTask,mqExchange,mqRoutingkey);
            }
        }
    }
    @RabbitListener(queues = RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE)
    public void receiveFinishChooseCourseTask(XcTask xcTask){
        if(xcTask!=null&& StringUtils.isNotEmpty(xcTask.getId())){
            taskService.finishTask(xcTask.getId());
        }
    }
}
