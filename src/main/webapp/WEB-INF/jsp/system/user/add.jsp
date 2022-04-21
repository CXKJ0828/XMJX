<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript" src="${ctx}/js/system/role/add.js"></script>
<div style="padding:10px 0 10px 60px">
    <form id="ff"  method="post">
        <table id="tt"></table>
    </form>
</div>
<div style="text-align:center;padding:5px">
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">确定</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
</div>