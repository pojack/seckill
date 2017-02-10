<%--
  Created by IntelliJ IDEA.
  User: LLPP
  Date: 2016/12/24
  Time: 9:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp" %>
<head>
    <title>秒杀列表页</title>
    <%@include file="common/head.jsp" %>
</head>
<body>
<!--页面显示部分-->
<div class="container">
    <!--展示版-->
    <div class="panel panel-default">
        <!--展示板头-->
        <div class="panel-heading text-center">
            <h2>秒杀列表</h2>
        </div>
        <!--展示板体-->
        <div class="panel-body">
            <!--秒杀列表表格-->
            <table class="table table-hover">
                <!--表格头-->
                <thead>
                <th>秒杀活动</th>
                <th>库存数量</th>
                <th>开始时间</th>
                <th>结束时间</th>
                <th>创建时间</th>
                </thead>
                <!--表格体-->
                <tbody>
                <c:forEach var="seckill" items="${list}">
                    <tr>
                        <td>${seckill.name}</td>
                        <td>${seckill.number}</td>
                        <td>
                            <fmt:formatDate value="${seckill.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${seckill.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${seckill.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                        </td>
                        <td>
                            <a class="btn btn-info" href="/seckill/${seckill.seckillId}/detail" target="_blank">details</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>


</body>
<%@include file="common/tail.jsp" %>
</html>
