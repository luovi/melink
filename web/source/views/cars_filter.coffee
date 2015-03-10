define [
    'backbone',
    'store',
    'modal',
    'bmaps',
    'city',
    'cars_filter'
    'text!templates/forms/super_search.html',
], (Backbone, Store ,Modal, Map, city, carsFilter, tmp_adv_search) ->
    'use strict'

    CarsFilterView = Backbone.View.extend
        initialize: (options={},list) ->
            self = @
            @isPublic = options.isPublic
            @pageSize = Store.get('carsPagesize')||20
            @options = data:{page_size:@pageSize}
            @subViews ?= []
            @list = list

            $('#J_advSearch').click ->
                self.showModal.call(self)

        showModal: _.debounce () ->
            self = @
            @modal = new Modal
                title: "高级搜索"
                content: $(@template(tmp_adv_search,{isPublic: @isPublic}))
                i_class:'add'
                cb: ->
                    self.renderCb.call(self)
                button: [
                    value: "确定"
                    class: "btn-small btn-confirm"
                    callback: ->
                        $('#tag_id').val('')
                        $('.selectTag').removeClass('active').eq(0).addClass('active')
                        self.search.call(self)
                        self.modal.destroy()
                ]
        , 800, true
                
        search: _.debounce (event) ->
            self = @
            $('#J_qsearchvalue').val('')
            _.extend @options.data, @_arguments($('#car_filter').serialize()), page_no:1, page_size:@options.data.page_size,q:''
            $.when( @list.getCars(@options) ).then( ->
                self.miniPage self.list.cars
            )
            @query = Backbone.history.fragment.split('?')[1]
            $('#to-public-cars').attr('href',"#public/cars?#{ @query }")
            
        , 800, true

        switchPos: _.debounce (event) ->
            self = @
            tmp_city_select = """
                <div class="modal-city-select" id="modal_city_list">
                    <div class="city-select-body"><div></div></div>
                    <div class="city-select-header">
                        <ul>
                            <li class="hot active">热门城市</li>
                            <% _.each(tabsel, function(city) { %>
                            <li data-char="<%= city[0] %>"><%= city[1] %></li>
                            <% }); %>
                        </ul>
                    </div>
                </div>
                """
            tmp_city_list = """
                <% _.each(citys, function(city) { %>
                <a href="javascript:;"><%= _.isArray(city) ? city[0] : city %></a>
                <% }); %>
                """
            $.when(
                modal = new Modal
                    title: "选择待运货物位置"
                    width: 700
                    content: """<div id="load_map" class="map-api modal-map"></div>"""
                    button: [
                        value: "切换城市"
                        class: "btn-small btn-confirm"
                        style: "float: left;"
                        callback: ->
                            tabsel = _.reject(_.pairs(city.cityTabSel), (i)-> i[0] is '热门城市')
                            $citySelect = $(self.template(tmp_city_select, tabsel:tabsel))
                            # 此函数用于渲染城市列表，并绑定点击事件
                            renderCityList = (citys) ->
                                $('.city-select-body div', $citySelect).html self.template(tmp_city_list, citys:citys)
                                $('.city-select-body div a', $citySelect).on 'click', (event) ->
                                    self.mapInstance?.map.centerAndZoom($(this).text(), 12)
                                    $citySelect.remove()
                            $('.modal', modal.$modal).append($citySelect)
                            # 默认展开热门城市
                            renderCityList city.hotCity
                            # 绑事件
                            $('.city-select-header li', $citySelect).on 'click', (event) ->
                                $(this).siblings().removeClass('active')
                                $(this).addClass('active')
                                if $(this).hasClass('hot')  # 热门城市
                                    citys = city.hotCity
                                else
                                    keys = $(this).data('char').split(',')
                                    citys = _.filter(_.pairs(city.allCity), (i) -> i[1][0][0] in keys)
                                renderCityList citys
                            $('form', modal.$modal).on 'click', -> $citySelect.remove()
                            self.lookTag()

                    ,
                        value: "确定"
                        class: "btn-small btn-confirm"
                        callback: ->
                            label = self.mapInstance?._marker?.getLabel() # 获取标记label
                            if label
                                if $('.tag')
                                    $('.tag').remove()
                                tag = $("""
                                <div class="tag filter-tag">
                                <span>货物位置：</span><a class="pushpin-switch" href="javascript:;"><em>#{ label.content }</em></a>
                                <i class="filter-close"></i>
                                </div>
                                """)
                                $('#cg_location').val(label.content)
                                $('.filter-close', tag).on 'click', ->
                                    self.setElement.call(self, tag)

                                $('.filter-header .tag-group').append tag
                                
                                # 得到位置传给后端查询车辆
                                point = self.mapInstance._marker.getPosition()
                                $('#lng').val(point.lng)
                                $('#lat').val(point.lat)
                                self.lookTag()
                        autoremove: true
                    ]
            ).then(->
                self.mapInstance = new Map("load_map")
                # 打开标记功能
                lon = self.getArguments('lng')
                lat = self.getArguments('lat')
                point = if lon and lat then self.mapInstance.getPoint(lat, lon) else null
                self.mapInstance.openMarkerTool(point)
            )
        , 800, true
     
        renderCb: ->
            self = @
            # init高级搜索插件
            carsFilter()

            @lookTag()
            @cityList1 = city.cityListOn $('.input-location'),
                dataHolder: $('#location')
                icon:true
                multi:true

            @cityList1 = city.cityListOn $('.input-destination'),
                dataHolder: $('#destination')
                icon:true
                multi:true
            
            timeout = setTimeout(->
                $('.filter-close', $('.tag')).on 'click', ->
                    self.setElement.call(self, $(this).parent('.tag'))

                $('#switch_pos').click ->
                    self.switchPos.call(self)
            ,0)

        setElement: (tag) ->
            tag.remove()
            $('#cg_location').val('')
            $('#lng').val('')
            $('#lat').val('')
            $('#switch_pos').show()
            @lookTag()
       
        lookTag: ->
            if _.some( $('.tag-group').children(), (oEl) -> $(oEl).html() )
                $('.J_filterHead').show() 
            else 
                $('.J_filterHead').hide()

        # 回收内存
        remove: ->
            _.invoke(@subViews, 'remove')
            @_super('remove')

