<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/system/resources/permissions.js"></script>
<div style="padding:10px 0 10px 60px">
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="getChecked()">确定</a>
    </div>
    <ul id="permissions" class="easyui-tree" ></ul>
    <%--<ul id="tt" class="easyui-tree" data-options="url:'/json/tree_data1_test.json',method:'get',animate:true,checkbox:true,onLoadSuccess:success()"></ul>--%>
</div>
