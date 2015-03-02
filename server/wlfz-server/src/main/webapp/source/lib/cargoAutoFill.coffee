define [
    'jquery'
    '_.str'
], ($, str) ->
    ###
    opts:
        target:
            input: ''
            icon: ''
        outTarget:
            titleEl: ''
            valueInput: ''
            descInput: ''
        
        incomeData: []
        
    ###
    ###
        使用方法： new CargoAutoFill(opts)
    ###
    
    class CargoAutoFill
        
        tmp_wrap: """
            <ul class="ui-autocomplete ui-menu ui-widget ui-widget-content ui-corner-all" 
                 style="z-index: 2; position: absolute; display: none; width:190px">
            </ul>
        """
        tmp_list: """
         <% _.each(data, function(item){%>
             <li class="ui-menu-item">
             <a class="ui-corner-all" href="javascript:void(0)">
                <span class="value"><%= item.company.company_name || item.contact_number|| "" %></span><br>
                <span class="desc"><%= item.contact_person || "" %></span><br>
                <span class="num" style="display:none"><%= item.contact_number || "" %></span><br>
            </a>
        </li>
        <%})%>
        """
        
        constructor: (opts) ->
            if (not opts or not _.isObject(opts))
                return
            @opts = opts
            @data = opts.incomeData
            @filterData = @data
            @render()
           
        
        
        render: ->
            @$ui = $(@tmp_wrap)
            @iconEvent()
            
        setPosition: ->
            offset = @opts.target.input.offset()
            @position = {
                'width': @opts.target.input.width() + 10
                'top': offset.top + @opts.target.input.height() + 10
                'left': offset.left
                }
            @$ui.css({
                top: @position.top
                left: @position.left
                width: @position.width
                })
            $('body').append(@$ui)
        
        iconEvent: ->
            self = @
            @opts.target.icon.on 'click', () ->
                self.show()
                self.alldata()
            @opts.target.input.on 'keyup', (e) ->
                if self.complate()
                    self.show()
                    self.complatedata()
                else
                    if $(e.currentTarget).val().length != 11
                        self.outputreset()
                        self.removeEl()
            @opts.target.input.on 'blur', (e) ->

                if self.complate()
                	if $('.ui-menu-item').first().length>0
                   		self.chosefirt()
                   	self.removeEl()
        # 补全验证
        complate: ->
            self = @
            text = @opts.target.input.val()
            if text.length > 0 and text.length <= 11
                _.some @data, (obj) ->
                    # obj = _.values obj
                    _.some obj, (v) ->
                        return _.str.include(v, text)


        # 全匹配选第一个
        chosefirt: ->
            self = @
            first = $('.ui-menu-item').first()
            val = $('.value', first).text()
            desc = $('.desc', first).text()
            num = $('.num', first).text()
            self.opts.target.input.val(num).change()
            self.opts.outTarget.titleEl.text(val).change()
            self.opts.outTarget.valueInput.val(val).change()
            self.opts.outTarget.descInput.val(desc).change()
        	
        # 点击选择第一个
        chose: ->
            self = @
            _.each $('li a', @$ui), (a, i) ->
                $(a).on 'click', (e) ->
                    val = $('.value', $(e.currentTarget)).text()
                    desc = $('.desc', $(e.currentTarget)).text()
                    num = $('.num', $(e.currentTarget)).text()
                    self.opts.target.input.val(num).change()
                    self.opts.outTarget.titleEl.text(val).change()
                    self.opts.outTarget.valueInput.val(val).change()
                    self.opts.outTarget.descInput.val(desc).change()
                    self.removeEl()
        # 补全数据
        complatedata: ->
            self = @
            text = @opts.target.input.val()
            @filterData = []
            _.each @data, (list, index) ->

                valid = _.some list, (v) ->
                    return _.str.include(v, text)
                if valid
                    self.filterData.push(self.data[index])
            $('.ui-autocomplete', $('body')).html($(_.template(@tmp_list, {data: @filterData})))
            @chose()
            
        
        alldata: ->

            if $('.ui-autocomplete', $('body')).length == 1
                $('.ui-autocomplete', $('body')).html($(_.template(@tmp_list, {data: @filterData})))
                @chose()
        
        show: ->
            $target = @$ui
            if @$ui.hasClass 'active' and not @complate()
                @$ui.css('display', 'none')
                @$ui.removeClass('active')
                @removeEl()
            else
                @$ui.css('display', 'block')
                @$ui.addClass('active')
                @setPosition()
        
        removeEl: ->            
            $('.ui-autocomplete', $('body')).remove()
            @$ui.css('display', 'none')
            @$ui.removeClass('active')
        
        outputreset: ->
            @opts.outTarget.titleEl.text('信息不全,点击这里完善')
            @opts.outTarget.valueInput.val('')
            @opts.outTarget.descInput.val('')
        
        
        output: (e) ->
            $target = $(e.currentTarget)
            @opts.outTarget.titleEl.text($('.value', $target).text()).change()
            @opts.outTarget.valueInput.val($('.value', $target).text()).change()
            @opts.outTarget.descInput.val($('.desc', $target).text()).change()
            
               