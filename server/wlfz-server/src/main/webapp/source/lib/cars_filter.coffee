define ['jquery', 'underscore'], ($, _) ->

    lookTag = () ->
        if _.some( $('.tag-group').children(), (oEl) -> $(oEl).html() )
            $('.J_filterHead').show() 
        else 
            $('.J_filterHead').hide()

    updateTag = (obj, content, clear) ->
        if obj.oTag.html()
            obj.oTag.find('em').text(content)
        else
            $('<div>').addClass('filter-tag').append(
                $('<span>').text(obj.title), 
                $('<em>').text(obj.text),
                $('<i>').addClass('filter-close').on('click', ->
                    clear and clear(obj)
                )
            ).appendTo(obj.oTag)

        lookTag()

    class filterLink
        defaults:
            title: 
                wheel:   '挂轮挂轴：'
                cart:    '货箱结构：'
                spec:    '特殊功能：'

        constructor: (options = {}) ->
            options = $.extend {}, @defaults, options
            @options = options
            @bindEv()
            @backfill()

        bindEv: ->
            self =@
            $('.filter-body .more').click ->
                $('.filter-hidden',$(@).siblings()).toggle()

            $('.filter-link a').click ->
                _this = $(@)
                _this.toggleClass('current')
                type = _this.data('type')
                obj = self.getLink(type, _this)

                self.linkClick(obj)
                if obj.oHidden.val()
                    updateTag(obj, obj.oHidden.attr('data-content').replace(/\u002f/g,'，'), self.clearLink)
                else
                    obj.oTag.empty()
                    lookTag()

        backfill: ->
            self = @
            _.each $('.CONVERT_CAR_DATA'), (oHidden) ->
                $oHidden = $(oHidden)
                if $oHidden.val() 
                    type = $(oHidden).attr('name')
                    idArr = $(oHidden).val().split(',')
                    aLinks = $('.filter-link a[data-type=' + type + ']')
                    arr = []

                    _.each aLinks, (aLink, i)->
                        $aLink = $(aLink)
                        id = $aLink.data(type) 
                        if _.indexOf(idArr, id) != -1
                            $aLink.addClass 'current'
                            arr.push $aLink.html()

                    if arr.length
                        $oHidden.attr 'data-content', arr.join(',')
                        updateTag({
                                title:   self.options.title[type]
                                text:    $oHidden.attr('data-content').replace(/\u002f/g,'，')
                                oTag:    $('.tag-holder-' + type)
                                type:    type
                                oHidden: $oHidden
                            }, null, self.clearLink)

        getLink: (type, _this) ->
            obj = 
                el:      _this
                type:    type
                id:      _this.data(type)
                text:    _this.text()
                oHidden: $('#' + type)
                oTag:    $('.tag-holder-' + type)
                title:   @options.title[type]

        clearLink: (obj) ->
            obj.oHidden.val('')
            obj.oHidden.attr('data-content', '')
            obj.oTag.empty()
            $('.filter-link a[data-type=' + obj.type + ']').removeClass('current')

            lookTag()

        linkClick: (obj) ->
            value = obj.oHidden.val()
            icontent = obj.oHidden.attr('data-content')
            dup = false
            unless value 
                obj.oHidden.val obj.id
                obj.oHidden.attr 'data-content', obj.text
            else
                arr = value.split(',')
                arr1 = obj.oHidden.attr('data-content').split(',')
                _.each arr, (item, i) ->
                    if item == obj.id
                        arr.splice(i, 1)
                        obj.oHidden.val arr.join(',')
                        dup = true
                        return

                _.each arr1, (item, i) ->
                    if item == obj.text
                        arr1.splice(i, 1 )
                        obj.oHidden.attr('data-content', arr1.join(','))
                        dup = true
                        return

                unless dup 
                    obj.oHidden.val( value + ',' + obj.id );
                    obj.oHidden.attr('data-content', icontent + ',' + obj.text );


    class filterIpt
        defaults:
            title: 
                filter_plate:   '车牌号码：'
                filter_mobile:  '随车电话：'
                filter_name:    '司机姓名：'
                location:       '发货城市：'
                destination:    '到货城市：'
                carlen:         '箱体长度：'
                load:           '载重吨位：'
            unit:
                carlen: '米',
                load:   '吨'

        constructor: (options = {}) ->
            options = $.extend {}, @defaults, options
            @options = options
            @bindEv()
            @backfill()

        backfill: ->
            _.each $('.J_filter_ipt'), (item, i) ->
                $(item).keyup()

            _.each $('.J_filter_count'), (item, i) ->
                $(item).keyup()

            $('#location').change()
            $('#destination').change()

        bindEv: ->
            self = @
            $('.J_filter_ipt').on 'keyup', ->
                self.setInfo.call self, $(@).attr('id'), $(@)

            $('#location').on 'change', ->
                self.setInfo.call self, 'location', $('#location')

            $('#destination').on 'change', ->
                self.setInfo.call self, 'destination', $('#destination')

            $('.J_filter_count').on 'keyup', ->
                self.setCount.call self, $(@)

        setCount: (_this) ->
            type = _this.data('type')
            aIpt = $('.J_' + type) 
            oMsg = $('.help-msg', _this.parent())
            val1 = aIpt.eq(0).val()
            val2 = aIpt.eq(1).val()
            oMsg.hide()
            if @checkValid(val1, val2)
                text = ''
                unit = @options.unit[type]
                if val1 == ''
                    text = val2 + unit + '以下'
                else if val2 ==''
                    text = val1 + unit + '以上'
                else
                    text = val1 + '-' + val2 + unit

                obj = @getIpt(type, _this, text)

                if (val1 == '' and val2 == '')
                    @clearUnit(obj, type)
                    return

                updateTag(obj, text, @clearUnit)
            else
                oMsg.show()
                

        setInfo: (type, _this) ->
            obj = @getIpt(type, _this)
            if obj.text
                updateTag(obj, obj.text, @clearIpt)
            else
                obj.oTag.empty()
                lookTag()

        clearUnit: (obj) ->
            $('.J_' + obj.type).val('')
            obj.oTag.empty()
            lookTag()

        clearIpt: (obj) ->
            obj.el.val('')
            obj.oTag.empty()
            lookTag()

        getIpt: (type, _this, text) ->
            obj =
                el: _this
                type: type
                text: text or _this.val()
                oTag: $('.tag-holder-' + type)
                title: @options.title[type]

        checkValid: (min,max) ->
            b = (min == '' and max == '')     or
                (@isFloat(min) and max == '') or
                (@isFloat(max) and min == '') or 
                (@isFloat(min) and @isFloat(max) and parseFloat(min) < parseFloat(max))
            pass = if b then true else false

        isFloat: (data)->
            # 正则表达式 匹配 非负浮点数，小数点后最多两位小数
            result = if data.match(/^\+{0,1}\d+(\.\d{1,2})?$/) then true else false

    init = () ->
        new filterLink 
        new filterIpt

















