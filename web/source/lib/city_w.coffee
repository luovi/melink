define [
    'jquery',
    'underscore'
    'lib/city_data'
], ($, _,city_data) ->
	class CitySelect
		defaults:
			inputEl : '.city_selet'

		constructor: (options = {}) ->

			self = @
			@options = $.extend {}, @defaults, options
			@bindEvent(options)
		bindEvent:->
			self = @
			@inputEl = @options.inputEl
			$('body').delegate(@inputEl, 'click', ->
				self.create $(self.inputEl)
			)
			$('.J_cityTab').click ->
				console.log 8
				
		create:(ipt)->
			tmp_btn = """
				
				<%if(type === 'J_typePro' ){%>
					<% $(data).each(function(num,item) { %>
						<a href="javascript:;" class="cb-c-c-btn J_cityBtn J_typePro"><%= item%></a>
					<%});%>
				<%}%>

				<%if(type === 'J_typeCity' ){%>
					<% $(data).each(function(num,item) { %>
						<a href="javascript:;" class="cb-c-c-btn J_cityBtn J_typeCity"><%= item%></a>
					<%});%>
				<%}%>

				<%if(type === 'J_typeCounty' ){%>
					<% $(data).each(function(num,item) { %>
						<a href="javascript:;" class="cb-c-c-btn J_cityBtn J_typeCounty"><%= item%></a>
					<%});%>
				<%}%> 

			"""
			tmp_select = """
				<div class="citybox" id="J_city_1" style="left:<%=l%>;top:<%=t%>;display:block;">
					<input type="hidden" class="J_p_hidden J_hidden">
					<input type="hidden" class="J_n_hidden J_hidden">
					<input type="hidden" class="J_c_hidden J_hidden">
					<div class="cb-header">
						<div class="cb-h-tab">
							<a href="javascript:;" class="J_cityTab active">热门</a>
							<a href="javascript:;" class="J_cityTab">省</a>
							<a href="javascript:;" class="J_cityTab">市</a>
							<a href="javascript:;" class="J_cityTab">区县</a>
						</div>
						<a href="javascript:;" class="cb-close J_cityClose">×</a>
					</div>
					<div class="cb-content">
						<div class="cb-c-cantainer active hot">

						</div>

						<div class="cb-c-cantainer">
							
						</div>

						<div class="cb-c-cantainer">
						</div>

						<div class="cb-c-cantainer">
						</div>
					</div>
				</div>
			"""
			# 获取热门城市
			common = @getList().common

			@$btn = $(_.template(tmp_btn,{type:'J_typeCity',data:common}))
			# 设置显示位置
			t = ipt.offset().top+ipt.get(0).offsetHeight+1+'px'
			l = ipt.offset().left+'px'
			@$modal = $(_.template(tmp_select,{t:t,l:l}))
			# 渲染
			$('.hot',@$modal).html @$btn
			$('body').append(@$modal)
			
		getList:->
			common:city_data.commonList
			cityList:city_data.cityList
				
