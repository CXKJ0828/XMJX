<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/technologylist.js"></script>
<body class="easyui-layout">
<div data-options="region:'north'">
    <form id="form" name="form"  method="post" enctype="multipart/form-data">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
        <input style="display: none"  type="text" id="orderdetailsId">
        <input id="contractNumber" placeholder="请输入订单号" style="line-height:20px;border:1px solid #ccc;margin-left: 10px;width: 120px">
        <input id="content" placeholder="请输入图号" style="line-height:20px;border:1px solid #ccc;margin-left: 10px;width: 120px">
        交货日期:
        <input  id="starttime" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
        至
        <input id="endtime" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </form>
</div>

<div data-options="region:'west'" style="width:220px;">
    <ul id="tt1"
        data-options="
            method:'get',animate:true"
    ></ul>
</div>
<script>
    initInputDataInput("starttime");
    initInputDataInput("endtime");

    var treeObjext=$('#tt1');
    function find() {
        communateGet(rootPath + '/blank/findTechnologylist.shtml?content='+getValueById("content")
            +"&startTime="+getDataboxValue("starttime")
            +"&endTime="+getDataboxValue("endtime")
            +"&contractNumber="+getValueById("contractNumber")
            ,function (data) {
                treeObjext.tree({data: data});
            })
    }

    communateGet(rootPath + '/blank/findTechnologylist.shtml?content='+getValueById("content")+"&contractNumber="+getValueById("contractNumber")
        ,function (data) {
            treeObjext.tree({data: data});
        })


    treeObjext.tree({
        onBeforeSelect:function (node) {
            var childrenNodes = treeObjext.tree('getChildren',node.target);
            var lengthChild=childrenNodes.length;
            var state=node.state;
            if(state=='closed'&&lengthChild==0){
                if(isContain(node.id,":客户")){
                    var clientId=node.id.replace(":客户","");
                    communateGet(rootPath + '/blank/findTechnologylistByClientId.shtml?content='+getValueById("content")
                        +"&startTime="+getDataboxValue("starttime")
                        +"&endTime="+getDataboxValue("endtime")
                        +"&contractNumber="+getValueById("contractNumber")
                        +"&clientId="+clientId,function (data) {
                        treeObjext.tree('append', {
                            parent: node.target,
                            data: data
                        });
                    })
                }
                if(isContain(node.id,":订单")){
                    orderId=node.id.replace(":订单","");
                    communateGet(rootPath + '/blank/findTechnologylistByOrderId.shtml?content='+getValueById("content")
                        +"&startTime="+getDataboxValue("starttime")
                        +"&endTime="+getDataboxValue("endtime")
                        +"&orderId="+orderId,function (data) {
                        treeObjext.tree('append', {
                            parent: node.target,
                            data: data
                        });
                        var orderdetailsId=data[0].id.replace("订单明细","");
                        setContentToInputById("orderdetailsId",orderdetailsId);
                    })
                    var url=rootPath+'/order/findUnProductByClientId.shtml?orderId='+orderId
                        +"&startTime="+getDataboxValue("starttime")
                        +"&endTime="+getDataboxValue("endtime")
                        +"&contractNumber="+getValueById("contractNumber")
                        +"&mapNumber="+getValueById("content");
                    $('#orderSituation').datagrid('options').url=url;
                    $("#orderSituation").datagrid('reload');
                }
            }
            if(isContain(node.id,"订单明细")){
                var orderdetailsId=node.id.replace("订单明细","");
                setContentToInputById("orderdetailsId",orderdetailsId);
                //订单情况
                var url=rootPath+'/order/findUnProductByClientId.shtml?orderId='+orderId
                    +"&startTime="+getDataboxValue("starttime")
                    +"&endTime="+getDataboxValue("endtime")
                    +"&contractNumber="+getValueById("contractNumber")
                    +"&mapNumber="+getValueById("content")
                    +"&orderdetailsId="+orderdetailsId;
                $('#orderSituation').datagrid('options').url=url;
                $("#orderSituation").datagrid('reload');


                var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                $('#goodprocessList').datagrid('options').url=url;
                $("#goodprocessList").datagrid('reload');

                var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                $('#feedgoodprocessList').datagrid('options').url=url;
                $("#feedgoodprocessList").datagrid('reload');

                var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                communateGet(url,function (data) {
                    img=data.img;
                    setContentToDivSpanById("mapNumber",data.mapNumber);
                    setContentToDivSpanById("name",data.goodName);
                    setContentToDivSpanById("materialQuality",data.materialQualityName);
                    setContentToDivSpanById("weight",data.weight);
                    setContentToDivSpanById("deliveryTime",data.deliveryTime);
                    setContentToDivSpanById("state",data.state);
                    setContentToInputById("goodId",data.id);
                })
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
        onClick:function (node) {
                if(isContain(node.id,":订单")){
                    orderId=node.id.replace(":订单","");
                    var url=rootPath+'/order/findUnProductByClientId.shtml?orderId='+orderId
                        +"&startTime="+getDataboxValue("starttime")
                        +"&endTime="+getDataboxValue("endtime")
                        +"&contractNumber="+getValueById("contractNumber")
                        +"&mapNumber="+getValueById("content");
                    $('#orderSituation').datagrid('options').url=url;
                    $("#orderSituation").datagrid('reload');

                }
        }
    });
</script>
<div data-options="region:'center'" >
    <div style="height: 30%">
        <table id="orderSituation"
               class="easyui-datagrid"
               style="width:auto;height:300px"
               data-options="rownumbers:true,
                    title:'订单情况',
                     singleSelect:true,
                     fit:true,
            fitColumns:true,
 pagination:true,
onClickRow:onOrderSituationClickRow,
  onLoadSuccess:onOrderSituationSuccess,">
            <thead>
            <tr>
                <th data-options="field:'id',hidden:true,width:100,align:'center'">编号</th>
                <th data-options="field:'contractNumber',width:200,align:'center'">订单编号</th>
                <th data-options="field:'mapNumber',width:200,align:'center'">图号</th>
                <th data-options="field:'goodName',width:200,align:'center'">产品名称</th>
                <th data-options="field:'makeTime',width:200,align:'center'">来单日期</th>
                <th data-options="field:'deliveryTime',width:200,align:'center'">交货日期</th>
                <th data-options="field:'amount',width:200,align:'center'">订单数量</th>
                <th data-options="field:'orderSituation',width:300,align:'center', formatter: function(value,row,index){
                    var orderdetailsId=row.id;
                    var url=rootPath+'/order/findSituationByOrderDetailsId.shtml?orderdetailsId='+orderdetailsId;
                       var htmlObj = $.ajax({
		                url : url,
                        async : false,
                        data : {id:value}
                        });
                    var text = htmlObj.responseText;
                    if(text==null||text==''){
                        return '';
                    }else{
                     var json = JSON.parse(text);
                    return json;
                    }

            }">订单情况</th>
                <th data-options="field:'alreadysendAmount',width:200,align:'center',editor:'text'">已发货数量</th>
                <th data-options="field:'unsendAmount',width:200,align:'center',editor:'text'">未发货数量</th>
                <th data-options="field:'stockAmount',width:200,align:'center',editor:'text'">库存数量</th>
                <th data-options="field:'sendTime',width:200,align:'center'">录入时间</th>
                <th data-options="field:'state',width:100,align:'center'">状态</th>
                <th data-options="field:'goodId',hidden:true,width:200,align:'center'">产品id</th>
            </tr>
            </thead>
        </table>
    </div>
    <input style="display: none" type="text" id="goodId">
    <script>
        function onOrderSituationSuccess(data){
            $('#orderSituation').datagrid('selectRow',0);
            orderdetailsId=ListToJsonString(data.rows[0].id);
            setContentToInputById("orderdetailsId",orderdetailsId);
            var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单"
            $('#goodprocessList').datagrid('options').url=url;
            $("#goodprocessList").datagrid('reload');
            var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
            communateGet(url,function (data) {
                img=data.img;
                setContentToDivSpanById("mapNumber",data.mapNumber);
                setContentToDivSpanById("name",data.goodName);
                setContentToDivSpanById("materialQuality",data.materialQualityName);
                setContentToDivSpanById("weight",data.weight);
                setContentToDivSpanById("deliveryTime",data.deliveryTime);
                setContentToDivSpanById("state",data.state);
                setContentToInputById("goodId",data.id);
            })
        }

        function onOrderSituationClickRow(rowIndex, rowData) {
            orderdetailsId=rowData.id;
            setContentToInputById("orderdetailsId",orderdetailsId);
            var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId;
            $('#goodprocessList').datagrid('options').url=url;
            $("#goodprocessList").datagrid('reload');
            var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
            communateGet(url,function (data) {
                img=data.img;
                setContentToDivSpanById("mapNumber",data.mapNumber);
                setContentToDivSpanById("name",data.goodName);
                setContentToDivSpanById("materialQuality",data.materialQualityName);
                setContentToDivSpanById("weight",data.weight);
                setContentToDivSpanById("deliveryTime",data.deliveryTime);
                setContentToDivSpanById("state",data.state);
                setContentToInputById("goodId",data.id);
            })
        }

        function showImg() {
            if(img==undefined){
                showErrorAlert("提示","暂无图纸");
            }else{
                var goodId=getValueById("goodId");
                window.open(rootPath + '/good/showImgUI.shtml?goodId='+goodId);
            }

        }
    </script>

    <input style="display: none;" type="text" value="${dailyWages}" id="dailyWages">
    <div style="height:69%">
        <div  style="height:80%">
            <div class="easyui-tabs" data-options="fit:true"   id="show">
                <div title="工艺信息"  style="padding:1px">
                    <table id="goodprocessList"
                           class="easyui-datagrid"
                           data-options="
                           rownumbers:true,
                     nowrap:false,
                   toolbar:'#tboprate',
                   fit:true,
            fitColumns:true,
            singleSelect:true">
                        <thead>
                        <tr>
                            <th data-options="field:'processId',hidden:true,width:40,align:'center',editor:'text'">工序id</th>
                            <th data-options="field:'number',width:40,align:'center',editor:'text'">工序号</th>
                            <th class="easyui-combogrid" data-options="field:'processName',width:100,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 200,
			idField: 'name',
			textField: 'name',
			url: '${pageContext.request.contextPath}/process/processSelect.shtml',
			mode: 'remote',
			columns: [[
				{field:'id',hidden:true,title:'编码',width:150,align:'center'},
               {field:'name',title:'名称',width:300,align:'center'},
                {field:'artificial',hidden:true,title:'人工',width:150,align:'center'},
                {field:'remark',hidden:true,title:'备注',width:150,align:'center'},
                {field:'isMust',hidden:true,title:'是否必须',width:150,align:'center'},
                {field:'multiple',hidden:true,title:'倍数',width:150,align:'center'},
			]],
			fitColumns: true,
			onSelect:selectProcess
			}
		}">工序</th>
                            <th data-options="field:'content',width:150,align:'center',editor:'text'">加工内容</th>
                            <th data-options="field:'amount',width:40,align:'center',editor:'text'">数量</th>
                            <th data-options="field:'unreceiveAmount',width:80,align:'center',editor:'text'">未接收数量</th>
                            <th data-options="field:'planneedDay',width:70,align:'center',editor:'text'">预计用时(天)</th>
                            <th data-options="field:'remarks',width:40,align:'center',editor:'text'">备注</th>
                            <th data-options="field:'state',width:40,align:'center',editor:'text',formatter: function(value,row,index){
            if (row.state=='已完成'){
                completeAmount=row.completeAmount;
                return '已完成:'+completeAmount;

            } else {
                return row.state;
            }
        }">状态</th>
                            <th data-options="field:'artificial',hidden:true,width:40,align:'center',editor:'text'">用时</th>
                            <th data-options="field:'badAmount',width:40,hidden:true,align:'center',editor:'text'">废品数量</th>
                            <th data-options="field:'completeAmount',hidden:true,width:40,align:'center',editor:'text'">完成数量</th>
                            <th data-options="field:'blankId',hidden:true,width:40,align:'center',editor:'text'">blankId</th>
                            <th data-options="field:'goodprocessId',hidden:true,width:150,align:'center',editor:'text'">goodprocessId</th>
                        </tr>
                        </thead>
                    </table>
                </div>
                <div title="补料工艺信息" style="padding:1px">
                    <table id="feedgoodprocessList"
                           class="easyui-datagrid"
                           data-options="rownumbers:true,
                     nowrap:false,
                     fit:true,
                   toolbar:'#tbopratefeed',
            fitColumns:true,
            singleSelect:true">
                        <thead>
                        <tr>
                            <th data-options="field:'processId',hidden:true,width:40,align:'center',editor:'text'">工序id</th>
                            <th data-options="field:'number',width:40,align:'center',editor:'text'">工序号</th>
                            <th class="easyui-combogrid" data-options="field:'processName',width:100,align:'center',editor:{type:'combogrid',
            options: {panelWidth: 200,
			idField: 'name',
			textField: 'name',
			url: '${pageContext.request.contextPath}/process/processSelect.shtml',
			mode: 'remote',
			columns: [[
				{field:'id',hidden:true,title:'编码',width:150,align:'center'},
               {field:'name',title:'名称',width:300,align:'center'},
                {field:'artificial',hidden:true,title:'人工',width:150,align:'center'},
                {field:'remark',hidden:true,title:'备注',width:150,align:'center'},
                {field:'isMust',hidden:true,title:'是否必须',width:150,align:'center'},
                {field:'multiple',hidden:true,title:'倍数',width:150,align:'center'},
			]],
			fitColumns: true,
			onSelect:selectProcess
			}
		}">工序</th>
                            <th data-options="field:'content',width:150,align:'center',editor:'text'">加工内容</th>
                            <th data-options="field:'amount',width:40,align:'center',editor:'text'">数量</th>
                            <th data-options="field:'unreceiveAmount',width:80,align:'center',editor:'text'">未接收数量</th>
                            <th data-options="field:'planneedDay',width:70,align:'center',editor:'text'">预计用时(天)</th>
                            <th data-options="field:'remarks',width:40,align:'center',editor:'text'">备注</th>
                            <th data-options="field:'state',width:40,align:'center',editor:'text'">状态</th>
                            <th data-options="field:'batchNumber',width:40,align:'center'">补料批次</th>
                            <th data-options="field:'artificial',hidden:true,width:40,align:'center',editor:'text'">用时</th>
                            <th data-options="field:'badAmount',width:40,hidden:true,align:'center',editor:'text'">废品数量</th>
                            <th data-options="field:'completeAmount',hidden:true,width:40,align:'center',editor:'text'">完成数量</th>
                            <th data-options="field:'blankId',hidden:true,width:40,align:'center',editor:'text'">blankId</th>
                            <th data-options="field:'goodprocessId',hidden:true,width:150,align:'center',editor:'text'">goodprocessId</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
        <div  style="height:20%">
            <table id="workersubmitList"
                   class="easyui-datagrid"
                   style="width:auto;height:auto"
                   data-options="rownumbers:true,
              title:'接收情况',
               toolbar:'#tbsubmiter',

            fitColumns:true">
                <thead>
                <tr>
                    <th data-options="field:'id',width:70,align:'center'">编码</th>
                    <th data-options="field:'userName',width:40,align:'center'">接收人</th>
                    <th data-options="field:'startTime',width:70,align:'center'">接收时间</th>
                    <th data-options="field:'amount',width:40,align:'center'">接收数量</th>
                    <th data-options="field:'completeTime',width:70,align:'center'">完成时间</th>
                    <th data-options="field:'completeAmount',width:40,align:'center',editor:'text'">完成数量</th>
                    <th data-options="field:'badAmount',width:40,align:'center',editor:'text'">废品数</th>
                    <th data-options="field:'deductRate',align:'center',width:50,
						editor:{
							type:'combobox',
							options:{
								valueField:'value',
								textField:'text',
								url:'${pageContext.request.contextPath}/json/deductRate.json',
							}
						}">扣除比例</th>
                </tr>
                </thead>
            </table>
        </div>


        <script>
            $('#show').tabs({
                border:false,
                onSelect:function(title,index){
                    orderdetailsId=getValueById("orderdetailsId");
                    if(orderdetailsId!=null&&orderdetailsId!=""){
                        if(title=='工艺信息'){
                            orderdetailsId=getValueById("orderdetailsId");
                            var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                            $('#goodprocessList').datagrid('options').url=url;
                            $("#goodprocessList").datagrid('reload');

                            var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                            communateGet(url,function (data) {
                                img=data.img;
                                setContentToDivSpanById("mapNumber",data.mapNumber);
                                setContentToDivSpanById("name",data.goodName);
                                setContentToDivSpanById("materialQuality",data.materialQualityName);
                                setContentToDivSpanById("weight",data.weight);
                                setContentToDivSpanById("deliveryTime",data.deliveryTime);
                                setContentToDivSpanById("state",data.state);
                                setContentToInputById("goodId",data.id);
                            })
                        }else{
                            orderdetailsId=getValueById("orderdetailsId");
                            var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                            $('#feedgoodprocessList').datagrid('options').url=url;
                            $("#feedgoodprocessList").datagrid('reload');

                            var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                            communateGet(url,function (data) {
                                img=data.img;
                                setContentToDivSpanById("mapNumber",data.mapNumber);
                                setContentToDivSpanById("name",data.goodName);
                                setContentToDivSpanById("materialQuality",data.materialQualityName);
                                setContentToDivSpanById("weight",data.weight);
                                setContentToDivSpanById("deliveryTime",data.deliveryTime);
                                setContentToDivSpanById("state",data.state);
                                setContentToInputById("goodId",data.id);
                            })
                        }
                    }

                }
            });

            initComboGridEditor();

            function selectProcessFeed(rowIndex, rowData) {
                if(checkIsSelectEd(goodprocesseditId)){
                    var rows=getDatagridRows("goodprocessList");
                    var rowsStr=ListToJsonString(rows);
                    var processName=rowData.name;
                    var checkContent='"processName":"'+processName+'"';
                    if(isContain(rowsStr,checkContent)){
                        showMessagerCenter("提示","不能选择已存在的工序");
                    }else{
                        setContentToDatagridEditorText('feedgoodprocessList',goodprocesseditId,"processId",rowData.id);
                        setContentToDatagridEditorText('feedgoodprocessList',goodprocesseditId,"processName",rowData.name);
                    }


                }
            }

            function selectProcess(rowIndex, rowData) {
                if(checkIsSelectEd(goodprocesseditId)){
                    var rows=getDatagridRows("goodprocessList");
                    var rowsStr=ListToJsonString(rows);
                    var processName=rowData.name;
                    var checkContent='"processName":"'+processName+'"';
                    if(isContain(rowsStr,checkContent)){
                        showMessagerCenter("提示","不能选择已存在的工序");
                    }else{
                        setContentToDatagridEditorText('goodprocessList',goodprocesseditId,"processId",rowData.id);
                        setContentToDatagridEditorText('goodprocessList',goodprocesseditId,"processName",rowData.name);
                    }


                }
            }

            function  completeEntity() {
                showconfirmDialog("提示","是否确认完成？",function (back) {
                    if(back){
                        orderdetailsId=getValueById("orderdetailsId");
                        communateGet(rootPath +'/blank/completeEntity.shtml?orderdetailsId='+orderdetailsId+'&origin=订单',function back(data){
                            orderdetailsId=getValueById("orderdetailsId");
                            var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                            communateGet(url,function (data) {
                                img=data.img;
                                setContentToDivSpanById("mapNumber",data.mapNumber);
                                setContentToDivSpanById("name",data.goodName);
                                setContentToDivSpanById("materialQuality",data.materialQualityName);
                                setContentToDivSpanById("weight",data.weight);
                                setContentToDivSpanById("deliveryTime",data.deliveryTime);
                                setContentToDivSpanById("state",data.state);
                                setContentToInputById("goodId",data.id);
                            })
                        });
                    }
                })
            }

            function  completeFeedEntity() {
                showconfirmDialog("提示","是否确认完成？",function (back) {
                    if(back){
                        orderdetailsId=getValueById("orderdetailsId");
                        communateGet(rootPath +'/blank/completeEntity.shtml?orderdetailsId='+orderdetailsId+'&origin=补料',function back(data){
                            orderdetailsId=getValueById("orderdetailsId");
                            var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                            communateGet(url,function (data) {
                                img=data.img;
                                setContentToInputById("goodId",data.id);
                                setContentToDivSpanById("mapNumberFeed",data.mapNumber);
                                setContentToDivSpanById("nameFeed",data.goodName);
                                setContentToDivSpanById("materialQualityFeed",data.materialQualityName);
                                setContentToDivSpanById("weightFeed",data.weight);
                                setContentToDivSpanById("deliveryTimeFeed",data.deliveryTime);
                                setContentToDivSpanById("stateFeed",data.state);
                            })
                        });
                    }
                })
            }

            function  inputEntity() {
                showInputDialog("提示","请输入入库数量",function (input) {
                    if(input!=undefined){
                        if(input==''||input=='0'){
                            showErrorAlert("提示",'请输入入库数量');
                        }else{
                            orderdetailsId=getValueById("orderdetailsId");
                            communateGet(rootPath +'/blank/inputEntity.shtml?orderdetailsId='+orderdetailsId+"&amount="+input,function back(data){
                                orderdetailsId=getValueById("orderdetailsId");
                                var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId;
                                $('#goodprocessList').datagrid('options').url=url;
                                $("#goodprocessList").datagrid('reload');

                                var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                                communateGet(url,function (data) {
                                    img=data.img;
                                    setContentToDivSpanById("mapNumber",data.mapNumber);
                                    setContentToDivSpanById("name",data.goodName);
                                    setContentToDivSpanById("materialQuality",data.materialQualityName);
                                    setContentToDivSpanById("weight",data.weight);
                                    setContentToDivSpanById("deliveryTime",data.deliveryTime);
                                    setContentToDivSpanById("state",data.state);
                                    setContentToInputById("goodId",data.id);
                                })
                            });
                        }

                    }else{

                    }

                })
            }
            function  inputFeedEntity() {
                showInputDialog("提示","请输入入库数量",function (input) {
                    if(input!=undefined){
                        if(input==''||input=='0'){
                            showErrorAlert("提示",'请输入入库数量');
                        }else{
                            orderdetailsId=getValueById("orderdetailsId");
                            communateGet(rootPath +'/blank/inputEntity.shtml?orderdetailsId='+orderdetailsId+"&amount="+input,function back(data){
                                orderdetailsId=getValueById("orderdetailsId");
                                var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                                $('#feedgoodprocessList').datagrid('options').url=url;
                                $("#feedgoodprocessList").datagrid('reload');

                                var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                                communateGet(url,function (data) {
                                    setContentToDivSpanById("mapNumberFeed",data.mapNumber);
                                    setContentToDivSpanById("nameFeed",data.name);
                                    setContentToDivSpanById("materialQualityFeed",data.materialQualityName);
                                    setContentToDivSpanById("weightFeed",data.weight);
                                    setContentToDivSpanById("stateFeed",data.state);
                                })
                            });
                        }

                    }else{

                    }

                })
            }

            function saveEntity() {
                endEditDatagridByIndex('goodprocessList',goodprocesseditId);
                var rows= $('#goodprocessList').datagrid('getChanges');
                var url='${pageContext.request.contextPath}/blank/editTechnologyEntity.shtml';
                var data={
                    rows:rows,
                    goodId:getValueById("goodId")
                }
                communatePost(url,ListToJsonString(data),function back(data){
                    if(isContain(data,'error')){
                        showErrorAlert("警告",data);
                    }

                    orderdetailsId=getValueById("orderdetailsId");
                    var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                    $('#goodprocessList').datagrid('options').url=url;
                    $("#goodprocessList").datagrid('reload');
                })
            }

            function saveFeedEntity() {
                endEditDatagridByIndex('feedgoodprocessList',goodprocesseditId);
                var rows= $('#feedgoodprocessList').datagrid('getChanges');
                var url='${pageContext.request.contextPath}/blank/editTechnologyEntity.shtml';
                var data={
                    rows:rows,
                }
                communatePost(url,ListToJsonString(data),function back(data){
                    orderdetailsId=getValueById("orderdetailsId");
                    var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                    $('#feedgoodprocessList').datagrid('options').url=url;
                    $("#feedgoodprocessList").datagrid('reload');
                })
            }


            function saveWorkerSubmitEntity() {
                endEditDatagridByIndex('workersubmitList',goodprocesseditId);
                var rows= $('#workersubmitList').datagrid('getChanges');
                var url='${pageContext.request.contextPath}/blank/editWorkerSubmitEntity.shtml';
                var data={
                    rows:rows,
                }
                communatePost(url,ListToJsonString(data),function back(data){
                    var url=rootPath+'/blank/findWorkersubmitByBlankprocessId.shtml?blankprocessId='+blankprocessId;
                    $('#workersubmitList').datagrid('options').url=url;
                    $("#workersubmitList").datagrid('reload');
                })
            }
        </script>
    </div>

    <div id="tboprate" style="height:auto">
        <table>
            <tr>
                <td>图号:<span id="mapNumber"></span></td>
                <td>名称:<span id="name"></span></td>
                <td>材质:<span id="materialQuality"></span></td>
                <td>重量:<span id="weight"></span></td>
                <td>交货日期:<span id="deliveryTime"></span></td>
                <td>状态:<span id="state"></span></td>
                <td><a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="showImg()">查看图纸</a></td>
            </tr>
        </table>
        <a href="#" class="easyui-linkbutton" onclick="saveEntity()" title="保存" iconCls="icon-save" plain="true">保存</a>
        <a href="#" class="easyui-linkbutton" onclick="inputEntity()" title="入库" iconCls="icon-redo" plain="true">入库</a>
        <a href="#" class="easyui-linkbutton" onclick="refreshEntity()" title="刷新" iconCls="icon-reload" plain="true">刷新</a>
        <a href="#" class="easyui-linkbutton" onclick="cancelEntity()" title="取消" iconCls="icon-undo" plain="true">取消</a>
        <a href="#" class="easyui-linkbutton" onclick="completeEntity()" title="确认完成" iconCls="icon-ok" plain="true">确认完成</a>
    </div>
    <div id="tbopratefeed" style="height:auto">
        <table>
            <tr>
                <td>图号:<span id="mapNumberFeed"></span></td>
                <td>名称:<span id="nameFeed"></span></td>
                <td>材质:<span id="materialQualityFeed"></span></td>
                <td>重量:<span id="weightFeed"></span></td>
                <td>交货日期:<span id="deliveryTimeFeed"></span></td>
                <td>状态:<span id="stateFeed"></span></td>
                <td><a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="showImg()">查看图纸</a></td>
            </tr>
        </table>
        <a href="#" class="easyui-linkbutton" onclick="saveFeedEntity()" title="保存" iconCls="icon-save" plain="true">保存</a>
        <a href="#" class="easyui-linkbutton" onclick="inputFeedEntity()" title="入库" iconCls="icon-redo" plain="true">入库</a>
        <a href="#" class="easyui-linkbutton" onclick="refreshFeedEntity()" title="刷新" iconCls="icon-reload" plain="true">刷新</a>
        <a href="#" class="easyui-linkbutton" onclick="cancelFeedEntity()" title="取消" iconCls="icon-undo" plain="true">取消</a>
        <a href="#" class="easyui-linkbutton" onclick="completeFeedEntity()" title="确认完成" iconCls="icon-ok" plain="true">确认完成</a>
    </div>
    <div id="tbsubmiter" style="height:auto">
        <a href="#" class="easyui-linkbutton" onclick="saveWorkerSubmitEntity()" title="保存" iconCls="icon-save" plain="true">保存</a>
    </div>
    <div id="rightMenuGoodCodeFeed" class="easyui-menu" style="width: 50px; display: none;">
        <div  onclick="addProcessFeed()">新增</div> 
        <div  onclick="editProcessFeed()">修改</div>
        <div  onclick="deleteProcessFeed()">删除</div>  
        <div   onclick="startCodeFeed()">接收码</div> 
    </div>
    <div id="rightMenuGoodCode" class="easyui-menu" style="width: 50px; display: none;">
        <div  onclick="addProcess()">新增</div> 
        <div  onclick="editProcess()">修改</div> 
        <div  onclick="deleteProcess()">删除</div>  
        <div   onclick="startCode()">接收码</div> 
    </div>
    <div id="rightMenuWorkerSubmitGoodCode" class="easyui-menu" style="width: 50px; display: none;">
        <div  onclick="editWorkerSubmit()">修改</div>   
        <div   onclick="endCode()">完成码</div>
        <%--<div   onclick="unqualifiedOrder()">产品不合格单</div>--%>
        <%--<div   onclick="scrapOrder()">外协废品单</div>   --%>
    </div>
    <div id="codeShow">

    </div>
    <div id="unqualifiedOrder">

    </div>
    <div id="scrapOrder">

    </div>
    <script>
        function cancelEntity() {
            orderdetailsId=getValueById("orderdetailsId");
            var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单"
            $('#goodprocessList').datagrid('options').url=url;
            $("#goodprocessList").datagrid('reload');
        }

        function cancelFeedEntity() {
            orderdetailsId=getValueById("orderdetailsId");
            var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料"
            $('#feedgoodprocessList').datagrid('options').url=url;
            $("#feedgoodprocessList").datagrid('reload');
        }

        function refreshEntity() {
            orderdetailsId=getValueById("orderdetailsId");
            var url=rootPath+'/order/refreshProductionByOrderDetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
            communateGet(url,function (data) {
                orderdetailsId=getValueById("orderdetailsId");
                var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                $('#goodprocessList').datagrid('options').url=url;
                $("#goodprocessList").datagrid('reload');

                var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                communateGet(url,function (data) {
                    setContentToDivSpanById("mapNumber",data.mapNumber);
                    setContentToDivSpanById("name",data.goodName);
                    setContentToDivSpanById("materialQuality",data.materialQualityName);
                    setContentToDivSpanById("weight",data.weight);
                    setContentToDivSpanById("state",data.state);
                    setContentToInputById("goodId",data.id);
                })
            })
        }

        function refreshFeedEntity() {
            orderdetailsId=getValueById("orderdetailsId");
            var url=rootPath+'/order/refreshProductionByOrderDetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
            communateGet(url,function (data) {
                orderdetailsId=getValueById("orderdetailsId");
                var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                $('#feedgoodprocessList').datagrid('options').url=url;
                $("#feedgoodprocessList").datagrid('reload');

                var url=rootPath+'/blank/findTechnologyGoodByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                communateGet(url,function (data) {
                    img=data.img;
                    setContentToDivSpanById("mapNumber",data.mapNumber);
                    setContentToDivSpanById("name",data.goodName);
                    setContentToDivSpanById("materialQuality",data.materialQualityName);
                    setContentToDivSpanById("weight",data.weight);
                    setContentToDivSpanById("deliveryTime",data.deliveryTime);
                    setContentToDivSpanById("state",data.state);
                    setContentToInputById("goodId",data.id);
                })
            })
        }

        function unqualifiedOrder() {
            var row=getDatagridJsonByIndexAndId("workersubmitList",goodprocesseditId);
            var workersubmitId=row.id;
            $("#unqualifiedOrder").window({
                width:800,
                title:'产品不合格单',
                modal: true,
                top:10,
                href: rootPath + '/blank/unqualifiedOrderUI.shtml?orderdetailsId='+getValueById("orderdetailsId")+"&workersubmitId="+workersubmitId,
                onClose:function () {

                }
            });
        }

        function scrapOrder() {
            var row=getDatagridJsonByIndexAndId("workersubmitList",goodprocesseditId);
            var workersubmitId=row.id;
            $("#scrapOrder").window({
                width:800,
                title:'外协废品单',
                modal: true,
                top:10,
                href: rootPath + '/blank/scrapOrderPrintShowUI.shtml?orderdetailsId='+getValueById("orderdetailsId")+"&workersubmitId="+workersubmitId,
                onClose:function () {

                }
            });
        }




        var goodprocesseditId;
        var blankprocessId="";
        $("#goodprocessList").datagrid({
            onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
                e.preventDefault(); //阻止浏览器捕获右键事件
                if(goodprocesseditId!=null){
                    endEditDatagridByIndex('goodprocessList',goodprocesseditId)
                    endEditDatagridByIndex('workersubmitList',goodprocesseditId)
                }
                clearSelectAndSelectRowDatagrid('goodprocessList',rowIndex);
                goodprocesseditId=rowIndex;
                showMenu("rightMenuGoodCode",e.pageX,e.pageY);
                e.preventDefault();  //阻止浏览器自带的右键菜单弹出
            },
            onClickRow:function (rowIndex, rowData) {
                blankprocessId=rowData.id;
                var url=rootPath+'/blank/findWorkersubmitByBlankprocessId.shtml?blankprocessId='+blankprocessId;
                $('#workersubmitList').datagrid('options').url=url;
                $("#workersubmitList").datagrid('reload');
            },
        });

        $("#feedgoodprocessList").datagrid({
            onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
                e.preventDefault(); //阻止浏览器捕获右键事件
                if(goodprocesseditId!=null){
                    endEditDatagridByIndex('feedgoodprocessList',goodprocesseditId)
                    endEditDatagridByIndex('workersubmitList',goodprocesseditId)
                }
                clearSelectAndSelectRowDatagrid('feedgoodprocessList',rowIndex);
                goodprocesseditId=rowIndex;
                showMenu("rightMenuGoodCodeFeed",e.pageX,e.pageY);
                e.preventDefault();  //阻止浏览器自带的右键菜单弹出
            },
            onClickRow:function (rowIndex, rowData) {
                blankprocessId=rowData.id;
                var url=rootPath+'/blank/findWorkersubmitByBlankprocessId.shtml?blankprocessId='+blankprocessId;
                $('#workersubmitList').datagrid('options').url=url;
                $("#workersubmitList").datagrid('reload');
            },
        });

        $("#workersubmitList").datagrid({
            onRowContextMenu: function (e, rowIndex, rowData) { //右键时触发事件                      
                e.preventDefault(); //阻止浏览器捕获右键事件
                if(goodprocesseditId!=null){
                    endEditDatagridByIndex('goodprocessList',goodprocesseditId)
                    endEditDatagridByIndex('workersubmitList',goodprocesseditId)
                }
                goodprocesseditId=rowIndex;
                showMenu("rightMenuWorkerSubmitGoodCode",e.pageX,e.pageY);
                e.preventDefault();  //阻止浏览器自带的右键菜单弹出
            },
        });

        function startCodeFeed() {
            var row=getDatagridJsonByIndexAndId("feedgoodprocessList",goodprocesseditId);
            var unreceiveAmount=row.unreceiveAmount;
            showInputDialog("提示","请输入本次接收数量",function (content) {
                if(content!=undefined){
                    if(content==''||content=='0'){
                        showErrorAlert("提示",'请输入接收数量');
                    }else{
                        var receiveAmount=content;
                        if(VarToFloat(receiveAmount)>VarToFloat(unreceiveAmount)){
                            showErrorAlert("警告","本次接收数量不能大于未接收数量【"+unreceiveAmount+"】");
                        }else{
                            var startCode=row.startQRCode;
                            $("#codeShow").window({
                                width:250,
                                title:'接收码',
                                modal: true,
                                height:240,
                                top:50,
                                href: rootPath + '/blank/codeUI.shtml?code='+startCode+"amount"+receiveAmount,
                                onClose:function () {
                                    orderdetailsId=getValueById("orderdetailsId");
                                    var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                                    $('#feedgoodprocessList').datagrid('options').url=url;
                                    $("#feedgoodprocessList").datagrid('reload');
                                }
                            });
                        }
                    }

                }else{

                }

            })

        }

        function startCode() {
            var row=getDatagridJsonByIndexAndId("goodprocessList",goodprocesseditId);
            var unreceiveAmount=row.unreceiveAmount;
            showInputDialog("提示","请输入本次接收数量",function (content) {
                if(content!=undefined){
                    if(content==''||content=='0'){
                        showErrorAlert("提示",'请输入接收数量');
                    }else{
                        var receiveAmount=content;
                        if(VarToFloat(receiveAmount)>VarToFloat(unreceiveAmount)){
                            showErrorAlert("警告","本次接收数量不能大于未接收数量【"+unreceiveAmount+"】");
                        }else{
                            var startCode=row.startQRCode;
                            $("#codeShow").window({
                                width:250,
                                title:'接收码',
                                modal: true,
                                height:240,
                                top:50,
                                href: rootPath + '/blank/codeUI.shtml?code='+startCode+"amount"+receiveAmount,
                                onClose:function () {
                                    orderdetailsId=getValueById("orderdetailsId");
                                    var url=rootPath+'/blank/findTechnologyByOrderdetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                                    $('#goodprocessList').datagrid('options').url=url;
                                    $("#goodprocessList").datagrid('reload');
                                }
                            });
                        }
                    }

                }else{

                }

            })

        }
        function editProcess() {
            beginEditDatagridByIndex('goodprocessList',goodprocesseditId);
        }
        function editProcessFeed() {
            beginEditDatagridByIndex('feedgoodprocessList',goodprocesseditId);
        }

        function addProcess() {
            var rows = getAllRowsContent("goodprocessList");
            if(goodprocesseditId!=null||goodprocesseditId!=""){
                endEditDatagridByIndex("goodprocessList",goodprocesseditId);
            }
            $('#goodprocessList').datagrid('appendRow',
                {
                    "goodId":goodId,
                    "blankId":rows[0].blankId,
                    "number":rows.length+1
                }
            );
            goodprocesseditId=rows.length-1;
            beginEditDatagridByIndex('goodprocessList',goodprocesseditId);
        }

        function deleteProcess() {
            var rowData=getDatagridJsonByIndexAndId("goodprocessList",goodprocesseditId);
            var url='${pageContext.request.contextPath}/blank/deleteBlankProcessEntity.shtml?id='+rowData.id+"&goodprocessId="+rowData.goodprocessId;
            if(goodprocesseditId!=null){
                communateGet(url,function back(data){
                    reloadDatagridMessage('goodprocessList');
                });
            }else{
                $.messager.alert('警告','不存在可以删除数据','warning');
            }
        }

        function addProcessFeed() {
            var rows = getAllRowsContent("feedgoodprocessList");
            if(goodprocesseditId!=null||goodprocesseditId!=""){
                endEditDatagridByIndex("feedgoodprocessList",goodprocesseditId);
            }
            $('#feedgoodprocessList').datagrid('appendRow',
                {
                    "goodId":goodId,
                    "blankId":rows[0].blankId,
                    "number":rows.length+1
                }
            );
            goodprocesseditId=rows.length-1;
            beginEditDatagridByIndex('feedgoodprocessList',goodprocesseditId);
        }

        function deleteProcessFeed() {
            var rowData=getDatagridJsonByIndexAndId("feedgoodprocessList",goodprocesseditId);
            var url='${pageContext.request.contextPath}/blank/deleteBlankProcessEntity.shtml?id='+rowData.id+"&goodprocessId="+rowData.goodprocessId;
            if(goodprocesseditId!=null){
                communateGet(url,function back(data){
                    reloadDatagridMessage('feedgoodprocessList');
                });
            }else{
                $.messager.alert('警告','不存在可以删除数据','warning');
            }
        }

        function editWorkerSubmit() {
            beginEditDatagridByIndex('workersubmitList',goodprocesseditId);
            editAndAddSetContentAndChange();
        }

        function editAndAddSetContentAndChange() {
            editId=goodprocesseditId;
            setEditorOnChange("goodprocessList",3,function onChangeBack(index,content) {
                dailyWages=getValueById("dailyWages");
                artificial=getContentByEditor("goodprocessList",10);
                var planneedDay=VarToFloat(artificial)*VarToFloat(content)/VarToFloat(dailyWages);
                var  neesDay=parseInt(planneedDay)+1;
                setContentToDatagridEditorText("goodprocessList",editId,'planneedDay',neesDay);
            });
        }

        function endCode() {
            var row=getDatagridJsonByIndexAndId("workersubmitList",goodprocesseditId);
            var completeAmount=row.completeAmount;
            var complteAmountInt=VarToInt(completeAmount);
            var receiveAmount=row.amount;
            var receiveAmountInt=VarToInt(receiveAmount);
            if(complteAmountInt<=0||complteAmountInt>receiveAmountInt){
                showErrorAlert("提示",'不能生成二维码<br>完成数量为空或者完成数量打印接收数量');
            }else{
                var endCode=row.endQRCode+"workersubmitId"+row.id;
                $("#codeShow").window({
                    width:250,
                    title:'完成码',
                    modal: true,
                    height: 240,
                    top:50,
                    href: rootPath + '/blank/codeUI.shtml?code='+endCode,
                    onClose:function () {
                        var url=rootPath+'/blank/findWorkersubmitByBlankprocessId.shtml?blankprocessId='+blankprocessId;
                        $('#workersubmitList').datagrid('options').url=url;
                        $("#workersubmitList").datagrid('reload');
                    }
                });
            }

        }

    </script>
</div>
</body>