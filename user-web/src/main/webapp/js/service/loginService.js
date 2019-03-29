//服务层
app.service('loginService', function ($http) {
    //获取当前登录账号
    this.showName = function () {
        return $http.get('../login/name.do');
    }

});
