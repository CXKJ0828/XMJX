<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/employeeexport.js"></script>
<style>
    .tdShow{
        width:20%;
        text-align: left;
    }
    .spanShow{
        width: 30%!important;
    }
    .inputShow{
        width: 70%;
    }
</style>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'center'">
    <table id="tt" style="width:auto"></table>
</div>
<div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <input id="name" placeholder="请输入员工姓名" style="line-height:20px;border:1px solid #ccc;margin-left: 10px;margin-top:5px;width: 200px">
        <input  id="starttime" type="text">
        至
        <input id="endtime" type="text">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
</div>
</body>
<script>
    initInputDataInput("starttime");
    initInputDataInput("endtime");
</script>