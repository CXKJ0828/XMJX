<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/heattreat/completelist.js"></script>
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
        url:'${pageContext.request.contextPath}/heattreat/findPostSearch.shtml?origin=已完成',
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
                var sureCompleteUserId=node.id;
                if(isContain(sureCompleteUserId,"确认完成人")){
                    setContentToCombogrid("sureCompleteUser",node.text);
                    setContentToInputById("user","");
                    setContentToCombogrid("userId","");
                }else{
                    setContentToInputById("user",node.text);
                    setContentToCombogrid("userId",node.id);
                    setContentToCombogrid("sureCompleteUser","");
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

    <div style="display: none">
        <table id="ttselect" style="width:auto;"></table>
    </div>
    <div id="tb" style="height:auto">
        <input type="text" style="display: none" id="clientId" >
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        确认完成人:
        <select id="sureCompleteUser" style="width:100px" class="easyui-combogrid"  data-options="
                        panelWidth: 200,
                        idField: 'userName',
                        textField: 'userName',
                        mode: 'remote',
                        url: '${pageContext.request.contextPath}/user/sureCompleteUserSelect.shtml',
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
        <input  id="starttimeGET" type="text">
        至
        <input id="endtimeGET" type="text">
        完成时间:
        <input  id="starttime" type="text">
        至
        <input id="endtime" type="text">
        是否超时:
        <select class="easyui-combobox" id='isOverTime'  style="width:80px;">
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
        <a href="#" class="easyui-linkbutton" onclick="print()" title="打印工资条" iconCls="icon-print" plain="true">打印工资条</a>
        <br>
        <input style="display: none" id="roleId" type="text" value="${user.roleId}" />
        <input style="display: none"  id="roleName" type="text" value="${user.roleName}" />
            <span id="allMoney">合计:</span>
        <span style="font-size: 18px;font-weight: bold"  id="amountSum"></span>
        <script>
            var roleId=getValueById("roleId");
            var roleName=getValueById("roleName");
            if(roleId=='16'||isContain(roleName,'班组')){
                getObjectById("allMoney").style.display="none";
            }
        </script>
    </div>
</div>
<div id="printOrder">

</div>
<script>

    function print() {
        var user=getValueById("user");
        var startTime=getDataboxValue("starttime");
        var endTime=getDataboxValue("endtime");
        $("#printOrder").window({
            width:500,
            title:'工资条',
            modal: true,
            top:50,
            href: rootPath + '/heattreat/wagesPrintUI.shtml?user='+user+"&startTime="+startTime+"&endTime="+endTime,
            onClose:function () {

            }
        });
    }

    var endtime=getNowDate();
    starttime=getNowMonth()+"-01";
    initInputDataInput("starttime");
    initInputDataInput("endtime");
    setDataToDateBox("starttime",starttime);
    setDataToDateBox("endtime",endtime);

    initInputDataInput("starttimeGET");
    initInputDataInput("endtimeGET");

    function selectClient(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            setContentToDatagridEditorText('tt',editId,"clientId",rowData.id);
            setContentToInputById("clientId",rowData.id);
            setContentToDatagridEditorText('tt',editId,"clientFullName",rowData.fullName);
            var ed=getDatagridEditorObj('tt',editId,'goodMapNumber');
            if(ed!=null){
                ed.combogrid('grid').datagrid('options').url =  '${pageContext.request.contextPath}/good/goodSelectByClientId.shtml?clientId='+getValueById('clientId');
                ed.combogrid('grid').datagrid('reload');
            }
        }
    }
    function selectGood(rowIndex, rowData) {
        if(checkIsSelectEd(editId)){
            setContentToDatagridEditorText('tt',editId,"goodId",rowData.id);
            setContentToDatagridEditorText('tt',editId,"goodMapNumber",rowData.mapNumber);
            setContentToDatagridEditorText('tt',editId,"goodName",rowData.name);
            setContentToDatagridEditorText('tt',editId,"goodSize",rowData.goodSize);
            setContentToDatagridEditorText('tt',editId,"materialQuality",rowData.materialQuality);
        }
    }
    function editAndAddSetContentAndChange() {
        setEditorOnChange("tt",0,function onChangeBack(index,content) {//完成数量
            var rows = $('#tt').datagrid('getRows');
            var badAmount=getContentByEditor("tt",1);
            var taxPrice=rows[editId].taxPrice;
            var artificial=rows[editId].artificial;
            var completeAmount=content;
            var wages = VarToFloat(completeAmount)*VarToFloat(artificial);
            setContentToDatagridEditorText("tt",editId,'wages',wages);
            var deductRate=getComboboxContentByEditor('tt','deductRate');
            var deductWages=VarToFloat(taxPrice)*VarToFloat(badAmount)*VarToFloat(deductRate);
            setContentToDatagridEditorText("tt",editId,'deductWages',deductWages);
            var trueWages=VarToFloat(wages)-VarToFloat(deductWages);
            trueWages=floatToVar2(trueWages);
            setContentToDatagridEditorText("tt",editId,'trueWages',trueWages);
        });
        setEditorOnChange("tt",1,function onChangeBack(index,content) {//废品数量
            var rows = $('#tt').datagrid('getRows');
            var badAmount=content;
            var taxPrice=rows[editId].taxPrice;
            var artificial=rows[editId].artificial;
            var completeAmount=getContentByEditor("tt",0);;
            var wages = VarToFloat(completeAmount)*VarToFloat(artificial);
            setContentToDatagridEditorText("tt",editId,'wages',wages);
            var deductRate=getComboboxContentByEditor('tt','deductRate');
            var deductWages=VarToFloat(taxPrice)*VarToFloat(badAmount)*VarToFloat(deductRate);
            setContentToDatagridEditorText("tt",editId,'deductWages',deductWages);
            var trueWages=VarToFloat(wages)-VarToFloat(deductWages);
            trueWages=floatToVar2(trueWages);
            setContentToDatagridEditorText("tt",editId,'trueWages',trueWages);
        });
        setEditorOnChange("tt",2,function onChangeBack(index,content) {
            var rows = $('#tt').datagrid('getRows');
            var badAmount=getContentByEditor("tt",1);
            var taxPrice=rows[editId].taxPrice;
            var deductRate=getComboboxContentByEditor('tt','deductRate');
            var deductWages=VarToFloat(taxPrice)*VarToFloat(badAmount)*VarToFloat(deductRate);
            setContentToDatagridEditorText("tt",editId,'deductWages',deductWages);
            var trueWages=VarToFloat(content)-VarToFloat(deductWages);
            trueWages=floatToVar2(trueWages);
            setContentToDatagridEditorText("tt",editId,'trueWages',trueWages);
        });

    }
</script>
<div id="add" title="保存" style="background: ghostwhite">
</div>
</body>