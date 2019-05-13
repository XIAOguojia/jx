app.service('payService',function($http){
    //本地支付
    this.createNative=function(orderId){
        return $http.get('../order/createNative.do?orderId='+orderId);
    };

    //查询支付状态
    this.queryPayStatus=function(out_trade_no){
        return $http.get('order/queryPayStatus.do?out_trade_no='+out_trade_no);
    }


});
