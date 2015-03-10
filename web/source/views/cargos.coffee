define [
    'backbone',
    'store',
    'dropMenu'
    'modal'
    '../collections/cargo'
    'text!templates/modules/crumb.html'
    'text!templates/pages/cargos.html'
    'text!templates/lists/cargos.html'
], (Backbone, Store, DropMenu, Modal, CargosCtrl, tmp_crumb, tmp_cargos, tmp_cargos_list) ->
    'use strict'
           
    IndexView = Backbone.View.extend
        initialize: ->
            @subViews ?= []
            @options = 
                data:{page_no:1}
            @cargos = new CargosCtrl
            @cargos.fetch @fetchOptions(@options, true)
            @render()

            @listenTo @cargos, 'sync', @renderList

        events:
            'click .nav-tabs li a': 'changeList'
            'submit form': 'search'
            'click .select_tab_btn': 'toggleList'

        toggleList: (event)->
            $('.nav-tabs').toggle()

        search: _.debounce (event) ->
            $target = $(event.target)
            _.extend @options.data, q:@$keyword.val()
            @cargos.fetch @fetchOptions(@options, true)
        , 800, true

        changeList: _.debounce (event) ->
            $target = $(event.target)
            _.extend @options.data, status:$target.data('status')
            $('.select_tab_btn',  $target.parents('.select_tab')).val($target.html())
            $target.parents('.nav-tabs').hide()
            @cargos.fetch @fetchOptions(@options, true)
        , 800, true

        render: ->
            $container = $('<div class="container"></div>')
            $container
                .append(@template(tmp_crumb, {urls:[],current:''}))
                .append(@template(tmp_cargos, {}))
            @$el.html($container)
            @$('.J_cargo_list').append @spinner().el
            @$keyword = @$('#J_qsearchvalue', @$el)

            $(document).bind "click", (e)->
                target  = $(e.target)
                if target.closest(".select_tab_btn").length == 0 
                    $('.nav-tabs').hide()
            
        renderList: ->
            @$('.J_cargo_list').empty().html @template(tmp_cargos_list, {cargos:@cargos})
            @dropMenu = new DropMenu(el: @$('.J_cargo_list').children(), name: 'table', target: '.drop-down-link')
            @createPage @cargos, {target: $('.J_cargos_box')}
            
        remove: ->
            @_super('remove')
