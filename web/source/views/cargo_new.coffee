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
            modal = new Modal
                title: "确定删除联系人"
                content: "<div ><p>您确定要删除联系人 吗？</p></div>"
                button: [
                    value: "确定"
                    class: "btn-small btn-confirm"
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


