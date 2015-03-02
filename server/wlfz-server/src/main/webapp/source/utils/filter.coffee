define ['underscore', 'common'], (_, common) ->
    'use strict'

    formatDate: (date, newvalue='') ->
        if _.isNaN(date) or _.isNull(date) or date is ''
            return newvalue
        date = common.parseDate(date)
        if not date
            return newvalue
        now = new Date()
        year = now.getFullYear() - date.getFullYear()
        month = now.getMonth() - date.getMonth()
        seconds = Math.round((now - date)/1000)
        d = Math.floor(seconds / (3600 * 24))
        h = Math.floor(seconds / 3600)
        if year > 1 or (year==1 and month > 0)
            return "#{ year }年前"
        else if (year is 1 and month < 0) or (month > 0 and d >30)
            m = if month < 0 then (12 + month) else month
            return "#{ m }个月前"
        else if d > 0
            return "#{ d }天前"
        else if h > 0
            return "#{ h }小时前"
        else
            s = Math.floor(seconds/60)
            if s > 1
                return "#{ s }分钟前"
            else
                return "刚刚"

    formatCity: (city) ->
        if city.slice(-1) in ['市','省','盟']
            return city.slice(0,-1)
        else if city.slice(-3) in ['自治区','直辖市']
            return city.slice(0,-3)

