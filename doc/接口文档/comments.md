### 用户评价 /comments

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
 1|[/comments](#add_comment)			|POST 	|添加评价
 2|[/comments](#list_comment)			|GET 	|评价列表
 3|[/comments/:id](#update_comment)	|PUT	|修改评价
 4|[/comments/:id](#del_comment)		|DELETE	|删除评价
 
 
#### 1. <label id="add_comment">添加评价</label>

|方法名称|/comments|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:------|:--------|:--------|
|login_name		|N	|评价人登录名|
|type			|N	|评价类型，1对货主，2对车主|
|trade_id		|	|关联的交易|
|target			|	|被评价人的登录名|
|comment		|	|评价内容|
|mark			|	|-1差评，0中评，1好评|
|comment_time	|	|评价时间|


说明:

	根据trade_id找出货主与车主
	只有交易双方可以添加评价对方
	
返回数据:
>
	{
		"result":1
	}
 
#### 2. <label id="list_comment">评价列表</label>

|方法名称|/comments|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:------|:--------|:--------|
|login_name		|	|评价人登录名|
|type			|	|评价类型，1对货主，2对车主|
|trade_id		|	|关联的交易|
|target			|	|被评价人的登录名|
|comment		|	|评价内容|
|mark			|	|-1差评，0中评，1好评|
|comment_time_from	|	|评价时间|
|comment_time_to	|	|评价时间|


返回数据:
>
	{
		"result":1,
		"total":30,/记录数
		"page_no":1,//当前页码1开始
		"page_size":20,//每页记录数
		"data":[
			{
				"login_name":123,
				"type":"13812345678",
				"trade_id":"浙A12345",
				"target":"13087654321",
				"comment":"好评",
				"mark":1,
				"comment_time":"2015-03-02 12:30:00",
				
			},
			{
				...
			},
			...
		]
	}
 
#### 3. <label id="update_comment">修改评价</label>

|方法名称|/comments:id|
|:----|:--------|
|请求方式|PUT|

|参数名称|可空|说明|
|:------|:--------|:--------|
|comment		|	|评价内容|
|mark			|	|-1差评，0中评，1好评|
|comment_time	|	|评价时间|

说明：

	只有管理员与评价人才可以修改评价
	role==999 || comments.login_name=token.login_name
	
返回数据:
>
	{
		"result":1
	}
	
#### 4. <label id="del_comment">删除评价</label>

|方法名称|/comments:id|
|:----|:--------|
|请求方式|DELETE|

说明：

	只有管理员与评价人才可以删除评价
	role==999 || comments.login_name＝=token.login_name

返回数据:
>
	{
		"result":1
	}