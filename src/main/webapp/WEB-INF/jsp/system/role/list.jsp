<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/role/list.js"></script>
<style>

    body{
        padding:2px;
        margin: 0;
        border: 0;
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
<body data-options="fit:true">
    <table id="tt"  ></table>
<div id="add" title="添加">
</div>
<div id="tb" style="height:auto">
    <c:forEach items="${res}" var="key">
        ${key.description}
    </c:forEach>
    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-tip',plain:true" onclick="team()">班组分配</a>
    <a href="javascript:void(0)" onclick="process()" style="display: none" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-tip'">分配工序</a>
</div>
<ul id="permission" class="easyui-tree" ></ul>
    <div id="process" title="">
    </div>
    <div id="rightMenuWatch" class="easyui-menu" style="width: 50px;">    
        <div  data-options="iconCls:'shape_square_go'" onclick="watch()">查看工序</div>         
    </div>
</body>
<script>
    function team() {
        var selectList=$('#tt').datagrid('getChecked');
        if(selectList.length!=1){
            $.messager.alert('警告','请选择一个角色','warning');
        }else{
            $("#process").window({
                width: 600,
                modal: true,
                title:'班服分配',
                height: 200,
                top:50,
                href: rootPath + '/role/teamRoleShowUI.shtml?roleId='+selectList[0].id,
                onClose:function () {
                }
            });
        }
    }
    function watch() {
       var row=getDatagridJsonByIndexAndId('tt',editId);
        $("#process").window({
            width: 600,
            modal: true,
            title:'工序查看',
            height: 400,
            top:50,
            href: rootPath + '/role/processRoleShowUI.shtml?roleId='+row.id,
            onClose:function () {
            }
        });
    }
</script>