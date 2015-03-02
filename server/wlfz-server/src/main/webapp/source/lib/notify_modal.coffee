'use strict'
define [
    'backbone'
    '/component/models/track_shorten.js'
    '/component/models/sms.js'
    '/component/collections/contacts.js'
    'modal'
    'text!templates/modules/cargo_notify_modal.html'
    'popover'
], (Backbone, shortenModel, smsModel, contModel, Modal, tmp_modal, popover) ->
    tmp_contacts = """
        <ul class="ui-autocomplete ui-menu ui-widget ui-widget-content ui-corner-all active" style="z-index: 1999; position: relative; display: none; width: 190px;">
        </ul>
    """

    tmp_contacts_li = """
        <% _.each(models, function(model) { %>
        <li class="ui-menu-item" data-number="<%= model.get('contact_number') %>">
            <a class="ui-corner-all" href="javascript:;">
                <span class="value"><%= (model.get('company') || {}).company_name || model.get('contact_number') %></span><br>
                <span class="desc"><%= model.get('contact_person') %></span><br>
            </a>
        </li>
        <% }); %>
        """
    
    
    CargoNotify = Backbone.View.extend
        initialize: (opts)->
            self = @
            @cost = 0
            @cid = opts.cid
            @cname = opts.cname
            @model = opts.model
            @user_id = @_current_user().id
            @contacts = new contModel
            @contacts.fetch @fetchOptions()
            @shorten = new shortenModel
            @shorten.set()
            data =
                user_id: @user_id
            if @model == 'car'
                data['car_id'] = @cid
            else
                data['cargo_id'] = @cid
            ajaxOpts =
                data: data,
                processData: true,
                success: ->
                    self.modal = new Modal
                        title: '授权跟踪'
                        width: 600
                        content: self.template(tmp_modal, {shorten: self.shorten})
                        button: [
                            value: "确定"
                            class: "btn-small btn-confirm"
                            callback: (data) ->
                                data = self._arguments(data)
                                self.settrack(data)
                            autoremove: true
                        ]
                    #联系人异步完成绑定
                    self.listenTo self.contacts, 'sync', self.bindContact(self.modal)
            
            @shorten.fetch _.extend ajaxOpts, @fetchOptions()
            
        price: (modal) ->
            self = @
            locatePrice = 0.2
            smsPrice = 0.1
            $trackTimes = $('#track-times',  modal.$modal)
            $droplist = $('.droplist',  modal.$modal)
            _length = 0
            
            __setcost = (method)->
                _.map($droplist, (input) ->
                    _length = _.filter($droplist, (input) ->
                        $(input).val()
                    ).length
                    if method == 'plus'
                        self.cost = $trackTimes.val() * locatePrice + smsPrice * _length
                    if method == 'minus'
                        self.cost -= smsPrice * _length
                    $('.fee-total .error').text(_.str.toNumber(self.cost, 1))
                )
            
            $('input.checkbox').on 'change', (e) ->
                if $(e.currentTarget).is(':checked')
                    __setcost('plus')
                else
                    if _.str.toNumber($trackTimes.val() * locatePrice, 1) != _.str.toNumber($('.fee-total .error').text(), 1)
                        $('.fee-total .error').text(_.str.toNumber($trackTimes.val() * locatePrice ,1))
                    else
                        __setcost('minus')
            
            
            _.map($droplist, (input) ->
                $(input).change ->
                    _length = _.filter($droplist, (input) ->
                        $(input).val()
                    ).length
                    if $('input.checkbox').is(':checked')
                        self.cost = $trackTimes.val() * locatePrice + smsPrice * _length
                        $('.fee-total .error').text(_.str.toNumber(self.cost, 1))
            )
            $trackTimes.on 'keyup', ->
                if $('input.checkbox').is(':checked')
                    self.cost = $trackTimes.val() * locatePrice + smsPrice * _length
                else
                    self.cost = $trackTimes.val() * locatePrice
                $('.fee-total .error').text(_.str.toNumber(self.cost, 1))
            
            
        settrack: (data)->
            self = @
            shorten = new shortenModel
            if @model == 'car'
                shorten.set({
                'tracking_times': data.tracking_times,
                'car_id': @cid,
                'user_id': @user_id
                })
            else
                shorten.set({
                    'tracking_times': data.tracking_times,
                    'cargo_id': @cid,
                    'user_id': @user_id
                    })
            shorten.save {}, @fetchOptions()
            if data.enabled_sms
                @sms = new smsModel({
                    company: self._current_user().company_name
                    model: self.cname
                    msg_code: 'SMS_TRACKING'
                    short_url: self.shorten.get('track_url')
                    })
                
                self.sms.set({
                    'numbers': _.compact(data.phones),
                    'msg_code':'SMS_TRACKING'
                    })
                self.sms.save {}, _.extend self.fetchOptions(), {
                    data: $.param(self.sms.attributes, true)
                    success: () ->
                        msg = "#{ self.cname } 授权跟踪设置成功！"
                        self.notify().show(msg, 'success')
                        ###
                        if self.model == 'car'
                            self.redirect("#cars")
                        else
                            self.redirect("#cargos")
                        ###
                }
            else 
                msg = "#{ self.cname } 授权跟踪设置成功！"
                self.notify().show(msg, 'success')
                ###
                if self.model == 'car'
                    self.redirect("#cars")
                else
                    self.redirect("#cargos")
                ###

        
        bindContact: (modal) ->
            self = @
            @$contact_droplist ?= $(@template(tmp_contacts, contacts:@contacts))
            @$contact_droplist.appendTo('.contacts_list')
            # 绑定计费
            self.price(modal)
            
            _.each $('.droplist', modal.$modal), (el) ->
                $(el).on 'keyup', ->
                    $this = $(this)
                    leftc = $(el).attr('leftc')
                    # 检索结果
                    val = $this.val()
                    if val
                        data = _.filter self.contacts.models, (model)->
                            model.get('contact_number').indexOf(val) is 0
                        self.loadContact($this, data,leftc)
                    else
                        self.$contact_droplist.hide('fast')
                $(document).on 'click', (event) ->
                    event.stopPropagation()
                    self.$contact_droplist.hide('fast')
            _.each $('i.book', modal.$modal), (i) ->
                $(i).on 'click', (event) ->
                    leftc = $(this).prev().attr('leftc')
                    event.stopPropagation()
                    self.loadContact($(this).prev(), self.contacts.models,leftc)
            
            self.bindPop(modal)
                    
        
        bindPop: (modal) ->
            self = @
            $('.fee-total', modal.$modal).popover 
                trigger: 'hover'
                html: true
                # selector: '.fee-total'
                placement: 'top'
                content: "<div style='width:160px'><p><label>短信费：</label><span>0.1 元 / 条</span></p><p><label>定位费：</label><span>0.2 元 / 次</span></p><p>短信、定位失败不收费.</p><p>优先使用套餐优惠.</p></div>"
        
        loadContact: (target, data,lf) ->
            self = @
            # 填充结果显示
            @$contact_droplist.html @template(tmp_contacts_li, models:data)
            $('li', @$contact_droplist).on 'click', ->
                target.val($(this).data('number')).change()
                self.$contact_droplist.hide('fast')
            @$contact_droplist.show()
            # 重置位置
            setLeft = parseInt(lf)
            @$contact_droplist.css
                top: 2
                left: setLeft
        
        removeContact: ->
            @$contact_droplist.detach()
        
