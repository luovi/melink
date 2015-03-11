define(['jquery', 'underscore','lib/city_data',
		'text!templates/modules/city_select.html',
		'text!templates/modules/city_btn.html'
],
function($, _ ,cityData, tmp_city, tmp_btn){
	var idIndex = 0;

	//全部数据
	var cityList = cityData.cityList;
	//省
	var proList = getProList();
	//热门
	var commonList = cityData.commonList;

	var sClassArr = ['J_typePro', 'J_typeCity', 'J_typeCounty'];

	function citySelect(opt){
		this.opt = opt || {};

		this.init();
	}

	citySelect.prototype = {
		init: function(){
			var self=this;
			setTimeout(function(){self.getEl();},100);  //?
			
			this.arrEl = []
		},
		// 获取元素
		getEl:function(){
			var aInput = $(this.opt.inputEl);

			for(var i=0; i<aInput.length; i++){
				this.render($(aInput[i]));
			}
		},
		render: function(oIpt){
			idIndex ++;
			var html = _.template(tmp_city, {
				id: idIndex
			});

			if(this.opt.afterInput){
				oIpt.parent().append($(html))
			}
			else{
				$('body').append($(html));
			}


			var el = $('#J_city_' + idIndex);
			this.arrEl.push(el)

			if(this.opt.afterInput){
				el.css({top: oIpt[0].offsetHeight-1,left:0})
			}
			else{
				this.setPos(oIpt[0], el);
			}

			var aCantainer = $('.cb-c-cantainer', el);
			$(aCantainer[0]).html(_.template(tmp_btn, {data: commonList, type: 'J_typeCity'} ) );
			$(aCantainer[1]).html(_.template(tmp_btn, {data: proList, type: 'J_typePro'} ) );


			this.bind(oIpt, el);
		},
		bind: function(oIpt, el){
			var self = this;

			// tab切换
			var aBtn = $('.J_cityTab', el);
			var aDiv = $('.cb-c-cantainer', el);

			for(var i=0; i<aBtn.length; i++){
				(function(index){
					aBtn[i].onclick = function(){
						self.setTab(index, el);
					}
				})(i);
			}

			// 点击文本框显示控件
			oIpt.click(function(){
				oIpt.select();
				el.show();
			});

			//关闭控件
			el.delegate('.J_cityClose', 'click', function(){
				el.hide();
			});

			//点击控件外部关闭控件
			$(document).click(function(ev){
				var oEvent = ev || event;
				var oSrc = oEvent.srcElement || oEvent.target;

				if(!self.isChild(oSrc, el[0]) && oSrc != oIpt[0] && el.css('display')=='block') {
					el.hide();
				}
			});

			//缩放网页，调整控件位置
			$(window).on('resize', function(){
				if(!self.opt.afterInput){
					self.setPos(oIpt[0], el);
				}
			});

			var aCantainer = $('.cb-c-cantainer', el);
			var aHidden = $('.J_hidden', el);

			el.delegate('.J_cityBtn', 'click', function(){
				var oBtn = $(this);
				var btnStr = oBtn.html();
				console.log(btnStr)
				//省 -> 加载城市或区
				if(oBtn.hasClass('J_typePro')){
					aHidden.val('');
					$(aHidden[0]).val(btnStr);
					for(var i=0; i<cityList.length; i++){
						if(cityList[i].p === btnStr){
							var data_c = cityList[i].c;

							//加载区
							if(data_c.length === 1){
								$(aHidden[1]).val(data_c[0].n);
								// var arr = [];
								// arr.push(data_c[0].n)
								// $(aCantainer[2]).empty().html(_.template(tmp_btn, {data: arr, type: 'J_typeCity'}));
								self.renderCounty.call(self, el, data_c[0], $(aCantainer[3]));

							}
							//加载城市
							else if(data_c.length > 1){
								self.setTab(2, el);

								//临时变量，结束后会自动回收
								var arr = [];

								for(var j=0; j<data_c.length; j++){
									arr.push(data_c[j].n);
								}
								$(aCantainer[2]).empty().html(_.template(tmp_btn, {data: arr, type: 'J_typeCity'}));

							}
						}
					}
				}
				//城市 -> 加载区
				else if(oBtn.hasClass('J_typeCity')){
					for(var i=0; i<cityList.length; i++){

						for(var j=0; j<cityList[i].c.length; j++){

							if(btnStr === cityList[i].c[j].n){
								aHidden.val('');
								$(aHidden[0]).val(cityList[i].p);
								$(aHidden[1]).val(btnStr);
								self.renderCounty.call(self, el, cityList[i].c[j], $(aCantainer[3]))
								var arr = self.toArr(cityList[i].c)
								setTimeout(function(){
									$(aCantainer[2]).empty().html(_.template(tmp_btn, {data:arr , type: 'J_typeCity'}));
								},0)
							}

						}
					}
				}
				//区 -> 结束
				else if(oBtn.hasClass('J_typeCounty')){
					$(aHidden[2]).val(btnStr);
					el.hide();
				}

				self.setValue(aHidden,oIpt);
			});	
		},
		destroy:function(){

		},
		setValue:function(aHidden,oIpt){
			var str = '';
			for(var i=0;i<aHidden.length;i++){
				if((aHidden[i+1] && aHidden[i+1].value === '') || i === 2){
					str+= aHidden[i].value ? aHidden[i].value : '';
				}
				else{
					str+= aHidden[i].value ? aHidden[i].value + '-' : '';
				}
			}
			oIpt.val(str);
		},
		renderCounty:function(el, data_c, element){
			this.setTab(3, el);

			var arrDate = data_c.a;
			//临时变量，结束后会自动回收
			var arr = [];

			for(var i=0; i<arrDate.length; i++){
				arr.push(arrDate[i].s);
			}

			element.html(_.template(tmp_btn, {data: arr, type: 'J_typeCounty'}));
		},
		// 数据转数组
		toArr:function(json){
			var arr = [];
			for(var i=0;i<json.length;i++){
				arr.push(json[i].n)
			}
			return arr
		},
		// 设置tab
		setTab: function(index, el){
			var aBtn = $('.J_cityTab', el);
			var aDiv = $('.cb-c-cantainer', el);

			for(var i=0; i<aBtn.length; i++){
				$(aBtn[i]).removeClass('active');
				$(aDiv[i]).removeClass('active');
			}

			$(aBtn[index]).addClass('active');
			$(aDiv[index]).addClass('active');
		},
		// 获取位置
		getPos:function(obj){
			var x = 0;
			var y = 0;
			while(obj){
				x += obj.offsetLeft;
				y += obj.offsetTop;
				obj = obj.offsetParent;
			}
			return {left: x, top: y};
		},
		// 设置位置
		setPos:function(obj, el){
			var p = this.getPos(obj);
			var height = obj.offsetHeight-1;

			var pos={
				left: p.left,
				top: p.top + height
			}

			el.css(pos);
		},
		// 判断是否是子元素
		isChild:function(obj1,obj2){
			while(obj1){
				if(obj1 === obj2){
					return true;
				}
				else{
					obj1 = obj1.parentNode;
				}
			}

			return false;
		}
	}

	//获取省列表
	function getProList(){
		var arr = [];

		for(var i=0; i<cityList.length; i++){
			arr.push(cityList[i].p);
		}

		return arr;
	}

	return citySelect;
});























