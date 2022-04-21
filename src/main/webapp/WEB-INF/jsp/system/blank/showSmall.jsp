<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<style>
    .datagrid-cell,
    .datagrid-row,
    .datagrid-cell-group,
    .datagrid-header-rownumber,
    .datagrid-cell-rownumber {
        /*-webkit-text-stroke:0.2px#ffffff;*/

        margin: 0;
        padding: 0 4px;
        white-space: nowrap;
        word-wrap: normal;
        overflow: hidden;
        height: 30px;
        line-height: 30px;
        font-weight: normal;
        font-size: 14px;
    }
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/showSmall.js"></script>
<body  class="easyui-layout" >
<div style="width: 100%;height: 100%;background:url(${pageContext.request.contextPath}/images/bigshowBG.jpg);background-size:100% 100%;" id="content">
    <div>
        <div style="width: 100%;" >
            <div style="font-size: 40px;text-align: center;width: 100%;">调度展示　
                <div style="float: right;margin-top: 1%">
                    <a href="javascript:void(0)" id="btn" class="easyui-linkbutton" data-options="plain:true,iconCls:'arrow_out'">全屏</a>
                    <a href="javascript:void(0)" id="close" class="easyui-linkbutton" data-options="plain:true,iconCls:'arrow_in'">退出全屏</a>
                </div>
            </div>
            <div style="font-size: 18px;margin-left: 10px;">
                <span style="font-size: 18px;" id="allAmount" >数量合计:</span>
                订单编号:
                <input id="contractNumber"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
                图号:
                <input id="mapNumber"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
                产品名称:
                <select class="easyui-combobox" id='goodName' name="goodName" style="width:80px;">
                    <option value="不限"></option>
                    <option value="轴">轴</option>
                    <option value="套">套</option>
                    <option value="垫/片">垫/片</option>
                    <option value="其他">其他</option>
                    <option value="不限">不限</option>
                </select>
                物料编码：
                <input  id="materialCode" class="inputShow">
                状态:
                <select class="easyui-combobox" id='state' name="state" style="line-height:22px;width:100px;">
                    <option value=""></option>
                    <option value="未接收">未接收</option>
                    <option value="已接收未完成">已接收未完成</option>
                    <option value="已完成">已完成</option>
                    <option value="不限">不限</option>
                </select>
                工序：
                <input  id="processId" class="inputShow">
                编号：
                <input  id="code" class="inputShow">
                <script>
                    var materialCode="";
                    $('#materialCode').combogrid({
                        panelWidth:400,
                        idField:'materialCode',
                        textField:'materialCode',
                        pagination:true,
                        mode: 'remote',
                        url:'${pageContext.request.contextPath}/good/materialCodeSelect.shtml',
                        columns:[[
                            {field:'materialCode',title:'物料编码',width:350},
                        ]],
                        onSelect:function(materialCoderecord){
                            materialCode=getContentBycombogrid('materialCode');
                        }
                    });
                    $('#code').combogrid({
                        panelWidth:400,
                        idField:'code',
                        textField:'code',
                        pagination:true,
                        mode: 'remote',
                        url:'${pageContext.request.contextPath}/blank/codeSelectContainPage.shtml',
                        columns:[[
                            {field:'code',title:'编码',width:350},
                        ]],
                        onSelect:function(record){
                            code=getContentBycombogrid('code');
                        }
                    });
                </script>
                交货日期:
                <input  id="startTime" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
                至
                <input id="endTime" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
                材质：
                <input  id="materialQuality" class="inputShow">
                <input id="goodSize" placeholder="请输入成品尺寸" style="line-height:20px;width:100px;border:1px solid #ccc;margin-left: 10px">
                <input id="blankSize" placeholder="请输入下料尺寸" style="line-height:20px;width:100px;border:1px solid #ccc;margin-left: 10px">
                <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="downLoadAll()">全部导出</a>
            </div>
        </div>
        <div  style="width: 99%;height:89%;margin-left: 0.5%;margin-right: 0.5%;">
            <div style="width: 13%;height:100%;float:left;overflow: scroll;background-color: white ">
                <ul id="tt1"></ul>
            </div>
            <div style="width: 87%;height:100%;float:left;">
                <table id="tt"  data-options="fit:true" style="width:auto"></table>
            </div>
        </div>
    </div>
</div>
</div>


<script>
    var materialQuality="";
    $('#materialQuality').combobox({
        url:'${pageContext.request.contextPath}/blank/materialQualitySelect.shtml',
        valueField:'id',
        textField:'text',
        width:100,
        onSelect: function(record){
            materialQuality=record.text;
        }
    });
    var treeObjext=$('#tt1');
    communateGet(rootPath + '/blank/findBigShowClientByContent.shtml?processId='+processId
        +"&contractNumber="+getValueById("contractNumber")
        +"&code="+code
        +"&materialCode="+materialCode
        +"&mapNumber="+getValueById("mapNumber")
        +"&goodSize="+getValueById("goodSize")
        +"&blankSize="+getValueById("blankSize")
        +"&startTime="+getDataboxValue("startTime")
        +"&endTime="+getDataboxValue("endTime")
        +"&state="+getContentBycombogrid("state")
        +"&goodName="+getContentBycombogrid("goodName")
        +"&content="+getValueById("content")
        ,function (data) {
            treeObjext.tree({data: data});
        })
    var clientId="";
    var goodId="";
    var orderId="";
    treeObjext.tree({
        onBeforeSelect:function (node) {
            var childrenNodes = treeObjext.tree('getChildren',node.target);
            var lengthChild=childrenNodes.length;
            var state=node.state;
            if(state=='closed'&&lengthChild==0){
                if(isContain(node.id,"客户")){//根据获取获取订单
                    clientId=node.id.replace("客户","");
                    reloadDatagridMessage('tt');
                    communateGet(rootPath + '/blank/findBigShowOrderByContent.shtml?processId='+processId
                        +"&contractNumber="+getValueById("contractNumber")
                        +"&code="+code
                        +"&materialCode="+materialCode
                        +"&mapNumber="+getValueById("mapNumber")
                        +"&startTime="+getDataboxValue("startTime")
                        +"&endTime="+getDataboxValue("endTime")
                        +"&clientId="+clientId
                        +"&state="+getContentBycombogrid("state")
                        +"&goodName="+getContentBycombogrid("goodName")
                        +"&goodSize="+getValueById("goodSize")
                        +"&blankSize="+getValueById("blankSize"),function (data) {
                        treeObjext.tree('append', {
                            parent: node.target,
                            data: data
                        });
                    })
                }
                if(isContain(node.id,"订单")){//根据根据订单获取明细
                    orderId=node.id.replace("订单","");
                    reloadDatagridMessage('tt');
                    communateGet(rootPath + '/blank/findBigShowOrderDetailsByContent.shtml?processId='+processId
                        +"&contractNumber="+getValueById("contractNumber")
                        +"&code="+code
                        +"&materialCode="+materialCode
                        +"&mapNumber="+getValueById("mapNumber")
                        +"&startTime="+getDataboxValue("startTime")
                        +"&endTime="+getDataboxValue("endTime")
                        +"&orderId="+orderId
                        +"&state="+getContentBycombogrid("state")
                        +"&goodName="+getContentBycombogrid("goodName")
                        +"&goodSize="+getValueById("goodSize")
                        +"&blankSize="+getValueById("blankSize"),function (data) {
                        treeObjext.tree('append', {
                            parent: node.target,
                            data: data
                        });
                    })
                }
            }
            if(isContain(node.id,"明细")){
                goodId=node.id.replace("明细","");
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
    var btn = document.getElementById("btn");
    btn.onclick = function () {
        var elem = document.getElementById("content");
        requestFullScreen(elem);
    };


    var close = document.getElementById("close");
    close.onclick = function () {
        exitFullscreen();
    };


    function requestFullScreen(element) {
        var requestMethod = element.requestFullScreen || element.webkitRequestFullScreen || element.mozRequestFullScreen || element.msRequestFullScreen;
        if (requestMethod) {
            requestMethod.call(element);
        } else if (typeof window.ActiveXObject !== "undefined") {
            var wscript = new ActiveXObject("WScript.Shell");
            if (wscript !== null) {
                wscript.SendKeys("{F11}");
            }
        }
    }


    function exitFullscreen() {
        if (document.exitFullscreen) {
            document.exitFullscreen();
        } else if (document.msExitFullscreen) {
            document.msExitFullscreen();
        } else if (document.mozCancelFullScreen) {
            document.mozCancelFullScreen();
        } else if (document.webkitExitFullscreen) {
            document.webkitExitFullscreen();
        }
    }

</script>
<script>
    initInputDataInput("startTime");
    initInputDataInput("endTime");


    $('#processId').combobox({
        url:'${pageContext.request.contextPath}/process/processSelectTwoContent.shtml',
        valueField:'id',
        textField:'text',
        width:130,
        onSelect: function(record){
            processId=record.id;
        },
        onLoadSuccess : function(){
            $('#processId').combobox('setValue','46605');
        }
    });
</script>
</body>