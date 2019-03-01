app.service('uploadService', function ($http) {
    //上传图片
    this.uploadFile = function () {
        var formData = new FormData();
        var file = document.getElementById("file").files[0];
        formData.append('file', file);

        return $http({
            method: 'POST',
            url: "../upload.do",
            data: formData,
            headers: {'Content-Type': undefined},
            transformRequest: angular.identity
        }).success(function (data) {
            console.log('upload success');
        }).error(function (data) {
            console.log('upload fail');
        })
    }
});

//文件上传服务层
app.service("uploadService",function($http){
    this.uploadFile=function(){
        var formData=new FormData();
        formData.append("file",file.files[0]);
        return $http({
            method:'POST',
            url:"../upload.do",
            data: formData,
            headers: {'Content-Type':undefined},
            transformRequest: angular.identity
        });
    }
});

