define [
    'backbone',
    'store',
    'modal'
    '../collections/cars'
    'views/cars_filter'
    'views/side_store'
    'text!templates/modules/crumb.html'
    'text!templates/pages/cargo_push.html'
    'text!templates/lists/car_list.html'
], (Backbone, Store, Modal, CarCtrl, carsFilter, SideStoreView, tmp_crumb, tmp_push, tmp_car_list) ->
    'use strict'
           
    IndexView = Backbone.View.extend
        initialize: ->
            # Store.set('myLen',0)
            @key = 'mylist'
            @key_len = 'myLen'
            @len = Store.get(@key_len) or 0
            @data = Store.get(@key) or {}


            @subViews ?= []
            @options = 
                data:{page_no:1}
            @cars = new CarCtrl
            @cars.fetch @fetchOptions(@options, true)
            @$el.append @spinner().el
            @render()

            @listenTo @cars, 'sync', @renderList

        events:
            'submit form': 'search'
            'click tbody input:checkbox': 'toggleStore'
            'click thead input:checkbox': 'datchStore'
            'click .striped tbody tr': 'toChecked'

        toChecked: (event)->
            $target = $(event.target)
            $tr = $target.parents('tr')
            $checkbox = $('.datch', $tr)

            if $target.hasClass('datch') 
                return  

            if !$checkbox.is(":checked")
                $checkbox.attr('checked','true')
            else
                $checkbox.removeAttr('checked')

            @toggleStore(false, $checkbox)

        toggleStore: (event, oTarget) ->
            self = @
            $target =if event then $(event.target) else oTarget
            if $target.is(":checked")
                @side_store.addRow($target.val(), $target.data('title'), $target.data('name'))
            else
                @side_store.removeRow($target.val())

            @allIsChecked()

        datchStore:(event) ->
            self = @
            $target = $(event.target)
            if $target.is(":checked")
                @$('tbody input:checkbox').attr('checked','true')
                @$('tbody input:checkbox').each (index,item)->
                    target = $(item)
                    self.side_store.addRow(target.val(), target.data('title'), target.data('name'))
            else
                @$('tbody input:checkbox').removeAttr('checked')
                @$('tbody input:checkbox').each (index,item)->
                    self.side_store.removeRow($(item).val())

        allIsChecked:->
            if @IsChecked()
                @$('thead input:checkbox').attr('checked','true') 
            else
                @$('thead input:checkbox').removeAttr('checked')
        IsChecked:->
            IsChecked = true

            @$('tbody input:checkbox').each (index,item)->
                if !$(item).attr('checked')
                    IsChecked = false
            return IsChecked

        render: ->
            self = @
            $container = $('<div class="container"></div>')
            $container
                .append(@template(tmp_crumb, {urls:[],current:'推送货源'}))
                .append(@template(tmp_push, {}))
            @$el.html($container)
            @$keyword = @$('#J_qsearchvalue', @$el)
            setTimeout ->
                self.carsfilter = new carsFilter self.options, self.cars
            ,0
            
        renderList: ->
            $('.J_car_list').empty().append @template(tmp_car_list, {cars:@cars})
            @createPage @cars, {target: $('.J_cars_box')}

            @side_store = new SideStoreView
                $count:$('#J_selected')
                key:'mylist'
                target:$('input[type=checkbox]', @$el)
            @subViews.push(@side_store)

            @$el.append(@side_store.el)

        remove: ->
            _.invoke(@subViews, 'remove')
            @_super('remove')
