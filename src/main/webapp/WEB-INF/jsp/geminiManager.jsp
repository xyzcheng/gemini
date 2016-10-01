<%--
  Created by IntelliJ IDEA.
  array: xyzc
  Date: 2016/9/30
  Time: 14:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Weboth管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <style>
        ul,p{margin:0;padding:0;}
        li{list-style:none;}
        .box{width:auto;margin:100px auto; margin-left: 20px}
        .box .img{float:left;}
        .box .img img{width:100px;height:auto; min-height: 150px}
        .box .list{float:left;width:auto;margin-left:20px;max-height:150px;display:inline;}
        .box .list li{line-height:24px;font-size:14px;}
        .list li span{font-weight:bold; color: green}
    </style>
</head>
<body>
<script  type="text/javascript" src="js/jquery.js"></script>
<table>
    <tr>
        <td>输入希望删除图片的id：</td><td><input id="picId" type="number" /></td>
        <td>&nbsp;
            <button onclick="deletePic()">删除图片</button>
        </td>
    </tr>
    <tr>
        <td id="pageIndex">第1页</td>
        <td id="pageNum">&nbsp;总共1页</td>
        <td>&nbsp;
            <button onclick=prePage()>上一页</button>
        </td>
        <td>&nbsp;
            <button onclick=nextPage()>下一页</button>
        </td>
    </tr>

</table>



<script type="text/javascript">
    var pageIndex = 1;
    var pageNum = 1;
    var wechat=document.createElement('div')
    allMediaDesc();
    function allMediaDesc() {
        $.ajax({
            url: 'allMediaDesc.do',
            type: 'GET',
            dataType: 'json',
            data: {pageIndex: pageIndex},
            error: function () {
                alert("err");
            },
            success: function(data){
                var json = eval(data); //json object
                pageIndex = json.pageIndex;
                pageNum = json.pageNum;
                document.getElementById('pageIndex').innerHTML="第" + pageIndex + "页";
                document.getElementById('pageNum').innerHTML="总共" + pageNum + "页";
                var array = json.list;
                makeHtmlNode(array);
            }
        });
    }
    function deletePic() {
        var idInput = document.getElementById("picId");
        var delPicId = idInput.value;
        $.ajax({
            url: 'delDescById.do',
            type: 'GET',
            data: {id: delPicId},
            async: true,
            error: function () {
                alert("error");
            },
            success: function (data) {
                alert("删除成功");
               // window.location.reload();
                console.log("deletePic");
                document.getElementById('con').removeChild(wechat);
                allMediaDesc();
            }
        });
    }

    function makeHtmlNode(array) {
        var node = '';
        var changeWxPicUrl = 'http://read.html5.qq.com/image?src=forum&q=5&r=0&imgflag=7&imageUrl=';
        var wrap=document.getElementById('con')
        $.each(array, function (i, item) {
            node += '<div class="box">'
                    + '<div class="img">' + '<img src="'+ changeWxPicUrl + array[i].mediaUrl +　'"/>' + '</div>'
                    + '</div>'
                    + '<div class="list">'
                    + ''
                    + '<ul>'
                    + '<li><span> id:&nbsp; </span>' + array[i].id + '</li>'
                    + '<li><span> Time:&nbsp;</span>' + array[i].time + '</li>'
                    + '<li><span> Location:&nbsp; </span>' + array[i].location + '</li>'
                    + '<li><span> Story:&nbsp; </span>' + array[i].story + '</li>'
                    + '</ul>'
                    + '</div>' + '<br/>' ;
        });
        //$('.wechatList').append(node);
        wechat.innerHTML=node;
        wrap.appendChild(wechat)
    }
    function nextPage() {
        if (pageIndex >= pageNum) {
            alert("已经最后一页了");
        } else {
            console.log("nextPage");
            document.getElementById('con').removeChild(wechat);
            pageIndex ++;
            allMediaDesc();
        }
    }
    function prePage() {
        if (pageIndex <= 1) {
            alert("已经是第一页了");
        } else {
            console.log("prePage");
            document.getElementById('con').removeChild(wechat);
            pageIndex --;
            allMediaDesc();
        }
    }
</script>
<div class="container", id="con">
</div>
</body>
</html>
