<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: LLPP
  Date: 2016/12/24
  Time: 9:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common/tag.jsp"%>
<html>
<head>
    <title>详情页面</title>
    <%@include file="common/head.jsp" %>
</head>
<body>
    <div class="container">
        <div class="panel panel-default text-center">
            <div class="panel-heading">
                <h2>${seckill.name}</h2>
            </div>
            <div class="panel-body">
                <h2 class="text-danger">
                    <!--展示图标-->
                    <span class="glyphicon glyphicon-time"></span>
                    <!--展示秒杀计时器-->
                    <span class="glyphicon" id="seckill-box"></span>
                </h2>
            </div>
        </div>
    </div>
    <div id="killPhoneModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title text-center">
                        <span class="glyphicon glyphicon-phone"></span>秒杀电话
                    </h3>
                </div>

                <div class="modal-body">
                    <div class="row">
                        <div>
                            <input type="text" name="killPhone" id="killPhoneKey"
                            placeholder="请填手机号" class="form-control"/>
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <span id="killPhoneMessage" class="glyphicon"></span>
                    <button type="button" id="killPhoneBtn" class="btn btn-success">
                        <span class="glyphicon glyphicon-phone"></span>
                        提交信息
                    </button>
                </div>
            </div>
        </div>
    </div>
</body>
<%@include file="common/tail.jsp"%>
<!--从cdn-->
<script src="http://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="http://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
<script src="/resources/script/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
            seckill.detail.init(
                {
                    seckillId:${seckill.seckillId},
                    startTime:${seckill.startTime.time},
                    endTime:${seckill.endTime.time}
                }
            )
        });
</script>
</html>
