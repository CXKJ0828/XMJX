<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/orderSituation.js"></script>
<body class="easyui-layout">
<div data-options="region:'west'" style="width:200px;">
    <div id="tb" style="height:auto">
        <c:forEach items="${res}" var="key">
            ${key.description}
        </c:forEach>
    </div>
    <ul id="tt" class="easyui-tree" style="margin-top: 10px;margin-left: 10px"></ul>
</div>

<div data-options="region:'center'" >
    <div style="margin-left: 5px;margin-top: 10px">
        <input id="startTimeShow" style="display: none"  type="text" value="${startTimeShow}">
        <input id="endTimeShow" style="display: none" type="text" value="${endTimeShow}" >
        <%--<input  id="starttime"  type="text" value="${startTime}">--%>
        <%--至--%>
        <%--<input id="endtime"  type="text" value="${endTime}">--%>
        <input  id="contractNumber" class="inputShow" placeholder="请输入订单编号">
        <input  id="mapNumber" class="inputShow" placeholder="请输入图号">
        <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
    </div>

        <table id="goodprocessList"
               class="easyui-datagrid"
               style="width:auto;height:300px"
               data-options="rownumbers:true,
                    title:'发货情况',
                     singleSelect:true,
            fitColumns:true,
 pagination:true">
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
                    var json = JSON.parse(text);
                    return json;
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

        <div class="easyui-tabs">
            <div title="下料情况" style="padding:1px">
                <table id="blankSituation"
                       class="easyui-datagrid"
                       style="width:auto;height:auto"
                       data-options="rownumbers:true,
                     singleSelect:true,
            fitColumns:true">
                    <thead>
                    <tr>
                        <th data-options="field:'mapNumber',width:100,align:'center'">图号</th>
                        <th data-options="field:'goodSize',width:100,align:'center'">成品尺寸</th>
                        <th data-options="field:'orderAmount',width:80,align:'center'">订单数量</th>
                        <th data-options="field:'blankSize',width:100,align:'center'">下料尺寸</th>
                        <th data-options="field:'amount',width:80,align:'center'">下料数量</th>
                        <th data-options="field:'materialQuality',width:80,align:'center'">材质</th>
                        <th data-options="field:'length',width:80,align:'center'">下料米数</th>
                        <th data-options="field:'weight',width:80,align:'center'">下料吨数</th>
                        <th data-options="field:'state',width:80,align:'center'">状态</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div title="补料情况" style="padding:5px">
                <table id="feedSituation"
                       class="easyui-datagrid"
                       style="width:auto;height:auto"
                       data-options="rownumbers:true,
                     singleSelect:true,
            fitColumns:true">
                    <thead>
                    <tr>
                        <th data-options="field:'mapNumber',width:100,align:'center'">图号</th>
                        <th data-options="field:'goodSize',width:100,align:'center'">成品尺寸</th>
                        <th data-options="field:'orderAmount',width:80,align:'center'">订单数量</th>
                        <th data-options="field:'blankSize',width:100,align:'center'">下料尺寸</th>
                        <th data-options="field:'amount',width:80,align:'center'">下料数量</th>
                        <th data-options="field:'materialQuality',width:80,align:'center'">材质</th>
                        <th data-options="field:'length',width:80,align:'center'">下料米数</th>
                        <th data-options="field:'weight',width:80,align:'center'">下料吨数</th>
                        <th data-options="field:'state',width:80,align:'center'">状态</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>

        <div class="easyui-tabs">
            <div title="生产情况" style="padding:1px">
                <table id="blankProductSituation"
                       class="easyui-datagrid"
                       style="width:auto;height:auto"
                       data-options="rownumbers:true,
                     singleSelect:true,
                      view:detailview,
            detailFormatter:detailFormatter,
            onExpandRow:onBlankExpandRow,
            fitColumns:true">
                    <thead>
                    <tr>
                        <th data-options="field:'mapNumber',width:40,align:'center',editor:'text'">图号</th>
                        <th data-options="field:'goodName',hidden:true,width:40,align:'center',editor:'text'">产品名称</th>
                        <th data-options="field:'processId',hidden:true,width:40,align:'center',editor:'text'">工序号</th>
                        <th data-options="field:'number',width:40,align:'center',editor:'text'">工序号</th>
                        <th data-options="field:'processName',width:40,align:'center',editor:'text'">工序</th>
                        <th data-options="field:'amount',width:40,align:'center',editor:'text'">数量</th>
                        <th data-options="field:'planneedDay',width:50,align:'center',editor:'text'">预计用时(天)</th>
                        <th data-options="field:'completeAmount',width:40,align:'center',editor:'text'">完成数量</th>
                        <th data-options="field:'badAmount',width:40,align:'center',editor:'text'">废品数</th>
                        <th data-options="field:'completeTime',width:40,align:'center',editor:'text'">完成时间</th>
                        <th data-options="field:'state',width:40,align:'center',editor:'text'">状态</th>
                    </tr>
                    </thead>
                </table>
                <script>
                    var datagridDetailsArray=new Array();
                    var myArray=new Array();
                    function onBlankExpandRow(index, row) {
                        var blankprocessId=row.id;
                        var url=rootPath+'/blank/findWorkersubmitByBlankprocessId.shtml?blankprocessId='+blankprocessId;
                        var columns=[[
                            { field: 'id', title: '编号',hidden:true,align:'center', editor:'text' },
                            { field: 'startTime', title: '接收时间', width: 50, align: 'center',editor:'text' },
                            { field: 'userName', title: '接收人', width: 50, align: 'center',editor:'text' },
                            { field: 'amount', title: '接收数量', width: 50, align: 'center',editor:'text' },
                            { field: 'completeTime', title: '完成时间', width: 50, align: 'center',editor:'text' },
                            { field: 'completeAmount', title: '完成数量', width: 50, align: 'center',editor:'text' },
                            { field: 'badAmount', title: '废品数', width: 50, align: 'center',editor:'text' },
                            { field: 'deductRate', title: '扣除比例', width: 50, align: 'center',editor:'text' },
                        ]];
                        var parentDatagridId='blankProductSituation';
                        initExpandRow(index,row,url,columns,parentDatagridId,true,editId,function (number) {
                        });
                    }

                </script>
            </div>
            <div title="补料生产情况" style="padding:5px">
                <table id="feedProductSituation"
                       class="easyui-datagrid"
                       style="width:auto;height:auto"
                       data-options="rownumbers:true,
                   view:detailview,
            detailFormatter:detailFormatter2,
            onExpandRow:onFeedExpandRow,
                     singleSelect:true,
            fitColumns:true">
                    <thead>
                    <tr>
                        <th data-options="field:'mapNumber',width:40,align:'center',editor:'text'">图号</th>
                        <th data-options="field:'goodName',hidden:true,width:40,align:'center',editor:'text'">产品名称</th>
                        <th data-options="field:'processId',hidden:true,width:40,align:'center',editor:'text'">工序号</th>
                        <th data-options="field:'number',width:40,align:'center',editor:'text'">工序号</th>
                        <th data-options="field:'processName',width:40,align:'center',editor:'text'">工序</th>
                        <th data-options="field:'amount',width:40,align:'center',editor:'text'">数量</th>
                        <th data-options="field:'planneedDay',width:50,align:'center',editor:'text'">预计用时(天)</th>
                        <th data-options="field:'completeAmount',width:40,align:'center',editor:'text'">完成数量</th>
                        <th data-options="field:'badAmount',width:40,align:'center',editor:'text'">废品数</th>
                        <th data-options="field:'completeTime',width:40,align:'center',editor:'text'">完成时间</th>
                        <th data-options="field:'state',width:40,align:'center',editor:'text'">状态</th>
                    </tr>
                    </thead>
                </table>
                <script>
                    var datagridDetailsArray2=new Array();
                    var myArray2=new Array();
                    function onFeedExpandRow(index, row) {
                        var blankprocessId=row.id;
                        var url=rootPath+'/blank/findWorkersubmitByBlankprocessId.shtml?blankprocessId='+blankprocessId;
                        var columns=[[
                            { field: 'id', title: '编号',hidden:true,align:'center', editor:'text' },
                            { field: 'userName', title: '接收人', width: 50, align: 'center',editor:'text' },
                            { field: 'startTime', title: '接收时间',hidden:true, width: 50, align: 'center',editor:'text' },
                            { field: 'amount', title: '接收数量', width: 50, align: 'center',editor:'text' },
                            { field: 'completeTime', title: '完成时间', width: 50, align: 'center',editor:'text' },
                            { field: 'completeAmount', title: '完成数量', width: 50, align: 'center',editor:'text' },
                            { field: 'badAmount', title: '废品数', width: 50, align: 'center',editor:'text' },
                            { field: 'deductRate', title: '扣除比例', width: 50, align: 'center',editor:'text' },
                        ]];
                        var parentDatagridId='feedProductSituation';
                        initExpandRow2(index,row,url,columns,parentDatagridId,false,editId,function (number) {
                        });
                    }
                </script>
            </div>
        </div>

            <script>
                initInputDataInput("starttime");
                initInputDataInput("endtime");
                var editId;
                $("#goodprocessList").datagrid({
                    onClickRow:function (rowIndex, rowData) {
                        $('#goodprocessList').datagrid('selectRow',rowIndex);

                        var orderdetailsId=rowData.id;
                        var url=rootPath+'/order/findOrderSituation.shtml?orderdetailsId='+orderdetailsId+'&origin=订单';
                        $('#blankProductSituation').datagrid('options').url=url;
                        $("#blankProductSituation").datagrid('reload');

                        var url=rootPath+'/order/findOrderSituation.shtml?orderdetailsId='+orderdetailsId+'&origin=补料';
                        $('#feedProductSituation').datagrid('options').url=url;
                        $("#feedProductSituation").datagrid('reload');


                        var url=rootPath+'/blank/findByOriginAndOrderDetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                        $('#blankSituation').datagrid('options').url=url;
                        $("#blankSituation").datagrid('reload');

                        var url=rootPath+'/blank/findByOriginAndOrderDetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                        $('#feedSituation').datagrid('options').url=url;
                        $("#feedSituation").datagrid('reload');
                    },
                    onLoadSuccess:function(data){
                        var orderdetailsId=data.rows[0].id;
                        var url=rootPath+'/order/findSituationByOrderDetailsId.shtml?orderdetailsId='+orderdetailsId;
                        communateGet(url,function (data) {
                            data.rows[0].orderSituation=data;
                        })

                        $('#goodprocessList').datagrid('selectRow',0);
                        var orderdetailsId=data.rows[0].id;
                        var url=rootPath+'/order/findOrderSituation.shtml?orderdetailsId='+orderdetailsId+'&origin=订单';
                        $('#blankProductSituation').datagrid('options').url=url;
                        $("#blankProductSituation").datagrid('reload');

                        var url=rootPath+'/order/findOrderSituation.shtml?orderdetailsId='+orderdetailsId+'&origin=补料';
                        $('#feedProductSituation').datagrid('options').url=url;
                        $("#feedProductSituation").datagrid('reload');


                        var url=rootPath+'/blank/findByOriginAndOrderDetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=订单";
                        $('#blankSituation').datagrid('options').url=url;
                        $("#blankSituation").datagrid('reload');

                        var url=rootPath+'/blank/findByOriginAndOrderDetailsId.shtml?orderdetailsId='+orderdetailsId+"&origin=补料";
                        $('#feedSituation').datagrid('options').url=url;
                        $("#feedSituation").datagrid('reload');
                    }, rowStyler: function(index,row){
                        var deliveryTime=row.deliveryTime;
                        var startTime=getValueById("startTimeShow");
                        var endTime=getValueById("endTimeShow");
                        if (deliveryTime>=startTime&&deliveryTime<endTime){
                            return 'color:#ff0030;';//红色
                        }else{
                            return 'color:#000;';
                        }
                    },
                });
            </script>


        <script>
            function computeByState(id,colName) {
                var rows = $('#'+id).datagrid('getRows');
                var total = 0;
                for (var i = 0; i < rows.length; i++) {
                    if(rows[i].state!='已完成'){
                        total += StrToFloat(rows[i][colName]);
                    }

                }
                total=Math.round(total*100)/100
                return total;
            }

            var editId;
            $("#blankProductSituation").datagrid({
                onLoadSuccess: function () {
                    $('#blankProductSituation').datagrid('appendRow', {
                        startTime: '<span style="font-weight: bold;">合计</span>',
                        planneedDay: '<span style="font-weight: bold;">' + computeByState('blankProductSituation',"planneedDay")+ '</span>',
                    });
                },
                rowStyler: function(index,row){
                    var state=row.state;
                    if(state==''){
                        state="未完成";
                    }
                    if (state=='已完成'){
                        return 'color:#048133;font-weight:bold';
                    }else{
                        return 'color:#000;';
                    }
                }
            });

            $("#feedProductSituation").datagrid({
                onLoadSuccess: function () {
                    $('#feedProductSituation').datagrid('appendRow', {
                        startTime: '<span style="font-weight: bold;">合计</span>',
                        planneedDay: '<span style="font-weight: bold;">' + computeByState('feedProductSituation',"planneedDay")+ '</span>',
                    });
                },
                rowStyler: function(index,row){
                    var state=row.state;
                    if(state==''){
                        state="未完成";
                    }
                    if (state=='已完成'){
                        return 'color:#048133;font-weight:bold';
                    }else{
                        return 'color:#000;';
                    }
                }
            });

            $("#blankSituation").datagrid({
                rowStyler: function(index,row){
                    var state=row.state;
                    if(state==''){
                        state="未订料";
                    }
                    if (state=='已订料'){
                        return 'color:#048133;font-weight:bold';
                    }else{
                        return 'color:#000;';
                    }
                }
            });
            $("#feedSituation").datagrid({
                rowStyler: function(index,row){
                    var state=row.state;
                    if(state==''){
                        state="未订料";
                    }
                    if (state=='已订料'){
                        return 'color:#048133;font-weight:bold';
                    }else{
                        return 'color:#000;';
                    }
                }
            });
        </script>
    </div>
</div>
</body>