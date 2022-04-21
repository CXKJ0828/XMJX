<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/heattreat/checklist.js"></script>
<body class="easyui-layout">
<div data-options="region:'south',split:true" style="height:auto;">
</div>
<div data-options="region:'west'" style="width:150px;">
    <ul id="tt1"></ul>
</div>
<script>
    var treeObjext=$('#tt1');
    var target="";
    $('#tt1').tree({
        url:'${pageContext.request.contextPath}/heattreat/findCheckPostSearch.shtml',
        onBeforeSelect:function (node) {
            var childrenNodes = treeObjext.tree('getChildren',node.target);
            var lengthChild=childrenNodes.length;
            var state=node.state;
            target=node.target;
            if(state=='closed'&&lengthChild==0){
                communateGet(rootPath +'/heattreat/findUserByPost.shtml?post='+node.text+"&isWarning=false",function back(data){
                    treeObjext.tree('append', {
                        parent: target,
                        data: data
                    });
                });
            }else{
                var checkUserId=node.id;
                if(isContain(checkUserId,"检验确认人")){
                    setContentToCombogrid("checkUser",node.text);
                    setContentToInputById("user","");
                    setContentToCombogrid("userId","");
                }else{
                    setContentToInputById("user",node.text);
                    setContentToCombogrid("userId",node.id);
                    setContentToCombogrid("checkUser","");
                }

                reloadDatagridMessage('tt');
            }

        },
        onSelect:function(node){
            var childrenNodes = treeObjext.tree('getChildren',node.target);
            var lengthChild=childrenNodes.length;
            if(lengthChild>0){
                $(this).tree(node.state === 'closed' ? 'expand' : 'collapse', node.target);
                node.state = node.state === 'closed' ? 'open' : 'closed';
            }
        },
    });
</script>
<div data-options="region:'center'" style="height:150px;">
    <table id="tt" data-options="fit:true"  style="width:auto"></table>
    <div id="tb" style="height:auto">
        <input type="text" style="display: none" id="clientId" >
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        检验员:
        <select id="checkUser" style="width:100px" class="easyui-combogrid"  data-options="
                        panelWidth: 200,
                        idField: 'userName',
                        textField: 'userName',
                        mode: 'remote',
                        url: '${pageContext.request.contextPath}/user/checkUserSelect.shtml',
                        method: 'get',
                        columns: [[
                            {field:'id',hidden:true,title:'编号',width:80},
                            {field:'accountName',title:'账号',width:120},
                            {field:'userName',title:'姓名',width:120},

                        ]],
                        fitColumns: true,
                        onSelect:function(rowIndex, rowData){
                        }"></select>
        员工:
        <select id="userId" style="width:100px" class="easyui-combogrid"  data-options="
                        panelWidth: 200,
                        idField: 'id',
                        textField: 'userName',
                        mode: 'remote',
                        url: '${pageContext.request.contextPath}/user/userSelect.shtml',
                        method: 'get',
                        columns: [[
                            {field:'id',hidden:true,title:'编号',width:80},
                            {field:'accountName',title:'账号',width:120},
                            {field:'userName',title:'姓名',width:120},

                        ]],
                        fitColumns: true,
                        onSelect:function(rowIndex, rowData){
                           setContentToInputById('user',rowData.userName);
                        }"></select>
        <input id="user" placeholder="请输入员工姓名" style="display: none">
        <input id="content" placeholder="请输入关键字" style="line-height:20px;border:1px solid #ccc;margin-left: 10px;margin-top:5px;width: 100px">
        接收时间:
        <input  id="startTimeGET" type="text">
        至
        <input id="endTimeGET" type="text">
        检验时间:
        <input  id="startCheckTime" type="text">
        至
        <input id="endCheckTime" type="text">
        是否检验:
        <select class="easyui-combobox" id='isCheck'  style="width:80px;">
            <option value="不限"></option>
            <option value="否">否</option>
            <option value="是">是</option>
            <option value="不限">不限</option>
        </select>
        产品名称:
        <select class="easyui-combobox" id='goodName'  style="width:80px;">
            <option value="不限"></option>
            <option value="轴">轴</option>
            <option value="套">套</option>
            <option value="垫/片">垫/片</option>
            <option value="其他">其他</option>
            <option value="不限">不限</option>
        </select>
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <a href="#" class="easyui-linkbutton" onclick="selectAll()" title="全选" iconCls="icon-sum" plain="true">全选</a>
        <a href="#" class="easyui-linkbutton" onclick="clearselectAll()" title="取消全选" iconCls="icon-undo" plain="true">取消全选</a>
        <a href="#" class="easyui-linkbutton" onclick="deleteEntity()" title="删除" iconCls="icon-cancel" plain="true">删除</a>
        <span id="allMoney">合计:</span>
        <span style="font-size: 18px;font-weight: bold"  id="amountSum"></span>

    </div>
</div>

<script>
    function clearselectAll() {
        amountSum=0;
        badAmountSum=0;
        setContentToDivSpanById("amountSum","检验数量合计:"+amountSum+"废品数量合计:"+badAmountSum);
        clearDatagridSelections('tt');
    }
    function selectAll() {
        amountSum=0;
        badAmountSum=0;
        //获取数据列表中的所有数据
        var rows = $("#tt").datagrid("getRows");
        //循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
        for(var i=0;i<rows.length;i++){
            var index = $("#tt").datagrid("getRowIndex",rows[i])
            $("#tt").datagrid("checkRow",index);
        }
    }

    function deleteEntity() {
        var rows=getDatagridSelections('tt');
        var ids="";
        for(i=0;i<rows.length;i++){
            ids=ids+rows[i].id+",";
        }
        if(ids!=""){
            $.messager.prompt('提示', '请输入删除操作所需密码', function(r){
                if (r){
                    communateGet(rootPath +'/heattreat/deleteCheckEntity.shtml?ids='+ids+'&password='+r,function back(data){
                        if(data=='success'){
                            reloadDatagridMessage('tt');
                        }else{
                            showErrorAlert("操作",data);
                        }
                    });
                }
            });
        }else{
            $.messager.alert('警告','不存在可以删除数据','warning');
        }
    }


    var endTimeGET=getNowDate();
    var startTimeGET=getNowMonth()+"-01";
    initInputDataInput("startCheckTime");
    initInputDataInput("endCheckTime");

    initInputDataInput("startTimeGET");
    initInputDataInput("endTimeGET");
    setDataToDateBox("startTimeGET",startTimeGET);
    setDataToDateBox("endTimeGET",endTimeGET);


</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>