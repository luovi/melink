define [
    'backbone',
    'store',
    'modal'
    'text!templates/modules/crumb.html'
    'text!templates/forms/cargo_add.html'
], (Backbone,Store,Modal,tmp_crumb,tmp_cargo_add) ->
    'use strict'

    cargoAddView = Backbone.View.extend
        initialize: ->
            @render()

        events:
            'submit form': 'submit'
        submit: _.debounce (event) ->
            self = @
            @dirty = false
            event.preventDefault()
            $target = $(event.currentTarget)

            # attr = _.extend @_arguments($target.serialize())
            # @log attr
            tmp_1 = """
                <div class="modal-content">
                    <div class="group clearfix">
                        <div class="group-y">
                            <label class="label">车牌号码：</label>
                            <div class="control-holder">
                                <input type="text" name="" id="" class="text input-slarge">
                            </div>
                        </div>
                       
                    </div>
                    <div class="group clearfix">
                        <div class="group-y">
                            <label class="label">随车手机：</label>
                            <div class="control-holder">
                                <input type="text" name="" id="" class="text input-slarge">
                            </div>
                        </div>
                       
                    </div>
                    <p class="font_hint">若已有车辆承运该单货，请输入车辆车牌号和司机手机号码如：浙A45321 ，13977852256</p>
                </div>
            """
            modal = new Modal
                title: "计划计划"
                width:500
                content: tmp_1
                button: [
                    value: "确定"
                    class: "btn btn-xslarge btn-normal"
                    callback: ->
                       
                    autoremove: true
                ]

        ,800
        render:->
            $container = $('<div class="container"></div>')
            user = Store.get('current_user').username
            $container.append(@template(tmp_crumb, {urls:[{url:'#',name:'货源信息'}],current:'发布货源'}))
            $container.append(@template(tmp_cargo_add, {user:user}))
            @$el.html($container)
        
        remove: ->
            @_super('remove')


