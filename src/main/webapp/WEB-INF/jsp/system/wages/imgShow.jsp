<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>新明机械企业管理系统</title>
<%@include file="/common/showImg.jspf"%>
<style>
    .zoomer_wrapper { border: 1px solid #ddd; border-radius: 3px; height:80%; margin: 10px 0; overflow: hidden; width: 100%; }
    .zoomer.dark_zoomer img { box-shadow: 0 0 5px rgba(0, 0, 0, 0.5); }
</style>
<body>
            <div class="row">
                <div>
                    <h5>【${mapNumber}】图纸展示</h5>
                    <div class="zoomer_wrapper zoomer_basic">
                        <img style="align-content: center;" src="${img}" />
                            <%--<img style="align-content: center;" src="${pageContext.request.contextPath}/images/nasa-bw-2.jpg" />--%>
                    </div>
                    <script>
                        $(document).ready(function() {
                            $(".zoomer_basic").zoomer();

                            $(".zoomer_custom").zoomer({
                                controls: {
                                    zoomIn: ".zoomer_custom_zoom_in",
                                    zoomOut: ".zoomer_custom_zoom_out"
                                },
                                customClass: "dark_zoomer",
                                increment: 0.03,
                                interval: 0.1,
                                marginMax: 50
                            });

                            $(window).on("snap", function() {
                                $(".zoomer-element").zoomer("resize");
                            });

                            $(window).one("pronto.load", function() {
                                $(".zoomer-element").zoomer("destroy");
                                $(window).off("rubberband");
                            });
                        });
                    </script>
                </div>
            </div>
</body>