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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/blank/showSmall2.js"></script>
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
                <input type="text" style="display: none" id="origin" value="${origin}">
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
                交货日期:
                <input  id="startTime" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
                至
                <input id="endTime" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
                领料日期:
                <input  id="startTimePick" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
                至
                <input id="endTimePick" type="text"  style="line-height:22px;width:100px;border:1px solid #ccc;margin-left: 10px">
                材质：
                <input  id="materialQuality" class="inputShow">
                <input id="goodSize" placeholder="请输入成品尺寸" style="line-height:20px;width:100px;border:1px solid #ccc;margin-left: 10px">
                <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true" onclick="downLoadAll()">全部导出</a>
                <a href="#" id="distributionOprate" class="easyui-linkbutton" onclick="distributionEntity()" title="分配" iconCls="icon-redo" plain="true">分配</a>
                <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-undo',plain:true" onclick="cancel()">取消</a>
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
    function cancel() {
        $('#tt').datagrid('clearSelections');
    }
    function distributionEntity() {
        var origin=getValueById("origin");
        var rows=getDatagridSelections('tt');
        var ids="";
        var length=rows.length;
        var isOprate=true;
        if(length==0){
            showErrorAlert("提示","请选择分配内容");
        }else{
            for(i=0;i<length;i++) {
                var id = rows[i].id;
                ids = ids + id + ",";
            }
            $("#distributionWindow").window({
                width: 400,
                modal: true,
                height: 200,
                top:100,
                href: rootPath + '/heattreat/distributionUI.shtml?ids='+ids+'&origin='+getValueById("origin")+"&source=小调度",
                onClose:function () {
                    $('#tt').datagrid("reload");
                }
            });
        }
    }


    initInputDataInput("startTime");
    initInputDataInput("endTime");

    initInputDataInput("startTimePick");
    initInputDataInput("endTimePick");

    var endtime=getNowDate();
    starttime=getNowMonth()+"-01";

    setDataToDateBox("startTimePick",starttime);
    setDataToDateBox("endTimePick",endtime);

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
    var clientId="";
    $('#tt1').tree({
        url:'${pageContext.request.contextPath}/blank/findSmallShow2ShowClientByContent.shtml',
        onBeforeLoad: function (node,params) {
            var origin=getValueById('origin');
            params.contractNumber=getValueById("contractNumber");
            params.mapNumber=getValueById("mapNumber");
            params.startTime=getDataboxValue("startTime");
            params.endTime=getDataboxValue("endTime");
            params.startTimePick=getDataboxValue("startTimePick");
            params.endTimePick=getDataboxValue("endTimePick");
            params.goodName=getContentBycombogrid("goodName");
            params.materialQuality=materialQuality;
            params.goodSize=getValueById("goodSize");
            params.origin=getValueById("origin");
        },onBeforeSelect:function (node) {
            clientId=node.id;
            reloadDatagridMessage("tt");
        }
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


</script>
<div id="distributionWindow" title="分配">
</div>
<div id="progressWindow" class="easyui-window"
     data-options="modal:true,closed:true,iconCls:'icon-save'"
     title="进度详情"
     style="width:500px;height:200px;padding:10px;">
    <div id="progressContent"></div>
</div>
</body>