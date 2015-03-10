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
            @dataId = @arrData() or []
            @render()

        events:
            'click #message_toggle': 'toggle'
            'click i.trash': 'delRow'
            'click a.send_msg': 'sendSMS'
            'click a.send_weixin': 'sendWX'
            'click a.edit_tag': 'editTag'
            'click a.add_notify': 'addNotify'
            'click a.store_clear':'clearAll'

        arrData:->
            _.map @data,(con,key)->
                return key
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
        
        editTag: (event) ->

            self = @
            $content = $(@template("""

                <div id="form_edit_tag" class="modal-content form-y">
                    <% var len = _.size(data) %>
                    <div class="group">
                        <%if(options.key == 'pm_list'){%>
                            <label class="label type" data-type="<%=options.key%>">所选用户：</label>
                            <% if(len>=6){ %>
                            <div class="tag-select">当前已选择 <span class="select-number"><%= _.size(data) %></span> 个用户
                                <span class="tag-unfold tag-toggle">
                                        <a href="javascript:void(0);">详情<i class="drop-down-gray"></i></a>
                                </span>
                            </div>
                            <% }; %>

                        <%}else{%>
                            <label class="label">所选车辆：</label>
                            <% if(len>=6){ %>
                            <div class="tag-select">当前已选择 <span class="select-number"><%= _.size(data) %></span> 辆车
                                <span class="tag-unfold tag-toggle">
                                        <a href="javascript:void(0);">详情<i class="drop-down-gray"></i></a>
                                </span>
                            </div>
                            <% }; %>

                        <%}%>
                        <div class="controls select-cars <% if(len>=6){ %>hide<% }; %>">

                        </div>
                    </div>
                    <div class="label-group group clearfix" style="margin-left:91px;"></div>
                    <div class="group clearfix">
                        <label class="label">标签：</label>
                        <div class="controls">
                            <input type="hidden" class="input-xlarge" name="tags" />
                            <div class="tags-suggest"></div>
                        </div>
                    </div>
                </div>""", data:@data,options:@options))

            $tmp = $(@template("""
                <% _.each(_.pairs(data), function(d){ %>
                <div class="filter-tag"><span><%= d[1][0] %>：</span><em><%= d[1][1] %></em><i class="filter-close" data-id="<%= d[0] %>"></i></div>
                <% }); %>

            """, data:@data))
            
            timeout = setTimeout(->
                $('.select-cars',$content).html $tmp
            ,0)

            tagging = new Tagging $('input[name=tags]', $content),
                placeholder:'请输入标签, 空格或逗号结束'
                inputZoneClass:'input-xlarge mr5 pull-left'
                tagwrap: $('.label-group', $content)
                tagClass:'filter-tag car-label'
                closeClass:'filter-close'
            tagging.reload()
            modal = new Modal
                title: "编辑标签"
                width: 700
                content: $content
                button: [
                    value: "确定"
                    class: "btn-small btn-confirm"
                    callback: (data) ->
                        tags = _.reject(self._arguments(data)['tags'].split(','), (n)->
                            $.trim(n) is ''
                        )
                        cids = _.map self.data, (n,k)-> k
                        if _.isEmpty(tags) or _.isEmpty(cids)
                            return
                        if $('.type').attr('data-type')=='pm_list'
                            # '用户标签'
                            $.ajax
                                url: "/api/partners/#{ self._current_user().id }/tags"

                                type: 'POST'
                                data: $.param
                                    tag: tags
                                    uid:cids
                                , true
                                dataType: 'json'
                                success: ->
                                    msg = "<p>用户批量新增标签成功</p>"
                                    self.notify().show(msg, 'success')
                                error: ->
                                    msg = "<p>用户批量新增标签失败，请稍后重试!</p>"
                                    self.notify().show(msg, 'error')
                                beforeSend: (xhr) ->
                                    xhr.setRequestHeader('Authorization', self._current_user().token)
                        else
                            $.ajax
                                url: "/api/cars/#{ self._current_user().id }/tags"
                                type: 'POST'
                                data: $.param
                                    tag: tags
                                    cid: cids
                                , true
                                dataType: 'json'
                                success: ->
                                    msg = "<p>车辆批量新增标签成功</p>"
                                    self.notify().show(msg, 'success')
                                error: ->
                                    msg = "<p>车辆批量新增标签失败，请稍后重试!</p>"
                                    self.notify().show(msg, 'error')
                                beforeSend: (xhr) ->
                                    xhr.setRequestHeader('Authorization', self._current_user().token)
                    autoremove: true
                ]

            if _.size(@data)<6
                setTimeout ->
                    $('.filter-close', $content).on 'click', bindClose
                ,0
                                
            $('.tag-toggle', $content).on 'click', ->
                if $('.select-cars',$content).hasClass('hide')
                    $('.select-cars',$content).removeClass('hide')
                    $('.filter-close', $content).on 'click', bindClose
                    $('.tag-toggle a', $content).html '收起<i class="drop-down-gray"></i>'
                else
                    $('.select-cars').addClass('hide')
                    $('.tag-toggle a', $content).html '详情<i class="drop-down-gray"></i>'

            bindClose = (event)=>
                self = @
                $target = $(event.target)
                id = $target.data('id')
                $target.parent().remove()
                self.removeRow(id)
                $('.select-number',$content).text _.size(self.data)
                self.rebuildCheckbox()
                if _.isEmpty(self.data)
                    modal.release()
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
            $('.pm_length').html("当前选择:#{ @len }用户")
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
            $('.pm_length').html("当前选择:#{ @len }用户")
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

        clear: ->
            Store.remove(@key)
            Store.remove(@key_len)
        

