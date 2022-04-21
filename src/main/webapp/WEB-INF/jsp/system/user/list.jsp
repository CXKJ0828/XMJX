<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/user/list.js"></script>--%>
<style>

    body{
        padding:2px;
        margin: 0;
        border: 0;
        min-width: 1100px;
        box-sizing: border-box;
        font-size: 14px;
    }
    #fm{
        margin:0;
        padding:10px 30px;
    }
    .ftitle{
        font-size:14px;
        font-weight:bold;
        color:#666;
        padding:5px 0;
        margin-bottom:10px;
        border-bottom:1px solid #ccc;
    }
    .fitem{
        margin-bottom:5px;
    }
    .fitem label{
        display:inline-block;
        width:80px;
    }
</style>
<body>

<div style="width: auto">
    <div style="padding:10px 0 10px 60px">
        <ul id="tt" class="easyui-tree" ></ul>
        <%--<ul id="tt" class="easyui-tree" data-options="method:'get',animate:true,checkbox:true"></ul>--%>
    </div>
    <div style="text-align:center;padding:5px">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
    </div>
    <script>
        function clearForm() {
            $('#tt').tree({
                url:'${pageContext.request.contextPath}/json/tree_data1_test.json',
                method:'get',
                animate:true,
                checkbox:true
            });
        }
    </script>
    <%--<table id="tt"></table>--%>
    <%--<div id="add" title="添加">--%>
    <%--</div>--%>
    <%--<div id="tb" style="height:auto">--%>
        <%--<c:forEach items="${res}" var="key">--%>
            <%--${key.description}--%>
        <%--</c:forEach>--%>
    <%--</div>--%>
</div>
</body>