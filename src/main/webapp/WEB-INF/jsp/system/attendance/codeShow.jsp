<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${ctx}/js/qrcode.min.js"></script>
<style>
    .tdShow{
        width:20%;
        height: 40px;
        text-align: left;
    }
    .spanShow{
        width: 30%!important;
    }
</style>
<script>
    var start ="";
    var end ="";
    $(function() {
        start = new QRCode(document.getElementById("start"), {
            width : 200,
            height : 200
        });
        end = new QRCode(document.getElementById("end"), {
            width : 200,
            height : 200
        });
        var day=getNowDate();
        start.makeCode("start:"+day);
        end.makeCode("end:"+day);
    });
    var day=getNowDate();
    function onChangeDate(date) {
        day=getDataboxValue("day");
        start.makeCode("start:"+day);
        end.makeCode("end:"+day);
    }
</script>
<body>
生成日期：<input  id="day" type="text" data-options="onSelect:onChangeDate">
        <div  id="printCodeContent" style="text-align: center;margin-top: 10px">
            <div class="easyui-accordion" style="width:300px;height:300px;float: left">
                <div title="开始二维码" data-options="iconCls:'icon-ok'" style="overflow:auto;padding:10px;">
                    <div id="start"></div>
                </div>
            </div>
            <div class="easyui-accordion" style="width:300px;height:300px;">
                <div title="结束二维码" data-options="iconCls:'icon-ok'" style="padding:10px;">
                    <div id="end"></div>
                </div>
            </div>
        </div>
<script>
    initInputDataInput("day");
    setDataToDateBox("day",getNowDate());
</script>
</body>