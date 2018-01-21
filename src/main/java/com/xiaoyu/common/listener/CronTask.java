/*
 * 唯有读书,不慵不扰
 * 
 */
package com.xiaoyu.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xiaoyu.modules.biz.article.service.api.IArticleService;

/**
 * 定时任务
 * 
 * @author xiaoyu
 * @date 2018-01
 * @description
 */
@Component
public class CronTask {

    private static final Logger LOG = LoggerFactory.getLogger(CronTask.class);

    @Autowired
    private IArticleService articleService;

    /**
     * 每周六凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * 6")
    public void synElastic() {
        LOG.info("定时同步文章开始......");
        this.articleService.synElastic();
        LOG.info("定时同步文章结束......");
    }
}