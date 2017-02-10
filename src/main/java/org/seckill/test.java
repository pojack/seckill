package org.seckill;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Created by LLPP on 2016/12/31.
 */
public class test implements ApplicationContextAware{
    ApplicationContext applicationContext;
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
    public void getResource() throws IOException {
        Resource resource = applicationContext.getResource("classpath:/spring/spring-dao.xml");
        long length = resource.contentLength();
        System.out.println("文件名称："+resource.getFilename()+" 文件长度："+length);
    }
}
