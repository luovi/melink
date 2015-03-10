### 账户 /accounts

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
1|[/accounts/:id/recharge](#recharge)	|POST|充值
2|[/accounts/:id/pay](#pay)				|POST|支付
3|[/accounts/:id/cash](#account_cash)	|POST|提现
4|[/accounts/:id/transfer](#account_transfer)	|POST|转账
5|[/accounts/:id/convert](#account_convert)	|POST|兑换
6|[/accounts/:id](#account_info)		|GET |帐户信息
7|[/accounts/:id/log](#account_log)	|GET |支付与充值记录

#### 1. <label id="recharge">充值</label>

|方法名称|/accounts/:id/recharge|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|amount|N|金额|
|memo||备注信息|

说明:

	银联或者支付宝充值成交后调用此接口
	修改个人账户余额，添加账户变更记录


返回数据:
>
	{
		"result":1,
		"data":{
			"balance":"100.12",//充值后的余额
			"change_time":"充值时间"
		}
	}

#### 2. <label id="pay">支付</label>

|方法名称|/accounts/:id/pay|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|amount|N|金额|
|target_id|N|支付的目标账户|
|memo||充值的备注信息|
	
返回数据:
>
	{
		"result":1,
		"data":{
			"balance":"100.12",//支付后的余额
			"change_time":"支付成功时间"
		}
	}



