define [
    'backbone',
    'store',
    'modal',
    'tagging',
    'text!templates/modules/side_store.html'
], (Backbone, Store, Modal, Tagging, tmp_side_store) ->
    'use strict'

    SideStoreView = Backbone.View.extend
        initialize: (options={}) ->
            @options = options
            @key = options.key or 'mylist'
            @key_len = options.key_len or 'myLen';
            @len = Store.get(@key_len) or 0;
            @target = options.target
            @data = Store.get(@key) or {}
            @$count = options.$count
            @render()

        events:
            'click #message_toggle': 'toggle'
            'click i.trash': 'delRow'
            'click a.send_msg': 'sendSMS'
            'click a.send_weixin': 'sendWX'
            'click a.add_notify': 'addNotify'
            'click a.store_clear':'clearAll'

        render: ->

            @$el.html @template(tmp_side_store, data:@data, options:@options,len:@len)
            @$body = $('tbody', @$el)
            if not _.isEmpty(@data)
                @show()
                @rebuildCheckbox()

        sendSMS: _.debounce (event) ->
            self = @
            user = new UserModel(@_current_user())
            if user.isGuest()
                @notify().show("</p>您现在是以游客身份登录，没有相应的使用权限，请先<a href='#signup'>免费注册</a>为正式用户！</p>", 'warning')
                return

            if user.isExpired()
                modal = new Modal
                    title: "确认提醒"
                    content: '<div class="modal-content">普通会员不能执行此操作，立即成为高级会员</div>'
                    button: [
                        value: "立即开通"
                        class: "btn-small btn-confirm"
                        callback: =>
                            @redirect("account/buy_packages")
                        autoremove: true
                    ]
            else
                @redirect("service/send/sms?key=#{ @key }&route=#{ encodeURIComponent(Backbone.history.fragment) }")
        , 800, true

        sendWX: _.debounce (event) ->
            self = @
            user = new UserModel(@_current_user())
            if user.isGuest()
                @notify().show("</p>您现在是以游客身份登录，没有相应的使用权限，请先<a href='#signup'>免费注册</a>为正式用户！</p>", 'warning')
                return

            if user.isExpired()
                modal = new Modal
                    title: "确认提醒"
                    content: '<div class="modal-content">普通会员不能执行此操作，立即成为高级会员</div>'
                    button: [
                        value: "立即开通"
                        class: "btn-small btn-confirm"
                        callback: =>
                            @redirect("account/buy_packages")
                        autoremove: true
                    ]
            else
                @redirect("service/send/weixin?key=#{ @key }&route=#{ encodeURIComponent(Backbone.history.fragment) }")
        , 800, true
        
        addNotify: _.debounce (event) ->
            self = @
            user = new UserModel(@_current_user())
            if user.isGuest()
                @notify().show("</p>您现在是以游客身份登录，没有相应的使用权限，请先免费<a href='#signup'>注册</a>为正式用户！</p>", 'warning')
                return

            if user.isExpired()
                modal = new Modal
                    title: "确认提醒"
                    content: '<div class="modal-content">普通会员不能执行此操作，立即成为高级会员</div>'
                    button: [
                        value: "立即开通"
                        class: "btn-small btn-confirm"
                        callback: =>
                            @redirect("account/buy_packages")
                        autoremove: true
                    ]
                return

            if @key is 'pv_list'
                if !(1 in (_.map(_.values(@data), (n) -> n[4])))
                    modal = new Modal
                        title: "确认提醒"
                        content: '<div class="modal-content">您所选择的车辆都未开通定位，为了保证您继续享用我们的位置服务，建议您立即开通车辆定位。</div>'
                        button: [
                            value: "立即开通"
                            class: "btn-small btn-confirm"
                            callback: ->
                                self.redirect("cars?status=closed")
                            autoremove: true
                        ]

                else if 0 in _.map(_.values(@data), (n) -> n[4])
                    modal = new Modal
                        title: "确认提醒"
                        content: '<div class="modal-content">您当前选择的车辆中有<span style="color:red">未开通定位</span>的车辆，如需继续使用此服务我们将会过滤未开通定位车辆。</div>'
                        button: [
                            value: "继续使用"
                            class: "btn-small btn-confirm"
                            callback: ->
                                copy_data = {}
                                $.extend(copy_data,self.data)
                                _.each(_.values(copy_data), (n,k) ->
                                    if n[4] is 0
                                        self.removeRow(_.keys(copy_data)[k])
                                        self.rebuildCheckbox()
                                )
                                self.redirect("service/notify?key=#{ self.key }")
                            autoremove: true
                        ]
                else
                    @redirect("service/notify?key=#{ @key }")
            else
                @redirect("service/notify?key=#{ @key }")
        , 800, true
        
        clearAll:(event)->
            self = @
            $target = $(event.target)
            $('i.trash').each (index,item)->
                $val = $(item).data('id')
                self.removeRow($val)
                self.rebuildCheckbox()


        delRow: (event) ->
            $target = $(event.target)
            $target.parents('tr').remove()
            @delete($target.data('id'))
            @rebuildCheckbox()

        load: ->
            if _.isEmpty(@data)
                @hide()

        addRow: (id, args...) ->
            if @inData(id)
                return
            self = @
            tmp = """
            <%var lastArg = "#{ args[args.length-1]}"%>
            <%if(lastArg =='pm_list'){%>
            <tr><td>#{ args[0] }</td><td class="pm_td"><span>#{ args[1] }</span></td><td><i class="trash" data-id="#{ id }"></i></td></tr>
            <%}else{%>
            <tr><td>#{ args[0] }</td><td >#{ args[1] }</td><td><i class="trash" data-id="#{ id }"></i></td></tr>
            <%}%>
            """
            $row = @template(tmp,{})
            @$body.append $row
            # save
            @data[id] = args
            @len++
            @$count.html(@len)
            Store.set(@key, @data)
            Store.set(@key_len, @len)
            @show()

        removeRow: (id) ->
            $target = $("i.trash[data-id='#{ id }']", @$el)
            $target.parents('tr').remove()
            @delete(id)

        show: ->
            $('#message_toggle, #message_block', @$el).show()

        hide: ->
            $('#message_toggle, #message_block', @$el).hide()

        toggle: ->
            $('#message_block', @$el).toggle()
            $('#message_toggle', @$el).toggleClass('unfold-message')

        delete: (id) ->
            delete @data[id]
            @len--
            if @len < 0 
              @len = 0  
            @$count.html(@len)
            @load()
            Store.set(@key, @data)
            Store.set(@key_len, @len)

        inData: (id) ->
            _.has(@data, id)

        rebuildCheckbox: ->
            self = @
            if @target
                @target.each ->
                    checked = if self.inData($(this).val()) then true else false
                    $(this).attr('checked',checked)

            @$count.html(@len)

        clear: ->
            Store.remove(@key)
            Store.remove(@key_len)
        

