/**
 * Created by LLPP on 2016/12/24.
 */
//存放主要逻辑d代码
var seckill = {
    //封装所有url
    URL: {
        now:function(){
            return '/seckill/time/now';
        },
        exposer:function (seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },
        execution:function (seckillId,md5) {
            var url = '/seckill/'+seckillId+'/'+md5+'/execution';
            return '/seckill/'+seckillId+'/'+md5+'/execution';
        }
    },
    validatePhone: function (killPhone) {
        if (killPhone != null && killPhone.length == 11 && !isNaN(killPhone)) {
            return true;
        }
        else {
            return false;
        }
    },
    //秒杀处理
    handleSeckill:function (seckillId,node) {

        //添加秒杀按钮（先隐藏再暴露）
        node.hide().html('<button class="btn bg-primary btn-lg" id="killBtn">点我秒杀</button>');
        //暴露秒杀url
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //如若秒杀未开启或暴露地址失败，重现计时器面板
            if(result && result['success']){
                var exposer=result['data'];
                if(!exposer['exposed']||exposer['exposed']==null){
                    var now=exposer['now'];
                    var start=exposer['start'];
                    var end=exposer['end'];
                    seckill.counter(seckillId,start,end,now);
                }else{
                    //秒杀已经开启
                    var killBtn=$('#killBtn');
                    var md5=exposer['md5'];
                    //监听一次点击事件
                    $('#killBtn').one('click',function () {
                        //点击后马上禁用按钮
                        $(this).addClass('disabled');
                        var URL =seckill.URL.execution(seckillId,md5);
                        $.post(URL,{},function (result) {
                            if(result&&result['success']){
                                var execution =result['data'];
                                var stateInfo=execution['stateInfo'];
                                $('#killBtn').html('sonofbitch11');
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                            else{
                                var message=result['message'];
                                $('#killBtn').html('sonofbitch22');
                                node.html('<span class="label label-success">' + message + '</span>');
                            }
                        });
                    });
                    node.show();
                }
            }else{
                console.log('result:'+result);
            }
        });
    },
    //计时器模块
    counter:function(seckillId, startTime, endTime, nowTime){
        var seckillbox = $('#seckill-box');
        if(nowTime>endTime){
            seckillbox.html('秒杀结束了，客官。');
        }else if(nowTime<startTime){
            //计时器
            var killTime = new Date(startTime+1000);
            seckillbox.countdown(startTime,function (event) {
                var foramt=event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒！');
                seckillbox.html(foramt);
            }).on('finish.countdown',function () {
                seckill.handleSeckill(seckillId,seckillbox);
            });
        }else{
            //秒杀开始
            seckill.handleSeckill(seckillId,seckillbox);
        }
    },
    //详情页面的秒杀逻辑
    detail: {
        //初始化
        init: function (params) {
            //用户手机验证
            var killPhone = $.cookie('killPhone');

            //验证失败：1.弹出窗口进行验证
            if (!seckill.validatePhone(killPhone)) {
                //用id选择器得到modal
                var killPhoneModal = $('#killPhoneModal');
                //设置参数
                killPhoneModal.modal({
                    show: true,
                    keyboard: false,//关闭键盘事件
                    backdrop: 'static'//禁止关闭
                });
                //监听点击事件
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    //当验证成功：1.将killphone放入cookie 2.重新加载页面
                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号格式错误</label>').show(500);
                    }
                });
            }
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];

            //验证成功：进入计时器
            $.get(seckill.URL.now(),{}, function (result) {
                    if (result && result['success']) {
                        //进入计时模块
                        var nowTime = result['data'];
                        seckill.counter(seckillId, startTime, endTime, nowTime);
                    } else {
                        //输出错误信息
                        console.log('result' + result);
                    }
                }
            );
        }
    }
}