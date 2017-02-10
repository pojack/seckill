package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by LLPP on 2016/12/22.
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    /**
     * 秒杀列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/list",//uri
            method = RequestMethod.GET)//请求方式
    public String list(Model model) {
        List<Seckill> list = seckillService.getSeckillList();//得到秒杀列表
        model.addAttribute("list", list);
        return "list";
    }

    /**
     * 秒杀细节
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "ridirect:/seckill/list";
        }
        Seckill seckill = seckillService.getSeckillById(seckillId);
        if (seckillId == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 接口暴露器
     * @param seckillId
     * @return
     */
    //ajax接口，返回json
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"}
    )
    //不是返回view加nodel不需要传入model
    @ResponseBody   //此注解可以将SeckillResult封装为json
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exposeUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
            System.out.println("exposer:"+exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, "发生了不可预知的错误");
        }
        return result;
    }

    /**
     * 秒杀执行器
     * @param seckillId
     * @param md5
     * @param userPhone
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
    method = RequestMethod.POST,
    produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execution(@PathVariable("seckillId") Long seckillId,
                                                     @PathVariable("md5") String md5,
                                                     @CookieValue(value = "killPhone",required = false) Long userPhone){
        SeckillResult<SeckillExecution> result=null;
        SeckillExecution seckillExecution=null;
        try{
                if(userPhone==null){
                    throw new SeckillException("电话为空");
                }
                seckillExecution=seckillService.executeSeckill(seckillId,userPhone,md5);
                result = new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (SeckillCloseException e1){
            //秒杀关闭异常处理
            logger.error(e1.getMessage(), e1);
            seckillExecution=new SeckillExecution(seckillId,SeckillStateEnum.END);
            result = new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (RepeatKillException e2){
            //重复秒杀异常处理
            logger.error(e2.getMessage(), e2);
            seckillExecution=new SeckillExecution(seckillId,SeckillStateEnum.REPEAT_KILL);
            result = new SeckillResult<SeckillExecution>(true,seckillExecution);
        }catch (SeckillException e){
            //其他异常处理
            logger.error(e.getMessage(), e);
            seckillExecution=new SeckillExecution(seckillId,SeckillStateEnum.INNER_ERROR);
            result = new SeckillResult<SeckillExecution>(true,seckillExecution);
        }finally {
            System.out.println("execution:"+result);
        }
        return result;
    }

    /**
     * 返回系统当前时间
     * @return
     */
    @RequestMapping(value = "/time/now",
            method = RequestMethod.GET,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<Long> now(){
        SeckillResult<Long> result=null;
        long time = new Date().getTime();
        result=new SeckillResult<Long>(true,time);
        return result;
    }

}
