<%@ page language="java"  contentType="text/html; charset=UTF-8" %>

<html>
<body>
<h2>Tomcat1!</h2>
<h2>Tomcat1!</h2>
<h2>Tomcat1!</h2>
<h2>Hello World!</h2>



<form name="form1" action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="springmvc file upload" />
</form>


<form name="form2" action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" />
    <input type="submit" value="image upload" />
</form>

</body>
</html>
