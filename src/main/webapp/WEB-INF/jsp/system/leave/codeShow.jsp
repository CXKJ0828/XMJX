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

    $(function() {
        code = new QRCode(document.getElementById("code"), {
            width : 200,
            height : 200
        });
        code.makeCode("leave:0.5");
    });
    function onChangeDate() {
        alert("aaa");
        duration=getContentBySelect("duration");
        code.makeCode("leave:"+duration);

    }
</script>
<body>
选择请假天数：
<input id="duration" class="easyui-combobox" data-options="
    valueField: 'value',
    textField: 'text',
    url: '${pageContext.request.contextPath}/json/days.json',
    onLoadSuccess: function (data) {
       $('#duration').combobox('setValue',data[0].value);//选择后台查出来的第一个值
    },
    onSelect: function(rec){
    code.makeCode('leave:'+rec.value);
    }">
<script>
</script>
<div  id="printCodeContent" style="text-align: center;margin-top: 10px">
    <div class="easyui-accordion" style="width:300px;height:300px;float: left">
        <div title="二维码" data-options="iconCls:'icon-ok'" style="overflow:auto;padding:10px;">
            <div id="code"></div>
        </div>
    </div>
</div>
</body>