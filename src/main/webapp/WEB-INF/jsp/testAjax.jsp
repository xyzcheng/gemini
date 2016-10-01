<%--
  Created by IntelliJ IDEA.
  User: xyzc
  Date: 2016/9/30
  Time: 16:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script  type="text/javascript" src="../../js/jquery.js"></script>\
    <script type="text/javascript">
        $(function(){

            $.ajax({
                type:"get",
                url:"http://m.weather.com.cn/data/101010100.html", //中央台天气预报
                dataType:"json",
                success:function(data){
                    var test = eval(data);
                    alert(1);   //这个都没有反应啊T_T
                    alert(test.weatherinfo.city);
                }
            });
        })
    </script>
</head>
<body>

</body>
</html>
