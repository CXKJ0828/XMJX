<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/heattreat/list.js"></script>
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
<div data-options="region:'north'">
    <input style="display: none"  id="roleId" type="text" value="${user.roleId}" />
    <input style="display: none"  id="roleName" type="text" value="${user.roleName}" />
    <form id="form">
        <input style="display: none" id="origin" name="heattreatFormMap.origin" value="${origin}">
        <input style="display: none" id="remarks" name="heattreatFormMap.remarks" value="${remarks}">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <div id="makeGoodDiv" style="float: left">
            <a href="javascript:void(0)"  onclick="makeGood()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-redo'">生成成品</a>
        </div>
        <div id="makeHeattreatDiv" style="float: left">
            <a href="javascript:void(0)"  onclick="makeHeattreat()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-redo'">生成渗碳</a>
        </div>
        <div id="makeSendDiv" style="float: left">
            <a href="javascript:void(0)"  onclick="makeSend()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-redo'">生成发货</a>
        </div>
        <div id="makeMidfrequencyDiv" style="float: left">
            <a href="javascript:void(0)"  onclick="makeMidfrequency()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-redo'">生成中频</a>
        </div>
        <script>
            function makeSend(origin) {
                var rows=getDatagridSelections('tt');
                var ids="";
                for(i=0;i<rows.length;i++){
                    ids=ids+rows[i].id+",";
                }
                if(ids!=""){
                    communateGet(rootPath +'/heattreat/modifyIsSend.shtml?ids='+ids,function back(data){
                        if(data=='success'){
                            reloadDatagridMessage("tt");
                            showMessagerCenter("提示","操作成功");
                        }else{
                            showErrorAlert("操作",data);
                        }
                    });
                }else{
                    $.messager.alert('警告','不存在可以删除数据','warning');
                }
            }

            function makeHeattreat(origin) {
                var rows=getDatagridSelections('tt');
                var ids="";
                for(i=0;i<rows.length;i++){
                    ids=ids+rows[i].id+",";
                }
                if(ids!=""){
                    communateGet(rootPath +'/heattreat/makeHeartTreatEntity.shtml?ids='+ids,function back(data){
                        if(data=='success'){
                            reloadDatagridMessage("tt");
                            showMessagerCenter("提示","操作成功");
                        }else{
                            showErrorAlert("操作",data);
                        }
                    });
                }else{
                    $.messager.alert('警告','不存在可以删除数据','warning');
                }
            }
            function makeGood(origin) {
                var rows=getDatagridSelections('tt');
                var ids="";
                for(i=0;i<rows.length;i++){
                    ids=ids+rows[i].id+",";
                }
                if(ids!=""){
                    communateGet(rootPath +'/heattreat/makeGoodEntity.shtml?ids='+ids,function back(data){
                        if(data=='success'){
                            reloadDatagridMessage("tt");
                            showMessagerCenter("提示","操作成功");
                        }else{
                            showErrorAlert("操作",data);
                        }
                    });
                }else{
                    $.messager.alert('警告','不存在可以删除数据','warning');
                }
            }

            function makeMidfrequency(origin) {
                var rows=getDatagridSelections('tt');
                var ids="";
                for(i=0;i<rows.length;i++){
                    ids=ids+rows[i].id+",";
                }
                if(ids!=""){
                    communateGet(rootPath +'/heattreat/makeMidfrequencyEntity.shtml?ids='+ids,function back(data){
                        if(data=='success'){
                            reloadDatagridMessage("tt");
                            showMessagerCenter("提示","操作成功");
                        }else{
                            showErrorAlert("操作",data);
                        }
                    });
                }else{
                    $.messager.alert('警告','不存在可以删除数据','warning');
                }
            }
        </script>
        <a href="#" id="distribution" class="easyui-linkbutton" onclick="printDistributionEntity()" title="打印派工单" iconCls="icon-print" plain="true">打印派工单</a>
        <a href="#" id="drawing" class="easyui-linkbutton" onclick="printDrawingEntity()" title="打印图纸" iconCls="icon-print" plain="true">打印图纸</a>
        <a href="#" id="technology" class="easyui-linkbutton" onclick="pringTechnologyEntity()" title="打印工艺卡" iconCls="icon-print" plain="true">打印工艺卡</a>
        <a href="#" id="distributionOprate" class="easyui-linkbutton" onclick="distributionEntity()" title="分配" iconCls="icon-redo" plain="true">分配</a>
        <a href="#" class="easyui-linkbutton" onclick="selectAll()" title="全选" iconCls="icon-sum" plain="true">全选</a>
        <span id="all">合计:</span>
        <br>
        产品名称:
        <select class="easyui-combobox" id='goodName' name="heatTreatFormMap.goodName" style="width:80px;">
            <option value="不限"></option>
            <option value="轴">轴</option>
            <option value="套">套</option>
            <option value="垫/片">垫/片</option>
            <option value="其他">其他</option>
            <option value="不限">不限</option>
        </select>
        客户：
        <input  id="clientId" style="width:100px" name="heatTreatFormMap.clientId" class="inputShow">
        材质：
        <input  id="materialQuality" style="width:100px" name="heatTreatFormMap.materialQuality" class="inputShow">
        接收状态:
        <select class="easyui-combobox" id='getstate' name="blankFormMap.getstate" style="width:80px;">
            <option value="不限"></option>
            <option value="未接收">未接收</option>
            <option value="已接收">已接收未完成</option>
            <option value="已完成">已完成</option>
            <option value="不限">不限</option>
        </select>
        发货状态:
        <select class="easyui-combobox" id='sendstate' name="blankFormMap.sendstate" style="width:80px;">
            <option value="不限"></option>
            <option value="未发货">未发货</option>
            <option value="已发货">已发货</option>
            <option value="不限">不限</option>
        </select>
        <div id="divUserId" style="float: left">
            完成人:
            <select id="userId" style="width:200px;" class="easyui-combogrid" name="heatTreatFormMap.userId"  data-options="
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
                        fitColumns: true"></select>
        </div>
        <div id="isMakeCarburizationDiv" style="float: left">
            是否生成渗碳:
            <select class="easyui-combobox" id='isMakeCarburization' name="blankFormMap.isMakeCarburization" style="width:80px;">
                <option value="不限"></option>
                <option value="未生成">未生成</option>
                <option value="已生成">已生成</option>
                <option value="不限">不限</option>
            </select>
        </div>
        员工:
        <select id="oprateUserId" style="width:200px" class="easyui-combogrid" name="heatTreatFormMap.oprateUserId"  data-options="
                        panelWidth: 200,
                        idField: 'id',
                        textField: 'userName',
                        mode: 'remote',
                        url: '${pageContext.request.contextPath}/user/userSelectHeatTreat.shtml?origin='+'${origin}'+'&remarks='+'${remarks}',
                        method: 'get',
                        columns: [[
                            {field:'id',hidden:true,title:'编号',width:80},
                            {field:'accountName',title:'账号',width:120},
                            {field:'userName',title:'姓名',width:120},

                        ]],
                        fitColumns: true"></select>
        接收工序:
        <input id="oprateProcessId"  name="heatTreatFormMap.oprateProcessId"  style="width:80px" class="easyui-combobox" data-options="
                    valueField: 'value',
                    textField: 'text',
                     panelWidth: 80,
                    url: '${pageContext.request.contextPath}${processUrl}'">
        是否回料:
        <select class="easyui-combobox" id='state' name="blankFormMap.state" style="width:80px;">
            <option value="不限"></option>
            <option value="未回料">未回料</option>
            <option value="已回料">已回料</option>
            <option value="不限">不限</option>
        </select>
        是否分配:
        <select class="easyui-combobox" id='isDistribution' name="blankFormMap.isDistribution" style="width:80px;">
            <option value="不限"></option>
            <option value="未分配">未分配</option>
            <option value="已分配">已分配</option>
            <option value="不限">不限</option>
        </select>
        关键字:<input id="content" style="line-height:20px;border:1px solid #ccc;margin-left: 10px">
        领料日期:<input  id="pickTimeStart" value="${pickTimeStart}" name="heattreatFormMap.pickTimeStart" type="text">
        至
        <input id="pickTimeEnd" value="${pickTimeEnd}" name="heattreatFormMap.pickTimeEnd" type="text">
        回料日期:<input  id="backTimeStart" name="heattreatFormMap.backTimeStart" type="text">
        至
        <input id="backTimeEnd" name="heattreatFormMap.backTimeEnd" type="text">
        交货日期:<input  id="starttime" name="heattreatFormMap.starttime" type="text">
        至
        <input id="endtime" name="heattreatFormMap.endtime" type="text">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        <span style="font-size: 18px;font-weight: bold">分配数量合计:</span>
        <span style="font-size: 18px;font-weight: bold" id="distributionAmountSum"></span>
        <script>
            $('#materialQuality').combobox({
                url:'${pageContext.request.contextPath}/materialQualityType/materialQualityTypeSelect.shtml',
                valueField:'id',
                textField:'name',
                width:100
            });

            $('#clientId').combobox({
                url:'${pageContext.request.contextPath}/client/clientSelectTwoContent.shtml',
                valueField:'id',
                textField:'text',
                width:130
            });


            function distributionEntity() {
                var origin=getValueById("origin");
                var rows=getDatagridSelections('tt');
                var ids="";
                var length=rows.length;
                var isOprate=true;
                if(length==0){
                    showErrorAlert("提示","请选择分配内容");
                }else{
                    for(i=0;i<rows.length;i++){
                        var oprateState=rows[i].oprateState;
                        var id=rows[i].id;
                        ids=ids+id+",";
                        var backAmount=rows[i].backAmount;
                        var distributionAmount=rows[i].distributionAmount;
                        if(origin=='正火'
                            ||origin=='车后'
                            ||origin=='火前后'
                            ||origin=='铣槽后'
                            ||origin=='钳后情况'
                            ||origin=='外圆粗磨'
                            ||origin=='端面平磨'
                            ||origin=='消差磨'
                            ||origin=='统一尺寸'
                            ||origin=='内孔磨'
                            ||origin=='外圆精磨'
                            || origin=='外磨内磨端面'
                            || origin=='打磨'
                            ||origin=='渗碳'
                            ||origin=='调质'
                            ||origin=='中频'
                           ){
                            if(isNull(distributionAmount)||VarToFloat(distributionAmount)<=0){
                                isOprate=false;
                                i=rows.length;
                                showErrorAlert("提示","分配数量不可为空");
                            }else {
                                isOprate=true;
                            }
                        }else{
                            if(VarToFloat(backAmount)<=0){
                                isOprate=false;
                                i=rows.length;
                                showErrorAlert("提示","未回料不可进行分配操作");
                            }else if(isNull(distributionAmount)||VarToFloat(distributionAmount)<=0){
                                isOprate=false;
                                i=rows.length;
                                showErrorAlert("提示","分配数量不可为空");
                            }else if(VarToFloat(backAmount)<VarToFloat(distributionAmount)){
                                isOprate=false;
                                i=rows.length;
                                showErrorAlert("提示","分配数量不可大于回料数量");
                            }else {
                                isOprate=true;
                            }
                        }

                    }
                    if(isOprate){
                        $("#distributionWindow").window({
                            width: 400,
                            modal: true,
                            height: 300,
                            top:100,
                            href: rootPath + '/heattreat/distributionUI.shtml?ids='+ids+'&origin='+getValueById("origin")+"&source=热处理情况"+'&remarks='+getValueById("remarks"),
                            onClose:function () {
                                $('#tt').datagrid("reload");
                            }
                        });
                    }

                }
            }

            function selectAll() {
                //获取数据列表中的所有数据
                var rows = $("#tt").datagrid("getRows");
                //循环数据找出列表中ID和需要选中数据的ID相等的数据并选中
                for(var i=0;i<rows.length;i++){
                    var index = $("#tt").datagrid("getRowIndex",rows[i])
                    $("#tt").datagrid("checkRow",index);
                }
            }

            initInputDataInput('pickTimeStart');
            initInputDataInput('pickTimeEnd');
            initInputDataInput('backTimeStart');
            initInputDataInput('backTimeEnd');
            initInputDataInput('starttime');
            initInputDataInput('endtime');
        </script>
    </form>
</div>
<div data-options="region:'west'" style="width:220px;">
    <ul id="tt1"
        data-options="
            method:'get',animate:true"
    ></ul>
    <script>
        $('#tt1').tree({
            url:'${pageContext.request.contextPath}/heattreat/findClientByPage.shtml',
            onBeforeLoad: function (node,params) {
                var origin=getValueById('origin');
                params.origin=origin;
                var remarks=getValueById('remarks');
                params.remarks=remarks;
                var userId="";
                if(origin=='车后'
                    ||(origin=='渗碳'&&remarks=='外圆粗磨')
                    ||origin=='端面平磨'
                    ||origin=='消差磨'
                    ||origin=='统一尺寸'
                    ||origin=='内孔磨'
                    ||origin=='外圆精磨'){
                    userId=getContentBySelect("userId");
                }
                params.userId=userId;
                params.content=getValueById('content');
                params.pickTimeStart=getDataboxValue('pickTimeStart');
                params.pickTimeEnd=getDataboxValue('pickTimeEnd');
                params.backTimeStart=getDataboxValue('backTimeStart');
                params.backTimeEnd=getDataboxValue('backTimeEnd');
                params.goodName=getContentBySelect("goodName");
                params.clientId=getContentBySelect("clientId");
                params.materialQuality=getContentBySelect("materialQuality");
                params.oprateUserId=getContentBySelect("oprateUserId");
                params.getstate=getContentBySelect("getstate");
                params.sendstate=getContentBySelect("sendstate");
                params.state=getContentBySelect("state");
                params.isDistribution=getContentBySelect("isDistribution");
                params.oprateProcessId=getContentBySelect("oprateProcessId");
                params.startTime=getDataboxValue("starttime");
                params.endTime=getDataboxValue("endtime");
            },onBeforeSelect:function (node) {
                var clientId=node.id;
                setContentToCombobox("clientId",clientId);
                reloadDatagridMessage("tt");
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
</div>
<div data-options="region:'center'" style="height:200px;">
    <table id="tt"></table>
    <table id="ttselect" style="display: none"></table>
</div>
<div id="printOrder">

</div>
<div id="printDistribution">

</div>
<div id="printDrawing">

</div>
<div id="pringTechnology">

</div>
<div id="distributionWindow" title="分配">
</div>
<div id="progressWindow" class="easyui-window"
     data-options="modal:true,closed:true,iconCls:'icon-save'"
     title="进度详情"
     style="width:500px;height:200px;padding:10px;">
    <div id="progressContent"></div>
</div>
<div id="printShow">

</div>
<div id="rightMenu" class="easyui-menu" style="width: 50px; display: none;">
    <div   onclick="showProgress()">进度详情</div>
    <div   onclick="printShow('打印废品单')">打印废品单</div>
</div>
<script>
    function showProgress() {
        var goodId=heattreatEntity.goodId;
        var contractNumber=heattreatEntity.contractNumber;
        var deliveryTime=heattreatEntity.deliveryTime;
        var url=rootPath + '/blank/getProgressByContractNumberAndGoodId.shtml?goodId='+goodId+"&contractNumber="+contractNumber+"&deliveryTime="+deliveryTime;
        communateGet(url,function (data) {
            document.getElementById("progressContent").innerHTML=data[0].progress;
            // setContentToDivSpanById("progressContent",progress);
            $('#progressWindow').window('open');
        })
    }
    function printShow(origin) {
        $("#printShow").window({
            width:600,
            title:origin,
            modal: true,
            top:20,
            href: rootPath + '/heattreat/badprintorderShowUI.shtml?heattreatId='+heattreatEntity.id+"&origin="+origin+getValueById("origin"),
            onClose:function () {

            }
        });
    }
</script>
</body>