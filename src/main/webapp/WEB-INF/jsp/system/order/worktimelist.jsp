<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/common/common.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/system/order/worktimelist.js"></script>
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
<div data-options="region:'center'">
    <table id="tt" style="width:auto"></table>
    <div id="tb" style="height:auto">
        <form id="form" name="form"  method="post" enctype="multipart/form-data">
            <c:forEach items="${res}" var="key">
                ${key.description}
            </c:forEach>
            <input id="selectTime" type="text"/>
            <input id="content" type="text"/>
            <a href="javascript:void(0)" onclick="find()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-search'">检索</a>
        </form>
    </div>
</div>
<script>
    $('#selectTime').datebox({
        //显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
        onShowPanel: function () {
            //触发click事件弹出月份层
            span.trigger('click');
            if (!tds)
            //延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                setTimeout(function () {
                    tds = p.find('div.calendar-menu-month-inner td');
                    tds.click(function (e) {
                        //禁止冒泡执行easyui给月份绑定的事件
                        e.stopPropagation();
                        //得到年份
                        var year = /\d{4}/.exec(span.html())[0],
                            //月份
                            //之前是这样的month = parseInt($(this).attr('abbr'), 10) + 1;
                            month = parseInt($(this).attr('abbr'), 10);

                        //隐藏日期对象
                        $('#selectTime').datebox('hidePanel')
                        //设置日期的值
                            .datebox('setValue', year + '-' + month);
                    });
                }, 0);
        },
        //配置parser，返回选择的日期
        parser: function (s) {
            if (!s) return new Date();
            var arr = s.split('-');
            return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10) - 1, 1);
        },
        //配置formatter，只返回年月 之前是这样的d.getFullYear() + '-' +(d.getMonth());
        formatter: function (d) {
            var currentMonth = (d.getMonth() + 1);
            var currentMonthStr = currentMonth < 10 ? ('0' + currentMonth) : (currentMonth + '');
            return d.getFullYear() + '-' + currentMonthStr;
        }
    });

</script>
</body>