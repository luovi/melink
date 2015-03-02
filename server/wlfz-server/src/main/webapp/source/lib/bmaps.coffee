define ['jquery', 'underscore', 'bmapapi'], ($, _, bmapapi) ->
    'use strict'

    Ak = 'EF6aee05088be9901b4b11fcab5da1d1' # 设置百度地址ak值, bmapapi也使用此值在main里
    CUR_IMG = "/static/images/transparent.cur" # 鼠标透明样式

    class Convert
        ### 将gps坐标转换为百度坐标
        http://developer.baidu.com/map/changeposition.htm
        ###
        constructor: ->
            @urlRoot = 'http://api.map.baidu.com/geoconv/v1/'
            @options =
                from: 1
                to: 5
                output: 'jsonp'
                #callback: 'convert'
                ak: Ak

        fetch: (cols) ->
            defer = $.Deferred()
            return defer.reject() unless cols.length
            coords = []
            if cols.models
                for i in cols.models
                    coords.push "#{ i.get('longitude') },#{ i.get('latitude') }"
                    _.extend @options, coords:coords.join(';')

            else
                for i in cols
                    coords.push "#{ i.longitude },#{ i.latitude }"
                    _.extend @options, coords:coords.join(';')

            $.ajax
                url: @urlRoot
                data: @options
                dataType: 'jsonp'
                success: (data,success,xhr) ->
                    if data.status is 0
                        if cols.models
                            for i,index in cols.models
                                i.set('x', data.result[index].x)
                                i.set('y', data.result[index].y)
                        else
                            for i,index in cols
                                i.x = data.result[index].x
                                i.y = data.result[index].y

                        defer.resolve(cols)
                    else
                        defer.reject()
                error: ->
                    defer.reject()
            defer.promise()

    class FollowMarker extends BMap.Marker
        constructor: (point, opt) ->
            super point, opt or {}
            @_lab = new BMap.Label('左击添加标注', {offset: new BMap.Size(20, 0)})
            @setLabel @_lab
            @setZIndex 1000
            @hide()

    class Map
        constructor: (element) ->
            @map = new BMap.Map(element)
            @map.centerAndZoom(new BMap.Point(106.346, 33.866), 5) # 初始化地图
            @map.addControl(new BMap.NavigationControl()) # 导航控制
            @map.enableScrollWheelZoom() # 启用滚轮放大缩小，默认禁用
            @map.enableContinuousZoom() # 启用连续缩放效果,默认禁用
            @map.addEventListener 'tilesloaded', @removeCopy() # 事件绑定
            @geocoder = new BMap.Geocoder
            @_markers = []
            @_points = []

        removeCopy: ->
            self = @
            ->
                $('.BMap_cpyCtrl', self.map.getContainer()).remove()

        getIcon: (style) ->
            # style: origin|dest|orbit|cur
            url = '/static/v/v1/images/ico-location-4.png'  # 图片地址
            offset =
                origin: [0, 0]
                dest  : [-30, 0]
                orbit : [-60, 0]
                cur   : [-90, 0]
            [l,t] = offset[style]
            new BMap.Icon url, new BMap.Size(30, 37),
                anchor: new BMap.Size(16, 36)
                imageOffset: new BMap.Size(l,t)

        getLabel: (index) ->
            if index <= 9
                offset = [11, 5]
            else if index >= 100
                offset = [4, 5]
            else
                offset = [8, 5]
            content = "<b>#{ index }</b>"
            label = new BMap.Label content,
                offset: new BMap.Size(offset[0], offset[1])
            label.setStyle
                border: 'none'
                fontSize: '12px'
                backgroundColor: 'transparent'
                color: '#fff'
            label

        getInfoWindow: (name, time, location) ->
            content = """
            <b class='iw_poi_title'> #{ name } </b>
            <div class='iw_poi_content'> #{ time } 到达 #{ location } </div>
            """
            new BMap.InfoWindow content,
                offset: new BMap.Size(0, -35)

        showOrbits: (cols, title) ->
            self = @
            convert = new Convert
            # 先处理坐标转换再进行渲染图标
            convert.fetch(cols).then((cols)->
                self.render(cols, title)
            )

        render: (cols, title) ->
            if cols.models
                for model, i in cols.models
                    index = cols.models.length - i
                    [marker,point] = @addOrbit(model, title, index, cols.models.length)
                    @map.addOverlay(marker) # 地图上加载图标
                    @_points.push point
                    @_markers.push marker # 图标保存到全局列表
            else
                for model, i in cols
                    index = cols.length - i
                    [marker,point] = @addOrbit(model, title, index, cols.length)
                    @map.addOverlay(marker) # 地图上加载图标
                    @_points.push point
                    @_markers.push marker # 图标保存到全局列表

            @setCenter()

        setCenter: ->
            # 以坐标点调整地址视野，调整后的视野会保证包含提供的地理区域或坐标
            @map.setViewport(@_points)
            @map.setCenter(_.first(@_points)) # 设置地图中心点
            @map.openInfoWindow(_.first(@_markers).infowindow, _.first(@_points))
            @map.setZoom(13) # 调整视野

        resetIndex: ->
            @setZIndex @zindex

        showInfoEvent: ->
            @openInfoWindow(@infowindow)

        showInfoByOrbit: (index) ->
            marker = @_markers[index]
            marker.openInfoWindow(marker.infowindow)
            marker.setZIndex(marker.zindex + 200)
            point = marker.getPosition()
            @map.setCenter(point)
            @map.setZoom(13)

        addOrbit: (model, title, index, length, style) ->
            if model.longitude and model.latitude
                [x,y] = [model.longitude,model.latitude]
            else if model.get('x') and model.get('y')
                [x,y] = [model.get('x'),model.get('y')]
            else
                [x,y] = [model.get('longitude'),model.get('latitude')]
            point = new BMap.Point(x, y)
            # 创建图标
            if model.locate_time and model.location
                infowindow = @getInfoWindow(title, model.locate_time, model.location)
            else
                infowindow = @getInfoWindow(title, model.get('locate_time'), model.get('location'))
            icon = if style then @getIcon(style) else @getIcon(if index < length then 'orbit' else 'cur')
            label = if index < length then @getLabel(index) else null
            marker = new BMap.Marker(point)
            marker.enableMassClear() # 允许覆盖物在map.clearOverlays方法中被清除
            if label # 设置图标上文字
                marker.setLabel label
            marker.setIcon icon
            marker.infowindow = infowindow
            marker.zindex = index
            marker.setZIndex(index)
            # 监听图标点击事件
            marker.addEventListener 'click', @showInfoEvent
            marker.addEventListener 'infowindowclose', @resetIndex
            [marker, point]

        prependOrbit: (model, title) ->
            # 之前当前图标更新成普通图标
            first_marker = _.first(@_markers)
            if first_marker
                first_marker.setIcon(@getIcon('orbit'))
            # 生成最新图标，并插入到最前面
            [marker,point] = @addOrbit(model, title, @_markers.length+1, 'cur')
            @_points.unshift point
            @_markers.unshift marker

        appendOrbits: (newcols, title) ->
            @map.clearOverlays() # 不先清除，会叠加label和icon
            # 重新计算并生成原markers的label和icon
            for marker, i in @_markers
                length = newcols.models.length + @_markers.length
                index =  length - i
                label = if index < length then @getLabel(index) else null
                icon = @getIcon(if index < length then 'orbit' else 'cur')
                marker.setIcon icon
                marker.setLabel label
                marker.zindex = index
                marker.setZIndex(index)
                @map.addOverlay(marker) # 地图上加载图标
            # 加载新数据
            @showOrbits(newcols, title)

        # 打开标记坐标获取位置功能
        openMarkerTool: (city) ->
            self = @
            if not @_marker
                @_marker = new BMap.Marker(@map.getCenter())
                @_marker.hide()
                @map.addOverlay @_marker
            # 跟踪图标初始化
            if not @_followMarker
                @_followMarker = new FollowMarker(@map.getCenter(), { offset: new BMap.Size(-10, -10) })
                @map.addOverlay @_followMarker
                @map.setDefaultCursor("url(#{ CUR_IMG }), default") # 设置鼠标样式

            if city
                if typeof(city) is 'object'
                    point = city
                @map.centerAndZoom(city, 12)
            else
                # 不指定城市则通过ip获取当前城市
                myCity = new BMap.LocalCity()
                myCity.get (result) ->
                    self.map.centerAndZoom(result.name, 12)
            
            # 鼠标跟随事件
            @map.addEventListener 'mousemove', (event) ->
                self._followMarker.setPosition(event.point)
                self._followMarker.show()
            
            # 鼠标点击事件
            @map.addEventListener 'click', (event) ->
                self.getLocation event.point, (result) ->
                    evtPix = event.pixel
                    iconPix = new BMap.Pixel(evtPix.x - 10, evtPix.y - 10)  # 补偿_followMarker的-10像素问题,解决cursor问题
                    point = self.map.pixelToPoint(iconPix)
                    self.addEventPoint(point, result.address)

            if point
                self.getLocation point, (result) ->
                    self.addEventPoint(point, result.address)

        # 给指定point，加上marker(红点)和内容
        addEventPoint: (point, text) ->
            self = @
            self._marker.setPosition(point)
            label = self._marker.getLabel()
            if not label
                label = new BMap.Label(text, {offset: new BMap.Size(20, 0)})
                self._marker.setLabel(label)
            else
                label.setContent(text)
            self._marker.show()

        # 通过坐标获取地址, 结果通过执行callback返回
        getLocation: (point, callback) ->
            @geocoder.getLocation(point, callback)

        addCityPoint: (city, style, index) ->
            self = @
            @geocoder.getPoint city, (point) ->
                # 创建图标
                icon = self.getIcon(style)
                marker = new BMap.Marker(point)
                marker.enableMassClear() # 允许覆盖物在map.clearOverlays方法中被清除
                marker.setIcon icon
                marker.zindex = index
                marker.setZIndex(index)
                self.map.addOverlay(marker) # 地图上加载图标
                # 监听图标点击事件
                #marker.addEventListener 'click', self.showInfoEvent
                #marker.addEventListener 'infowindowclose', self.resetIndex
                #
        getPoint: (lat, lon) ->
            new BMap.Point(lon, lat)

