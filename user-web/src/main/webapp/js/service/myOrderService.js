//服务层
app.service('myOrderService', function ($http) {
    //查找我的订单
    this.findAllByUserId = function () {
        return $http.get('../order/findAllByUserId.do');
    };

    //确认收货
    this.confirmOrder = function (orderId) {
        return $http.get('../order/confirmOrder.do?orderId='+orderId);
    };
});
