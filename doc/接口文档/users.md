##用户 /users

序号		|接口名称  |请求方式|说明
------:|--------|-------------------|------------------
 1|[/users/login](#login)			|POST	|登录
 2|[/users/register](#register)		|POST	|注册用户,用户在MeFP中不存在
 3|[/users/activate](#activate)		|POST	|激活用户,用户在MeFP中已存在
 4|[/users/:id](#update_user)		|PUT 	|修改用户信息，修改密码，停用、启用等
 5|[/users/:id](#user_info)			|GET 	|获取用户详情
 6|[/users](#userlist)				|GET 	|获取用户列表
 7|[/users/token](#token)			|POST	|为其它App提交接口,仅供MeFP访问
 8|[/users/consignors](#consignors)|POST	|添加货主信息
 9|[/users/consignors/:id](#update_consignors)|PUT|修改货主信息
 10|[/users/consignors/:id](#consignors_info)|GET|获取车主信息
 11|[/users/drivers](#drivers)				|POST|添加车主信息
 12|[/users/driver/:id](#update_drivers)	|PUT|修改货主信息
 13|[/users/drivers/:id](#drivers_info)	|GET|获取车主信息
 14|[/users/check](#user_check)		|GET	|验证用户是否存在
 15|[/users/captcha](#captcha)		|GET	|验证发送
 
#### 1. <label id="login">用户登录</label>

|方法名称|/users/login|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|login_name|N|登录名,目前的Login_name为手机号码|
|password|N|联系人姓名|

说明:

	根据用户与密码,加密后提交到 MeFP_Protal/auth/login
	
	根据返回值处理
		返回失败:返回错误信息
		
	验证通过,在users表中根据login_name查找
		查找不到,说明该用户未激活,前端提示并转到激活页面
		
	将查找到的数据中的(值为null时,取key="",而不是key=null)
		login_name,
		user_name,
		role,
		area,
		reg_time,
		checked,
		status,
		expired_time(过期时间,当前时间加一天),
	用AES加密后再用Base64加密,作为token


返回数据:
>
	{
		"result":1,
		"data":{
			"token":"xxxxxxxxxxxxxxxxx",
			"login_name":"13812345678",
			"user_id":123,
			"user_name":"张三",
			"role":0,
			"checked":1,
			"status":1,
			"reg_time":"2015-01-01 12:30:00",
			"login_time":"2015-03-01 12:30:00",//上次登录时间
			"login_ip":"10.10.10.10",			//上次登录ip
			"login_count":10,					//登录次数
			"sign":"这个家伙很懒,什么也没有留下",
			"consignors":{
				"id":123,
				"contact_number":"货方联系电话",
				"contact_name":"货方联系人",
				"company":"公司名",
				"address":"地址",
				"memo":"货方备注"
			},
			"drivers":{
				"id":345
				"contact_number":"货方联系电话",
				"contact_name":"货方联系人",
				"plate_number":"车牌",
				"memo":"车方备注"
			}
		}
	}

#### 2. <label id="register">用户注册</label>

|方法名称|/users/register|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|login_name|N|登录名|
|password|N|密码|
|captcha|N|手机验证码|
|user_name||昵称|
|city||所在城市|
|sign||签名档|
|memo||备注信息|

说明:

	注册是指本系统与MeFP中都不存在的用户，注册时要在MeFP端注册用户，然后在本系统增加用户
	
	组织参数 login_name,password,captcha,向 MeFP_Portal/auth/register 发起请求
	返回成功后,将login_name,user_name,city,sign,memo,reg_time(当前时间)等入到数据库表users中
	
	users表中area字段可空,配合role作为合作方的扩展
	合作方下属的用户为 area like '{area}%' and role <{role}


返回数据:
>
	{
		"result":1
	}

#### 3. <label id="activate">用户激活</label>

|方法名称|/users/activate|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|login_name	|N	|登录名|
|user_name	|	|昵称|
|city		|	|所在城市|
|sign		|	|签名档|
|memo		|	|备注信息|

说明:
	
	激活是指在MeFP中存在，但是本系统中不存在的用户，在第一次登录时需要进行激活操作
	
	向 MeFP_Portal/auth/check 发起请求,确认用户存在后
	将login_name,user_name,city,sign,memo,reg_time(当前时间)等添加到数据库表users中
	

返回数据:
>
	{
		"result":1
	}

#### 4. <label id="update_user">用户修改</label>

|方法名称|/users/:id|
|:----|:--------|
|请求方式|PUT|

|参数名称|可空|说明|
|:--|:--------|
|user_name| |昵称|
|city| |所在城市|
|sign| |签名档|
|memo| |备注信息|

说明:

	修改 users 中相应的字段
	

返回数据:
>
	{
		"result":1
	}

#### 5. <label id="user_info">用户详情</label>

|方法名称|/users/:id|
|:----|:--------|
|请求方式|GET|

返回数据:
>
	{
		"result":1,
		"data":{
			"login_name":"13812345678",
			"user_id":123,
			"user_name":"张三",
			"role":0,
			"city":"杭州",
			"area":"浙江-杭州",
			"status":1,
			"checked":1
			"reg_time":"2015-01-01 12:30:00",
			"login_time":"2015-03-01 12:30:00",
			"login_ip":"10.10.10.10",
			"login_count":10,
			"sign":"这个家伙很懒,什么也没有留下",
			"memo":"备注信息",
			"consignors":{
				"id":123,				
				"contact_number":"货方联系电话",
				"contact_name":"货方联系人",
				"company":"公司名",
				"address":"地址",
				"memo":"货方备注信息",
				"img_idcard":"idcard/123.jpg",
				"img_org":"org/123.jpg",
				"img_biz":"biz/123.jpg",
			},
			"drivers":{
				"id":345,
				"contact_number":"货方联系电话",
				"contact_name":"货方联系人",
				"plate_number":"车牌",
				"location":"最新位置",
				"lng":119.12345,
				"lat":38.123456,
				"memo":"车方备注",
				"img_idcard":"idcard/123.jpg",
				"img_car":"car/123.jpg"		
				"img_driver":"drvier/123.jpg"
				"img_driving":"driving/123.jpg"
				"img_opt":"opt/123.jpg"
			}
		}
	}

#### 6. <label id="userlist">用户列表</label>

|方法名称|/users/:id|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:--|:--------|
|keyword||关键字模糊查询,匹配login_name,user_name,city,sign,memo|
|role| |角色|
|status| |状态|
|checked| |审核状况|

返回数据:
>
	{
		"result":1,
		"total":30,/记录数
		"page_no":1,//当前页码1开始
		"page_size":20,//每页记录数
		"data":[
			{
				"login_name":"13812345678",
				"user_id":123,
				"user_name":"张三",
				"role":0,
				"city":"杭州",
				"area":"区域",
				"checked":1,
				"status":1,
				"consignors":{
					"id":123,
					"contact_number":"货方联系电话",
					"contact_name":"货方联系人",
					"company":"公司名",
					"address":"地址",
					"app_id":"货主端appid"
				},
				"drivers":{
					"id":345,
					"idcard_number":"330323198010101234",
					"contact_number":"货方联系电话",
					"contact_name":"货方联系人",
					"plate_number":"车牌",
					"location":"最新位置",							"lng":119.12345,
					"lat":38.123456,
					"app_id":"车主端appid"
				}
			}
		]
	}

#### 7. <label id="token">为MeFP提供的 token</label>

|方法名称|/users/:id|
|:----|:--------|
|请求方式|POST|
|说明|传入与输出的内容都须经过加密|

|参数名称|可空|说明|
|:--|:--------|
|login_name||登录名|

说明:
	
	本接口不对外开放,只允许MeFP访问,要做IP限制,IP要写在配置文件中
	本接口接收与返回的数据均为加密数据
	
	接收的数据先进行Base64解密,然后用AES解密,AES加密所需要的密钥写在配置文件,
		mefp.scret为32位密钥
		app.scret为16位密钥,作为初始向量
		
	返回的数据,先经AES加密,然后再经Base64加密.
	返回的数据格式参阅<统一验证接口文档.md>
	
传入的数据解密后:
>
	{
		"login_name":"13812345678"
	}

返回数据加密前:
>
	{
		"token":"xxxxxxxxxxxxxxxxxxx"
	}

#### 8. <label id="consignors">添加货主信息</label>

|方法名称|/users/consignors|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|login_name		|N|登录名|
|contact_name	|N|联系人|
|contact_number	|N|联系电话|
|company		||公司名|
|address		||地址|
|device_id		||手机设备号|
|weixin_id		||微信号|
|jpush_sn		||极光推送序列号|
|img_org		||组织代码证照片|
|img_biz		||营业执照证照片|
|memo			||备注|


返回数据:
>
	{
		"result":1
	}

#### 9. <label id="update_consignors">修改货主信息</label>

|方法名称|/users/consignors/:id|
|:----|:--------|
|请求方式|PUT|

|参数名称|可空|说明|
|:--|:--------|
|contact_name	||联系人|
|contact_number	||联系电话|
|company		||公司名|
|address		||地址|
|device_id		||手机设备号|
|weixin_id		||微信号|
|jpush_sn		||极光推送序列号|
|img_org		||组织代码证照片|
|img_biz		||营业执照证照片|
|memo			||备注|


返回数据:
>
	{
		"result":1
	}	

#### 10. <label id="consignors_info">获取货主信息</label>

|方法名称|/users/consignors/:id|
|:----|:--------|
|请求方式|GET|


返回数据:
>
	{
		"result":1,
		"data":{
			"id":123,
			"login_name":"13812345678",
			"company":"杭州米阳科技有限公司"，
			“address”:"杭州市拱墅区北部软件园D座220室"，
			“contact_name”:"张三"，
			“contact_number”:"13812345678",
			"decice_id":"",
			"weixin_id":"",
			"jpush_sn":"",
			"img_org":"",
			"img_biz":"",
			"memo":""
		}
	}
	
#### 11. <label id="drivers">添加车主信息</label>

|方法名称|/users/drivers|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|login_name		|N|登录名|
|plate_number	|N|车牌|
|contact_name	|N|联系人|
|contact_number	|N|联系电话|
|idcard_number||身份证号|
|driving_license||驾驶证编号|
|locate_number	||定位手机|
|device_id		||手机设备号|
|weixin_id		||微信号|
|jpush_sn		||极光推送序列号|
|img_car		||车身照片|
|img_driver		||驾驶证证照片|
|img_driving	||行驶证照证照片|
|img_opt		||营运证照片|
|memo			||备注|


返回数据:
>
	{
		"result":1
	}

#### 13. <label id="update_drivers">修改车主信息</label>

|方法名称|/users/drivers/:id|
|:----|:--------|
|请求方式|PUT|

|参数名称|可空|说明|
|:--|:--------|
|plate_number		||车牌|
|idcard_number		||身份证|
|driving_license	||行驶证|
|contact_name		||联系人|
|contact_number		||联系电话|
|driving_license	||驾驶证编号|
|locate_number		||定位手机|
|device_id			||手机设备号|
|weixin_id			||微信号|
|jpush_sn			||极光推送序列号|
|img_car			||车身照片|
|img_driver			||驾驶证证照片|
|img_driving		||行驶证照证照片|
|img_opt			||营运证照片|
|memo				||备注|


返回数据:
>
	{
		"result":1
	}


#### 13. <label id="drivers_info">获取车主信息</label>

|方法名称|/users/drivers/:id|
|:----|:--------|
|请求方式|GET|


返回数据:
>
	{
		"result":1,
		"data":{
			"id":123,
			"login_name":"13812345678",
			"plate_number":"浙A12345"，
			“contact_name”:"张三"，
			“contact_number”:"13812345678",
			"idcard_number":"身份证号",
			"driving_license":"",
			"decice_id":"",
			"weixin_id":"",
			"jpush_sn":"",
			"img_car":"",
			"img_driver":"",
			"img_driving":"",
			"img_opt":"",
			"memo":""
		}
	}

#### 14. <label id="user_check">验证用户是否存在</label>

|方法名称|/users/check|
|:----|:--------|
|请求方式|GET|

|参数名称|可空|说明|
|:--|:--------|
|login_name||登录名|

返回数据:
>
	{
		"result":1,
		"data:{
			"reg_app": "melink"	,//不存在时值为空字符串
        	"reg_time": "2011-07-23 20:27:00"
		}
	}	

#### 15. <label id="captcha">验证用户是否存在</label>

|方法名称|/users/captcha|
|:----|:--------|
|请求方式|POST|

|参数名称|可空|说明|
|:--|:--------|
|login_name||登录名|
|usage||用途，1注册，2找回密码|

说明：
	
	成功后向目标手机发送验证码
	
返回数据:
>
	{
		"result":1
	}	
